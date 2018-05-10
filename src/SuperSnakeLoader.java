import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
** Name : SuperSnakeLoader
** Author : Kyle
** Date : 5/7/18
** Description : Responsible for reading and writing "super snakes" to and from a file to save them, as well as loading them to be used again.
*
* =========================================== FILE ITEM ORDER: ============================================
*           ITEMS ARE SEPARATED BY COMMAS AND NETWORKS ARE SEPARATED BY NEW LINES (\n)
* TESTED FITNESS (double)
* FITNESS (double)
* NUMBER OF LAYERS (int)
* LAYERS (values separated by spaces " ") (int[])
* MEAN (double)
* STANDARD DEVIATION (double)
* BIASES (Matrices separated by Commas ",", Rows separated by hashtags "#", values separated by spaces " ") (double)
* WEIGHTS (Matrices separated by Commas ",", Rows separated by hashtags "#", values separated by spaces " ") (double)
*
*/
 class SuperSnakeLoader {

    private String filePath;
    private FileWriter writer;
    private FileReader reader;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

     SuperSnakeLoader(String filePath) {
        this.filePath = filePath;
    }

     void saveNetwork(Network network){
        try {
            writer = new FileWriter(filePath, true);
            bufferedWriter = new BufferedWriter(writer);
        } catch (IOException e){
            throw new RuntimeException("File not found");
        }
        String saved = "";

        //tested fitness
        saved += network.getTestedFitness() + ",";

        //fitness
        saved += network.getFitness() + ",";

        //number of layers
        saved += network.getNumOfLayers() + ",";

        //layers
        for (int i : network.getLayers())
            saved += i + " ";
        saved += ",";

        //Mean
        saved += network.getMean() + ",";

        //Standard deviation
        saved += network.getStDev() + ",";

        //biases
        for (int i = 0, ii = network.getBiases().size(); i < ii; i++) {
            for (int j = 0, jj = network.getBiases().get(i).getRows(); j < jj; j++){
                for (int k = 0, kk = network.getBiases().get(i).getCols(); k < kk; k++){
                    saved += network.getBiases().get(i).get(j,k) + " ";
                }
                saved += "#";
            }
            saved += ",";
        }

        //weights
        for (int i = 0, ii = network.getWeights().size(); i < ii; i++) {
            for (int j = 0, jj = network.getWeights().get(i).getRows(); j < jj; j++){
                for (int k = 0, kk = network.getWeights().get(i).getCols(); k < kk; k++){
                    saved += network.getWeights().get(i).get(j,k) + " ";
                }
                saved += "#";
            }
            saved += ",";
        }

        //writes
        saved += "\n";
        try {
            bufferedWriter.write(saved);
            bufferedWriter.close();
        } catch (IOException e){
            throw new RuntimeException("File not Found");
        }
    }

     private Network parseNetwork(String networkString){
        String[] netValues = networkString.split(",");

        //fitness values
        double testedFitness = Double.parseDouble(netValues[0]);
        double fitness = Double.parseDouble(netValues[1]);

        //layers
        int numLayers = Integer.parseInt(netValues[2]);
        String[] strlyrs = netValues[3].split(" ");
        int[] layers = new int[strlyrs.length];
        for (int i = 0; i < strlyrs.length; i++){
            layers[i] = Integer.parseInt(strlyrs[i]);
        }

        //mean and stddev
        double mean = Double.parseDouble(netValues[4]);
        double stdDev = Double.parseDouble(netValues[5]);

        //biases and weights
        List<Matrix> biases = new ArrayList<Matrix>();
        List<Matrix> weights = new ArrayList<Matrix>();
        double[][] tempArray;
        String[] rowsOfDoubleStrings;
        String[] doubleArrayString;
        double[] tempRow;

        //first the biases
        for (int i = 1; i < layers.length; i++){
            //gets an array of the arrays of doubles as strings
            rowsOfDoubleStrings = netValues[5+i].split("#");
            tempArray = new double[rowsOfDoubleStrings.length][];
            //iterates through the row strings
            for (int j = 0; j < rowsOfDoubleStrings.length; j++){
                //gets an array of doubles
                doubleArrayString = rowsOfDoubleStrings[j].split(" ");
                tempRow = new double[doubleArrayString.length];

                for (int k = 0; k < doubleArrayString.length; k++){
                    tempRow[k] = Double.parseDouble(doubleArrayString[k]);
                }
                //adds the array to the tempArray
                tempArray[j] = tempRow;
            }
            //adds the tempArray as a new Matrix
            //order will be the same
            biases.add(new Matrix(tempArray));
        }

        //next the weights
        for (int i = 1; i < layers.length; i++){
            //gets an array of the arrays of doubles as strings
            rowsOfDoubleStrings = netValues[5 + layers.length - 1 + i].split("#");
            tempArray = new double[rowsOfDoubleStrings.length][];
            //iterates through the row strings
            for (int j = 0; j < rowsOfDoubleStrings.length; j++){
                //gets an array of doubles
                doubleArrayString = rowsOfDoubleStrings[j].split(" ");
                tempRow = new double[doubleArrayString.length];

                for (int k = 0; k < doubleArrayString.length; k++){
                    tempRow[k] = Double.parseDouble(doubleArrayString[k]);
                }
                //adds the array to the tempArray
                tempArray[j] = tempRow;
            }
            //adds the tempArray as a new Matrix
            //order will be the same
            weights.add(new Matrix(tempArray));
        }

        //sets the values to the network
        Network newNet = new Network(layers, mean,stdDev);
        newNet.setTestedFitness(testedFitness);
        newNet.setFitness(fitness);
        newNet.setBiases(biases);
        newNet.setWeights(weights);
        return newNet;
    }

     private String pullNetwork(int line) {
        try {
            reader = new FileReader(filePath);
        bufferedReader = new BufferedReader(reader);
        //skips lines until the correct one is reached
        for (int i = 0; i < line - 1; i++)
            bufferedReader.readLine();

        //returns the right line
        return bufferedReader.readLine();
        } catch (IOException e){
            throw new RuntimeException("File not Found");
        }
    }

     private Network loadNetwork(int line){
        String s = pullNetwork(line);
        return parseNetwork(s);
    }

     List<Network> loadBestNetworks(double minimumTestedFitness, int[] layers){
        List<Network> goodNetworks = new ArrayList<Network>();
        int lines;
        Network temp;
        //counts the number of lines in the file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (IOException e){
            throw new RuntimeException("File not Found");
        }
        for (int i = 1; i <= lines; i++){
            temp = loadNetwork(i);
            if (temp.getTestedFitness() >= minimumTestedFitness && Arrays.equals(layers,temp.getLayers()))
                goodNetworks.add(temp);
        }
        return goodNetworks;
    }

     List<Network> loadBestNetworks(double minimumTestedFitness){
        List<Network> goodNetworks = new ArrayList<Network>();
        int lines;
        Network temp;
        //counts the number of lines in the file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (IOException e){
            throw new RuntimeException("File not Found");
        }
        for (int i = 1; i <= lines; i++){
            temp = loadNetwork(i);
            if (temp.getTestedFitness() >= minimumTestedFitness)
                goodNetworks.add(temp);
        }
        return goodNetworks;
    }

     Network loadBestNetwork(){
        List<Network> allNetworks = loadBestNetworks(0);
        Network largest = allNetworks.get(0);
        for (Network n : allNetworks)
            if (n.getTestedFitness() > largest.getTestedFitness())
                largest = n;
        if (largest.getTestedFitness() == -1)
            throw new RuntimeException("No networks with matching layers found");
        return largest;
    }

     Network loadBestNetwork(int[] layers){
        Network largest = new Network(layers,0,0);
        largest.setTestedFitness(-1);
        List<Network> allNetworks = loadBestNetworks(0,layers);
        for (Network n : allNetworks)
            if (n.getTestedFitness() > largest.getTestedFitness() && Arrays.equals(layers,n.getLayers()))
                largest = n;
        if (largest.getTestedFitness() == -1)
            throw new RuntimeException("No networks with matching layers found");
        return largest;
    }

     void clearFile(){
        try {
            writer = new FileWriter(filePath);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("");
            bufferedWriter.close();
        } catch (IOException e){
            throw new RuntimeException("File not Found");
        }

    }
}
