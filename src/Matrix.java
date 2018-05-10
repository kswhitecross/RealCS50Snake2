import java.util.Random;

/*
** Name : snake.Matrix
** Author : Kyle
** Date : 4/15/18
** Description : A matrix made of doubles with static add, subtract, multiply and divide methods.  Used to provide simple and fast multiplication in the Network class.
*/
public class Matrix {

    private int cols; //number of columns
    private int rows; //number of rows
    private double[][] data; //actual Matrix

    //data constructor
     Matrix(double[][] paramData){
        data = paramData;
        rows = data.length;
        cols = data[0].length;
    }

    //null constructor
     Matrix(int numRows, int numColumns){
        data = new double[numRows][numColumns];
        rows = numRows;
        cols = numColumns;
    }

    //random constructor
     Matrix(int numRows, int numColumns, double mean, double stdDev){
        Random random = new Random();
        if (numRows == 0|| numColumns == 0)
            return;
        data = new double[numRows][numColumns];
        for (int i = 0; i < numRows; i++){
            for (int j = 0; j < data[i].length; j++){
                data[i][j] =  random.nextGaussian() * stdDev + mean;
            }
        }
        rows = numRows;
        cols = numColumns;
    }

    //randomizes a value
     void randomize(int row, int col, double mean, double stdDev){
        Random random = new Random();
        double value = random.nextGaussian() * stdDev + mean;
        data[row][col] = value;
    }

    //accessor method
     double get(int reqRow, int reqCol){
        return data[reqRow][reqCol];
    }

    //mutator method
     void set(double value, int reqRow, int reqCol){
        data[reqRow][reqCol] = value;
    }

    //same dimensions method
     boolean isSameSize(Matrix m){
        if (this.rows != m.rows || this.cols != m.cols)
            return false;
        return true;
    }

    //returns an identity matrix
    public static Matrix identity(int n){
        Matrix identity = new Matrix(new double[n][n]);
        for (int i = 0; i < n; i++)
            identity.set(1,i,i);
        return identity;
    }

    //adds
     static Matrix add(Matrix one, Matrix two){
        if (!one.isSameSize(two))
            throw new ArithmeticException("Matrices are incompatible for addition");
        double temp;
        Matrix sum = new Matrix(one.rows, one.cols);
        for (int i = 0; i < one.rows; i++){
            for (int j = 0; j < one.cols; j++){
                temp = one.get(i,j) + two.get(i,j);
                sum.set(temp,i,j);
            }
        }
        return sum;
    }

    //subtracts
    public static Matrix subtract(Matrix one, Matrix two){
        if (!one.isSameSize(two))
            throw new ArithmeticException("Matrices are incompatible for subtraction");
        return add(one, multiply(-1,two));
    }

    //multiplies by a constant
     static Matrix multiply(double value, Matrix m){
        Matrix product = new Matrix(m.rows, m.cols);
        for (int i = 0; i < m.rows; i++){
            for (int j = 0; j < m.cols; j++){
                product.set(m.get(i,j) * value,i,j);
            }
        }
        return product;
    }

    //multiplies two matrices
     static Matrix multiply(Matrix m, Matrix n){
        if (m.cols != n.rows)
                throw new ArithmeticException("Matrices are incompatible for multiplication");
        Matrix product = new Matrix(m.rows, n.cols);
        double sum;
        //iterates through the rows
        for (int i = 0; i < m.rows; i++){
            //iterates through the columns
            for (int j = 0; j < n.cols; j++){
                //actual calculations
                sum = 0;
                for (int k = 0; k < m.cols; k++){
                    sum += m.get(i,k) * n.get(k,j);
                }
                product.set(sum, i, j);
            }
        }
        return product;
    }

    //applies the sigmoid function to every element of the matrix
     static Matrix sigmoid(Matrix m){
        double value;
        for (int i = 0; i < m.rows; i++){
            for (int j = 0; j < m.cols; j++){
                value = sigmoid(m.get(i,j));
                m.set(value,i,j);
            }
        }
        return m;
    }


    //takes all of the values from 0,0 to r,j from matrix a and the rest from matrix b




     static Matrix merge(Matrix a, Matrix b, int r, int c){
        if (!a.isSameSize(b))
            throw new ArithmeticException("Matrices not the same size");
        Matrix mat = new Matrix(a.rows,a.cols);
        for (int i = 0, n = mat.getRows(); i < n; i++){
            for (int j = 0, m = mat.getCols(); j < m; j++){
                //gets it from the first matrix
                if (i < r || (i == r && j <= c))
                    mat.set(a.get(i,j),i,j);
                //gets it from the second matrix
                else
                    mat.set(b.get(i,j),i,j);
            }
        }
        return mat;
    }

    //sigmoid function, 1/(1 + e^-x)
     private static double sigmoid(double x){
        return 1.0 / (1 + Math.exp(-x));
    }

    //Prints out the array to 3 decimal places
     public String toString(){
        String s = "";

        for (int i = 0; i < this.rows; i++){
            for (int j = 0; j < this.cols; j++){
                s += String.format("%.3f", this.data[i][j]) + "  ";
            }
            s += "\n";
        }
        return s;
    }

     double[][] toArray(){
        return data;
    }

    int getRows() {
        return rows;
    }

    int getCols(){
        return cols;
    }
}
