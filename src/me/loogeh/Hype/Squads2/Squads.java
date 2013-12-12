package me.loogeh.Hype.Squads2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.SQL.Permissions.Ranks;
import me.loogeh.Hype.Squads.Claim;
import me.loogeh.Hype.Squads.hSquad;
import me.loogeh.Hype.Squads.hSquad.Rank;
import me.loogeh.Hype.util.Utilities;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilServer;
import me.loogeh.Hype.util.utilTime;
import me.loogeh.Hype.util.utilWorld;
import me.loogeh.Hype.util.utilTime.TimeUnit;

public class Squads {
	public static Hype plugin;
	public static HashMap<String, hSquad> squadMap = new HashMap<String, hSquad>();
	public static HashMap<String, Claim> claimMap = new HashMap<String, Claim>(); //Chunk - Claim

	public static HashMap<String, hSquad.Rank> rankMap = new HashMap<String, hSquad.Rank>(); //Stores Player - Rank
	public static HashMap<String, String> memberMap = new HashMap<String, String>(); //Stores Player - Squad
	public static HashMap<String, String> allyMap = new HashMap<String, String>(); //Stores Squad -  Ally
	public static HashMap<String, String> requestAllyMap = new HashMap<String, String>(); //Stores Squad - Ally
	public static HashMap<String, String> requestNeutralMap = new HashMap<String, String>(); //Stores Squad - Enemy
	public static HashMap<String, String> enemyMap = new HashMap<String, String>(); //Stores Squad - Enemy
	public static HashMap<String, String> inviteMap = new HashMap<String, String>(); //Stores Squad - Invitee

	public static HashMap<String, Long> regenMap = new HashMap<String, Long>(); //Stores Squad - Last Power
	public static HashMap<String, String> homeMap = new HashMap<String, String>(); //Stores Squad - Home
	public static HashMap<String, Location> homeLocMap = new HashMap<String, Location>(); //Stores Squad - Home

	public static HashSet<Integer> denyInteract = new HashSet<Integer>();
	public static HashSet<Integer> allowInteract = new HashSet<Integer>();

	public static HashSet<Integer> denyUsePlace = new HashSet<Integer>();
	public static HashSet<Integer> allowUsePlace = new HashSet<Integer>();

	public static HashSet<Integer> allowBreak = new HashSet<Integer>();
	
	public static HashSet<String> squadAdmin = new HashSet<String>();


	public static long powerUpdate = 300000L; // 5 Minutes

