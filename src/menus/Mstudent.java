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
                + "\n\t 1 - Display all existing students"
                + "\n\t 2 - Add a new student"
                + "\n\t 3 - Remove a student"
            );
            
            switch(choice)
            {
                case 0:
                    return;
                case 1:
                    db.queryOutput("SELECT Person.PersonName FROM Student, Person WHERE Student.PersonID = Person.PersonID ORDER BY Person.PersonID ASC");
                    break;
                case 2:
                    db.insertStudentTeacher(true);
                    break;
                case 3:
                    break;
            }
        }
        while(choice != 0);
    }
}
