package nlp.grader.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
		HashMap<String, Set<String>> verbVerbRules = Rules.getVerbVerbRules();
		
		//keep track of first verb
        TaggedWord firstVerb = null;
        for(int j = 0; j < taggedWords.size(); j++) {
			if(isVerbTag(taggedWords.get(j).tag())) {
				firstVerb = taggedWords.get(j);
				break;
			}
        }
        
        //1. no verb in sentence
        if(firstVerb == null)
        	errors1c.addError("Main Verb missing.");
        else {
        	//2. check if first verb is any of the following (incorrect) cases.
        	if((firstVerb.tag().equals("VBN"))) { //(firstVerb.tag().equals("VBG") || || firstVerb.tag().equals("VB") 
        		errors1c.addError("Incorrect Verb order (missing verb). [" + firstVerb + "]");
        	}

        	//3. first and last words in sentence cannot be vbz
        	if(taggedWords.get(0).tag().equals("VBZ") || taggedWords.get(taggedWords.size()-1).tag().equals("VBZ")) {
        		errors1c.addError("Incorrect Verb order (cannot start or end with vbz). [" + 
        				taggedWords.get(0) + " / " + taggedWords.get(taggedWords.size()-1) + "]");
        	}

        	//traverse through tagged word list and do all checks.
        	for(int j = 1;  taggedWords.size() > 1 && j < taggedWords.size(); j++) {

        		TaggedWord prevVerb = taggedWords.get(j-1), mainVerb = taggedWords.get(j);//, nextVerb = taggedWords.get(j+1);

        		//4. is there a verb after modal?
        		if(prevVerb.tag().equals("MD") && !isVerbTag(mainVerb.tag())) {
        			errors1c.addError("Incorrect Verb order (no verb after modal). [" + prevVerb + "-" + mainVerb + "]");
        		}

        		if(prevVerb.tag().equals("PRP")) {
        			//5. if there is prp, is it followed by verb?
        			if(!isVerbTag(mainVerb.tag()) && !mainVerb.tag().equalsIgnoreCase("them") 
        					&& !mainVerb.tag().equalsIgnoreCase("us") && !mainVerb.tag().equalsIgnoreCase("him")
        					&& !mainVerb.tag().equalsIgnoreCase("her") && !mainVerb.tag().equalsIgnoreCase(".")) {
        				errors1c.addError("Missing verb after PRP. [" + prevVerb + "]");
        			}
        			//6. if there is prp followed by verb, is it agreeing?
        			else if(!isVerbPRPAgreeing(mainVerb, prevVerb))
        				errors1c.addError("Missing verb after PRP. [" + prevVerb + "-" + mainVerb + "]");
        		}

        		if(isVerbTag(mainVerb.tag())) {

        			//7, 8. does word before/after a gerund agree?
        			if(mainVerb.tag().equals("VBG")) {
        				if(Rules.getBeforeGerundErrorRules().contains(prevVerb.tag())) {
        					errors1c.addError("Incorrect Verb order (word before gerund). [" + prevVerb + "-" + mainVerb + "]");
        				}
        				//cannot have verb after gerund.
        				if( (j+1 < taggedWords.size()) && ( isVerbTag(taggedWords.get(j+1).tag())  ) ) { //|| taggedWords.get(j+1).tag().equals("NN")
        					errors1c.addError("Incorrect Verb order (word after gerund). [" + mainVerb + "-" + taggedWords.get(j+1) + "]");
        				}
        			}

        			//8. does verb verb agree?
        			if(isVerbTag(prevVerb.tag()) && !mainVerb.tag().equals("VBG")) {
        				if(!verbVerbRules.get(prevVerb.tag()).contains(mainVerb.tag())) {
        					errors1c.addError("Incorrect Verb(s) order. [" + prevVerb + "-" + mainVerb + "]");
        				}
        			}

//				//are there 3 verbs in a row?
//				if(isVerbTag(prevVerb.tag()) && isVerbTag(nextVerb.tag()))
//					errors1c.addError("Incorrect Verb(s) order (3 verbs tog). [" + prevVerb + "-" + mainVerb + "-" + nextVerb + "]");
//				
//				HashMap<String, Set<String>> beforeVerbRules = Rules.getBeforeVerbRules();
//				//does prev and main agree?
//				if(beforeVerbRules.containsKey(mainVerb.tag()) && !mainVerb.tag().equals("VBG") && !prevVerb.tag().equals("MD")) {
//					if(!isVerbTag(prevVerb.tag()) && !beforeVerbRules.get(mainVerb.tag()).contains(prevVerb.tag())) {
//						errors1c.addError("Incorrect (word before) Verb order. [" + prevVerb + "-" + mainVerb + "]");
//					}
//				}
//				
//				HashMap<String, Set<String>> afterVerbRules = Rules.getAfterVerbRules();
//				//does main and next agree?
//				if(afterVerbRules.containsKey(mainVerb.tag()) && !mainVerb.tag().equals("VBG") && !mainVerb.tag().equals("MD")) {
//					if(!isVerbTag(nextVerb.tag()) && !afterVerbRules.get(mainVerb.tag()).contains(nextVerb.tag())) {
//						errors1c.addError("Incorrect (word after) Verb order. [" + mainVerb + "-" + nextVerb + "]");
//					}
//				}

        		}
        	}
        }
		//put back all errors
		sentence.getErrors().put("1c", errors1c);
		return sentence.getErrors().get("1c");
	}
	
	//(for verb verb agreement)
	private static boolean isVerbPRPAgreeing(TaggedWord lhs, TaggedWord rhs) {
		//the pronouns he/she, cannot have a vbp
		if(lhs.tag().equals("VBP") &&
				(rhs.word().equalsIgnoreCase("he") || rhs.word().equalsIgnoreCase("she") 
				|| rhs.word().equalsIgnoreCase("it"))) {
				return false;
		}
		if(lhs.tag().equals("VBN") && 
				(rhs.word().equalsIgnoreCase("i") || rhs.word().equalsIgnoreCase("he") 
				|| rhs.word().equalsIgnoreCase("she") || rhs.word().equalsIgnoreCase("it"))) {
				return false;
		}
		if((lhs.tag().equals("VBG") || lhs.tag().equals("VB")) && rhs.tag().equals("PRP")) {
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
								errors1b.addError("Subject-Verb do not agree. [" + depnsubj.dep() + rhs + "-" + depaux.dep() + lhs + "]");
						} else {
							//check for wrong combinations
							//check if rhs is prp -> he/she -> lhs shud be singular
							if(!isPRPAgreeing(taggedWords.get(depaux.dep().index()-1), taggedWords.get(depnsubj.dep().index()-1)))
								errors1b.addError("Subject-Verb do not agree. [" + depnsubj.dep() + rhs + "-" + depaux.dep() + lhs + "]");
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
						errors1b.addError("Subject-Verb do not agree. [" + dep.dep() + rhs + "-" + dep.gov() + lhs + "]");
				} else {
					//check for wrong combinations
					//check if rhs is prp -> he/she -> lhs shud be singular
					if(!isPRPAgreeing(taggedWords.get(dep.gov().index()-1), taggedWords.get(dep.dep().index()-1)))
						errors1b.addError("Subject-Verb do not agree. [" + dep.dep() + rhs + "-" + dep.gov() + lhs + "]");
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
		return sentence.getErrors().get("1b");}
	
	/**
	 * Check verb agreement for personal pronouns 
	 * (for subject verb agreement)
	 * 
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	private static boolean isPRPAgreeing(TaggedWord lhs, TaggedWord rhs) {
		if(lhs.tag().equals("VBZ") && rhs.tag().equals("PRP")) {
			//the pronouns they/we/i/you cannot have a vbz
			if(rhs.word().equalsIgnoreCase("they") || rhs.word().equalsIgnoreCase("we") 
					|| rhs.word().equalsIgnoreCase("i")  || rhs.word().equalsIgnoreCase("you"))
				return false;
		}
		if(lhs.tag().equals("VBP") && rhs.tag().equals("PRP")) {
			//the pronouns he/she, cannot have a vbp
			if(rhs.word().equalsIgnoreCase("he") || rhs.word().equalsIgnoreCase("she") || rhs.word().equalsIgnoreCase("it"))
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
