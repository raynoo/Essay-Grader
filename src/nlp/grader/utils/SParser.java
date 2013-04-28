package nlp.grader.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import nlp.grader.utils.SParserConfig;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

public class SParser {
	
	private String inputSentence = null;
	private Tree parseTree = null;
	private TypedDependency[] dependencyTree = null;
	private HashMap<String, ArrayList<TaggedWord>> tagset = null;
	
	public SParser(String s) {
		this.inputSentence = s;
		this.parseTree = SParserConfig.getLexParser().apply(inputSentence);
		//System.out.println(parseTree.taggedYield());
		
		GrammaticalStructureFactory gsf = new PennTreebankLanguagePack().grammaticalStructureFactory();
		Collection<TypedDependency> td = gsf.newGrammaticalStructure(this.parseTree).allTypedDependencies();
		this.dependencyTree = td.toArray(new TypedDependency[0]);
	}
	
	/**
	 * Get the dependency tree of a sentence in a list form.
	 * @param sentence
	 * @return dependency list
	 */
	public TypedDependency[] getDependencyTree() {
		return this.dependencyTree;
	}
	
	/**
	 * Process a sentence using stanford parser to get its parse tree.
	 * @param sentence
	 * @return parse tree
	 */
	public Tree getParseTree() {
		return this.parseTree;
	}
	
	/**
	 * Get all the words in sentence along with their tags.
	 * @return
	 */
	public ArrayList<TaggedWord> getTaggedWords() {
		return this.parseTree.taggedYield();
	}
	
	//hashmap of tags and words
	public HashMap<String, ArrayList<TaggedWord>> getTaggedWordsAsMap() {
		if(this.tagset == null) {
			this.tagset = new HashMap<String, ArrayList<TaggedWord>>();
			
			ArrayList<TaggedWord> tags;
			
			for(TaggedWord tw : this.parseTree.taggedYield()) {
				
				tags = this.tagset.get(tw.tag());
				if(tags == null)
					tags = new ArrayList<TaggedWord>();
				
				tags.add(tw);
				this.tagset.put(tw.tag(), tags);
			}
		}
		return this.tagset;
	}
}
