import java.util.LinkedList;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

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

    LinkedList<String>[][] schedule = new LinkedList[days][timeslots];
    Map<UUID, course> courseMap;


    
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
            for(int i = 0;i < days;i++){
                for(int j = 0; j < timeslots; j++){
                    schedule[i][j] = new LinkedList<String>();
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the log file: " + e.getMessage());
        }
    }

    public void ScheduleCourses(){
        for(UUID courseId: courseMap.keySet()){
            addCourse(courseId);
        }
    }


    //calls the other helper method depending on requirements 
    //currently handles only section logic
    public void addCourse(UUID courseId) {

        course course = courseMap.get(courseId);

        // logic for sections here
        // if more than 1 section for a course
        // add -1 -2 -3 etc... to course ID  

        // this section adds the course's own code to the its conflict list
        // this helps with different sections                 
        String[] courseNameSplit = course.courseID.split("-");
        String courseID = courseNameSplit[0];
    
        if (!course.conflictingCourses.contains(courseID)) {
            course.conflictingCourses.add(courseID);
        }
        System.out.println(course.conflictingCourses.toString());

        course currentSection = null;
        if(course.numberOfSections > 1){
            for(int i = 1; i <= course.numberOfSections;i++){
                
                //currentSection same exact attributes as course but with courseID += "-"+i
               currentSection = new course(course.courseID+"-"+i,course.courseName,
                  course.numberOfCredits,course.numberOfSections,
                  course.numberOfSessions,course.instructorName,
                  course.instructorDays,course.TimeSlotIndexstart, course.TimeSlotIndexEnd,course.conflictingCourses,
                  course.courseType,course.nbOfSlots);

                
                UUID courseSectionId = UUID.randomUUID();
                courseMap.put(courseSectionId, currentSection);
                
                addCourseHelper(courseSectionId);
                
                if(currentSection.numberOfSessions > 0){
                    unscheduledCourseHeap.add(currentSection);
                }
            }
            courseMap.remove(courseId);

        }else{
            printWriter.println("Adding course " + course.courseID);
            
            courseMap.put(courseId, course);
            addCourseHelper(courseId);
            if(course.numberOfSessions > 0){
                unscheduledCourseHeap.add(course);
            }
        }
    }


    private void addCourseHelper(UUID courseId) {
        
        //  TODO:   case of < 1
        //           day pairs: MON-WED   T-TH 

        printWriter.println("adding course " + courseMap.get(courseId).courseID + "\nStart timeslot Index = " + courseMap.get(courseId).TimeSlotIndexstart + "\nEnd timeslot Index = " + courseMap.get(courseId).TimeSlotIndexEnd + "\nDays" + courseMap.get(courseId).instructorDays.toString());
            
        for(int dayIndex: courseMap.get(courseId).instructorDays){
            
            int sessionsPerDay = courseMap.get(courseId).numberOfSessions / courseMap.get(courseId).instructorDays.size();
            int sessionsScheduled = 0;

            for(int i = courseMap.get(courseId).TimeSlotIndexstart; i < courseMap.get(courseId).TimeSlotIndexEnd && sessionsScheduled < sessionsPerDay; i++){
                if (isSlotAvailable(courseId, dayIndex, i)) {
                    if (courseMap.get(courseId).nbOfSlots > 1) { // case 1, each course lecture takes more than 1 slot of time
                        if (areSlotsAvailable(courseId,dayIndex, i,  i + courseMap.get(courseId).nbOfSlots - 1)) {
                            scheduleCourseInSlots(courseId, dayIndex, i, i + courseMap.get(courseId).nbOfSlots - 1);
                            sessionsScheduled++;
                        }
                    } else { // case 2 each course lecture is just 1 slot
                        scheduleCourseInSlot(courseId, dayIndex, i);
                        sessionsScheduled++;
                    }
                }
            }
        }
        printWriter.println("\n");
        printWriter.flush();
    }


    //day spread:
    //adding will attempts to spread 
    //lectures across all 
    //available days if possible
    private void addCourseHelperWithoutSpread(course course) {

        

    }


    private void scheduleCourseInSlot(UUID courseId, int dayIndex, int slotIndex){
        printWriter.println("added course to day: " + dayIndex + " time slot: "+ slotIndex);
        schedule[dayIndex][slotIndex].add(courseId+"");
        printWriter.flush();
    }
    
    private void scheduleCourseInSlots(UUID courseId, int dayIndex, int SlotIndexStart, int slotIndexEnd){
        for(int i = SlotIndexStart; i <= slotIndexEnd; i++){
            printWriter.println("added course to day: " + dayIndex + " time slot: "+ i);
            schedule[dayIndex][i].add(courseId+"");
        };
        printWriter.flush();
    }



    //checks for conflicts in given slot
    private boolean isSlotAvailable(UUID courseId, int dayIndex, int slotIndex) {
        
        for (String conflict : courseMap.get(courseId).conflictingCourses) {
    
            for (String scheduledCourse : schedule[dayIndex][slotIndex]) {

                // parsing to get rid of -i of different sections
                // for example instead of comparing PHYS201-2 (present in slot) which isnt present in the course.conflictingCourses
                // we compare PHYS201
                String[] scheduledCourseSplit = scheduledCourse.split("-");
                String scheduledCourseID = scheduledCourseSplit[0];
                if (scheduledCourseID.equals(conflict)) {
                    return false;
                }
            }
    
        }
        return true;
    }

    // checks for conflicts in several slots in a row, used for courses with longer than 1 slot lecture time
    // TODO: add lecture parsing like in isSlotAvailable method
    private boolean areSlotsAvailable(UUID courseId, int dayIndex, int slotIndexStart, int slotIndexEnd){

        for(int i = slotIndexStart; i <= slotIndexEnd;i++){
            return isSlotAvailable(courseId, dayIndex, i);
        }
        return true;
    }


    private int getLastScheduledIndex(LinkedList<String>[] slots) {
        int lastScheduledIndex = -1;
        for (int i = 0; i < slots.length; i++) {
            if (!slots[i].isEmpty()) {
                lastScheduledIndex = i;
            }
        }
        return lastScheduledIndex;
    }
    
    
    //convert day string to index to use in 2d array
    private int getDayIndex(String day) {
        switch (day) {
            case "Monday":
                return 0;
            case "Tuesday":
                return 1;
            case "Wednesday":
                return 2;
            case "Thursday":
                return 3;
            case "Friday":
                return 4;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }
    }

    //p sure this is working now
    private int[] getSlotsIndicies(String hour1, String hour2) {
        LocalTime startTime = LocalTime.parse(hour1);
        LocalTime endTime = LocalTime.parse(hour2);


        LocalTime[] slots = {
            LocalTime.parse("08:00"), // Slot 0
            LocalTime.parse("09:30"), // Slot 1
            LocalTime.parse("11:00"), // Slot 2
            LocalTime.parse("13:00"), // Slot 3
            LocalTime.parse("14:30"), // Slot 4
            LocalTime.parse("16:00"), // Slot 5
            LocalTime.parse("17:15")  // Final hour
        };

        int startSlot = -1;
        int endSlot = -1;

        // Find the first slot
        if((startTime.isAfter(LocalTime.parse("12:15")) && startTime.isBefore(LocalTime.parse("13:00"))) // between 12:15 and 1 exclusive
            || startTime.equals(LocalTime.parse("12:15"))){
                startSlot = 3;
        }else{
            boolean slotFound = false;
            for (int i = 0; i < slots.length; i++) {
                if (startTime.isBefore(slots[i]) || startTime.equals(slots[i])) {
                    startSlot = i;
                    slotFound = true;
                    break;
                }
            }
            if(!slotFound){
                startSlot = slots.length-2;
            }
        }

        // Find the last slot
        if((endTime.isAfter(LocalTime.parse("12:15")) && endTime.isBefore(LocalTime.parse("13:00"))) // between 12:15 and 1 exclusive
            || endTime.equals(LocalTime.parse("12:15"))){
            endSlot = 3;
        }else{
            boolean slotFound = false;
            for (int i = startSlot; i < slots.length; i++) {
                if (endTime.isBefore(slots[i]) || endTime.equals(slots[i])) {
                    endSlot = i-1;
                    slotFound = true;
                    break;
                }
                if(!slotFound){
                    endSlot = slots.length-1;
                }
            }
        }
        // Return the viable slots as an array
        return new int[]{startSlot, endSlot};
    } 


    // returns hours as strings
    // these are to be sent to function:getSlotIndex()
    private LinkedList<String> convertHourstoSlots(String instructorHours){
        LinkedList<String> slots = new LinkedList<>();
        String[] hours = instructorHours.split("/");

        for (String hour : hours) {
            slots.add(hour.trim());
        }

        return slots;
    }


    public void displaySchedule(){
        for(int i = 0; i < days; i++){
            for(int j = 0; j < timeslots; j++){
                System.out.print(schedule[i][j].toString());
            }   
            System.out.println();
        }

        System.out.println("\nUnscheduled courses remaining: ");
        for(course c: unscheduledCourseHeap){
            System.out.println(c.courseID);
        }
    }

}
