package nlp.grader.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nlp.grader.utils.Reader;

public class Rules {
	
	private static List<Rule> verbNounRules = null;
	
	private static HashMap<String, Set<String>> verbVerbRules = null;
	private static HashMap<String, Set<String>> beforeVerbRules = null;
	private static HashMap<String, Set<String>> afterVerbRules = null;
	
	//blacklisted tags that (should not) appear preceding a gerund
	private static String[] beforeGerundRules = { "VB", "VBG", "VBD", "VBN", "TO" };
	private static String[] afterPRPRules = { "VBD", "VBP", "VBZ" }; //vb vbg vbn 
	//I/she/he/it-VBN is wrong
//	private static String[] afterGerundRules = { "PRP", "VB", "WP", "VBG", "MD", "NN", "VBD" };
	
	public static HashMap<String, Set<String>> getBeforeVerbRules() {
		if(beforeVerbRules == null) {
			beforeVerbRules = new HashMap<String, Set<String>>();
			
			for(String s : Reader.readFile("rules/1c_before_verb_rules.txt")) {
				String[] rule = s.split("\\s");
				
				if(rule.length > 1) {
					Set<String> rhs = null;
					
					//index 1 has the main verb tags
					if(beforeVerbRules.containsKey(rule[1])) {
						rhs = beforeVerbRules.get(rule[1]);
					} else {
						rhs = new HashSet<String>();
					}
					rhs.add(rule[0]);
					
					beforeVerbRules.put(rule[1], rhs);
				}
			}
		}
		return beforeVerbRules;
	}
	
	public static HashMap<String, Set<String>> getAfterVerbRules() {
		if(afterVerbRules == null) {
			afterVerbRules = new HashMap<String, Set<String>>();
			
			for(String s : Reader.readFile("rules/1c_after_verb_rules.txt")) {
				String[] rule = s.split("\\s");
				
				if(rule.length > 1) {
					Set<String> rhs = null;
					
					//index 0 has the main verb tags
					if(afterVerbRules.containsKey(rule[0])) {
						rhs = afterVerbRules.get(rule[0]);
					} else {
						rhs = new HashSet<String>();
					}
					rhs.add(rule[1]);
					
					afterVerbRules.put(rule[0], rhs);
				}
			}
		}
		return afterVerbRules;
	}
	
	public static List<Rule> getVerbNounRules() {
		if(verbNounRules == null) {
			verbNounRules = new ArrayList<Rule>();
			
			for(String s : Reader.readFile("rules/1b_verb_noun_agreement_rules.txt")) {
				String[] rule = s.split("\\s");
				verbNounRules.add(new Rule(rule[0], rule[1]));
			}
		}
		return verbNounRules;
	}
	
	public static HashMap<String, Set<String>> getVerbVerbRules() {
		if(verbVerbRules == null) {
			verbVerbRules = new HashMap<String, Set<String>>();
			
			for(String s : Reader.readFile("rules/1c_verb_verb_rules.txt")) {
				String[] rule = s.split("\\s");
				
				if(rule.length > 1) {
					Set<String> rhs = null;
					
					//index 0 has the main verb tags
					if(verbVerbRules.containsKey(rule[0])) {
						rhs = verbVerbRules.get(rule[0]);
					} else {
						rhs = new HashSet<String>();
					}
					rhs.add(rule[1]);
					
					verbVerbRules.put(rule[0], rhs);
				}
			}
		}
		return verbVerbRules;
	}
	
	public static Set<String> getBeforeGerundErrorRules() {
		return new HashSet<String>(Arrays.asList(beforeGerundRules));
	}
}
