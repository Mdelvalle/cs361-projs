/*
Juan P. Mata
jpm2873

Miguel DelValle
mad2962
delvalle

*/
/*import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;*/
import java.io.*;
import java.util.*;



class ACS 
{
  public static Map<String, String> mapUsers;
  public static Map<String, String> mapFiles;
  public static boolean option;
  
  public static void main(String[] args) throws IOException
  {
    String userListName;
    String fileListName;
    option = false;
    mapUsers = new TreeMap<String, String>();
    mapFiles = new TreeMap<String, String>();
    Scanner sc = new Scanner(System.in);
    boolean exit = false;
    
    
    if(args.length == 3 && args[0].equals("-r"))
    {
      option = true;
      userListName = args[1];
      fileListName = args[2];
    }
    else
    {
      userListName = args[0];
      fileListName = args[1];
    }
    
    BufferedReader bR;
    
    bR = new BufferedReader(new FileReader(userListName));
    String line = null;
    for(int i= 0; (line = bR.readLine()) != null; ++i)
    {
      String[] parse = line.split(" ");
      mapUsers.put(parse[0], parse[1]);
    }
    bR.close();
    
    if(!option)
    {
      mapUsers.put("root", "root");
    }
    
    bR = new BufferedReader(new FileReader(fileListName));
    line = null;
    for(int i= 0; (line = bR.readLine()) != null; ++i)
    {
      String[] parse = line.split(" ");
      mapFiles.put(parse[0], parse[1] +" "+ parse[2]);
    }
    bR.close();
    

    while(!exit)
    {
        System.out.println("Input:");
        String in = sc.nextLine();
/*        sc.close();*/
        String[] lineInput = in.split(" ");

        if(lineInput[0].equalsIgnoreCase("READ"))
        {
          String[] fileValue = (mapFiles.get(lineInput[2])).split(" ");
          System.out.println("Output:");
          permRead(lineInput[1], fileValue[1], lineInput[2]);
        }
        
        else if(lineInput[0].equalsIgnoreCase("WRITE"))
        {
          String[] fileValue = (mapFiles.get(lineInput[2])).split(" ");
          System.out.println("Output:");
          permWrite(lineInput[1], fileValue[1], lineInput[2]);
        }
        
        else if(lineInput[0].equalsIgnoreCase("EXECUTE"))
        {
          String[] fileValue = (mapFiles.get(lineInput[2])).split(" ");
          System.out.println("Output:");
          permExecute(lineInput[1], fileValue[1], lineInput[2]);
        }

        else if(lineInput[0].equalsIgnoreCase("CHMOD"))
        {
          String[] fileValue = (mapFiles.get(lineInput[2])).split(" ");
          System.out.println("Output:");
          permChmod(lineInput[1], fileValue[1], lineInput[2], lineInput[3]);
        }

        else if(lineInput[0].equalsIgnoreCase("EXIT"))
        {
          File file = new File("state.log");
          if(!file.exists())
              file.createNewFile();
          FileWriter writer = new FileWriter(file);

          for(String treeKey : mapFiles.keySet())
          {
              String result = printState(treeKey);
              writer.write(result + "\n");

          }
          writer.close();
          exit = true;
          sc.close();
        }
    }

  }
  

  public static void permRead(String user, String mode, String file)
  {
    //System.out.println(mode);
    String lastVal = mode.substring(3);
    String[] fileUser = mapFiles.get(file).split(" ");
    String ownerGroup = mapUsers.get(fileUser[0]);
    String runninGroup = mapUsers.get(user);
    String fileOwner = fileUser[0];

    if(option && user.equals("root")) {
      System.out.println("read root root 0");
      return;
    }

    if(!option && user.equals("root")) {
        System.out.println("read root root 1");
        return;
    }

    // bit set for everyone
    if(lastVal.equals("4") || lastVal.equals("5") || 
       lastVal.equals("6") || lastVal.equals("7"))
    {
      System.out.println("read "+ user + " " + mapUsers.get(user) + " 1");
      return;
    }

    lastVal = mode.substring(2, 3);
    // bit set for group
    if((ownerGroup.equals(runninGroup)) && (lastVal.equals("4") || lastVal.equals("5") || 
                                            lastVal.equals("6") || lastVal.equals("7")))
    {
      System.out.println("read "+ user + " " + mapUsers.get(user) + " 1");
      return;
    }

    // bit set for user
    lastVal = mode.substring(1, 2);
    if((user.equals(fileOwner)) && (lastVal.equals("4") || lastVal.equals("5") || 
                                    lastVal.equals("6") || lastVal.equals("7")))
    {
      System.out.println("read "+ user + " " + mapUsers.get(user) + " 1");
      return;
    }

    // bit not set at all
    else
      System.out.println("read "+ user + " " + mapUsers.get(user) + " 0");
  }

