
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;



public class GameState {
	private Map<String, GamePlayer> players = new HashMap<>();
	private List<String> playerList = new ArrayList<>();
	private List<String> cache = new ArrayList<>();
	private Map<String, Integer> scoreList = new HashMap<>();
	private Random randomizer = new Random();

	public GameState(){}

	public static <K, V extends Comparable<? super V>> Map<K, V> 
        sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
	
	public void update(String name, GamePlayer player){
		players.put(name, player);
		playerList.add(name);
		scoreList.put(name, 0);
	}
	
	public String toString(){
		String retVal="";
		for(Iterator<?> ite = players.keySet().iterator();ite.hasNext();){
			String name = (String)ite.next();
			GamePlayer player = players.get(name);
			retVal+= player.toString()+":";
		}
		return retVal;
	}
	
	public String getScoreList(){
		Map<String,Integer> defensiveCopy = sortByValue(scoreList);
		
		String return_val ="SCORELIST ";
		for(Iterator<?> ite = defensiveCopy.keySet().iterator();ite.hasNext();){
			String name = (String)ite.next();
			int score = defensiveCopy.get(name);
			return_val+= name+" "+score+":SCORELIST ";
		}

		return return_val;
	}

	public Map<String, GamePlayer> getPlayers(){
		return players;
	}

	public List<String> getPlayerList(){
		return this.playerList;
	}

	public String getRandomPlayer(){
		int numOfPlayers = playerList.size();
		int cacheSize = cache.size();

		System.out.println(cacheSize+"cache "+numOfPlayers+" players");
		System.out.println(playerList);
		String playerName = randomizePlayer();

		while(cache.contains(playerName)){
			if(cacheSize == numOfPlayers){
				cache.clear();
			}
			playerName = randomizePlayer();
		}
		cache.add(playerName);
		System.out.println(playerName);
		return playerName;
	}

	private String randomizePlayer(){
		int numOfPlayers = playerList.size();
		int index = randomizer.nextInt(numOfPlayers);
		String player = playerList.get(index);

		return player;
	}

	public void addScore(String playerName, int score){
		scoreList.put(playerName,scoreList.get(playerName)+score);
		System.out.println(scoreList);
	}

	public int getNumOfPlayers(){
		return this.scoreList.size();
	}
}
