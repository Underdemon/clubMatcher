/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseConnect;

import dataStructures.dll.DLL;
import dataStructures.graphs.Graph;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 *
 * @author rayan
 */
public class DatabaseConnect
{
    private static Connection conn = null;
    
    public DatabaseConnect(String db_name)
    {
        try
        {
            Class.forName("org.sqlite.JDBC"); //Specify the SQLite Java driver
            double start_time = System.nanoTime();
            conn = DriverManager.getConnection("jdbc:sqlite:" + db_name); //Specify the database, since relative in the main project folder
            conn.setAutoCommit(false);// Important as you want control of when data is written
            double end_time = System.nanoTime();
            System.out.println("\nOpened database successfully");
            System.out.println("It took " + (end_time - start_time)/(1000000000) + " seconds to open the database (" + db_name + ")!");
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public void reconnect(String dbName)
    {
        try
        {
            Class.forName("org.sqlite.JDBC"); // Specify the SQLite Java driver
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db"); // Specify the database, path relative in the main project folder
            conn.setAutoCommit(false); // gives program of when data is written
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public void close() 
    {
        try
        {
            conn.close();
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void executeQuery(String query)
    {
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.execute(query);
            stmt.close();
            conn.commit();
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public boolean createTable(String ddlPath)
    {
        Statement stmt = null;

        try
        {
            stmt = conn.createStatement();
            
            Path ddlCreate = Paths.get(ddlPath);
            Stream<String> lines = Files.lines(ddlCreate);
            String data = lines.collect(Collectors.joining("\n"));
            lines.close();
            
            String sql = data;
            stmt.executeUpdate(sql);         
            stmt.close();           
            conn.commit();
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return stmt != null;
    }
    
    public boolean insert(String csvName, String CSVpath)
    {
        boolean bInsert = false;
        Statement stmt = null;

        try
        {
            stmt = conn.createStatement();
            long lineCount = 0;
            try (Stream<String> stream = Files.lines(Paths.get(CSVpath), StandardCharsets.UTF_8)) 
            {
                lineCount = stream.count();
            }
            
            String sql = "INSERT INTO " + csvName;
            BufferedReader br = new BufferedReader(new FileReader(CSVpath));
            String line;
            int lineNum = 0;
            String[] headers = null;    //can replace with function coded
            String[][] values = new String[(int)lineCount - 1][];
            
            while((line = br.readLine()) != null)
            {
                if(lineNum == 0)
                {
                    headers = line.split(",");
                }
                else
                {
                    values[lineNum - 1] = line.split(",");
                }
                
                lineNum++;
            }
                        
            for(int i = 0; i < headers.length; i++)
            {
                if(i == 0)
                {
                    sql += " (" + headers[i] + ",";
                }
                else if(i == headers.length - 1)
                {
                    sql += headers[i] + ") VALUES (";
                }
                else
                {
                    sql += headers[i] + ",";
                }
            }
                        
            String sqlTemp = "";
            for(int j = 0; j < lineCount - 1; j++)
            {
                for(int i = 0; i < values[j].length; i++)
                {
                    if(isNumeric(values[j][i]))
                    {
                        if(i == values[j].length - 1)
                        {
                            sqlTemp += values[j][i] + ")";
                        }
                        else
                        {
                            sqlTemp += values[j][i] + ",";
                        }
                    }
                    else
                    {
                        if(i == values[j].length - 1)
                        {
                            sqlTemp += "'" + values[j][i] + "')";
                        }
                        else
                        {
                            sqlTemp += "'" + values[j][i] + "',";
                        }
                    }
                }
                String complete_statement = sql + sqlTemp;
                // System.out.println(complete_statement);
                stmt.executeUpdate(complete_statement);
                sqlTemp = "";
            }
            
            stmt.close();
            conn.commit();
            bInsert = true;
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return bInsert;
    }
    
    public boolean insert(String tableName)
    {
        boolean bInsert = false;
        Statement stmt = null;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            
            System.out.println("\nThe fields in the " + tableName + " table are:");
            String[] columnNames = selectColumnNames(tableName);
            String columnNamesConcat = "";
            for(int i = 0; i < columnNames.length; i++)
            {
                System.out.print("\n|\n=---" + columnNames[i]);
                if(i == (columnNames.length - 1))
                    columnNamesConcat += columnNames[i];
                else
                    columnNamesConcat += columnNames[i] + ",";
            }
            
            rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
            int rowCount = rs.getInt(1);
            
            System.out.println("\n");
            Scanner scnr = new Scanner(System.in);
            String sql = "INSERT INTO " + tableName + "(" + columnNamesConcat + ") VALUES (" + (rowCount + 1) + ",";
            //(rowCount + 1) is the new "column"ID
            //this assumes we aren't adding into what should be static .db (like MovieFormat)
            String tmp = null;
            for(int i = 1; i < columnNames.length; i++)
            {
                System.out.println("Please enter the value you want to insert into " + columnNames[i]);
                tmp = scnr.nextLine();
                if(i == (columnNames.length - 1))
                {
                    if(isNumeric(tmp))
                        sql += tmp + ")";
                    else
                        sql += "'" + tmp + "')";
                }
                else
                {
                    if(isNumeric(tmp))
                        sql += tmp + ",";
                    else
                        sql += "'" + tmp + "',";
                }
            }
            stmt.execute(sql);
            stmt.close();
            conn.commit();
            bInsert = true;
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return bInsert;
    }
    
    public boolean update(String tableName)
    {
        boolean bUpdate = false;
        Statement stmt = null;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            
            String[] columnNames = selectColumnNames(tableName);
            
            String sql = "";
            Scanner scnr = new Scanner(System.in);
            System.out.println("\nPlease input the " + columnNames[0] + " that you want to change: ");            
            int id = scnr.nextInt();
            scnr.nextLine();
            System.out.println("Please input the EXACT name of the field above you want to modify: ");
            String field = scnr.nextLine();
            System.out.println("Please enter what you would like to change " + field + " to: ");
            String choice = scnr.nextLine();
            if(!isNumeric(choice))
                sql = "UPDATE " + tableName + " set " + field + " = '" + choice + "' WHERE " + columnNames[0] + "=" + id;
            else
                sql = "UPDATE " + tableName + " set " + field + " = " + choice + " WHERE " + columnNames[0] + "=" + id;
            System.out.println(sql);
            stmt.execute(sql);
            stmt.close();
            conn.commit();
            bUpdate = true;
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return bUpdate;
    }
    
    public boolean delete(String tableName)    // can be used with any table that has the "xxxName" field (club, department, person and subjects tables)
    {
        boolean bDelete = false;
        Statement stmt = null;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            
            String sql = "";
            Scanner scnr = new Scanner(System.in);
            System.out.println("\nPlease input the name of the " + tableName + " that you want to remove: ");
            String choice = scnr.nextLine();
            sql = "DELETE FROM " + tableName + " WHERE " + tableName + "Name" + " = '" + choice + "'";
            // System.out.println(sql);
            stmt.execute(sql);
            stmt.close();
            conn.commit();
            bDelete = true;
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return bDelete;
    }
    
    public void deleteStudentTeacher(boolean isStudentTeacher)    // can be used with any table that has the "xxxName" field (club, department, person and subjects tables)
    {
        Scanner scnr = new Scanner(System.in);
        String choice = null;
        if(isStudentTeacher)
            System.out.println("\nPlease input the name of the student that you want to remove: ");
        else
            System.out.println("\nPlease input the name of the teacher that you want to remove: ");
        
        choice = scnr.nextLine();
        deleteStudentTeacher(isStudentTeacher, choice);
    }
    
    public boolean deleteStudentTeacher(boolean isStudentTeacher, String name)    // can be used with any table that has the "xxxName" field (club, department, person and subjects tables)
    {
        boolean bDelete = false;
        Statement stmt = null;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            
            String sql = "";
            Scanner scnr = new Scanner(System.in);
            
            int id = getID(name, "Person");
            if(isStudentTeacher)
                sql = "DELETE FROM Student WHERE PersonID" + " = '" + id + "'";
            else
                sql = "DELETE FROM Teacher WHERE PersonID" + " = '" + id + "'";
            // System.out.println(sql);
            stmt.execute(sql);
            stmt.close();
            conn.commit();
            bDelete = true;
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return bDelete;
    }
    
    public boolean queryOutput(String query, String tableName)
    {
        System.out.println("\n");
        boolean bQuery = false;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            ResultSetMetaData metaData  = rs.getMetaData();
            
            while(rs.next())
            {
                if(tableName.equals("Student"))
                {
                    System.out.println
                    (
                        "Student Name: " + rs.getString(1)
                        + "\nStudent Year Group: " + rs.getInt(2) + "\n"
                    );
                }
                else if(tableName.equals("Teacher"))
                {
                    System.out.println
                    (
                        "Teacher Name: " + rs.getString(1)
                        + "\nTeacher Department: " + rs.getString(2) + "\n"
                    );
                }
                else if(tableName.equals("Club"))
                {
                    String day_running = null;
                    if(rs.getInt(3) == 1)
                        day_running = "Monday";
                    else if(rs.getInt(3) == 2)
                        day_running = "Tuesday";
                    else if(rs.getInt(3) == 3)
                        day_running = "Wednesday";
                    else if(rs.getInt(3) == 4)
                        day_running = "Thursday";
                    else if(rs.getInt(3) == 5)
                        day_running = "Friday";
                    
                    String BLA = null;
                    if(rs.getString(4).equals("B"))
                        BLA = "Before School";
                    else if(rs.getString(4).equals("L"))
                        BLA = "During Lunch";
                    else if(rs.getString(4).equals("A"))
                        BLA = "After School";
                    
                    System.out.println
                    (
                        "Club Name: " + rs.getString(1)
                        + "\nSubject Associated to Club: " + rs.getString(2)
                        + "\nDay Running: " + day_running
                        + "\nTime Club Takes Place: " + BLA
                        + "\nTeacher Running Club: " + rs.getString(5)
                        + "\nDepartment Running Club: " + rs.getString(6)
                        + "\nClub Start Time: " + rs.getString(7)
                    );
                }
                else if(tableName.equals("ClubLog"))
                {
                    System.out.println
                    (
                        "Club: " + getName(rs.getInt("ClubID"), "Club")
                        + "\n\tStudent: " + getName(rs.getInt("StudentID"), "StudentNames")
                        + "\n\tDate: " + rs.getString("Date")
                        + "\n\tTimestamp: " + rs.getString("Timestamp")
                    );
                }
                else if(tableName.equals("SubjectGraph"))
                {
                    System.out.println
                    (
                        "Club 1: " + getName(rs.getInt(1), "SubjectGraph")
                        + "\nClub 2: " + getName(rs.getInt(2), "SubjectGraph")
                        + "\nWeight: " + rs.getInt(3)
                    );
                }
                else
                {
                    for(int i = 1; i <= metaData.getColumnCount(); i++)
                    {
                        int colType = metaData.getColumnType(i);    // getColumnType returns an int corresponding to data type of column

                        if(colType == 4)    // integer
                            System.out.println(rs.getInt(i));
                        else if(colType == 12)  // varchar
                            System.out.println(rs.getString(i));
                        else if(colType == 16)  // boolean
                            System.out.println(rs.getBoolean(i));
                    }
                }
                System.out.println("\n");
            }
            stmt.close();
            conn.commit();
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return bQuery;
    }
    
    private static String[] selectColumnNames(String tableName)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String[] columnName = null;
        
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            columnName = new String[count];

            for (int i = 1; i <= count; i++)
            {
               columnName[i - 1] = metaData.getColumnLabel(i);
            }
            
            rs.close();
            stmt.close();   
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return columnName;
    }
        
    public int table_len(String tableName)
    {
        Statement stmt = null;
        int len = 0;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM Subjects");
            len = rs.getInt(1);
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return len;
    }
    
    public int getID(String name, String table)
    {
        Statement stmt = null;
        int ID = 0;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            if(table.equals("Student"))
            {
                rs = stmt.executeQuery("SELECT Student.StudentID FROM Student INNER JOIN Person ON Student.PersonID = Person.PersonID WHERE Person.PersonName = '" + name + "'");
                if(rs.next())
                    ID = rs.getInt(1);
            }
            else
            {
                rs = stmt.executeQuery("SELECT " + table + "." + table + "ID FROM " + table + " WHERE " + table + "." + table + "Name = '" + name + "'");
                if(rs.next())
                    ID = rs.getInt(table + "ID");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return ID;
    }
    
    public String getName(int ID, String table)
    {
        Statement stmt = null;
        String name = null;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            if(table.equals("Club"))
                rs = stmt.executeQuery("SELECT " + table + "." + table + "Name FROM " + table + " WHERE " + table + "." + table + "ID = '" + ID + "'");
            else if(table.equals("StudentNames"))
                rs = stmt.executeQuery("SELECT Person.PersonName FROM Student INNER JOIN Person ON Student.PersonID = Person.PersonID WHERE Student.StudentID = " + ID);
            else if(table.equals("Subjects"))
                rs = stmt.executeQuery("SELECT Subjects.SubjectsName FROM Subjects WHERE Subjects.SubjectsID = " + ID);
            else if(table.equals("SubjectGraph"))
                rs = stmt.executeQuery("SELECT Subjects.SubjectsName FROM Subjects WHERE Subjects.SubjectsID = " + ID);
            else
                rs = stmt.executeQuery("SELECT Person.PersonName FROM " + table + " INNER JOIN Person ON " + table + ".PersonID = Person.PersonID WHERE " + table + "." + table + "ID = '" + ID + "'");
            
            if(rs.next())
                if(table.equals("StudentNames"))
                    name = rs.getString("PersonName");
                else if(table.equals("SubjectGraph"))
                    name = rs.getString(1);
                else
                    name = rs.getString(table + "Name");
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return name;
    }
    
    public int getTeacherID(String name)
    {
        Statement stmt = null;
        ResultSet rs = null;        
        int personID = getID(name, "Person");
        int teacherID = 0;
        
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT Teacher.TeacherID FROM Teacher INNER JOIN Person ON Teacher.PersonID = Person.PersonID WHERE Person.PersonID = " + personID);
            if(rs.next())
                teacherID = rs.getInt("TeacherID");
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return teacherID;
    }
    
    private boolean insertPerson(String name)
    {
        Statement stmt = null;
        boolean bInsert = false;
        
        try
        {
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO PERSON (PersonName) VALUES('" + name + "')");
            stmt.close();
            conn.commit();
            bInsert = true;
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return bInsert;
    }
    
    private boolean insertDepartment(String name)
    {
        Statement stmt = null;
        boolean bInsert = false;
        
        try
        {
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO Department (DepartmentName) VALUES('" + name + "')");
            stmt.close();
            conn.commit();
            bInsert = true;
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return bInsert;
    }
    
    public String studentTeacherName(boolean isStudentTeacher)
    {
        Scanner scnr = new Scanner(System.in);
        String name = null;
        
        if(isStudentTeacher)
        {
            System.out.println("Please input the name of the student you want to add (enter \"list\" to list the students in the database): ");
            name = scnr.nextLine();
            while(name.equals("list"))
            {
                queryOutput("SELECT Person.PersonName FROM Student, Person WHERE Student.PersonID = Person.PersonID", "");
                System.out.println("Please input the name of the student to replace the current one (enter \"list\" to list the students in the database): ");
                name = scnr.nextLine();
            }
        }
        else
        {
            System.out.println("Please input the name of the teacher you want to add (enter \"list\" to list the teachers in the database): ");
            name = scnr.nextLine();
            while(name.equals("list"))
            {
                queryOutput("SELECT Person.PersonName FROM Teacher, Person WHERE Teacher.PersonID = Person.PersonID", "");
                System.out.println("Please input the name of the teacher to replace the current one (enter \"list\" to list the teachers in the database): ");
                name = scnr.nextLine();
            }
        }
        
        return name;
    }
    
    public void insertStudentTeacher(boolean isStudentTeacher)    // if bool is true, insert a student, else, insert a teacher
    {
        insertStudentTeacher(isStudentTeacher, studentTeacherName(isStudentTeacher));
    }
    
    public boolean insertStudentTeacher(boolean isStudentTeacher, String name)    // if bool is true, insert a student, else, insert a teacher
    {
        Scanner scnr = new Scanner(System.in);
        boolean bInsert = false;
        Statement stmt = null;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            
            if(getID(name, "Person") == 0)    // if student/teacher doesn't exist in person table
                insertPerson(name);
            
            
            int personID = getID(name, "Person");
            
            if(isStudentTeacher)
            {
                rs = stmt.executeQuery("SELECT COUNT(*) FROM Teacher WHERE Teacher.PersonID = " + personID);
                if(rs.getInt(1) > 0)
                {
                    String choice = null;
                    
                    System.out.println(name + " has been found in the Teacher table."
                                            + " Would you like to remove " + name + " from the teacher table? Y/N: ");
                    choice = scnr.nextLine();
                    if(choice.equals("Y") || choice.equals("y"))
                        deleteStudentTeacher(false, name);
                    else
                        return false;
                }
                
                // search for student in teacher table
                int yearGroup = 0;
                do
                {
                    System.out.println("Please input the year group of the student (it must be between 7-13): ");
                    yearGroup = scnr.nextInt();
                    scnr.nextLine();
                }
                while(yearGroup < 7 || yearGroup > 13);
            
                stmt.execute("INSERT INTO Student (PersonID, YearGroup) VALUES (" + personID + ", " + yearGroup + ")");
            }
            else
            {
                rs = stmt.executeQuery("SELECT COUNT(*) FROM Student WHERE Student.PersonID = " + personID);
                if(rs.getInt(1) > 0)
                {
                    String choice = null;
                    System.out.println(name + " has been found in the Student table."
                                            + " Would you like to remove " + name + " from the student table? Y/N: ");
                    
                    choice = scnr.nextLine();
                    if(choice.equals("Y") || choice.equals("y"))
                        deleteStudentTeacher(true, name);
                    else
                        return false;
                }
                
                // search for teacher in student table
                String department_name = null;
                do
                {
                    System.out.println("Please input the Department of the teacher (enter \"list\" to list the departments in the database):");
                    department_name = scnr.nextLine();
                    
                    if(department_name.equals("list"))
                        queryOutput("SELECT Department.DepartmentName FROM Department", "");
                    
                    System.out.println("\n");
                }
                while(department_name == null || department_name.equals("list"));
                
                if(getID(department_name, "Department") == 0)
                    insertDepartment(department_name);
                    
                stmt.execute("INSERT INTO Teacher (PersonID, DepartmentID) VALUES (" + personID + ", " + getID(department_name, "Department") + ")");
            }
            
            
            stmt.close();
            conn.commit();
            bInsert = true;
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return bInsert;
    }
    
    
    // functions needed for club recommendation //    
    public boolean getGraph(Graph<String> graph)
    {
        boolean bGraph = false;
        Statement stmt = null;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            
            rs = stmt.executeQuery("SELECT * FROM SubjectGraph");
            while(rs.next())
                graph.add(rs.getString(1), rs.getString(2), rs.getInt(3));
                // var x = getName(rs.getInt(1), "Subjects");
                // var y = getName(rs.getInt(2), "Subjects");
                // var z = rs.getInt(3);
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return bGraph;
    }

    public DLL<Integer> getUnassignedStudents(DLL<Integer> students, int limit)
    {
        Statement stmt = null;
        ResultSet rs = null;

        try
        {
            stmt = conn.createStatement();

            rs = stmt.executeQuery
                    (
                            "SELECT DISTINCT StudentSubjects.StudentID FROM StudentSubjects "
                                    + "INNER JOIN Student ON StudentSubjects.StudentID = Student.StudentID "
                                    + "WHERE Student.isAssigned = 0 "
                                    + "LIMIT " + limit  // only takes in the first X studentIDs where X = limit
                    );

            while(rs.next())
                students.append(rs.getInt(1));

        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return students;
    }

    public void SubjectsStudent(int studentID, DLL<Integer> subjects)
    {
        Statement stmt = null;
        ResultSet rs = null;

        try
        {
            stmt = conn.createStatement();

            rs = stmt.executeQuery
                    (
                            "SELECT DISTINCT StudentSubjects.SubjectsID FROM StudentSubjects "
                                    + "WHERE StudentSubjects.StudentID = " + studentID
                    );

            while(rs.next())
                subjects.append(rs.getInt(1));

        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public int getUnassignedStudentSubjectsCount(int limit)
    {
        Statement stmt = null;
        ResultSet rs = null;
        int count = -1;

        try
        {
            stmt = conn.createStatement();

            rs = stmt.executeQuery
            (
            "SELECT COUNT(DISTINCT StudentSubjects.StudentID) FROM StudentSubjects "
                + "INNER JOIN Student ON StudentSubjects.StudentID = Student.StudentID "
                + "WHERE Student.isAssigned = 0 "
                + "LIMIT " + limit
            );

            count = rs.getInt(1);
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return count;
    }

    public DLL<String> getUnassignedStudentSubjectsName(int limit)
    {
        Statement stmt = null;
        ResultSet rs = null;
        DLL<String> names = new DLL<>();

        try
        {
            stmt = conn.createStatement();

            rs = stmt.executeQuery
                    (
                            "SELECT DISTINCT StudentSubjects.SubjectsID FROM StudentSubjects "
                                    + "INNER JOIN Student ON StudentSubjects.StudentID = Student.StudentID "
                                    + "WHERE Student.isAssigned = 0 "
                                    + "LIMIT " + limit
                    );

            while(rs.next())
                names.append(rs.getString(1));
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return names;
    }

    public int getDistinctSubjectsCountForAssignment()
    {
        Statement stmt = null;
        ResultSet rs = null;
        int count = -1;

        try
        {
            stmt = conn.createStatement();

            rs = stmt.executeQuery("SELECT COUNT(DISTINCT StartVertex) FROM SubjectGraph");

            if(rs.next())
                count = rs.getInt(1);
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return count;
    }

    public boolean isUnassignedStudentsLeft()
    {
        Statement stmt = null;
        ResultSet rs = null;
        boolean isUnassignedStudentsLeft = true;

        try
        {
            stmt = conn.createStatement();

            rs = stmt.executeQuery("SELECT COUNT(*) FROM Student WHERE Student.isAssigned = 0");

            if(rs.next())
                if(rs.getInt(1) == 0)
                    isUnassignedStudentsLeft = false;
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return isUnassignedStudentsLeft;
    }

    public String getRandomClub(int subjects)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String club = null;

        try
        {
            stmt = conn.createStatement();

            rs = stmt.executeQuery("SELECT Club.ClubName FROM Club WHERE Club.SubjectsID = " + subjects + " ORDER BY RANDOM() LIMIT 1");

            if (rs.next())
                club = rs.getString(1);

        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return club;
    }

    /**
     * for user db checks
     * @return
     */
    public boolean correctUser(String username, String password)
    {
        Statement stmt = null;
        ResultSet rs = null;
        boolean bSearch = false;

        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Users WHERE Users.Username = '" + username + "' AND Users.Password = '" + password + "'");
            if(rs.next())
                bSearch = true;
            else
                bSearch = false;
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return bSearch;
    }

    /**
     * get user access level
     */
    public int getUACLevel(String username, String password)
    {
        Statement stmt = null;
        ResultSet rs = null;
        int uac = -1;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT Users.isAdmin FROM Users WHERE Users.Username = '" + username + "' AND Users.Password = '" + password + "'");
            if(rs.next())
                uac = rs.getInt(1);
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return uac;
    }

    public String getCurrentUserPassword(String username)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String password = null;

        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT Users.Password FROM Users WHERE Users.Username = '" + username + "'");
            if(rs.next())
                password = rs.getString(1);
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return password;
    }

    public void exportClubMatcherToCSV()
    {
        Statement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData metaData = null;

        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery
            (
            "SELECT COUNT(*) FROM sqlite_master "
                + "WHERE type IN ('table','view') AND name NOT LIKE 'sqlite_%' "
                + "UNION ALL "
                + "SELECT name FROM sqlite_temp_master "
                + "WHERE type IN ('table','view') "
                + "ORDER BY 1;"
            );

            String[] tableNames = new String[rs.getInt(1)];

            rs = stmt.executeQuery
            (
            "SELECT name FROM sqlite_master "
                + "WHERE type IN ('table','view') AND name NOT LIKE 'sqlite_%' "
                + "UNION ALL "
                + "SELECT name FROM sqlite_temp_master "
                + "WHERE type IN ('table','view') "
                + "ORDER BY 1;"
            );
            int i = 0;
            while(rs.next())
            {
                tableNames[i] = rs.getString(1);
                i++;
            }

            exportDBtoCSV(tableNames);
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void exportDBtoCSV(String[] table_names)
    {
        Statement stmt = null;
        ResultSet rs = null;
        BufferedWriter fileWriter;

        Scanner scanner = new Scanner(System.in);
        System.out.println("What would you like to call this backup: ");
        String backupName = scanner.nextLine();
        try
        {
            Files.createDirectories(Paths.get(".\\src\\databaseConnect\\data\\" + backupName));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        try
        {
            stmt = conn.createStatement();

            for(String table_name : table_names)
            {
                Path newFilePath = Paths.get(".\\src\\databaseConnect\\data\\" + backupName + "\\" + table_name + ".csv");
                Files.createFile(newFilePath);
                File csvFile = new File(".\\src\\databaseConnect\\data\\" + backupName + "\\" + table_name + ".csv");
                fileWriter = new BufferedWriter(new FileWriter(csvFile));

                rs = stmt.executeQuery("SELECT * FROM " + table_name);
                ResultSetMetaData metaData = rs.getMetaData();
                String header = "";
                int colCount = metaData.getColumnCount();
                for(int i = 1; i <= metaData.getColumnCount(); i++)
                    header = header.concat(metaData.getColumnLabel(i).concat(","));

                fileWriter.write(header);

                while(rs.next())
                {
                    String line = "";

                    for(int i = 1; i <= metaData.getColumnCount(); i++)
                    {
                        String value = "";
                        Object obj = rs.getObject(i);
                        if(obj != null)
                            value = obj.toString();

                        if(obj instanceof String)
                            value = value.replaceAll("\"", "\"\"") ;    // add string literal

                        line = line.concat(value);

                        if(i != metaData.getColumnCount())
                            line = line.concat(",");
                    }
                    fileWriter.newLine();
                    fileWriter.write(line);
                }
                stmt.close();
                fileWriter.close();
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public boolean isNumeric(String str)
    {
        if(str.equals(null))
            return false;
        
        final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        /*
        -?          determines if number starts with minus
        \d+         matches one or more digits
        (\.\d+)?    matches for decimal point and digits following
        */
        
        return pattern.matcher(str).matches();
    }
}
