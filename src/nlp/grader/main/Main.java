package nlp.grader.main;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.Tree;

public class Main {

	/**
	 * Demo of generating parse tree using PCFG.
	 * @param args
	 */
	public static void main(String[] args) {
		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		lp.setOptionFlags(new String[]{"-outputFormat", "penn,typedDependenciesCollapsed", "-retainTmpSubcategories"});
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		
		new Main().processSentence("Following exception caught during parsing.", tokenizerFactory, lp);
	}
	
	public Tree processSentence(String sentence, TokenizerFactory<CoreLabel> tokenizerFactory,
			LexicalizedParser lp) {
		List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(sentence)).tokenize();
		Tree bestParse = lp.parseTree(rawWords);
		System.out.println(bestParse.toString());
		return bestParse;
	}

}
