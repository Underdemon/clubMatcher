/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clubmatcher;

import algorithms.dijkstra.Dijkstra;
import algorithms.hungarian.Hungarian;
import dataStructures.dll.DLL;
import dataStructures.graphs.Graph;
import dataStructures.hashmap.Pair;
import databaseConnect.DatabaseConnect;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import encryption_api.REST_API_Connect;
import menus.*;

/**
 *
 * @author rayan
 */
public class ClubMatcher extends Menu implements Runnable
{
    protected static DatabaseConnect db = null;
    protected static int uac;
    protected static String username;
    protected static String encrypted_pass;
    protected static String plaintext_pass;
    public void run()
    {
        db = new DatabaseConnect("clubMatcher.db");
    }

    public static int login()
    {
        int hasLoggedIn = -1;
        Scanner scanner = new Scanner(System.in);
        REST_API_Connect parser = new REST_API_Connect();
        // System.out.println(parser.encrypt(parser.encrypt("Nj80XzViNXNgRVg=", 3),1 ));
        System.out.println("Please input your username: ");
        username = scanner.nextLine();
        System.out.println("Please input your password: ");
        plaintext_pass = scanner.nextLine();
        encrypted_pass = parser.encrypt(parser.encrypt(plaintext_pass, 1), 2);
        System.out.print("Connecting to database");
        DatabaseConnect users_db = new DatabaseConnect("users.db");
        if(users_db.correctUser(username, encrypted_pass))
        {
            System.out.println("\n");
            if (users_db.getUACLevel(username, encrypted_pass) == 0)
            {
                System.out.println("student " + username + " has logged in");
                hasLoggedIn = 0;
            }
            else
            {
                System.out.println("admin " + username + " has logged in");
                hasLoggedIn = 1;
            }
            System.out.println("\n");
        }
        users_db.close();
        return hasLoggedIn;
    }

