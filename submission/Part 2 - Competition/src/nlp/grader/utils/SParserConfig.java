package nlp.grader.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
//import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Provides instance of parser and tagger.
 * @author renus
 *
 */
public class SParserConfig {
	
	private static LexicalizedParser lp;
//	private static MaxentTagger tagger;

	public static LexicalizedParser getLexParser() {
		if(lp == null) {
			lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
			lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
		}
		return lp;
	}
	
//	public static MaxentTagger getTagger() {
//		if(tagger == null) {
//			try {
//				tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//		return tagger;
//	}
//	
//	public static List<TaggedWord> getWordTags(String sentence) {		
//		List<List<HasWord>> tokenList = MaxentTagger.tokenizeText(new StringReader(sentence));
//		return tagger.tagSentence(tokenList.get(0));
//	}
}
