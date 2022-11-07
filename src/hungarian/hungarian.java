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
    
    // marks row/column element that has been reduced to a 0 with a "square"
    int[] zero_row, zero_col;
    //
    int[] rowCovered, colCovered;
    
    public hungarian(int[][] matrix)
    {
        this.cost_matrix = matrix;
    }
    
    /**
     * Step 1 - row reduction
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
     * Step 2 - column reduction
     * 
     * ensures there's at least one 0 in a column
     * achieved by subtracting the minima from each number in the column
     */
    private void col_reduction()
    {
        for(int i = 0; i < cost_matrix[0].length; ++i)  // can use cost_matrix[0] since matrix must be square
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
     * Step 3 - Zero Marking
     * 
     * mark all reduced values that are equal to 0 with a "star"
     * one zero per row and column (one zero per worker job pair)
     */
    private void zeroMarker()
    {
        int[] row_has_starred_zero = new int[cost_matrix.length];
        int[] col_has_starred_zero = new int[cost_matrix[0].length];
        
        for(int i = 0; i < cost_matrix.length; ++i)
        {
            for(int j = 0; j < cost_matrix[0].length; ++j)
            {
                /**
                 * if:
                 *      current (reduced) value is == 0
                 *      no already marked 0s in the row
                 *      no already marked 0s in the column
                 */
                
                if(cost_matrix[i][j] == 0 && row_has_starred_zero[i] == 0 && col_has_starred_zero[j] == 0)
                {
                    // mark that the current row now has a 0
                    row_has_starred_zero[i] = 1;
                    
                    // and the current column now has a 0
                    col_has_starred_zero[j] = 1;
                    
                    // save row position of 0
                    zero_row[i] = j;
                    // save column position of 0
                    zero_col[j] = i;
                    
                    continue;   // next row ( a zero has been discovered )
                }
            }
        }
    }
    
    /**
     * Step 4 - Covering the 0
     * 
     * cover all columns that have "stars"
     */
    private void zeroCover()
    {
        for(int i = 0; i < )
    }
    
    
}
