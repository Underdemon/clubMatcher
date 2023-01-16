/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import java.util.Scanner;

/**
 *
 * @author rayan
 */
public class MAttendance extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = "";
    
    public MAttendance()
    {
        System.out.println("\n===============\nATTENDANCE VIEW\n===============\n");

        db.executeQuery("DROP VIEW IF EXISTS Attendance");
        
        db.executeQuery
        (
            "CREATE VIEW IF NOT EXISTS Attendance AS "
            + "SELECT Person.PersonName, ClubLog.Timestamp, ClubLog.Date, Club.ClubName, Club.StartTime, "
            + "((strftime('%s', Club.StartTime) - strftime('%s', ClubLog.Timestamp)) * 1.0) / 60 AS time_delta "
            + "FROM ClubLog "
            + "INNER JOIN Student ON Student.StudentID = ClubLog.StudentID "
            + "INNER JOIN Person ON Student.PersonID = Person.PersonID "
            + "INNER JOIN Club ON ClubLog.ClubID = Club.ClubID"
        );
        
        do
        {
            choice = validateInput
            (
                "\n\t 0 - Exit to ClubLog Menu"
                + "\n\t 1 - View all lates ordered by lateness"
                + "\n\t 2 - View the total number of lates"
                + "\n\t 3 - View the attendance of a specific student"
                + "\n\t 4 - Show the number of lates of a specific student"
            );
            
            switch(choice)
            {
                case 0:
                    return;
                    
                case 1:
                    db.queryOutput
                    (
                            "SELECT Attendance.PersonName, Attendance.ClubName, Attendance.Timestamp, Attendance.StartTime, Attendance.time_delta, Attendance.Date "
                            + "FROM Attendance WHERE Attendance.time_delta > 5 OR Attendance.time_delta < -5 "
                            + "ORDER BY ABS(time_delta) ASC", ""
                    );
                    break;
                    
                case 2:
                    db.queryOutput("SELECT COUNT(*) FROM Attendance WHERE Attendance.time_delta > 5 OR Attendance.time_delta < -5", "");
                    break;
                    
                case 3:
                    System.out.println("Please enter the name of the student you want to view the attendance of: ");
                    input = scanner.nextLine();
                    db.queryOutput("SELECT * FROM Attendance WHERE Attendance.PersonName = '" + input + "'", "");
                    break;
                    
                case 4:
                    System.out.println("Please enter the name of the student you want to view the number of lates of: ");
                    input = scanner.nextLine();
                    db.queryOutput("SELECT COUNT(*) FROM Attendance WHERE (Attendance.time_delta > 5 OR Attendance.time_delta < -5) AND Attendance.PersonName = '" + input + "'", "");
                    break;
            }
        }
        while(choice != 0);
    }
}
