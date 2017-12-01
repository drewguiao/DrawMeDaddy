import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordFetcher {
	private List<String> bagOfWords = new ArrayList<>();
	private List<String> cache = new ArrayList<>();
	private Random randomizer = new Random();
	
	public WordFetcher(){
		try(BufferedReader breader = new BufferedReader(new FileReader("BagOfWords.txt"))){
			while(breader.ready()){
				String word = breader.readLine();
				bagOfWords.add(word);
			}
		}catch(IOException ioe){
			System.out.println("Error reading file!");
		}
	}
	
	public String fetch(){
		int bagSize = bagOfWords.size();
		int cacheSize = cache.size();

		String word = getRandomWord();
		
		while(cache.contains(word)){
			if(cacheSize == bagSize){
				cache.clear();
			}
			word = getRandomWord();
		}
		
		cache.add(word);
		
		return word;
	}

	public String getRandomWord(){
		int bagSize = bagOfWords.size();
		int index = randomizer.nextInt(bagSize);
		String word = bagOfWords.get(index);

		return word;
	}
}