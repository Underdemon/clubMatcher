/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menus;

import algorithms.dijkstra.Dijkstra;
import algorithms.dijkstra.Edge;
import algorithms.hungarian.Hungarian;
import dataStructures.dll.DLL;
import dataStructures.graphs.Graph;
import dataStructures.hashmap.Pair;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Math.round;

/**
 *
 * @author rayan
 */
public class MRecommendation extends clubmatcher.ClubMatcher
{
    private int choice = 0;
    private Scanner scanner = new Scanner(System.in);
    private String input = "";
    private Graph graph;
    private DLL<String> studentSubjects;
    private Dijkstra dijkstra;
    private Hungarian hungarian;
    
    public MRecommendation()
    {
        graph = new Graph();
        studentSubjects = new DLL();
        dijkstra = new Dijkstra();
        int size = db.table_len("Subjects");
        int[][] m = new int[size][size];
        
        do
        {
            choice = validateInput
            (
                "\n\t 0 - Exit to Main Menu"
                + "\n\t 1 - View all unassigned students"
                + "\n\t 2 - Assign all unassigned students (suggest a club)"
            );

            switch(choice)
            {
                case 0:
                    return;

                case 1:
                    db.queryOutput("SELECT Person.PersonName FROM Student INNER JOIN Person ON Student.PersonID = Person.PersonID WHERE Student.isAssigned = 0", "");
                    break;

                case 2:
                    db.getGraph(graph); // inserts the subjectGraph table from the db into the graph object
                    db.getStudentSubjects(studentSubjects); // gets all the subjects being done by the selected (unassigned) students
                    dijkstra = new Dijkstra();

                    int unassignedStudentSubjects = db.getUnassignedStudentSubjectsCount();
                    int distinctSubjects = db.getDistinctSubjectsCountForAssignment();

                    DLL<Integer> unassignedStudents = new DLL<>();
                    db.getUnassignedStudents(unassignedStudents);

                    // initialsing the matrices to the max assignment size (if there are more people to assign than the number of subjects
                    // that students are doing, the assignment will be done in rounds)
                    int[][] cost_matrix = new int[distinctSubjects][distinctSubjects];
                    // the cost mask tracks the subject name in each index (since assignment may not keep the same subjects in the same row)
                    String[] cost_mask = new String[distinctSubjects];
                    // the student names mask tracks which column corresponds to a student
                    String[] studentNamesMask = new String[distinctSubjects];

                    if(unassignedStudents.getLen() <= distinctSubjects)
                    {
                        for(int i = 0; i < unassignedStudents.getLen(); i++)  // for each student in the list of unassigned students
                        {
                            studentNamesMask[i] = db.getName(unassignedStudents.returnAtIndex(i), "StudentNames");
                            DLL<Integer> subjectsOfStudent = new DLL<>();
                            db.SubjectsStudent(unassignedStudents.returnAtIndex(i), subjectsOfStudent);

                            DLL<Integer> individual_cost = new DLL<>();
                            for(int j = 0; j < distinctSubjects; j++)
                            {
                                individual_cost.append(0);  // defaults the DLL to have 0 values, which will be combined with the weights from dijkstra
                            }

                            for(int j = 0; j < subjectsOfStudent.getLen(); j++)
                            {
                                individual_cost.combine(dijkstra.shortestPath(Integer.toString(subjectsOfStudent.returnAtIndex(j)), graph));
                            }

                            int j = 0;
                            for(int k : individual_cost)
                            {
                                cost_matrix[i][j] = round(k/(subjectsOfStudent.getLen())) + (int)Math.floor(Math.random() * 3);
                                j++;
                            }
                        }

                        DLL<String> subjects = new DLL<>();
                        db.getStudentSubjects(subjects);
                        int l = 0;
                        for(String s : subjects)
                        {
                            cost_mask[l] = s;
                            l++;
                        }

                        if(unassignedStudents.getLen() < distinctSubjects)    // fill matrices dummy values so they are square
                        {
                            for(int i = unassignedStudents.getLen(); i < distinctSubjects; i++)
                            {
                                for(int j = 0; j < distinctSubjects; j++)
                                    cost_matrix[i][j] = 0;
                            }
                        }

                        hungarian = new Hungarian(cost_matrix);
                        DLL<Pair<Integer, Integer>> assignment = hungarian.optimalAssignment();
                        for(Pair<Integer, Integer> pair : hungarian.optimalAssignment())
                        {
                            System.out.println(studentNamesMask[pair.getKey()] + ": " + cost_mask[pair.getValue()]);
                            /*              outputs the student name with the suggested subject                   */
                        }

                        db.executeQuery("UPDATE Student SET isAssigned = 1");
                    }
                    else // if there are too many students to assign, then get the first x students that keep the matrix square, run it normally, set students as assigned to club, and continue getting sets of x students until all students assigned
                    {
                        // find out how many assignment iterations will have to be done
                        int noOfAssignments = unassignedStudents.getLen() % distinctSubjects;
                        for(int i = 0; i <= noOfAssignments; i++)
                        {
                            DLL<Integer> unassignedStudentsGroup = new DLL<>();
                            db.getUnassignedStudents(unassignedStudentsGroup, distinctSubjects * i, distinctSubjects);

                            for(int j = 0; j < unassignedStudentsGroup.getLen(); j++)  // for each student in the list of unassigned students
                        // the list of unassigned students seperates fine but when more students than subjects it reassigns twice, excluding 1 person on 2nd assignment
                            {
                                studentNamesMask[j] = db.getName(unassignedStudentsGroup.returnAtIndex(j), "StudentNames");
                                DLL<Integer> subjectsOfStudent = new DLL<>();
                                db.SubjectsStudent(unassignedStudentsGroup.returnAtIndex(j), subjectsOfStudent);

                                DLL<Integer> individual_cost = new DLL<>();
                                for(int k = 0; k < distinctSubjects; k++)
                                {
                                    individual_cost.append(0);  // defaults the DLL to have 0 values, which will be combined with the weights from dijkstra
                                }

                                for(int k = 0; k < subjectsOfStudent.getLen(); k++)
                                {
                                    individual_cost.combine(dijkstra.shortestPath(Integer.toString(subjectsOfStudent.returnAtIndex(k)), graph));
                                }

                                int k = 0;
                                for(int l : individual_cost)
                                {
                                    cost_matrix[j][k] = round(l/(subjectsOfStudent.getLen())) + (int)Math.floor(Math.random() * 3);
                                    k++;
                                }
                            }

                            DLL<String> subjects = new DLL<>();
                            db.getStudentSubjects(subjects);
                            int l = 0;
                            for(String s : subjects)
                            {
                                cost_mask[l] = s;
                                l++;
                            }

                            if(unassignedStudentsGroup.getLen() < distinctSubjects)    // fill matrices dummy values so they are square
                            {
                                for(int j = unassignedStudentsGroup.getLen(); j < distinctSubjects; j++)
                                {
                                    for(int k = 0; k < distinctSubjects; k++)
                                        cost_matrix[j][k] = 0;
                                }
                            }

                            hungarian = new Hungarian(cost_matrix);
                            DLL<Pair<Integer, Integer>> assignment = hungarian.optimalAssignment();
                            for(Pair<Integer, Integer> pair : hungarian.optimalAssignment())
                            {
                                System.out.println(studentNamesMask[pair.getKey()] + ": " + cost_mask[pair.getValue()]);
                                /*              outputs the student name with the suggested subject                   */
                            }
                        }
                        Arrays.fill(studentNamesMask, null);
                        Arrays.fill(cost_matrix, null);
                        Arrays.fill(cost_mask, null);

                        db.executeQuery("UPDATE Student SET isAssigned = 1");
                    }
                    break;
            }
        }
        while(choice != 0);
    }
}

