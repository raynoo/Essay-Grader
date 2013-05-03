package nlp.grader.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scorer {
	
	static HashMap<Integer, ArrayList<Float>> trueScores = new HashMap<Integer, ArrayList<Float>>();
	static HashMap<Integer, ArrayList<Float>> ourScores = new HashMap<Integer, ArrayList<Float>>();
	
	public static void main(String[] args) {
		
		loadScores("src/nlp/grader/utils/true_scores.txt", trueScores);
		loadScores("src/nlp/grader/utils/our_scores.txt", ourScores);
		
		System.out.println(getScore());
	}
	
	static float getScore() {
		
		float avg1a=0, avg1b=0, avg1c=0, avg1d=0, avg2a=0, avg2b=0, avg3=0;
		int numessays = trueScores.size();
		
		for(Integer i : trueScores.keySet()) {
			avg1a += Math.abs(trueScores.get(i).get(0) - ourScores.get(i).get(0));
			avg1b += Math.abs(trueScores.get(i).get(1) - ourScores.get(i).get(1));
			avg1c += Math.abs(trueScores.get(i).get(2) - ourScores.get(i).get(2));
			avg1d += Math.abs(trueScores.get(i).get(3) - ourScores.get(i).get(3));
			
			avg2a += Math.abs(trueScores.get(i).get(4) - ourScores.get(i).get(4));
			avg2b += Math.abs(trueScores.get(i).get(5) - ourScores.get(i).get(5));
			
			avg3 += Math.abs(trueScores.get(i).get(6) - ourScores.get(i).get(6));
		}
		
		return (avg1a + avg1b + avg1c + (2*avg1d) + avg2a + (3*avg2b) + avg3)/(10*numessays);
	}
	
	static void loadScores(String file, HashMap<Integer, ArrayList<Float>> scoremap) {
		List<String> scores = Reader.readFile(file);
		
		for(String s:scores) {
			String[] vals = s.split("\\s");
			
			ArrayList<Float> scorelist = new ArrayList<Float>();
			for(int i = 1; i < vals.length; i++) {
				scorelist.add(Float.parseFloat(vals[i]));
			}
			scoremap.put(Integer.parseInt(vals[0]), scorelist);
		}
		
	}
}