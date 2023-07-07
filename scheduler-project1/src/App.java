import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class App {  
        public static void main(String[] args) {

            courseScheduler scheduler = new courseScheduler();

            LinkedList<Integer> instructorDays1 = new LinkedList<>(List.of(0, 2)); // Monday, Wednesday
            LinkedList<String> conflictingCourses1 = new LinkedList<>(List.of("MTH202"));
            course course1 = new course("CSE101", "Course 1", 3, 1, 4,
                    "John Doe", instructorDays1, 1, 4, conflictingCourses1,
                    "Type 1", 1);
    
            LinkedList<Integer> instructorDays2 = new LinkedList<>(List.of(0, 2)); // Monday, Wednesday
            LinkedList<String> conflictingCourses2 = new LinkedList<>(List.of("CSE101"));
            course course2 = new course("MTH202", "Course 2", 4, 1, 2,
                    "Jane Smith", instructorDays2, 1, 4, conflictingCourses2,
                    "Type 2", 1);
    
            LinkedList<Integer> instructorDays3 = new LinkedList<>(List.of(4)); // Friday
            LinkedList<String> conflictingCourses3 = new LinkedList<>(List.of("CSE101", "MTH202"));
            course course3 = new course("ENG101", "Course 3", 3, 1, 1,
                    "Sarah Johnson", instructorDays3, 5, 6, conflictingCourses3,
                    "Type 3", 1);
    
            LinkedList<Integer> instructorDays4 = new LinkedList<>(List.of(0, 2)); // Monday, Wednesday
            LinkedList<String> conflictingCourses4 = new LinkedList<>(List.of("CSE101", "MTH202"));
            course course4 = new course("PHY201", "Course 4", 4, 2, 2,
                    "Michael Brown", instructorDays4, 3, 6, conflictingCourses4,
                    "Type 4", 1);
    
            LinkedList<Integer> instructorDays5 = new LinkedList<>(List.of(3, 4)); // Thursday, Friday
            LinkedList<String> conflictingCourses5 = new LinkedList<>(List.of("MTH202"));
            course course5 = new course("BIO101", "Course 5", 3, 1, 2,
                    "Emily Davis", instructorDays5, 2, 4, conflictingCourses5,
                    "Type 5", 1);
    
            LinkedList<Integer> instructorDays6 = new LinkedList<>(List.of(1, 2, 3)); // Tuesday, Thursday
            LinkedList<String> conflictingCourses6 = new LinkedList<>(List.of("CSE101", "PHY201"));
            course course6 = new course("ENG202", "Course 6", 3, 1, 2,
                    "David Wilson", instructorDays6, 1, 6, conflictingCourses6,
                    "Type 6", 4);
            
            scheduler.courseMap.put(UUID.randomUUID(), course1);
            scheduler.courseMap.put(UUID.randomUUID(), course2);
            scheduler.courseMap.put(UUID.randomUUID(), course3);
            scheduler.courseMap.put(UUID.randomUUID(), course4);
            scheduler.courseMap.put(UUID.randomUUID(), course5);
            scheduler.courseMap.put(UUID.randomUUID(), course6);
                
            scheduler.ScheduleCourses();

            System.out.println();
            // Display the course schedule
            scheduler.displaySchedule();
    }
}
