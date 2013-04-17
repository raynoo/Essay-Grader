package nlp.grader.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nlp.grader.objects.Essay;
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
	 * Processes the essay and calculates grade.
	 * @param args
	 * (@return final grade)
	 */
	public static void main(String[] args) {

		File corpusPath = new File("test_corpus");
		Main grader = new Main();

		for(File file : corpusPath.listFiles()) {
			if(file.isFile()) {
				System.out.println(file.getName());
				Essay essay = new Essay(Reader.readTestingFile(file.getAbsolutePath()));
//				System.out.println(essay.getSentences());
				grader.checkEssay(essay);
			}
		}

//		List<String> temp = new ArrayList<String>(); 
//		temp.add("My name is Aurora Garcia I likes English to learn because I understands with my supervisor");
//		Essay es = new Essay(temp);	
//
//		Sentence s = new Sentence("I like to running");
//		WordOrder.getWordOrderErrors(s);
//		System.out.println("\n" + Criteria.isVerbNounAgreeing(s));
//		System.out.println(Criteria.isVerbAgreeing(s));	
	}
	
	
	/**
	 * Perform criteria checks for each sentence in essay.
	 * @param essay
	 * @return Points object
	 */
	public void checkEssay(Essay essay) {
		for(Sentence s : essay.getSentences()) {
			
			System.out.println("\n" + s);
			
			//all criteria checks
			WordOrder.getWordOrderErrors(s);
			Criteria.isVerbNounAgreeing(s);
			Criteria.isVerbAgreeing(s);
			
			s.printAllErrors();
		}
		System.out.println("\n--------------------------------------------\n");
	}
	
	/**
	 * Assign a point (1-5) for each criteria according to number of errors. 
	 */
	private void calculatePoints() {
		
	}
	
	/**
	 * Calculate the final grade as per essay Points.
	 */
	private void calculateFinalGrade() {
		
	}
}
