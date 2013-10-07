
/*
 * Huffman Algorithm for the DEI's Programming Contest, 2004
 * (c) Paulo Marques, 2004.
 * pmarques@dei.uc.pt
 *
 * Note: this program only process text characters:
 *       ('a'-'z' / 'A'-'Z'). Everything else is ignored.
 */

import java.io.*;
import java.util.*;

class Node
        implements Comparable
{
        private int     value;
        private char    content;
        private Node    left;
        private Node    right;

        public Node(char content, int value)
        {
                this.content  = content;
                this.value    = value;
        }

        public Node(Node left, Node right)
        {
                // Assumes that the left three is always the one that is lowest
                this.content  = (left.content < right.content) ? left.content : right.content;
                this.value    = left.value + right.value;
                this.left         = left;
                this.right    = right;
        }

        public int compareTo(Object arg)
        {
                Node other = (Node) arg;

                // Content value has priority and then the lowest letter
                if (this.value == other.value)
                        return this.content-other.content;
                else
                        return this.value-other.value;
        }

        ////////////////

        private void printNode(String path)
        {
                if ((left==null) && (right==null))
                        System.out.println(content + " " + path);

                if (left != null)
                        left.printNode(path + '0');
                if (right != null)
                        right.printNode(path + '1');
        }

        public static void printTree(Node tree)
        {
                tree.printNode("");
        }
}

public class Huffman
{
        public static void main(String[] args)
                throws IOException
        {
                StringBuffer fileContents = new StringBuffer();

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                String line = null;
                while ((line = br.readLine()) != null)
                        fileContents.append("\n").append(line);

                processFile(fileContents.toString());
        }

        private static void processFile(String fileContents)
        {
                int[] frequency = new int['Z'-'A'+1];           // Frequency table of each letter
                TreeSet<Node> trees     = new TreeSet<Node>();  // List containing all trees -- ORDERED!

                // Build the frequency table of each letter
                for (int i=0; i<fileContents.length(); i++)
                {
                        char ch = Character.toUpperCase(fileContents.charAt(i));
                        if ((ch >= 'A') && (ch <= 'Z'))
                                ++frequency[ch - 'A'];
                }

                // Build up the initial trees
                for (int i=0; i<'Z'-'A'+1; i++)
                {
                        if (frequency[i] > 0)
                        {
                                Node n = new Node((char)('A'+i), frequency[i]);
                                trees.add(n);
                        }
                }

                // Huffman algoritm
                while (trees.size() > 1)
                {
                        Node tree1 = (Node) trees.first();
                        trees.remove(tree1);
                        Node tree2 = (Node) trees.first();
                        trees.remove(tree2);

                        Node merged = new Node(tree1, tree2);
                        trees.add(merged);
                }

                // Print the resulting tree
                if (trees.size() > 0)
                {
                        Node theTree = (Node) trees.first();
                        Node.printTree(theTree);
                }
                else
                        System.out.println("The file didn't contain useful characters.");
        }
}