import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/*
** Name : Snake
** Author : Kyle
** Date : 5/1/18
** Description : A basic snake game mean to be used with computer input from a neural network.  The goal of the game is to stay alive as long as possible while collecting as many "cherries" as possible.
*/
public class Snake {

    //parts of the Snake
    private Point head;
    private List<Point> moves;
    private Point cherry;
    private int currentDir;

    //move controls
    private final int MOVE_LEFT = 0;
    private final int MOVE_UP = 1;
    private final int MOVE_RIGHT = 2;
    private final int MOVE_DOWN = 3;

    //board values
    private final int EMPTY_SPACE = 0;
    private final int SNAKE_BODY = 1;
    private final int CHERRY = 2;
    private final int WALL = 3;
    private final int SCALE = 25;
    private final int DELAY = 5;

    //board
    private int[][] board;
    private int boardHeight;
    private int boardWidth;

    //score values
    private int score;
    private boolean gameEnded;

    //fitness Values
    private int correctMoves;
    private int incorrectMoves;
    private double pointsPerCherry = 10;
    private double pointsPerCorrectMove = 1;
    private double pointsPerIncorrectMove = -1.5;
    private int movesSincePoint;

    //constructor
     Snake(int boardHeight, int boardWidth, double pointsPerCherry, double pointsPerCorrectMove, double pointsPerIncorrectMove){

        //initializes board
        board = new int[boardHeight][boardWidth];
        for (int i = 0; i < boardHeight; i++){
            for (int j = 0; j < boardWidth; j++){
                //walls
                if (i == 0 || i == boardHeight - 1 || j == 0 || j == boardWidth - 1)
                    board[i][j] = WALL;
                else
                    board[i][j] = EMPTY_SPACE;
            }
        }

        //other stuff
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.pointsPerCherry = pointsPerCherry;
        this.pointsPerCorrectMove = pointsPerCorrectMove;
        this.pointsPerIncorrectMove = pointsPerIncorrectMove;
        moves = new ArrayList<>();

        score = 1;
        gameEnded = false;
        correctMoves = 0;
        incorrectMoves = 0;
        movesSincePoint = 0;
    }

    //startup
     private void start(){
        placeHead();
        board[(int)head.getY()][(int)head.getX()] = SNAKE_BODY; //makes sure the cherry is different from the snake
        placeCherry();
        updateBoard();
        currentDir = -1;
    }

    //puts cherry on a random spot not taken by anything else
    //assumes there is a blank spot to put a cherry
    private void placeCherry(){

        Random rand = new Random();
        int x;
        int y;
        do {
            //doesn't include walls
            x = rand.nextInt(boardWidth - 2) + 1;
            y = rand.nextInt(boardHeight - 2) + 1;
        } while (board[y][x] != 0);
        cherry = new Point(x,y);
    }

    //puts the head somewhere
    private void placeHead(){

        Random rand = new Random();
        int x;
        int y;
        do {
            //doesn't include walls
            x = rand.nextInt(boardWidth - 2) + 1;
            y = rand.nextInt(boardHeight - 2) + 1;
        } while (board[y][x] != 0);
        head = new Point(x,y);
    }

    private int getXDisCherry(){
        return (int) Math.abs(cherry.getX() - head.getX());
    }

     private int getYDisCherry(){
        return (int) Math.abs(cherry.getY() - head.getY());
    }

    //prints the board to the screen
    private void printBoard(){
        for (int i = 0; i < boardHeight; i++){
            for (int j = 0; j < boardWidth; j++){
                //colors
                switch (board[i][j]){
                    case 0: System.out.print("\u001b[37m"); break;
                    case 1: System.out.print("\u001b[34m"); break;
                    case 2: System.out.print("\u001b[31m"); break;
                    case 3: System.out.print("\u001b[30m"); break;
                }
                System.out.print(board[i][j] + "\t");
                System.out.print("\u001b[0m");
            }
            System.out.println();
        }
        System.out.println();
    }

    //gets the input
    //sets the current direction
    private void getMove(int direction){
        //hasn't started moving yet
        if (currentDir == -1)
            currentDir = direction;
        //opposite direction of the current direction... do nothing
        if (Math.abs(currentDir - direction) == 2);
        //same direction of current direction... do nothing
        else if (currentDir == direction);
        //different direction
        else currentDir = direction;
    }

