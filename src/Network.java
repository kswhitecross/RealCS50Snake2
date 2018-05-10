
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
** Name : Network
** Author : Kyle
** Date : 4/15/18
** Description : A simple SGD-less neural network used for a genetic algorithm.  Can mutate and crossover with other networks.
*/
 class Network implements Comparable{

    //actual network
    private List<Matrix> biases = new ArrayList<>(); //there are no biases for the input layer
    private List<Matrix> weights = new ArrayList<>(); //there are no weights for the input layer
    private List<Matrix> weightedSums = new ArrayList<>(); //there are no weighted sums for the input layer
    private List<Matrix> activations = new ArrayList<>();

    //bunch of parameters
    private double mean;
    private double stDev;
    private int[] layers;
    private int numOfLayers;
    private double fitness;
    private double testedFitness;

     Network(int[] layers, double mean, double stDev){
        //instance data
        this.mean = mean;
        this.stDev = stDev;
        this.layers = layers;
        this.fitness = 0;
        numOfLayers = layers.length;

        //activation values
        for (int i : layers){
            activations.add(new Matrix(i,1));
        }

        //biases, weighted sums and weights
        for (int i = 1; i < layers.length; i++){

            biases.add(new Matrix(layers[i],1,mean,stDev));

            weightedSums.add(new Matrix(layers[i],1));

            weights.add(new Matrix(layers[i],layers[i-1],mean,stDev));

        }
    }

    //feeds the matrix of inputs forward and returns the output layer
     Matrix feedforward(Matrix inputs){
        //safety stuff
        if (!inputs.isSameSize(activations.get(0)))
            throw new RuntimeException("Inputs not the same size as input layer");
        //sets inputs
        activations.set(0,inputs);
        //feeds forward
        for (int i = 1, n = numOfLayers; i < n; i++){
            weightedSums.set(i-1,Matrix.add(Matrix.multiply(weights.get(i-1),activations.get(i-1)),biases.get(i-1)));
            activations.set(i,Matrix.sigmoid(weightedSums.get(i-1)));
        }
        //returns output layer
        return activations.get(numOfLayers - 1);
    }

    //Crosses over with another network
     static Network crossover(Network a, Network b){
        //Checks dimensions
        if (a.getLayers() != b.getLayers())
            throw new RuntimeException("Matrices not the same size");

        //Creates child
        Network child = new Network(a.getLayers(), 0, 0);
        Random random = new Random();
        int randRow;
        int randCol;

        //iterates through the weights and biases
        for (int i = 0, n = a.getWeights().size(); i < n; i++){
            randRow = random.nextInt(a.getWeights().get(i).getRows());
            randCol = random.nextInt(a.getWeights().get(i).getCols());
            child.getWeights().set(i,Matrix.merge(a.getWeights().get(i),b.getWeights().get(i),randRow,randCol));
            child.getBiases().set(i,Matrix.merge(a.getBiases().get(i),b.getBiases().get(i),randRow,0));
        }

        return child;
    }

     static Network mutate(Network n, double mutationChance, double mean, double stDev){
        Random random = new Random();
        double temp;
        for (int i = 0; i < n.getWeights().size(); i++){
            for (int j = 0; j < n.getWeights().get(i).getRows(); j++){
                for (int k = 0; k < n.getWeights().get(i).getCols(); k++){
                    temp = random.nextDouble();
                    if (temp <= mutationChance)
                        n.getWeights().get(i).randomize(j,k,mean,stDev);
                }
            }
        }

        //biases
        for (int i = 0; i < n.getBiases().size(); i++){
            for (int j = 0; j < n.getBiases().get(i).getRows(); j++){
                for (int k = 0; k < n.getBiases().get(i).getCols(); k++){
                    temp = random.nextDouble();
                    if (temp <= mutationChance)
                        n.getBiases().get(i).randomize(j,k,mean,stDev);
                }
            }
        }
        return n;
    }

     void mutate(double mutationChance){
        Random random = new Random();
        double temp;
        for (int i = 0; i < this.getWeights().size(); i++){
            for (int j = 0; j < this.getWeights().get(i).getRows(); j++){
                for (int k = 0; k < this.getWeights().get(i).getCols(); k++){
                    temp = random.nextDouble();
                    if (temp <= mutationChance)
                        this.getWeights().get(i).randomize(j,k,mean,stDev);
                }
            }
        }

        //biases
        for (int i = 0; i < this.getBiases().size(); i++){
            for (int j = 0; j < this.getBiases().get(i).getRows(); j++){
                for (int k = 0; k < this.getBiases().get(i).getCols(); k++){
                    temp = random.nextDouble();
                    if (temp <= mutationChance)
                        this.getBiases().get(i).randomize(j,k,mean,stDev);
                }
            }
        }
    }

     @Override
     public int compareTo(Object o) {
        Network n = (Network) o;
        if (this.fitness > n.getFitness())
            return 1;
        else if (this.fitness < n.getFitness())
            return -1;
        else
            return 0;
    }

     List<Matrix> getBiases() {
        return biases;
    }

     void setBiases(List<Matrix> biases) {
        this.biases = biases;
    }

     List<Matrix> getWeights() {
        return weights;
    }

     void setWeights(List<Matrix> weights) {
        this.weights = weights;
    }

     List<Matrix> getWeightedSums() {
        return weightedSums;
    }

     void setWeightedSums(List<Matrix> weightedSums) {
        this.weightedSums = weightedSums;
    }

     int getNumOfLayers() {
        return numOfLayers;
    }

     int[] getLayers(){
        return layers;
    }

     double getFitness(){
        return fitness;
    }

     void setFitness(double d){
        fitness = d;
    }

     double getTestedFitness() {
        return testedFitness;
    }

     void setTestedFitness(double testedFitness) {
        this.testedFitness = testedFitness;
    }

     double getMean() {
        return mean;
    }

     public void setMean(double mean) {
        this.mean = mean;
    }

     double getStDev() {
        return stDev;
    }

     public void setStDev(double stDev) {
        this.stDev = stDev;
    }
}
