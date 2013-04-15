package nlp.grader.main;

import nlp.grader.objects.Essay;
import nlp.grader.utils.Reader;

/**
 * Entry point class for the essay grader.
 * 
 * @author renus
 *
 */
public class Main {

	/**
	 * Read from input file and passes essay to Grader to get final grade.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Essay essay = new Essay(Reader.readTestingFile("test_corpus/2.txt"));
		Grader essayGrader = new Grader();
		
		essayGrader.gradeEssay(essay);
		
//		Sentence s = new Sentence("You are defeating me.");
//		System.out.println("\n" + Criteria.isVerbNounAgreeing(s));
//		System.out.println(Criteria.isVerbAgreeing(s));	
	}
	
}
