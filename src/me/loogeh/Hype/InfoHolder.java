package me.loogeh.Hype;

import java.util.HashMap;

import me.loogeh.Hype.Effects.EffectPlayer;
import me.loogeh.Hype.entity.NPCManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InfoHolder {
	public static Hype plugin = Hype.plugin;
	
	public static HashMap<String, HPlayer> hype_players = new HashMap<String, HPlayer>();
	public static HashMap<String, Charge> player_charge = new HashMap<String, Charge>();
	public static HashMap<String, EffectPlayer> effects = new HashMap<String, EffectPlayer>();
	public static HashMap<String, ChatChannel> chats = new HashMap<String, ChatChannel>();
	public static NPCManager npcmanager = new NPCManager(Hype.plugin);
	
	private static int checkerID;
	
	public static void startChecker() {
		checkerID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				for(Player players : Bukkit.getOnlinePlayers()) {
					if(hype_players.containsKey(players.getName())) {
						HPlayer.handleTime(players);
					}
				}
			}
		}, 1200L, 1200L);
	}
	
	public static void stopChecker() {
		Bukkit.getScheduler().cancelTask(checkerID);
	}
}
