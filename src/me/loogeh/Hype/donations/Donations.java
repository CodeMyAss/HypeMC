package me.loogeh.Hype.donations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.MySQL;

public class Donations {
	public static Hype plugin;
	public static HashMap<String, Donations> donationsMap = new HashMap<String, Donations>();
	
	public String player = "";
	public int donatedAmt = 0;
	public int morphs = 0;
	public boolean claimed = false;
	
	public Donations(String player, int donatedAmt, int morphs, boolean claimed) {
		this.player = player;
		this.donatedAmt = donatedAmt;
		this.morphs = morphs;
		this.claimed = claimed;
	}
	
	public static void load() {
		ResultSet rs = MySQL.doQuery("SELECT player,donated,morphs,claimed FROM donations");
		try {
			while(rs.next()) {
				String player = rs.getString(1);
				int donated = rs.getInt(2);
				int morphs = rs.getInt(3);
				boolean claimed = rs.getBoolean(4);
				
				donationsMap.put(player, new Donations(player, donated, morphs, claimed));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void save() {
		
	}
	
	public static boolean hasPermission(String player, Benefit benefit) {
		if(benefit == Benefit.PIG_MORPH) {
			ResultSet rs = MySQL.doQuery("SELECT pig_morph FROM permissions WHERE player='" + player + "'");
			try {
				return rs.getBoolean(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(benefit == Benefit.SEIZURE) {
			ResultSet rs = MySQL.doQuery("SELECT seizure FROM permissions WHERE player='" + player + "'");
			try {
				if(rs.next()) return rs.getBoolean(1);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static void addDonated(String player, int amount) {
		if(!donationsMap.containsKey(player)) {
			return;
		}
		donationsMap.remove(player);
	}
	
	public static void sendMessage(Player player, String message) {
		if(player == null) {
			return;
		}
		player.sendMessage(ChatColor.GOLD + "Donor - " + message);
	}
	
	public enum Benefit {
		PIG_MORPH,
		SEIZURE;
	}
}
