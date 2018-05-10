README.md
Kyle Whitecross
5/9/18

HOW TO COMPILE AND RUN THE PROGRAMS : 

Unfortunately, Ubuntu does not contain Java 1.8 by default (which the program is written in) so we must install it.
`$ sudo add-apt-repository ppa:openjdk-r/ppa`
`$ sudo apt-get update`
`$ sudo apt-get install openjdk-8-jdk`

You can verify that the installed version of java is now 1.8 with:
`$ javac -version`

These commands will install a different JDk which we can use to run the program.

Then, to compile all of the files we can use the command
`$ javac -d /home/ubuntu/workspace/RealCS50Snake/out /home/ubuntu/workspace/RealCS50Snake/src/*.java`
to save the compiled files to the first folder.

To run the files, change to the directory with the compiled files and execute:
`java -cp . <ClassName> <filelocation>`
where the file location is the location of the file with the pre-saved snakes.

To run Demo:
`java -cp . Demo <filelocation>`

To run GeneticAlgorithm:
`java -cp . GeneticAlgorithm <filelocation>`



RealCS50Snake is a project designed to evolve a simple neural network to play the old arcade game Snake.

Snake is a simple game played on a grid of spaces.  The player uses arrow keys to control a line called the snake and move it across the board.  The rules are simple : 
 1. The world is a grid.
 2. The snake can only travel horizontally and vertically along this grid.
 3. This world has a border that kills the snake on contact.
 4. The snake cannot stop moving.
 5. If the snake runs into itself, it dies.
 6. Every time the snake eats a cherry, it grows longer.
 7. The goal is to grow as long as possible.
 
This project has two main programs.  Demo is a simple program designed to graphically demonstrate how a neural network plays snake.  It loads previously saved networks from a file and has them play snake.  The first game played will always be played with the "best network," the network with the highest tested fitness, and all other games will be played with a network randomly selected from the specified file.  Demo has 6 parameters at the top of the file that can be configured.

`int boardHeight` : This parameter controls how many boxes tall the grid the networks will play snake on will be.  The default value is 20.

`int boardWidth` : This parameter controls how many boxes wide the grid the networks will play snake on will be.  The default value is 20.

`double pointsPerCherry` : This parameter controls how many points the network will be awarded every time the snake eats a cherry.  The default value is 10.

`double pointsPerCorrectMove` : This parameter controls how many points the network will be awarded each time the snake moves towards the cherry, so that the distance between the cherry and the head of the snake decreases.  The default value is 1.

