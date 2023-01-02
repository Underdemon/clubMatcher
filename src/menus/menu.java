/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menus;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author rayan
 */
public abstract class Menu
{  
    final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    
    public boolean isNumeric(String str)
    {
        if(str.equals(null))
            return false;
               
        /*
        -?          determines if number starts with minus
        \d+         matches one or more digits
        (\.\d+)?    matches for decimal point and digits following
        */
        
        return pattern.matcher(str).matches();
    }
    
    public static int validateInput(String message)
    {
        Scanner scanner = new Scanner(System.in);
        String inputChoice;
        int choice = 0;
        
        System.out.println(message);
        inputChoice = scanner.nextLine();
        try
        {
            //wrapping parseInt in try catch in case an int was not entered
            choice = Integer.parseInt(inputChoice);
        } 
        catch (NumberFormatException x)
        {
            //provides details on the exception that is thrown and also provides a more concise, readable error message
            System.out.println("\n" + x + "\n\nYou have entered an illegal value, please provide a valid input\n");
            return validateInput(message);
        }
        return choice;
    }
}
