package me.loogeh.Hype;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.util.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Warp {
	public static Hype plugin;

	public static void warp(Player player, String warp) {
		if(!exists(warp)) {
			M.v(player, "Warp", ChatColor.WHITE + "Invalid warp");
			return;
		}
		player.teleport(new Location(player.getWorld(), getWarpX(warp), getWarpY(warp), getWarpZ(warp), getWarpYaw(warp), 0));
		player.sendMessage(ChatColor.DARK_PURPLE + "Warp: " + ChatColor.WHITE + "You warped to " + ChatColor.DARK_PURPLE + warp);
	}

	public static void saveWarp(Player player, String name) {
		ResultSet rs = MySQL.doQuery("SELECT warp FROM warp WHERE warp='" + name + "'");
		try {
			if(rs.next()) {
				player.sendMessage(ChatColor.DARK_PURPLE + "Warp: " + ChatColor.WHITE + "The warp [" + ChatColor.YELLOW + name + ChatColor.WHITE + "] already exists");
			} else {
				MySQL.doUpdate("INSERT INTO `warp`(`warp`, `creator`, `x`, `y`, `z`, `yaw`) VALUES ('" + name + "','" + player.getName() + "',"
						+ player.getLocation().getX() + ","
						+ player.getLocation().getY() + ","
						+ player.getLocation().getZ() + ","
						+ player.getLocation().getYaw() + ")");
				player.sendMessage(ChatColor.DARK_PURPLE + "Warp: " + ChatColor.WHITE + "Warp [" + ChatColor.YELLOW + name + ChatColor.WHITE + "] was created");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean exists(String warp) {
		ResultSet rs = MySQL.doQuery("SELECT warp FROM warp WHERE warp='" + warp + "'");
		try {
			if(rs.next()) return true;
			else return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void delWarp(Player player, String name) {
		ResultSet rs = MySQL.doQuery("SELECT warp FROM warp WHERE warp='" + name + "'");
		try {
			if(rs.next()) {
				MySQL.doUpdate("DELETE FROM warp WHERE warp='" + name + "'");
				player.sendMessage(ChatColor.DARK_PURPLE + "Warp: " + ChatColor.WHITE + "Deleted warp [" + ChatColor.YELLOW + name + ChatColor.WHITE + "]");
			} else {
				player.sendMessage(ChatColor.DARK_PURPLE + "Warp: " + ChatColor.WHITE + "The warp [" + ChatColor.YELLOW + name + ChatColor.WHITE + "] does not exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void resetWarp(Player player, String warp, double x, double y, double z, float yaw) {
		ResultSet rs = MySQL.doQuery("SELECT warp FROM warp WHERE warp='" + warp + "'");
		try {
			if(rs.next()) {
				MySQL.doUpdate("UPDATE `warp` SET `warp`='" + warp + "',`creator`='" + player.getName() + "',`x`=" + x + ",`y`=" + y + ",`z`=" + y + "`yaw`=" + yaw +" WHERE warp='" + warp +"'");
			} else {
				player.sendMessage(ChatColor.DARK_PURPLE + "Warp: " + ChatColor.WHITE + "The warp [" + ChatColor.YELLOW + warp + ChatColor.WHITE + "] does not exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getWarpX(String warp) {
		ResultSet rs = MySQL.doQuery("SELECT x FROM warp WHERE warp='" + warp + "'");
		int x = 0;
		try {
			if(rs.next()) {
				x = rs.getInt(1);
			} else {
				return (Integer) null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return x;
	}

	public static int getWarpY(String warp) {
		ResultSet rs = MySQL.doQuery("SELECT y FROM warp WHERE warp='" + warp + "'");
		int y = 0;
		try {
			if(rs.next()) {
				y = rs.getInt(1);
			} else {
				return (Integer) null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return y;
	}

	public static int getWarpZ(String warp) {
		ResultSet rs = MySQL.doQuery("SELECT z FROM warp WHERE warp='" + warp + "'");
		int z = 0;
		try {
			if(rs.next()) {
				z = rs.getInt(1);
			} else {
				return (Integer) null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return z;
	}

	public static float getWarpYaw(String warp) {
		ResultSet rs = MySQL.doQuery("SELECT yaw FROM warp WHERE warp ='" + warp + "'");
		float yaw = 0;
		try {
			if(rs.next()) {
				yaw = rs.getFloat(1);
			} else {
				return (Float) null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return yaw;
	}

	public static void sendWarpList(Player player) {
		ResultSet rs = MySQL.doQuery("SELECT warp FROM warp");
		String warpList = "";
		player.sendMessage(ChatColor.DARK_PURPLE + "Warp List " + ChatColor.WHITE + "----");
		try {
			while(rs.next()) {
				warpList = ChatColor.YELLOW + rs.getString(1) + ", ";
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		player.sendMessage(Utilities.removeLastIntChars(warpList, 2));
	}

	public static void sendWarpDetails(Player player, String name) {
		ResultSet rs = MySQL.doQuery("SELECT * FROM warp WHERE warp='" + name +"'");
		String warp;
		String creator;
		int x;
		int y;
		int z;
		try {
			if(rs.next()) {
				warp = rs.getString(1);
				creator = rs.getString(2);
				x = (int) rs.getDouble(3);
				y = (int) rs.getDouble(4);
				z = (int) rs.getDouble(5);
				player.sendMessage(ChatColor.DARK_PURPLE + "Warp Info: " + ChatColor.WHITE + warp);
				player.sendMessage(ChatColor.DARK_PURPLE + "Creator: " + ChatColor.WHITE + creator);
				player.sendMessage(ChatColor.DARK_PURPLE + "X: " + ChatColor.WHITE + x);
				player.sendMessage(ChatColor.DARK_PURPLE + "Y: " + ChatColor.WHITE + y);
				player.sendMessage(ChatColor.DARK_PURPLE + "Z: " + ChatColor.WHITE + z);
			} else {
				player.sendMessage(ChatColor.DARK_PURPLE + "Warp Info: " + ChatColor.GRAY + "The warp " + ChatColor.WHITE + "[" + ChatColor.YELLOW + name + ChatColor.WHITE + "]" + ChatColor.GRAY + " does not exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
