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
	 * Read from input file and passes essay to Grader to get final grade.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {



				File corpusPath = new File("test_corpus");
				
				for(File file : corpusPath.listFiles()) {
					if(file.isFile()) {
						System.out.println(file.getName());
						Essay essay = new Essay(Reader.readTestingFile(file.getAbsolutePath()));
						System.out.println(essay.getSentences());
						
//						for(Sentence sentence :essay.getSentences()){
//							Criteria.isVerbAgreeing(sentence);
//							Criteria.isVerbNounAgreeing(sentence);
//							WordOrder.getWordOrderErrors(sentence);
//							
//							System.out.println(sentence);
//							System.out.println(sentence.getTaggedWords());
//							sentence.printAllErrors();
//							
//							
//		
//						}
//						System.out.println("\n--------------------------------------------\n");
						
					}
				}


//		List<String> temp = new ArrayList<String>(); 
//		temp.add("My name is Aurora Garcia I likes English to learn because I understands with my supervisor");
//		Essay es = new Essay(temp);	

		//		Sentence s = new Sentence("I like to running");
		//		WordOrder.getWordOrderErrors(s);
		////		System.out.println("\n" + Criteria.isVerbNounAgreeing(s));
		////		System.out.println(Criteria.isVerbAgreeing(s));	
	}

}
