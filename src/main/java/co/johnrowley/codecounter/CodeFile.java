/*
 * Some multi line comments
 */

package co.johnrowley.codecounter;

import java.io.*;
import java.util.*;

class CodeFile {
	private int lines = 0;

	public int lines(){
		return lines;
	}

	public CodeFile(String filename){ /* a multiline comment at the end of a function */
		try {
			File file = new File(filename);
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String data = "";
        String line;
        while((line = br.readLine()) != null)
            data += (line + "\n");

  		  // this regex matches all comments, it is used so that comments are replaced with nothing,
  		  // stripping all comments
        data = data.replaceAll("(/\\*([^*]|[\r\n]|(\\*+([^*/]|[\r\n])))*\\*+/)|(//.*)","");

        // this regex matches all empty lines. It is used to remove empty lines
        data = data.replaceAll("^[ \\t]*$\\r?\\n","");

        // this regex matches all newlines
        String[] tmp = data.split("\\r\\n|\\r|\\n");
        ArrayList<String> goodLines = new ArrayList<String>();
        
        for(int i = 0;i < tmp.length; i++){
            String t = tmp[i].trim();
            if(t.length() > 0)
                goodLines.add(line);
        }

        lines = goodLines.size();
      }
		} catch (FileNotFoundException fnfe){
			System.out.println("File Not Found " + filename);
		} catch (IOException ioe){
			System.out.println("IO Error");
		}

	}
}
