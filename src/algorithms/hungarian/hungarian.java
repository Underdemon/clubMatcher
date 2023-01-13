/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.hungarian;

import dataStructures.dll.DLL;
import dataStructures.hashmap.Pair;

import java.util.Arrays;
/**
 *
 * @author rayan
 */
public class Hungarian
{
    int[][] cost_matrix;
    int[][] original_matrix;
    
    // markers
    
    int[] row_star, col_star;
    
    int[] row_prime;
    
    boolean[] rowCovered, colCovered;
    
    
    
    public Hungarian(int[][] matrix)
    {
        if(matrix.length != matrix[0].length)
        {
            try
            {
                throw new IllegalAccessException("The matrix is not square!");
            }
            catch (IllegalAccessException ex)
            {
                System.err.println(ex);
                System.exit(1);
            }
        }
        this.cost_matrix = matrix;
        
        original_matrix = Arrays.stream(cost_matrix).map(int[]::clone).toArray(int[][]::new);
        
        row_star = new int[matrix.length];
        row_prime = new int[matrix.length];
        col_star = new int[matrix[0].length];
        
        rowCovered = new boolean[matrix.length];
        colCovered = new boolean[matrix[0].length];
        
        Arrays.fill(row_star, -1);
        Arrays.fill(col_star, -1);
        Arrays.fill(row_prime, -1);
        Arrays.fill(rowCovered, false);
        Arrays.fill(colCovered, false);
    }
    
    
    
    public DLL<Pair<Integer, Integer>> optimalAssignment()
    {
        row_reduction();
        
        col_reduction();
        
        init_task_assign();
        
        cover_zero_col();
        
        while(!is_all_columns_covered())
        {
            int[] chosen_zero = prime_uncovered();
            while(chosen_zero == null)
            {
                post_reduction();
                chosen_zero = prime_uncovered();
                System.out.println(Arrays.deepToString(cost_matrix));
            }

            if(row_star[chosen_zero[0]] == -1)
            {
                zero_pathing(chosen_zero);
                cover_zero_col();
            }
            else
            {
                rowCovered[chosen_zero[0]] = true;
                colCovered[row_star[chosen_zero[0]]] = false;
                post_reduction(); 
           }
        }

        DLL<Pair<Integer, Integer>> optimalAssignment = new DLL<>();

        System.out.println("\n");
        for(int i = 0; i < cost_matrix.length; i++)
        {
            optimalAssignment.append(new Pair<Integer, Integer>(col_star[i], row_star[col_star[i]]));
        }

        // System.out.println(Arrays.deepToString(optimalAssignment));
        return optimalAssignment;
    }
    
    
    
    /**
     * Step 1 - Row Reduction
     * 
     * ensures there's at least one 0 in a row
     * achieved by subtracting the minima from each number in the row
     */
    private void row_reduction()
    {
        for(int i = 0; i < cost_matrix.length; i++)
        {
            int rowMin = Integer.MAX_VALUE;
            for(int j = 0; j < cost_matrix[i].length; j++)
            {
                if(cost_matrix[i][j] < rowMin)
                    rowMin = cost_matrix[i][j];
            }
            
            for(int j = 0; j < cost_matrix[i].length; j++)
            {
                cost_matrix[i][j] -= rowMin;
            }
        }
        
        System.out.println(Arrays.deepToString(cost_matrix));
    }
    
    
    
    /**
     * Step 2 - Column Reduction
     * 
     * ensures there's at least one 0 in a column
     * achieved by subtracting the minima from each number in the column
     * 
     * note: // can use cost_matrix[0] since matrix must be square
     */
    private void col_reduction()
    {
        // for(int i = 0; i < cost_matrix[0].length; i++)
        for(int i = 0; i < cost_matrix[0].length; i++)
        {
            int colMin = Integer.MAX_VALUE;
            for(int j = 0; j < cost_matrix.length; j++)
            {
                if(cost_matrix[j][i] < colMin)
                    colMin = cost_matrix[j][i];
            }
            
            for(int j = 0; j < cost_matrix.length; j++)
            {
                cost_matrix[j][i] -= colMin;
            }
        }
    }
    
    
    
    /**
     * Step 3 - Zero Marking/Initial Task Assignments
     * 
     * mark all reduced values that are equal to 0 with a "star"
     * one zero per row and column (one zero per worker job pair)
     */
    private void init_task_assign()
    {
        int[] row_has_starred_zero = new int[cost_matrix.length];
        int[] col_has_starred_zero = new int[cost_matrix[0].length];
        
        for(int i = 0; i < cost_matrix.length; i++)
        {
            // for(int j = 0; j < cost_matrix[0].length; j++)
            for(int j = 0; j < cost_matrix[0].length; j++)
            {
                
                /**
                 * if:
                 *      current (reduced) value is == 0         (minimum relative cost)
                 *      no already marked 0s in the row         (one 0 per row)
                 *      no already marked 0s in the column      (one 0 per column)
                 */
                
                if(cost_matrix[i][j] == 0 && row_has_starred_zero[i] == 0 && col_has_starred_zero[j] == 0)
                {
                    // mark that the current row now has a 0
                    row_has_starred_zero[i] = 1;
                    
                    // and the current column now has a 0
                    col_has_starred_zero[j] = 1;
                    
                    // save row position of 0
                    row_star[i] = j;
                    // save column position of 0
                    col_star[j] = i;
                    
                    continue;   // next row ( a zero has been discovered )
                }
            }
        }
    }
    
    
    
