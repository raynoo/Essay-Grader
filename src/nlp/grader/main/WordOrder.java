package nlp.grader.main;

/**
 * 
 * @author kevin
 *
 */
public class WordOrder {

	private final String[] INTRANSITIVE_VERB = {"appear", "arrive", "come", "cry", "die", "go", "happen", "occur", "rain", "sleep", "stay", "walk","age"}; // cannot be used in a passive sentence // they can exists without a object

	private final String[] TRANSITIVE_VERB   = {"bring","cost","give","lend","offer","pass","play","read","send","sing","teach","write","buy","get","leave","make","owe","pay","promise","refuse","show","take","tell",}; // sentence should have a object

	public WordOrder(String dependency, String sentence)
	{
		//		How can I lemmatize (reduce to a base, dictionary form) words that have been tagged with the POS tagger?
		//
		//				For English (only), you can do this using the included Morphology class. However, unlike for the Stanford parser, there is at present no support for doing this automatically using options of the command-line version of the tagger. You'd have to do it using code you write. 
	}


	//******************************8
	// position of verb
	
	private void basicCheck()
	{
		// verb should not be at starting
	}


	//**************************************************************
	// position of objects

	private void objectRequirement()
	{
		// if it has intransitive verb we can go without a object

		// if it has a transitive verb then we need a object
	}

	
	//************************************************************************
	// position of prepostion

	private boolean isWHSentence(String sentence)
	{
		boolean status = false;


		return status;
	}

	private void isPassive()
	{
		// if the sentence is Passive then preposition can come before object.

		// if it has intransitive verb then the sentence cannot be passive
		
		// so if it has intransitive verb and proposition comes after verb then its an error. 
	}

}



