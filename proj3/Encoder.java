import java.util.*;
import java.io.*;

class Node implements Comparable {
  private int  value;
  private char content;
  private Node left;
  private Node right;

  public Node(char content, int value) {
    this.content  = content;
    this.value    = value;
  }

  public Node(Node left, Node right) {
    // Assumes that the left three is always the one that is lowest
    this.content  = (left.content < right.content) ? left.content : right.content;
    this.value    = left.value + right.value;
    this.left     = left;
    this.right    = right;
        }

  public int compareTo(Object arg) {
    Node other = (Node) arg;

    // Content value has priority and then the lowest letter
    if (this.value == other.value)
      return this.content-other.content;
    else
      return this.value-other.value;
    }

        ////////////////

  private void printNode(String path) {
    if ((left==null) && (right==null))
      System.out.println(content + " " + path);

    if (left != null)
      left.printNode(path + '1');
    if (right != null)
      right.printNode(path + '0');
    }

  public static void printTree(Node tree) {
    tree.printNode("");
  }
}

class Encoder {
  public static void main(String[] args) throws IOException {
    // Globals
    int n, sum;
    int[] nums;
    double entropy;
    double[] probabilities;
    // int[] frequency;
    String fileName;
    TreeSet<Node> trees = new TreeSet<Node>();  // List containing all trees -- ordered

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
    for(int i = 0; i < n; i++) {
      probabilities[i] = nums[i] / (sum*1.0);
      System.out.println("probs: " + probabilities[i]);
    }
    
    // Build up the initial trees
    for(int i = 0; i < n; i++) {
      if(nums[i] > 0) {
        Node nde = new Node((char)('A'+i), nums[i]);
        trees.add(nde);
      }
    }

    // Huffman algorithm
    while(trees.size() > 1) {
      Node tree1 = (Node) trees.first();
      trees.remove(tree1);
      Node tree2 = (Node) trees.first();
      trees.remove(tree2);

      Node merged = new Node(tree1, tree2);
      trees.add(merged);
    }

    // Print the resulting tree
    if(trees.size() > 0) {
      Node theTree = (Node) trees.first();
      Node.printTree(theTree);
    }
    else
      System.out.println("The file didn't contain useful characters.");
  }
}
