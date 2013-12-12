package me.loogeh.Hype.moderation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilTime;
import me.loogeh.Hype.util.utilTime.TimeUnit;

public class Mute {
	public static Hype plugin;
	public static HashMap<String, Double> mutetimeMap = new HashMap<String, Double>();
	public static HashMap<String, Long> mutesysMap = new HashMap<String, Long>();
	public static HashMap<String, Double> muteMap = new HashMap<String, Double>();
	public static HashMap<String, String> mutereasonMap = new HashMap<String, String>();

	public static void load() {
		ResultSet rs = MySQL.doQuery("SELECT player,reason,hours,mutetime,systime FROM player_mute");
		try {
			while(rs.next()) {
			muteMap.put(rs.getString(1), rs.getDouble(3));
			mutetimeMap.put(rs.getString(1), rs.getDouble(4));
			mutesysMap.put(rs.getString(1), rs.getLong(5));
			mutereasonMap.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Mutes - Successfully loaded");
		
	}
	
	public static void mute(String player, Player muter, double duration, String reason) {
		Player mutee = Bukkit.getServer().getPlayer(player);
		if(player == null) {
			muter.sendMessage(ChatColor.GOLD + "Mute: " + ChatColor.GRAY + "That player " + ChatColor.WHITE + "[" + ChatColor.YELLOW + player + ChatColor.WHITE + "]" + ChatColor.GRAY + " is offline");
			return;
		}
		if(isMuted(mutee)) {
			muter.sendMessage(ChatColor.GOLD + "Mute: " + ChatColor.GRAY + "The player " + ChatColor.WHITE + "[" + ChatColor.YELLOW + mutee.getName() + ChatColor.WHITE + "]" + ChatColor.GRAY + " is already muted for " + getRemaining(player));
			return;
		}
		if(!Permissions.isStaff(muter)) {
			utilPlayer.permMsg(muter);
			return;
		}
		if(!Permissions.outranks(muter.getName(), mutee.getName())) {
			muter.sendMessage(ChatColor.GRAY + "You must outrank" + ChatColor.WHITE + " [" + ChatColor.YELLOW + mutee.getName() + ChatColor.WHITE + "]" + ChatColor.GRAY + " to mute them");
			return;
		}

		MySQL.doUpdate("INSERT INTO `player_mute`(`player`, `muter`, `rank`, `reason`, `hours`, `mutetime`, `systime`, `date`) VALUES ('" + mutee.getName() + "','" + muter.getName() + "','" + Permissions.getLevel(muter) + "','" + reason + "'," + duration + "," + (duration * 3600000.0) + "," + System.currentTimeMillis() + ",'" + utilTime.timeStr() + "')");
		mutetimeMap.put(mutee.getName(), duration * 3600000.0);
		muteMap.put(mutee.getName(), duration);
		mutesysMap.put(mutee.getName(), System.currentTimeMillis());
		mutereasonMap.put(mutee.getName(), reason);
		Bukkit.broadcastMessage(ChatColor.DARK_AQUA + muter.getName() + ChatColor.YELLOW + " muted " + ChatColor.DARK_AQUA + mutee.getName() + ChatColor.YELLOW + " [" + ChatColor.AQUA + getRemaining(player) + ChatColor.YELLOW + "] for" + ChatColor.DARK_AQUA + reason);
	}

	public static void unmute(Player player, Player unmuter) {
		muteMap.remove(player.getName());
		mutetimeMap.remove(player.getName());
		mutesysMap.remove(player.getName());
		mutereasonMap.remove(player.getName());
		MySQL.doUpdate("DELETE FROM player_mute WHERE player='" + player.getName() + "'");
		Bukkit.broadcastMessage(ChatColor.GOLD + "Mute: " + ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " was unmuted by " + ChatColor.YELLOW + unmuter.getName());
	}

	public static boolean isMuted(Player player) {
		ResultSet rs = MySQL.doQuery("SELECT player FROM player_mute WHERE player='" + player.getName() + "'");
		try {
			if(rs.next()) {
				return true;
			} else if(!rs.next()){
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isMuted(String player) {
		ResultSet rs = MySQL.doQuery("SELECT player FROM player_mute WHERE player='" + player + "'");
		try {
			if(rs.next()) {
				return true;
			} else if(!rs.next()){
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static double getDuration(String player) {
		return (muteMap.get(player));
	}
	
	public static double getDurLeft(String player, TimeUnit unit) {
		if(unit == TimeUnit.HOURS) return (((System.currentTimeMillis() + mutetimeMap.get(player)) - System.currentTimeMillis()) / 3600000);
		else if(unit == TimeUnit.MINUTES) return (((System.currentTimeMillis() + mutetimeMap.get(player)) - System.currentTimeMillis()) / 60000);
		else return (((System.currentTimeMillis() + mutetimeMap.get(player)) - System.currentTimeMillis()) / 1000);
	}
	
//	public static String getDurBest(String player) {
//		if((((System.currentTimeMillis() + mutetimeMap.get(player)) - System.currentTimeMillis()) / 3600000) > 1) {
//			return getDurLeft(player, TimeUnit.HOURS) + " Hours";
//		} else if((((System.currentTimeMillis() + mutetimeMap.get(player)) - System.currentTimeMillis()) / 60000) < 61) {
//			return getDurLeft(player, TimeUnit.MINUTES) + " Minutes";
//		} else {
//			return getDurLeft(player, TimeUnit.SECONDS) + "Seconds";
//		}
//	}
	
	public static String getRemaining(String player) {
		if(!isMuted(player)) {
			return "Not muted";
		}
		return utilTime.convertString(mutesysMap.get(player) + mutetimeMap.get(player).longValue() - System.currentTimeMillis(), TimeUnit.BEST, 1);
	}
	
	public static double getRemInt(String player) {
		if(!isMuted(player)) {
			return 0.0;
		}
		return utilTime.convert(mutesysMap.get(player) + mutetimeMap.get(player).longValue() - System.currentTimeMillis(), TimeUnit.BEST, 1);
	}
	
	public static void checkMute() {
		for(Entry<String, Double> muteEntry : muteMap.entrySet()) {
			String mutee = muteEntry.getKey();
			if(getRemInt(mutee) <= 0) {
				muteMap.remove(mutee);
				mutetimeMap.remove(mutee);
				mutesysMap.remove(mutee);
				MySQL.doUpdate("DELETE FROM player_mute WHERE player='" + mutee + "'");
				Bukkit.broadcastMessage(ChatColor.GOLD + "Mute: " + ChatColor.YELLOW + mutee + ChatColor.WHITE + " was unmuted");
			}
		}
	}
	
	public static String getReason(String player) {
		return mutereasonMap.get(player);
	}
}
