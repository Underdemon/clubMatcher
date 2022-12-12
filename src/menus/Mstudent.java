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
public class MStudent extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = null;
    
    public MStudent()
    {
        do
        {
            choice = validateInput
            (
                "\n\t 0 - Exit to Main Menu"
                + "\n\t 1 - Display all exisiting students with their year group"
                + "\n\t 2 - Display all existing students"
                + "\n\t 3 - Display all students in a year group"
                + "\n\t 4 - Search for a specific student's details"
                + "\n\t 5 - Add a new student"
                + "\n\t 6 - Remove a student"
                + "\n\t 7 - Change the year group of a student"
            );
            
            switch(choice)
            {
                case 0:
                    return;
                    
                case 1:
                    db.queryOutput("SELECT Person.PersonName, Student.YearGroup FROM Student, Person WHERE Student.PersonID = Person.PersonID ORDER BY Person.PersonID ASC", "Student");
                    break;
                    
                case 2:
                    db.queryOutput("SELECT Person.PersonName FROM Student, Person WHERE Student.PersonID = Person.PersonID ORDER BY Person.PersonID ASC", "");
                    break;
                    
                case 3:
                    int year = 0;
                    year = validateInput("Please input the year group you want to search for (Year 7 - Year 13)");
                    while(year < 7 || year > 13)
                    {
                        System.out.println("Please input a valid year group (Year 7 - Year 13)");
                        year = validateInput("Please input the year group you want to search for (Year 7 - Year 13)");
                    }
                    db.queryOutput
                    (
                            "SELECT Person.PersonName FROM Student "
                            + "INNER JOIN Person ON Person.PersonID = Student.PersonID "
                            + "WHERE Student.YearGroup = " + year, ""
                    );
                    break;
                    
                case 4:
                    System.out.println("Please input the name of the student you wish to search for: ");
                    input = scanner.nextLine();
                    db.queryOutput
                    (
                            "SELECT Person.PersonName, Student.YearGroup FROM Student "
                            + "INNER JOIN Person ON Student.PersonID = Person.PersonID "
                            + "WHERE Person.PersonName = '" + input + "'", "Student"
                    );
                    break;
                    
                case 5:
                    db.insertStudentTeacher(true);
                    break;
                    
                case 6:
                    db.deleteStudentTeacher(true);
                    break;
                    
                case 7:
                    System.out.println("Please input the name of the student you wish to search for: ");
                    input = scanner.nextLine();
                    int ID = db.getNameID(input);
                    while(ID == 0)
                    {
                        System.out.println("The student does not exist in the database\n"
                        + "Please input the name of the student you wish to search for, or type \"back\" to exit to the main menu");
                        input = scanner.nextLine();
                        if(input.equals("back"))
                            return;
                        ID = db.getNameID(input);
                    }
                    
                    int newYearGroup = 0;
                    while(newYearGroup < 7 || newYearGroup > 13)
                        newYearGroup = validateInput("\"Please enter the Year Group you wish to change the student to: ");
                    
                    db.executeQuery
                    (
                            "UPDATE Student SET YearGroup = " + newYearGroup + " WHERE Student.PersonID = " + ID
                    );
                    break;
            }
        }
        while(choice != 0);
    }
}