  public static void permWrite(String user, String mode, String file)
  {
    //System.out.println(mode);
    String lastVal = mode.substring(3);
    String[] fileUser = mapFiles.get(file).split(" ");
    String ownerGroup = mapUsers.get(fileUser[0]);
    String runninGroup = mapUsers.get(user);
    String fileOwner = fileUser[0];

    if(option && user.equals("root")) {
      System.out.println("write root root 0");
      return;
    }

    if(!option && user.equals("root")) {
        System.out.println("write root root 1");
        return;
    }

    // bit set for everyone
    if(lastVal.equals("2") || lastVal.equals("3") || 
       lastVal.equals("6") || lastVal.equals("7"))
    {
      System.out.println("write "+ user + " " + mapUsers.get(user) + " 1");
      return;
    }

    lastVal = mode.substring(2, 3);
    // bit set for group
    if((ownerGroup.equals(runninGroup)) && (lastVal.equals("2") || lastVal.equals("3") || 
                                            lastVal.equals("6") || lastVal.equals("7")))
    {
      System.out.println("write "+ user + " " + mapUsers.get(user) + " 1");
      return;
    }

    // bit set for user
    lastVal = mode.substring(1, 2);
    if((user.equals(fileOwner)) && (lastVal.equals("2") || lastVal.equals("3") || 
                                    lastVal.equals("6") || lastVal.equals("7")))
    {
      System.out.println("write "+ user + " " + mapUsers.get(user) + " 1");
      return;
    }

    // bit not set at all
    else
      System.out.println("write "+ user + " " + mapUsers.get(user) + " 0");
  }
  
  public static void permExecute(String user, String mode, String file)
  {
    //System.out.println(mode);
    String firstVal = mode.substring(0,1);
    String lastVal = mode.substring(3);
    String[] fileUser = mapFiles.get(file).split(" ");
    String ownerGroup = mapUsers.get(fileUser[0]);
    String runningGroup = mapUsers.get(user);
    String fileOwner = fileUser[0];

  
      if(option && user.equals("root")) {
          System.out.println("execute root root 0");
          return;
      }

      if(!option && user.equals("root")) {
          System.out.println("execute root root 1");
          return;
      }
  
     // if user ID is set in bit 0 then you change the running user to owner
     // if group ID is set in bit 1 then you change the running user to owner group
     // if sticky is set in bit 2 then the object is a directory
     // Executing a directory doesn't really make a lot of sense 
     // so think of this as a traverse permission.
     // A user must have execute access to the bin directory in order to execute 
     // ls or cd command.
      
    // if firstVal = 2 change running group to owner group
    // if firstVal = 4 change running user to owner user
    // if firstVal = 6 change running group and user to owner group and user

    // bit set for everyone
    if(lastVal.equals("1") || lastVal.equals("3") || 
       lastVal.equals("5") || lastVal.equals("7"))
    {   
        if(firstVal.equals("0") || firstVal.equals("1"))
        {
          System.out.println("execute "+ user + " " + runningGroup + " 1");
          return;
        }
        if(firstVal.equals("2") || firstVal.equals("3"))
        {
          System.out.println("execute "+ user + " " + ownerGroup + " 1");
          return;
        }
        if(firstVal.equals("4") || firstVal.equals("5"))
        {

          System.out.println("execute "+  fileOwner + " " + runningGroup + " 1");
          return;
        }
        if(firstVal.equals("6") || firstVal.equals("7"))
        {
          System.out.println("execute "+  fileOwner + " " + ownerGroup + " 1");
          return;
        }
    }

    lastVal = mode.substring(2, 3);
    // bit set for group
    if((ownerGroup.equals(runningGroup)) && (lastVal.equals("1") || lastVal.equals("3") || 
                                          lastVal.equals("5") || lastVal.equals("7")))
    {
        if(firstVal.equals("0") || firstVal.equals("1"))
        {
          System.out.println("execute "+ user + " " + runningGroup + " 1");
          return;
        }
        if(firstVal.equals("2") || firstVal.equals("3"))
        {
          System.out.println("execute "+ user + " " + ownerGroup + " 1");
          return;
        }
        if(firstVal.equals("4") || firstVal.equals("5"))
        {

          System.out.println("execute "+  fileOwner + " " + runningGroup + " 1");
          return;
        }
        if(firstVal.equals("6") || firstVal.equals("7"))
        {
          System.out.println("execute "+  fileOwner + " " + ownerGroup + " 1");
          return;
        }
    }

     // bit set for user
    lastVal = mode.substring(1, 2);
    if((user.equals(fileOwner)) && (lastVal.equals("1") || lastVal.equals("3") || 
                                    lastVal.equals("5") || lastVal.equals("7")))
    {
        if(firstVal.equals("0") || firstVal.equals("1"))
        {
          System.out.println("execute "+ user + " " + runningGroup + " 1");
          return;
        }
        if(firstVal.equals("2") || firstVal.equals("3"))
        {
          System.out.println("execute "+ user + " " + ownerGroup + " 1");
          return;
        }
        if(firstVal.equals("4") || firstVal.equals("5"))
        {

          System.out.println("execute "+  fileOwner + " " + runningGroup + " 1");
          return;
        }
        if(firstVal.equals("6") || firstVal.equals("7"))
        {
          System.out.println("execute "+  fileOwner + " " + ownerGroup + " 1");
          return;
        }
    }

    // bit not set at all
    else
    {
      System.out.println("execute "+ user + " " + mapUsers.get(user) + " 0");
    }
  }

