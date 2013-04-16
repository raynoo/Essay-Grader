package nlp.grader.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nlp.grader.objects.ErrorDetails;
import nlp.grader.objects.Rule;
import nlp.grader.objects.Rules;
import nlp.grader.objects.Sentence;
import nlp.grader.objects.Tags;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * Performs necessary checks for grading criteria 1b, 1c
 * @author renus
 *
 */
public class Criteria {
	
	/**
	 * Criteria 1c
	 * @param sentence
	 * @return
	 */
	public static ErrorDetails isVerbAgreeing(Sentence sentence) {
		ErrorDetails errors1c;
		
		if(sentence.getErrors().containsKey("1c"))
			errors1c = sentence.getErrors().get("1c");
		else
			errors1c = new ErrorDetails("1c");
		
		List<TaggedWord> taggedWords = sentence.getTaggedWords();
		int i = 0;
		
		//check for verb-gerund sequence
		for(Iterator<TaggedWord> iter = taggedWords.iterator(); iter.hasNext(); i++) {
			TaggedWord word = iter.next();
			TaggedWord prev = null;
			
			//if gerund is present, check if preceding tag is among blacklisted tags
			if(word.tag().equals("VBG")) {
				if(i-1 >= 0) {
					prev = taggedWords.get(i-1);
					
					if(Rules.getVGerundErrorRules().contains(prev.tag())) {
						errors1c.addError("Verb-Gerund do not agree. [" + prev + "-" + word + "]");
					}
				}
			}
		}
		
		String lhs, rhs;
		
		//check for aux dependency of 2 verbs
		TypedDependency[] list = sentence.getDependencyTree();
		for(TypedDependency dep : list) {
			if(dep.reln().getShortName().equals("aux")) {
				lhs = taggedWords.get(dep.dep().index()-1).tag();
				rhs = taggedWords.get(dep.gov().index()-1).tag();
				
				if(!isVerbVerbAgreeing(lhs, rhs)) {
					errors1c.addError("Verb-Verb do not agree. [" + dep.dep() + lhs + "-" + dep.gov() + rhs + "]");
				}
			}
		}
		
		//parse through sentence to get consecutive verb-verb pairs that are not covered in aux's
		for(int j = 0; j < taggedWords.size() && j+1 < taggedWords.size(); j++) {
			TaggedWord firstVerb = taggedWords.get(j), secondVerb = taggedWords.get(j+1);
			
			if(firstVerb.tag().equals("MD") && !isVerbTag(secondVerb.tag())) {
				//if MD is encountered, then next word should be a verbtag
				errors1c.addError("Missing Verb. A verb should follow a modal. [" + firstVerb + "]");
			}
			
			//any other verb-verb combinations?
			if(isVerbTag(firstVerb.tag()) && isVerbTag(secondVerb.tag())) {
				if(!isVerbVerbAgreeing(secondVerb.tag(), firstVerb.tag())) {
					errors1c.addError("Other: Verb-Verb do not agree. [" + firstVerb + "-" + secondVerb + "]");
				}
			}
		}
		
		//put back all errors
		sentence.getErrors().put("1c", errors1c);
		return sentence.getErrors().get("1c");
	}
	
