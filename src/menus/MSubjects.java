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
public class MSubjects extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = null;
    
    public MSubjects()
    {
        do
        {
            choice = validateInput
            (
                "\n\t 0 - Exit to Main Menu"
                + "\n\t 1 - Display all existing subjects"
                + "\n\t 2 - Add a new subject"
                + "\n\t 3 - Remove a subject"
            );
            
            switch(choice)
            {
                case 0:
                    return;
                case 1:
                    db.queryOutput("SELECT Subjects.SubjectName FROM Subjects");
                    break;
                case 2:
                    //System.out.println("Please input the name of the department you want to add: ");
                    //input = scanner.nextLine();
                    db.insert("Subject");
                    break;
                case 3:
                    System.out.println("\nThe fields in the subject table are:");
                    db.queryOutput("SELECT Subjects.SubjectName FROM Subjects");
                    db.delete("Subjects");
                    break;
            }
        }
        while(choice != 0);
    }
}
