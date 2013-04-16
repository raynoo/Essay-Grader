package nlp.grader.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import edu.stanford.nlp.trees.Tree;

public class Essay {

	private List<Sentence> essay;
	private Points essayPoints;

	public Essay(List<String> sentences) {


		this.essay = new ArrayList<Sentence>();
		for(String s : sentences)
		{
			String lastSentence = "";
			
			s = s.replaceAll("\\s+,", ",");
			Sentence sentence = new Sentence(s);

			Queue<Tree> que = new LinkedList<Tree>();
			que.add(sentence.getParseTree());
			Iterator<Tree> subChildren = sentence.getParseTree().iterator();

			while( subChildren!= null && subChildren.hasNext() )
			{
				Tree t = subChildren.next();
				if(t.value().equals("SBAR"))
				{
					que.add(t);
				}
			}

			while(!que.isEmpty())
			{
				String newSentence = "";
				Iterator<Tree> t = que.remove().iterator();
				boolean foundS = false;

				while(t.hasNext())
				{
					Tree temp = t.next();
					if(temp == que.peek())
					{
						break;
					}
					else
					{
						if(temp.value().equals("S") || temp.value().equals("ROOT"))
						{
							foundS = true;
						}
						else if(temp.isLeaf() && foundS)
						{
							if(newSentence.equals(""))
								newSentence +=temp.value();
							else if(temp.value().equals(","))
								newSentence = newSentence + temp.value();
							else
								newSentence = newSentence + " " + temp.value();
						}
					}
				}				

				lastSentence = newSentence;
				
				if(newSentence.length() > 1)
				{
					
					this.essay.add(new Sentence(newSentence));
				}
			}

			if( s.indexOf(lastSentence) + lastSentence.length() +1 < s.length())
				this.essay.add(new Sentence( s.substring( s.indexOf(lastSentence) + lastSentence.length() +1 ) ));
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
