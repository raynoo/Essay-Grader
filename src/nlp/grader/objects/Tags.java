package nlp.grader.objects;

import java.util.HashSet;

import nlp.grader.utils.Reader;

/**
 * Reads tags from file and saves them in HashSet
 * @author renus
 *
 */
public class Tags {

	private static HashSet<String> verbTags = null;
	private static HashSet<String> nounTags = null;
	private static HashSet<String> femalePRPWords = null;
	private static HashSet<String> malePRPWords = null;
	private static HashSet<String> pluralPRPWords = null;
	
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
	
}
