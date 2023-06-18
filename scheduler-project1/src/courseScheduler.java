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

    public void addCourse(course c) {
        
        boolean courseUnscheduled = true;
    
        int sessionsPerDay = c.numberofSessions / c.instructorDays.size();

        for (String day : c.instructorDays) {
            
            int sessionsScheduled = 0;
            int dayIndex = getDayIndex(day);
            LinkedList<String> timeslotshour = convertHourstoSlots(c.instructorHours);
    
            // Start of instructor's available time (convert string --> int index)
            int index1 = getSlotIndex(timeslotshour.get(0));
            // End of instructor's available time (convert string --> int index)
            int index2 = getSlotIndex(timeslotshour.get(1));
    
            boolean conflictFlag = false;
            for (int i = index1; i < index2; i++) {
                for (String conflict : c.conflictingCourses) {
                    if (schedule[dayIndex][i].contains(conflict)) {
                        System.out.println(c.courseID + " conflict found with " + conflict);
                        conflictFlag = true;
                        break;
                    }
                }
    
                if (conflictFlag) {
                    conflictFlag = false;
                    continue;
                }
    
                if (sessionsScheduled == sessionsPerDay) {
                    break;
                }

                if(c.numberofSessions == 0){
                    break;
                }
                schedule[dayIndex][i].add(c.courseID);
                c.numberofSessions--;
                sessionsScheduled++;

            }
            if (c.numberofSessions <= 0) {
                System.out.println(c.courseID + " reached 0 sessions");
                courseUnscheduled = false;
                break;
            }
        }         

        if(courseUnscheduled && c.numberofSessions > 0){
            addCourseNoDaySpread(c);
        }
    }

    //day spread is for spreading the sessions
    //across all available days if possible
    public void addCourseNoDaySpread(course c) {
        boolean courseUnscheduled = true;
    
        if (c.numberofSessions == 0) {
            System.out.println(c.courseID + " reached 0 sessions");
            return;
        }
    
        for (String day : c.instructorDays) {
            int dayIndex = getDayIndex(day);
            LinkedList<String> timeslotshour = convertHourstoSlots(c.instructorHours);
    
            // Start of instructor's available time (convert string --> int index)
            int index1 = getSlotIndex(timeslotshour.get(0));
            // End of instructor's available time (convert string --> int index)
            int index2 = getSlotIndex(timeslotshour.get(1));
    
            // Retrieve the last scheduled timeslot index for the current day
            int lastScheduledIndex = getLastScheduledIndex(schedule[dayIndex]);
    
            boolean conflictFlag = false;
            for (int i = lastScheduledIndex + 1; i < index2; i++) {
                for (String conflict : c.conflictingCourses) {
                    if (schedule[dayIndex][i].contains(conflict)) {
                        System.out.println(c.courseID + " conflict found with " + conflict);
                        conflictFlag = true;
                        break;
                    }
                }
    
                if (conflictFlag) {
                    conflictFlag = false;
                    continue;
                }
    
                if (c.numberofSessions == 0) {
                    break;
                }
                schedule[dayIndex][i].add(c.courseID);
                c.numberofSessions--;
    
                lastScheduledIndex = i; // Update the last scheduled timeslot index
    
                courseUnscheduled = false;
            }
            if (c.numberofSessions <= 0) {
                break;
            }
        }
        if (courseUnscheduled) {
            unscheduledCourseHeap.add(c);
        }
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
            return -1;
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
