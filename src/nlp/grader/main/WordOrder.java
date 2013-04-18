package nlp.grader.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nlp.grader.objects.ErrorDetails;
import nlp.grader.objects.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * 
 * @author kevin
 *
 */
public class WordOrder {

	private final String[] INTRANSITIVE_VERB = {"appear", "arrive", "come", "cry", "die", "go", "happen", "occur", "rain", "sleep", "stay", "walk","age"}; // cannot be used in a passive sentence // they can exists without a object

	private final String[] TRANSITIVE_VERB   = {"bring","cost","give","lend","offer","pass","play","read","send","sing","teach","write","buy","get","leave","make","owe","pay","promise","refuse","show","take","tell",}; // sentence should have a object

	static HashMap<String, Set<String> > orderPair;
	static
	{
		orderPair = new HashMap<String, Set<String>>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("rules/word_order_rules"));
			String line;
			while((line = br.readLine()) != null)
			{
				String tag1 = line.split("\\s+")[0];
				String tag2 = line.split("\\s+")[1];
				Set<String> toAdd;

				if( (toAdd = orderPair.get(tag1) ) == null )
				{
					toAdd = new HashSet<String>();
					orderPair.put(tag1, toAdd);
				}

				toAdd.add(tag2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void getWordOrderErrors(Sentence sentence)
	{
		ErrorDetails ed = sentence.getErrors().get("1a");

		if(ed == null)
		{
			ed = new ErrorDetails("1a");
			sentence.getErrors().put("1a", ed);
		}		

				basicCheck(sentence,ed);
				twoPronoun(sentence,ed);
				checkConjunction(sentence,ed);
				checkTo(sentence, ed);
				checkwordOrder(sentence, ed);
		//		checkWordOrderRules(sentence, ed);
		


		//System.out.println(ed.toString());
	}

	/** check word order*/

	private static void checkwordOrder(Sentence sentence , ErrorDetails ed)
	{		
		
		TypedDependency[] depArray = sentence.getDependencyTree();
		// dep 
		// gov

		int subjPos = -1;
		int verbPos = -1;
		int objPos = -1;

		for(TypedDependency dep : depArray)
		{
			if( dep.reln().toString().equals("nsubj") )
			{ 				
				subjPos = dep.dep().index();
				verbPos = dep.gov().index();

				if( !sentence.getTaggedWords().get(verbPos-1).tag().contains("VB") )
				{
					verbPos = -1;
				}				
				break;				
			}			
		}

		if(verbPos < 0)
		{
			for(TypedDependency dep : depArray)
			{
				if( dep.reln().toString().contains("cop") )
				{
					objPos = dep.gov().index();
					verbPos = dep.dep().index();
					break;
				}
			}
		}
		else
		{
			for(TypedDependency dep : depArray)
			{
				if( dep.reln().toString().contains("iobj") || dep.reln().toString().contains("dobj") )
				{
					objPos = dep.dep().index();
					break;
				}
			}

			if(objPos < 0)
			{
				for(TypedDependency dep : depArray)
				{
					if( dep.reln().toString().contains("pobj"))
					{
						objPos = dep.dep().index();
						break;
					}
				}
			}
		}


		if(subjPos < 0)
		{
			for(TypedDependency dep : depArray)
			{
				if( dep.reln().toString().contains("nsubjpass"))
				{
					subjPos = dep.dep().index();
				}
				else if( dep.reln().toString().contains("auxpass"))
				{
					verbPos = dep.dep().index();
				}
			}

		}

		if(subjPos < 0)
			ed.addError("Subject not found ");
		else if(verbPos < 0)
			ed.addError("Verb not found");
		else
		{
			if(objPos < 0)
				objPos = sentence.getTaggedWords().size()+1;

			if( !(objPos > verbPos) || !(verbPos > subjPos) )
				ed.addError("SVO mismmatch");			
		}	

	}

	//******************************8
	// position of verb

	private static void basicCheck(Sentence sentence,ErrorDetails ed)
	{
		// verb should not be at starting
		Tree tree =  sentence.getParseTree();
		String label = null;

		for(Tree t : tree)
		{
			label = t.label().value();
			if(!label.equals("S")&& !label.equals("ROOT"))
			{
				break;
			}
		}


		 if(label.equals("ADVP"))
		{
			if(!sentence.toString().contains(","))
			{
				ed.addError("after ADVP there is no comma");
			}
		}
//		else if (!label.equals("NP"))
//		{
//			ed.addError("The starting tag is not correct");
//		}
	}


	//**************************************************************
	// position of objects

	private void objectRequirement()
	{
		// if it has intransitive verb we can go without a object

		// if it has a transitive verb then we need a object or it should have xcomp
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

	private static void twoPronoun(Sentence sentence,ErrorDetails ed)
	{
		String sen = "";
		for(TaggedWord taggedWord : sentence.getTaggedWords())
		{
			sen += taggedWord.tag() + " ";
		}

		Pattern p = Pattern.compile("PRP\\s+[A-Z]*\\s+PRP");
		Pattern p1 = Pattern.compile("PRP\\s+PRP");

		Matcher m = p.matcher(sen);
		Matcher m1 = p1.matcher(sen);

		while(m.find())
		{
			String sub = sen.subSequence(m.start(), m.end()).toString();
			if( !sub.contains(",") && !sub.contains("CC") && !sub.contains("VB"))
			{
				ed.addError("Two pronoun without , or conjunction or verb");
			}
		}

		while(m1.find())
		{
			ed.addError("Two pronoun without , ");
		}
	}

	public int checkCapital()
	{
		// There can be no capital letter between a sentence unless it is a Noun or I or acronoym
		return 0;
	}

	private static void checkTo(Sentence sentence,ErrorDetails ed)
	{
		// after To there can be only base form of the verb

		boolean findTo = false;
		Pattern p = Pattern.compile("^to/TO");

		for(TaggedWord taggedWord : sentence.getTaggedWords() )
		{

			if( p.matcher(taggedWord.toString()).find())
			{

				findTo = true;
			}
			else if(findTo)
			{
				findTo = false;
				if(taggedWord.toString().contains("/VB") && !taggedWord.tag().equals("VB"))
				{

					ed.addError("after \" to \" there should be base form of the verb");
				}
			}


		}
	}

	private static void checkWordOrderRules(Sentence sentence , ErrorDetails ed)
	{
		for(int i = 0 ,n = sentence.getTaggedWords().size(); i < n ; i++)
		{
			if(i+1 < n)
			{
				String tag1 = sentence.getTaggedWords().get(i).tag();
				String tag2 = sentence.getTaggedWords().get(i+1).tag();
				if ( orderPair.containsKey(tag1) )
				{
					Set<String> set = orderPair.get(tag1);
					if(!set.contains(tag2))
						ed.addError(" " + tag1 + " " + tag2 + " is not compatible ");
				}
			}
		}
	}

	private static void checkConjunction(Sentence sentence,ErrorDetails ed)
	{
		String label;


		for(Tree t : sentence.getParseTree())
		{
			label = t.label().value();
			if(label.contains("CC"))
			{
				List<Tree> child = t.siblings(sentence.getParseTree());

				String otherTag = null;
				for(int i = 0 ; i <  child.size(); i++ )
				{
					String tag = child.get(i).label().value();

					if(tag.contains("NN"))
					{
						tag = "NN";
					}

					if(otherTag == null && !tag.contains("CC") && !tag.contains(","))
					{
						otherTag = tag;
					}
					else if(otherTag != null && !tag.contains("CC") && !tag.contains(",") && !tag.contains("."))
					{
						if(!otherTag.equals(tag))
						{
							//							System.out.println("###############");
							//							System.out.println(sentence + "  **" + otherTag + "**" + tag );
							ed.addError("Conjunction should join members of the same type ");
							break;
						}
					}
				}
			}

		}
	}


}



