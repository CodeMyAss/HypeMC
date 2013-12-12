package me.loogeh.Hype.games;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.loogeh.Hype.HPlayer;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.M;
import me.loogeh.Hype.economy.Money;
import me.loogeh.Hype.games.Games.GameStatus;
import me.loogeh.Hype.games.Games.StopReason;
import me.loogeh.Hype.util.Utilities;
import me.loogeh.Hype.util.utilServer;
import me.loogeh.Hype.util.utilTime;
import me.loogeh.Hype.util.utilTime.TimeUnit;

public class Splegg {
	public static Hype plugin = Hype.plugin;
	
	public static ArrayList<String> no_fall = new ArrayList<String>();
	
	public static void generate() {
		utilServer.loadSchematic("splegg_islands", Bukkit.getWorld("world"), 56, 48, 2238);
	}
	
	public static List<Location> getSpawns() {
		List<Location> list = new ArrayList<Location>();
		list.add(new Location(Bukkit.getWorld("world"), 38.5D, 85.0D, 2221.5, 135.0F, 0.0F));
		list.add(new Location(Bukkit.getWorld("world"), 6.5D, 85.0D, 2220.5D, -135.0F, 0.0F));
		list.add(new Location(Bukkit.getWorld("world"), 6.5D, 85.0D, 2187.5, -45.0F, 0.0F));
		list.add(new Location(Bukkit.getWorld("world"), 38.5, 85.0D, 2190.5D, 45.0F, 0.0F));
		return list;
	}
	
	public static void addInventory(Player player) {
		HPlayer.get(player.getName()).storeInventory("splegg", player.getInventory().getContents());
		HPlayer.get(player.getName()).storeInventory("splegg_armour", player.getInventory().getArmorContents());
		player.getInventory().clear();
		player.getInventory().setItem(0, new ItemStack(Material.IRON_SPADE));
	}
	
	public static void retrieveInventory(Player player) {
		if(player == null) return;
		if(HPlayer.get(player.getName()) == null) return;
		if(HPlayer.get(player.getName()).getInventory("splegg") != null) player.getInventory().setContents(HPlayer.get(player.getName()).getInventory("splegg"));
		if(HPlayer.get(player.getName()).getInventory("splegg_armour") != null) player.getInventory().setContents(HPlayer.get(player.getName()).getInventory("splegg_armour"));
	}
	
	public static void die(Player player) {
		get().getPlayers().remove(player.getName());
		Utilities.skipRespawn(player);
		player.teleport(Games.getSpec());
		message(ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " died");
		if(get().getPlayers().size() < 2) {
			for(String players : get().getPlayers().keySet()) {
				Player target = Bukkit.getPlayer(players);
				if(target == null) return;
				target.teleport(Games.getSpec());
				Bukkit.broadcastMessage(ChatColor.BLUE + "Game - " + ChatColor.YELLOW + players + ChatColor.WHITE + " won " + ChatColor.LIGHT_PURPLE + "Splegg");
				Money.addMoney(target, 2000);
			}
		}
	}
	
	public static Game get() {
		return Games.get("splegg");
	}
	
	public static void start() {
		int spawn = 0;
		for(String players : get().getPlayers().keySet()) {
			Player player = Bukkit.getPlayer(players);
			if(player != null) {
				if(get().getPlayers().size() < 2) {
					end(StopReason.INSUFFICIENT_PLAYERS);
					return;
				}
				player.setGameMode(GameMode.SURVIVAL);
				player.teleport(getSpawns().get(spawn));
				if(spawn >= 3) spawn = 0;
				else spawn++;
			} else {
				get().getPlayers().remove(players);
			}
		}
	}
	
	public static void end(StopReason reason) {
		if(reason == StopReason.INSUFFICIENT_PLAYERS) {
			for(String players : get().getPlayers().keySet()) {
				Player player = Bukkit.getPlayer(players);
				if(player != null) {
					player.teleport(Games.getSpec());
					player.getInventory().clear();
					player.getInventory().setContents(HPlayer.get(player.getName()).getInventory("splegg"));
					player.getInventory().setArmorContents(HPlayer.get(player.getName()).getInventory("splegg_armour"));
					M.v("Game", ChatColor.YELLOW + "Splegg " + ChatColor.WHITE + "cancelled, less than 2 players");
				}
			}
			get().getPlayers().clear();
		}
		if(reason == StopReason.GAME_FINISH) {
			
		}
	}
	
	public static void join(Player player) {
		if(player == null) return;
		if(get().getPlayers().containsKey(player.getName())) {
			M.v(player, "Splegg", ChatColor.WHITE + "You are already playing " + ChatColor.YELLOW + "Splegg");
			return;
		}
		if(get().getStatus() == GameStatus.ENDED || get().getStatus() == GameStatus.STOPPED) {
			if(getRemaining() < 0.0) {
				get().getPlayers().put(player.getName(), 0);
				player.teleport(get().getLobby());
				message(ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " joined");
				addInventory(player);
				if(get().getPlayers().size() == 1) {
					startLobby();
				}
				return;
			}
			M.v(player, "Splegg", ChatColor.WHITE + "Starting in " + getRemainingStr());
			return;
		}
		if(get().getStatus() == GameStatus.PLAYING) {
			M.v(player, "Splegg", ChatColor.YELLOW + "Splegg " + ChatColor.WHITE + "is already in progress");
			return;
		}
		get().getPlayers().put(player.getName(), 0);
		player.teleport(get().getLobby());
		message(ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " joined");
		if(get().getPlayers().size() == 1) startLobby();
	}
	
	public static void startLobby() {
		generate();
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				start();
			}
		}, 1200L);
	}
	
	public static void leave(Player player) {
		if(player == null) return;
		if(!get().getPlayers().containsKey(player)) return;
		player.teleport(Games.getSpec());
		player.getInventory().setContents(HPlayer.get(player.getName()).getInventory("splegg"));
		player.getInventory().setArmorContents(HPlayer.get(player.getName()).getInventory("splegg_armour"));
		message(ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " left the game");
	}
	
	public static void message(String message) {
		for(String players : get().getPlayers().keySet()) {
			Player player = Bukkit.getPlayer(players);
			if(player != null) {
				player.sendMessage(ChatColor.BLUE + "Splegg - " + message);
			}
		}
	}
	
	public static double getRemaining() {
//		return utilTime.convert((System.currentTimeMillis() - get().getLastRun()), TimeUnit.HOURS, 1);
		return -0.1;
	}
	
	public static String getRemainingStr() {
		return utilTime.convertString((System.currentTimeMillis() - get().getLastRun() + 7200000), TimeUnit.BEST, 1);
	}
}