	public static void loadPlayers() {
		ResultSet rs = MySQL.doQuery("SELECT player,role,squad FROM squad_player");
		try {
			while(rs.next()) {
				memberMap.put(rs.getString(1), rs.getString(3));
				rankMap.put(rs.getString(1), strToRank(rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Squads SQL: Error while loading players");
		}
	}

	public static void loadSquads() {
		ResultSet rs = MySQL.doQuery("SELECT squad,power,home,admin,date,systime,lastPower FROM squad");
		try {
			while(rs.next()) {
				squadMap.put(rs.getString(1), new hSquad(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getBoolean(4), rs.getString(5), rs.getLong(6)));
				regenMap.put(rs.getString(1), rs.getLong(7));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Squads SQL: Error while loading squads");
		}
	}

	public static void loadHomes() {
		ResultSet rs = MySQL.doQuery("SELECT squad,x,y,z,yaw,world FROM squad_home");
		try {
			while(rs.next()) {
				if(homeLocMap.containsKey(rs.getString(1))) {
					homeLocMap.remove(rs.getString(1));
				}
				homeLocMap.put(rs.getString(1), new Location(utilWorld.strToWorld(rs.getString(6)), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getFloat(5), 0.0F));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void loadClaims() {
		ResultSet rs = MySQL.doQuery("SELECT squad,chunk,safe FROM squad_claims");
		try {
			while(rs.next()) {
				claimMap.put(rs.getString(2), new Claim(rs.getString(1), rs.getString(2), rs.getBoolean(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Squads SQL: Error while loading claims");
		}
	}

	public static void loadAllies() {
		ResultSet rs = MySQL.doQuery("SELECT squad,ally,trust FROM squad_ally");
		try {
			while(rs.next()) {
				allyMap.put(rs.getString(1), rs.getString(2));
				squadMap.get(rs.getString(1)).allyMap.put(rs.getString(2), rs.getBoolean(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Squads SQL: Error while loading allies");
		}
	}

	public static void loadEnemies() {
		ResultSet rs = MySQL.doQuery("SELECT squad,enemy FROM squad_enemy");
		try {
			while(rs.next()) {
				enemyMap.put(rs.getString(1), rs.getString(2));
				squadMap.get(rs.getString(1)).enemyMap.put(rs.getString(2), false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Squads SQL: Error while loading enemies");
		}
	}
	
	public static void loadTimes() {
		ResultSet rs = MySQL.doQuery("SELECT squad,systime,date FROM squad");
		try {
			while(rs.next()) {
				squadMap.get(rs.getString(1)).systime = rs.getLong(2);
				squadMap.get(rs.getString(1)).date = rs.getDate(3).toString();
			}
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Squads SQL: Error while loading date/time");
		}
	}

	public static void load() {
		loadSquads();
		loadPlayers();
		loadClaims();
		loadAllies();
		loadEnemies();
		loadHomes();
		loadTimes();
	}

	public static void saveSquads() {
		for(String keys : squadMap.keySet()) {
			int power = squadMap.get(keys).power;
			String home = homeMap.get(keys);
			boolean admin = squadMap.get(keys).admin;
			String date = squadMap.get(keys).date;
			long systime = squadMap.get(keys).systime;
			long lastPower = 365464526543744314L;
			if(regenMap.containsKey(keys)) lastPower = regenMap.get(keys).longValue();
			else lastPower = 365464526543744313L;

			MySQL.doUpdate("DELETE FROM squad WHERE squad=" + MySQL.f(keys));
			MySQL.doUpdate("INSERT INTO `squad`(`squad`, `power`, `home`, `admin`, `date`, `systime`, `lastPower`) VALUES (" + MySQL.fc(keys) + power + ", " + MySQL.fc(home) + admin + ", " + MySQL.fc(date) + systime + ", " + lastPower + ")");
		}
	}


	public static void saveClaims() {
		for(String keys : claimMap.keySet()) {
			insertTerritory(claimMap.get(keys).owner, claimMap.get(keys).chunk, claimMap.get(keys).safe);
		}
		System.out.println("Squads SQL: Saved claims");
	}

	public static void saveAllies() {
		//		for(String keys : allyMap.keySet()) {
		//			insertAlly(keys, allyMap.get(keys), areTrusted(keys, allyMap.get(keys)), areTrusted(allyMap.get(keys), keys));
		//		}
		for(String squads : squadMap.keySet()) {
			for(String allies : squadMap.get(squads).allyMap.keySet()) {
				insertAlly(squads, allies, squadMap.get(squads).allyMap.get(allies), squadMap.get(allies).allyMap.get(squads));
			}
		}
	}

	public static void saveEnemies() {
		//		for(String keys : enemyMap.keySet()) {
		//			insertEnemy(keys, enemyMap.get(keys));
		//		}
		for(String squads : squadMap.keySet()) {
			for(Entry<String, Boolean> enemies : squadMap.get(squads).enemyMap.entrySet()) {
				insertEnemy(squads, enemies.getKey());//CHANGED FROM KEY - VALUE to SQUADS - KEY
			}
		}
	}

	public static void saveHomes() {
		for(String keys : homeLocMap.keySet()) {
			MySQL.doUpdate("UPDATE `squad_home` SET `x`=" + homeLocMap.get(keys).getX() + ",`y`=" + homeLocMap.get(keys).getY() + ",`z`=" + homeLocMap.get(keys).getZ() + ",`yaw`=" + homeLocMap.get(keys).getYaw() + " WHERE squad=" + MySQL.f(keys));
		}
	}
	
	public static void save() {
		saveSquads();
		saveClaims();
		saveAllies();
		saveEnemies();
		saveHomes();
	}

	public static boolean areTrusted(String squadA, String squadB) {
		if(!squadMap.get(squadA).allyMap.containsKey(squadB) || !squadMap.get(squadB).allyMap.get(squadA)) {
			return false;
		}
		return squadMap.get(squadA).allyMap.get(squadB) && squadMap.get(squadB).allyMap.get(squadA);
	}

	public static void insertTerritory(String squad, String chunk, boolean safe) {
		ResultSet rs = MySQL.doQuery("SELECT chunk FROM squad_claims WHERE chunk=" + MySQL.f(chunk));
		try {
			if(rs.next()) {
				return;
			} else {
				MySQL.doUpdate("INSERT INTO squad_claims (squad,chunk,safe) VALUES (" + MySQL.fc(squad) + MySQL.fc(chunk) + safe + ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Squads SQL: Error while INSERT_TERRITORY [" + chunk + "]");
		}
	}

	public static void deleteTerritory(String chunk) {
		MySQL.doUpdate("DELETE FROM squad_claims WHERE chunk=" + MySQL.f(chunk));
	}

	public static void deleteTerritoryAllSquadLand(String squad) {
		MySQL.doUpdate("DELETE FROM squad_claims WHERE squad=" + MySQL.f(squad));
	}

	public static void insertAlly(String squad, String ally, boolean trustA, boolean trustB) {
		if(areAllies(squad, ally)) {
			return;
		}
		ResultSet rs = MySQL.doQuery("SELECT squad,ally FROM squad_ally WHERE squad=" + MySQL.f(squad) + " AND ally=" + MySQL.f(ally));
		try {
			if(rs.next()) {
				return;
			} else {
				MySQL.doUpdate("INSERT INTO squad_ally (squad,ally,trust) VALUES (" + MySQL.fc(squad) + MySQL.fc(ally) + trustA + ")");
				MySQL.doUpdate("INSERT INTO squad_ally (squad,ally,trust) VALUES (" + MySQL.fc(ally) + MySQL.fc(squad) + trustA + ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void deleteAlly(String squad, String ally) {
		if(!areAllies(squad, ally)) {
			return;
		}
		MySQL.doUpdate("DELETE FROM squad_ally WHERE (squad=" + MySQL.f(squad) + " AND ally=" + MySQL.f(ally) + ") OR (squad=" + MySQL.f(ally) + " AND ally=" + MySQL.f(squad) + ")");
	}

	public static void insertEnemy(String squad, String enemy) {
		if(areEnemies(squad, enemy)) {
			return;
		}
		ResultSet rs = MySQL.doQuery("SELECT squad,enemy FROM squad_enemy WHERE squad=" + MySQL.f(squad) + " AND enemy=" + MySQL.f(enemy));
		try {
			if(rs.next()) {
				return;
			} else {
				MySQL.doUpdate("REPLACE INTO squad_enemy (squad,enemy) VALUES (" + MySQL.fc(squad) + MySQL.f(enemy) + ")");
				MySQL.doUpdate("REPLACE INTO squad_enemy (squad,enemy) VALUES (" + MySQL.fc(enemy) + MySQL.f(squad) + ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void deleteEnemy(String squad, String enemy) {
		if(!areEnemies(squad, enemy)) {
			return;
		}
		MySQL.doUpdate("DELETE FROM squad_enemy WHERE (squad=" + MySQL.f(squad) + " AND enemy=" + MySQL.f(enemy) + ") OR (squad=" + MySQL.f(enemy) + " AND enemy " + MySQL.f(squad));
	}

	public static Rank strToRank(String rank) {
		if(rank.equalsIgnoreCase("leader")) return Rank.LEADER;
		if(rank.equalsIgnoreCase("admin")) return Rank.ADMIN;
		if(rank.equalsIgnoreCase("member")) return Rank.MEMBER;
		return Rank.NONE;
	}

	public static void create(String name, String creator, boolean admin) {
		if(!admin) {
			squadMap.put(name, new hSquad(name, 3, "", false, utilTime.timeStr(), System.currentTimeMillis()));
			addPlayer(creator, name, Rank.LEADER);
			MySQL.doUpdate("INSERT INTO `squad`(`squad`, `power`, `home`, `admin`, `date`, `systime`, `lastPower`) VALUES (" + MySQL.fc(name) + " 3, '', 0, " + MySQL.fc(utilTime.timeStr()) + System.currentTimeMillis() + ", " + System.currentTimeMillis() + ")");
			return;
		}
		squadMap.put(name, new hSquad(name, 3, "", true, utilTime.timeStr(), System.currentTimeMillis()));
		addPlayer(creator, name, Rank.LEADER);
		MySQL.doUpdate("INSERT INTO `squad`(`squad`, `power`, `home`, `admin`, `date`, `systime`, `lastPower`) VALUES (" + MySQL.fc(name) + " 10000, '', 1, " + MySQL.fc(utilTime.timeStr()) + System.currentTimeMillis() + ", " + System.currentTimeMillis() + ")");


	}

	public static void addPlayer(String player, String squad, Rank rank) {
		if(!squadMap.containsKey(squad)) {
			return;
		}
		if(hasSquad(player)) {
			return;
		}
		memberMap.put(player, squad);
		rankMap.put(player, rank);
		regenMap.put(squad, System.currentTimeMillis());
		MySQL.doUpdate("INSERT INTO `squad_player`(`player`, `role`, `squad`) VALUES (" + MySQL.f(player) +", " + MySQL.f(rank.toString().toLowerCase()) + ", " + MySQL.f(squad) + ")");
	}

	public static void removePlayer(String player, String squad) {
		memberMap.remove(player);
		rankMap.remove(player);
		MySQL.doUpdate("DELETE FROM squad_player WHERE player=" + MySQL.f(player));
	}


	public static int getMemberSize(String squad) {
		int count = 0;
		if(!memberMap.containsValue(squad)) return 0;
		for(String values : memberMap.values()) {
			if(values.equals(squad)) {
				count++;
			}
		}
		return count;
	}

	public static int getMaxPower(String squad) {
		if(squadMap.get(squad).admin) {
			return 10000;
		}
		return 2 + getMemberSize(squad);
	}

	public static int getPower(String squad) {
		if(!squadMap.containsKey(squad)) return 12345;
		if(squadMap.get(squad).admin) return 10000;
		return squadMap.get(squad).power;
	}

	public static int getLand(String squad) {
		int count = 0;
		for(String keys : claimMap.keySet()) {
			if(claimMap.get(keys).owner.equals(squad)) count++;
		}
		return count;

	}

	public static void unclaimAll(String squad) {
		ArrayList<String> toUnclaim = new ArrayList<String>();
		for(String chunk : claimMap.keySet()) {
			if(claimMap.get(chunk).owner.equals(squad)) toUnclaim.add(chunk);
		}
		for(String chunk1 : toUnclaim) {
			claimMap.remove(chunk1);
		}
		deleteTerritoryAllSquadLand(squad);
	}

	public static int getMaxLand(String squad) {
		if(squadMap.get(squad).admin) return 1000;
		return 2 + getMemberSize(squad);
	}

	public static boolean memberOnline(String squad) {
		for(Player players : Bukkit.getOnlinePlayers()) {
			if(getSquad(players.getName()).equals(squad)) return true;
		}
		return false;
	}

	public static String memberOnlineColour(String squad) {
		if(memberOnline(squad)) return ChatColor.GREEN + "Online";
		return ChatColor.RED + "Offline";
	}


	public static int getMaxAllies(String squad) {
		if(getMemberSize(squad) > 9) return 1;
		return 10 - getMemberSize(squad);
	}

	public static void addTrust(String squad, String squadB) {
		if(!areAllies(squad, squadB)) return;
		if(oneTrust(squad, squadB)) return;
		if(areTrusted(squad, squadB)) return;
		squadMap.get(squad).allyMap.put(squadB, true);
		MySQL.doUpdate("UPDATE squad_ally SET trust=1 WHERE squad=" + MySQL.f(squad) + "AND ally=" + MySQL.f(squadB));
	}

	public static void revokeTrust(String squad, String squadB) {
		if(!oneTrust(squad, squadB)) return;
		squadMap.get(squad).allyMap.put(squadB, false);
		MySQL.doUpdate("UPDATE squad_ally SET trust=0 WHERE squad=" + MySQL.f(squad) + " AND ally=" + MySQL.f(squadB));
	}


	public static void sendHelpMenu(Player player, int page) {
		player.sendMessage(ChatColor.BLUE + "Squads Help Page " + page);
		player.sendMessage(ChatColor.BLUE + "Ranks - " + ChatColor.YELLOW + "L - Leader, A - Admin, M - Member, N - None");
		if(page == 1) {
			player.sendMessage(ChatColor.BLUE + "/s (squad) " + ChatColor.WHITE + "Displays Squad Info " + ChatColor.YELLOW + "M");
			player.sendMessage(ChatColor.BLUE + "/s create <name> " + ChatColor.WHITE + "Creates a new Squad " + ChatColor.YELLOW + "N");
			player.sendMessage(ChatColor.BLUE + "/s set home " + ChatColor.WHITE + "Set new home " + ChatColor.YELLOW + "A");
			player.sendMessage(ChatColor.BLUE + "/s home " + ChatColor.WHITE + "Teleport to home " + ChatColor.YELLOW + "M");
			player.sendMessage(ChatColor.BLUE + "/s claim " + ChatColor.WHITE + "Claims a chunk " + ChatColor.YELLOW + "A");
			player.sendMessage(ChatColor.BLUE + "/s unclaim " + ChatColor.WHITE + "Unclaims a chunk " + ChatColor.YELLOW + "A");
			return;
		}
		if(page == 2) {
			player.sendMessage(ChatColor.BLUE + "/s invite <player> " + ChatColor.WHITE + "Invite a player " + ChatColor.YELLOW + "A");
			player.sendMessage(ChatColor.BLUE + "/s deinvite <player> " + ChatColor.WHITE + "De-invite a player " + ChatColor.YELLOW + "A");
			player.sendMessage(ChatColor.BLUE + "/s ally <squad> " + ChatColor.WHITE + "Request alliance with a Squad " + ChatColor.YELLOW + "A");
			player.sendMessage(ChatColor.BLUE + "/s neutral <squad> " + ChatColor.WHITE + "Neutralize relation with Squad " + ChatColor.YELLOW + "A");
			player.sendMessage(ChatColor.BLUE + "/s enemy <squad> " + ChatColor.WHITE + "Enemy a squad " + ChatColor.YELLOW + "A");
			player.sendMessage(ChatColor.BLUE + "/s leave " + ChatColor.WHITE + "Leave your current Squad " + ChatColor.YELLOW + "M");
			player.sendMessage(ChatColor.BLUE + "/s join <player> " + ChatColor.WHITE + "Join a Squad " + ChatColor.YELLOW + "N");
			return;
		}
		if(page == 3) {
			player.sendMessage(ChatColor.BLUE + "/s kick <player> " + ChatColor.WHITE + "Kick a member " + ChatColor.YELLOW + "L");
			player.sendMessage(ChatColor.BLUE + "/s promote <player> " + ChatColor.WHITE + "Promote player to Admin " + ChatColor.YELLOW + "A");
			player.sendMessage(ChatColor.BLUE + "/s demote <player> " + ChatColor.WHITE + "Demote player to member " + ChatColor.YELLOW + "L");
			player.sendMessage(ChatColor.BLUE + "/s list " + ChatColor.WHITE + "Sends list of Squads " + ChatColor.YELLOW + "N");
			player.sendMessage(ChatColor.BLUE + "/s trust <squad> " + ChatColor.WHITE + "Give trust to an ally " + ChatColor.YELLOW + "A");
			player.sendMessage(ChatColor.BLUE + "/s leave " + ChatColor.WHITE + "Leave your current Squad " + ChatColor.YELLOW + "M");
			player.sendMessage(ChatColor.BLUE + "/s neutral <squad> " + ChatColor.WHITE + "Neutral a squad from enemy " + ChatColor.YELLOW + "A");
		}
	}

	public static void invite(Player inviter, String player) {
		String squad = getSquad(inviter.getName());
		for(String keys : inviteMap.keySet()) {
			if(inviteMap.get(keys).equals(player)) return;
		}
		inviteMap.put(squad, player);
	}

	public static void deinvite(String player, String squad) {
		for(String keys : inviteMap.keySet()) {
			if(inviteMap.get(keys).equals(player)) {
				inviteMap.remove(keys);
				return;
			}
			return;
		}
	}

	public static boolean isInvited(String player, String squad) {
		for(String keys : inviteMap.keySet()) {
			if(inviteMap.get(keys).equals(player)) return true;
		}
		return false;
	}

	public static String getMembers(String squad) {
		String members = "";
		for(String keys : memberMap.keySet()) {
			if(memberMap.get(keys).equals(squad)) {
				Player player = Bukkit.getServer().getPlayer(keys);
				if(getRank(keys) == Rank.LEADER) {
					if(player == null) {
						members = members + ChatColor.RED + "**" + keys + ", ";
					} else {
						members = members + ChatColor.GREEN + "**" + keys + ", ";
					}
				} else if(getRank(keys) == Rank.ADMIN) {
					if(player == null) {
						members = members + ChatColor.RED + "*" + keys + ", ";
					} else {
						members = members + ChatColor.GREEN + "*" + keys + ", ";
					}
				} else {
					if(player == null) {
						members = members + ChatColor.RED + keys + ", ";
					} else {
						members = members + ChatColor.GREEN + keys + ", ";
					}
				}
			}
		}
		return Utilities.removeLastIntChars(members, 2);
	}

	public static String getAllies(String squad) {
		String allies = "";
		for(Entry<String, Boolean> ally : Squads.squadMap.get(squad).allyMap.entrySet()) {
			if(ally.getValue() == false) {
				allies = allies + ChatColor.GREEN + ally.getKey() + ", ";
			} else {
				allies = allies + ChatColor.DARK_GREEN + ally.getKey() + ", ";
			}
		}
		return Utilities.removeLastIntChars(allies, 2);
	}

	public static void removeAllies(String squad) {
		if(squadMap.containsKey(squad)) {
			for(String allies : squadMap.get(squad).allyMap.keySet()) {
				if(squadMap.get(allies).allyMap.containsKey(squad)) squadMap.get(allies).allyMap.remove(squad);
			}
			squadMap.get(squad).allyMap.clear();
		}
	}

	public static void removeEnemies(String squad) {
		enemyMap.keySet().removeAll(Collections.singleton(squad));
		Iterator<String> it = enemyMap.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			if(key.equalsIgnoreCase(squad)) {
				it.remove();
			} else {
				if(enemyMap.get(key).equalsIgnoreCase(squad)) {
					it.remove();
				}
			}
		}
	}



	public static String getEnemies(String squad) {
		String enemies = "";
		for(String keys : squadMap.get(squad).enemyMap.keySet()) enemies = enemies + ChatColor.GREEN + keys + ", ";
		return Utilities.removeLastIntChars(enemies, 2);
	}

	public static void sendMembersMsg(String squad, String message) {
		ArrayList<String> memberList = new ArrayList<String>();
		for(String keys : memberMap.keySet()) {
			if(memberMap.get(keys).equals(squad)) {
				memberList.add(keys);
			}
		}
		for(String members : memberList) {
			Player players = Bukkit.getPlayerExact(members);
			if(players != null) {
				players.sendMessage(ChatColor.BLUE + "Squads - " + message);
			}
		}
	}

	public static String getOwnerChunk(Chunk chunk) {
		if(!isClaimed(utilWorld.chunkToStr(chunk))) {
			return "Wilderness";
		}
		return claimMap.get(utilWorld.chunkToStr(chunk)).owner;
	}

	public static boolean areAllies(String squad, String squadB) {
		if(!squadMap.containsKey(squad) || !squadMap.containsKey(squadB)) {
			return false;
		}
		return squadMap.get(squad).allyMap.containsKey(squadB);
		//		if(!allyMap.containsKey(squad) || !allyMap.containsKey(squadB) || !allyMap.containsValue(squad) || !allyMap.containsValue(squadB)) {
		//			return false;
		//		}
		//		for(String keys : allyMap.keySet()) {
		//			if(keys.equalsIgnoreCase(squad)) {
		//				if(allyMap.get(keys).equalsIgnoreCase(squadB)) {
		//					return true;
		//				}
		//			}
		//			if(keys.equalsIgnoreCase(squadB)) {
		//				if(allyMap.get(keys).equalsIgnoreCase(squad)) {
		//					return true;
		//				}
		//			}
		//		}

		//return false;
	}

	public static boolean oneTrust(String squad, String squadB) {
		for(Entry<String, Boolean> ally : Squads.squadMap.get(squad).allyMap.entrySet()) {
			if(ally.getKey().equalsIgnoreCase(squadB)) {
				if(ally.getValue() == true) {
					return true;
				}
			}
		}
		return false;
		//		if(!squadMap.get(squad).allyMap.containsKey(squadB)) {
		//			return false;
		//		}
		//		if(squadMap.get(squad).allyMap.get(squadB) == true) {
		//			return true;
		//		}
		//		return false;
		//		if(!trustMap.containsKey(squad) || !trustMap.containsValue(squadB)) {
		//			return false;
		//		}
		//		for(String keys : trustMap.keySet()) {
		//			if(keys.equalsIgnoreCase(squad)) {
		//				if(trustMap.get(keys).equalsIgnoreCase(squadB)) {
		//					return true;
		//				}
		//			}
		//		}
		//		
		//		return false;
	}

	public static void setNeutral(String squad, String squadB) {
		if(!squadMap.containsKey(squad) || !squadMap.containsKey(squadB)) {
			return;
		}
		if(!areEnemies(squad, squadB) && !areAllies(squad, squadB)) {
			return;
		}
		squadMap.get(squad).enemyMap.remove(squadB);
		squadMap.get(squadB).enemyMap.remove(squad);
		MySQL.doUpdate("DELETE FROM squad_enemy WHERE squad=" + MySQL.f(squad) + " AND enemy=" + MySQL.f(squadB));
		MySQL.doUpdate("DELETE FROM squad_enemy WHERE squad=" + MySQL.f(squadB) + " AND enemy=" + MySQL.f(squad));
		//		Iterator<String> it = enemyMap.keySet().iterator();
		//		while(it.hasNext()) {
		//			String key = it.next();
		//			if(key.equalsIgnoreCase(squad)) {
		//				if(enemyMap.get(key).equalsIgnoreCase(squadB)) {
		//					//enemyMap.remove(key);
		//					it.remove();
		//				}
		//			} else {
		//				if(key.equalsIgnoreCase(squadB)) {
		//					if(enemyMap.get(key).equalsIgnoreCase(squad)) {
		//						//enemyMap.remove(key);
		//						it.remove();
		//					}
		//				}
		//			}
		//		}


	}

	public static void addEnemy(String squad, String enemy) {
		if(!squadMap.containsKey(squad) || !squadMap.containsKey(enemy)) {
			return;
		}
		if(areEnemies(squad, enemy)) {
			return;
		}
		squadMap.get(squad).enemyMap.put(enemy, false);
		squadMap.get(enemy).enemyMap.put(squad, false);
		MySQL.doUpdate("INSERT INTO `squad_enemy`(`squad`, `enemy`) VALUES (" + MySQL.fc(squad) + MySQL.f(enemy) + ")");
		MySQL.doUpdate("INSERT INTO `squad_enemy`(`squad`, `enemy`) VALUES (" + MySQL.fc(enemy) + MySQL.f(squad) + ")");
	}

	public static void addAlly(String squad, String ally) {
		if(!memberMap.containsValue(squad)) {
			return;
		}
		if(areAllies(squad, ally)) {
			return;
		}
		allyMap.put(squad, ally);
		allyMap.put(ally, squad);
		squadMap.get(squad).allyMap.put(ally, false);
		squadMap.get(ally).allyMap.put(squad, false);
		MySQL.doUpdate("INSERT INTO `squad_ally`(`squad`, `ally`, `trust`) VALUES (" + MySQL.fc(squad) + MySQL.fc(ally) + "0)");
		MySQL.doUpdate("INSERT INTO `squad_ally`(`squad`, `ally`, `trust`) VALUES (" + MySQL.fc(ally) + MySQL.fc(squad) + "0)");
	}

	public static void requestAlliance(String squad, String ally) {
		if(!squadMap.containsKey(ally)) {
			return;
		}
		if(areAllies(squad, ally)) {
			return;
		}
		if(areEnemies(squad, ally)) {
			return;
		}
		if(requestAllyMap.containsKey(squad)) {
			for(String keys : requestAllyMap.keySet()) {
				if(requestAllyMap.get(keys).equalsIgnoreCase(ally)) {
					return;
				}
			}
		}
		requestAllyMap.put(squad, ally);
	}

	public static void requestNeutral(String squad, String enemy) {
		if(!squadMap.containsKey(squad) || !squadMap.containsKey(enemy)) {
			return;
		}
		if(!areEnemies(squad, enemy)) {
			return;
		}
		if(requestNeutralMap.containsKey(squad)) {
			for(String keys : requestNeutralMap.keySet()) {
				if(requestNeutralMap.get(keys).equalsIgnoreCase(enemy)) {
					return;
				}
			}
		}
		requestNeutralMap.put(squad, enemy);
	}

	public static void acceptNeutral(String squad, String squadB) {
		if(!squadMap.containsKey(squad) || !squadMap.containsKey(squadB)) {
			return;
		}
		if(!requestNeutralMap.containsKey(squad)) {
			return;
		}
		for(String keys : requestNeutralMap.keySet()) {
			if(requestNeutralMap.get(keys).equalsIgnoreCase(squadB)) {
				setNeutral(squad, squadB);
			}
		}
	}

	public static boolean areEnemies(String squad, String squadB) {
		return squadMap.get(squad).enemyMap.containsKey(squadB);
		//		if(!enemyMap.containsKey(squad) || !enemyMap.containsKey(squadB) || !enemyMap.containsValue(squad) || !enemyMap.containsValue(squadB)) {
		//			return false;
		//		}
		//		for(String keys : enemyMap.keySet()) {
		//			if(keys.equalsIgnoreCase(squad)) {
		//				if(enemyMap.get(keys).equalsIgnoreCase(squadB)) {
		//					return true;
		//				}
		//			}
		//			if(keys.equalsIgnoreCase(squadB)) {
		//				if(enemyMap.get(keys).equalsIgnoreCase(squad)) {
		//					return true;
		//				}
		//			}
		//		}
		//		if(enemyMap.get(squad).equals(squadB)) {
		//			return true;
		//		}
		//		if(enemyMap.get(squadB).equals(squad)) {
		//			return true;
		//		}
	}

	public static void clearAllies(String squad) {
		for(String values : allyMap.values()) {
			if(allyMap.get(squad).equals(values)) {
				allyMap.remove(squad);
			}
			if(allyMap.get(values).equals(squad)) {
				allyMap.remove(values);
			}
		}
		MySQL.doUpdate("DELETE FROM squad_ally WHERE squad=" + MySQL.f(squad) + " OR ally=" + MySQL.f(squad));
	}

//	public static void clearTrusts(String squad) {
//		for(String values : trustMap.values()) {
//			if(trustMap.get(squad).equals(values)) {
//				trustMap.remove(squad);
//			}
//			if(trustMap.get(values).equals(squad)) {
//				trustMap.remove(values);
//			}
//		}
//	}

	public static void clearInvites(String squad) {
		inviteMap.keySet().removeAll(Collections.singleton(squad));
	}

	public static void clearEnemies(String squad) {
		for(String values : enemyMap.values()) {
			if(enemyMap.get(squad).equals(values)) {
				enemyMap.remove(squad);
			}
		}
		MySQL.doUpdate("DELETE FROM squad_enemy WHERE squad=" + MySQL.f(squad) + " OR enemy=" + MySQL.f(squad));
	}

	public static void leave(String player, String squad) {
		if(getPower(squad) == getMaxPower(squad)) {
			subtractPower(squad, 1);
		}
		memberMap.remove(player);
		rankMap.remove(player);
		MySQL.doUpdate("DELETE FROM squad_player WHERE player=" + MySQL.f(player));
	}

	public static boolean hasSquad(String player) {
		return memberMap.containsKey(player);
	}


	public static void disband(Player player, String squad) {
		if(getRank(player.getName()) != Rank.LEADER) {
			player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be the " + ChatColor.AQUA + "Leader " + ChatColor.WHITE + "to disband");
			return;
		}
		sendMembersMsg(squad, ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " disbanded your " + ChatColor.YELLOW + "Squad");
		memberMap.values().removeAll(Collections.singleton(squad));
		removeAllies(squad);
		removeEnemies(squad);
		unclaimAll(squad);
		if(homeLocMap.containsKey(squad)) homeLocMap.remove(squad);
		if(squadMap.containsKey(squad)) squadMap.remove(squad);
		MySQL.doUpdate("DELETE FROM squad WHERE squad=" + MySQL.f(squad));
		MySQL.doUpdate("DELETE FROM squad_player WHERE squad=" + MySQL.f(squad));
		MySQL.doUpdate("DELETE FROM squad_ally WHERE squad=" + MySQL.f(squad) + " OR ally=" + MySQL.f(squad));
		MySQL.doUpdate("DELETE FROM squad_enemy WHERE squad=" + MySQL.f(squad) + " OR enemy=" + MySQL.f(squad));
		MySQL.doUpdate("DELETE FROM squad_home WHERE squad=" + MySQL.f(squad));
	}

	public static Rank getRank(String player) {
		if(!hasSquad(player)) return Rank.NONE;
		return rankMap.get(player);

	}

	public static String getSquad(String player) {
		if(!hasSquad(player)) return "";
		return memberMap.get(player);
	}

	public static int getAllySize(String squad) {
		return squadMap.get(squad).allyMap.size();
	}

	public static int getEnemySize(String squad) {
		return squadMap.get(squad).enemyMap.size();
	}

	public static SquadType getType(String squad) {
		if(squadMap.get(squad).admin == true) return SquadType.ADMIN;
		return SquadType.REGULAR;
	}

	public static enum SquadType {
		ADMIN,
		REGULAR;
	}

	public static boolean isClaimed(String chunk) {
		return claimMap.containsKey(chunk);
	}

	public static void claim(String squad, String chunk, boolean safe) {
		if(isClaimed(chunk)) {
			return;
		}
		claimMap.put(chunk, new Claim(squad, chunk, safe));
		MySQL.doUpdate("INSERT INTO `squad_claims`(`squad`, `chunk`, `safe`) VALUES (" + MySQL.fc(squad) + MySQL.fc(chunk) + safe + ")");
	}

	public static void unclaim(String squad, String chunk) {
		if(!isClaimed(chunk)) {
			return;
		}
		claimMap.remove(chunk);
		MySQL.doUpdate("DELETE FROM squad_claims WHERE chunk=" + MySQL.f(chunk));
	}

	public static String getOwner(String chunk) {
		if(!isClaimed(chunk)) {
			return "None";
		}
		return claimMap.get(chunk).owner;
	}

	public static void setRank(String player, Rank rank) {
		if(!hasSquad(player)) {
			return;
		}
		if(rankMap.containsKey(player)) {
			rankMap.remove(player);
		}
		rankMap.put(player, rank);
		MySQL.doUpdate("UPDATE squad_player SET role=" + MySQL.f(rank.toString()) + " WHERE player=" + MySQL.f(player));
	}

	public static void promote(String player) {
		if(getRank(player) == Rank.LEADER) {
			return;
		}
		if(getRank(player) == Rank.ADMIN) {
			return;
		}
		setRank(player, Rank.ADMIN);
	}

	public static void demote(String player) {
		if(getRank(player) == Rank.LEADER) {
			return;
		}
		if(getRank(player) == Rank.MEMBER) {
			return;
		}
		setRank(player, Rank.MEMBER);
	}

	public static String searchSquadByPlayer(String search) {
		if(memberMap.containsKey(search)) {
			return Squads.getSquad(search);
		}
		for(String keys : memberMap.keySet()) {
			if(keys.contains(search)) {
				return memberMap.get(keys);
			}
		}
		return null;
	}

	public static hSquad getSquadByName(String name) {
		if(!squadMap.containsKey(name)) return null;
		return squadMap.get(name);
	}
	
	public static void addPower(String squad, int amount) {
		Squads.squadMap.get(squad).power = Squads.squadMap.get(squad).power + amount;
	}

	public static void subtractPower(String squad, int amount) {
		Squads.squadMap.get(squad).power = Squads.squadMap.get(squad).power - amount;
	}


	public static void sendSquadDetails(Player player, String squad) {
		if(!squadMap.containsKey(squad)) {
			player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Squad " + ChatColor.YELLOW + squad + ChatColor.WHITE + " doesn't exist");
			return;
		}
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Squad " + squad + ChatColor.WHITE + " - " + Squads.memberOnlineColour(squad));
		player.sendMessage(ChatColor.YELLOW + "Age [" + ChatColor.GREEN + getAge(squad) + ChatColor.YELLOW + "] " + getDate(squad));
		player.sendMessage(ChatColor.YELLOW + "Members [" + ChatColor.GREEN + Squads.getMemberSize(squad) + ChatColor.YELLOW + "] " + Squads.getMembers(squad));
		player.sendMessage(ChatColor.YELLOW + "Allies [" + ChatColor.GREEN + Squads.getAllySize(squad) + ChatColor.YELLOW + "/" + ChatColor.GREEN + Squads.getMaxAllies(squad) + ChatColor.YELLOW + "] " + Squads.getAllies(squad));
		player.sendMessage(ChatColor.YELLOW + "Enemies [" + ChatColor.GREEN + Squads.getEnemySize(squad) + ChatColor.YELLOW + "] " + Squads.getEnemies(squad));
		player.sendMessage(ChatColor.YELLOW + "Power [" + ChatColor.GREEN + Squads.getPower(squad) + ChatColor.YELLOW + "/" + ChatColor.GREEN + Squads.getMaxPower(squad) + ChatColor.YELLOW + "]");
		player.sendMessage(ChatColor.YELLOW + "Land [" + ChatColor.GREEN + Squads.getLand(squad) + ChatColor.YELLOW + "/" + ChatColor.GREEN + Squads.getMaxLand(squad) + ChatColor.YELLOW + "]");
	}
	
	public static void sendNewDetails(Player player, String squad) {
		if(player == null) return;
		if(!squadMap.containsKey(squad)) {
			player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Squad " + ChatColor.YELLOW + squad + ChatColor.WHITE + " doesn't exist");
			return;
		}
	}

	public static void sendSquadList(Player player) {
		String squads = "";
		int count = 0;
		for(String keys : squadMap.keySet()) {
			if(count > 99) break;
			squads = squads + ChatColor.YELLOW + keys + ", ";
			count++;
		}
		player.sendMessage(ChatColor.BLUE + "Squads List - " + ChatColor.WHITE + "1 - 100");
		player.sendMessage(squads + "[" + ChatColor.GREEN + count + ChatColor.YELLOW + "]"); 
	}
	
	public static String getAge(String squad) {
		if(squadMap.containsKey(squad)) {
			String conversion = utilTime.convertString((System.currentTimeMillis() - squadMap.get(squad).systime), TimeUnit.BEST, 1);
			return conversion;
		}
		return "N/A";
	}
	
	public static String getDate(String squad) {
		if(squadMap.containsKey(squad)) return squadMap.get(squad).date;
		return "N/A";
	}

	public static boolean hasLeader(String squad) {
		if(!memberMap.containsValue(squad)) {
			return false;
		}
		LinkedList<String> members = new LinkedList<String>();
		for(String keys : memberMap.keySet()) {
			if(memberMap.get(keys).equals(squad)) members.add(keys);
		}
		for(String member : members) {
			if(rankMap.get(member) == Rank.LEADER) return true;
			return false;
		}
		return false;
	}

	public static double getRemaining(String squad) {
		if(!regenMap.containsKey(squad)) return (Double) null;
		return utilTime.convert(regenMap.get(squad) + powerUpdate - System.currentTimeMillis(), TimeUnit.BEST, 1);
	}

	public static void setHome(String squad, Location loc) {
		if(!isClaimed(utilWorld.chunkToStr(loc.getChunk()))) return;
		if(!squadMap.containsKey(squad)) return;
		if(loc == null || squad == null) return;
		if(utilServer.isInSpawn(loc)) return;
		if(Squads.isClaimed(utilWorld.chunkToStr(loc.getWorld().getChunkAt(loc)))) {
			if(!getOwnerChunk(loc.getWorld().getChunkAt(loc)).equals(squad)) return;
		}
		if(homeLocMap.containsKey(squad)) {
			homeLocMap.remove(squad);
			MySQL.doUpdate("UPDATE `squad_home` SET `x`=" + loc.getX() + ",`y`=" + loc.getY() + ",`z`=" + loc.getZ() + ",`yaw`=" + loc.getYaw() + ",`world`=" + MySQL.f(loc.getWorld().getName()) + " WHERE squad=" + MySQL.f(squad));
			homeLocMap.put(squad, new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0.0F));
			return;
		}
		homeLocMap.put(squad, new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0.0F));
		MySQL.doUpdate("INSERT INTO `squad_home`(`squad`, `x`, `y`, `z`, `yaw`, `world`) VALUES (" + MySQL.fc(squad) + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getYaw() + ", " + MySQL.f(loc.getWorld().getName()) + ")");
	}

	public static void goHome(Player player, String squad) {
		if(!squadMap.containsKey(squad)) return;
		if(!utilServer.isInNewSpawn(player)) return;
		if(!homeLocMap.containsKey(squad)) {
			player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You have not set a " + ChatColor.YELLOW + "Home");
			return;
		}
		Location home = homeLocMap.get(squad);
		player.teleport(home);
		player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You teleported " + ChatColor.YELLOW + "Home");
	}
	
	public static Location getHome(String squad) {
		if(!squadMap.containsKey(squad)) return null;
		if(!homeLocMap.containsKey(squad)) return null;
		return homeLocMap.get(squad);
	}

	public static void unally(String squadA, String squadB) {
		if(!squadMap.containsKey(squadA) || !squadMap.containsKey(squadB)) return;
		if(!areAllies(squadA, squadB)) return;
		squadMap.get(squadA).allyMap.remove(squadB);
		squadMap.get(squadB).allyMap.remove(squadA);
		MySQL.doUpdate("DELETE FROM squad_ally WHERE squad=" + MySQL.f(squadA) + " AND ally=" + MySQL.f(squadB));
		MySQL.doUpdate("DELETE FROM squad_ally WHERE squad=" + MySQL.f(squadB) + " AND ally=" + MySQL.f(squadA));
		Squads.sendMembersMsg(squadB, ChatColor.YELLOW + squadA + ChatColor.WHITE + " un-allied you");
	}

	public static void stealLand(String squad, String squadB, Chunk chunk) {
		if(!squadMap.containsKey(squad) || !squadMap.containsKey(squadB)) return;
		if(chunk == null) return;
		if(getLand(squad) >= getMaxLand(squad)) return;
		if(getPower(squad) < 1 || getPower(squadB) > 0) return;
		if(!getOwner(utilWorld.chunkToStr(chunk)).equalsIgnoreCase(squadB)) return;
		unclaim(squadB, utilWorld.chunkToStr(chunk));
		claim(squad, utilWorld.chunkToStr(chunk), false);
		addPower(squadB, 1);
	}

	public static String searchSquad(String search) {
		Player target = Bukkit.getPlayer(search);
		if(target != null) {
			if(!hasSquad(target.getName())) return "None";
			return getSquad(target.getName());
		}
		String memberMatch = "None";
		LinkedList<String> memberList = new LinkedList<String>();
		for(String members : memberMap.keySet()) {
			if(members.contains(search)) memberList.add(members);
		}
		if(memberList.size() == 0) {}
		if(memberList.size() == 1) return getSquad(memberList.getFirst());
		for(String members : memberList) {
			int chars = 0;
			if(members.length() > chars) {
				chars = members.length();
				memberMatch = members;
			}
		}
		if(!memberMatch.equalsIgnoreCase("None")) if(hasSquad(memberMatch)) return getSquad(memberMatch);
		LinkedList<String> matchList = new LinkedList<String>();
		String curMatch = "None";
		for(String squads : squadMap.keySet()) {
			if(squads.contains(squads)) matchList.add(squads);
		}
		if(matchList.size() == 0) return "None";
		if(matchList.size() == 1) return matchList.getFirst();
		for(String matches : matchList) {
			int chars = 0;
			if(matches.length() > chars) {
				curMatch = matches;
				chars = matches.length();
			}
		}
		return curMatch;
	}

	public static String getRelationColour(String squad, String squadB) {
		if(areEnemies(squad, squadB)) return ChatColor.DARK_RED + squad + ChatColor.RED;
		if(areAllies(squad, squadB)) return ChatColor.DARK_GREEN + squad + ChatColor.GREEN;
		return ChatColor.GOLD + squad + ChatColor.YELLOW;
	}
	
	public static boolean canDamage(String damager, String damagee) {
		if(!hasSquad(damager) || !hasSquad(damagee)) return true;
		String squad = getSquad(damager);
		String target = getSquad(damagee);
		if(squad.equalsIgnoreCase(target)) return false;
		if(areAllies(squad, target)) return false;
		return true;
	}
	
	public static boolean isAdminMode(Player player) { return squadAdmin.contains(player.getName()); }
	
	public static void playerAdmin(Player player) {
		if(Permissions.getRank(player) != Ranks.OWNER) {
			utilPlayer.permMsg(player);
			return;
		}
		if(squadAdmin.contains(player.getName())) {
			squadAdmin.remove(player.getName());
			player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are now in " + ChatColor.YELLOW + "Regular Mode");
			return;
		}
		squadAdmin.add(player.getName());
		player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are now in " + ChatColor.YELLOW + "Squad Mode");
		return;
	}
	
	public static boolean checkFaulty(String squad) {
		if(searchSquad(squad).equalsIgnoreCase("None")) {
			if(squadMap.containsKey(squad)) {
				squadMap.get(squad).allyMap.clear();
				squadMap.get(squad).enemyMap.clear();
				squadMap.remove(squad);
				MySQL.doUpdate("DELETE FROM squad WHERE squad=" + MySQL.f(squad));
				MySQL.doUpdate("DELETE FROM squad_player WHERE squad=" + MySQL.f(squad));
				MySQL.doUpdate("DELETE FROM squad_ally WHERE squad=" + MySQL.f(squad) + " OR ally=" + MySQL.f(squad));
				MySQL.doUpdate("DELETE FROM squad_enemy WHERE squad=" + MySQL.f(squad) + " OR enemy=" + MySQL.f(squad));
				MySQL.doUpdate("DELETE FROM squad_home WHERE squad=" + MySQL.f(squad));
			}
			for(String members : memberMap.keySet()) {
				if(memberMap.get(members).equalsIgnoreCase(squad)) memberMap.remove(members);
				if(rankMap.containsKey(members)) {
					if(getSquad(members).equalsIgnoreCase(squad)) rankMap.remove(members);
				}
			}
			unclaimAll(squad);
			clearInvites(squad);
			if(regenMap.containsKey(squad)) regenMap.remove(squad);
			if(homeLocMap.containsKey(squad)) homeLocMap.remove(squad);
			System.out.println("Squads - Removed faulty squad [" + squad + "]");
			return true;
		}
		return false;
	}
	
	public static void disbandCheck(String squad) {
		memberMap.values().removeAll(Collections.singleton(squad));
		removeAllies(squad);
		removeEnemies(squad);
		unclaimAll(squad);
		if(homeLocMap.containsKey(squad)) homeLocMap.remove(squad);
		if(squadMap.containsKey(squad)) squadMap.remove(squad);
		MySQL.doUpdate("DELETE FROM squad WHERE squad=" + MySQL.f(squad));
		MySQL.doUpdate("DELETE FROM squad_player WHERE squad=" + MySQL.f(squad));
		MySQL.doUpdate("DELETE FROM squad_ally WHERE squad=" + MySQL.f(squad) + " OR ally=" + MySQL.f(squad));
		MySQL.doUpdate("DELETE FROM squad_enemy WHERE squad=" + MySQL.f(squad) + " OR enemy=" + MySQL.f(squad));
		MySQL.doUpdate("DELETE FROM squad_home WHERE squad=" + MySQL.f(squad));
	}
	
	public static void sendMap(Player player) {
		if(player == null) return;
		String[] mapArray = new String[5];
		if(!hasSquad(player.getName())) {
			for(int i = 0; i < 5; i++) {
				for(int x = player.getLocation().getChunk().getX() + i; x < 20 + i; x++) {
					for(int z = player.getLocation().getChunk().getZ() + i; z < 20 + i; z++) {
						if(!isClaimed(utilWorld.chunkToStr(player.getWorld().getChunkAt(x, z)))) mapArray[i] = mapArray[i] + "-";
						else mapArray[i] = mapArray[i] + "+";
					}
				}
			}
		}
	}

	public static void startSaveThread() {
		plugin = Hype.plugin;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Saving data...");
				long run = System.currentTimeMillis();
				save();
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Data Save - Took " + (System.currentTimeMillis() - run) + " millis");
			}
		}, 72000L, 72000L);
	}
	
	public static void handlePower() {
		if(regenMap.isEmpty()) {
			return;
		}
		for(String squads : regenMap.keySet()) {
			try {
			if(!squadMap.containsKey(squads)) {
				regenMap.remove(squads);
				return;
			}
			if(getPower(squads) == 12345) {
				regenMap.remove(squads);
				return;
			}
			if(getPower(squads) >= getMaxPower(squads)) {
				regenMap.remove(squads);
				return;
			}
			if(getRemaining(squads) <= 0) {
				addPower(squads, 1);
				sendMembersMsg(squads, ChatColor.WHITE + "Power " + ChatColor.GREEN + "+1");
				}
			} catch (NullPointerException e) {
				checkFaulty(squads);
			}
		}
	}

	public static boolean disallowInteract(int id) {
		if(denyInteract.isEmpty()) {

			denyInteract.add(Integer.valueOf(23));
			denyInteract.add(Integer.valueOf(26));
			denyInteract.add(Integer.valueOf(54));
			denyInteract.add(Integer.valueOf(61));
			denyInteract.add(Integer.valueOf(62));
			denyInteract.add(Integer.valueOf(64));

			denyInteract.add(Integer.valueOf(71));

			denyInteract.add(Integer.valueOf(93));
			denyInteract.add(Integer.valueOf(94));
			denyInteract.add(Integer.valueOf(96));
			denyInteract.add(Integer.valueOf(107));
		}
		return denyInteract.contains(Integer.valueOf(id));
	}

	public static boolean allowInteract(int id) {
		if(allowInteract.isEmpty()) {
			allowInteract.add(Integer.valueOf(64));

			allowInteract.add(Integer.valueOf(71));

			allowInteract.add(Integer.valueOf(96));
			allowInteract.add(Integer.valueOf(107));
		}
		return allowInteract.contains(Integer.valueOf(id));
	}

	public static boolean denyUsePlace(int id) {
		if(denyUsePlace.isEmpty()) {
			denyUsePlace.add(Integer.valueOf(259));
			denyUsePlace.add(Integer.valueOf(259));
			denyUsePlace.add(Integer.valueOf(321));
			denyUsePlace.add(Integer.valueOf(323));
			denyUsePlace.add(Integer.valueOf(324));
			denyUsePlace.add(Integer.valueOf(326));
			denyUsePlace.add(Integer.valueOf(327));
			denyUsePlace.add(Integer.valueOf(330));
			denyUsePlace.add(Integer.valueOf(331));
			denyUsePlace.add(Integer.valueOf(333));
			denyUsePlace.add(Integer.valueOf(355));
			denyUsePlace.add(Integer.valueOf(356));
			denyUsePlace.add(Integer.valueOf(379));
			denyUsePlace.add(Integer.valueOf(380));
		}
		if ((id > 0) && (id < 256)) {
			return true;
		}
		return denyUsePlace.contains(Integer.valueOf(id));
	}

	public static boolean allowUsePlace(int id) {
		if(allowUsePlace.isEmpty()) {
			allowUsePlace.add(Integer.valueOf(37));
			allowUsePlace.add(Integer.valueOf(38));
		}

		return allowUsePlace.contains(Integer.valueOf(id));
	}

	public static boolean canBreak(int id) {
		if(allowBreak.isEmpty()) {
			allowBreak.add(Integer.valueOf(6));
			allowBreak.add(Integer.valueOf(12));
			allowBreak.add(Integer.valueOf(13));
			allowBreak.add(Integer.valueOf(39));
			allowBreak.add(Integer.valueOf(40));
			allowBreak.add(Integer.valueOf(59));
			allowBreak.add(Integer.valueOf(81));
			allowBreak.add(Integer.valueOf(83));
			allowBreak.add(Integer.valueOf(86));
			allowBreak.add(Integer.valueOf(103));
			allowBreak.add(Integer.valueOf(104));
			allowBreak.add(Integer.valueOf(105));
			allowBreak.add(Integer.valueOf(115));
			allowBreak.add(Integer.valueOf(127));
		}
		return allowBreak.contains(Integer.valueOf(id));
	}

}
