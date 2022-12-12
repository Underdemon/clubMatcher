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
public class MTeacher extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = null;
    
    public MTeacher()
    {
        do
        {
            choice = validateInput
            (
                "\n\t 0 - Exit to Main Menu"
                + "\n\t 1 - Display all existing teachers"
                + "\n\t 2 - Search for all teachers in an existing department"
                + "\n\t 3 - Add a new teacher"
                + "\n\t 4 - Remove a teacher"
                + "\n\t 5 - "
            );
            
            switch(choice)
            {
                case 0:
                    return;
                case 1:
                    db.queryOutput("SELECT Person.PersonName FROM Teacher, Person WHERE Teacher.PersonID = Person.PersonID ORDER BY Person.PersonID ASC");
                    break;
                case 2:
                    String department_name = null;
                    do
                    {
                        System.out.println("Please input the Department of the teacher (enter \"list\" to list the departments in the database):");
                        department_name = scanner.nextLine();

                        if(department_name.equals("list"))
                            db.queryOutput("SELECT Department.DepartmentName FROM Department");
                    }
                    while(department_name == null || department_name.equals("list"));
                    
                    db.queryOutput("SELECT Person.PersonName FROM Person "
                                    + "INNER JOIN Teacher ON Teacher.PersonID = Person.PersonID "
                                    + "INNER JOIN Department ON Teacher.DepartmentID = Department.DepartmentID "
                                    + "WHERE Department.DepartmentName = '" + department_name + "'");
                    break;
                case 3:
                    db.insertStudentTeacher(false);
                    break;
                case 4:
                    db.deleteStudentTeacher(false);
                    break;
            }
        }
        while(choice != 0);
    }
}
