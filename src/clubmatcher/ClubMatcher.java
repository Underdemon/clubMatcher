/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clubmatcher;

import BST.BST;
import hashmap.hashMap;
import hungarian.hungarian;
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
        // TODO code application logic herehow to do
        
        int[][] m = new int[3][3];
        
        m[0][0] = 8;
        m[0][1] = 12;
        m[0][2] = 98;
        
        m[1][0] = 34;
        m[1][1] = 18;
        m[1][2] = 33;
        
        m[2][0] = 10;
        m[2][1] = 73;
        m[2][2] = 22;
        
        hungarian h = new hungarian(m);
        h.optimalAssignment();
    }
    
}
