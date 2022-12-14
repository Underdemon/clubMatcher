/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menus;

import java.util.Scanner;

/**
 *
 * @author rayan
 */
public class MClub extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private int isDetailed = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = null;
    private int hour = 0;
    private int min = 0;
    private String start_time = null;
    private String BLA = null;
    
    public MClub()
    {
        do
        {
            choice = validateInput
            (
                "\n\t 0 - Exit to Main Menu"
                + "\n\t 1 - Display all exisiting clubs with their corresponding details"
                + "\n\t 2 - Display all existing club names"
                + "\n\t 3 - Search for a club's details"
                + "\n\t 4 - Search for clubs that occur before school/during lunch/after school"
                + "\n\t 5 - Search for all clubs running on a specific day"
                + "\n\t 6 - Search for all clubs in a department"
                + "\n\t 7 - Change the teacher running the club"
                + "\n\t 8 - Change the day the club is running on"                
                + "\n\t 9 - Change the time the club starts"
                + "\n\t10 - Insert a club"
                + "\n\t11 - Remove a club"
                + "\n\t12 - Remove all clubs running on a day"
                + "\n\t13 - Remove all clubs running before school/during lunch/after school"
                + "\n\t14 - Remove all clubs in a department"
            );
            
            switch(choice)
            {
                case 0:     // exit to main menu
                    return;

                case 1:     // display all clubs with detail
                    db.queryOutput
                    (
                                "SELECT Club.ClubName, Subjects.SubjectsName, Club.DayRunning, Club.BLA, Person.PersonName, Department.DepartmentName, Club.StartTime "
                                + "FROM Club "
                                + "INNER JOIN Subjects ON Club.SubjectID = Subjects.SubjectsID "
                                + "INNER JOIN Department ON Club.DepartmentID = Department.DepartmentID "
                                + "INNER JOIN Teacher ON Club.TeacherID = Teacher.TeacherID "
                                + "INNER JOIN Person ON Teacher.PersonID = Person.PersonID"
                                , "Club"
                    );
                    break;

                case 2:     // display names of all clubs
                    db.queryOutput
                    (
                            "SELECT Club.ClubName FROM Club"
                            , ""
                    );
                    break;

                case 3:     // search for a club
                    System.out.println("Please enter the name of the club you would like to see the details of: ");
                    input = scanner.nextLine();
                    db.queryOutput
                    (
                                "SELECT Club.ClubName, Subjects.SubjectsName, Club.DayRunning, Club.BLA, Person.PersonName, Department.DepartmentName, Club.StartTime "
                                + "FROM Club "
                                + "INNER JOIN Subjects ON Club.SubjectID = Subjects.SubjectsID "
                                + "INNER JOIN Department ON Club.DepartmentID = Department.DepartmentID "
                                + "INNER JOIN Teacher ON Club.TeacherID = Teacher.TeacherID "
                                + "INNER JOIN Person ON Teacher.PersonID = Person.PersonID "
                                + "WHERE Club.ClubName = '" + input + "'"
                                , "Club"
                    );
                    break;

                case 4:     // search for clubs BLA
                    isDetailed = validateInput("Please enter:\n\t 1 - if you only want the club name\n\t 2 - if you want the club name and the club details");                    
                    while(isDetailed < 1 || isDetailed > 2)
                    {
                        System.out.println("The number entered was out of the specified range");
                        isDetailed = validateInput("Please enter:\n\t 1 - if you only want the club name\n\t 2 - if you want the club name and the club details");  
                    }
                    
                    choice = validateInput
                    (
                            "Please input the corresponding number to the time you want to search for: "
                            + "\n\t 1 - Before School"
                            + "\n\t 2 - During Lunch"
                            + "\n\t 3 - After School"
                    );
                    while((choice < 1 || choice > 3) && choice != 0)
                    {
                        System.out.println("The value you entered is not in the specified range");
                        choice = validateInput
                        (
                                "Please input the corresponding number to the time you want to search for: "
                                + "\n\t 1 - Before School"
                                + "\n\t 2 - During Lunch"
                                + "\n\t 3 - After School"
                        );
                    }
                    
                    if(choice == 1)
                        input = "B";
                    else if(choice == 2)
                        input = "L";
                    else if(choice == 3)
                        input = "A";
                    
                    if(isDetailed == 1)
                    {
                        db.queryOutput
                        (
                                "SELECT Club.ClubName FROM Club WHERE Club.BLA = '" + input + "'", "" 
                        );
                    }
                    else
                    {
                        db.queryOutput
                        (
                                    "SELECT Club.ClubName, Subjects.SubjectsName, Club.DayRunning, Club.BLA, Person.PersonName, Department.DepartmentName, Club.StartTime "
                                    + "FROM Club "
                                    + "INNER JOIN Subjects ON Club.SubjectID = Subjects.SubjectsID "
                                    + "INNER JOIN Department ON Club.DepartmentID = Department.DepartmentID "
                                    + "INNER JOIN Teacher ON Club.TeacherID = Teacher.TeacherID "
                                    + "INNER JOIN Person ON Teacher.PersonID = Person.PersonID "
                                    + "WHERE Club.BLA = '" + input + "'"
                                    , "Club"
                        );
                    }
                    break;

                case 5:     // search for clubs on a specific day
                    isDetailed = validateInput("Please enter:\n\t 1 - if you only want the club name\n\t 2 - if you want the club name and the club details");                    
                    while(isDetailed < 1 || isDetailed > 2)
                    {
                        System.out.println("The number entered was out of the specified range");
                        isDetailed = validateInput("Please enter:\n\t 1 - if you only want the club name\n\t 2 - if you want the club name and the club details");  
                    }
                    
                    choice = validateInput
                    (
                            "Please enter the number corresponding to the day you want to search"
                            + "\n\t 1 - Monday"
                            + "\n\t 2 - Tuesday"
                            + "\n\t 3 - Wednesday"
                            + "\n\t 4 - Thursday"
                            + "\n\t 5 - Friday"
                    );
                    
                    while(choice < 1 || choice > 5)
                    {
                        System.out.println("The number you entered was invalid");
                        choice = validateInput
                        (
                                "Please enter the number corresponding to the day you want to search"
                                + "\n\t 1 - Monday"
                                + "\n\t 2 - Tuesday"
                                + "\n\t 3 - Wednesday"
                                + "\n\t 4 - Thursday"
                                + "\n\t 5 - Friday"
                        );
                    }
                    
                    if(isDetailed == 1)
                    {
                        db.queryOutput
                        (
                                "SELECT Club.ClubName FROM Club WHERE Club.DayRunning = " + choice, "" 
                        );
                    }
                    else
                    {
                        db.queryOutput
                        (
                                    "SELECT Club.ClubName, Subjects.SubjectsName, Club.DayRunning, Club.BLA, Person.PersonName, Department.DepartmentName, Club.StartTime "
                                    + "FROM Club "
                                    + "INNER JOIN Subjects ON Club.SubjectID = Subjects.SubjectsID "
                                    + "INNER JOIN Department ON Club.DepartmentID = Department.DepartmentID "
                                    + "INNER JOIN Teacher ON Club.TeacherID = Teacher.TeacherID "
                                    + "INNER JOIN Person ON Teacher.PersonID = Person.PersonID "
                                    + "WHERE Club.DayRunning = " + choice
                                    , "Club"
                        );
                    }
                    break;

                case 6:     // search for all clubs ina department
                    isDetailed = validateInput("Please enter:\n\t 1 - if you only want the club name\n\t 2 - if you want the club name and the club details");                    
                    while(isDetailed < 1 || isDetailed > 2)
                    {
                        System.out.println("The number entered was out of the specified range");
                        isDetailed = validateInput("Please enter:\n\t 1 - if you only want the club name\n\t 2 - if you want the club name and the club details");  
                    }
                    
                    System.out.println("Please input the department you want to search for (enter \"list\" to list the departments in the database): ");
                    input = scanner.nextLine();
                    while(input.equals("list"))
                    {
                        db.queryOutput("SELECT Department.DepartmentName FROM Department", "");
                        System.out.println("Please input the department you want to search for (enter \"list\" to list the departments in the database): ");
                        input = scanner.nextLine();
                    }
                    
                    if(isDetailed == 1)
                    {
                        db.queryOutput
                        (
                                "SELECT Club.ClubName FROM Club "
                                + "INNER JOIN Department ON Club.DepartmentID = Department.DepartmentID"
                                + "WHERE Department.DepartmentName = '" + input + "'", "" 
                        );
                    }
                    else
                    {
                        db.queryOutput
                        (
                                    "SELECT Club.ClubName, Subjects.SubjectsName, Club.DayRunning, Club.BLA, Person.PersonName, Department.DepartmentName, Club.StartTime "
                                    + "FROM Club "
                                    + "INNER JOIN Subjects ON Club.SubjectID = Subjects.SubjectsID "
                                    + "INNER JOIN Department ON Club.DepartmentID = Department.DepartmentID "
                                    + "INNER JOIN Teacher ON Club.TeacherID = Teacher.TeacherID "
                                    + "INNER JOIN Person ON Teacher.PersonID = Person.PersonID "
                                    + "WHERE Department.DepartmentName = '" + input + "'"
                                    , "Club"
                        );
                    }
                    break;
                    
                case 7:     // change the teacher running a club
                    System.out.println("Please enter the club you want to change the teacher for (enter \"list\" to list the clubs in the database): ");
                    input = scanner.nextLine();
                    while(input.equals("list"))
                    {
                        db.queryOutput("SELECT Club.ClubName FROM Club", "");
                        System.out.println("Please input the club you want to search for (enter \"list\" to list the clubs in the database): ");
                        input = scanner.nextLine();
                    }
                    
                    System.out.println("Current Teacher: ");
                    db.queryOutput
                    (
                            "SELECT Person.PersonName FROM Club "
                            + "INNER JOIN Teacher ON Club.TeacherID = Teacher.TeacherID "
                            + "INNER JOIN Person ON Teacher.PersonID = Person.PersonID "
                            + "WHERE Club.ClubName = '" + input + "'"
                            , ""
                    );
                    
                    
                    String name = db.studentTeacherName(false);
                    
                    db.executeQuery
                    (
                            "UPDATE Club "
                            + "SET TeacherID = "
                            + "(SELECT Teacher.TeacherID FROM Teacher INNER JOIN Person ON Teacher.PersonID = Person.PersonID "
                            + "WHERE Person.PersonName = '" + name + "') "
                            + "WHERE Club.ClubName = '" + input + "'"
                    );
                    
                    break;

                case 8:     // change the day the club is running on
                    System.out.println("Please enter the club you want to change the teacher for (enter \"list\" to list the clubs in the database): ");
                    input = scanner.nextLine();
                    while(input.equals("list"))
                    {
                        db.queryOutput("SELECT Club.ClubName FROM Club", "");
                        System.out.println("Please input the club you want to search for (enter \"list\" to list the clubs in the database): ");
                        input = scanner.nextLine();
                    }
                    
                    choice = validateInput
                    ("Please enter the day you want to change the club to"
                            + "\n\t 1 - Monday"
                            + "\n\t 2 - Tuesday"
                            + "\n\t 3 - Wednesday"
                            + "\n\t 4 - Thursday"
                            + "\n\t 5 - Friday"
                    );
                    while(choice < 1 || choice > 5)
                    {
                        System.out.println("Please input a valid number");
                        choice = validateInput
                        ("Please enter the day you want to change the club to"
                                + "\n\t 1 - Monday"
                                + "\n\t 2 - Tuesday"
                                + "\n\t 3 - Wednesday"
                                + "\n\t 4 - Thursday"
                                + "\n\t 5 - Friday"
                        );
                    }
                    
                    db.executeQuery
                    (
                            "UPDATE Club "
                            + "SET DayRunning = " + choice
                            + " WHERE Club.ClubName = '" + input + "'"
                    );
                    break;

                case 9:     // change the time the club starts
                    System.out.println("Please enter the club you want to change the teacher for (enter \"list\" to list the clubs in the database): ");
                    input = scanner.nextLine();
                    while(input.equals("list"))
                    {
                        db.queryOutput("SELECT Club.ClubName FROM Club", "");
                        System.out.println("Please input the club you want to search for (enter \"list\" to list the clubs in the database): ");
                        input = scanner.nextLine();
                    }
                    
                    System.out.println(input + " club start time: ");
                    db.queryOutput
                    (
                            "SELECT Club.StartTime FROM Club WHERE Club.ClubName = '" + input + "'"
                            , ""
                    );
                    
                    while(hour > 8 && hour < 12 || hour > 13 && hour < 15 || hour < 6 || hour > 18)
                    {
                        hour = validateInput
                        (
                                "The club must either"
                                        + "\n\t a) begin before school starts (8:00 and before)"
                                        + "\n\t b) begin during lunch time (between 12:52 and 13:15)"
                                        + "\n\t c) begin after school (afer 15:35)"
                                        + "\n\t NOTE: The school opens at 06:00 and closes at 18:00"
                                        + "\n Please input the hour the club starts at: "
                        );
                    }
                    
                    min = validateInput("The hour you have entered is " + hour + "\nPlease enter the minute of the hour the club starts at (between 00-59)");
                    if(hour == 8)
                    {
                        while(min > 30 || min < 0)     // while inputted time is > 8:30
                        {
                            min = validateInput("The time you entered was 8:" + min + "\nThis time is past the starting time for school (8:30)\nPlease enter a time that is before the start of school");
                        }
                    }
                    else if(hour == 12)
                    {
                        while(min < 52 || min >= 60)
                        {
                            min = validateInput("The time you entered was 12:" + min + "\nThis time is before lunch begins (12:52)\nPlease enter a time that is after the start of lunch");
                        }
                    }
                    else if(hour == 13)
                    {
                        while(min > 15 || min < 0)
                        {
                            min = validateInput("The time you entered was 13:" + min + "\nThis time is past the maximum club starting time at lunch (13:15)\nPlease enter a time that is before 13:15");
                        }
                    }
                    else if(hour == 15)
                    {
                        while(min < 35 || min >= 60)
                        {
                            min = validateInput("The time you entered was 15:" + min + "\nThis time is before school ends (15:35)\nPlease enter a time that is after the end of school");
                        }
                    }
                    else
                    {
                        while(min < 0 || min >= 60)
                        {
                            min = validateInput("The time you entered was " + hour + ":" + min + "\nPlease enter a valid minuted value");
                        }
                    }
                    
                    start_time = Integer.toString(hour) + ":" + Integer.toString(min);
                    if(hour <= 8)
                        BLA = "B";
                    else if(hour >= 3)
                        BLA = "A";
                    else
                        BLA = "L";
                    db.executeQuery("UPDATE Club SET StartTime = '" + start_time + "', BLA = '" + BLA + "' WHERE Club.ClubName = '" + input + "'");
                    break;
                    
                case 10:    // add a club
                    System.out.println("Please input the name of the club you want to add: ");
                    String club_name = scanner.nextLine();
                    
                    System.out.println("Please enter the subject associated to the club (enter \"list\" to list the subjects in the database): ");
                    String subject = scanner.nextLine();
                    while(subject.equals("list"))
                    {
                        db.queryOutput("SELECT Subjects.SubjectsName FROM Subjects", "");
                        System.out.println("Please enter the subject associated to the club (enter \"list\" to list the subjects in the database): ");
                        subject = scanner.nextLine();
                    }
                    int subjectID = db.getID(subject, "Subjects");
                    
                    int day_running = 0;
                    while(day_running < 1 || day_running > 5)
                    {
                        day_running = validateInput
                        (
                                "Please input the day the club is running on: "
                                + "\n\t 1 - Monday"
                                    + "\n\t 2 - Tuesday"
                                    + "\n\t 3 - Wednesday"
                                    + "\n\t 4 - Thursday"
                                    + "\n\t 5 - Friday"
                        );
                    }
                    
                    String teacher_name = db.studentTeacherName(false);
                    int teacherID = db.getTeacherID(teacher_name);
                    
                    System.out.println("Please input the department the club is associated to (enter \"list\" to list the departments in the database): ");
                    String department = scanner.nextLine();
                    while(department.equals("list"))
                    {
                        db.queryOutput("SELECT Department.DepartmentName FROM Department", "");
                        System.out.println("Please input the department the club is associated to (enter \"list\" to list the departments in the database): ");
                        department = scanner.nextLine();
                    }
                    int depID = db.getID(department, "Department");
                    
                    System.out.println("Please enter the hour the club takes place: ");
                    while(hour > 8 && hour < 12 || hour > 13 && hour < 15 || hour < 6 || hour > 18)
                    {
                        hour = validateInput
                        (
                                "The club must either"
                                        + "\n\t a) begin before school starts (8:00 and before)"
                                        + "\n\t b) begin during lunch time (between 12:52 and 13:15)"
                                        + "\n\t c) begin after school (afer 15:35)"
                                        + "\n\t NOTE: The school opens at 06:00 and closes at 18:00"
                                        + "\n Please input the hour the club starts at: "
                        );
                    }
                    
                    min = validateInput("The hour you have entered is " + hour + "\nPlease enter the minute of the hour the club starts at (between 00-59)");
                    if(hour == 8)
                    {
                        while(min > 30 || min < 0)     // while inputted time is > 8:30
                        {
                            min = validateInput("The time you entered was 8:" + min + "\nThis time is past the starting time for school (8:30)\nPlease enter a time that is before the start of school");
                        }
                    }
                    else if(hour == 12)
                    {
                        while(min < 52 || min >= 60)
                        {
                            min = validateInput("The time you entered was 12:" + min + "\nThis time is before lunch begins (12:52)\nPlease enter a time that is after the start of lunch");
                        }
                    }
                    else if(hour == 13)
                    {
                        while(min > 15 || min < 0)
                        {
                            min = validateInput("The time you entered was 13:" + min + "\nThis time is past the maximum club starting time at lunch (13:15)\nPlease enter a time that is before 13:15");
                        }
                    }
                    else if(hour == 15)
                    {
                        while(min < 35 || min >= 60)
                        {
                            min = validateInput("The time you entered was 15:" + min + "\nThis time is before school ends (15:35)\nPlease enter a time that is after the end of school");
                        }
                    }
                    else
                    {
                        while(min < 0 || min >= 60)
                        {
                            min = validateInput("The time you entered was " + hour + ":" + min + "\nPlease enter a valid minuted value");
                        }
                    }
                    
                    String start_time = Integer.toString(hour) + ":" + Integer.toString(min);
                    if(hour <= 8)
                        BLA = "B";
                    else if(hour >= 3)
                        BLA = "A";
                    else
                        BLA = "L";
                    
                    db.executeQuery
                    (
                            "INSERT INTO Club (ClubName, SubjectID, DayRunning, BLA, TeacherID, DepartmentID, StartTime) "
                            + "VALUES ('" + club_name + "', " + subjectID + ", " + day_running + ", '" + BLA + "', " + teacherID + ", " + depID + ", '" + start_time + "')"
                    );
                    
                    break;
                    
                case 11:    // remove a club
                    db.delete("Club");
                    break;

                case 12:    // remove all clubs running on a day
                    int day = 0;
                    while(day < 1 || day > 5)
                    {
                        day = validateInput
                        (
                                "Please input the day the club you want to remove is running on: "
                                + "\n\t 1 - Monday"
                                    + "\n\t 2 - Tuesday"
                                    + "\n\t 3 - Wednesday"
                                    + "\n\t 4 - Thursday"
                                    + "\n\t 5 - Friday"
                        );
                    }
                    db.executeQuery("DELETE FROM Club WHERE DayRunning = " + day);
                    break;

                case 13:    // remove all clubs running BLA
                    int BLA = 0;
                    while(BLA < 1 || BLA > 3)
                        BLA = validateInput("Please enter\n\t1 - if you want to delete clubs that run before school\n\t2 - if you want to delete clubs that run during lunch\n\t3 - if you want to delete clubs that run after school");
                    db.executeQuery("DELETE FROM Club WHERE BLA = '" + BLA + "'");
                    break;

                case 14:    // remove all clubs in a department
                    System.out.println("Please input the department of the associated clubs (enter \"list\" to list the departments in the database): ");
                    String dep = scanner.nextLine();
                    while(dep.equals("list"))
                    {
                        db.queryOutput("SELECT Department.DepartmentName FROM Department", "");
                        System.out.println("Please input the department of the associated clubs (enter \"list\" to list the departments in the database): ");
                        department = scanner.nextLine();
                    }
                    depID = db.getID(dep, "Department");
                    db.executeQuery("DELETE FROM Club WHERE DepartmentID = " + depID);
                    break;
            }
        }
        while(choice != 0);
    }
}