    /**
     * Step 4 - Covering the 0
     * 
     * cover all columns that have "stars" by marking it with a 1
     */
    private void cover_zero_col()
    {
        for(int i = 0; i < col_star.length; i++)
        {
            /**
             * ternary conditional operators
             * 
             * https://stackoverflow.com/a/10336927
             * https://en.wikipedia.org/wiki/Ternary_conditional_operator
             * 
             * when to use it (can use it on chained condition - multiple conditions - but don't use it on nested conditions)
             * https://stackoverflow.com/questions/160218/to-ternary-or-not-to-ternary
             * 
             * ==============================
             * TRANSLATION OF STATEMENT BELOW
             * ==============================
             * 
             * colCovered[i] = col_star[i] != -1 ? 1 : 0;
             * 
             * ================================
             * IS TRANSLATED TO (IN LONG FORM) |
             * ================================
             * 
             * if(col_star[i] != -1         // all arrays filled with -1 as default values
             * {
             *      colCovered[i] = true;
             * }
             * else
             * {
             *      colCovered[i] = false;
             * }
             * 
             */
            
            colCovered[i] = (col_star[i] != -1) ? true : false;
        }
    }
    
    
    
    /**
     * Step 5 - Priming the Uncovered
     * 
     * find a non-covered 0 and "prime" it
     * 
     * if the to-be-primed 0 is on the same row as an already starred 0 then:
     *      cover the corresponding row
     *      uncover the column of the starred 0
     */
    
    private int[] prime_uncovered()
    {
        for(int i = 0; i < cost_matrix.length; i++)
        {
            if(!rowCovered[i])
            {
                for(int j = 0; j < cost_matrix[0].length; j++)
                {            
                    if(cost_matrix[i][j] == 0 && !colCovered[j])    // if there is a non-covered 0
                    {
                        row_prime[i] = j;

                        return new int[]{i, j};
                    }
                }
            }
        }
        
        return null;
    }
    
    
    
    /**
     * Step 5.5 - Post Reduction
     * 
     * The function runs on the condition that the prime_uncovered() function returns -1
     * The function has 3 steps to it
     * 
     * 1) find the minimum uncovered value in the matrix
     * 2) subtract the minimum value from all uncovered values
     * 3) add the minimum value to all twice covered values (covered with both a row & column)
     */
    private void post_reduction()
    {
        int uncovered_min = Integer.MAX_VALUE;
        for(int i = 0; i < cost_matrix.length; i++)
        {
            if(rowCovered[i])
                continue;
            
            for(int j = 0; j < cost_matrix[i].length; j++)
            {
                if((cost_matrix[i][j] < uncovered_min) && !colCovered[j])
                    uncovered_min = cost_matrix[i][j];
            }
        }
        
        if(uncovered_min > 0)
        {
            for(int i = 0; i < cost_matrix.length; i++)
            {
                for(int j = 0; j < cost_matrix[i].length; j++)
                {
                    if(rowCovered[i] && colCovered[j])
                    {
                        cost_matrix[i][j] += uncovered_min;
                    }
                    else if(!rowCovered[i] && !colCovered[j])
                    {
                        cost_matrix[i][j] -= uncovered_min;
                    }
                }
            }
        }
    }
    
    
    
    private void zero_pathing(int[] chosen_zero)
    {
        int i = chosen_zero[0];
        int j = chosen_zero[1];
        
        DLL<int[]> dll = new DLL<>();
        dll.append(chosen_zero);
        boolean found = false;
        
        do
        {
            if(col_star[j] != -1)
            {
                dll.append(new int[]{col_star[j], j});
                found = true;
            }
            else
                found = false;
            
            if(!found) {break;}
            
            i = col_star[j];
            j = row_prime[i];
            
            if(j != -1)
            {
                dll.append(new int[]{i, j});
                found = true;
            }
            else
                found = false;
        }
        while(found);
        
        for(int[] zero : dll)
        {
            if(col_star[zero[1]] == zero[0])
            {
                row_star[zero[0]] = -1;
                col_star[zero[1]] = -1;
            }

            if(row_prime[zero[0]] == zero[1])
            {
                row_star[zero[0]] = zero[1];
                col_star[zero[1]] = zero[0];
            }
        }
        
        Arrays.fill(row_prime, -1);
        Arrays.fill(rowCovered, false);
        Arrays.fill(colCovered, false);
    }
    
    
    /**
     * The Ultimate Step - Final Destination
     * 
     * If all columns are covered (there is one 0 in each column)
     * Then the optimal solution to the assignment problem has been solved
     */
    private boolean is_all_columns_covered()
    {
        for(boolean i : colCovered)
        {
            if(i == false)
                return false;
        }
        return true;
    }
}

/*
    GENERAL NOTES:

    enhanced for loops used in 1D arrays
        2D arrays would require nested enhanced for loops, at which point it is easier to use a normal for loop

    LINE 183:
        colCovered[i] == 0 OR colCovered[j] == 0
        INDEX I OR J????
*/