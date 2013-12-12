package me.loogeh.Hype.games;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.loogeh.Hype.SQL.MySQL;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Leaderboards {
//	SELECT * FROM arena ORDER BY score DESC LIMIT 5
//	SELECT COUNT(*) FROM arena WHERE exp > (SELECT exp FROM arena WHERE player = 'Loogeh')
	
	public static void getTopPlayers(Player player, LType type, Leaderboard leaderboard, int amount) {
		if(leaderboard == Leaderboard.ARENA) {
			if(amount > 30) {
				player.sendMessage(ChatColor.BLUE + "Arena - " + ChatColor.WHITE + "Exceeded maximum amount - Defaulting to " + ChatColor.YELLOW + "30");
				amount = 30;
			}
			ResultSet rs = MySQL.doQuery("SELECT player," + typeToString(type) + " FROM " + lbToString(leaderboard) + " ORDER BY " + typeToString(type) + " DESC LIMIT " + amount);
			if(type == LType.SCORE || type == LType.KILLS || type == LType.DEATHS || type == LType.EXP || type == LType.WINS || type == LType.LOSSES) {
				player.sendMessage(ChatColor.BLUE + "Arena - " + ChatColor.YELLOW + "Top " + amount + ChatColor.WHITE + " - " + ChatColor.YELLOW + "Score");
				try {
					while(rs.next()) {
						player.sendMessage(ChatColor.YELLOW + rs.getString(1) + ChatColor.WHITE + " - "  + rs.getInt(2));
					}
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Failed to load Top " + amount + " for " + typeToString(type) + " from Leaderboard " + lbToString(leaderboard));
				}
				return;
			}
		}
	}
	
	public static String typeToString(LType type) {
		if(type == LType.EXP) return "exp";
		else if(type == LType.KILLS) return "kills";
		else if(type == LType.DEATHS) return "deaths";
		else if(type == LType.KD) return "kd";
		else if(type == LType.SCORE) return "score";
		else if(type == LType.WINS) return "wins";
		else if(type == LType.LOSSES) return "losses";
		else return "None";
	}
	
	public static LType stringToType(String type) {
		if(type.equalsIgnoreCase("kills")) return LType.KILLS;
		else if(type.equalsIgnoreCase("deaths")) return LType.DEATHS;
		else if(type.equalsIgnoreCase("kd")) return LType.KD;
		else if(type.equalsIgnoreCase("score")) return LType.SCORE;
		else if(type.equalsIgnoreCase("exp")) return LType.EXP;
		else if(type.equalsIgnoreCase("wins")) return LType.WINS;
		else if(type.equalsIgnoreCase("losses")) return LType.LOSSES;
		return LType.NONE;
	}
	
	public static String lbToString(Leaderboard leaderboard) {
		if(leaderboard == Leaderboard.ARENA) return "arena";
		else if(leaderboard == Leaderboard.GAMES) return "games";
		return "None";
	}
	
	public static int getPosition(String player, LType type, Leaderboard leaderboard) {
		int pos = 0;
		if(leaderboard == Leaderboard.ARENA) {
			ResultSet rs = MySQL.doQuery("SELECT COUNT(*) FROM arena WHERE " + typeToString(type) + " > (SELECT " + typeToString(type) + " FROM arena WHERE player = '" + player + "')");
			try {
				if(rs.next()) {
					pos = rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Arena - Failed to get position for player " + player);
			}
		}
		if(leaderboard == Leaderboard.GAMES) {
			return 0;
		}
		return pos + 1;
	}
	
	public enum Leaderboard {
		ARENA,
		GAMES;
	}
	
	public enum LType {
		EXP,
		KILLS,
		DEATHS,
		KD,
		SCORE,
		WINS,
		LOSSES,
		NONE;
	}
}
