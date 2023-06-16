import java.util.LinkedList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Create a course scheduler
        courseScheduler scheduler = new courseScheduler();
       // Create example courses
       LinkedList<String> instructorDays1 = new LinkedList<>(List.of("Monday", "Wednesday"));
       LinkedList<String> conflictingCourses1 = new LinkedList<>(List.of("MTH202"));
       course course1 = new course("CSE101", 3, 1, 2, "John Doe", instructorDays1, "8:00 / 9:30", conflictingCourses1);

       LinkedList<String> instructorDays2 = new LinkedList<>(List.of("Tuesday", "Thursday"));
       LinkedList<String> conflictingCourses2 = new LinkedList<>(List.of("CSE101"));
       course course2 = new course("MTH202", 4, 1, 3, "Jane Smith", instructorDays2, "11:00 / 2:30", conflictingCourses2);

       LinkedList<String> instructorDays3 = new LinkedList<>(List.of("Friday"));
       LinkedList<String> conflictingCourses3 = new LinkedList<>(List.of("CSE101", "MTH202"));
       course course3 = new course("ENG101", 3, 1, 2, "Sarah Johnson", instructorDays3, "9:30 / 11:00", conflictingCourses3);

       LinkedList<String> instructorDays4 = new LinkedList<>(List.of("Monday", "Wednesday", "Friday"));
       LinkedList<String> conflictingCourses4 = new LinkedList<>(List.of("CSE101", "MTH202"));
       course course4 = new course("PHY201", 4, 2, 3, "Michael Brown", instructorDays4, "8:00 / 4:00", conflictingCourses4);

       LinkedList<String> instructorDays5 = new LinkedList<>(List.of("Tuesday", "Thursday"));
       LinkedList<String> conflictingCourses5 = new LinkedList<>(List.of("MTH202"));
       course course5 = new course("BIO101", 3, 1, 2, "Emily Davis", instructorDays5, "10:45 / 12:15", conflictingCourses5);

       LinkedList<String> instructorDays6 = new LinkedList<>(List.of("Monday", "Wednesday"));
       LinkedList<String> conflictingCourses6 = new LinkedList<>(List.of("CSE101", "PHY201"));
       course course6 = new course("ENG202", 3, 1, 2, "David Wilson", instructorDays6, "1:00 / 2:30", conflictingCourses6);

        // Add courses to the scheduler
        scheduler.addCourse(course1);
        scheduler.addCourse(course2);
        scheduler.addCourse(course3);
        scheduler.addCourse(course4);

        // Display the course schedule
        scheduler.displaySchedule();
    }
}
    

