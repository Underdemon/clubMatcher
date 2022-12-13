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
    private Scanner scanner = new Scanner(System.in);
    private String input = null;
    
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
                + "\n\t 5 - Search for all clubs in a department"
                + "\n\t 6 - Search for all clubs running on a specific day"
                + "\n\t 7 - Change what time a club happends (before school/during lunch/after school)"
                + "\n\t 8 - Change the day the club is running on"
                + "\n\t 9 - Change the teacher running the club"
                + "\n\t10 - Change the time the club starts"
                + "\n\t11 - Remove a club"
                + "\n\t12 - Remove all clubs running on a day"
                + "\n\t13 - Remove all clubs running before school/during lunch/after school"
                + "\n\t14 - Remove all clubs in a department"
            );
            
            switch(choice)
            {
                case 0:
                    return;

                case 1:
                    db.queryOutput
                    (
                                "SELECT Club.ClubName, Subjects.SubjectsName, Club.DayRunning, Club.BLA, Person.PersonName, Department.DepartmentName, Club.StartTime "
                                + "FROM Club "
                                + "INNER JOIN Subjects ON Club.SubjectID = Subjects.SubjectID "
                                + "INNER JOIN Department ON Club.DepartmentID = Department.DepartmentID "
                                + "INNER JOIN Teacher ON Club.TeacherID = Teacher.TeacherID "
                                + "INNER JOIN Person ON Teacher.PersonID = Person.PersonID"
                                , "Club"
                    );
                    break;

                case 2:
                    db.queryOutput
                    (
                            "SELECT Club.ClubName FROM Club"
                            , ""
                    );
                    break;

                case 3:
                    System.out.println("Please enter the name of the club you would like to see the details of: ");
                    input = scanner.nextLine();
                    db.queryOutput
                    (
                                "SELECT Club.ClubName, Subjects.SubjectsName, Club.DayRunning, Club.BLA, Person.PersonName, Department.DepartmentName, Club.StartTime "
                                + "FROM Club "
                                + "INNER JOIN Subjects ON Club.SubjectID = Subjects.SubjectID "
                                + "INNER JOIN Department ON Club.DepartmentID = Department.DepartmentID "
                                + "INNER JOIN Teacher ON Club.TeacherID = Teacher.TeacherID "
                                + "INNER JOIN Person ON Teacher.PersonID = Person.PersonID "
                                + "WHERE Club.ClubName = '" + input + "'"
                                , "Club"
                    );
                    break;

                case 4:
                    do
                    {
                        int choice = validateInput
                        (
                                "Please input the corresponding number to the time you want to search for: "
                                + "\n\t 1 - Before School"
                                + "\n\t 2 - During Lunch"
                                + "\n\t 3 - After School"
                        );
                    }
                    while(choice == 0);
                    
                    
                    break;

                case 5:
                    
                    break;

                case 6:
                    
                    break;

                case 7:
                    
                    break;

                case 8:
                    
                    break;

                case 9:
                    
                    break;

                case 10:
                    
                    break;

                case 11:
                    
                    break;

                case 12:
                    
                    break;

                case 13:
                    
                    break;

                case 14:
                    
                    break;
            }
        }
        while(choice != 0);
    }
}