`double pointsPerIncorrectMove` : This parameter controls how many points the network is awarded each time the snake moves away from the cherry, so that the distance between the cherry and the head of the snake increases.  The default value is -1.5.

 `int gamesToPlay`: This parameter controls the amount of games the network will play after the first game is played.  In total, the program will play this number + 1 games.  The default value is 50.
 
 `String filePath` : This parameter defines the file location of the file to load networks from.  If the file does not exist the program will throw an exception.  The default value is "Saved Snakes/SuperSnakes.txt"
 
 Demo will run until completion.
 
 Genetic algorithm is a more complicated program intended to use natural selection to evolve one or more networks to play snake.  It creates an initial population of random networks and evaluates them based on their performance in snake.  Every generation, the best snakes survive and crossover with each other to produce new snakes.  In addition, each snake has a chance to mutate randomly to introduce new potentially good genes into the population.  The program will keep running until a snake has a fitness value above a certain threshold.  It is then tested multiple times to ensure it was not just lucky the first time. If the network continues to test above a certain threshold, then it is shown playing snake, and can be saved to a file.  In addition, previously saved snakes can be loaded into the initial population to jump-start it.
 
 Every generation, average fitness of all networks, the fitness of the best network, and the generation number will be printed out.  If a network is tested additionally, the average fitness over all its tests will be displayed.  If a network is saved to a file, the file location will be printed out.
 
 `double mean` : This parameter controls what the mean of any of the random numbers generated to be used as weights or biases will be.  The default value is 0.
 
 `double standardDeviation` : This parameter controls what the standard deviation of any of the random numbers generated to be used as weights or biases will be.  The default value is 1.
 
 `int[] networkArchitecture` : This parameter controls the how structure of all of the networks will be initialized.  The first number represents the input layer and must be set to 8.  The last number represents the output layer and must be set to 4.  There can be any number of hidden layers in between with any number of neurons in each.  The default value is [8,40,4].
 
 `double mutationChance` : This parameter controls how likely each value in each network is to mutate to a new random value at the end of each generation.  For example a value of 0.01 would mean a 1% chance of mutation.  The default value is 0.01.
 
 `int populationSize` : This parameter controls how many networks will be in the population for each generation.  This value must be equal to numSurvivors + numSurvivors * numCrossovers / 2.  Note that a larger population size will significantly decrease performance.  The default value is 1000.
 
 `int numSurvivors` : This parameter controls how many networks will "survive" and advance to the next generation.  For example, a value of 100 means the 100 best networks will advance to the next generation and the rest will be deleted.  The default value is 500.
 
 `int numCrossovers` : This parameter controls how many children each pair of networks will have.  For example if 500 networks survive, and numCrossovers is 2, then 500 new networks will be created.  Each crossover is random, and having more than one crossover does not mean more than multiple identical networks.  The default value is 2.
 
 `boolean saveNetworks` : This parameter controls if networks should be saved to a file or not if it has a high enough tested fitness.  The default value is true.
 
 `double minSuperSnakeFitness` : This parameter controls how high a networks tested fitness has to be to be saved to a file.  If the network scores above this number, then it will be saved (assuming saveNetworks is true).  The default value is 700.
 
 `boolean loadNetworks` : This parameter controls if networks should be loaded into the initial population from the specified file or not.  The default value is false.
 
 `int superSnakeToLoadFitness` : This parameter controls the minimum tested fitness a network needs to have to be loaded into the initial population.  For example, if this value is set to 600, then all of the networks in the specified file with a fitness above 600 will be loaded into the initial population.  The default value is 700.
 
 `double maximumFitness` : This parameter controls the maximum fitness a network will have to test above to end the program.  If the network has a tested fitness above this value then the algorithm stops and that network plays a final game of snake with graphics.  The default value is 800.
 
 `int boardHeight` : This parameter controls how many boxes tall the grid the networks will play snake on will be.  The default value is 20.

 `int boardWidth` : This parameter controls how many boxes wide the grid the networks will play snake on will be.  The default value is 20.
 
 `int testGames` : This parameter controls how many games a network will have to play to calculate its tested fitness.  For example, if this value is 20, then the tested fitness of the network will be set to the average fitness of all those games.  The default value is 20.
 
 `int maxGenerations` : This parameter controls the maximum number of generations the program will run if a network does not test above the maximum fitness.  If this number is reached, then the best network that has been evolved so far will play a game with graphics and the program will end.
 
 `String filePath` : This parameter specifies the location of the file to save networks to or load networks from.  It will be used if no commandline argument is used.  The default value is set to the included SuperSnakes.txt file with its pre-loaded networks.
 
`double pointsPerCorrectMove` : This parameter controls how many points the network will be awarded each time the snake moves towards the cherry, so that the distance between the cherry and the head of the snake decreases.  The default value is 1.

`double pointsPerIncorrectMove` : This parameter controls how many points the network is awarded each time the snake moves away from the cherry, so that the distance between the cherry and the head of the snake increases.  The default value is -1.5.

 `int gamesToPlay`: This parameter controls the amount of games the network will play after the first game is played.  In total, the program will play this number + 1 games.  The default value is 50.
 
 GeneticAlgorithm will run until completion
 
 Although a sample file with pre-loaded networks is already included, the networks included barely scratch the surface of what this program can do.  I encourage you to play and experiment with the hyper parameters and the source code to try and evolve the best network possible!
 
 Enjoy!