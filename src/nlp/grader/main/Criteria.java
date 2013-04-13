package nlp.grader.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nlp.grader.objects.Rule;
import nlp.grader.objects.Rules;
import nlp.grader.objects.Tags;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class Criteria {
	
	public static boolean isVerbAgreeing(Tree tree) {
		List<TypedDependency> nsubjs = new ArrayList<TypedDependency>();
		List<TypedDependency> cop = new ArrayList<TypedDependency>();
		List<TypedDependency> nsubjpass = new ArrayList<TypedDependency>();
		
		//get the dependency tree
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Collection<TypedDependency> td = gs.typedDependenciesCollapsed();

		System.out.println(td);

		TypedDependency[] list = td.toArray(new TypedDependency[0]);
		
		//collect all nsubj, cop, nsubjpass dependencies
		for(TypedDependency dep : list) {
			if(dep.reln().getShortName().equals("nsubj")) {
				nsubjs.add(dep);
			} else if(dep.reln().getShortName().equals("cop")) {
				cop.add(dep);
			} else if(dep.reln().getShortName().equals("nsubjpass")) {
				nsubjpass.add(dep);
			}
		}
		
		//if none are present, grammar is wrong
		if(nsubjs.isEmpty() && cop.isEmpty() && nsubjpass.isEmpty())
			return false;
		
		List<TaggedWord> taggedWords = tree.taggedYield();

		//handle nsubj with cop
		if(!cop.isEmpty()) {
			if(!nsubjs.isEmpty()) {
				for(TypedDependency depnsubj : nsubjs)
					for(TypedDependency depcop : cop) {
						if(depnsubj.gov().nodeString().equals(depcop.gov().nodeString())) {
							String lhs = taggedWords.get(depcop.dep().index()-1).tag();
							String rhs = taggedWords.get(depnsubj.dep().index()-1).tag();
							return isVerbAgreeing(lhs, rhs);
						}
					}
			}
			return false;
		}
		//handle nsubj without cop
		else {
			if(!nsubjs.isEmpty()) {
				for(TypedDependency dep : nsubjs) {
					String lhs = taggedWords.get(dep.gov().index()-1).tag();
					String rhs = taggedWords.get(dep.dep().index()-1).tag();
					return isVerbAgreeing(lhs, rhs);
				}
			}
			return false;
		}
		//handle nsubjpass
		//...
	}
	
	private static boolean isVerbAgreeing(String lhs, String rhs) {
		List<Rule> verbNounRules = Rules.getVerbNounRules();
		
		//nsubj's governor is not a verb in all cases. it can be adjective or noun.
		if(isVerbTag(lhs)) {
			for(Rule r : verbNounRules) {
				if(r.lhs().equals(lhs) && r.rhs().equals(rhs))
					return true;
			}
		} 
//		else if(isVerbTag(rhs)) {
//			for(Rule r : verbNounRules) {
//				if(r.lhs().equals(rhs) && r.rhs().equals(lhs))
//					return true;
//			}
//		}
		return false;
	}
	
	private static boolean isVerbTag(String tag) {
		return Tags.getVerbTags().contains(tag);
	}
	
}
