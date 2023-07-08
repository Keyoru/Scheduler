import java.util.LinkedList;
import java.util.UUID;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;


public class courseScheduler {

    int days = 5; 
    int timeslots = 6;

    LinkedList<UUID>[][] schedule = new LinkedList[days][timeslots];
    Map<UUID, course> courseMap;

    private final List<List<Integer>> dayPairs = new ArrayList<>();


    
    //for courses with no place found to store them
    LinkedList<course> unscheduledCourseHeap = new LinkedList<>();

    FileWriter fileWriter;
    PrintWriter printWriter; 
    File logFile;

    courseScheduler() {

        try {

            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM_dd_HH:mm");
            String timestamp = currentTime.format(formatter);
    
            String fileName = "log_" + timestamp + ".txt";
            logFile = new File(fileName);
    

            fileWriter = new FileWriter(logFile, true); // 'true' for appending to an existing file
            printWriter = new PrintWriter(fileWriter);
            courseMap = new HashMap<>();

            // Initialize day pairs (M,W) and (T,Th)
            dayPairs.add(List.of(0, 2)); // Monday, Wednesday
            dayPairs.add(List.of(1, 3)); // Tuesday, Thursday
            dayPairs.add(List.of(2, 4)); // Wednesday, Friday

            for(int i = 0;i < days;i++){
                for(int j = 0; j < timeslots; j++){
                    schedule[i][j] = new LinkedList<UUID>();
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the log file: " + e.getMessage());
        }
    }

    public void ScheduleCourses() {
        Set<UUID> courseIds = new HashSet<>(courseMap.keySet()); // without it this doesnt work idk why
        for (UUID courseId : courseIds) {
            addCourse(courseId);
        }
    }


    //calls the other helper method depending on requirements 
    //currently handles only section logic
    public void addCourse(UUID courseId) {

        // logic for sections here
        // if more than 1 section for a course
        // add -1 -2 -3 etc... to course ID  

        // this section adds the course's own code to the its conflict list
        // this helps with different sections                 
        String[] courseNameSplit = courseMap.get(courseId).courseID.split("-");
        String courseID = courseNameSplit[0];
    
        if (!courseMap.get(courseId).conflictingCourses.contains(courseID)) {
            courseMap.get(courseId).conflictingCourses.add(courseID);
        }

        //System.out.println(courseMap.get(courseId).courseID);
        //System.out.println(courseMap.get(courseId).conflictingCourses.toString());

        course currentSection = null;
        if(courseMap.get(courseId).numberOfSections > 1){
            for(int i = 1; i <= courseMap.get(courseId).numberOfSections;i++){
                
                //currentSection same exact attributes as course but with courseID += "-"+i
               currentSection = new course(courseMap.get(courseId).courseID+"-"+i,courseMap.get(courseId).courseName,
               courseMap.get(courseId).numberOfCredits,courseMap.get(courseId).numberOfSections,
               courseMap.get(courseId).numberOfSessions,courseMap.get(courseId).instructorName,
               courseMap.get(courseId).instructorDays,courseMap.get(courseId).TimeSlotIndexstart, courseMap.get(courseId).TimeSlotIndexEnd,courseMap.get(courseId).conflictingCourses,
               courseMap.get(courseId).courseType,courseMap.get(courseId).nbOfSlots);

                
                UUID courseSectionId = UUID.randomUUID();
                courseMap.put(courseSectionId, currentSection);
                
                if (attemptDayPairSchedule(courseSectionId)) {
                    System.out.println("pairs");
                    return;
                }
    
                if (attemptEqualSpreadSchedule(courseSectionId)) {
                    System.out.println("equal spread");
                    return;
                }
            
                if (attemptAnySchedule(courseSectionId)) {
                    System.out.println("any");
                    return;
                }

                // Course couldn't be scheduled
                unscheduledCourseHeap.add(courseMap.get(courseId));
            }    
        }else{
            printWriter.println("Adding course " + courseMap.get(courseId).courseID);

            if (attemptDayPairSchedule(courseId)) {
                System.out.println("pairs true");
                return;
            }

            if (attemptEqualSpreadSchedule(courseId)) {
                System.out.println("equal spread true");
                return;
            }
        
            if (attemptAnySchedule(courseId)) {
                System.out.println("any true");
                return;
            }

            unscheduledCourseHeap.add(courseMap.get(courseId)); 
        }
    }



    // TODO different lectures on different day pairs or times if possible
    private boolean attemptDayPairSchedule(UUID courseId) {
        course course = courseMap.get(courseId);
    
        for (List<Integer> dayPair : dayPairs) {
            if (canWorkWith(dayPair, courseMap.get(courseId).instructorDays)) {
                int pairSessions = course.numberOfSessions / 2;
                int sessionsScheduled = 0;
    
                int dayIndex1 = dayPair.get(0);
                int dayIndex2 = dayPair.get(1);
                int timeSlotIndex = course.TimeSlotIndexstart;
    
                while (sessionsScheduled < pairSessions && timeSlotIndex <= course.TimeSlotIndexEnd - course.nbOfSlots) {
                    boolean canSchedule = true;
    
                    for (int session = 0; session < course.nbOfSlots && sessionsScheduled < pairSessions; session++) {
                        for (int slot = 0; slot < course.nbOfSlots; slot++) {
                            if (!isSlotAvailable(courseId, dayIndex1, timeSlotIndex + slot) ||
                                    !isSlotAvailable(courseId, dayIndex2, timeSlotIndex + slot)) {
                                canSchedule = false;
                                break;
                            }
                        }
    
                        if (canSchedule) {
                            for (int slot = 0; slot < course.nbOfSlots; slot++) {
                                scheduleCourseInSlot(courseId, dayIndex1, timeSlotIndex + slot);
                                scheduleCourseInSlot(courseId, dayIndex2, timeSlotIndex + slot);
                            }
                            sessionsScheduled++;
                        } else {
                            break;
                        }
                    }
    
                    timeSlotIndex++;
                }
    
                System.out.println(sessionsScheduled);
    
                if (sessionsScheduled >= pairSessions) {
                    return true;
                } else {
                    System.out.println("pair false");
                }
            }
        }
    
        return false;
    }
    
    
    private boolean attemptEqualSpreadSchedule(UUID courseId) {
        
        int sessionsPerDay = courseMap.get(courseId).numberOfSessions / courseMap.get(courseId).instructorDays.size();
        System.out.println("sessions per day " + sessionsPerDay);
        int sessionsScheduled = 0;

        for (int i = courseMap.get(courseId).TimeSlotIndexstart; i < courseMap.get(courseId).TimeSlotIndexEnd && sessionsScheduled <= sessionsPerDay; i++) {
            for (int dayIndex: courseMap.get(courseId).instructorDays) {
                if (isSlotAvailable(courseId, dayIndex, i)) {
                    if (courseMap.get(courseId).nbOfSlots > 1 && areSlotsAvailable(courseId, dayIndex, i, i + courseMap.get(courseId).nbOfSlots - 1)) {
                        scheduleCourseInSlots(courseId, dayIndex, i, i + courseMap.get(courseId).nbOfSlots - 1);
                        sessionsScheduled++;
                    } else if (courseMap.get(courseId).nbOfSlots == 1) {
                        scheduleCourseInSlot(courseId, dayIndex, i);
                        sessionsScheduled++;
                    }
                }
            }
        }

        System.out.println(sessionsScheduled);

        if (sessionsScheduled == courseMap.get(courseId).numberOfSessions) {
            return true;
        }else{
            System.out.println("equal spread false");
        }

        return false;
    }


    private boolean attemptAnySchedule(UUID courseId) {
        int sessionsScheduled = 0;
    
        for (int dayIndex: courseMap.get(courseId).instructorDays) {
            for (int i = courseMap.get(courseId).TimeSlotIndexstart; i < courseMap.get(courseId).TimeSlotIndexEnd && sessionsScheduled < courseMap.get(courseId).numberOfSessions; i++) {
                if (isSlotAvailable(courseId, dayIndex, i)) {
                    if (courseMap.get(courseId).nbOfSlots > 1 && areSlotsAvailable(courseId, dayIndex, i, i + courseMap.get(courseId).nbOfSlots - 1)) {
                        scheduleCourseInSlots(courseId, dayIndex, i, i + courseMap.get(courseId).nbOfSlots - 1);
                        sessionsScheduled++;
                    } else if (courseMap.get(courseId).nbOfSlots == 1) {
                        scheduleCourseInSlot(courseId, dayIndex, i);
                        sessionsScheduled++;
                    }
                }
            }
        }

        System.out.println(sessionsScheduled);
    
        if (sessionsScheduled == courseMap.get(courseId).numberOfSessions) {
            return true;
        }else{
            System.out.println("any false");
        }
    
        return false;
    }
    

    private void scheduleCourseInSlot(UUID courseId, int dayIndex, int slotIndex){
        System.out.println("added course to day: " + dayIndex + " time slot: "+ slotIndex);
        schedule[dayIndex][slotIndex].add(courseId);
        printWriter.flush();
    }
    
    private void scheduleCourseInSlots(UUID courseId, int dayIndex, int SlotIndexStart, int slotIndexEnd){
        if(courseMap.get(courseId).TimeSlotIndexEnd < slotIndexEnd){
            return;
        }
        for(int i = SlotIndexStart; i <= slotIndexEnd; i++){
            printWriter.println("added course to day: " + dayIndex + " time slot: "+ i);
            schedule[dayIndex][i].add(courseId);
        };
        printWriter.flush();
    }


    //checks for conflicts in given slot
    private boolean isSlotAvailable(UUID courseId, int dayIndex, int slotIndex) {
        for (String conflict : courseMap.get(courseId).conflictingCourses) {
            for (UUID scheduledCourseUUID : schedule[dayIndex][slotIndex]) {

                String scheduledCourseid = courseMap.get(scheduledCourseUUID).courseID;
                // parsing to get rid of -i of different sections
                // for example instead of comparing PHYS201-2 (present in slot) which isnt present in the course.conflictingCourses
                // we compare PHYS201
                String[] scheduledCourseSplit = scheduledCourseid.split("-");
                String scheduledCourseID = scheduledCourseSplit[0];
                if (scheduledCourseID.equals(conflict)) {
                    return false;
                }
            }
    
        }
        return true;
    }

    // checks for conflicts in several slots in a row, used for courses with longer than 1 slot lecture time
    private boolean areSlotsAvailable(UUID courseId, int dayIndex, int slotIndexStart, int slotIndexEnd) {
        for (int i = slotIndexStart; i < slotIndexEnd; i++) {
            if (!isSlotAvailable(courseId, dayIndex, i)) {
                return false;
            }
        }
        return true;
    }    


    private static boolean canWorkWith(List<Integer> daypair, List<Integer> instructorDays) {
        for (int day : instructorDays) {
            if (!daypair.contains(day)) {
                System.out.println("false");
                return false; // The pair does not contain one of the working days
            }
        }
        System.out.println("true");
        return true; // All working days are present in the pair
    }


    public void displaySchedule(){
        for(int i = 0; i < days; i++){
            for(int j = 0; j < timeslots; j++){
                System.out.print("[");
                for(UUID courseUUID: schedule[i][j]){
                    System.out.print(courseMap.get(courseUUID).courseID+" ");
                }
                System.out.print("] ");
            }   
            System.out.println();
        }

        System.out.println("\nUnscheduled courses remaining: ");
        for(course c: unscheduledCourseHeap){
            System.out.println(c.courseID);
        }
    }

}
