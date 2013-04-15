package nlp.grader.main;

import nlp.grader.objects.Sentence;
import nlp.grader.utils.Reader;

/**
 * Entry point class for the essay grader.
 * 
 * @author renus
 *
 */
public class Main {

	/**
	 * TODO: Read from file and calls Checker and Grader
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String input = "Dole will defeating me";
		Sentence s = new Sentence(input);
		
		System.out.println("\n1b Verb-Noun agreement: " + Criteria.isVerbNounAgreeing(s));
		System.out.println("1c Verb Gerund agreement: " + Criteria.isVerbAgreeing(s));
		
		Reader.readTestingFile("test_corpus/2.txt");
	}
	
}
