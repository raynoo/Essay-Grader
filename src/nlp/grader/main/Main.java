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

	public static void main(String[] args) {
		
		Main main = new Main();
		String input = "My dog is usually good.";

//		List<TaggedWord> twords = main.getWordTags(input);
		
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
		List<TaggedWord> twords = tagger.tagSentence(tokenList.get(0));
		
		for(TaggedWord s : twords)
			System.out.println("Word: " + s.value() + " Tag: " + s.tag());
		
		return twords;
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
	
	private void spellCheck() {
		
	}

}
