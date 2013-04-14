package nlp.grader.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * Provides parsing and tagging functions using Stanford Parser.
 * @author renus
 *
 */
public class SParser {
	
	/**
	 * Process a sentence using stanford parser to get its parse tree.
	 * @param sentence
	 * @return parse tree
	 */
	public static Tree getParseTree(String sentence) {
		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});

		Tree tree = (Tree) lp.apply(sentence);

		//depth is 1 when parsing is not possible.
//		System.out.println(tree.depth());

		return tree;
	}
	
	/**
	 * Get the dependency tree of a parse tree in a list form.
	 * @param tree
	 * @return dependency list
	 */
	public static TypedDependency[] getDependencyTree(Tree tree) {
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Collection<TypedDependency> td = gs.typedDependenciesCollapsed();

		System.out.println(td);
		
		return td.toArray(new TypedDependency[0]);
	}
	
	/**
	 * Get the dependency tree of a sentence in a list form.
	 * @param sentence
	 * @return dependency list
	 */
	public static TypedDependency[] getDependencyTree(String sentence) {
		//get parse tree
		Tree tree = getParseTree(sentence);
		
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Collection<TypedDependency> td = gs.typedDependenciesCollapsed();

		System.out.println(td);
		
		return td.toArray(new TypedDependency[0]);
	}
	
	/**
	 * Process a sentence using stanford parser's POS Tagger to get tagged words.
	 * @param sentence
	 * @return list of tagged words
	 */
	public static List<TaggedWord> getWordTags(String sentence) {
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
}
