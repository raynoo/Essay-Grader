package nlp.grader.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class Criteria {
	
	public static List<TypedDependency> isVerbAgreeing(Tree tree) {
		List<TypedDependency> nsubjs = new ArrayList<TypedDependency>();
		
		//get the dependency tree
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Collection<TypedDependency> td = gs.typedDependenciesCollapsed();

		System.out.println(td);

		TypedDependency[] list = td.toArray(new TypedDependency[0]);
		
		for(TypedDependency dep : list) {
			if (dep.reln().getShortName().equals("nsubj")) {
				nsubjs.add(dep);
			}
		}
		
		List<TaggedWord> taggedWords = tree.taggedYield();

		for(TypedDependency dep : nsubjs) {
			//governor word is the verb
			String lhs = taggedWords.get(dep.gov().index()-1).tag();
			//dependency word is the noun
			String rhs = taggedWords.get(dep.dep().index()-1).tag();
			
			System.out.println("");
			System.out.println(dep.gov().index() + ", " + dep.gov().value() + ", " + dep.dep().index() + ", " + dep.dep().value());
			System.out.println(lhs + ", " + rhs);
			
//			isVerbAgreeing(lhs, rhs);
		}
		
		return nsubjs;
	}
	
	private static boolean isVerbAgreeing(String verbTag, String nounTag) {
		List<Rule> verbNounRules = loadVerbNounRules();
		
		for(Rule r : verbNounRules) {
			if(r.lhs().equals(verbTag) && r.rhs().equals(nounTag))
				return true;
		}
		
		return false;
	}
	
	public static List<Rule> loadVerbNounRules() {
		BufferedReader br = null;
		String line;
		List<Rule> rules = new ArrayList<Rule>();
		
		try {
			br = new BufferedReader(new FileReader("rules/verb_noun_agreement_rules.txt"));

			while ((line = br.readLine()) != null) {
				System.out.println(line.split("\\s"));
				String[] rule = line.split("\\s");
				rules.add(new Rule(rule[0], rule[1]));
			}

			br.close();
			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return rules;
	}
}
