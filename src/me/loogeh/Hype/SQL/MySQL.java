package me.loogeh.Hype.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;

public class MySQL {
	static Connection SQL;
	public static String pass = "password";
	public static String host = "host";
	public static String user = "username";

	public static boolean testDBConnection() {
		try {
			Connection con = DriverManager.getConnection(host, user, pass);
			con.close();
		} catch (SQLException e) {
			System.out.println("Hype SQL: Test connection failed");
			return false;
		}
		return true;
	}

	public static void setupMySql() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
		} catch (ClassNotFoundException e) {
			System.out.println("Hype SQL: Could not setup up MySQL");
			e.printStackTrace();
		}
		SQL = MySQL.getSQLConnection();
	}

	public static Connection getSQLConnection() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			return DriverManager.getConnection(host, user, pass);
		} catch (SQLException e) {
			System.out.println("Hype SQL: Unable to connect");
			e.printStackTrace();
		}
		return null;
	}

	public static String SQLFormat(String data) {
		return "'" + data + "'";
	}

	public static void doUpdate(String statement){
		Statement st;
		try {
			st = SQL.createStatement();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		try {
			st.executeUpdate(statement);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Hype SQL: Could not execute Statement " + statement);
			return;
		}
	}

	public static ResultSet doQuery(String query){
		Statement st;
		try {
			st = SQL.createStatement();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		try {
			ResultSet rs = st.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Hype SQL: Could not execute query " + query);
			return null;
		}
	}
	public static boolean checkContains(Player player, String table) {
		if(table.equalsIgnoreCase("permissions")) {
			ResultSet rs = MySQL.doQuery("SELECT player FROM permissions WHERE player ='" + player.getName() + "'");
			try {
				if(rs.next()) {
					return true;
				} else if(!rs.next()) {
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if(table.equalsIgnoreCase("donations")) {
			ResultSet rs = MySQL.doQuery("SELECT player FROM donations WHERE player ='" + player.getName() + "'");
			try {
				if(rs.next()) {
					return true;
				} else if(!rs.next()) {
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if(table.equalsIgnoreCase("money")) {
			ResultSet rs = MySQL.doQuery("SELECT player FROM money WHERE player ='" + player.getName() + "'");
			try {
				if(rs.next()) {
					return true;
				} else if(!rs.next()) {
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//    		else if(table.equalsIgnoreCase("item_names")) {
		//    			ResultSet rs = MySQL.doQuery("SELECT name FROM item_names WHERE player ='" + player.getName() + "'");
		//    			try {
		//    				if(rs.next()) {
		//    					return true;
		//    				} else if(!rs.next()) {
		//    					return false;
		//    				}
		//    			} catch (SQLException e) {
		//    				e.printStackTrace();
		//    			}
		//    		}
		return false;
	}

	public static String f(String string) {
		return "'" + string + "'";
	}

	public static String fc(String string) {
		return "'" + string + "',";
	}
}