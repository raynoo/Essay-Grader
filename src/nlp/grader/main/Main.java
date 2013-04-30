package nlp.grader.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

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
	static StringBuffer outputString = null;
	
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
			Essay essay = new Essay(Reader.readTestingFile(corpusPath.getAbsolutePath()),corpusPath.getName());
			grader.checkEssay(essay);
			writeOutput();
		}
		else
		{
			//making sure its sorted
			File[] files = corpusPath.listFiles();
			Arrays.sort(files);
			
			for(File file : files) {
				if(file.isFile()) {		
					Essay essay = new Essay(Reader.readTestingFile(file.getAbsolutePath()),file.getName());
					grader.checkEssay(essay);
				}
			}
			writeOutput();
		}
	}


	/**
	 * Perform criteria checks for each sentence in essay.
	 * @param essay
	 * @return Points object
	 */
	public void checkEssay(Essay essay) {
		float n = essay.getSentences().size();
		float d1 = 0, a1 = 0, b1 = 0, c1 = 0, a2 = 0, b2 = 0, a3 = n, finalgrade = 0;

		
		//Part 1 - 1a, 1b, 1c, 3
		for( Sentence s : essay.getSentences() ) {

			WordOrder.getWordOrderErrors(s);
			Criteria.isVerbNounAgreeing(s);
			Criteria.isVerbAgreeing(s);

			b1+=s.getErrors().get("1b").getErrorCount();
			a1+=s.getErrors().get("1a").getErrorCount();
			c1+=s.getErrors().get("1c").getErrorCount();

			if(printErrorDetails)
				s.printAllErrors();
		}
		if(n >= 5)
			a3 = 5;
		
		//Part 2 - 1d, 2a, 2b 
		SemanticTwoA.processSecondPart(essay); // calculate 2a score
		SemanticTwoB.processSecondPart(essay); // calculate 2b score		
		a2 = essay.getTwoBScore();
		d1 = getOneD(essay);
		
		
		
		//2a crap
		int sbars = WordOrder.countSbar(essay);
		int twoa = essay.getTwoAErrors();
		essay.setTwoAErrors( (int) (twoa + ((sbars/2) + b1)/2) );
		essay.setTwoAScore(calculatePoints(essay.getTwoAErrors(), essay.getOriginalSentence().size()));
//		System.out.println("2a: " + twoa + ", sbars: " + sbars + ", 1b: " + b1 + ", " + essay.getOriginalSentence().size());
		b2 = essay.getTwoAScore();
		
		finalgrade = calculateFinalGrade(calculatePoints(a1, n), calculatePoints(b1, n), calculatePoints(c1, n), d1, a2, b2, a3);
		
		
		
		System.out.println(essay.getFilename() + "\n");
		System.out.println("Number of sentences = " + n);
		System.out.println("number of 1a error = " + a1 + ", number of 1b error = " + b1 + ", number of 1c error = " + c1);
//		System.out.println("number of 1d error = " + d1 + ", number of 2a error = " + a2 + ", number of 2b error = " + b2 + ", number of 3a error = " + a3);
		System.out.println("\nScores are:");
		System.out.println("1a = " + calculatePoints(a1, n));
		System.out.println("1b = " + calculatePoints(b1, n));
		System.out.println("1c = " + calculatePoints(c1, n));
		System.out.println("1d = " + d1);
		System.out.println("2a = " + a2);
		System.out.println("2b = " + b2);
		System.out.println("3a = " + a3);
		System.out.println("Final Grade: " + finalgrade);
		
		System.out.println("\n--------------------------------------------\n");
		
		updateOutputString(calculatePoints(a1, n), calculatePoints(b1, n), calculatePoints(c1, n), d1, a2, b2, a3, finalgrade);
	}

	private static void updateOutputString(float a1, float b1, float c1, float d1, 
			float a2, float b2, float a3, float finalGrade) {
		
		if(outputString == null) {
			//header
			outputString = new StringBuffer("1a,1b,1c,1d,2a,2b,3a,final\n");
		}
		//entry for each essay
		outputString.append((int)a1 + "," + (int)b1 + "," + (int)c1 + "," + (int)d1 + "," +
				(int)a2 + "," + (int)b2 + "," + (int)a3 + "," + finalGrade + "\n");
	}
	
	private static void writeOutput() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("output.txt")));
			bw.write(outputString.toString());
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int getOneD(Essay essay)
	{
		float d1 = 0;
		int sbar = WordOrder.countSbar(essay);
		int temp1 = 0,temp2 = 0,temp3 = 0;
		
		int originalSentenceNumber = essay.getOriginalSentence().size();
		for(Sentence s : essay.getOriginalSentence()){
			WordOrder.getWordOrderErrors(s);
			Criteria.isVerbNounAgreeing(s);
			Criteria.isVerbAgreeing(s);
			
			temp1+=s.getErrors().get("1b").getErrorCount();
			temp2+=s.getErrors().get("1a").getErrorCount();
			temp3+=s.getErrors().get("1c").getErrorCount();
		}
		
		float ratio = (sbar+temp1 + temp2 + temp3)/(float)originalSentenceNumber;
		
		if(ratio < .5){
			d1 = 5;
//			System.out.println(essay.getFilename());
		}
		else if(ratio<=1.3){
			d1 = 4;
//			System.out.println(essay.getFilename());
		}
		if(ratio > 1.3){
			d1 = 1;
//			System.out.println(essay.getFilename());
		}
		
		return (int)d1;

	}
	
	//Assign a point (1-5) for each criteria according to number of errors.
	static int calculatePoints(float errorNumber, float essaySize) {
		return Math.round(5 * ( (essaySize - errorNumber) / essaySize) );
	}

	private static float calculateFinalGrade(float a1, float b1, float c1, float d1, float a2, float b2, float a3) {
		float score = (a1 + b1 + c1 + 2*d1 + a2 + 3*b2 + a3) / 10;
		float decimal = score - (int) score;
		
		if(decimal >= 0.25f && decimal < 0.75f)
			score = ((int) score) + 0.5f;
		else
			score = Math.round(score);
		
		return score;
	}
}