/*
to get db into graph of subjects
since vertices are subjectID get subject name
startSubject, endSubject, weight
while(rs.next())
    graph.add(startSubject, endSubject, weight);

select the students you want to assign

    SELECT Subjects.SubjectsName FROM Subjects
    INNER JOIN StudentSubjects ON StudentSubjects.SubjectsID = Subjects.SubjectsID
    INNER JOIN Student ON StudentSubjects.StudentID = Student.StudentID
    INNER JOIN Person ON Student.PersonID = Person.PersonID
    WHERE Person.PersonName = 'Antonussaltassussy'

select subjects for each student
use dijkstra to find weights between one of the student's studied subjects and all other subjects
    option to include studied subjects in recommendation
repeat for all of the student's subjects
repeat for all students (taking 2+ same subjects) (students can be taken in groups no bigger than the number of subjects)

this is the cost matrix per subject
run hungarian on cost matrix
search optimal solution in dll of dijkstra solutions to find which subjects have been recommended to each student
recommend clubs in that subject

if unhappy with subjects, allow user to increase cost for that subject to infinity and rerun matching algo


Select all distinct subjects to run dijkstra from
SELECT DISTINCT Subjects.SubjectsName FROM StudentSubjects
INNER JOIN Subjects ON StudentSubjects.SubjectsID = Subjects.SubjectsID
*/

