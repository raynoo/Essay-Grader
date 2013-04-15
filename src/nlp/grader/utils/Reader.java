package nlp.grader.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class Reader {

	public static List<String> readFile(String filename) {
		BufferedReader br = null;
		String line;
		List<String> lines = new ArrayList<String>();

		try {
			br = new BufferedReader(new FileReader(filename));

			while ((line = br.readLine()) != null &&
					line != "" && line.charAt(0) != '#') {
				lines.add(line);
			}

			br.close();

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return lines;
	}


	static Pattern pat = Pattern.compile("[A-Z]$"); // capital letter at the string after split by.
	static Pattern pat1 = Pattern.compile("^[A-Z]$"); //only 1 capital letter
	static Pattern pat2 = Pattern.compile("^[\\s][a-z]"); // sentenc starts with small letter
	static Pattern pat3 = Pattern.compile("^\\s[A-Z]"); // sentence has space in begening

	public static List<String> readTestingFile(String filename)
	{
		BufferedReader br = null;
		String line;
		List<String> lines = new ArrayList<String>();

		try 
		{			
			br = new BufferedReader(new FileReader(filename));

			while ( ( line = br.readLine() ) != null ) 
			{
				String temp[] = line.split("\\.");

				for( int i = 0 ; i < temp.length ; i++ )
				{
					if(pat.matcher(temp[i]).find())
					{
						String store = temp[i];
						int k = i+1;
						while(k<temp.length)
						{
							if( pat1.matcher(temp[k]).find() || pat2.matcher(temp[k]).find())
							{
								store = store + "." + temp[k];
								i=k;
								k++;
							}
							else
								break;
						}
						
						lines.add(store+".");
					}
					else
					{
						String store;
						if(pat3.matcher(temp[i]).find())
							store = temp[i].substring(1);
						else
							store = temp[i];
						
						if(line.contains(store+"."))
							lines.add(store+".");
						else
							lines.add(store);
						
						
					}
				}
			}


			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

//		for(String st : lines)
//			System.out.println(st);
		return lines;
	}
}
