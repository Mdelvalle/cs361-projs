import java.util.*;
import java.io.*;

class Encoder {
  public static void main(String[] args) throws IOException {
    // Globals
    int n, sum;
    int[] nums;
    double entropy;
    double[] probabilities;
    String fileName;
    char[] alphabet = new char[25];
    // Map<String, int> alph_probs;

    // Initialization
    fileName = args[0];
    BufferedReader br = new BufferedReader
                            (new FileReader(fileName));

    // Interpret/Store list of n non-negative integers
    n = Integer.parseInt(args[1]);
    nums = new int[n];
    sum = 0;
    String line = null;
    for(int i = 0; (line = br.readLine()) != null; i++) {
      nums[i] = Integer.parseInt(line);
      sum += nums[i];
    }

    // Compute probabilites
    probabilities = new double[n];
    for(int i = 0; i < probabilities.length; i++) {
      probabilities[i] = (double) nums[i] / sum;
      System.out.println("probs: " + probabilities[i]);
    }
    
  }
}
