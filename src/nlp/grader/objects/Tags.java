package nlp.grader.objects;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import nlp.grader.utils.Reader;

/**
 * Reads tags from file and saves them in HashSet
 * @author renus
 *
 */
public class Tags {

		
	private static HashSet<String> verbTags = null;
	private static HashSet<String> nounTags = null;
	
	private static HashSet<String> femalePRPWords = null; // pronoun
	private static HashSet<String> malePRPWords = null;
	private static HashSet<String> pluralPRPWords = null;
	
	private static HashSet<String> femaleWords = null;
	private static HashSet<String> maleWords = null;
	
	static{
		String[] fePrp = {"she","her","hers"};
		String[] maPrp = {"he","his","him"};
		String[] plPrp = {"we","they","them","their","those","us"};
		
		femalePRPWords.addAll(Arrays.asList(fePrp));
		malePRPWords.addAll(Arrays.asList(maPrp));
		pluralPRPWords.addAll(Arrays.asList(plPrp));
		
		
		try 
		{
			String word;
			BufferedReader br = new BufferedReader(new FileReader("rules/Male.txt"));
			while( (word = br.readLine().trim()) != null)
			{
				maleWords.add(word);
			}
			br.close();
			br = new BufferedReader(new FileReader("rules/Female.txt"));
			while( (word = br.readLine().trim()) != null)
			{
				femaleWords.add(word);
			}
			
			br.close();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
		
	public static HashSet<String> getVerbTags() {
		if(verbTags == null) {
			verbTags = new HashSet<String>();
			verbTags.addAll(Reader.readFile("rules/verb_tags.txt"));
		}
		return verbTags;
	}
	
	public static HashSet<String> getNounTags() {
		if(nounTags == null) {
			nounTags = new HashSet<String>();
			nounTags.addAll(Reader.readFile("rules/noun_tags.txt"));
		}
		return nounTags;
	}
	
	public static boolean isFemalePrp(String word)
	{
		return femalePRPWords.contains(word);
	}
	
	public static boolean isMalePrp(String word)
	{
		return malePRPWords.contains(word);
	}
	
	public static boolean isPluralPrp(String word)
	{
		return pluralPRPWords.contains(word);
	}
	
	public static boolean isFemaleWord(String word)
	{
		return femaleWords.contains(word);
	}
	
	public static boolean isMaleWord(String word)
	{		
		return maleWords.contains(word);
	}
	public static boolean isNeutralGender(String word)
	{
		throw new NotImplementedException();
	}
	
	
	
}