/*
user login system:

user logs in
based on usertype, you have either restricted access (just an if statement that changes what is output and the switch cases in the menu to only allow for that access level) or you have full access
*/


//                    int i = 0;
//                    int j = 0;
//
//                    for(String s : studentSubjects)
//                    {
//                        for(Pair<String, Integer> pair : dijkstra.shortestPath(s, graph))
//                        {
//                            m[i][j] = pair.getValue();
//                            j++;
//                        }
//
//                        i++;
//                        j = 0;

//                        if(Integer.parseInt(s) == 3)
//                            dijkstra.shortestPath(s, graph);
//                        System.out.println("\n========== PRINTING LIST ==========");
//                        for(Pair<String, Integer> pair : dijkstra.shortestPath(s,graph))
//                            System.out.println(db.getName(Integer.parseInt(pair.getKey()), "Subjects") + ": " + pair.getValue());


                        /*
                        run everything in a for loop (for each group of students to be assigned)

                        initialise cost matrix
                        initialise 2D array mask (masks the cost matrix with input of ints)
                            both of these should be the same size (square array with sides of size = no of subjects in db graph)

                            to make square array

                            int[][] m = new int[2][3];
                            int[][] new_m = new int[m[0].length][m[0].length];
                            m[0][0] = 17;
                            m[0][1] = 7;
                            m[0][2] = 49;

                            m[1][0] = 12;
                            m[1][1] = 60;
                            m[1][2] = 60;

                            if(m.length < m[0].length)
                            {
                                new_m = new int[m[0].length][m[0].length];
                                for(int i = 0; i < m.length; i++)
                                    for(int j = 0; j < m[0].length; j++)
                                        new_m[i][j] = m[i][j];

                                System.out.println(Arrays.deepToString(new_m));

                                for(int i = m.length; i < new_m.length; i++)
                                    for(int j = 0; j < m[0].length; j++)
                                        new_m[i][j] = 0;
                            }



                            if unassigned_students.size == subjects_in_subjects_graph.size
                                continue
                            else if unassigned_students.size < subjects_in_subjects_graph.size
                                make cost matrix square by adding columns of infinite cost dummy values until the matrix is square
                            else unassigned_students.size > subjects_in_subjects_graph.size
                                multiple groups

                        for each student in the list of unassigned students
                            dll of ints INDIVIDUAL_COST for: no of subjects done by student --> dll.append(0)
                            for each subject the student does
                                run dijkstra on the entire graph of subjects
                                INDIVIDUAL_COST.combine(dijkstra_output)
                            for each node in INDIVIDUAL_COST
                                divide the value by the no of student (calculates mean)
                                add the cost to the cost array
                                add the names of the subject to the mask array

                        run hungarian
                            get coords of the chosen ints
                            use coords on the masked array to get the subjects chosen
                            to get name of person suggested
                         */

                        /*
                        SELECT SubjectsID, COUNT(*) FROM StudentSubjects GROUP BY StudentSubjects.SubjectsID ORDER BY COUNT(*) DESC
/*
ACTUAL GETTING COSTS
avg the weights to each subject destination from all runs of dijkstra
    eg: if I do comp sci, physics, maths and FM, to get the cost of FM i avg the cost of FM from each of the other subjects

GROUPING STUDENTS TO BE RUN ON HUNGARIAN
if the no of unassigned students > the no of subjects in the subjectGraph
selects the no of students doing each subject
since each row is distinct, students cannot do the same subject
select the 1st subject with the highest count (the subject taken by the most people)
and search for the first X no of people who take it (where X = the no of subjects to ensure a square matrix) using the LIMIT and then subquery no of subjects

                         */