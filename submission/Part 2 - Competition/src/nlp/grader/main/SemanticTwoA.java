package nlp.grader.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import edu.stanford.nlp.ling.TaggedWord;

import nlp.grader.objects.ErrorDetails;
import nlp.grader.objects.Essay;
import nlp.grader.objects.Sentence;
import nlp.grader.objects.Tags;

public class SemanticTwoA
{
	private static List<Sentence> sentences = null;
	private static String[] thirdSingular = {"he", "she", "him", "her", "his", "her", "hers"};
	private static String[] thirdPlural = {"they", "them", "their", "theirs", "those"};//we, us?
	private static HashSet<String> thirdSingularSet = null;
	private static HashSet<String> thirdPluralSet = null;
	
	private static boolean isThirdSingular(String pronoun) {
		if(thirdSingularSet == null) {
			thirdSingularSet = new HashSet<String>(Arrays.asList(thirdSingular));
		}
		return thirdSingularSet.contains(pronoun);
	}
	
	private static boolean isThirdPlural(String pronoun) {
		if(thirdPluralSet == null) {
			thirdPluralSet = new HashSet<String>(Arrays.asList(thirdPlural));
		}
		return thirdPluralSet.contains(pronoun);
	}
	
	public static void processSecondPart(Essay essay) {
		sentences = essay.getOriginalSentence();
		int errorCount = 0;

		for(int i = 0; i < sentences.size(); i++) {
			Sentence sentence = sentences.get(i);

			ErrorDetails ed = sentence.getErrors().get("2a");
			if(ed == null)
				ed = new ErrorDetails("2a");

			//check for 2nd person pronoun
			if(hasSecondPerson(sentence))
				ed.addError("Second person pronoun present. [sentence " + i + "]");

			//check for 3rd person pronoun
			//and check for prev noun (in upto 2 sentences back)
			ArrayList<TaggedWord> words = sentence.getTaggedWords();

			for(int j = 0; j < words.size(); j++) {
				TaggedWord word = words.get(j);

				if(word.tag().equals("PRP") || word.tag().equals("PRP$")) {

					//if he/she then check antecedent's gender
					if(isThirdSingular(word.word())) {
						List<TaggedWord> antecedents = getAntecedentNouns(word, i, j);

						if(antecedents == null || antecedents.isEmpty()) {
							ed.addError("Missing antecedent for pronoun. [" + word + " in sentence " + i + "]");
							continue;
						}

						TaggedWord genderMatch = null;
						for(TaggedWord t : antecedents) {
							//check gender
							if(genderMatch == null && checkGender(word.word(), t.word())) {
								genderMatch = t;
								break;
							}
						}
						if(genderMatch == null)
							ed.addError("No antecedent / Gender-mismatch for pronoun. [" + word + "]");

					}
					//if they/them/their check for plural noun
					else if(isThirdPlural(word.word())) {
//						TaggedWord numberMatch = null;
//						//check number
//						if(numberMatch == null && checkNumber(word.word(), t.word())) {
//							numberMatch = t;
//						}
//						if(numberMatch == null)
//							ed.addError("Number mismatch for pronoun. [" + word + "]");
					}

				}
				
			}
			errorCount += ed.getErrorCount();
			sentence.getErrors().put("2a", ed);
		}
		//adding number of sbars to errors
		errorCount += WordOrder.countSbar(essay);
		essay.setTwoAErrors(errorCount);
		
	}
	
	private static boolean checkGender(String pronounWord, String nounWord) {
		if(Tags.isMalePrp(pronounWord) && (Tags.isMaleWord(nounWord) || Tags.isNeutralGender(nounWord))) {
//			System.out.println("Matched: " + pronounWord + " - " + nounWord);
			return true;
		}
		
		if(Tags.isFemalePrp(pronounWord) && (Tags.isFemaleWord(nounWord) || Tags.isNeutralGender(nounWord))) {
//			System.out.println("Matched: " + pronounWord + " - " + nounWord);
			return true;
		}
//		System.out.println("Not Matched: " + pronounWord + " - " + nounWord);
		return false;
	}
	
	private static boolean checkNumber(String pronounWord, String nounTag) {
		if((Tags.isFemalePrp(pronounWord) || Tags.isMalePrp(pronounWord)) 
				&& (nounTag.equals("NNS") || nounTag.equals("NN")))
			return true;
		
		if(Tags.isPluralPrp(pronounWord) && (nounTag.equals("NNP") || nounTag.equals("NNPS")))
			return true;
		
		return false;
	}
	
	private static List<TaggedWord> getAntecedentNouns(TaggedWord pronounWord, int sentenceIndex, int wordIndex) {
		List<TaggedWord> ante = new ArrayList<TaggedWord>();
		
		for(int i = 0; i < 3 && sentenceIndex >= 0; i++) {
			if(sentenceIndex == 0 && i == 0)
				return null;
			
			if(i!=0)
				wordIndex = sentences.get(sentenceIndex).getTaggedWords().size();
			ListIterator<TaggedWord> iter = sentences.get(sentenceIndex).getTaggedWords().listIterator(wordIndex);
			
			while(iter.hasPrevious()) {
				TaggedWord word = iter.previous();
				
				if(Tags.isNounTag(word.tag()) && !word.tag().equals("PRP")) {
					ante.add(word);
				}
			}
			sentenceIndex -= 1;
		}
		return ante;
	}
	
	/**
	 * Check if the sentence has second person you and your 
	 * should not be there
	 */
	private static boolean hasSecondPerson(Sentence sentence) {
		ArrayList<TaggedWord> words = sentence.getTaggedWordsAsMap().get("PRP");
		
		if(words != null) {
			for(TaggedWord t : words) {
				if(t.value().contains("you"))
					return true;
			}
		}
		return false;
	}
	
}
