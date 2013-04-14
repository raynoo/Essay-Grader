package nlp.grader.objects;

import java.util.ArrayList;
import java.util.List;

public class ErrorDetails
{
	String typeOfError;
	int errorCount = 0;
	List<String> errorMessage = new ArrayList<String>();

	public ErrorDetails(String typeOfError)
	{
		this.typeOfError = typeOfError;
	}


	public void addError(String message)
	{
		if(message != null)
			errorMessage.add(message); // just send null if there is nothing particular to say about the error
		
		errorCount++;
	}

	public int getErrorCount()
	{
		return errorCount;
	}

	@Override
	public String toString() 
	{
		String result = "Total number of error of type " + typeOfError + " = " + errorCount; 
		return result;
	}

}
