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


	static boolean printErrorDetails = false;
	public static void main(String[] args) {

		if(args.length != 1 && args.length != 2)
		{
			System.err.println("You have to specify a file name or folder path containing the" +
					"documents which are to be test \n Please try again..");

			System.err.println("To print just the individual scores  use \t java -jar essay.jar <filename|folder name>");
			System.err.println("Inorder to print error details please use \t java -jar essay.jar <filename|folder name> <\"details\"> ");

			System.exit(1);
		}

		if(args.length == 2)
		{
			printErrorDetails = true;
		}

		File corpusPath = new File(args[0]);
		Main grader = new Main();

		if( !corpusPath.exists() )
		{
			System.err.println("File or directory not found " + args[0]);
			System.exit(1);
		}

		if(corpusPath.isFile())
		{
			System.out.println(corpusPath.getName());
			Essay essay = new Essay(Reader.readTestingFile(corpusPath.getAbsolutePath()));
			grader.checkEssay(essay);
		}
		else
		{
			for(File file : corpusPath.listFiles()) {
				if(file.isFile()) {
					System.out.println(file.getName());
					Essay essay = new Essay(Reader.readTestingFile(file.getAbsolutePath()));
					grader.checkEssay(essay);

				}
			}
		}
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

			WordOrder.getWordOrderErrors(s);
			Criteria.isVerbNounAgreeing(s);
			Criteria.isVerbAgreeing(s);

			b1+=s.getErrors().get("1b").getErrorCount();
			a1+=s.getErrors().get("1a").getErrorCount();
			c1+=s.getErrors().get("1c").getErrorCount();

			if(printErrorDetails)
				s.printAllErrors();

		}

		System.out.println("Number of sentences = " + n + ", number of 1a error = " + a1 + ", number of 1b error = " + b1 + ", number of 1c error = " + c1);
		System.out.println("Scores are ");
		System.out.println("1a = " + Math.round((5 * ((n-a1)/n) )));
		System.out.println("1b = " + Math.round((5 * ((n-b1)/n) )));
		System.out.println("1c = " + Math.round((5 * ((n-c1)/n) )));
		
		if(n >= 5)
			System.out.println("3a = 5");
		else
			System.out.println("3a = " + n);
		
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
