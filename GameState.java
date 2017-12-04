
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GameState {
	private Map<String, GamePlayer> players = new HashMap<>();
	private List<String> playerList = new ArrayList<>();
	private List<String> cache = new ArrayList<>();
	private Map<String, Integer> scoreList = new HashMap<>();
	private Random randomizer = new Random();

	public GameState(){}
	
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
		String return_val ="";
		for(Iterator<?> ite = scoreList.keySet().iterator();ite.hasNext();){
			String name = (Strng)ite.next();
			return_val+= name+" ";
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

		String playerName = randomizePlayer();

		while(cache.contains(playerName)){
			if(cacheSize == numOfPlayers){
				cache.clear();
			}
			playerName = randomizePlayer();
		}
		cache.add(playerName);

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

}
