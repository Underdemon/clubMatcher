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
public class MDepartment extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = null;
    
    public MDepartment()
    {
        choice = validateInput
        (
            "\n\t 0 - Exit to Main Menu"
            + "\n\t 1 - Display all existing departments"
            + "\n\t 2 - Add a new department"
            + "\n\t 3 - Remove a department"
        );
        
        switch(choice)
        {
            case 0:
                return;
            case 1:
                db.query("SELECT * FROM Department");
                break;
            case 2:
                //System.out.println("Please input the name of the department you want to add: ");
                //input = scanner.nextLine();
                db.insert("Department");
                break;
            case 3:
                break;
        }
    }
}
