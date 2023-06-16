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
            course course2 = new course("MTH202", 4, 1, 3, "Jane Smith", instructorDays2, "11:00 / 1:00 / 2:30", conflictingCourses2);
    
            // Add courses to the scheduler
            scheduler.addCourse(course1);
            scheduler.addCourse(course2);
    
            // Display the course schedule
            scheduler.displaySchedule();
        }
}
    

