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
    private String input = "";
    private int subject = 0;
    private int subject2 = 0;
    private int weight = 0;
    private int table_size = 0;
    
    public MSubjects()
    {
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
                                    + "\n\t 1 - Display all existing subjects"
                                    + "\n\t 2 - Search for a subject"
                                    + "\n\t 3 - View all graph connections"
                    );

            switch(choice)
            {
                case 0:
                    return;

                case 1:
                    db.queryOutput("SELECT Subjects.SubjectsName FROM Subjects", "");
                    break;

                case 2:
                    System.out.println("Please input the subject you want to search for in the table");
                    input = scanner.nextLine();
                    db.queryOutput("SELECT Subjects.SubjectsName FROM Subjects WHERE Subjects.SubjectsName = '" + input + "'", "");
                    break;

                case 3:
                    db.queryOutput("SELECT * FROM SubjectGraph", "SubjectGraph");
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
                                    + "\n\t 1 - Display all existing subjects"
                                    + "\n\t 2 - Add a new subject"      // add graph connections to subjects
                                    + "\n\t 3 - Remove a subject"       // remove all graph connections from subject graph
                                    + "\n\t 4 - Search for a subject"
                                    + "\n\t 5 - View all graph connections"
                                    + "\n\t 6 - Add a graph connection"
                                    + "\n\t 7 - Remove a graph connection"
                                    + "\n\t 8 - Change the weight between 2 subjects"
                    );

            switch(choice)
            {
                case 0:
                    return;

                case 1:
                    db.queryOutput("SELECT Subjects.SubjectsName FROM Subjects", "");
                    break;

                case 2:
                    System.out.println("Please enter the name of the subject you wish to add: ");
                    input = scanner.nextLine();
                    db.executeQuery("INSERT INTO Subjects (SubjectsName) VALUES ('" + input + "')");
                    String input2 = "";
                    int ID = db.getID(input, "Subjects");
                    table_size = db.table_len("Subjects");

                    while(!input2.equals("stop") && !input2.equals("s") && !input2.equals("0"))
                    {
                        subject = validateInput("Please select the corresponding number for the subject you want to create a connection between (or type \"0\" to list all the subjects): ");
                        while(subject <= 0 || subject >= table_size)
                        {
                            if(subject == 0)
                                db.queryOutput("SELECT * FROM Subjects", "Subjects");

                            subject = validateInput("Please select the corresponding number for the subject you want to create a connection between (or type \"0\" to list all the subjects): ");
                        }

                        weight = validateInput
                                (
                                        "Please input the weight you want between " + input + " and " + db.getName(subject, "Subjects")
                                                + "\nWeights should be between 0-10, with 0 being the least related and 10 being the most related: "
                                );

                        while(weight < 0 || weight > 10)
                        {
                            weight = validateInput
                                    (
                                            "Please input a valid weight"
                                                    + "\nWeights should be between 0-10, with 0 being the least related and 10 being the most related: "
                                    );
                        }

                        db.executeQuery("INSERT INTO SubjectGraph VALUES (" + ID + ", " + subject + "," + weight + ")");
                        db.executeQuery("INSERT INTO SubjectGraph VALUES (" + subject + ", " + ID + "," + weight + ")");

                        System.out.println("Please type \"stop\" to stop entering subject connections\nOtherwise, input anything else to continue: ");
                        input2 = scanner.nextLine();
                    }
                    break;

                case 3:
                    db.queryOutput("SELECT * FROM Subjects", "");
                    subject = validateInput("Please enter the ID corresponding to the subject you want to delete");

                    table_size = db.table_len("Subjects");
                    while(subject <= 0 || subject > table_size)
                    {
                        System.out.println("Invalid ID entered");
                        subject = validateInput("Please enter the ID corresponding to the subject you want to delete");
                    }

                    db.executeQuery("DELETE FROM SubjectGraph WHERE StartVertex = " + subject + " OR EndVertex = " + subject);
                    db.executeQuery("DELETE FROM Subjects WHERE Subjects.SubjectsID = " + subject);

                    break;

                case 4:
                    System.out.println("Please input the subject you want to search for in the table");
                    input = scanner.nextLine();
                    db.queryOutput("SELECT Subjects.SubjectsName FROM Subjects WHERE Subjects.SubjectsName = '" + input + "'", "");
                    break;

                case 5:
                    db.queryOutput("SELECT * FROM SubjectGraph", "SubjectGraph");
                    break;

                case 6:
                    subject = validateInput("Please input a subject ID that corresponds to the the first subject in the connection (or type \"list\" to list the current graph connections)");
                    table_size = db.table_len("Subjects");
                    while(subject < 1 || subject > table_size)
                    {
                        if(subject == 0)
                            db.queryOutput("SELECT * FROM SubjectGraph", "SubjectGraph");
                        subject = validateInput("Please input a subject ID that corresponds to the the first subject in the connection (or type \"list\" to list the current graph connections)");
                    }

                    subject2 = validateInput("Please input a subject ID that corresponds to the the second subject in the connection (or type \"list\" to list the current graph connections)");
                    while(subject2 < 1 || subject2 > table_size || subject == subject2)
                    {
                        if(subject2 == 0)
                            db.queryOutput("SELECT * FROM SubjectGraph", "SubjectGraph");
                        else if(subject == subject2)
                            System.out.println("The subject cannot have a connection to itself");
                        subject2 = validateInput("Please input a subject ID that corresponds to the the second subject in the connection (or type \"list\" to list the current graph connections)");
                    }

                    weight = validateInput("Please input the weight to be added between the subjects (it should be between 0-10): ");
                    while(weight < 0 || weight > 10)
                        weight = validateInput("Invalid weight\nPlease input a weight between 0-10: ");

                    db.executeQuery("INSERT INTO SubjectGraph VALUES(" + subject + ", " + subject2 + ", " + weight + ")");
                    db.executeQuery("INSERT INTO SubjectGraph VALUES(" + subject2 + ", " + subject + ", " + weight + ")");

                    break;

                case 7:
                    subject = validateInput("Please input a subject ID that corresponds to the the first subject in the connection (or type \"list\" to list the current graph connections)");
                    table_size = db.table_len("Subjects");
                    while(subject < 1 || subject > table_size)
                    {
                        if(subject == 0)
                            db.queryOutput("SELECT * FROM SubjectGraph", "SubjectGraph");
                        subject = validateInput("Please input a subject ID that corresponds to the the first subject in the connection (or type \"list\" to list the current graph connections)");
                    }

                    subject2 = validateInput("Please input a subject ID that corresponds to the the second subject in the connection (or type \"list\" to list the current graph connections)");
                    while(subject2 < 1 || subject2 > table_size || subject == subject2)
                    {
                        if(subject2 == 0)
                            db.queryOutput("SELECT * FROM SubjectGraph", "SubjectGraph");
                        else if(subject == subject2)
                            System.out.println("The subject cannot have a connection to itself");
                        subject2 = validateInput("Please input a subject ID that corresponds to the the second subject in the connection (or type \"list\" to list the current graph connections)");
                    }

                    db.executeQuery("DELETE FROM SubjectGraph WHERE SubjectGraph.StartVertex = " + subject + " AND SubjectGraph.EndVertex = " + subject2);
                    break;

                case 8:
                    subject = validateInput("Please input a subject ID that corresponds to the the first subject in the connection (or type \"0\" to list the current graph connections)");
                    table_size = db.table_len("Subjects");
                    while(subject < 1 || subject > table_size)
                    {
                        if(subject == 0)
                            db.queryOutput("SELECT * FROM Subjects", "Subjects");
                        subject = validateInput("Please input a subject ID that corresponds to the the first subject in the connection (or type \"0\" to list the current graph connections)");
                    }

                    subject2 = validateInput("Please input a subject ID that corresponds to the the second subject in the connection (or type \"0\" to list the current graph connections)");
                    while(subject2 < 1 || subject2 > table_size || subject == subject2)
                    {
                        if(subject2 == 0)
                            db.queryOutput("SELECT * FROM Subjects", "Subjects");
                        else if(subject == subject2)
                            System.out.println("The subject cannot have a connection to itself");
                        subject2 = validateInput("Please input a subject ID that corresponds to the the second subject in the connection (or type \"0\" to list the current graph connections)");
                    }

                    System.out.println("Data about selected subjects: ");
                    db.queryOutput("SELECT * FROM SubjectGraph WHERE StartVertex = " + subject + " AND EndVertex = " + subject2, "SubjectGraph");

                    weight = validateInput("Please input what you want to change the weight to between the subjects (it should be between 0-10): ");
                    while(weight < 0 || weight > 10)
                        weight = validateInput("Invalid weight\nPlease input a weight between 0-10: ");

                    db.executeQuery("UPDATE SubjectGraph SET Weight = " + weight + " WHERE StartVertex = " + subject + " AND EndVertex = " + subject2);
                    db.executeQuery("UPDATE SubjectGraph SET Weight = " + weight + " WHERE StartVertex = " + subject2 + " AND EndVertex = " + subject);
                    break;
            }
        }
        while(choice != 0);
    }
}
