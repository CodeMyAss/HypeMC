package me.loogeh.Hype.games;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.util.utilMath;

public class MobArena {
	public static Hype plugin;
	
	public static ArrayList<String> players = new ArrayList<String>();
	public static boolean inGame = false;
	public static boolean inLobby = false;
	public static int round = 0;
	
	public static double[] netherSpawn = {791.5, 24.0, 397.5};
	public static float netherSpawnYaw = -45.0F;
	
	public static double[] endSpawn = {832.5, 24.0, 396.5};
	public static float endSpawnYaw = 46.0F;
	
	public static double[] castleSpawn = {831.7, 24.0, 421.7};
	public static float castleSpawnYaw = 135.2F;
	
	public static double[] woodSpawn = {790.4, 24.0, 422.2};
	public static float woodSpawnYaw = -132.0F;
	
	public static int mobQuantity = round * 4;
	public static Random rand = new Random();
	public static int mobsLeft;
	
	public static EntityType[] spawnable = {EntityType.ZOMBIE, EntityType.CREEPER, EntityType.PIG_ZOMBIE, EntityType.WOLF, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SKELETON};
	public static EntityType[] boss = {EntityType.WITHER, EntityType.BLAZE};
	
	public static void initialize() {
		if(inGame == true) {
			return;
		} else if(inGame == false) {
			if(inLobby == true) {
				return;
			} else if(inLobby == false) {
				inGame = false;
				inLobby = true;
				broadcast(ChatColor.WHITE + "Joining enabled");
				broadcast(ChatColor.WHITE + "Type /ma join");
				broadcast(ChatColor.WHITE + "Price " + ChatColor.DARK_GREEN + "$1000");
			}
		}
	}
	
	public static void join(Player player) {
		
	}
	
	public static void leave(Player player) {
		
	}
	
	
	public static void broadcast(String message) {
		Bukkit.broadcastMessage(ChatColor.BLUE + "Mob Arena - " + message);
	}
	
	public static void messagePlayers(String message) {
	}
	
	public static void start() {
		if(players.size() == 0) {
			broadcast(ChatColor.WHITE + "Start cancelled due to not enough players");
			return;
		}
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				int platform = utilMath.getRandom(1, 4);
				if(platform == 1) {
					
				}
			}
		}, 600L);
	}
	
	public static void setRound(int round) {
		
	}
}
