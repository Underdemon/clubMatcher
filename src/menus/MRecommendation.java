/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menus;

import algorithms.dijkstra.Dijkstra;
import algorithms.dijkstra.Edge;
import dataStructures.dll.DLL;
import dataStructures.graphs.Graph;
import dataStructures.hashmap.Pair;
import java.util.Arrays;
import java.util.Scanner;

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
                + "\n\t 2 - Assign all students"
                + "\n\t 3 - Assign all unassigned students"
                + "\n\t 4 - Assign a specific student"
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
                    int i = 0;
                    int j = 0;

                    dijkstra.shortestPath("3", graph);
                    for(String s : studentSubjects)
                    {
//                        for(Pair<String, Integer> pair : dijkstra.shortestPath(s))
//                        {
//                            m[i][j] = pair.getValue();
//                            j++;
//                        }
//
//                        i++;
//                        j = 0;

                        if(Integer.parseInt(s) == 3)
                            dijkstra.shortestPath(s, graph);
                        System.out.println("\n========== PRINTING LIST ==========");
                        for(Pair<String, Integer> pair : dijkstra.shortestPath(s,graph))
                            System.out.println(db.getName(Integer.parseInt(pair.getKey()), "Subjects") + ": " + pair.getValue());


                        /*
                        SELECT SubjectsID, COUNT(*) FROM StudentSubjects GROUP BY StudentSubjects.SubjectsID ORDER BY COUNT(*) DESC
/*
if the no of unassigned students > the no of subjects in the subjectGraph
selects the no of students doing each subject
since each row is distinct, students cannot do the same subject
select the 1st subject with the highest count (the subject taken by the most people)
and search for the first X no of people who take it (where X = the no of subjects to ensure a square matrix) using the LIMIT and then subquery no of subjects
                         */
                    }
                    
                    break;

                case 3:
                    break;

                case 4:
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