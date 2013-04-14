package nlp.grader.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {
	
	public static List<String> readFile(String filename) {
		BufferedReader br = null;
		String line;
		List<String> lines = new ArrayList<String>();
		
		try {
			br = new BufferedReader(new FileReader(filename));
			
			while ((line = br.readLine()) != null &&
					line != "" && line.charAt(0) != '#') {
				lines.add(line);
				System.out.println(line);
			}
			
			br.close();
			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return lines;
	}
}
