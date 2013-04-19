package nlp.grader.objects;

import java.util.ArrayList;
import java.util.HashMap;

import nlp.grader.utils.SParser;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * Represents an input String sentence with its parsed  
 * @author renus
 *
 */
public class Sentence {

	private String sentence;
	private SParser parser;
	private HashMap<String, ErrorDetails> parseErrors;

	public Sentence(String sentence) {
		this.sentence = sentence;
		this.parser = new SParser(this.sentence);
		this.parseErrors = new HashMap<String, ErrorDetails>();
	}

	public Tree getParseTree() {
		return this.parser.getParseTree();
	}

	public ArrayList<TaggedWord> getTaggedWords() {
		return this.parser.getTaggedWords();
	}

	public TypedDependency[] getDependencyTree() {
		return this.parser.getDependencyTree();
	}	

	public void getWords() {

	}

	public void getTags() {

	}

	public HashMap<String, ErrorDetails> getErrors() {
		return this.parseErrors;
	}


	public void printAllErrors()
	{
		boolean status = false;
		System.out.println("\n" + this.toString());
		System.out.println(getTaggedWords());
	
		for(String key :  parseErrors.keySet())
		{
			if(parseErrors.get(key).getErrorCount() > 0)
			{
				status = true;
				System.out.println("\t" + parseErrors.get(key).toString());
//				System.out.println("***********************\n");
			}
		}

	}
		

	// ************never change this ********************8
	@Override
	public String toString() {
		return this.sentence;
	}
}
