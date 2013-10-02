import java.util.*;
import java.io.*;

class Encoder {
  public static void main(String[] args) { throws IOException
    Integer n = Integer.valueOf(args[1]);
    String[] lst = new String[n];
    // String f = new String(args[0]);
    try {
      BufferedReader br = new BufferedReader(new FileReader(args[0]));
    }

    // Interpret list of n non-negative integers
    String line = null;
    for(int i = 0; (line = br.readLine()) != null; i++) {
      lst[i] = line;
      System.out.println(lst[i]);
    }
  }
}
