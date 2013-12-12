package me.loogeh.Hype.Squads;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.util.utilWorld;

public class sDB {
	public static Hype plugin;
	
	public static void loadSquads() {
		int count = 0;
		
		ResultSet rs = MySQL.doQuery("SELECT squad,desc,power,home,admin,date,systime FROM squad");
		try {
			while(rs.next()) {
				String name = rs.getString(1);
				int power = rs.getInt(3);
				String home = rs.getString(4);
				if((home.length() > 0) && (utilWorld.strToLoc(home) == null)) {
					System.out.println("Corrupt Home [" + home + "] for Squad [" + name + "]");
					home = "";
				}
				
				boolean admin = rs.getBoolean(5);
				String date = rs.getString(6);
				long systime = rs.getLong(7);
				
				Squads.squadMap.put(name, new hSquad(name, power, home, admin, date, systime));
				count++;
				System.out.println("Loaded [" + count + "] Squads from Database.");
						
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void linkPlayers() {
		
	}
	
	public static void cleanSquads() {
		int count = 0;
		
		LinkedList<hSquad> emptyList = new LinkedList<hSquad>();
		
		for(hSquad squad : Squads.squadMap.values()) {
			if((squad.memberMap.isEmpty()) && (!squad.admin)) {
				emptyList.add(squad);
			}
		}
		for(hSquad squad : emptyList) {
			System.out.println("Deleting Empty Squad '" + squad.name + "'");
			Squads.deleteSquad(squad);
			count++;
		}
		System.out.println("Deleted " + count + " Empty Squads");
		
	}
	
	public static void loadAlly() {
		ResultSet rs = MySQL.doQuery("SELECT squad,ally,trust FROM squad_ally");
		try {
			while(rs.next()) {
				String name = rs.getString(1);
				String ally = rs.getString(2);
				boolean trust = rs.getBoolean(3);
				
				if(Squads.squadMap.containsKey(name) && (Squads.squadMap.containsKey(ally))) {
					((hSquad) Squads.squadMap.get(name)).allyMap.put(ally, trust);
				} else {
					System.out.println("Removed corrupt alliance '" + name + "' and '" + ally + "'");
					MySQL.doUpdate("DELETE FROM squad_ally WHERE squad=" + name + " AND ally=" + ally);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded all alliances successfully");
	}
	
//	public static void loadEnemy() {
//		ResultSet rs = MySQL.doQuery("SELECT squad,enemy FROM squad_enemy");
//		try {
//			while(rs.next()) {
//				String name = rs.getString(1);
//				String enemy = rs.getString(2);
//				
//				if((Squads.squadMap.containsKey(name)) && (Squads.squadMap.containsKey(enemy))) {
//					((hSquad)Squads.squadMap.get(name)).enemyMap.put(name, enemy);
//				} else {
//					System.out.println("Removed corrupt enemy '" + name + "' and '" + enemy + "'");
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		System.out.println("Loaded all enemies successfully");
//	}
	
	public static void loadTerritory() {
		ResultSet rs = MySQL.doQuery("SELECT squad,chunk,safe FROM squad_claims");
		try {
			while(rs.next()) {
				String owner = rs.getString(1);
				String chunk = rs.getString(2);
				boolean safe = rs.getBoolean(3);
				if(!Squads.squadMap.containsKey(owner)) {
					System.out.println("Removed corrupt Claim '" + chunk + "' from '" + owner + "'");
					MySQL.doUpdate("DELETE FROM squad_calims WHERE squad=" + owner + " AND chunk=" + chunk);
				} else if(utilWorld.strToChunk(chunk) == null) {
					System.out.println("Removed corrupt Claim '" + chunk + "' from '" + owner + "'");
					MySQL.doUpdate("DELETE FROM squad_calims WHERE squad=" + owner + " AND chunk=" + chunk);
				} else {
					Claim claim = new Claim(owner, chunk, safe);
					((hSquad)Squads.squadMap.get(owner)).claimSet.add(chunk);
					Squads.claimMap.put(chunk, claim);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded all claims successfully");
	}
	
	public static void insertTerritory(String squad, String chunk, boolean safe) {
		MySQL.doUpdate("REPLACE INTO squad_claims (squad,chunk,safe) VALUES (" + MySQL.fc(squad) + MySQL.fc(chunk) + safe + ")");
	}
	
	public static void deleteTerritory(String chunk) {
		MySQL.doUpdate("DELETE FROM squad_claims WHERE chunk=" + MySQL.f(chunk));
	}
	
	public static void insertAlly(String squad, String ally, boolean trustA, boolean trustB) {
		MySQL.doUpdate("REPLACE INTO squad_ally (squad,ally,trust) VALUES (" + MySQL.fc(squad) + MySQL.fc(ally) + trustA + ", " + trustB + ")");
		MySQL.doUpdate("REPLACE INTO squad_ally (squad,ally,trust) VALUES (" + MySQL.fc(ally) + MySQL.fc(squad) + trustA + ", " + trustB + ")");
	}
	
	public static void deleteAlly(String squad, String ally) {
		MySQL.doUpdate("DELETE FROM squad_ally WHERE (squad=" + MySQL.f(squad) + " AND ally=" + MySQL.f(ally) + ") OR (squad=" + MySQL.f(ally) + " AND ally=" + MySQL.f(squad) + ")");
	}
	
	public static void insertEnemy(String squad, String enemy) {
		MySQL.doUpdate("REPLACE INTO squad_enemy (squad,enemy) VALUES (" + MySQL.fc(squad) + MySQL.f(enemy) + ")");
		MySQL.doUpdate("REPLACE INTO squad_enemy (squad,enemy) VALUES (" + MySQL.fc(enemy) + MySQL.f(squad) + ")");
	}
	
	public static void deleteEnemy(String squad, String enemy) {
		MySQL.doUpdate("DELETE FROM squad_enemy WHERE (squad=" + MySQL.f(squad) + " AND enemy=" + MySQL.f(enemy) + ") OR (squad=" + MySQL.f(enemy) + " AND enemy " + MySQL.f(squad));
	}
	
	

}
