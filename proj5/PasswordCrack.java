import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


// 1. Extract the encrypted passsword and salt for that user.
// 2. Seed the word list with words that the user might have utilized in constructing.
// 3. With the salt and augmented wordlist, systematically encrypt words and compare
//    against the stored encrypted password.
// 4. Redo step 3, but using mangled versions of the words.
// 5. Redo step 4, attempting to apply two mangles to each word.
class PasswordCrack {

	private static String salted_pass(String salt, String pass) {
		return jcrypt.crypt(salt, pass);
	}

	public boolean compare_pass(String a, String salt, String pass) {
		return a.equals(salted_pass(salt, pass));
	}

    // Should add mangled words to the dictionary
	public static String mangle(ArrayList<String> list, int index) {
		String line = list.get(index);
		String name = line.substring(0, line.indexOf(":"));
		
		return name;
	}

	public static String prepend(String pass, String encPass, String salt)
	{
		String trial = pass.toLowerCase();
		boolean correct = false;// encPass.equals(salted_pass(salt, trial));
/*		if(correct)
			return trial;*/
		for(int i = 33; i < 127; ++i)
		{
			trial = ((char) i) + pass;
			correct = encPass.equals(salted_pass(salt, trial));
			if(correct)
			{
				return trial;
			}
			trial = pass + ((char) i);
			correct = encPass.equals(salted_pass(salt, trial));
			if(correct)
			{
				return trial;
			}
			trial = (new StringBuffer(pass).reverse().toString());
			trial = ((char) i) + trial;
			correct = encPass.equals(salted_pass(salt, trial));
			if(correct)
			{
				return trial;
			}
			trial = (new StringBuffer(pass).reverse().toString());
			trial = trial + ((char) i);
			correct = encPass.equals(salted_pass(salt, trial));
			if(correct)
			{
				return trial;
			}
			trial = pass.toLowerCase();
			trial = trial.substring(1, trial.length());
			trial = trial + ((char) i);
			correct = encPass.equals(salted_pass(salt, trial));
			if(correct)
			{
				return trial;
			}
			trial = pass.toLowerCase();
			trial = trial.substring(0, trial.length() - 1);
			trial = ((char) i) + trial;
			correct = encPass.equals(salted_pass(salt, trial));
			if(correct)
			{
				return trial;
			}
			trial = pass.toLowerCase();
			trial = trial.substring(1, trial.length());
			trial = ((char) i) + trial;
			correct = encPass.equals(salted_pass(salt, trial));
			if(correct)
			{
				return trial;
			}
			trial = pass.toLowerCase();
			trial = trial.substring(0, trial.length() - 1);
			trial = trial + ((char) i);
			correct = encPass.equals(salted_pass(salt, trial));
			if(correct)
			{
				return trial;
			}
			trial = pass.toUpperCase();
			trial = Character.toLowerCase(trial.charAt(0)) + trial.substring(1);
			trial = trial + ((char) i);
			correct = encPass.equals(salted_pass(salt, trial));
			if(correct)
			{
				return trial;
			}
			trial = pass.toLowerCase();
			trial = Character.toUpperCase(trial.charAt(0)) + trial.substring(1);
			trial = trial + ((char) i);
			correct = encPass.equals(salted_pass(salt, trial));
			if(correct)
			{
				return trial;
			}

		}
		return "-1";
	}
	
	public static String delete(String pass, String encPass, String salt)
	{
		String trial = pass.substring(1, pass.length());
		boolean correct = false;
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		trial = pass.substring(0, pass.length() - 1);
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		return "-1";
	}
	
	public static String rev(String pass, String encPass, String salt)
	{
		String trial = pass;
		boolean correct = false;
		trial = (new StringBuffer(trial).reverse().toString());
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		String trial2 = trial;
		trial = pass + trial;
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		trial = trial2 + pass;
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		trial = pass + pass;
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		return "-1";
	}
	
	public static String upLow(String pass, String encPass, String salt)
	{
		String trial = pass.toUpperCase();
		boolean correct = false;		
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		trial = pass.toLowerCase();
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		return "-1";
	}
	
