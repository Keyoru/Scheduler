import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.UUID;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileReader {
    private String file;

    public FileReader(String file) {
        this.file = file;
    }

    public Map<UUID, Course> readCoursesFromSheet(String sheetName) throws IOException {
       HashMap<UUID, Course> Courses = new HashMap<UUID, Course>();

        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);

        // Get the specific sheet by name
        Sheet sheet = workbook.getSheet(sheetName);

        if (sheet != null) {
            // Iterate over rows
            for (Row row : sheet) {
                String course_id = "";
                String course_name = "";
                String num_credits = "";
                String num_sessions = "";
                String num_sections = "";
                String time_slot = "";
                String instructor_name = "";
                String instructor_hours = "";
                String course_type = "";
                String num_of_slots = "";
                LinkedList<String>conflicting_courses;
                String instructors_day;

                // Iterate over cells in the row
                for (Cell cell : row) {
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            course_id = cell.getStringCellValue();
                            break;
                        case 1:
                            course_name = cell.getStringCellValue();
                            break;
                        case 4:
                            num_credits = cell.getStringCellValue();
                            break;
                        case 5:
                            num_sessions = cell.getStringCellValue();
                            break;  
                        case 6:
                            time_slot = cell.getStringCellValue();
                            break; 
                        case 7:
                            num_sessions = cell.getStringCellValue();
                            break; 
                        case 8:
                            instructor_name = cell.getStringCellValue();
                            break;  
                        case 9:
                            instructor_hours = cell.getStringCellValue();
                            break; 
                        case 10:
                            course_type = cell.getStringCellValue();
                            break;     
                        case 11:
                            num_of_slots = cell.getStringCellValue();
                            break;      
                        case 12:
                            conflicting_courses = new LinkedList<String>();
                            conflicting_courses = cell.getStringCellValue();
                            break; 
                        case 13:
                            instructors_day = cell.getStringCellValue();
                            break;                                         
                    }
                }

               LinkedList<String>Split_Days = Split_Days(instructors_day);
               LinkedList<Integer>instructor_days = new LinkedList<Integer>();

               for(int i=0;i<Split_Days.size();i++){
                   instructor_days.add(getDayIndex(Split_Days.get(i)));
               }

              
              LinkedList<String> timeslots = convertHourstoSlots(instructor_hours);
               int[] slots = getSlotsIndicies(timeslots.getFirst() , timeslots.getLast());
               int index1 = slots[0];
               int index2 = slots[1];


               Course course = new  Course(course_id, course_name, Integer.parseInt(num_credits),Integer.parseInt(num_sections),Integer.parseInt(num_sessions), instructor_name,
                                instructor_days, index1, index2, conflicting_courses , course_type ,Integer.parseInt(num_of_slots) );

            //    Course course = new Course(course_id, course_name, Integer.parseInt(num_credits),Integer.parseInt(num_sections),Integer.parseInt(num_sessions),instructor_name,
             //                           instructors_day,instructor_hours,conflicting_courses,course_type, Integer.parseInt(num_of_slots));
                Courses.put(UUID.randomUUID() , course);
            }
        } 

        workbook.close();
        fis.close();

        return Courses;
    }

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

     private LinkedList<String> convertHourstoSlots(String instructorHours){
        LinkedList<String> slots = new LinkedList<>();
        String[] hours = instructorHours.split("/");

        for (String hour : hours) {
            slots.add(hour.trim());
        }

        return slots;
    }

    private LinkedList<String> Split_Days(String instructors_day){
        LinkedList<String> slots = new LinkedList<String>();
        String[] hours = instructors_day.split("and");

        for (String hour : hours) {
            slots.add(hour.trim());
        }

        return slots;
    }


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
 }