  public static void permChmod(String user, String mode, String file, String change)
  {
    String firstVal = mode.substring(0,1);
    String lastVal = mode.substring(3);
    String[] fileUser = mapFiles.get(file).split(" ");
    String ownerGroup = mapUsers.get(fileUser[0]);
    String runningGroup = mapUsers.get(user);
    String fileOwner = fileUser[0];

      if(option && user.equals("root")) {
          System.out.println("chmod root root 0");
          return;
      }

      if(!option && user.equals("root")) {
        // Change the mode of specified file in mapFiles
          mapFiles.put(file, fileUser +" "+ change);
          System.out.println("chmod root root 1");
          return;
      }

      if(user.equals(fileOwner))
      {
          mapFiles.put(file, fileUser +" "+ change);
          System.out.println("chmod " + user +" "+ runningGroup +" 1");
          return;
      }
      else
          System.out.println("chmod " + user +" "+ runningGroup +" 0");
  }

  public static String printState(String treeKey)
  {
          String[] val = mapFiles.get(treeKey).split(" ");
          String owner = val[0];
          String ownerGroup = mapUsers.get(owner);
          String mode = val[1];
          String zero = mode.substring(0, 1);
          String one = mode.substring(1, 2);
          String two = mode.substring(2, 3);
          String three = mode.substring(3, 4);

          String result = "";

          // 1xx xx0 ==> put S in 0
          if(zero.equals("4") || zero.equals("5") || zero.equals("6") || zero.equals("7"))
          {
              if(one.equals("0"))
                  result += "--S";
              else if(one.equals("1"))
                  result += "--s";
              else if(one.equals("2"))
                  result += "-wS";
              else if(one.equals("3"))
                  result += "-ws";
              else if(one.equals("4"))
                  result += "r-S";
              else if(one.equals("5"))
                  result += "r-s";
              else if(one.equals("6"))
                  result += "rwS";
              else if(one.equals("7"))
                  result += "rws";
          }

          else if(zero.equals("0") || zero.equals("1") || zero.equals("2") || zero.equals("3"))
          {
              if(one.equals("0"))
                  result += "---";
              else if(one.equals("1"))
                  result += "--x";
              else if(one.equals("2"))
                  result += "-w-";
              else if(one.equals("3"))
                  result += "-wx";
              else if(one.equals("4"))
                  result += "r--";
              else if(one.equals("5"))
                  result += "r-x";
              else if(one.equals("6"))
                  result += "rw-";
              else if(one.equals("7"))
                  result += "rwx";
          }

          if(zero.equals("2") || zero.equals("3") || zero.equals("6") || zero.equals("7"))
          {
              if(two.equals("0"))
                  result += "--S";
              else if(two.equals("1"))
                  result += "--s";
              else if(two.equals("2"))
                  result += "-wS";
              else if(two.equals("3"))
                  result += "-ws";
              else if(two.equals("4"))
                  result += "r-S";
              else if(two.equals("5"))
                  result += "r-s";
              else if(two.equals("6"))
                  result += "rwS";
              else if(two.equals("7"))
                  result += "rws";
          }

          else if(zero.equals("0") || zero.equals("1") || zero.equals("4") || zero.equals("5"))
          {
              if(two.equals("0"))
                  result += "---";
              else if(two.equals("1"))
                  result += "--x";
              else if(two.equals("2"))
                  result += "-w-";
              else if(two.equals("3"))
                  result += "-wx";
              else if(two.equals("4"))
                  result += "r--";
              else if(two.equals("5"))
                  result += "r-x";
              else if(two.equals("6"))
                  result += "rw-";
              else if(two.equals("7"))
                  result += "rwx"; 
          }

          if(zero.equals("1") || zero.equals("3") || zero.equals("5") || zero.equals("7"))
          {
              if(three.equals("0"))
                  result += "--T";
              else if(three.equals("1"))
                  result += "--t";
              else if(three.equals("2"))
                  result += "-wT";
              else if(three.equals("3"))
                  result += "-wt";
              else if(three.equals("4"))
                  result += "r-T";
              else if(three.equals("5"))
                  result += "r-t";
              else if(three.equals("6"))
                  result += "rwT";
              else if(three.equals("7"))
                  result += "rwt";
          }

          else if(zero.equals("0") || zero.equals("2") || zero.equals("4") || zero.equals("6"))
          {
              if(three.equals("0"))
                  result += "---";
              else if(three.equals("1"))
                  result += "--x";
              else if(three.equals("2"))
                  result += "-w-";
              else if(three.equals("3"))
                  result += "-wx";
              else if(three.equals("4"))
                  result += "r--";
              else if(three.equals("5"))
                  result += "r-x";
              else if(three.equals("6"))
                  result += "rw-";
              else if(three.equals("7"))
                  result += "rwx"; 
          }
          return result +" "+ owner +" "+ ownerGroup +" "+ treeKey;
  }

}