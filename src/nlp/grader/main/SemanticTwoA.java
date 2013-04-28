package nlp.grader.main;

import java.util.ArrayList;
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
//	private static Essay essay;
//	HashMap<Integer, Sentence> sentences = new HashMap<Integer, Sentence>();
	
	
	public static void main(String[] args) {
		sentences = new ArrayList<Sentence>();
		sentences.add(new Sentence("My son is 2 years old."));
		sentences.add(new Sentence("I love him very much."));
		
		List<TaggedWord> antecedents = getAntecedentNoun(new TaggedWord(), 1, 2);
		
		System.out.println(antecedents);
	}
	
	
	public static void processSecondPart(Essay essay)
	{
		if(sentences == null)
			sentences = essay.getSentences();
		
		for(int i = 0; i < sentences.size(); i++) {
			Sentence sentence = sentences.get(i);
			
			ErrorDetails ed = sentence.getErrors().get("2a");
			if(ed == null) {
				ed = new ErrorDetails("2a");
			}
			
			//check for 2nd person pronoun
			if(hasSecondPerson(sentence))
				ed.addError("Second person pronoun present. [sentence " + i + "]");
			
			//check for 3rd person pronoun
			//and check for prev noun (in upto 2 sentences back)
//			ArrayList<TaggedWord> words = sentence.getTaggedWordsAsMap().get("PRP");
			ArrayList<TaggedWord> words = sentence.getTaggedWords();
			
			for(int j = 0; j < words.size(); j++) {
				TaggedWord word = words.get(j);
				
				//if he/she then check antecedent's gender
				if(word.tag().equals("PRP") && (word.word().equalsIgnoreCase("he") 
						|| word.word().equalsIgnoreCase("she"))) {
					
					List<TaggedWord> antecedents = getAntecedentNoun(word, i, j);
					
					if(antecedents.isEmpty()) {
						ed.addError("Missing antecedent for pronoun. [" + word + " in sentence " + i + "]");
						continue;
					}
					
					TaggedWord genderMatch = null, numberMatch = null;
					for(TaggedWord t : antecedents) {
						//check gender
						//word, antecedents
						if(genderMatch == null && checkGender(word.word(), t.word())) {
							genderMatch = t;
						}
						
						//check number
						if(numberMatch == null && checkNumber(word.word(), t.word())) {
							numberMatch = t;
						}
						
					}
					if(genderMatch == null)
						ed.addError("Gender mismatch for pronoun. [" + word + "]");
					if(numberMatch == null)
						ed.addError("Number mismatch for pronoun. [" + word + "]");
				}
			}
			
			
			sentence.getErrors().put("2a", ed);
		}
	}
	
	private static boolean checkGender(String pronounWord, String nounWord) {
		if(Tags.isMalePrp(pronounWord) && (Tags.isMaleWord(nounWord) || Tags.isNeutralGender(nounWord)))
			return true;
		
		if(Tags.isFemalePrp(pronounWord) && (Tags.isFemaleWord(nounWord) || Tags.isNeutralGender(nounWord)))
			return true;
		
		return false;
	}
	
	private static boolean checkNumber(String pronounWord, String nounTag) {
		if((Tags.isFemalePrp(pronounWord) || Tags.isMalePrp(pronounWord)) 
				&& (nounTag.equals("NNS") || nounTag.equals("NN")))
			return true;
		
		if(Tags.isPluralPrp(pronounWord) && (nounTag.equals("NNP")))
			return true;
		
		return false;
	}
	
	private static List<TaggedWord> getAntecedentNoun(TaggedWord pronounWord, int sentenceIndex, int wordIndex) {
		List<TaggedWord> ante = new ArrayList<TaggedWord>();
		
		for(int i = 0; i < 3 && sentenceIndex >= 0; i++) {
			if(sentenceIndex == 0 && i == 0)
				return null;
			
			if(i!=0)
				wordIndex = sentences.get(sentenceIndex).getTaggedWords().size();
			ListIterator<TaggedWord> iter = sentences.get(sentenceIndex).getTaggedWords().listIterator(wordIndex);
			
			while(iter.hasPrevious()) {
				TaggedWord word = iter.previous();
				if(Criteria.isNounTag(word.tag()) && !word.tag().equals("PRP")) {
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
				if(t.value().equalsIgnoreCase("you") || t.value().equalsIgnoreCase("your"))
					return true;
			}
		}
		return false;
	}
	
	private static boolean hasThirdPerson(Sentence sentence, ErrorDetails ed) {
		ArrayList<TaggedWord> words = sentence.getTaggedWordsAsMap().get("PRP");
		
		for(TaggedWord t : words) {
//			if(t.value() == 3rd person)
//				return true;
		}
		
		return false;
	}
}
