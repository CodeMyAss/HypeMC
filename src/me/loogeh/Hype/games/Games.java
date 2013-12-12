package me.loogeh.Hype.games;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.loogeh.Hype.Hype;

public class Games {
	public static Hype plugin;
	
	public static HashMap<String, Game> games = new HashMap<String, Game>();
	
	public static void init() {
		if(!games.containsKey("kit_arena")) games.put("kit_arena", new Game("Kit Arena", GameType.KIT_ARENA, 0, 1000, KitArena.getSpawns(), new Location(Bukkit.getWorld("world"), 23.5, 108.85, 2203.5), GameStatus.ENDED, System.currentTimeMillis(), true));
		if(!games.containsKey("splegg")) games.put("splegg", new Game("Splegg", GameType.SPLEGG, 1, 1000, Splegg.getSpawns(), new Location(Bukkit.getWorld("world"), 21.0D, 48.0D, 2204.0D), GameStatus.ENDED, System.currentTimeMillis(), true));
	}
	
	public static HashMap<String, ItemStack[]> needInv = new HashMap<String, ItemStack[]>();
	public static HashMap<String, ItemStack[]> needArmour = new HashMap<String, ItemStack[]>();
	
	public static Location getSpec() {
		return new Location(Bukkit.getWorld("world"), 65.0D, 65.0D, 2202.0D);
	}
	
	public static Location getArenaSpec() {
		return new Location(Bukkit.getWorld("world"), 65.0D, 119.0D, 2202.0D);
	}
	
	public static void saveInvent(Player player) {
		if(player.getInventory().getArmorContents() != null) {
			needArmour.put(player.getName(), player.getInventory().getArmorContents());
		}
		needArmour.put(player.getName(), player.getInventory().getContents());
	}
	
	public static void retrieveContents(Player player) {
		if(player == null) return;
		if(needArmour.containsKey(player.getName())) player.getInventory().setArmorContents(needArmour.get(player.getName()));
		if(needInv.containsKey(player.getName())) player.getInventory().setContents(needInv.get(player.getName()));
	}
	
	public static boolean canDamage(Player damager, Player damagee) {
		if(GameManager.getGame(damager) == GameType.NONE || GameManager.getGame(damagee) == GameType.NONE) return true;
		if(GameManager.getGame(damager) != GameManager.getGame(damagee)) return true;
		if(GameManager.isFFA(GameManager.getGame(damager)) && GameManager.isFFA(GameManager.getGame(damagee))) return true;
		return false;
		
	}
	
	public static Game get(String name) {
		if(!games.containsKey(name)) return null;
		return games.get(name);
	}
	
	public static Game getCurrentGame(Player player) {
		if(player == null) return null;
		for(String keys : games.keySet()) {
			if(games.get(keys).getPlayers().containsKey(player.getName())) return games.get(keys);
		}
		return null;
	}
	
	public enum GameType {
		NONE,
		KIT_ARENA,
		SPLEEF,
		MOB_ARENA,
		TRESPASSERS,
		PROP_HUNT,
		GHOSTBUSTERS,
		ARCTIC_BRAWL,
		SPLEGG,
		HOT_POTATO,
		FOUR_CORNERS,
		CTF;
	}
	
	public enum GameStatus {
		LOBBY,
		PLAYING,
		STOPPED,
		ENDED;
	}
	
	public enum StopReason {
		INSUFFICIENT_PLAYERS,
		GAME_FINISH;
		
	}
}
