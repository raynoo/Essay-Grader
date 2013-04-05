package nlp.grader.main;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Main {

	/**
	 * Demo of generating parse tree using PCFG.
	 * @param args
	 */
	public static void main(String[] args) {
		
		Main main = new Main();
//		main.spellCheck();
		List<TaggedWord> twords = main.getWordTags("The strongest rain ever recorded " +
				"in India shut down the financial hub of Mumbai, snapped communication lines, " +
				"closed airports and forced thousands of people to sleep in their offices or walk " +
				"home during the night, officials said today.");
		
		for(TaggedWord s : twords)
			System.out.println("Word: " + s.value() + " Tag: " + s.tag());
	}
	
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
	
	public void spellCheck() {
		
	}

}
