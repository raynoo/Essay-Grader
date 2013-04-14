package nlp.grader.main;

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
		
		String input = "Dole are defeat me";
		
		System.out.println("\n1b Verb-Noun agreement: " + Criteria.isVerbAgreeing(input));
		
		
		Reader.readTestingFile("test_corpus/2.txt");
	}
	
	private void spellCheck() {
		
	}

}
