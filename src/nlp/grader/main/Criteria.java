package nlp.grader.main;

import java.util.ArrayList;
import java.util.List;

import nlp.grader.objects.Rule;
import nlp.grader.objects.Rules;
import nlp.grader.objects.Tags;
import nlp.grader.utils.SParser;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

public class Criteria {
	
	/**
	 * checks for verb-noun agreement of a sentence according to rules in rules/
	 * @param sentence
	 * @return number of errors in sentence
	 */
	public static int isVerbAgreeing(String sentence) {
		List<TypedDependency> nsubjs = new ArrayList<TypedDependency>();
		List<TypedDependency> auxs = new ArrayList<TypedDependency>();
		
		int errors = 0;
		
		//get parse tree
		Tree tree = SParser.getParseTree(sentence);
		//get the dependency tree (in list form)
		TypedDependency[] list = SParser.getDependencyTree(tree);
		
		//collect all nsubj, cop, nsubjpass dependencies
		for(TypedDependency dep : list) {
			if(dep.reln().getShortName().equals("nsubj")) {
				nsubjs.add(dep);
			} else if(dep.reln().getShortName().equals("nsubjpass")) {
				nsubjs.add(dep);
			} else if(dep.reln().getShortName().equals("cop")) {
				auxs.add(dep);
			} else if(dep.reln().getShortName().equals("aux")) {
				auxs.add(dep);
			} else if(dep.reln().getShortName().equals("auxpass")) {
				auxs.add(dep);
			}
		}
		
		//if none are present, grammar is wrong
		if(nsubjs.isEmpty() && auxs.isEmpty()) {
			return ++errors;
		}
		
		//list of words and its tags. to get the tag of a given word.
		List<TaggedWord> taggedWords = tree.taggedYield();
		//lhs = TypedDependency's governor word = verb
		//rhs = TypedDependency's dependency word = noun
		String lhs, rhs;
		
		//handle nsubj with cop, aux, auxpass
		if(!auxs.isEmpty()) {
			for(TypedDependency depnsubj : nsubjs) {
				for(TypedDependency depaux : auxs) {
					if(depnsubj.gov().nodeString().equals(depaux.gov().nodeString())) {

						lhs = taggedWords.get(depaux.dep().index()-1).tag();
						rhs = taggedWords.get(depnsubj.dep().index()-1).tag();
						
						if(!isVerbAgreeing(lhs, rhs))
							errors++;
					}
				}
			}
		} else {
			//handle nsubj, nsubpass alone
			if(!nsubjs.isEmpty()) {
				for(TypedDependency dep : nsubjs) {
					lhs = taggedWords.get(dep.gov().index()-1).tag();
					rhs = taggedWords.get(dep.dep().index()-1).tag();
					
					if(!isVerbAgreeing(lhs, rhs))
						errors++;
				}
			}
		}
		return errors;
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
		return false;
	}
	
	private static boolean isVerbTag(String tag) {
		return Tags.getVerbTags().contains(tag);
	}
	
	private static boolean isNounTag(String tag) {
		return Tags.getNounTags().contains(tag);
	}
}
