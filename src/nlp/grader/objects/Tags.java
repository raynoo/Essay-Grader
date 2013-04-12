package nlp.grader.objects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * Reads tags from file and saves them in HashSet
 * @author renus
 *
 */
public class Tags {

	private static HashSet<String> verbTags = null;
	private static HashSet<String> nounTags = null;
	
	public static HashSet<String> getVerbTags() {
		if(verbTags == null) {
			verbTags = new HashSet<String>();
			
			BufferedReader br = null;
			String line;
			
			try {
				br = new BufferedReader(new FileReader("rules/verb_tags.txt"));
				
				while ((line = br.readLine()) != null) {
					verbTags.add(line);
				}
				
				br.close();
				
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return verbTags;
	}
	
	public static HashSet<String> getNounTags() {
		if(nounTags == null) {
			nounTags = new HashSet<String>();
			
			BufferedReader br = null;
			String line;
			
			try {
				br = new BufferedReader(new FileReader("rules/noun_tags.txt"));
				
				while ((line = br.readLine()) != null) {
					nounTags.add(line);
				}
				
				br.close();
				
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return nounTags;
	}
	
}
