package me.loogeh.Hype.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import me.loogeh.Hype.Hype;

public class Permissions {
	public static Hype plugin;


	public static String getLevel(Player player) {
		String level = new String();
		ResultSet rs = MySQL.doQuery("SELECT level FROM permissions WHERE player ='" + player.getName() + "'");
		try {
			if(rs.next()) {
				level = rs.getString(1);
				rs.close();
			} else {
				level = "default";
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return level;
	}

	public static int getID(Player player) {
		int id = 0;
		ResultSet rs = MySQL.doQuery("SELECT id FROM permissions WHERE player ='" + player.getName() + "'");
		try {
			if(rs.next()) {
				id = rs.getInt(1);
				rs.close();
			} else {
				id = 0;
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	
	public static int getIDStr(String player) {
		int id = 0;
		ResultSet rs = MySQL.doQuery("SELECT id FROM permissions WHERE player ='" + player + "'");
		try {
			if(rs.next()) {
				id = rs.getInt(1);
				rs.close();
			} else {
				id = 0;
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}


	public static boolean isStaff(Player player) {
		if(getLevel(player).equalsIgnoreCase("owner")) return true;
		if(getLevel(player).equalsIgnoreCase("admin")) return true;
		if(getLevel(player).equalsIgnoreCase("mod")) return true;
		else return false;
	}

	public static boolean isAdmin(Player player) {
		if(getLevel(player).equalsIgnoreCase("admin") || getLevel(player).equalsIgnoreCase("owner")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isDonator(Player player) {
		if(getLevel(player).equalsIgnoreCase("donator")) return true;
		else return false;
	}


	public enum Ranks {
		OWNER(5),
		ADMIN(4),
		MODERATOR(3),
		CONTRIBUTOR(2),
		DONATOR(1),
		DEFAULT(0);

		private int i;

		private Ranks(int value) {
			this.i = value;
		}

		public int toInt() {
			return this.i;
		}

	}

	public static Ranks getRank(Player player) {
		if(getLevel(player).equalsIgnoreCase("owner")) return Ranks.OWNER;
		if(getLevel(player).equalsIgnoreCase("admin")) return Ranks.ADMIN;
		if(getLevel(player).equalsIgnoreCase("mod")) return Ranks.MODERATOR;
		if(getLevel(player).equalsIgnoreCase("contributor")) return Ranks.CONTRIBUTOR;
		if(getLevel(player).equalsIgnoreCase("donator")) return Ranks.DONATOR;
		else return Ranks.DEFAULT;
	}

	public static boolean outranks(String player, String outranks) {
		return (getIDStr(player) > getIDStr(outranks));
	}
	
}
