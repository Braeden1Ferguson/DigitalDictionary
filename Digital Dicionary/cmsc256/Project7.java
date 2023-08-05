package cmsc256;
/**
 *   CMSC 256
 *   Computer Science Department
 *   College of Engineering
 *   Virginia Commonwealth University
 */
// Import Bridges and relevant data source
import bridges.base.BSTElement;
import bridges.connect.Bridges;
import bridges.data_src_dependent.*;

import java.util.List;
import java.util.Map;


public class Project7 {

	// Splits string into an array of words
	// Tutorial on regular expressions: 
	// https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
	public static String[] splitText(String text) {      
		// remove '.' from all sentences by replacing with empty space
		text = text.replaceAll("[.]", "");  

		// remove non-printing white-space beginning and end of text
		text = text.trim();

		// separate each word based on spaces and add to word array
		String[] words = text.split("\\s+");

		// remove special characters from before/after words in the array
		for (int i = 0; i < words.length; i++) {        
			// \W+ is to find any non-word character 
			words[i] = words[i].replaceAll("\\W+$", "");  	// $ looks at end of the word
			words[i] = words[i].replaceAll("^\\W+", "");	// ^ looks at the beginning
			words[i] = words[i].trim();
		}

		return words;
	}


	public static void main(String[] args){

		// Initialize a Bridges connection with your credentials
		Bridges bridges = new Bridges(7, "fergusonba", "182845337641");

		// Set an assignment title
		bridges.setTitle("Project7");

		try {
			List<Shakespeare> shakespeare_list = bridges.getDataSource().getShakespeareData("poems");
			// Look at the first literary work, a poem
			Shakespeare po1 = shakespeare_list.get(1);

			// Display text as written
			System.out.println(po1.getText());

			// Separate into individual words
			String[] wordlist = splitText(po1.getText());

			// Display word array, one word per line
			for (int i = 0; i < wordlist.length; ++i) {
				System.out.println(wordlist[i]);
			}


			StandardDictionary<String, Integer> my_dictionary = new StandardDictionary<String, Integer>();
			MyHashTable<String, Integer> my_map = new MyHashTable<String, Integer>();
			MySearchTree<String, Integer> my_tree = new MySearchTree<String, Integer>();



			//TODO: 2.Use StandardDictionary to compute the number of occurrences of each word.
			//populating each and trying each of the different sorting methods
			for (String word : wordlist) {
				word = word.toUpperCase();
				Integer entry = my_dictionary.get(word);
				if (entry == null) {
					my_dictionary.set(word, 1);
				} else {
					entry += 1;
					my_dictionary.set(word, entry);
				}
			}

			System.out.print("");
			for (String word : wordlist) {
				word = word.toUpperCase();
				Integer entry = my_tree.get(word);
				if (entry == null) {
					my_tree.set(word, 1);
				} else {
					entry += 1;
					my_tree.set(word, entry);
				}
			}

			for (String word : wordlist) {
				word = word.toUpperCase();
				Integer entry = my_map.get(word);
				if (entry == null) {
					my_map.set(word, 1);
				} else {
					entry += 1;
					my_map.set(word, entry);
				}
			}


			for (Map.Entry<String, Integer> e : my_dictionary) {
				System.out.println(e.getKey() + " " + e.getValue());
			}
			my_tree.set("purple", 1);
			//TODO: uncomment after completing step 2
					my_map.visualize(bridges);
					my_tree.visualize(bridges);

		}
		catch(Exception ex) {
			System.out.println("Unable to access BRIDGES");
		}

	}

}
