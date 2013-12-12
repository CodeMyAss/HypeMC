package me.loogeh.Hype.economy;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.MySQL;

public class Votes {
	public static Hype plugin;
	
	public static void addVote(String player, int amount) {
		if(!hasVoted(player)) {
			insertVote(player);
			return;
		}
		MySQL.doUpdate("UPDATE votes SET votes=votes+" + amount + ", totalVotes=totalVotes+" + amount + " WHERE player=" + MySQL.f(player));
	}
	
	public static void subVote(String player, int amount) {
		if(!hasVoted(player)) {
			return;
		}
		MySQL.doUpdate("UPDATE votes SET votes=votes-" + amount + " WHERE player=" + MySQL.f(player));
		
	}
	
	public static void withdrawVote(String player) {
		if(!hasVoted(player)) {
			return;
		}
		int votes = getVotes(player);
		Money.addMoneyOffline(player, votes * 5000);
		MySQL.doUpdate("UPDATE `votes` SET `votes`=0 WHERE player=" + MySQL.f(player));
		
	}
	
	public static int getVotes(String player) {
		if(!hasVoted(player)) {
			return 0;
		}
		int count = 0;
		ResultSet rs = MySQL.doQuery("SELECT votes FROM votes WHERE player=" + MySQL.f(player));
		try {
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public static int getTotalVotes(String player) {
		if(!hasVoted(player)) {
			return 0;
		}
		int count = 0;
		ResultSet rs = MySQL.doQuery("SELECT totalVotes FROM votes WHERE player=" + MySQL.f(player));
		try {
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public static boolean hasVoted(String player) {
		ResultSet rs = MySQL.doQuery("SELECT player FROM votes WHERE player=" + MySQL.f(player));
		try {
			if(rs.next()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void insertVote(String player) {
		if(hasVoted(player)) {
			return;
		}
		MySQL.doUpdate("INSERT INTO `votes`(`player`, `votes`, `totalVotes`) VALUES (" + MySQL.fc(player) + "1, 1)");
	}
}
