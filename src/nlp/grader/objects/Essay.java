package nlp.grader.objects;

import java.util.ArrayList;
import java.util.List;

public class Essay {
	
	private List<Sentence> essay;
	private Points essayPoints;
	
	public Essay(List<String> sentences) {
		this.essay = new ArrayList<Sentence>();
		for(String s : sentences) {
			this.essay.add(new Sentence(s));
		}
		
		this.essayPoints = new Points();
	}
	
	public List<Sentence> getSentences() {
		return this.essay;
	}
	
	public Points getPoints() {
		return this.essayPoints;
	}
	
}
