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
                + "\n\t 2 - Add a new teacher"
                + "\n\t 3 - Remove a teacher"
            );
            
            switch(choice)
            {
                case 0:
                    return;
                case 1:
                    db.queryOutput("SELECT Person.PersonName FROM Teacher, Person WHERE Teacher.PersonID = Person.PersonID ORDER BY Person.PersonID ASC");
                    break;
                case 2:
                    db.insertStudentTeacher(false);
                    break;
                case 3:
                    break;
            }
        }
        while(choice != 0);
    }
}
