import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
** Name : snake.GeneticAlgorithm
** Author : Kyle
** Date : 4/27/18
** Description : An algorithm meant to simulate natural selection to evolve networks to play the old video game "snake" very well.  The algorithm is completely dependent on all of the hyper parameters below.
*/
public class GeneticAlgorithm {




    public static void main(String[] args){

        //hyper parameters
        double mean = 0; //mean value for initial network parameters
        double standardDeviation = 1; //standard deviation value for for initial network parameters
        int[] networkArchitecture = {8,40,4}; //Network architecture, represents the amount of neurons in each layer
        double mutationChance = 0.01; //chance of a random mutation occurring and changing a network parameter
        int populationSize = 1000;  //size of the population for each generation.  Must be equal to numSurvivors + numSurvivors / 2 * numCrossovers
        int numSurvivors = 500; //Number of networks that "survive" and are added to the next generation
        int numCrossovers = 2; //Number of new networks created by each pair of survivors and added to the population
        boolean saveNetworks = true; //will save good networks to a file if this is true
        double minSuperSnakeFitness = 700; //saves a snake to the file if its tested fitness is >= this number
        boolean loadNetworks = false; //will load good networks from this file if this is true
        int superSnakeToLoadFitness = 700; //loads networks from SuperSnakes if they're >= this tested fitness
        double maximumFitness = 800; //Fitness required to end the program if reached
        int boardHeight = 20; //Height of the board networks will be tested on
        int boardWidth = 20; //Width of the board networks will be tested on
        int testGames = 20; //Number of games each network will have to play to be saved to a file or end the program
        int maxGenerations = 1000; //Maximum number of generations the program will go through before terminating
        String filePath = "Saved Snakes/SuperSnakes.txt"; //File path of the file to load and save networks to
        double pointsPerCherry = 10; //The amount of fitness points a network will be awarded for "eating" a cherry
        double pointsPerCorrectMove = 1; //The amount of fitness points a network will be awarded for moving towards a cherry
        double pointsPerIncorrectMove = -1.5; //Tha amount of fitness points a network will be awarded for moving away from a cherry

        //loads file name from argument, if there is one
        if (args.length > 1) {
            System.out.println("Invalid arguments");
            return;
        }
        else if (args.length == 1)
            filePath = args[0];

        //other data
        List<Network> population = new ArrayList<>();
        List<Network> lastPopulation = new ArrayList<>();
        Network bestNetworkForGen = new Network(networkArchitecture,0,1);
        Network bestNetworkForTest = new Network(networkArchitecture,0,0);
        SuperSnakeLoader loader = new SuperSnakeLoader(filePath);
        Snake snake;
        Network temp;
        int generations = 0;
        double avg = 0;
        double testAvg;

        //Output Display
        System.out.println("###############HYPER PARAMETERS##############");
        System.out.println("Initial Network parameter mean: " + mean);
        System.out.println("Initial Network parameter Standard Deviation: " + standardDeviation);
        System.out.println("Network Structure: " + Arrays.toString(networkArchitecture));
        System.out.println("Chance of Mutation: " + mutationChance);
        System.out.println("Population Size: " + populationSize);
        System.out.println("Number of Survivors per Generation: " + numSurvivors);
        System.out.println("Number of Crossovers per Survivor Pair: " + numCrossovers);
        System.out.println("Save networks to " + filePath + ": " + saveNetworks);
        System.out.println("Minimum Fitness Required to save network: " + minSuperSnakeFitness);
        System.out.println("Load networks from " + filePath + ": " + loadNetworks);
        System.out.println("Maximum fitness to stop at: " + maximumFitness);
        System.out.println("Board Dimensions: " + boardHeight + "x" + boardWidth);
        System.out.println("Number of games to Demo best Network: " + testGames);
        System.out.println("Maximum generations to run: " + maxGenerations);
        System.out.println("Fitness points per Cherry eaten: " + pointsPerCherry);
        System.out.println("Fitness points per move towards Cherry: " + pointsPerCorrectMove);
        System.out.println("Fitness points per move away from Cherry: " + pointsPerIncorrectMove);
        System.out.println("###############HYPER PARAMETERS##############");
        System.out.println();



        //adds super snakes to the population
        if (loadNetworks) {
            population.addAll(loader.loadBestNetworks(superSnakeToLoadFitness, networkArchitecture));
            System.out.println("Super Snakes Loaded : " + population.size());
        }

        //Creates initial networks
        bestNetworkForTest.setTestedFitness(0);
        for (int i = 0, n = populationSize - population.size(); i < n; i++){
            temp = new Network(networkArchitecture,mean,standardDeviation);
            population.add(temp);
        }

        //Evolves the species
        do {
            do {
                //get fitness's for every network
                for (int i = 0, n = population.size(); i < n; i++) {
                    snake = new Snake(boardHeight, boardWidth,pointsPerCherry,pointsPerCorrectMove,pointsPerIncorrectMove);
                    avg += snake.playNetworkGame(population.get(i));
                }

                //sort networks based on fitness in ascending order
                for (int i = 0, n = population.size(); i < n; i++) {
                    Collections.sort(population);
                }
                Collections.reverse(population);

                //stores highest fitness and best network
                //assumes the population list has its best network at last index
                if (population.get(0).getFitness() > bestNetworkForGen.getFitness())
                    bestNetworkForGen = population.get(0);

                //stores the last population
                lastPopulation.clear();
                for (int i = 0; i < populationSize; i++) {
                    lastPopulation.add(population.remove(0));
                }
                //take the top survivors and cross them over
                for (int i = 0; i < numSurvivors; i += 2) {
                    for (int j = 0; j < numCrossovers; j++) {
                        population.add(Network.crossover(lastPopulation.get(i + 1), lastPopulation.get(i + 1)));
                    }
                }

                //adds the survivors back to the population
                for (int i = 0; i < numSurvivors; i++)
                    population.add(lastPopulation.remove(0));

                //mututates all of the new population
                for (int i = 0; i < populationSize; i++) {
                    population.get(i).mutate(mutationChance);
                }

                System.out.print("AVG: " + avg / populationSize + ", \tBest: " + bestNetworkForGen.getFitness() + ", \tGen: " + (++generations));
                if (bestNetworkForGen.getFitness() < maximumFitness)
                    System.out.println();

                avg = 0;

            } while (bestNetworkForGen.getFitness() < maximumFitness && generations < maxGenerations);

            testAvg = 0;
            for (int i = 0; i < testGames; i++) {
                snake = new Snake(boardHeight, boardWidth,pointsPerCherry,pointsPerCorrectMove,pointsPerIncorrectMove);
                testAvg += snake.playNetworkGame(bestNetworkForGen);
            }

            testAvg = testAvg / testGames;
            bestNetworkForGen.setTestedFitness(testAvg);
            System.out.print("\t TestAvg: " + testAvg);


            //stores best network
            if (bestNetworkForGen.getTestedFitness() > bestNetworkForTest.getTestedFitness())
                bestNetworkForTest = bestNetworkForGen;

            //saves it to the file
            if (bestNetworkForGen.getTestedFitness() >= minSuperSnakeFitness && saveNetworks) {
                loader.saveNetwork(bestNetworkForGen);
                System.out.print("\t\t Network Saved to " + filePath);
            }
            System.out.println();
        } while (testAvg < maximumFitness && generations < maxGenerations);
        snake = new Snake(boardHeight,boardWidth,pointsPerCherry,pointsPerCorrectMove,pointsPerIncorrectMove);
        snake.paintPlayNetworkGame(bestNetworkForTest);
    }
}

