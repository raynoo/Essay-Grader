package nlp.grader.main;

import nlp.grader.objects.ErrorDetails;
import nlp.grader.objects.Sentence;

public class SemanticTwoA 
{

	
	public void processSecondPart(Sentence sentence)
	{
		ErrorDetails ed = sentence.getErrors().get("2a");

		if(ed == null)
		{
			ed = new ErrorDetails("2a");
			sentence.getErrors().put("2a", ed);
		}	
	
	
	}
	
	/**
	 * Check if the sentence has second person you and your 
	 * should not be there
	 * 
	 */
	private void hasSecondPerson(Sentence sentence , ErrorDetails ed)
	{
		
	}
	
	
}
