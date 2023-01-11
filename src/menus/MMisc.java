package menus;

import java.util.Scanner;

public class MMisc extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = null;
    public MMisc()
    {
        do
        {
            choice = validateInput
                    (
                        "\n\t0 - Exit to Main Menu"
                        + "\n\t1 - Export Database Data to CSV"
                        + "\n\t2 - Add a user to the program"
                        + "\n\t3 - Remove a user from the program"
                        + "\n\t4 - Change your (" + username + "'s) password"
                    );
        }
        while(choice != 0);
    }
}