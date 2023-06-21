import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileReader {
    private String file;

    public FileReader(String file) {
        this.file = file;
    }

    public LinkedList<Course> readCoursesFromSheet(String sheetName) throws IOException {
        LinkedList<Course> courseList = new LinkedList<>();

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
                LinkedList<String>instructors_day;

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
                            conflicting_courses = cell.getStringCellValue();
                            break; 
                        case 13:
                            instructors_day = cell.getStringCellValue();
                            break;                                         
                    }
                }

                Course course = new course(course_id, course_name, Integer.parseInt(num_credits),Integer.parseInt(num_sections),Integer.parseInt(num_sessions),instructor_name,
                                        instructors_day,instructor_hours,conflicting_courses,course_type, Integer.parseInt(num_of_slots));
                courseList.add(course);
            }
        } 

        workbook.close();
        fis.close();

        return courseList;
    }

}