    public DLL<String> returnFileNamesInDirectory(File[] files, int index, int level, DLL<String> names)
    {
        if(index == files.length)   // recursion base case (if there are no more files in the directory)
            return names;

        if(files[index].isFile())   // checks if the object is a file
            names.append(files[index].getName());

        returnFileNamesInDirectory(files, index + 1, level, names);

        return null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
//        int[][] m = new int[3][3];
//        m[0][0] = 95;
//        m[0][1] = 33;
//        m[0][2] = 88;
//
//        m[1][0] = 98;
//        m[1][1] = 72;
//        m[1][2] = 82;
//
//        m[2][0] = 44;
//        m[2][1] = 2;
//        m[2][2] = 24;
//
//        Hungarian h = new Hungarian(m);
//        int sum = 0;
//        for(Pair<Integer, Integer> pair : h.optimalAssignment())
//        {
//            System.out.println("Value: " + h.getOriginal_matrix()[pair.getValue()][pair.getKey()]);
//            sum += h.getOriginal_matrix()[pair.getValue()][pair.getKey()];
//        }
//
//        System.out.println("TOTAL COST: " + sum);



















//        Graph<String> graph = new Graph<>();
//        graph.add("Maths", "Physics", 2);
//        graph.add("Maths", "Further Maths", 1);
//        graph.add("Physics", "Maths", 2);
//        graph.add("Physics", "Comp Sci", 6);
//        graph.add("Physics", "Music", 4);
//        graph.add("Further Maths", "Maths", 1);
//        graph.add("Music", "Comp Sci", 1);
//        graph.add("Music", "Physics", 4);
//        graph.add("Comp Sci", "Music", 1);
//        graph.add("Comp Sci", "Physics", 6);
//
//        Dijkstra dijkstra = new Dijkstra();
//        DLL<Pair<String, Integer>> dijk = dijkstra.shortestPath("Maths", graph, true);
//        for(int i = 0; i < dijk.getLen(); i++)
//        {
//            System.out.println(dijk.returnAtIndex(i).getKey() + ": " + dijk.returnAtIndex(i).getValue());
//        }







        uac = login();
        if(uac == -1)
        {
            System.out.println("Failed to login. Exiting program...");
            System.exit(0);
        }

        String temp;
        Scanner scanner = new Scanner(System.in);
        Thread db_thread = new Thread(new ClubMatcher());
        System.out.print("Connecting to database");
        
        db_thread.start();
        while(db_thread.isAlive())
        {
            System.out.print(".");
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(ClubMatcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println("\n\n============ CLUB MATCHER PROGRAM ============\n\n");
        try
        {
            Thread.sleep(250);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ClubMatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        clrscreen();    // cls command cannot be executed in the IDE, only in the OS terminal
        
        System.out.println("Load Database Values?\nY/N: ");
        temp = scanner.nextLine().toUpperCase();
        if(temp.equals("Y") || temp.equals("YES") || temp.equals(Integer.toString(1)))
        {
            db.close();
            File file = new File("clubMatcher.db");
            file.delete();
            db.reconnect("clubMatcher");

            String selectedFolder = null;
            File f  = new File(".\\src\\databaseConnect\\data");
            File[] backupsList = f.listFiles(); // gets a list of all files and folders in the directory

            int choice = -1;
            while(choice < 0 || choice > backupsList.length + 1)
            {
                System.out.println("\nPlease input the number corresponding to the backup you wish to restore\n0: restore the latest backup");
                for (int i = 0; i < backupsList.length; i++)
                    System.out.println((i + 1) + ": " + backupsList[i].getName());
                choice = validateInput("");
            }

            if(choice == 0)
            {
                Arrays.sort(backupsList, LastModifiedFileComparator.LASTMODIFIED_REVERSE);  // sorts the files by last modified
                selectedFolder = backupsList[0].getName();
            }
            else
                selectedFolder = backupsList[choice - 1].getName();


            String ddlPath = ".\\src\\databaseConnect\\DDLs";
            String csvPath = ".\\src\\databaseConnect\\data\\" + selectedFolder;
            File ddl = new File(ddlPath);
            File csv = new File(csvPath);

            File[] filesDDL = ddl.listFiles();
            for(int i = 0; i < filesDDL.length; i++)
                db.createTable(filesDDL[i].getPath());

            File[] filesCSV = csv.listFiles();
            for (File value : filesCSV)
            {
                String s = value.getName();
                db.insert(s.substring(0, s.lastIndexOf('.')), csvPath + "\\" + s);
            }
            
            System.out.println("\nLoaded!");
        }

        /*
            the student/admin menu functions will be overridden for each menu subclass
            java does not allow for overriding static methods at runtime since method overriding is dynamically bounded  (method call is bounded to method body at runtime)
            this means the menu functions cannot be static
            however, static methods are statically bounded at compile-time (static methods resolved before any objects instantiated)
            since the main function is static and thereby statically bounded, the student/admin menu functions cannot be referenced from the static context of the program's main function
            in order to circumvent this, I create a new instance of the main class the program is running from (ClubMatcher)
            since static fields and methods are shared with all instances and is connected to the class itself, the main class is also shared with the newly instantiated clubMatcher class
            however, the non-static functionality (i.e. the student and admin menus) is now accessible to the new instance of the class and so the function can be called
             */
        // outputs the correct menu for the user access level
        if(uac == 0)
            new ClubMatcher().studentMenu();
        else
            new ClubMatcher().adminMenu();
    }
    
    /**
     * clears the screen
     */
    public static void clrscreen()
    {
        try
        {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        }
        catch (IOException | InterruptedException ex) {}
    }

    @Override
    public void studentMenu()
    {
        int choice = 0;

        do
        {
            System.out.println("\n=================\nMAIN MENU OPTIONS\n=================\n");
            choice = validateInput
                    (
                            "\n\nPlease input the number corresponding to the option you want to choose:"
                            + "\n\t0 - Exit the program"
                            + "\n\t1 - Student Operations"  // includes student subjects
                            + "\n\t2 - Department Operations"
                            + "\n\t3 - Teacher Operations"
                            + "\n\t4 - Subject Operations"  // includes subject graph
                            + "\n\t5 - Club Operations"
                            + "\n\t6 - Club Log Operations"
                            + "\n\t7 - Club Recommendation"
                    );

            switch(choice)
            {
                case 1:
                    MStudent student = new MStudent();
                    break;
                case 2:
                    MDepartment department = new MDepartment();
                    break;
                case 3:
                    MTeacher teacher = new MTeacher();
                    break;
                case 4:
                    MSubjects subject = new MSubjects();
                    break;
                case 5:
                    MClub club = new MClub();
                    break;
                case 6:
                    MClubLog log = new MClubLog();
                    break;
                case 7:
                    db.close();
                    MRecommendation recommendation = new MRecommendation();
                    db.reconnect("clubMatcher");
                    break;
            }
        }
        while(choice != 0);
    }

    @Override
    public void adminMenu()
    {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);

        do
        {
            System.out.println("\n=================\nMAIN MENU OPTIONS\n=================\n");
            choice = validateInput
                    (
                            "\n\nPlease input the number corresponding to the option you want to choose:"
                            + "\n\t0 - Exit the program"
                            + "\n\t1 - Student Operations"  // includes student subjects
                            + "\n\t2 - Department Operations"
                            + "\n\t3 - Teacher Operations"
                            + "\n\t4 - Subject Operations"  // includes subject graph
                            + "\n\t5 - Club Operations"
                            + "\n\t6 - Club Log Operations"
                            + "\n\t7 - Club Recommendation"
                            + "\n\t8 - User Management Operations"   // login db management
                            + "\n\t9 - Export Database to CSV"
                    );

            switch(choice)
            {
                case 1:
                    MStudent student = new MStudent();
                    break;
                case 2:
                    MDepartment department = new MDepartment();
                    break;
                case 3:
                    MTeacher teacher = new MTeacher();
                    break;
                case 4:
                    MSubjects subject = new MSubjects();
                    break;
                case 5:
                    MClub club = new MClub();
                    break;
                case 6:
                    MClubLog log = new MClubLog();
                    break;
                case 7:
                    MRecommendation recommendation = new MRecommendation();
                    break;
                case 8:
                    MMisc misc = new MMisc();
                    break;
                case 9:
                    db.exportClubMatcherToCSV();
                    break;
            }
        }
        while(choice != 0);
    }
}


/*
int[][] m = new int[10][10];    
m[0][0] = 33;
m[0][1] = 19;
m[0][2] = 56;
m[0][3] = 13;
m[0][4] = 74;
m[0][5] = 93;
m[0][6] = 40;
m[0][7] = 8;
m[0][8] = 19;
m[0][9] = 45;

m[1][0] = 26;
m[1][1] = 74;
m[1][2] = 88;
m[1][3] = 41;
m[1][4] = 28;
m[1][5] = 27;
m[1][6] = 98;
m[1][7] = 79;
m[1][8] = 81;
m[1][9] = 1;

m[2][0] = 33;
m[2][1] = 52;
m[2][2] = 93;
m[2][3] = 18;
m[2][4] = 66;
m[2][5] = 36;
m[2][6] = 16;
m[2][7] = 97;
m[2][8] = 17;
m[2][9] = 14;

m[3][0] = 56;
m[3][1] = 9;
m[3][2] = 96;
m[3][3] = 24;
m[3][4] = 70;
m[3][5] = 69;
m[3][6] = 28;
m[3][7] = 5;
m[3][8] = 70;
m[3][9] = 9;

m[4][0] = 65;
m[4][1] = 39;
m[4][2] = 2;
m[4][3] = 62;
m[4][4] = 67;
m[4][5] = 16;
m[4][6] = 28;
m[4][7] = 62;
m[4][8] = 71;
m[4][9] = 7;

m[5][0] = 40;
m[5][1] = 75;
m[5][2] = 78;
m[5][3] = 39;
m[5][4] = 74;
m[5][5] = 49;
m[5][6] = 50;
m[5][7] = 99;
m[5][8] = 49;
m[5][9] = 71;

m[6][0] = 92;
m[6][1] = 39;
m[6][2] = 83;
m[6][3] = 23;
m[6][4] = 45;
m[6][5] = 88;
m[6][6] = 85;
m[6][7] = 40;
m[6][8] = 58;
m[6][9] = 56;

m[7][0] = 70;
m[7][1] = 69;
m[7][2] = 54;
m[7][3] = 83;
m[7][4] = 67;
m[7][5] = 92;
m[7][6] = 48;
m[7][7] = 43;
m[7][8] = 75;
m[7][9] = 33;

m[8][0] = 77;
m[8][1] = 29;
m[8][2] = 74;
m[8][3] = 82;
m[8][4] = 64;
m[8][5] = 39;
m[8][6] = 29;
m[8][7] = 31;
m[8][8] = 14;
m[8][9] = 13;

m[9][0] = 19;
m[9][1] = 37;
m[9][2] = 29;
m[9][3] = 80;
m[9][4] = 51;
m[9][5] = 38;
m[9][6] = 8;
m[9][7] = 93;
m[9][8] = 58;
m[9][9] = 44;

hungarian h = new hungarian(m);
h.optimalAssignment();
*/

/*
dll list1 = new dll();
dll list2 = new dll();

list1.append(1, 2, 3);
list2.append(4, 5, 6);
list1.printList();
list2.printList();
list1.concatenate(list2);
list2 = null; // 2ns list concatenated
// remove references to object
// jvm garbage collection will remove object
list1.printList();

System.out.println(list1.returnAtIndex(5).getData().toString());


BST bst = new BST(5);
bst.insert(bst.getRoot(), 2, 1, 8, 4, 6);
bst.prettyPrint(bst.getRoot(), 0);

dll data = bst.returnData();
data.printList();


hashMap map = new hashMap();
map.add(1, "z");
map.add(2, "a");
map.add(3, "b");
map.add(4, "c");
map.add(5, "d");
map.add(6, "e");
map.add(6, "e");
map.add(8, "g");
map.add(12, "h");

map.returnData();

pQueue<String> q = new pQueue<>();
q.enqueue("a", 1);
q.enqueue("b", 2);
q.enqueue("c", 3);
q.enqueue("d", 4);
q.enqueue("e", 5);
System.out.println(q.peek());
q.printQueue();


Graph<String> graph = new Graph<>();
        graph.add("a", "b", 4);
        graph.add("a", "c", 2);
        graph.add("b", "a", 4);
        graph.add("b", "c", 3);
        graph.add("b", "d", 2);
        graph.add("b", "e", 3);
        graph.add("c", "a", 2);
        graph.add("c", "b", 3);
        graph.add("c", "d", 4);
        graph.add("c", "e", 5);
        graph.add("d", "b", 2);
        graph.add("d", "c", 4);
        graph.add("d", "e", 1);
        graph.add("e", "b", 3);
        graph.add("e", "c", 5);
        graph.add("e", "d", 1);



        Dijkstra d = new Dijkstra();
        d.shortestPath("a", graph).printList();



graph<String> graph = new graph<>();
graph.add("a", "b", 5);
graph.add("a", "c", 3);
graph.add("a", "d", 10);
graph.add("b", "d", 6);
graph.add("c", "b", 1);
graph.add("c", "d", 4);


dijkstra d = new dijkstra(graph);
d.shortestPath("a");

File file = new File("clubMatcher.db");
file.delete();

DatabaseConnect db = new DatabaseConnect();
db.createTable(".\\src\\databaseConnect\\DDLs\\department.txt");
db.createTable(".\\src\\databaseConnect\\DDLs\\person.txt");
db.createTable(".\\src\\databaseConnect\\DDLs\\student.txt");
db.createTable(".\\src\\databaseConnect\\DDLs\\teacher.txt");
db.createTable(".\\src\\databaseConnect\\DDLs\\club.txt");
db.createTable(".\\src\\databaseConnect\\DDLs\\subjects.txt");
db.createTable(".\\src\\databaseConnect\\DDLs\\StudentSubjects.txt");
db.createTable(".\\src\\databaseConnect\\DDLs\\subjectGraph.txt");
db.createTable(".\\src\\databaseConnect\\DDLs\\clubLog.txt");

db.insert("Department", ".\\src\\databaseConnect\\CSVs\\department.csv");
db.insert("Person", ".\\src\\databaseConnect\\CSVs\\person.csv");
db.insert("Student", ".\\src\\databaseConnect\\CSVs\\student.csv");
db.insert("Teacher", ".\\src\\databaseConnect\\CSVs\\teacher.csv");
db.insert("Club", ".\\src\\databaseConnect\\CSVs\\club.csv");
db.insert("Subjects", ".\\src\\databaseConnect\\CSVs\\subjects.csv");
db.insert("StudentSubjects", ".\\src\\databaseConnect\\CSVs\\StudentSubjects.csv");
db.insert("subjectGraph", ".\\src\\databaseConnect\\CSVs\\subjectGraph.csv");
db.insert("ClubLog", ".\\src\\databaseConnect\\CSVs\\clubLog.csv");

//db.update("Person");

db.close();



//        Graph<String> graph = new Graph<>();
//        graph.add("a", "b", 2);
//        graph.add("a", "c", 1);
//        graph.add("b", "a", 2);
//        graph.add("b", "d", 4);
//        graph.add("b", "e", 5);
//        graph.add("c", "a", 1);
//        graph.add("d", "b", 4);
//        graph.add("d", "e", 7);
//        graph.add("e", "b", 5);
//        graph.add("e", "d", 7);
//
//
//
//        Dijkstra d = new Dijkstra();
//        d.shortestPath("e", graph).printList();

//        int[][] m = new int[5][5];
//        m[0][0] = 0;
//        m[0][1] = 2;
//        m[0][2] = 4;
//        m[0][3] = 5;
//        m[0][4] = 6;
//
//        m[1][0] = 0;
//        m[1][1] = 2;
//        m[1][2] = 3;
//        m[1][3] = 6;
//        m[1][4] = 7;
//
//        m[2][0] = 0;
//        m[2][1] = 2;
//        m[2][2] = 3;
//        m[2][3] = 6;
//        m[2][4] = 7;
//
//        m[3][0] = 0;
//        m[3][1] = 2;
//        m[3][2] = 4;
//        m[3][3] = 6;
//        m[3][4] = 7;
//
//        m[4][0] = 0;
//        m[4][1] = 2;
//        m[4][2] = 4;
//        m[4][3] = 6;
//        m[4][4] = 7;
//
//        Hungarian h = new Hungarian(m);
//        for(Pair<Integer, Integer> p : h.optimalAssignment())
//        {
//            System.out.println(h.getOriginal_matrix()[p.getKey()][p.getValue()]);
//        }

//        PQueue<String> pQueue = new PQueue<>();
*/