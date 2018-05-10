import java.util.List;
import java.util.Random;

/*
** Name : Demo
** Author : Kyle
** Date : 5/7/18
** Description : Plays a bunch of games with random networks.  The first game is with the best network and the next ones are with a randomly selected network.  Meant to be used as a visual demo program.
*/
 class Demo {
    public static void main(String[] args) throws Exception{

        //parameters
        int boardHeight = 30;
        int boardWidth = 50;
        double pointsPerCherry = 10;
        double pointsPerCorrectMove = 1;
        double pointsPerIncorrectMove = -1.5;
        int gamesToPlay = 50;
        String filePath = "Saved Snakes/SuperSnakes.txt";


        //loads file name from argument, if there is one
        if (args.length > 1) {
            System.out.println("Invalid arguments");
            return;
        }
        else if (args.length == 1)
            filePath = args[0];

        //instance data
        int rand;
        SuperSnakeLoader loader = new SuperSnakeLoader(filePath);
        Random random = new Random();
        Network bestNetwork = loader.loadBestNetwork();
        List<Network> networks = loader.loadBestNetworks(0);
        Snake snake = new Snake(boardHeight,boardWidth,pointsPerCherry,pointsPerCorrectMove,pointsPerIncorrectMove);

        //playing games
        snake.paintPlayNetworkGame(bestNetwork);
        for (int i = 0; i < gamesToPlay; i++){
            rand = random.nextInt(networks.size());
            snake = new Snake(boardHeight,boardWidth,pointsPerCherry,pointsPerCorrectMove,pointsPerIncorrectMove);
            snake.paintPlayNetworkGame(networks.get(rand));
        }
    }
}
