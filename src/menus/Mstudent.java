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
    private String name = null;
    
    public MStudent()
    {
        System.out.println("\n============\nSTUDENT MENU\n============\n");
        if(uac == 0)
            studentMenu();
        else
            adminMenu();
    }

    @Override
    public void studentMenu()
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
                                    + "\n\t 5 - List a student's subjects"
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
                    System.out.println("Please enter the name of the student you wish to list the subjects of: ");
                    input = scanner.nextLine();
                    db.queryOutput
                            (
                                    "SELECT Person.PersonName, Subjects.SubjectsName FROM StudentSubjects "
                                            + "INNER JOIN Student ON StudentSubjects.StudentID = Student.StudentID "
                                            + "INNER JOIN Person ON Student.PersonID = Person.PersonID "
                                            + "INNER JOIN Subjects ON StudentSubjects.SubjectsID = Subjects.SubjectsID "
                                            + "WHERE Person.PersonName = '" + input + "'"
                                    , "StudentSubjects"
                            );
                    break;
            }
        }
        while(choice != 0);
    }

    @Override
    public void adminMenu()
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
                                    + "\n\t 8 - List a student's subjects"
                                    + "\n\t 9 - Add a student's subject"
                                    + "\n\t10 - Remove a student's subject"
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
                    int ID = db.getID(input, "Person");
                    while(ID == 0)
                    {
                        System.out.println("The student does not exist in the database\n"
                                + "Please input the name of the student you wish to search for, or type \"back\" to exit to the main menu");
                        input = scanner.nextLine();
                        if(input.equals("back"))
                            return;
                        ID = db.getID(input, "Person");
                    }

                    int newYearGroup = 0;
                    while(newYearGroup < 7 || newYearGroup > 13)
                        newYearGroup = validateInput("\"Please enter the Year Group you wish to change the student to: ");

                    db.executeQuery
                            (
                                    "UPDATE Student SET YearGroup = " + newYearGroup + " WHERE Student.PersonID = " + ID
                            );
                    break;

                case 8:
                    System.out.println("Please enter the name of the student you wish to list the subjects of: ");
                    input = scanner.nextLine();
                    db.queryOutput
                            (
                                    "SELECT Person.PersonName, Subjects.SubjectsName FROM StudentSubjects "
                                            + "INNER JOIN Student ON StudentSubjects.StudentID = Student.StudentID "
                                            + "INNER JOIN Person ON Student.PersonID = Person.PersonID "
                                            + "INNER JOIN Subjects ON StudentSubjects.SubjectsID = Subjects.SubjectsID "
                                            + "WHERE Person.PersonName = '" + input + "'"
                                    , "StudentSubjects"
                            );
                    break;

                case 9:
                    System.out.println("Please enter the name of the student you wish to add a subject to: ");
                    name = scanner.nextLine();
                    System.out.println("Please input the name of the subject you wish to add (type \"list\" to list the subjects): ");
                    input = scanner.nextLine();
                    while(input.equals("list"))
                    {
                        db.queryOutput("SELECT Subjects.SubjectsName FROM Subjects", "Subjects");
                        System.out.println("Please input the name of the subject you wish to add (type \"list\" to list the subjects): ");
                        input = scanner.nextLine();
                    }
                    db.executeQuery("INSERT INTO StudentSubjects VALUES(" + db.getID(name, "Student") + ", " + db.getID(input, "Subjects") + ")");
                    break;

                case 10:
                    System.out.println("Please enter the name of the student you wish to remove the subjects of: ");
                    name = scanner.nextLine();
                    System.out.println("Please input the name of the subject you wish to remove (type \"list\" to list the subjects of the student): ");
                    input = scanner.nextLine();
                    while(input.equals("list"))
                    {
                        db.queryOutput
                                (
                                        "SELECT Person.PersonName, Subjects.SubjectsName FROM StudentSubjects "
                                                + "INNER JOIN Student ON StudentSubjects.StudentID = Student.StudentID "
                                                + "INNER JOIN Person ON Student.PersonID = Person.PersonID "
                                                + "INNER JOIN Subjects ON StudentSubjects.SubjectsID = Subjects.SubjectsID "
                                                + "WHERE Person.PersonName = '" + name + "'"
                                        , "StudentSubjects"
                                );
                        System.out.println("Please input the name of the subject you wish to add (type \"list\" to list the subjects of the student): ");
                        input = scanner.nextLine();
                    }
                    db.executeQuery("DELETE FROM StudentSubjects WHERE StudentSubjects.StudentID = " + db.getID(name, "Student") + " AND StudentSubjects.SubjectsID = " + db.getID(input, "Subjects"));
                    break;
            }
        }
        while(choice != 0);
    }
}
