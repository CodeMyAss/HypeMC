package me.loogeh.Hype.games;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.games.Games.GameStatus;
import me.loogeh.Hype.games.Games.GameType;
import me.loogeh.Hype.util.utilTime.TimeUnit;

public class GameManager {
	public static Hype plugin = Hype.plugin;
	
	private static HashMap<String, Game> games = new HashMap<String, Game>();
	
	public static GameType getGame(Player player) {
		if(player ==  null) return GameType.NONE;
		GameType type = GameType.NONE;
		for(String game : games.keySet()) {
			if(games.get(game).getPlayers().containsKey(player.getName())) type = games.get(game).getType();
		}
		return type;
	}
	
	public static Game getKA() {
		if(!games.containsKey("kit_arena")) {
			games.put("kit_arena", new Game("Kit Arena", GameType.KIT_ARENA, 0, 1000, KitArena.getSpawns(), new Location(Bukkit.getWorld("world"), 23.5, 108.5, 2203.5), GameStatus.ENDED, System.currentTimeMillis(), true));
		}
		return games.get("kit_arena");
	}
	
	public static HashMap<String, Game> getGames() {
		return games;
	}
	
//	public static Game get(Player player) {
//		if(player == null) return;
//		for(String games :  )
//	}
	
	public static boolean isFFA(GameType type) {
		if(type == GameType.ARCTIC_BRAWL || type == GameType.SPLEEF || type == GameType.HOT_POTATO || type == GameType.SPLEGG) return true;
		return false;
	}
	
	public static void beginStarter() {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				for(String keys : games.keySet()) {
					long last = games.get(keys).getLastRun();
					if((System.currentTimeMillis() - last) > TimeUnit.HOURS.toMillis(2)) {
						games.get(keys).initiate();
					}
				}
			}
		}, 1200L, 1200L);
	}
	
}
