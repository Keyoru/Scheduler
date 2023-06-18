import java.util.LinkedList;
import java.util.List;

public class App {
    public static void main(String[] args) {

        courseScheduler scheduler = new courseScheduler();

        LinkedList<String> instructorDays1 = new LinkedList<>(List.of("Monday", "Wednesday"));
        LinkedList<String> conflictingCourses1 = new LinkedList<>(List.of("MTH202"));
        course course1 = new course("CSE101", "Course 1", 3, 1, 2,
                "John Doe", instructorDays1, "8:00 / 5:15", conflictingCourses1,
                "Type 1", 1);

        LinkedList<String> instructorDays2 = new LinkedList<>(List.of("Tuesday"));
        LinkedList<String> conflictingCourses2 = new LinkedList<>(List.of("CSE101"));
        course course2 = new course("MTH202", "Course 2", 4, 1, 2,
                "Jane Smith", instructorDays2, "11:00 / 2:30", conflictingCourses2,
                "Type 2", 1);

        LinkedList<String> instructorDays3 = new LinkedList<>(List.of("Friday"));
        LinkedList<String> conflictingCourses3 = new LinkedList<>(List.of("CSE101", "MTH202"));
        course course3 = new course("ENG101", "Course 3", 3, 1, 2,
                "Sarah Johnson", instructorDays3, "9:30 / 12:15", conflictingCourses3,
                "Type 3", 1);

        LinkedList<String> instructorDays4 = new LinkedList<>(List.of("Monday", "Wednesday", "Friday"));
        LinkedList<String> conflictingCourses4 = new LinkedList<>(List.of("CSE101", "MTH202"));
        course course4 = new course("PHY201", "Course 4", 4, 2, 3,
                "Michael Brown", instructorDays4, "8:00 / 4:00", conflictingCourses4,
                "Type 4", 2);

        LinkedList<String> instructorDays5 = new LinkedList<>(List.of("Tuesday", "Thursday"));
        LinkedList<String> conflictingCourses5 = new LinkedList<>(List.of("MTH202"));
        course course5 = new course("BIO101", "Course 5", 3, 1, 2,
                "Emily Davis", instructorDays5, "11:00 / 2:30", conflictingCourses5,
                "Type 5", 1);

        LinkedList<String> instructorDays6 = new LinkedList<>(List.of("Monday", "Wednesday"));
        LinkedList<String> conflictingCourses6 = new LinkedList<>(List.of("CSE101", "PHY201"));
        course course6 = new course("ENG202", "Course 6", 3, 1, 2,
                "David Wilson", instructorDays6, "1:00 / 2:30", conflictingCourses6,
                "Type 6", 4);

        scheduler.addCourse(course1);
        scheduler.addCourse(course2);
        scheduler.addCourse(course3);
        scheduler.addCourse(course4);
        scheduler.addCourse(course5);
        scheduler.addCourse(course6);

        System.out.println();
        // Display the course schedule
        scheduler.displaySchedule();
    }
}
