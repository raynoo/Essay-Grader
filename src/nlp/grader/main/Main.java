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

		float f= 3.4f;
		int k = (int) f;
		System.out.println(k);
		File corpusPath = new File("test_corpus");
		Main grader = new Main();

		for(File file : corpusPath.listFiles()) {
			if(file.isFile()) {
				System.out.println(file.getName());
				Essay essay = new Essay(Reader.readTestingFile(file.getAbsolutePath()));
				grader.checkEssay(essay);

			}
		}


		//		Sentence s = new Sentence("she came");
		//		WordOrder.getWordOrderErrors(s);
		//		
		//		s.printAllErrors();

	}


	/**
	 * Perform criteria checks for each sentence in essay.
	 * @param essay
	 * @return Points object
	 */
	public void checkEssay(Essay essay) {
		float a1 = 0;
		float b1 = 0;
		float c1 = 0;
		float n = essay.getSentences().size();

		for(Sentence s : essay.getSentences()) {

			//System.out.println("\n" + s + "\t" + s.getTaggedWords());

			//all criteria checks
//			WordOrder.getWordOrderErrors(s);
//			Criteria.isVerbNounAgreeing(s);
//			b1+=s.getErrors().get("1b").getErrorCount();
//			a1+=s.getErrors().get("1a").getErrorCount();
			Criteria.isVerbAgreeing(s);
			s.printAllErrors();
			c1+=s.getErrors().get("1c").getErrorCount();


		}

//		System.out.println("Number of sentences = " + n + " 1a error = " + a1 + " 1b error " + b1);
//		System.out.println("1a = " + Math.round((5 * ((n-a1)/n) )));
//		System.out.println("1b = " + Math.round((5 * ((n-b1)/n) )));
//		System.out.println("1c = " + Math.round((5 * ((n-c1)/n) )));
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
