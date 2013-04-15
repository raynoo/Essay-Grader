package nlp.grader.main;

import java.io.File;

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
	 * Read from input file and passes essay to Grader to get final grade.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		File corpusPath = new File("test_corpus");
		
		for(File file : corpusPath.listFiles()) {
			if(file.isFile()) {
				Essay essay = new Essay(Reader.readTestingFile(file.getAbsolutePath()));
				Grader essayGrader = new Grader();
				
				essayGrader.gradeEssay(essay);
				System.out.println("\n--------------------------------------------\n");
				
			}
		}
		
		
//		Sentence s = new Sentence("Julie jump over the moon.");
//		System.out.println("\n" + Criteria.isVerbNounAgreeing(s));
//		System.out.println(Criteria.isVerbAgreeing(s));	
	}
	
}
