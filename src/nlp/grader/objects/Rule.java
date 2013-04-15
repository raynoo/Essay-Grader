package nlp.grader.objects;

/**
 * Represents tag rules (to be checked) stored in files. 
 * @author renus
 *
 */
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
	
	@Override
	public String toString() {
		return this.lhs + " - " + this.rhs;
	}
}