
package projekalgo;


public class Algo {
	private double[][] originalmatrix; 
	private double[][] clonedmatrix;
	private int[][] lines; 
	private int numberoflines;
	double rowMinValue[];
        double colMinValue[];
	int rows[]; // Index of the column selected by every row (The final result)
	int occupiedCols[]; // Verify that all column are occupied, used in the optimize step
	double minUncoveredValue;
	public Algo(double[][] matrix) {
		// Initialization
		originalmatrix = matrix; // Given matrix
		clonedmatrix = cloneMatrix(matrix); // Cloned matrix to be processed
		rows = new int[clonedmatrix.length];
		occupiedCols = new int[clonedmatrix.length];
		rowMinValue= new double[clonedmatrix.length];
                colMinValue= new double[clonedmatrix.length];
                minUncoveredValue = 0;
		//Algorithm
                findminrow();                           // Step 1
		subtractRow(); 				
                findmincol();                           // Step 2
                subtractcol();				
		coverZero();				// Step 3
		while(numberoflines < clonedmatrix.length){
                        findminuncoveredcell();
			createAdditionalZeros();	// Step 4 (Condition)
			coverZero();			// Step 3 Again (Condition)
		}
		optimize();				// optimize
	}
	

	public void subtractRow(){	
		//subtract minimum from each row using rowMinValue[]
		for(int row=0; row<clonedmatrix.length;row++){
			for(int col=0; col<clonedmatrix.length;col++){
				clonedmatrix[row][col] -= rowMinValue[row];
			}
		}
	} 
	public void findminrow(){
            //get the minimum for each row and store in rowMinValue[]
                    for(int row=0; row<clonedmatrix.length;row++){
			rowMinValue[row] = clonedmatrix[row][0];
			for(int col=1; col<clonedmatrix.length;col++){
				if(clonedmatrix[row][col] < rowMinValue[row])
					rowMinValue[row] = clonedmatrix[row][col];
			}
		}
		
        }//End Step 1

	public void subtractcol(){
		//subtract minimum from each column using colMinValue[]
		for(int col=0; col<clonedmatrix.length;col++){
			for(int row=0; row<clonedmatrix.length;row++){
				clonedmatrix[row][col] -= colMinValue[col];
			}
		}
	} 
        public void findmincol(){
        //get the minimum for each column and store them in colMinValue[]
		for(int col=0; col<clonedmatrix.length;col++){
			colMinValue[col] = clonedmatrix[0][col];
			for(int row=1; row<clonedmatrix.length;row++){
				if(clonedmatrix[row][col] < colMinValue[col])
					colMinValue[col] = clonedmatrix[row][col];
			}
		}
        }//End Step 2
	public void coverZero(){
		numberoflines = 0;
		lines = new int[clonedmatrix.length][clonedmatrix.length];
		
		for(int row=0; row<clonedmatrix.length;row++){
			for(int col=0; col<clonedmatrix.length;col++){
				if(clonedmatrix[row][col] == 0)
					drawline(row, col, maxVH(row, col));
			}
		}
	}
	private int maxVH(int row, int col){
		int result = 0;
		for(int i=0; i<clonedmatrix.length;i++){
			if(clonedmatrix[i][col] == 0)
				result++;
			if(clonedmatrix[row][i] == 0)
				result--;
		}
		return result;
	}

	private void drawline(int row, int col, int maxVH){
		if(lines[row][col] == 2) // if cell is colored twice before (intersection cell), don't color it again
			return;
		
		if(maxVH > 0 && lines[row][col] == 1) // if cell colored vertically and needs to be recolored vertically, don't color it again (Allowing this step, will color the same line (result won't change), but the num of line will be incremented (wrong value for the num of line drawn))
			return;
			
		if(maxVH <= 0 && lines[row][col] == -1) // if cell colored horizontally and needs to be recolored horizontally, don't color it again (Allowing this step, will color the same line (result won't change), but the num of line will be incremented (wrong value for the num of line drawn))
			return;
		
		for(int i=0; i<clonedmatrix.length;i++){ // Loop on cell at indexes [row][col] and its neighbors
			if(maxVH > 0)	// if value of maxVH is positive, color vertically
				lines[i][col] = lines[i][col] == -1 || lines[i][col] == 2 ? 2 : 1; // if cell was colored before as horizontal (-1), and now needs to be colored vertical (1), so this cell is an intersection (2). Else if this value was not colored before, color it vertically
			else			// if value of maxVH is zero or negative color horizontally
				lines[row][i] = lines[row][i] == 1 || lines[row][i] == 2 ? 2 : -1; // if cell was colored before as vertical (1), and now needs to be colored horizontal (-1), so this cell is an intersection (2). Else if this value was not colored before, color it horizontally
		}
		

		numberoflines++;
	}//End step 3
        
	public void createAdditionalZeros(){
		// Subtract min form all uncovered elements, and add it to all elements covered twice
		for(int row=0; row<clonedmatrix.length;row++){
			for(int col=0; col<clonedmatrix.length;col++){
				if(lines[row][col] == 0) // If uncovered, subtract
					clonedmatrix[row][col] -= minUncoveredValue;
				
				else if(lines[row][col] == 2) // If covered twice, add
					clonedmatrix[row][col] += minUncoveredValue;
			}
		}
	} 

        public void findminuncoveredcell(){
                for(int row=0; row<clonedmatrix.length;row++){
			for(int col=0; col<clonedmatrix.length;col++){
				if(lines[row][col] == 0 && (clonedmatrix[row][col] < minUncoveredValue || minUncoveredValue == 0))
					minUncoveredValue = clonedmatrix[row][col];
			}
		}
		
        }// End step 4

	private boolean optimize(int row){
		if(row == rows.length) // If all rows were assigned a cell
			return true;
		
		for(int col=0; col<clonedmatrix.length;col++){ // Try all columns
			if(clonedmatrix[row][col] == 0 && occupiedCols[col] == 0){ // If the current cell at column `col` has a value of zero, and the column is not reserved by a previous row
				rows[row] = col; // Assign the current row the current column cell
				occupiedCols[col] = 1; // Mark the column as reserved
				if(optimize(row+1)) // If the next rows were assigned successfully a cell from a unique column, return true
					return true;
				occupiedCols[col] = 0; // If the next rows were not able to get a cell, go back and try for the previous rows another cell from another column
			}
		}
		return false; // If no cell were assigned for the current row, return false to go back one row to try to assign to it another cell from another column
	}
	
	public boolean optimize(){
		return optimize(0);
	} //End optimize
        
	public double getSum(){
		double total = 0;
		for(int row = 0; row < clonedmatrix.length; row++)
			total += originalmatrix[row][rows[row]];
		return total;
	}

	public double[][] cloneMatrix(double[][] matrix){
		double[][] tmp = new double[matrix.length][matrix.length];
		for(int row = 0; row < matrix.length; row++){
			tmp[row] = matrix[row].clone();
		}
		return tmp;
	}
        
        public void printoptimalsolution(){
                        for(int row = 0; row < clonedmatrix.length; row++){
			  System.out.println("Resource "+(row+1)+" assign to Task "+(rows[row]+1));
                         }
        }
}