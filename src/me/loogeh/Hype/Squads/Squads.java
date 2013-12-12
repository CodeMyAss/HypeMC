package me.loogeh.Hype.Squads;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.Squads.hSquad.Rank;
import me.loogeh.Hype.util.utilTime;
import me.loogeh.Hype.util.utilWorld;

public class Squads {
	public static Hype plugin;

	public static HashMap<String, hSquad> squadMap = new HashMap<String, hSquad>();
	public static HashMap<String, Claim> claimMap = new HashMap<String, Claim>();

	public static HashMap<String, squadPlayer> playerMap = new HashMap<String, squadPlayer>(); //Stores Squad - squadPlayer
	public static HashMap<String, String> allyMap = new HashMap<String, String>(); //Stores Squad - Ally
	public static HashMap<String, String> trustMap = new HashMap<String, String>(); //Stores Squad - Trusted
	public static HashMap<String, String> enemyMap = new HashMap<String, String>(); //Stores Squad - Enemy

	public static HashSet<Integer> denyInteract = new HashSet<Integer>();
	public static HashSet<Integer> allowInteract = new HashSet<Integer>();

	public static HashSet<Integer> denyUsePlace = new HashSet<Integer>();
	public static HashSet<Integer> allowUsePlace = new HashSet<Integer>();

	public static HashSet<Integer> allowBreak = new HashSet<Integer>();

	public static HashSet<Region> regionSet = new HashSet<Region>();

	public long lastPower = System.currentTimeMillis();

	public int nameMin = 3;
	public int nameMax = 10;
	public long powerTime = 300000L;



	public void updatePower() {
		HashSet<hSquad> set = new HashSet<hSquad>();

		for(Player players : Bukkit.getServer().getOnlinePlayers()) {
			hSquad squad = utilSquads.getSquadP(players.getName());

			if(squad != null) {
				if(squad.getPower() == squad.getPowerMax()) {
					squad.powerTime = 0L;
				} else if(!set.contains(squad)) {
					if(!utilSquads.isSafe(players)) {
						if(!players.isDead()) {
							squad.powerTime += System.currentTimeMillis() - this.lastPower;

							if(squad.powerTime > this.powerTime) {
								squad.powerTime = 0L;
								squad.changePower(1);
							}
							set.add(squad);
						}
					}
				}
			}
		}
		this.lastPower = System.currentTimeMillis();
	}

	public static void load() {
		
	}

	public static void save() {

	}