	/**
	 * Check if 2 tags (lhs = verb rhs = verb) agree as per verb-verb agreement rules
	 * @param lhs
	 * @param rhs
	 * @return agreement
	 */
	public static boolean isVerbVerbAgreeing(String lhs, String rhs) {
		List<Rule> verbVerbRules = Rules.getVerbVerbRules();
		
		//nsubj's governor is not a verb in all cases. it can be adjective or noun.
		if(isVerbTag(lhs)) {
			for(Rule r : verbVerbRules) {
				if(r.lhs().equals(lhs) && r.rhs().equals(rhs))
					return true;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Criteria 1b
	 * checks for verb-noun agreement of a sentence according to rules in rules/
	 * @param sentence
	 * @return number of errors in sentence
	 */
	public static ErrorDetails isVerbNounAgreeing(Sentence sentence) {
		List<TypedDependency> nsubjs = new ArrayList<TypedDependency>();
		List<TypedDependency> auxs = new ArrayList<TypedDependency>();
		List<TypedDependency> conj = new ArrayList<TypedDependency>();
		
		ErrorDetails errors1b;
		
		if(sentence.getErrors().containsKey("1b"))
			errors1b = sentence.getErrors().get("1b");
		else
			errors1b = new ErrorDetails("1b");
		
		//get the dependency tree (in list form)
		TypedDependency[] list = sentence.getDependencyTree();
		
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
			} else if(dep.reln().getShortName().equals("conj")) {
				conj.add(dep);
			}
		}
		
		//if none are present, grammar is wrong
		if(nsubjs.isEmpty()) {
			errors1b.addError("No Subject-Verb relation present.");
			sentence.getErrors().put("1b", errors1b);
			return sentence.getErrors().get("1b");
		}
		
		//list of words and its tags. to get the tag of a given word.
		List<TaggedWord> taggedWords = sentence.getTaggedWords();
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
						
						if(!isVerbNounAgreeing(lhs, rhs)) {
							//if lhs is plural verb and rhs is singular noun, check for a conj with same noun
							if(!isConjPresent(lhs, rhs, depnsubj, conj))
								errors1b.addError("Subject-Verb do not agree. [" + depaux.dep() + lhs + "-" + depnsubj.dep() + rhs + "]");
						} else {
							//check for wrong combinations
							//check if rhs is prp -> he/she -> lhs shud be singular
							if(!isPRPAgreeing(taggedWords.get(depaux.dep().index()-1), taggedWords.get(depnsubj.dep().index()-1)))
								errors1b.addError("Subject-Verb do not agree. [" + depaux.dep() + lhs + "-" + depnsubj.dep() + rhs + "]");
						}
					}
				}
			}
		}
		//handle nsubj, nsubpass alone
		if(!nsubjs.isEmpty()) {
			for(TypedDependency dep : nsubjs) {
				lhs = taggedWords.get(dep.gov().index()-1).tag();
				rhs = taggedWords.get(dep.dep().index()-1).tag();

				if(!isVerbNounAgreeing(lhs, rhs)) {
					//if lhs is plural verb and rhs is singular noun, check for a conj with same noun
					if(!isConjPresent(lhs, rhs, dep, conj))
						errors1b.addError("Subject-Verb do not agree. [" + dep.gov() + lhs + "-" + dep.dep() + rhs + "]");
				} else {
					//check for wrong combinations
					//check if rhs is prp -> he/she -> lhs shud be singular
					if(!isPRPAgreeing(taggedWords.get(dep.gov().index()-1), taggedWords.get(dep.dep().index()-1)))
						errors1b.addError("Subject-Verb do not agree. [" + dep.gov() + lhs + "-" + dep.dep() + rhs + "]");
				}
			}
		}
		//parse through sentence to get consecutive noun-verb pairs
		for(int i = 0; i < taggedWords.size(); i++) {
			if(isNounTag(taggedWords.get(i).tag()) 
					&& i != taggedWords.size()-1 && isVerbTag(taggedWords.get(i+1).tag())) {
				
				TaggedWord noun = taggedWords.get(i), verb = taggedWords.get(i+1);
				boolean alreadyProcessed = false;
				
				//has this noun been processed earlier, as part of nsubj?
				for(TypedDependency td : nsubjs) {
					if(td.dep().value().equals(noun.value()))
						alreadyProcessed = true;
				}
				//if not, then what are you waiting for? check agreement for this pair!
				if(!alreadyProcessed && !isVerbNounAgreeing(verb.tag(), noun.tag())) {
					errors1b.addError("Subject-Verb do not agree. [" + noun + "-" + verb + "]");
				}
			}
		}
		
		//put back all errors
		sentence.getErrors().put("1b", errors1b);
		return sentence.getErrors().get("1b");
	}
	
	/**
	 * Check verb agreement for personal pronouns 
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	private static boolean isPRPAgreeing(TaggedWord lhs, TaggedWord rhs) {
		if(lhs.tag().equals("VBZ") && rhs.tag().equals("PRP")) {
			//if not he/she, the verb cannot be vbz
			if(!(rhs.word().equalsIgnoreCase("he") || rhs.word().equalsIgnoreCase("she")))
				return false;
		}
		return true;
	}
	
	/**
	 * Checks for a conjunction with given verb-noun pair, where noun is a personal pronoun
	 * @param lhs
	 * @param rhs
	 * @param dep
	 * @param conj
	 * @return
	 */
	private static boolean isConjPresent(String lhs, String rhs, TypedDependency dep, List<TypedDependency> conj) {
		//personal pronouns can have vbp and vbg only if it is part of a conjunction
		if((rhs.equals("NN") || rhs.equals("NNP") 
				|| dep.dep().nodeString().equalsIgnoreCase("i") || dep.dep().nodeString().equalsIgnoreCase("me") 
				|| dep.dep().nodeString().equalsIgnoreCase("he") || dep.dep().nodeString().equalsIgnoreCase("she"))
			&& (lhs.equals("VBP") || lhs.equals("VBG"))) {

			if(!conj.isEmpty()) {
				for(TypedDependency d : conj) {
					if(d.dep().nodeString().equals(dep.dep().nodeString()) ||
							d.gov().nodeString().equals(dep.dep().nodeString()))
						
						return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Check if 2 tags (lhs = verb rhs = noun) agree as per verb-noun agreement rules
	 * @param lhs
	 * @param rhs
	 * @return agreement
	 */
	private static boolean isVerbNounAgreeing(String lhs, String rhs) {
		List<Rule> verbNounRules = Rules.getVerbNounRules();
		
		//nsubj's governor is not a verb in all cases. it can be adjective or noun.
		if(isVerbTag(lhs)) {
			for(Rule r : verbNounRules) {
				if(r.lhs().equals(lhs) && r.rhs().equals(rhs))
					return true;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Is a tag that of a verb?
	 * @param tag
	 * @return
	 */
	private static boolean isVerbTag(String tag) {
		return Tags.getVerbTags().contains(tag);
	}
	
	/**
	 * Is a tag that of a noun?
	 * @param tag
	 * @return
	 */
	private static boolean isNounTag(String tag) {
		return Tags.getNounTags().contains(tag);
	}
}
