import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileReader {
    private File file;

    public FileReader(File f) {
        this.file = f;
    }

    public LinkedList<Course> readCourses() throws IOException {
    LinkedList<Course> courseList = new LinkedList<>();

    FileInputStream fis = new FileInputStream(file);
    Workbook workbook = new XSSFWorkbook(fis);
    Sheet sheet = workbook.getSheetAt(0); 

    // Iterate over rows
    for (Row row : sheet) {
        String course_code = "";
        String num_credits = "";
        String num_sessions = "";
        String time_slot = "";


        // Iterate over cells in the row
        for (Cell cell : row) {
            int columnIndex = cell.getColumnIndex();
            switch (columnIndex) {
                case 0:
                    course_code = cell.getStringCellValue();
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
                // Add more cases for additional columns if needed
            }
        }

        Course course = new Course(course_code, num_credits, num_credits,time_slot);
        courseList.add(course);
    }

    workbook.close();
    fis.close();

    return courseList;
}

private Object getCellValue(Cell cell) {
    CellType cellType = cell.getCellType();

    switch (cellType) {
        case STRING:
            return cell.getStringCellValue();
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            } else {
                return cell.getNumericCellValue();
            }
        case BOOLEAN:
            return cell.getBooleanCellValue();
        case FORMULA:
            // Handle formulas as needed
            return cell.getCellFormula();
        case BLANK:
            return "";
        default:
            return null;
    }
}


}