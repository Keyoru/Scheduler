import java.util.LinkedList;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class courseScheduler {

    int days = 5; 
    int timeslots = 6;

    LinkedList<String>[][] schedule = new LinkedList[days][timeslots];
    
    //for courses with no place found to store them
    LinkedList<course> unscheduledCourseHeap = new LinkedList<>();

    courseScheduler(){
        for(int i = 0;i < days;i++){
            for(int j = 0; j < timeslots; j++){
                schedule[i][j] = new LinkedList<String>();
            }
        }
    }


    //calls the other helper method depending on requirements 
    //currently handles only section logic
    public void addCourse(course course) {
 
        if (course.numberOfSessions <= 0) {
            System.out.println(course.courseID + " has no sessions remaining.");
            return;
        }

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

        if(course.numberOfSections > 1){
            for(int i = 1; i <= course.numberOfSections;i++){
                
                //currentSection same exact attributes as course but with courseID += "-"+i
                course currentSection = new course(course.courseID+"-"+i,course.courseName,
                  course.numberOfCredits,course.numberOfSections,
                  course.numberOfSessions,course.instructorName,
                  course.instructorDays,course.instructorHours,course.conflictingCourses,
                  course.courseType,course.nbOfSlots);

                addCourseHelper(currentSection);
            }
        }else{
            addCourseHelper(course);
        }
        

    }


    private void addCourseHelper(course course) {

        boolean courseUnscheduled = true;
        
        //  ISSUE:   case of < 1
        //           day pairs: MON-WED   T-TH 

        for(String day: course.instructorDays){
            int dayIndex = getDayIndex(day);
            
            LinkedList<String> timeslots = convertHourstoSlots(course.instructorHours);

            int startIndex = getSlotIndex(timeslots.getFirst());
            int endIndex = getSlotIndex(timeslots.getLast());

            int sessionsPerDay = course.numberOfSessions / course.instructorDays.size();
            int sessionsScheduled = 0;

            System.out.println(startIndex);
            System.out.println(endIndex);
            for(int i = startIndex; i < endIndex && sessionsScheduled < sessionsPerDay; i++){
                if (isSlotAvailable(course, dayIndex, i)) {
                    if (course.nbOfSlots > 1) { // case 1, each course lecture takes more than 1 slot of time
                        if (areSlotsAvailable(course.conflictingCourses,dayIndex, i,  i + course.nbOfSlots - 1)) {
                            scheduleCourseInSlots(course.courseID, dayIndex, startIndex, i + course.nbOfSlots - 1);
                            sessionsScheduled++;
                        }
                    } else { // case 2 each course lecture is just 1 slot
                        scheduleCourseInSlot(course.courseID, dayIndex, i);
                        sessionsScheduled++;
                    }
                }
            }
        }
    }


    //day spread:
    //adding will attempts to spread 
    //lectures across all 
    //available days if possible
    private void addCourseHelperWithoutSpread(course course) {

        

    }


    private void scheduleCourseInSlot(String courseID, int dayIndex, int slotIndex){
        schedule[dayIndex][slotIndex].add(courseID);
    }
    
    private void scheduleCourseInSlots(String courseID, int dayIndex, int SlotIndexStart, int slotIndexEnd){
        for(int i = SlotIndexStart; i <= slotIndexEnd; i++){
            schedule[dayIndex][i].add(courseID);
        };
    }



    //checks for conflicts in given slot
    private boolean isSlotAvailable(course course, int dayIndex, int slotIndex) {
        
        for (String conflict : course.conflictingCourses) {
    
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
    private boolean areSlotsAvailable(LinkedList<String> courseConflicts, int dayIndex, int slotIndexStart, int slotIndexEnd){
        for(String course: courseConflicts){
            for(int i = slotIndexStart; i <= slotIndexEnd;i++){
                if(schedule[dayIndex][i].contains(course)){
                    return false;
                }
            }
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


    private int getSlotIndex(String hour) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime inputTime = LocalTime.parse(hour, formatter);

        if (inputTime.isBefore(LocalTime.parse("08:00"))) {
            return 0;
        }
        if (inputTime.isAfter(LocalTime.parse("12:15")) && inputTime.isBefore(LocalTime.parse("13:00"))) {
            System.out.println("between 12:15 and 1");
            return 2;
        }
        
        
        LocalTime[] slotTimes = {
            LocalTime.parse("08:00"),
            LocalTime.parse("09:30"),
            LocalTime.parse("11:00"),
            LocalTime.parse("12:15"),
            LocalTime.parse("14:30"),
            LocalTime.parse("16:00"),
            LocalTime.parse("17:15")
        };
        for (int i = 0; i < slotTimes.length; i++) {
            if(inputTime.equals(slotTimes[i])){
                return i;
            } else if (inputTime.isAfter(slotTimes[i]) && inputTime.isBefore(slotTimes[i + 1])) {
                return i+1;
            }
        }
        return slotTimes.length - 1;
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


    private boolean checkConflictinSlot(LinkedList<String> conflictsList, LinkedList<String> slotList){
        for (String conflict : conflictsList) {
            if (slotList.contains(conflict)) {
                return true;
            }
        }
        return false;

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
