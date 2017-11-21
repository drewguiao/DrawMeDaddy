
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameState {
	private Map<String, GamePlayer> players = new HashMap<>();
	
	public GameState(){}
	
	public void update(String name, GamePlayer player){
		players.put(name, player);
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
	
	public Map<String, GamePlayer> getPlayers(){
		return players;
	}
}
