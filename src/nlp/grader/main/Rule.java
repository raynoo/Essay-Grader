package nlp.grader.main;

public class Rule {
	private String lhs, rhs;
	
	public Rule(String lhs, String rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public String lhs() {
		return lhs;
	}

	public String rhs() {
		return rhs;
	}
	
}