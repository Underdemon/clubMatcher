/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clubmatcher;

import algorithms.dijkstra.edge;
import algorithms.dijkstra.dijkstra;
import algorithms.hungarian.hungarian;
import dataStructures.BST.BST;
import dataStructures.dll.dll;
import dataStructures.graphs.graph;
import dataStructures.hashmap.hashMap;
import dataStructures.pQueue.pQueue;
import databaseConnect.databaseConnect;
import java.io.File;

/**
 *
 * @author rayan
 */
public class ClubMatcher
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // TODO code application logic here   
        
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
        
        
        graph<String> graph = new graph<>();
        graph.add("a", "b", 4);
        graph.add("a", "c", 2);
        graph.add("b", "c", 3);
        graph.add("b", "d", 2);
        graph.add("b", "e", 3);
        graph.add("c", "b", 1);
        graph.add("c", "d", 4);
        graph.add("c", "e", 5);
        graph.add("e", "d", 1);
        
        
        dijkstra d = new dijkstra(graph);
        d.shortestPath("a");
        
        
        graph<String> graph = new graph<>();
        graph.add("a", "b", 5);
        graph.add("a", "c", 3);
        graph.add("a", "d", 10);
        graph.add("b", "d", 6);
        graph.add("c", "b", 1);
        graph.add("c", "d", 4);
        
        
        dijkstra d = new dijkstra(graph);
        d.shortestPath("a");
        
        
        
        */
        
        File file = new File("clubMatcher.db");
        file.delete();
        
        databaseConnect db = new databaseConnect();
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
    }
}
