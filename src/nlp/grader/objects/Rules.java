package nlp.grader.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nlp.grader.utils.Reader;

public class Rules {
	
	private static List<Rule> verbNounRules = null;
	private static List<Rule> verbVerbRules = null;
	//blacklisted tags that (should not) appear preceding a gerund
	private static String[] gerundRules = { "PRP", "VB", "WP", "VBG", "MD", "NN" };
	
	public static List<Rule> getVerbNounRules() {
		if(verbNounRules == null) {
			verbNounRules = new ArrayList<Rule>();
			
			for(String s : Reader.readFile("rules/verb_noun_agreement_rules.txt")) {
				String[] rule = s.split("\\s");
				verbNounRules.add(new Rule(rule[0], rule[1]));
			}
		}
		return verbNounRules;
	}
	
	public static List<Rule> getVerbVerbRules() {
		if(verbVerbRules == null) {
			verbVerbRules = new ArrayList<Rule>();
			
			for(String s : Reader.readFile("rules/verb_verb_agreement_rules.txt")) {
				String[] rule = s.split("\\s");
				verbVerbRules.add(new Rule(rule[0], rule[1]));
			}
		}
		return verbVerbRules;
	}
	
	public static Set<String> getVGerundErrorRules() {
		return new HashSet<String>(Arrays.asList(gerundRules));
	}
}
