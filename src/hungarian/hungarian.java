/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hungarian;

/**
 *
 * @author rayan
 */
public class hungarian
{
    int[][] cost_matrix;
    
    
    // markers
    
    int[] row_star, col_star;
    
    boolean[] rowCovered, colCovered;
    
    int[] rowPrime;
    
    public hungarian(int[][] matrix)
    {
        this.cost_matrix = matrix;
    }
    
    
    
    /**
     * Step 1 - Row Reduction
     * 
     * ensures there's at least one 0 in a row
     * achieved by subtracting the minima from each number in the row
     */
    private void row_reduction()
    {
        for(int i = 0; i < cost_matrix.length; ++i)
        {
            int rowMin = Integer.MAX_VALUE;
            for(int j = 0; j < cost_matrix[i].length; ++j)
            {
                if(cost_matrix[i][j] < rowMin)
                    rowMin = cost_matrix[i][j];
            }
            
            for(int j = 0; j < cost_matrix[i].length; ++j)
            {
                cost_matrix[i][j] -= rowMin;
            }
        }
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
        // for(int i = 0; i < cost_matrix[0].length; ++i)
        for(int i : cost_matrix[0])
        {
            int colMin = Integer.MAX_VALUE;
            for(int j = 0; j < cost_matrix.length; ++j)
            {
                if(cost_matrix[j][i] < colMin)
                    colMin = cost_matrix[j][i];
            }
            
            for(int j = 0; j < cost_matrix.length; ++j)
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
        
        for(int i = 0; i < cost_matrix.length; ++i)
        {
            // for(int j = 0; j < cost_matrix[0].length; ++j)
            for(int j : cost_matrix[0])
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
                    row_star[i] = i;
                    // save column position of 0
                    col_star[j] = j;
                    
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
        // for(int i = 0; i < col_star.length; ++i)
        for(int i : col_star)
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
    private void prime_uncovered()
    {
        for(int i = 0; i < cost_matrix.length; ++i)
        {
            if(colCovered[i])
                continue;
            
            for(int j : cost_matrix[0])
            {
                
                
                if(cost_matrix[i][j] == 0 && !rowCovered[i])    // if there is a non-covered 0
                {
                    rowPrime[j] = i;

                    if(row_star[j] != -1)    // if the 0* is on the same row as the primed 0
                    {
                        rowCovered[j] = true;
                        //uncover the row of 0*
                        
                        colCovered[row_star[j]] = false;
                    }
                    
                    if(cost_matrix[i][j] == 0 &&)
                }
            }
        }
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
            if(!i)
                return false;
        }
        for(boolean i : rowCovered)
        {
            if(!i)
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