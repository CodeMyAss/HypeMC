package me.loogeh.Hype.moderation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.util.utilTime;
import me.loogeh.Hype.util.utilTime.TimeUnit;

public class Ban {
	public static Hype plugin;
	public static HashMap<String, Double> bantimeMap = new HashMap<String, Double>();
	public static HashMap<String, Long> bansysMap = new HashMap<String, Long>();
	public static HashMap<String, Double> banMap = new HashMap<String, Double>();
	public static HashMap<String, String> banreasonMap = new HashMap<String, String>();
	
	public static void load() {
		ResultSet rs = MySQL.doQuery("SELECT player,reason,hours,bantime,systime FROM player_ban");
		try {
			while(rs.next()) {
			banMap.put(rs.getString(1), rs.getDouble(3));
			bantimeMap.put(rs.getString(1), rs.getDouble(4));
			bansysMap.put(rs.getString(1), rs.getLong(5));
			banreasonMap.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Bans - Successfully loaded");
	}
	
	public static void banPlayer(String player, Player banner, double duration, String reason) {
		Player banee = Bukkit.getServer().getPlayer(player);
		if(!Permissions.outranks(banner.getName(), player)) {
			banner.sendMessage(ChatColor.GRAY + "You must outrank" + ChatColor.WHITE + " [" + ChatColor.YELLOW + banee.getName() + ChatColor.WHITE + "]" + ChatColor.GRAY + " to ban them");
			return;
		}
		if(isBanned(banee.getName())) {
			banner.sendMessage(ChatColor.GOLD + "Ban: " + ChatColor.YELLOW + banee.getName() + ChatColor.GRAY + " is already banned for " + ChatColor.GREEN + getRemaining(banee.getName()));
			return;
		}
		MySQL.doUpdate("INSERT INTO `player_ban`(`player`, `banner`, `rank`, `reason`, `hours`, `bantime`, `systime`, `date`) VALUES ('" + banee.getName() + "','" + banner.getName() + "','" + Permissions.getLevel(banner) + "','" + reason + "'," + duration + "," + (duration * 3600000.0) + "," + System.currentTimeMillis() + ",'" + utilTime.timeStr() + "')");
		bantimeMap.put(banee.getName(), duration * 3600000.0);
		banMap.put(banee.getName(), duration);
		bansysMap.put(banee.getName(), System.currentTimeMillis());
		banee.kickPlayer(ChatColor.RED + "Banned " + ChatColor.WHITE + " - " + ChatColor.YELLOW + "for " + ChatColor.GREEN + Ban.getReason(banee.getName() + ChatColor.WHITE + " - " + ChatColor.GREEN + Ban.getRemaining(banee.getName())));
		Bukkit.broadcastMessage(ChatColor.GOLD + banner.getName() + ChatColor.YELLOW + " banned " + ChatColor.GOLD + player + ChatColor.YELLOW + " [" + ChatColor.AQUA + getRemaining(player) + ChatColor.YELLOW + "] for" + ChatColor.AQUA + reason);
//		InfoHolder.hype_players.get(player).addBanCount();
	}
	
	public static void banOfflinePlayer(String player, Player banner, double duration, String reason) {
		if(!Permissions.outranks(banner.getName(), player)) {
			banner.sendMessage(ChatColor.GRAY + "You must outrank" + ChatColor.WHITE + " [" + ChatColor.YELLOW + player + ChatColor.WHITE + "]" + ChatColor.GRAY + " to ban them");
			return;
		}
		if(isBanned(player)) {
			banner.sendMessage(ChatColor.GOLD + "Ban: " + ChatColor.YELLOW + player + ChatColor.GRAY + " is already banned for " + ChatColor.GREEN + getRemaining(player));
			return;
		}
		MySQL.doUpdate("INSERT INTO `player_ban`(`player`, `banner`, `rank`, `reason`, `hours`, `bantime`, `systime`, `date`) VALUES ('" + player + "','" + banner.getName() + "','" + Permissions.getLevel(banner) + "','" + reason + "'," + duration + "," + (duration * 3600000.0) + "," + System.currentTimeMillis() + ",'" + utilTime.timeStr() + "')");
		bantimeMap.put(player, duration * 3600000.0);
		banMap.put(player, duration);
		bansysMap.put(player, System.currentTimeMillis());
		Bukkit.broadcastMessage(ChatColor.GOLD + banner.getName() + ChatColor.YELLOW + " banned " + ChatColor.GOLD + player + ChatColor.YELLOW + " [" + ChatColor.AQUA + getRemaining(player) + ChatColor.YELLOW + "] for" + ChatColor.AQUA + reason);
	}
	
	
	
	public static double getDuration(String player) { return (banMap.get(player)); }
	
	public static String getRemaining(String player) {
		if(!isBanned(player)) {
			return player + "is not banned";
		}
		return utilTime.convertString(bansysMap.get(player) + bantimeMap.get(player).longValue() - System.currentTimeMillis(), TimeUnit.BEST, 1);
	}
	
	public static double getRemInt(String player) {
		if(!isBanned(player)) {
			return 0.0;
		}
		return utilTime.convert(bansysMap.get(player) + bantimeMap.get(player).longValue() - System.currentTimeMillis(), TimeUnit.BEST, 1);
	}
	
	public static boolean isBanned(String player) {
		return banMap.containsKey(player);
	}
	
	public static String getBanner(String player) {
		ResultSet rs = MySQL.doQuery("SELECT banner FROM player_ban WHERE player='" + player + "'");
		try {
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void unban(String player, Player caller) {
		Player banee = Bukkit.getServer().getPlayer(player);
		if(banee != null) {
			if(banMap.containsKey(banee.getName())) {
				banMap.remove(banee.getName());
				bantimeMap.remove(banee.getName());
				bansysMap.remove(banee.getName());
				banreasonMap.remove(banee.getName());
				Bukkit.broadcastMessage(ChatColor.DARK_AQUA + caller.getName() + ChatColor.WHITE + " unbanned " + ChatColor.DARK_AQUA + banee.getName());
				MySQL.doUpdate("DELETE FROM player_ban WHERE player='" + player + "'");
				return;
			}
		}
		if(banMap.containsKey(player)) {
			banMap.remove(player);
			bantimeMap.remove(player);
			bansysMap.remove(player);
			banreasonMap.remove(player);
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + caller.getName() + ChatColor.WHITE + " unbanned " + ChatColor.DARK_AQUA + player);
			MySQL.doUpdate("DELETE FROM player_ban WHERE player='" + player + "'");
			return;
		}
		caller.sendMessage(ChatColor.DARK_AQUA + player + ChatColor.GRAY + " is not banned");
	}
	
	public static void unban(String player) {
		if(banMap.containsKey(player)) {
			banMap.remove(player);
			bantimeMap.remove(player);
			bansysMap.remove(player);
			banreasonMap.remove(player);
			MySQL.doUpdate("DELETE FROM player_ban WHERE player='" + player + "'");
			return;
		}
	}
	
	public static String getReason(String player) {
		return banreasonMap.get(player);
	}
}
