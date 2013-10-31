import java.util.*;
import java.io.*;

class Node implements Comparable {
  private int  value;
  private char content;
  private Node left;
  private Node right;

  public static Map<Character, String> letter_encoding;
  public static Map<String, Character> letter_decode;

  public Node(char content, int value) {
    this.content  = content;
    this.value    = value;
  }
/*  public Node(String strContent, int strValue) {
    this.strContent = strContent;
    this.strValue = strValue;
  }
*/
  public Node(Node left, Node right) {
    // Assumes that the left three is always the one that is lowest
    this.content  = (left.content < right.content) ? left.content : right.content;
    this.value    = left.value + right.value;
    this.left     = left;
    this.right    = right;

    letter_encoding = new HashMap<Character, String>(26);
    letter_decode = new HashMap<String, Character>(26);
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
    if ((left==null) && (right==null)) {
      letter_encoding.put(content, path);
      letter_decode.put(path, content);
      System.out.println(content + " " + path);
    }

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
    int k, n, sum;
    int[] nums;
    double entropy;
    double[] probabilities;
    String fileName;
    TreeSet<Node> trees = new TreeSet<Node>();  // List containing all trees -- ordered
    char[] letter_array;

    // Initialization
    fileName = args[0];
    BufferedReader br = new BufferedReader
                            (new FileReader(fileName));

    // Interpret/Store list of n non-negative integers
    k = Integer.parseInt(args[1]);
    n = 0;
    nums = new int[26];
    sum = 0;
    String line = null;
    for(int i = 0; (line = br.readLine()) != null; i++) {
      n++;
      nums[i] = Integer.parseInt(line);
      //System.out.println("nums: " + nums[i]);
      sum += Integer.parseInt(line);
    }

    // Compute probabilites
    probabilities = new double[n];
    for(int i = 0; i < n; i++) {
      probabilities[i] = nums[i] / (sum*1.0);
      //System.out.println("probs: " + probabilities[i]);
    }
    
    letter_array = new char[sum];
    int j = 0;
    int idx = 0;
    for(int i = 0; i < n; i++) {
      int temp = j+nums[i];
      for( ; j < temp; j++) {
        char c = ((char)('A'+i));
        letter_array[j] = c;
      }
      j = temp;
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


    // Write volume of text with expected probabilities
     File file = new File("testText");
    if(!file.exists())
      file.createNewFile();
    
    FileWriter fw = new FileWriter(file.getAbsoluteFile());
    BufferedWriter bw = new BufferedWriter(fw);

    Random generator = new Random();
    int rand = generator.nextInt(sum);
    for(int i = 0; i < k; i++) {
      String str =  Node.letter_encoding.get(letter_array[rand]);
      bw.write(letter_array[rand]);
      System.out.println("@@@@@ " + str);

      rand = generator.nextInt(sum);
    }
    bw.close();
    fw.close();



    File file2 = new File("testText.enc1");
    if(!file2.exists())
      file2.createNewFile();
    
    FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
    BufferedWriter bw2 = new BufferedWriter(fw2);
    BufferedReader br2 = new BufferedReader(new FileReader("testText"));
    String ln = null;
    while((ln = br2.readLine()) != null) {
      bw2.write(ln);
    }
    bw2.close();
    fw2.close();
    



     BufferedReader br3 = new BufferedReader(new FileReader(file2));

    File file3 = new File("testText.dec1");
    if(!file3.exists())
      file3.createNewFile();
    
    FileWriter fw3 = new FileWriter(file2.getAbsoluteFile());
    BufferedWriter bw3 = new BufferedWriter(fw3);

    String l = null;
    while((l = br3.readLine()) != null) {
      bw3.write(Node.letter_decode.get(line));
    }
    bw3.close();
   

    BufferedReader br4 = new BufferedReader(new FileReader(file2));

    File file4 = new File("testText.enc2");
    if(!file4.exists())
      file4.createNewFile();
    
    FileWriter fw4 = new FileWriter(file4.getAbsoluteFile());
    BufferedWriter bw4 = new BufferedWriter(fw4);

    String l = null;
    while((l = br3.readLine()) != null) {
      bw3.write(Node.letter_decode.get(line));
    }
    bw3.close();
   
  }
}
