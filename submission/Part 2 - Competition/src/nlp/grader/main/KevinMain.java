package nlp.grader.main;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;



/**
 * My main class to test individual stuff
 * @author kevin
 *
 */
public class KevinMain 
{

	private static List<String> sentences= new ArrayList<String>();
	
	public static void main(String[] args) throws IOException 
	{
		
		BufferedReader br = new BufferedReader(new FileReader("rfc.txt"));
		StringBuilder paragraph = new StringBuilder();
		String temp;
		
		while( (temp = br.readLine() ) != null )
		{
			if(temp.length() == 0)
			{
				tokenizeIntoSentence(paragraph.toString());
				paragraph = new StringBuilder();
			}
			else
			{
				paragraph.append(temp +"\n");
				
			}
		}
		
//tokenizeIntoSentence("My baby Cherry the 3 years my husband his name is Ben the 27 years.");
		
	
	}
	
	private static void tokenizeIntoSentence(String paragraph)
	{
		
		Reader reader = new StringReader(paragraph);
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		
		Iterator<List<HasWord>> it = dp.iterator();
		
		while (it.hasNext()) {
		   StringBuilder sentenceSb = new StringBuilder();
		   List<HasWord> sentence = it.next();
		   for (HasWord token : sentence) {
		      if(sentenceSb.length()>1) {
		         sentenceSb.append(" ");
		      }
		      sentenceSb.append(token);
		   }
		   
		   //System.out.println(sentenceSb.toString());
		   sentences.add(sentenceSb.toString());
		}		
	}
	
	
	
	
}
