import java.util.LinkedList;

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



    public void addCourse(course course) {
 
        if (course.numberofSessions <= 0) {
            System.out.println(course.courseID + " has no sessions remaining.");
            return;
        }



    }

    //day spread:
    //adding will attempts to spread 
    //lectures across all 
    //available days if possible
    private void addCourseWithoutSpread(course course) {

        

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

    //convert hours string to index
    private int getSlotIndex(String hour) {

        switch(hour){
            case "8:00":
                return 0;
            case "9:30":
                return 1;
            case "11:00":
                return 2;

            case "12:15":
                return 3;
            case "1:00":
                return 3;
            
            case "2:30":   
                return 4;
            case "4:00":
                return 5;
            case "5:15":
                return 6;
            default:
            throw new IllegalArgumentException("Invalid hour: " + hour);
            }

    }

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
