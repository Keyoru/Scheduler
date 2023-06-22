import java.util.LinkedList;
import java.util.List;


public class App {
        
        public static void main(String[] args) {

        courseScheduler scheduler = new courseScheduler();
        
        LinkedList<String> instructorDays1 = new LinkedList<>(List.of("Monday", "Wednesday"));
        LinkedList<String> conflictingCourses1 = new LinkedList<>(List.of("MTH202"));
        course course1 = new course("CSE101", "Course 1", 3, 2, 2,
                "John Doe", instructorDays1, "08:00 / 11:00", conflictingCourses1,
                "Type 1", 1);

        LinkedList<String> instructorDays2 = new LinkedList<>(List.of("Tuesday", "Thursday"));
        LinkedList<String> conflictingCourses2 = new LinkedList<>(List.of("CSE101"));
        course course2 = new course("MTH202", "Course 2", 4, 1, 2,
                "Jane Smith", instructorDays2, "11:00 / 12:15", conflictingCourses2,
                "Type 2", 1);

        LinkedList<String> instructorDays3 = new LinkedList<>(List.of("Friday"));
        LinkedList<String> conflictingCourses3 = new LinkedList<>(List.of("CSE101", "MTH202"));
        course course3 = new course("ENG101", "Course 3", 3, 1, 1,
                "Sarah Johnson", instructorDays3, "11:15 / 12:30", conflictingCourses3,
                "Type 3", 1);

        LinkedList<String> instructorDays4 = new LinkedList<>(List.of("Monday", "Wednesday"));
        LinkedList<String> conflictingCourses4 = new LinkedList<>(List.of("CSE101", "MTH202"));
        course course4 = new course("PHY201-1", "Course 4", 4, 1, 2,
                "Michael Brown", instructorDays4, "08:00 / 17:15", conflictingCourses4,
                "Type 4", 2);

        course course5 = new course("PHY201-2", "Course 4", 4, 1, 2,
        "Michael Brown", instructorDays4, "08:00 / 17:15", conflictingCourses4,
        "Type 4", 2);

        //LinkedList<String> instructorDays5 = new LinkedList<>(List.of("Thursday", "Friday"));
        //LinkedList<String> conflictingCourses5 = new LinkedList<>(List.of("MTH202"));
        //course course5 = new course("BIO101", "Course 5", 3, 1, 2,
        //        "Emily Davis", instructorDays5, "09:00 / 11:00", conflictingCourses5,
        //        "Type 5", 1);

        LinkedList<String> instructorDays6 = new LinkedList<>(List.of("Monday", "Wednesday"));
        LinkedList<String> conflictingCourses6 = new LinkedList<>(List.of("CSE101", "PHY201"));
        course course6 = new course("ENG202", "Course 6", 3, 1, 2,
                "David Wilson", instructorDays6, "01:00 / 14:30", conflictingCourses6,
                "Type 6", 4);



        //scheduler.addCourse(course1);
        //scheduler.addCourse(course2);
        //scheduler.addCourse(course3);
        scheduler.addCourse(course4);
        scheduler.addCourse(course5);
        //scheduler.addCourse(course6);
        
        System.out.println();
        // Display the course schedule
        scheduler.displaySchedule();
    }
}
