package nlp.grader.objects;

import java.util.ArrayList;
import java.util.List;

import nlp.grader.utils.Reader;

public class Rules {
	private static List<Rule> verbNounRules = null;
	
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
}
