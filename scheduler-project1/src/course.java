import java.util.LinkedList;

public class course {
    
    String courseID;
    String courseName;

    int numberOfCredits; 
    int numberofSessions;   //per week
    int numberOfSections;  //if 2 sections and 2 sessions per week then each section has 2 sessions per week

    String instructorName;
    LinkedList<String> instructorDays;
    String instructorHours; //format for example
                            //8:00 / 9:30 

    LinkedList<String> conflictingCourses;

    String courseType;
    int nbOfSlots;   //number of slots a single lecture takes
                     //if course lecture length is 1:15 then 1 slot, 2 hours length is 2 slots etc etc

    course(String ID, String name, int creds, int sections, int sessions, String instname,
     LinkedList<String> instdays, String insthours, LinkedList<String> conflicts
     ,String Type, int Slots){
        courseID = ID;
        courseName = name;
        numberOfCredits = creds;
        numberOfSections = sections;
        numberofSessions = sessions;
        instructorName = instname;
        instructorDays = instdays;
        instructorHours = insthours;
        conflictingCourses = conflicts;
        courseType = Type;
        nbOfSlots = Slots;
     }
}
