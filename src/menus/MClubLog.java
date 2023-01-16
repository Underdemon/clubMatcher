/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menus;

import dataStructures.dll.DLL;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author rayan
 */
public class MClubLog extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = "";
    private String club_name = null;
    
    public MClubLog()
    {
        System.out.println("\n=============\nCLUB LOG MENU\n=============\n");

        do
        {
            choice = validateInput
            (
                "\n\t 0 - Exit to Main Menu"
                + "\n\t 1 - Output the Club Log"
                + "\n\t 2 - Sign student(s) into a club"
                + "\n\t 3 - Enter Attendance Menu"
                + "\n\t 4 - View the top 3 students with the highest club attendance"
                + "\n\t 5 - View the top 3 clubs with the highest attendance"
            );
            
            switch(choice)
            {
                case 1:
                    db.queryOutput("SELECT * FROM ClubLog", "ClubLog");
                    break;
                    
                case 2:
                    do
                    {
                        System.out.println("Please input the name of the club (enter \"list\" to list the club names stored in the database):");
                        club_name = scanner.nextLine();

                        if(club_name.equals("list"))
                            db.queryOutput("SELECT Club.ClubName FROM Club", "");
                    }
                    while(club_name == null || club_name.equals("list"));
                    
                    
                    DLL<String> students = new DLL();
                    System.out.println
                    (
                        "(type \"list\" to list the students in the database)"
                        + "\ntype \"exit\" or \"e\" when you have inputted all the students you want to add: "
                    );
                    input = scanner.nextLine();
                    while(!(input.equals("exit") || input.equals("e")))
                    {
                        if(input.equals("list"))
                            db.queryOutput("SELECT Person.PersonName FROM Student INNER JOIN Person ON Student.PersonID = Person.PersonID", "");
                        else
                            students.append(input);
                        
                        System.out.println("Please input a student, type \"list\" to list students or type \"exit\" or \"e\" when you have inputted all the students you want to add: ");
                        input = scanner.nextLine();
                    }
                    
                    
                    for(String s : students)
                    {
                        db.executeQuery("INSERT INTO ClubLog VALUES('" + LocalTime.now().toString() + "', '" + LocalDate.now().toString() + "', " + db.getID(s, "Student") + ", " + db.getID(club_name, "Club") + ")");
                    }
                    break;
                    
                case 3:
                    MAttendance attendance = new MAttendance();
                    break;
                    
                case 4:
                    db.queryOutput
                    (
                            "SELECT Person.PersonName "
                            + "FROM ClubLog "
                            + "INNER JOIN Student ON Student.StudentID = ClubLog.StudentID "
                            + "INNER JOIN Person ON Student.PersonID = Person.PersonID "
                            + "GROUP BY ClubLog.StudentID ORDER BY COUNT(*) DESC LIMIT 3 "
                            , ""
                    );
                    break;
                    
                case 5:
                    db.queryOutput
                    (
                            "SELECT Club.ClubName "
                            + "FROM ClubLog "
                            + "INNER JOIN Club ON ClubLog.ClubID = Club.ClubID "
                            + "GROUP BY ClubLog.ClubID ORDER BY COUNT(*) DESC LIMIT 3"
                            , ""
                    );
            }
        }
        while(choice != 0);
    }
}
