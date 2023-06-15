import java.util.LinkedList;

public class course {
    
    String courseID;
    int numberOfCredits;
    int numberOfSections;
    int numberofSessions;   //per week

    String instructorName;
    LinkedList<String> instructorDays;
    String instructorHours; //format for example
                            //8:00 / 9:30 
    LinkedList<String> conflictingCourses;

    public course(String ID, int creds, int sections, int sessions, String instname, LinkedList<String> instdays, String insthours, LinkedList<String> conflicts){
        courseID = ID;
        numberOfCredits = creds;
        numberOfSections = sections;
        numberofSessions = sessions;
        instructorName = instname;
        instructorDays = instdays;
        instructorHours = insthours;
        conflictingCourses = conflicts;
    }

    


}
