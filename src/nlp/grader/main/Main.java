package nlp.grader.main;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

public class Main {

	HashMap<String, String> tagMap = new HashMap<String, String>();
	
	/**
	 * Demo of generating parse tree using PCFG.
	 * @param args
	 */
	public static void main(String[] args) {
		
		Main main = new Main();
		String input = "The strongest rain ever recorded " +
				"in India shut down the financial hub of Mumbai, snapped communication lines, " +
				"closed airports and forced thousands of people to sleep in their offices or walk " +
				"home during the night, officials said today.";

		List<TaggedWord> twords = main.getWordTags(input);
		
		for(TaggedWord s : twords)
			System.out.println("Word: " + s.value() + " Tag: " + s.tag());
		
		Tree tree = main.getParseTree(input);
		Criteria.isVerbAgreeing(tree);
	}
	
	//process each sentence using pos tagger
	public List<TaggedWord> getWordTags(String sentence) {
		MaxentTagger tagger = null;
		try {
			tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		List<List<HasWord>> tokenList = MaxentTagger.tokenizeText(new StringReader(sentence));
		
		return tagger.tagSentence(tokenList.get(0));
	}
	
	//process each sentence using parser to get parse tree
	public Tree getParseTree(String sentence) {
		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});

		Tree tree = (Tree) lp.apply(sentence);
		
		//depth is 1 when parsing is not possible.
		System.out.println(tree.depth());
		
		return tree;
	}
	
	public void spellCheck() {
		
	}

}
