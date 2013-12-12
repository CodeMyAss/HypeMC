package me.loogeh.Hype.economy;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.loogeh.Hype.SQL.MySQL;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Money {
	
	public static int getMoney(Player player) {
		int money = 0;
		ResultSet rs = MySQL.doQuery("SELECT money FROM money WHERE player ='" + player.getName() + "'");
		try {
        	while (rs.next()) {
        		money = rs.getInt(1);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
    	}
		return money;
	}
	
	public static void subtractMoney(Player player, int amount) {
		ResultSet rs = MySQL.doQuery("SELECT player FROM money WHERE player ='" + player.getName() + "'");
		try {
			if(rs.next()) {
				MySQL.doUpdate("UPDATE `money` SET `money`=money-" + amount + " WHERE player='" + player.getName() + "'");
			} else if(!rs.next()) {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void addMoney(Player player, int amount) {
		ResultSet rs = MySQL.doQuery("SELECT player FROM money WHERE player ='" + player.getName() + "'");
		try {
			if(rs.next()) {
				MySQL.doUpdate("UPDATE `money` SET `money`=money+" + amount + " WHERE player='" + player.getName() + "'");
			} else if(!rs.next()) {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void addMoneyOffline(String player, int amount) {
		ResultSet rs = MySQL.doQuery("SELECT player FROM money WHERE player ='" + player + "'");
		try {
			if(rs.next()) {
				MySQL.doUpdate("UPDATE `money` SET `money`=money+" + amount + " WHERE player='" + player + "'");
			} else if(!rs.next()) {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setMoney(Player player, int amount) {
		ResultSet rs = MySQL.doQuery("SELECT player FROM money WHERE player ='" + player.getName() + "'");
		try {
			if(rs.next()) {
				MySQL.doUpdate("UPDATE `money` SET `money`=" + amount + " WHERE player='" + player.getName() + "'");
			} else if(!rs.next()) {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getMoneyFromStr(String name) {
		int money = 0;
		ResultSet rs = MySQL.doQuery("SELECT money FROM money WHERE player ='" + name + "'");
		try {
        	while (rs.next()) {
        		money = rs.getInt(1);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
    	}
		return money;
	}
	
	public static void sendTopList(Player player) {
		ResultSet rs = MySQL.doQuery("SELECT * FROM `money` ORDER BY money DESC LIMIT 10");
		try {
			while(rs.next()) {
				player.sendMessage(ChatColor.YELLOW + rs.getString(1) + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "$" + rs.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
