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
public class MClubLog extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = null;
    
    public MClubLog()
    {
        do
        {
            choice = validateInput
            (
                "\n\t 0 - Exit to Main Menu"
                + "\n\t 1 - Output the Club Log"
                + "\n\t 2 - Sign a student into a club"
                + "\n\t 3 - Sign in all students into a club"
                + "\n\t 4 - View all lates"
                + "\n\t 5 - View a specific student's attendace"
                + "\n\t 6 - Show the number of lates of a specific student"
                + "\n\t 7 - View students with the highest club attendance"
            );
            
            switch(choice)
            {
                case 1:
                    db.queryOutput("", "ClubLog");
                    
                case 2:
                    break;
                    
                case 3:
                    break;
                    
                case 4:
                    break;
                    
                case 5:
                    break;
                    
                case 6:
                    break;
            }
        }
        while(choice != 0);
    }
}
