package menus;

import databaseConnect.DatabaseConnect;
import encryption_api.REST_API_Connect;

import java.io.Console;
import java.util.Scanner;
import java.util.logging.Logger;

public class MMisc extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = null;
    private String name = null;
    private String password = null;
    private String isAdmin = null;
    private static DatabaseConnect usersDB = null;
    public MMisc()
    {
        Logger log = Logger.getLogger(MMisc.class.getName());
        REST_API_Connect parser = new REST_API_Connect();
        usersDB = new DatabaseConnect("Users.db");
        do
        {
            choice = validateInput
                    (
                            "\n\t0 - Exit to Main Menu"
                                    + "\n\t1 - Export Database Data to CSV"
                                    + "\n\t2 - Import Database Data from CSVs"
                                    + "\n\t3 - Add a user to the program"
                                    + "\n\t4 - Remove a user from the program"
                                    + "\n\t5 - Change your (" + username + "'s) password"
                                    + "\n\t6 - Output your (" + username + "'s) password"
                                    + "\n\t7 - Output all usernames in the database"
                    );

            switch (choice)
            {
                case 0:
                    return;

                case 1:

                    break;

                case 2:

                    break;

                case 3:
                    System.out.println("Please enter the username of the user you want to add: ");
                    name = scanner.nextLine();
                    System.out.println("Please enter the password of the user you want to add: ");
                    password = scanner.nextLine();
                    System.out.println("Will this user be an admin (answer with Y/N or 1/0): ");
                    isAdmin = scanner.nextLine();

                    if (isAdmin.equals("Y") || isAdmin.equals("y") || isAdmin.equals("1"))
                        usersDB.executeQuery("INSERT INTO Users VALUES(' " + name + " ', ' " + parser.encrypt(parser.encrypt(password, 1), 2) + " ', 1)");
                    else
                        usersDB.executeQuery("INSERT INTO Users VALUES(' " + name + " ', ' " + parser.encrypt(parser.encrypt(password, 1), 2) + " ', 0)");
                    break;

                case 4:
                    System.out.println("Please enter the username of the user you want to remove: ");
                    name = scanner.nextLine();
                    usersDB.executeQuery("DELETE FROM Users WHERE Username = '" + name + "'");
                    break;

                case 5:
                    System.out.println("Please input the new password: ");
                    password = scanner.nextLine();
                    usersDB.executeQuery("UPDATE Users SET Password = '" + parser.encrypt(parser.encrypt(password, 1), 2) + "' WHERE Username = '" + username + "'");
                    break;

                case 6:
                    System.out.println(parser.encrypt(parser.encrypt(usersDB.getCurrentUserPassword(username), 3), 1));
                    break;

                case 7:
                    usersDB.queryOutput("SELECT Users.Username FROM Users", "Users");
                    break;
            }
        }
        while (choice != 0);
    }
}