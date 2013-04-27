package nlp.grader.objects;

import java.util.Arrays;
import java.util.HashSet;

import nlp.grader.utils.Reader;

/**
 * Reads tags from file and saves them in HashSet
 * @author renus
 *
 */
public class Tags {

	static{
		String[] fePrp = {"she","her","hers"};
		String[] maPrp = {"he","his","him"};
		String[] plPrp = {"we","they","them","their","those","us"};
	}
	
	private static HashSet<String> verbTags = null;
	private static HashSet<String> nounTags = null;
	
	private static HashSet<String> femalePRPWords = null; // pronoun
	private static HashSet<String> malePRPWords = null;
	private static HashSet<String> pluralPRPWords = null;
	
	private static HashSet<String> femaleWords = null;
	private static HashSet<String> maleWords = null;
	
	
		
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
	
	
	
	
}