    //moves and checks to see if the game has ended
    private void move(){
        //dist the snake would have to travel to get to the cherry
        int oldDist = getXDisCherry() + getYDisCherry();

        if (currentDir < 0 || currentDir > 3)
            throw new RuntimeException("Invalid Direction");

        //if the game isn't over yet
        if (!gameEnded) {
            //adds the heads location to the moves list
            moves.add(0, new Point(head));

            //moves the head
            switch (currentDir) {
                case MOVE_LEFT:
                    head.setLocation(head.getX() - 1, head.getY());
                    break;
                case MOVE_UP:
                    head.setLocation(head.getX(), head.getY() - 1);
                    break;
                case MOVE_RIGHT:
                    head.setLocation(head.getX() + 1, head.getY());
                    break;
                case MOVE_DOWN:
                    head.setLocation(head.getX(), head.getY() + 1);
                    break;
            }

            //checks to see if the score should increase
            if (head.equals(cherry)) {
                score++;
                placeCherry();
                movesSincePoint = 0;
            }
            else
                movesSincePoint++;

            //checks to see if its touching wall
            if (head.getX() == 0 || head.getX() == boardWidth - 1 || head.getY() == 0 || head.getY() == boardHeight - 1)
                gameEnded = true;

            //checks to see if its overlapping itself
            for (int i = 0, n = moves.size(); i < n; i++){
                if (head.equals(moves.get(i)))
                    gameEnded = true;
            }

            //deletes the extra moves
            //less than the score so the head is included in the size
            while (moves.size() >= score)
                moves.remove(moves.size() - 1);

            int newDist = getXDisCherry() + getYDisCherry();
            if (newDist > oldDist)
                incorrectMoves++;
            else
                correctMoves++;

            //======LOOP DETECTORS======\\
            //if its taken over 1000 moves and doesn't have enough points
            if (movesSincePoint >= boardHeight * boardWidth)
                gameEnded = true;

        }
    }

    private boolean isGameEnded(){
        return gameEnded;
    }

    private void updateBoard(){
        //draws the zeros
        for (int i = 1; i < boardHeight - 1; i++){
            for (int j = 1; j < boardWidth - 1; j++){
                board[i][j] = EMPTY_SPACE;
            }
        }

        //draws the snake
        board[(int)head.getY()][(int)head.getX()] = SNAKE_BODY;
        for (int i = 0, n = moves.size(); i < n; i++){
            board[(int) moves.get(i).getY()][(int) moves.get(i).getX()] = SNAKE_BODY;
        }

        //draws the cherry
        board[(int)cherry.getY()][(int)cherry.getX()] = CHERRY;
    }

    private int getScore(){
        return score;
    }

    private int[][] getBoard(){
        return board;
    }

    public double[][] getBoardAsDoubles(){
        double[][] arr = new double[boardHeight][boardWidth];
        for (int i = 0; i < arr.length; i++){
            arr[i] = Arrays.stream(board[i]).asDoubleStream().toArray();
        }
        return arr;
    }

    //returns a Matrix of features that represents how the snake would see the board
    /*
    Is the space to the left clear?
    Is the space to above clear?
    Is the space to the right clear?
    Is the space below clear?
    Is the cherry to the left?
    Is the cherry above?
    Is the cherry to the right?
    Is the cherry below?
     */
    private Matrix getFeatures(){
        double temp;
        Matrix features = new Matrix(8,1);
        //left clear?
        temp = (board[(int)head.getY()][(int)head.getX() - 1] == 0 || board[(int)head.getY()][(int)head.getX() - 1] == 2) ? 1.0 : 0.0;
        features.set(temp,0,0);
        //above clear?
        temp = (board[(int)head.getY() - 1][(int)head.getX()] == 0 || board[(int)head.getY() - 1][(int)head.getX()] == 2) ? 1.0 : 0.0;
        features.set(temp,1,0);
        //right clear?
        temp = (board[(int)head.getY()][(int)head.getX() + 1] == 0 || board[(int)head.getY()][(int)head.getX() + 1] == 2) ? 1.0 : 0.0;
        features.set(temp,2,0);
        //below clear?
        temp = (board[(int)head.getY() + 1][(int)head.getX()] == 0 || board[(int)head.getY() + 1][(int)head.getX()] == 2) ? 1.0 : 0.0;
        features.set(temp,3,0);
        //cherry left?
        temp = (head.getX() > cherry.getX()) ? 1.0 : 0.0;
        features.set(temp,4,0);
        //cherry above?
        temp = (head.getY() > cherry.getY()) ? 1.0 : 0.0;
        features.set(temp,5,0);
        //cherry right?
        temp = (head.getX() < cherry.getX()) ? 1.0 : 0.0;
        features.set(temp,6,0);
        //cherry below?
        temp = (head.getY() < cherry.getY()) ? 1.0 : 0.0;
        features.set(temp,7,0);
        return features;
    }

