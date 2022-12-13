/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseConnect;

import java.io.BufferedReader;
import java.io.FileReader;
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
    
    public DatabaseConnect()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");//Specify the SQLite Java driver
            double start_time = System.nanoTime();
            conn = DriverManager.getConnection("jdbc:sqlite:clubMatcher.db");//Specify the database, since relative in the main project folder
            conn.setAutoCommit(false);// Important as you want control of when data is written
            double end_time = System.nanoTime();
            System.out.println("\nOpened database successfully");
            System.out.println("It took " + (end_time - start_time)/(1000000000) + " seconds to open the database!");
        }
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public void reconnect()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:clubMatcher.db");
            conn.setAutoCommit(false);
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
            System.out.println("\nPlease input the name of the field that you want to remove: ");
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
            
            int id = getNameID(name);
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
                        + "\nClub Subject: " + rs.getString(2)
                        + "\nDay Running: " + day_running
                        + "\nTime Club Takes Place: " + BLA
                        + "\nTeacher Running Club: " + rs.getString(5)
                        + "\nDepartment Running Club: " + rs.getString(6)
                        + "\nClub Start Time: " + rs.getString(7)
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
    
    public int getNameID(String name)
    {
        Statement stmt = null;
        int nameID = 0;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT Person.PersonID FROM Person WHERE Person.PersonName = '" + name + "'");
            if(rs.next())
                nameID = rs.getInt("PersonID");
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return nameID;
    }
    
    public int getDepartmentID(String dep_name)
    {
        Statement stmt = null;
        int nameID = 0;
        ResultSet rs = null;
        
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT Department.DepartmentID FROM Department WHERE Department.DepartmentName = '" + dep_name + "'");
            if(rs.next())
                nameID = rs.getInt("DepartmentID");
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return nameID;
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
    
    public boolean insertStudentTeacher(boolean isStudentTeacher)    // if bool is true, insert a student, else, insert a teacher
    {
        boolean bInsert = false;
        Statement stmt = null;
        ResultSet rs = null;
        Scanner scnr = new Scanner(System.in);
        
        try
        {
            stmt = conn.createStatement();
            
            if(isStudentTeacher)
                System.out.println("Please input the name of the student you want to add: ");
            else
                System.out.println("Please input the name of the teacher you want to add: ");
            String name = scnr.nextLine();
            
            if(getNameID(name) == 0)    // if student/teacher doesn't exist in person table
                insertPerson(name);
            
            
            int personID = getNameID(name);
            
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
                
                if(getDepartmentID(department_name) == 0)
                    insertDepartment(department_name);
                    
                stmt.execute("INSERT INTO Teacher (PersonID, DepartmentID) VALUES (" + personID + ", " + getDepartmentID(department_name) + ")");
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
