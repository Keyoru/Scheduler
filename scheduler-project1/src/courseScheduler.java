import java.util.LinkedList;

public class courseScheduler {


    int days = 5; 
    int timeslots = 6;

    LinkedList<String>[][] array = new LinkedList[timeslots][days];
    

    public void addCourse(course c){

        for(String day:c.instructorDays){
            int dayIndex = getDayIndex(day);
            LinkedList<String> courseSlots = convertHourstoSlots(c.instructorHours);

            for (String slot : courseSlots) {
                int slotIndex = getSlotIndex(slot);
                schedule[dayIndex][slotIndex] = course.getCourseID();
            }
        }

    }


 

    //convert day string to index to use in 2d array
    public int getDayIndex(String day) {
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
    private int getSlotIndex(String slot) {



        return -1;
    }

    public LinkedList<String> convertHourstoSlots(String instructorHours){
        LinkedList<String> slots = new LinkedList<>();
        String[] hours = instructorHours.split("/");

        for (String hour : hours) {
            slots.add(hour.trim());
        }

        return slots;
    }

}
