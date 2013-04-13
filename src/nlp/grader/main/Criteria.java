package nlp.grader.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nlp.grader.objects.Rule;
import nlp.grader.objects.Tags;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class Criteria {
	
	private static List<Rule> verbNounRules = null;
	
	public static boolean isVerbAgreeing(Tree tree) {
		List<TypedDependency> nsubjs = new ArrayList<TypedDependency>();
		boolean agreement = true;
		int numOfNsubj = 0;
		
		//get the dependency tree
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Collection<TypedDependency> td = gs.typedDependenciesCollapsed();

		System.out.println(td);

		TypedDependency[] list = td.toArray(new TypedDependency[0]);
		
		for(TypedDependency dep : list) {
			if(dep.reln().getShortName().equals("nsubj") || dep.reln().getShortName().equals("cop")) {
				nsubjs.add(dep);
			}
		}
		
		if(nsubjs.isEmpty())
			return false;
		
		List<TaggedWord> taggedWords = tree.taggedYield();

		for(TypedDependency dep : nsubjs) {
			//governor word is the verb
			String lhs = taggedWords.get(dep.gov().index()-1).tag();
			//dependency word is the noun
			String rhs = taggedWords.get(dep.dep().index()-1).tag();
			
			System.out.println("");
			System.out.println(dep.gov().index() + ", " + dep.gov().value() + ", " + 
					dep.dep().index() + ", " + dep.dep().value());
			System.out.println(lhs + ", " + rhs);
			
			if(!isVerbAgreeing(lhs, rhs)) {
				agreement = false;
				break;
			}
		}
		System.out.println("\nVerb-Noun agreement: " + agreement);
		return agreement;
	}
	
	private static boolean isVerbAgreeing(String lhs, String rhs) {
		List<Rule> verbNounRules = getVerbNounRules();
		
		//nsubj's governor is not a verb in all cases. it can be adjective or noun.
		if(isVerbTag(lhs)) {
			for(Rule r : verbNounRules) {
				if(r.lhs().equals(lhs) && r.rhs().equals(rhs))
					return true;
			}
		} else if(isVerbTag(rhs)) {
			for(Rule r : verbNounRules) {
				if(r.lhs().equals(rhs) && r.rhs().equals(lhs))
					return true;
			}
		}
		return false;
	}
	
	private static boolean isVerbTag(String tag) {
		return Tags.getVerbTags().contains(tag);
	}
	
	private static List<Rule> getVerbNounRules() {
		if(verbNounRules == null) {
			verbNounRules = new ArrayList<Rule>();
			
			BufferedReader br = null;
			String line;
			
			try {
				br = new BufferedReader(new FileReader("rules/verb_noun_agreement_rules.txt"));
				
				while ((line = br.readLine()) != null) {
					String[] rule = line.split("\\s");
					verbNounRules.add(new Rule(rule[0], rule[1]));
				}
				
				br.close();
				
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return verbNounRules;
	}
}
