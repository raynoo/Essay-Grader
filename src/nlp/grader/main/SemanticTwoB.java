package nlp.grader.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;

import nlp.grader.objects.ErrorDetails;
import nlp.grader.objects.Essay;
import nlp.grader.objects.Sentence;
import nlp.grader.objects.Tags;

public class SemanticTwoB {

	//manager
	//boss
	//owner
	//employee


	private static HashSet<String> relaventWords = new HashSet<String>();

	static{

		String word;
		try{
			BufferedReader br = new BufferedReader(new FileReader("rules/relevent_words.txt"));
			while( (word = br.readLine()) != null){
				word = word.trim();
				if( word.length()  > 0 && !word.contains("#"))
					relaventWords.add(word);
			}
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	public static void processSecondPart(Essay essay)
	{
		int foundFamily = 0;  // sub 3 if 0, 2 if 1 
		int foundFirstPrp = 0; // sub 1
		int totalScore = 5;

		for(Sentence sentence : essay.getSentences())
		{
			for(TaggedWord taggedWord : sentence.getTaggedWords())
			{
				String word = Morphology.lemmaStatic(taggedWord.word(), taggedWord.tag(), true);
				//System.out.println(word);
				if( relaventWords.contains(word ) )
				{
//					System.out.println(word);
					foundFamily++;
				}
				else if( Tags.isPersonalPrp(word))
				{
					foundFirstPrp++;
				}
			}
		}
		
		if(foundFamily == 2)
			totalScore-=1;
		else if(foundFamily == 1)
			totalScore-=2;
		else if(foundFamily == 0)
			totalScore-=5;
		
		if(foundFirstPrp >=2 && foundFamily == 0)
		{			
			totalScore+=3;
		}
		else if(foundFirstPrp ==1 && foundFamily == 0)
		{			
			totalScore+=1;
		}		
		essay.setTwoBScore(totalScore);
	}



}
