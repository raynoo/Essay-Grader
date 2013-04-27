package nlp.grader.main;

import nlp.grader.objects.ErrorDetails;
import nlp.grader.objects.Sentence;

public class SemanticTwoB {

	
	public void processSecondPart(Sentence sentence)
	{
		ErrorDetails ed = sentence.getErrors().get("2b");

		if(ed == null)
		{
			ed = new ErrorDetails("2b");
			sentence.getErrors().put("2b", ed);
		}	
	}
	
	/**
	 * ratio of relavent noun phrases to total number of noun phrases
	 * have to store in some way
	 * 
	 * there should be some proper 
	 */
	public void countRelaventNoundPhrase()
	{
		
	}
	
}