    //returns the calculated fitness score
    private double getGameFitness(){
        double temp = 0;

        temp += (score - 1) * pointsPerCherry; //score starts at one to keep size at least one
        temp += correctMoves * pointsPerCorrectMove;
        temp += incorrectMoves * pointsPerIncorrectMove;

        //temp += (correctMoves + incorrectMoves);
        return temp;
    }

    //plays a game with no printing or painting
     double playNetworkGame(Network n){
        int networkMove;
        start();
        do{
            updateBoard();
            //printBoard();
            networkMove = parseMatrix(n.feedforward(getFeatures()));
            getMove(networkMove);
            move();
        } while (!isGameEnded());
        updateBoard();
        //printBoard();
        n.setFitness(getGameFitness());
        //System.out.println("Score: " + getScore());
        //System.out.println("Fitness: " + getGameFitness());
        return getGameFitness();
    }

    //plays a game and prints all of the moves to the log as arrays
     void printPlayNetworkGame(Network n){
        int networkMove;
        start();
        do{
            updateBoard();
            printBoard();
            networkMove = parseMatrix(n.feedforward(getFeatures()));
            getMove(networkMove);
            move();
        } while (!isGameEnded());
        updateBoard();
        printBoard();
        n.setFitness(getGameFitness());
        System.out.println("Score: " + getScore());
        System.out.println("Fitness: " + getGameFitness());
    }

    //plays a game and shows all of the moves on a JFrame with graphics
     void paintPlayNetworkGame(Network n){
        JPanel game = new JPanel(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                drawBoard(g);
            }

             void drawBoard(Graphics g){
                int temp;
                for (int i = 0; i < getBoard().length; i++)
                {
                    for (int j = 0; j < getBoard()[i].length; j++)
                    {
                        temp = getBoard()[i][j];
                        drawRect(temp,j,i,g);
                    }
                }
            }

             void drawRect(int colorInt, int x, int y, Graphics g){
                Color color = null;
                if (colorInt == 0) //background
                    color = new Color(255, 255, 255);
                else if (colorInt == 1) { //snake
                    if (!gameEnded)
                        color = new Color(0, 0, 0);
                    else
                        color = new Color(255,0, 4); }
                else if (colorInt == 2) //Cherry
                    color = new Color(39,255, 44);
                else if (colorInt == 3)
                    color = new Color(128, 128, 128);
                g.setColor(color);
                g.fillRect(x*SCALE,y*SCALE,SCALE,SCALE);
            }
        };
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(boardWidth*SCALE,boardHeight*SCALE + 20);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);

        int networkMove;
        start();
        do{
            updateBoard();
            try {
                Thread.sleep(DELAY);
            } catch (Exception e){}
            game.repaint();
            networkMove = parseMatrix(n.feedforward(getFeatures()));
            getMove(networkMove);
            move();
        } while (!isGameEnded());
        updateBoard();
        game.repaint();
        n.setFitness(getGameFitness());
        System.out.println("Score: " + getScore());
        System.out.println("Fitness: " + getGameFitness());
        try {
            Thread.sleep(2000);
        } catch (Exception e){}
        frame.dispose();
    }

    //parses the output Matrix from a network to play snake
    private int parseMatrix(Matrix m){
        double largest = m.get(0,0);
        int largestIndex = 0;
        for (int i = 0, n = m.getRows(); i < n; i++){
            if (m.get(i,0) > largest){
                largest = m.get(i,0);
                largestIndex = i;
            }
        }
        switch (largestIndex){
            case 0: return MOVE_LEFT;
            case 1: return MOVE_UP;
            case 2: return MOVE_RIGHT;
            case 3: return MOVE_DOWN;
        }
        throw new RuntimeException("Output size invalid or not 4");
    }
}
