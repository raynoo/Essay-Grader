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
		
		Sentence s = new Sentence("You is defeating cooking me.");
		
		System.out.println("\n" + Criteria.isVerbNounAgreeing(s));
		System.out.println(Criteria.isVerbAgreeing(s));	
		
		Reader.readTestingFile("test_corpus/2.txt");
	}
	
}
