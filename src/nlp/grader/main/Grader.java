package nlp.grader.main;

import nlp.grader.objects.Essay;
import nlp.grader.objects.Sentence;

/**
 * Checks for errors in essay according to Criteria.
 * 
 * @author renus
 *
 */
public class Grader {
	
	/**
	 * Processes the essay and calculates grade.
	 * @param essay
	 * (@return final grade)
	 */
	public void gradeEssay(Essay essay) {
		checkEssay(essay);
		calculatePoints();
		calculateFinalGrade();
	}
	
	/**
	 * Perform criteria checks for each sentence in essay.
	 * @param essay
	 * @return Points object
	 */
	private void checkEssay(Essay essay) {
		for(Sentence s : essay.getSentences()) {
			System.out.println("\n" + Criteria.isVerbNounAgreeing(s));
			System.out.println(Criteria.isVerbAgreeing(s));
		}
		
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
