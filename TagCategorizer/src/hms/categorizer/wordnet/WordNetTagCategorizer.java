package hms.categorizer.wordnet;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


public class WordNetTagCategorizer {

	
	
	public static Map<Integer, String> search(String word){
		
		Map<Integer, String> categories  = new HashMap<Integer, String>();
		
		
		word = word.replace(" ", "+");
		word = word.replace("_", "+");
		
		//http://wordnetweb.princeton.edu/perl/webwn?c=4&s=pen
		
		URLConnection uc = null;
		try {
			uc = new URL("http://wordnetweb.princeton.edu/perl/webwn?c=4&sub=Change&o2=&o0=&o8=1&o1=&o7=&o5=&o9=&o6=&o3=&o4=&i=-1&h=000000000000000&s="+word).openConnection();

			System.out.println("[WordNet CALL]: " + uc);
			
			DataInputStream dis = new DataInputStream(uc.getInputStream());
			String nextline = "";
			
			int index = 0 ;
			while ((nextline = dis.readLine()) != null) {
			

				if(nextline.contains("noun.")){
					String category = nextline.substring(nextline.indexOf("noun.")+"noun.".length(),nextline.indexOf("&gt"));
					
					categories.put(index++, category);
				}

			}
			dis.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return categories;
	}
	
	
	public static void main(String[] args) {
		Map<Integer, String> categories = search("canon");
		
		System.out.println(categories);
	}
}