	public static String caps(String pass, String encPass, String salt)
	{
		String trial = Character.toUpperCase(pass.charAt(0)) + pass.substring(1);
		boolean correct = false;	
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		
		trial = Character.toUpperCase(trial.charAt(0)) + trial.substring(1);
		trial = (new StringBuffer(trial).reverse().toString());
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}

		trial = pass.toLowerCase();
		trial = Character.toUpperCase(trial.charAt(0)) + trial.substring(1, trial.length() - 1);
		trial = (new StringBuffer(trial).reverse().toString());
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}

		trial = pass.toUpperCase();
		trial = Character.toLowerCase(pass.charAt(0)) + trial.substring(1);
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}

		trial = (new StringBuffer(pass).reverse().toString());
		trial = Character.toLowerCase(trial.charAt(0)) + trial.substring(1);
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		return "-1";
	}
	
	public static String toggle(String pass, String encPass, String salt)
	{
		String trial = "";
		String trial2 = "";
		boolean correct = false;
		
		for(int i = 0; i < pass.length() ; ++i)
		{
			if(i%2 == 0)
			{
				trial += Character.toUpperCase(pass.charAt(i));
				trial2 += Character.toLowerCase(pass.charAt(i));
			}
			else 
			{
				trial += Character.toLowerCase(pass.charAt(i));
				trial2 += Character.toUpperCase(pass.charAt(i));
			} 
		}
		correct = encPass.equals(salted_pass(salt, trial));
		if(correct)
		{
			return trial;
		}
		correct = encPass.equals(salted_pass(salt, trial2));
		if(correct)
		{
			return trial;
		}
		return "-1";
		
	}
	
	public static String brute_force(String encPass, String salt)
	{
		String trial = "";
		boolean correct = false;
		int x = 0;
		// HashMap<String, boolean> memoized = new HashMap<String, boolean>();

		for(int i = 33; i <= 126; ++i)
		{
			for(int j = 33; j <= 126; ++j)
			{
				for(int k = 33; k <= 126; ++k)
				{
					for(int l = 33; l <= 126; ++l)
					{
						for(int m = 33; m <= 126; ++m)
						{
							for(int n = 33; n <= 126; ++n)
							{
								trial = ""+((char) i)+((char) j)+((char) k)+((char) l)+((char) m)+((char) n);
								// System.out.println(trial);
								correct = encPass.equals(salted_pass(salt, trial));
								if(correct)
									return trial;
							}
						}

					}
				}
			}
		}
			return "-1";
	}

    public static void main(String[] args) throws IOException {
    	ArrayList<String> dictionary;
    	ArrayList<String> fi_passwords;
      ArrayList<String> enc_passwords;
      ArrayList<String> saltList;
      ArrayList<String> names;
      Map<String, ArrayList<String>> wordList;
    	String dictionary_file;
    	String password_file;
    	BufferedReader bR;

    	dictionary_file = args[0];
    	password_file = args[1];

    	// Add list of passwords to enc_passwords ArrayList
    	bR = new BufferedReader(new FileReader(password_file));
    	fi_passwords = new ArrayList<String>();
    	String line = null;
    	for(int i = 0; (line = bR.readLine()) != null; ++i) {
            fi_passwords.add(line);
    	}
    	bR.close();

    	// Add words to dictionary ArrayList
    	bR = new BufferedReader(new FileReader(dictionary_file));
    	dictionary = new ArrayList<String>();
    	line = null;
    	for(int i = 0; (line = bR.readLine()) != null; ++i) {
    		dictionary.add(line);
    	}
    	bR.close();

        // get the salt and encrypted password 13 char string
        // get the names of the person and add to dictionary
        enc_passwords = new ArrayList<String>();
        saltList = new ArrayList<String>();
        names = new ArrayList<String>();
        wordList = new HashMap<String, ArrayList<String>>();
        String pass = null;
        String dict = null;
        String salt = null;
        String[] lineSplit;
        for(int i = 0; i < fi_passwords.size(); ++i)
        {
            ArrayList<String> words = new ArrayList<String>();
            lineSplit = fi_passwords.get(i).split(":");
            // get salt and encrypted password
            pass = lineSplit[1];
            salt = pass.substring(0, 2);
            //pass = pass.substring(2);

            // add to arrays
            saltList.add(salt);
            enc_passwords.add(pass);

            // get users name to add to dictionary
            dict = lineSplit[4];
            String fname = dict.substring(0, dict.indexOf(" "));
            String lname = dict.substring((dict.indexOf(" ") + 1));
            names.add(dict);
            dictionary.add(fname.toLowerCase());
            dictionary.add(lname.toLowerCase());
            wordList.put(pass, words);

           //System.out.println("Salt: " + salt);
           //System.out.println("Encrypted pass: " + pass);
           //System.out.println("First Last name: " + fname +" " + lname);
        }
        
    	//PasswordCrack psc = new PasswordCrack();

		int indx = 0;
		String guess = null;
		ArrayList<String> brute = new ArrayList<String>();
		ArrayList<String> brute_salt = new ArrayList<String>();
		while(enc_passwords.size() > 0)
		{
			for(int i = 0; i < dictionary.size(); ++i)
			{
				guess = prepend(dictionary.get(i), enc_passwords.get(indx), saltList.get(indx));
				if(!guess.equals("-1"))
				{
					System.out.printf("%s's password is: %s\n", names.get(indx), guess);
					enc_passwords.remove(indx);
					saltList.remove(indx);
					names.remove(indx);
					indx--;
					break;
				}
				else
				{
					guess = delete(dictionary.get(i), enc_passwords.get(indx), saltList.get(indx));
				}
				
				if(!guess.equals("-1"))
				{
					System.out.printf("%s's password is: %s\n", names.get(indx), guess);
					enc_passwords.remove(indx);
					saltList.remove(indx);
					names.remove(indx);
					indx--;
					break;
				}
				else
				{
					guess = rev(dictionary.get(i), enc_passwords.get(indx), saltList.get(indx));
				}
				
				if(!guess.equals("-1"))
				{
					System.out.printf("%s's password is: %s\n", names.get(indx), guess);
					enc_passwords.remove(indx);
					saltList.remove(indx);
					names.remove(indx);
					indx--;
					break;
				}
				else
				{
					guess = upLow(dictionary.get(i), enc_passwords.get(indx), saltList.get(indx));
				}
				
				if(!guess.equals("-1"))
				{
					System.out.printf("%s's password is: %s\n", names.get(indx), guess);
					enc_passwords.remove(indx);
					saltList.remove(indx);
					names.remove(indx);
					indx--;
					break;
				}
				else
				{
					guess = caps(dictionary.get(i), enc_passwords.get(indx), saltList.get(indx));
				}
				
				if(!guess.equals("-1"))
				{
					System.out.printf("%s's password is: %s\n", names.get(indx), guess);
					enc_passwords.remove(indx);
					saltList.remove(indx);
					names.remove(indx);
					indx--;
					break;
				}
				else
				{
					guess = toggle(dictionary.get(i), enc_passwords.get(indx), saltList.get(indx));
				}
				
				if(!guess.equals("-1"))
				{
					System.out.printf("%s's password is: %s\n", names.get(indx), guess);
					enc_passwords.remove(indx);
					saltList.remove(indx);
					names.remove(indx);
					indx--;
					break;
				}
				else{
					guess = null;
					// add elements that we did not find passwords for to
					// another arraylist, then brute force these
					brute.add(enc_passwords.get(indx));
					brute_salt.add(saltList.get(indx));
					if(enc_passwords.size() == 1)
					{
						enc_passwords.remove(indx);
						saltList.remove(indx);
						break;
					}
				}
			}
			indx++;
			if(indx >= enc_passwords.size())
			{
				indx = 0;
			}

        }
        guess = null;
        indx = 0;
        // try to brute force the ones we did not find passwords for
        while(brute.size() > 0)
		{
			guess = brute_force(brute.get(indx), brute_salt.get(indx));
			if(!guess.equals("-1"))
			{
				System.out.printf("%s's password is: %s\n", names.get(indx), guess);
				brute.remove(indx);
				brute_salt.remove(indx);
				names.remove(indx);
				indx--;
				break;
			}
			indx++;
			if(indx >= brute.size())
			{
				indx = 0;
			}
		}
    }
}