	public static void createSquad(String name, Player creator, boolean admin) {
		if(hasSquad(creator)) {
			creator.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "You are already in " + ChatColor.WHITE + "[" + ChatColor.YELLOW + playerMap.get(creator.getName()) + ChatColor.WHITE + "]");
			return;
		} else {
			if(squadMap.containsKey(name)) {
				creator.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "The squad " + ChatColor.WHITE + "[" + ChatColor.YELLOW + name + ChatColor.WHITE + "]" + ChatColor.GRAY + " already exists");
				return;
			} else {
				if(!admin) {
					squadMap.put(name, new hSquad(name, 3, "", false, utilTime.timeStr(), System.currentTimeMillis()));
					playerMap.put(creator.getName(), new squadPlayer(creator.getName(), name, Rank.LEADER));
					MySQL.doUpdate("INSERT INTO `squad`(`squad`, `desc`, `power`, `home`, `admin`, `date`, `systime`, `lastPower`) VALUES ('" + name  + "','Default', 3, 'null', 0,'" + utilTime.timeStr() + "'," + System.currentTimeMillis() + ", 0)");
					creator.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "You created " + ChatColor.WHITE + "[" + ChatColor.YELLOW + name + ChatColor.WHITE + "]");
				} else {
					squadMap.put(name, new hSquad(name, 10000, "", true, utilTime.timeStr(), System.currentTimeMillis()));
					playerMap.put(creator.getName(), new squadPlayer(creator.getName(), name, Rank.LEADER));
					MySQL.doUpdate("INSERT INTO `squad`(`squad`, `desc`, `power`, `home`, `admin`, `date`, `systime`, `lastPower`) VALUES ('" + name  + "','Default', 1000, 'null', 1,'" + utilTime.timeStr() + "'," + System.currentTimeMillis() + ", 0)");
					creator.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "You created admin squad" + ChatColor.WHITE + "[" + ChatColor.YELLOW + name + ChatColor.WHITE + "]");
				}
			}
		}
	}

	public static hSquad getSquad(String squad) {
		return (hSquad)squadMap.get(squad);
	}

	public static void addPower(String squad, int amount) {
		ResultSet rs = MySQL.doQuery("SELECT squad FROM squad WHERE squad='" + squad + "'");
		try {
			if(rs.next()) {
				MySQL.doUpdate("UPDATE squad SET power=power+" + amount + " WHERE squad='" + squad + "'");
			} else {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void subPower(String squad, int amount) {
		ResultSet rs = MySQL.doQuery("SELECT squad FROM squad WHERE squad='" + squad + "'");
		try {
			if(rs.next()) {
				MySQL.doUpdate("UPDATE squad SET power=power-" + amount + " WHERE squad='" + squad + "'");
			} else {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setPower(String squad, int amount) {
		ResultSet rs = MySQL.doQuery("SELECT squad FROM squad WHERE squad='" + squad + "'");
		try {
			if(rs.next()) {
				MySQL.doUpdate("UPDATE squad SET power=" + amount + " WHERE squad='" + squad + "'");
			} else {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void claimLand(String squad) {

	}

	public static boolean isClaimed(Chunk chunk) {
		return false;

	}

	public static void disbandSquad(String name) {

	}

	public static boolean hasSquad(Player player) {
		return (playerMap.containsKey(player.getName()));
	}


	public static squadPlayer getCurrentSquad(Player player) {
		if(playerMap.containsKey(player.getName())) {
			return playerMap.get(player.getName());
		} else {
			return null;
		}
	}

	public static boolean isAdmin(String squad) {
		ResultSet rs = MySQL.doQuery("SELECT admin FROM squad WHERE squad='" + squad + "'");
		try {
			if(rs.next()) {
				if(rs.getBoolean(1) == true) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public static int getMaxPower(String squad) {
		int maxPwr = 2;
//				for(String squads : playerMap.get) {
//					if(squads.equals(squad)) {
//						maxPwr++;
//					}
//
//				}
		return maxPwr;
	}

	public static void leaveSquad(String squad, Player player) {

	}

	public static hSquad.Rank getRank(Player player) {
		String role = "none";
		ResultSet rs = MySQL.doQuery("SELECT role FROM squad_player WHERE player='" + player.getName() + "'");
		try {
			if(rs.next()) {
				role = rs.getString(1);
			} else {
				role = "none";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(role.equalsIgnoreCase("leader")) return hSquad.Rank.LEADER;
		else if(role.equalsIgnoreCase("admin")) return hSquad.Rank.ADMIN;
		else if(role.equalsIgnoreCase("member")) return hSquad.Rank.MEMBER;
		else return hSquad.Rank.NONE;

	}

	//	public static int getClaims(String squad) {
	//		int claims = 0;
	//		for(Entry<String, Chunk> squadClaims : claimMap.entrySet()) {
	//			if(squadClaims.getKey().equals(squad)) {
	//				claims++;
	//			}
	//		}
	//		return claims;
	//	}
	//
	//	public static int getClaimsMax(String squad) {
	//		int claimsMax = 2;
	//		for(String squads : squadMap.values()) {
	//			if(squads.equals(squad)) {
	//				claimsMax++;
	//			}
	//		}
	//		return claimsMax;
	//	}

	public static int getPower(String squad) {
		ResultSet rs = MySQL.doQuery("SELECT power FROM squad WHERE squad='" + squad + "'");
		int power = 0;
		try {
			if(rs.next()) {
				power = rs.getInt(1);
			} else {
				return (Integer) null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return power;
	}

	//	public static boolean isAlly(String squad, String with) {
	//		if(isEnemy(squad, with)) return false;
	//		if(allyMap.get(squad).equals(with)) return true;
	//		else return false;
	//			
	//	}

	//	public static boolean isEnemy(String squad, String with) {
	//		if(isAlly(squad, with)) return false;
	//		for(Entry<String, String> enemyEntry : enemyMap.entrySet()) {
	//		}
	//	}

	public static void sendSquadInfo(String squad, Player player) {
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Squad " + squad);
		player.sendMessage(ChatColor.GOLD + "Power - " + ChatColor.WHITE + getPower(squad) + "/" + getMaxPower(squad));
		player.sendMessage(ChatColor.GOLD + "Allies - ");
		player.sendMessage(ChatColor.GOLD + "Enemies - " );
		//		player.sendMessage(ChatColor.GOLD + "Land - " + ChatColor.WHITE + getLand(squad) + "/" + getMaxLand(squad));
	}

	public static int getLand(String squad) {
		return 0;

	}

	//	public static int getMaxLand(String squad) {
	//		return 2 + getMemberCount(squad);
	//		
	//	}

	//	public static int getMemberCount(String squad) {
	//		int memberCount = 0;
	//		for(String squads : squadMap.values()) {
	//			if(squads.equals(squad)) {
	//				memberCount++;
	//			}
	//		}
	//		return memberCount;
	//	}


	@EventHandler(priority=EventPriority.LOWEST)
	public void squadBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled()) {
			return;
		} else {

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

	public static boolean isAlly(String squad) {
		return Boolean.valueOf(allyMap.get(squad));
	}
	
	public static void deleteSquad(hSquad squad) {
//		for(String claims : squad.claimSet) {
//			claimMap.remove(claims);
//		}
//		squadMap.remove(squad.name);
//		
//		for(String members : squad.memberMap.keySet()) {
//		}
	}
	
	public static void join(String player, String squad) {
		
	}
	
	public static void leave(String player, String squad) {
		
	}
	
	public static void rank(String player, Rank rank) {
		
	}
	
	public static void invite(hSquad squad, String player, String inviter) {
		
	}
	
	public static void requestAlly(hSquad squad, hSquad target, String player) {
		
	}
	
	public static void ally(hSquad sA, hSquad sB, String player) {
		
	}
	
	public static boolean trust(hSquad sA, hSquad sB, String player) {
		return false;
		
	}
	
	public static void enemy(hSquad sA, hSquad sB, String player) {
		
	}
	
	@SuppressWarnings("deprecation")
	public static boolean claim(String squad, String chunk, String player, boolean safe) {
		if(!Squads.squadMap.containsKey(squad)) {
			return false;
		}
		Claim claim = new Claim(squad, chunk, safe);
		((hSquad)Squads.squadMap.get(squad)).claimSet.add(chunk);
		Squads.claimMap.put(chunk, claim);
		
		sDB.insertTerritory(squad, chunk, safe);
		
		Chunk c = utilWorld.strToChunk(chunk);
		if(!((hSquad)squadMap.get(squad)).admin) {
			for(int i = 0; i < 3; i++) {
				for(int x = 0; x < 16; x++) {
					for(int z = 0; z < 16; z++) {
						if((z == 0) || (z == 15) || (x == 0) || (x == 15)) {
							Block down = c.getWorld().getHighestBlockAt(c.getBlock(x, 0, z).getLocation()).getRelative(BlockFace.DOWN);
							
							if((down.getTypeId() == 1) || (down.getTypeId() == 2) || (down.getTypeId() == 3) || (down.getTypeId() == 12) || (down.getTypeId() == 8)) {
								down.setTypeIdAndData(5, (byte)0, false);
							}
						}
					}
				}
			}
		}
		System.out.println("Added claim for '" + squad + "' at '" + chunk + "' by '" + player + "'");
		return true;
		
	}
	
	public static boolean unclaim(String chunk, String player, boolean sql) {
		Claim claim = (Claim) claimMap.remove(chunk);
		
		if(claim == null) {
			return false;
		}
		
		hSquad squad = (hSquad)squadMap.get(claim.owner);
		
		if(squad == null) {
			return false;
		}
		
		squad.claimSet.remove(chunk);
		
		if(sql) {
			sDB.deleteTerritory(chunk);
		}
		
		return true;
	}
	
	public static void setHome(hSquad squad, Location loc, String player) {
		squad.home = loc;
		squad.write();
	}
	
	public static void safe(Claim claim, String player) {
		claim.safe = (!claim.safe);
		sDB.insertTerritory(claim.owner, claim.chunk, claim.safe);
		
	}
	

}
