package me.loogeh.Hype.games;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.apache.commons.lang3.text.WordUtils;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.GameMode;
//import org.bukkit.Location;
//import org.bukkit.entity.Player;
//import org.bukkit.event.entity.PlayerDeathEvent;
//import org.bukkit.scoreboard.DisplaySlot;
//import org.bukkit.scoreboard.Objective;
//import org.bukkit.scoreboard.Scoreboard;
//import org.bukkit.scoreboard.ScoreboardManager;
//import org.bukkit.scoreboard.Team;
//
//import me.loogeh.Hype.Hype;
//import me.loogeh.Hype.SQL.MySQL;
//import me.loogeh.Hype.economy.Money;
//import me.loogeh.Hype.games.Leaderboards.LType;
//import me.loogeh.Hype.games.Leaderboards.Leaderboard;
//import me.loogeh.Hype.util.Utilities;
//import me.loogeh.Hype.util.utilInv;
//import me.loogeh.Hype.util.utilMath;
//import me.loogeh.Hype.util.utilPlayer;
//
public class Arena {
//	public static Hype plugin;
//	public static double[] coords = {841.38, 64.00, 387.55851, 779.70, 47.2, 342.70};
//	public static double[] blueLobbyCoords = {789.80, 37.00, 383.00};
//	public static double[] redLobbyCoords = {785.10, 37.00, 383.00};
//	public static double[] redGameSpawn = {840.00, 39.00, 378.00};
//	public static double[] blueGameSpawn = {783.20, 50.00, 344.46};
//	public static double[] redSpectate = {799.30, 52.00, 383.55};
//	public static double[] blueSpectate = {828.60, 52.00, 383.70};
//	public static int redSpawnYaw = 133;
//	public static int blueSpawnYaw = -59;
//	public static boolean inLobby = false;
//	public static boolean inGame = false;
//	public static ArrayList<String> redTeam = new ArrayList<String>();
//	public static ArrayList<String> blueTeam = new ArrayList<String>();
//	public static ArrayList<String> allRedTeam = new ArrayList<String>();
//	public static ArrayList<String> allBlueTeam = new ArrayList<String>();
//	public static int countdown = 60;
//	public static int rPlayers;
//	public static int bPlayers;
//	public static HashMap<String, ArenaPlayer> playerMap = new HashMap<String, ArenaPlayer>();
//	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
//	public static Scoreboard board = manager.getNewScoreboard();
//
//	public static String[] ranks = {"Private", "Lance Corporal", "Corporal", "Sergeant", "Staff Sergeant", "Master Sergeant", "Second Lieutenant", "First Lieutenant", "Captain", "Major", "Lieutenant Colonel", "Colonel", "Brigadier General", "Major General", "Lieutenant General", "General", "Commander"};
//	public static int[] rankNumber = {0, 1000, 2200, 3800, 6000, 10000, 15000, 21000, 29000, 40000, 55000, 71000, 87000, 103000, 120000, 140000, 170000};
//	
////	
//
//	public static void load() {
//		ResultSet rs = MySQL.doQuery("SELECT player,kills,deaths,exp,rank,wins,losses,score FROM arena");
//		try {
//			//strToRank(rs.getString(5))
//			while(rs.next()) {
//				if(!playerMap.containsKey(rs.getString(1))) {
//					playerMap.put(rs.getString(1), new ArenaPlayer(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), strToRank(rs.getString(5)), rs.getInt(6), rs.getInt(7), rs.getInt(8)));
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.out.println("Arena - Error while loading stats to playerMap");
//			return;
//		}
//		System.out.println("Arena - Loaded");
//	}
//
//	public static void save() {
//		for(String keys : playerMap.keySet()) {
//			ResultSet rs = MySQL.doQuery("SELECT player FROM arena WHERE player=" + MySQL.f(keys));
//			try {
//				if(rs.next()) {
//					MySQL.doUpdate("UPDATE `arena` SET `kills`=" + playerMap.get(keys).kills + ",`deaths`=" + playerMap.get(keys).deaths + ",`exp`=" + playerMap.get(keys).exp + ",`rank`=" + MySQL.f(rankToString(playerMap.get(keys).rank)) + ",`wins`=" + playerMap.get(keys).wins + ",`losses`=" + playerMap.get(keys).losses + ", `score`=" + playerMap.get(keys).score + " WHERE player=" + MySQL.f(keys));
//				} else {
//					MySQL.doUpdate("INSERT INTO `arena`(`player`, `kills`, `deaths`, `exp`, `rank`, `wins`, `losses`, `score`) VALUES (" + MySQL.fc(keys) + playerMap.get(keys).kills + ", " + playerMap.get(keys).deaths + ", " + playerMap.get(keys).exp + ", " + MySQL.fc(rankToString(playerMap.get(keys).rank)) + playerMap.get(keys).wins + "," + playerMap.get(keys).losses + ", " + playerMap.get(keys).score + ")");
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				System.out.println("Arena - Error while saving stats from playerMap @ [" + keys + "]");
//				return;
//			}
//		}
//		System.out.println("Arena [playerMap] saved to database");
//	}
//
//	public static void initialize() {
//		if(inGame) {
//			return;
//		}
//		if(inLobby) {
//			return;
//		}
//		if(inGame == true) {
//			return;
//		}
//		inGame = false;
//		inLobby = true;
//		broadcast(ChatColor.WHITE + "Joining enabled");
//		broadcast(ChatColor.WHITE + "Type /arena join");
//		broadcast(ChatColor.WHITE + "Price " + ChatColor.DARK_GREEN + "$1000");
//	}
//
//	public static void assignTeam(Player player) {
//		if(hasTeam(player)) {
//			privMessage(player, "You are already in a team");
//			return;
//		}
//		if(inGame == true) {
//			privMessage(player, "Game has already started");
//			return;
//		}
//		if(!inLobby) {
//			privMessage(player, "Arena not enabled");
//			return;
//		}
//		utilInv.saveContents(player);
//		if(redTeam.isEmpty() && (blueTeam.isEmpty())) {
//			redTeam.add(player.getName());
//			allRedTeam.add(player.getName());
//			privMessage(player, "You joined team " + ChatColor.RED + "Red" + ChatColor.WHITE + "(" + ChatColor.YELLOW + redTeam.size() + ChatColor.WHITE + ")");
//			utilPlayer.clearInvent(player);
//			utilPlayer.arenaInv(player);
//			utilPlayer.arenaRed(player);
//			lobbyAreaRed(player);
//			player.setHealth(20);
//			player.setFireTicks(0);
//			player.setFoodLevel(20);
//			startTimer(player);
//			if(player.getGameMode() != GameMode.SURVIVAL) player.setGameMode(GameMode.SURVIVAL);
//		} else if(redTeam.size() > blueTeam.size()) {
//			blueTeam.add(player.getName());
//			allBlueTeam.add(player.getName());
//			privMessage(player, "You joined team " + ChatColor.BLUE + "Blue" + ChatColor.WHITE + "(" + ChatColor.YELLOW + blueTeam.size() + ChatColor.WHITE + ")");
//			utilPlayer.clearInvent(player);
//			utilPlayer.arenaInv(player);
//			utilPlayer.arenaBlue(player);
//			player.setHealth(20);
//			player.setFireTicks(0);
//			player.setFoodLevel(20);
//			lobbyAreaBlue(player);
//			if(player.getGameMode() != GameMode.SURVIVAL) player.setGameMode(GameMode.SURVIVAL);
//		} else if(redTeam.size() < blueTeam.size()) {
//			redTeam.add(player.getName());
//			allRedTeam.add(player.getName());
//			privMessage(player, "You joined team " + ChatColor.RED + "Red" + ChatColor.WHITE + "(" + ChatColor.YELLOW + redTeam.size() + ChatColor.WHITE + ")");
//			utilPlayer.clearInvent(player);
//			utilPlayer.arenaInv(player);
//			utilPlayer.arenaRed(player);
//			player.setHealth(20);
//			player.setFireTicks(0);
//			player.setFoodLevel(20);
//			lobbyAreaRed(player);
//			if(player.getGameMode() != GameMode.SURVIVAL) player.setGameMode(GameMode.SURVIVAL);
//		} else if(redTeam.size() == blueTeam.size()) {
//			redTeam.add(player.getName());
//			allRedTeam.add(player.getName());
//			privMessage(player, "You joined team " + ChatColor.RED + "Red" + ChatColor.WHITE + "(" + ChatColor.YELLOW + redTeam.size() + ChatColor.WHITE + ")");
//			utilInv.saveContents(player);
//			utilPlayer.clearInvent(player);
//			utilPlayer.arenaInv(player);
//			utilPlayer.arenaRed(player);
//			player.setHealth(20);
//			player.setFireTicks(0);
//			player.setFoodLevel(20);
//			lobbyAreaRed(player);
//			if(player.getGameMode() != GameMode.SURVIVAL) player.setGameMode(GameMode.SURVIVAL);
//		}
//	}
//
//
//	public static void startTimer(final Player player) {
//		broadcast(ChatColor.YELLOW + "Arena will start in 1 minute");
//		plugin = Hype.plugin;
//		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//
//			@Override
//			public void run() {
//				if((redTeam.size() == 0) || (blueTeam.size() == 0)) {
//					broadcast(ChatColor.WHITE + "Cancelled due to not enough players");
//					for(String s : redTeam) {
//						Player red = Bukkit.getPlayerExact(s);
//						arenaSpectateRed(red);
//						utilPlayer.clearInvent(red);
//						utilInv.retrieveContents(red);
//						Money.addMoney(red, 1000);
//					}
//					for(String s : blueTeam) {
//						Player blue = Bukkit.getPlayerExact(s);
//						arenaSpectateRed(blue);
//						utilPlayer.clearInvent(blue);
//						utilInv.retrieveContents(blue);
//						Money.addMoney(blue, 1000);
//					}
//				} else if(redTeam.size() > 0 && blueTeam.size() > 0) {
//					startGame();
//					sendPlayerStatus();
//				}
//			}
//		}, 1200L);
//	}
//
//
//	public static void removePlayer(Player player) {
//		utilInv.retrieveContents(player);
//		if(redTeam.contains(player.getName())) {
//			player.setGameMode(GameMode.SURVIVAL);
//			arenaSpectateRed(player);
//			utilInv.retrieveContents(player);
//			redTeam.remove(player.getName());
//			return;
//		}
//		if(blueTeam.contains(player.getName())) {
//			player.setGameMode(GameMode.SURVIVAL);
//			arenaSpectateBlue(player);
//			utilInv.retrieveContents(player);
//			blueTeam.remove(player.getName());
//			return;
//		}
//	}
//
//	public static void forceEndGame() {
//		if(!inGame && !inLobby) {
//			return;
//		}
//		for(String s : redTeam) {
//			Player red = Bukkit.getPlayerExact(s);
//			arenaSpectateRed(red);
//			utilPlayer.clearInvent(red);
//			utilInv.retrieveContents(red);
//		}
//		for(String s : blueTeam) {
//			Player blue = Bukkit.getPlayerExact(s);
//			arenaSpectateBlue(blue);
//			utilPlayer.clearInvent(blue);
//			utilInv.retrieveContents(blue);
//		}
//		inGame = false;
//		inLobby = false;
//		redTeam.clear();
//		blueTeam.clear();
//		Bukkit.broadcastMessage(ChatColor.BLUE + "Arena - " + ChatColor.WHITE + "Ended");
//	}
//	
//	public static void endNaturally(ArenaTeams winner) {
//		if(!inGame && !inLobby) {
//			return;
//		}
//		for(String s : redTeam) {
//			Player red = Bukkit.getPlayerExact(s);
//			arenaSpectateRed(red);
//			utilPlayer.clearInvent(red);
//			utilInv.retrieveContents(red);
//			if(winner == ArenaTeams.RED) {
//				Money.addMoneyOffline(s, 2000);
//				broadcast(ChatColor.RED + "Red Team " + ChatColor.WHITE + "won");
//			}
//		}
//		for(String s : blueTeam) {
//			Player blue = Bukkit.getPlayerExact(s);
//			arenaSpectateBlue(blue);
//			utilPlayer.clearInvent(blue);
//			utilInv.retrieveContents(blue);
//			if(winner == ArenaTeams.BLUE) {
//				Money.addMoneyOffline(s, 2000);
//				
//			}
//		}
//		inGame = false;
//		inLobby = false;
//		redTeam.clear();
//		blueTeam.clear();
//		broadcast(ChatColor.WHITE + "Finished");
//	}
//
//	public static void arenaSpectateBlue(Player player) {
//		if(player == null) {
//			return;
//		}
//		player.teleport(new Location(player.getServer().getWorld("world"), blueSpectate[0], blueSpectate[1], blueSpectate[2]));
//	}
//
//	public static void arenaSpectateRed(Player player) {
//		if(player == null) {
//			return;
//		}
//		player.teleport(new Location(player.getServer().getWorld("world"), redSpectate[0], redSpectate[1], redSpectate[2], 180, 0));
//	}
//
//	public static void lobbyAreaRed(Player player) {
//		player.teleport(new Location(player.getServer().getWorld("world"), redLobbyCoords[0], redLobbyCoords[1], redLobbyCoords[2], -90.0F, 0));
//	}
//	public static void lobbyAreaBlue(Player player) {
//		player.teleport(new Location(player.getServer().getWorld("world"), blueLobbyCoords[0], blueLobbyCoords[1], blueLobbyCoords[2], 90.0F, 0));
//	}
//	public static void startGame() {
//		for(String s : redTeam) {
//			Player red = Bukkit.getPlayerExact(s);
//			red.teleport(new Location(red.getServer().getWorld("world"), redGameSpawn[0], redGameSpawn[1], redGameSpawn[2], 132.8F, 0));
//			red.getLocation().setYaw(132.8F);
//			Money.subtractMoney(red, 1000);
//		}
//		for(String s : blueTeam) {
//			Player blue = Bukkit.getPlayerExact(s);
//			blue.teleport(new Location(blue.getServer().getWorld("world"), blueGameSpawn[0], blueGameSpawn[1], blueGameSpawn[2], -59.5F, 0));
//			blue.getLocation().setYaw(-59.5F);
//			Money.subtractMoney(blue, 1000);
//		}
//		bPlayers = blueTeam.size();
//		rPlayers = redTeam.size();
//	}
//
//
//	public static boolean hasTeam(Player player) {
//		if(!redTeam.contains(player.getName()) && (!blueTeam.contains(player.getName()))) {
//			return false;
//		} else if(redTeam.contains(player.getName()) || (blueTeam.contains(player.getName()))) {
//			return true;
//		}
//		return false;
//	}
//
//	public static int getKills(String player) {
//		return playerMap.get(player).kills;
//	}
//
//	public static int getDeaths(String player) {
//		return playerMap.get(player).deaths;
//	}
//
//	public static int getExp(String player) {
//		return playerMap.get(player).exp;
//	}
//	public static String getRankStr(String player) {
//		return rankToString(playerMap.get(player).rank);
//	}
//	public static int getWins(String player) {
//		return playerMap.get(player).wins;
//	}
//	public static int getLosses(String player) {
//		return playerMap.get(player).losses;
//	}
//	
//	public static int getScore(String player) {
//		return playerMap.get(player).score;
//	}
//
//	public static void privMessage(Player player, String message) {
//		player.sendMessage(ChatColor.BLUE + "Arena - " + ChatColor.WHITE + message);
//	}
//
//	public static void broadcast(String message) {
//		Bukkit.broadcastMessage(ChatColor.BLUE + "Arena - " + message);
//	}
//
//	public static void addExp(Player player, int amount) {
//		playerMap.get(player.getName()).exp += amount;
//		ResultSet rs = MySQL.doQuery("SELECT player FROM arena WHERE player ='" + player.getName() + "'");
//		try {
//			if(rs.next()) {
//				MySQL.doUpdate("UPDATE `arena` SET exp=exp+" + amount + " WHERE player='" + player.getName() + "'");
//			} else if(!rs.next()) {
//				return;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void addKills(Player player) {
//		playerMap.get(player.getName()).kills += 1;
//		ResultSet rs = MySQL.doQuery("SELECT player FROM arena WHERE player ='" + player.getName() + "'");
//		try {
//			if(rs.next()) {
//				MySQL.doUpdate("UPDATE `arena` SET kills=kills+1 WHERE player='" + player.getName() + "'");
//			} else if(!rs.next()) {
//				return;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		addExp(player, 200);
//	}
//
//	public static void addDeaths(Player player) {
//		if(playerMap.containsKey(player.getName())) {
//			playerMap.get(player.getName()).deaths += 1;
//		}
//		ResultSet rs = MySQL.doQuery("SELECT player FROM arena WHERE player ='" + player.getName() + "'");
//		try {
//			if(rs.next()) {
//				MySQL.doUpdate("UPDATE `arena` SET deaths=deaths+1 WHERE player='" + player.getName() + "'");
//			} else if(!rs.next()) {
//				return;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void addScore(Player player, int amount) {
//		ResultSet rs = MySQL.doQuery("SELECT player FROM arena WHERE player ='" + player.getName() + "'");
//		try {
//			if(rs.next()) {
//				MySQL.doUpdate("UPDATE `arena` SET score=score+" + amount + " WHERE player='" + player.getName() + "'");
//			} else if(!rs.next()) {
//				return;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void addWin(Player player) {
//		playerMap.get(player.getName()).wins += 1;
//		ResultSet rs = MySQL.doQuery("SELECT player FROM arena WHERE player ='" + player.getName() + "'");
//		try {
//			if(rs.next()) {
//				MySQL.doUpdate("UPDATE `arena` SET wins=wins+1 WHERE player='" + player.getName() + "'");
//			} else if(!rs.next()) {
//				return;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void addLoss(Player player) {
//		playerMap.get(player.getName()).losses += 1;
//		ResultSet rs = MySQL.doQuery("SELECT player FROM arena WHERE player ='" + player.getName() + "'");
//		try {
//			if(rs.next()) {
//				MySQL.doUpdate("UPDATE `arena` SET losses=losses+1 WHERE player='" + player.getName() + "'");
//			} else if(!rs.next()) {
//				return;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void setRankStr(Player player, String rank) {
//		playerMap.get(player.getName()).rank = strToRank(rank);
//		ResultSet rs = MySQL.doQuery("SELECT player FROM arena WHERE player ='" + player.getName() + "'");
//		try {
//			if(rs.next()) {
//				MySQL.doUpdate("UPDATE `arena` SET rank=" + rank + " WHERE player='" + player.getName() + "'");
//			} else if(!rs.next()) {
//				return;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		if(player != null) {
//			broadcast(ChatColor.YELLOW + player.getName() + ChatColor.YELLOW + " has been promoted to " + ChatColor.YELLOW + WordUtils.capitalize(rank.toLowerCase()));
//		}
//	}
//
//
//
//	public static void rankUp(Player player) {
//		ResultSet rs = MySQL.doQuery("SELECT rank FROM arena WHERE player='" + player.getName() + "'");
//		String currRank;
//		try {
//			currRank = rs.getString(1);
//			if(currRank.equalsIgnoreCase("private")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='lance corporal' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("lance corporal")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='corporal' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("corporal")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='sergeant' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("sergeant")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='staff sergeant' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("staff sergeant")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='master sergeant' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("master sergeant")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='second lieutenant' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("second lieutenant")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='first lieutenant' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("first lieutenant")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='captain' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("captain")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='major' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("major")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='lieutenant colonel' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("lieutenant colonel")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='colonel' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("colonel")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='brigadier general' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("brigadier general")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='major general' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("major general")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='lieutenant general' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("lieutenant general")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='general' WHERE player='" + player.getName());
//			} else if(currRank.equalsIgnoreCase("general")) {
//				MySQL.doUpdate("UPDATE `arena` SET `rank`='commander' WHERE player='" + player.getName());
//			} 
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static boolean isPlaying(Player player) {
//		return blueTeam.contains(player.getName()) || redTeam.contains(player.getName());
//	}
//
//	public static String getRankFormatted(String player) {
//		String rank = getRankStr(player);
//		if(rank.equalsIgnoreCase("private".toLowerCase())) {
//			return "Private";
//		} else if(rank.equalsIgnoreCase("lance corporal".toLowerCase())) {
//			return "Lance Corporal";
//		} else if(rank.equalsIgnoreCase("corporal".toLowerCase())) {
//			return "Corporal";
//		} else if(rank.equalsIgnoreCase("sergeant".toLowerCase())) {
//			return "Sergeant";
//		} else if(rank.equalsIgnoreCase("staff sergeant".toLowerCase())) {
//			return "Staff Sergeant";
//		} else if(rank.equalsIgnoreCase("master sergeant".toLowerCase())) {
//			return "Master Sergeant";
//		} else if(rank.equalsIgnoreCase("second lieutenant".toLowerCase())) {
//			return "Second Lieutenant";
//		} else if(rank.equalsIgnoreCase("first lieutenant".toLowerCase())) {
//			return "First Lieutenant";
//		} else if(rank.equalsIgnoreCase("captain".toLowerCase())) {
//			return "Captain";
//		} else if(rank.equalsIgnoreCase("major".toLowerCase())) {
//			return "Major";
//		} else if(rank.equalsIgnoreCase("lieutenant colonel".toLowerCase())) {
//			return "Lieutenant Colonel";
//		} else if(rank.equalsIgnoreCase("colonel".toLowerCase())) {
//			return "Colonel";
//		} else if(rank.equalsIgnoreCase("brigadier general".toLowerCase())) {
//			return "Brigadier General";
//		} else if(rank.equalsIgnoreCase("major general".toLowerCase())) {
//			return "Major General";
//		} else if(rank.equalsIgnoreCase("lieutenant general".toLowerCase())) {
//			return "Lieutenant General";
//		} else if(rank.equalsIgnoreCase("general".toLowerCase())) {
//			return "General";
//		} else if(rank.equalsIgnoreCase("commander".toLowerCase())) {
//			return "Commander";
//		}
//		return "Private";
//
//	}
//
//	public static void messageRed(String message) {
//		for(String red : redTeam) {
//			Player pRed = Bukkit.getPlayerExact(red);
//			pRed.sendMessage(ChatColor.BLUE + "Arena: " + message);
//		}
//	}
//	public static void messageBlue(String message) {
//		for(String red : blueTeam) {
//			Player pRed = Bukkit.getPlayerExact(red);
//			pRed.sendMessage(ChatColor.BLUE + "Arena: " + message);
//		}
//	}
//
//	public static void messageRedRaw(String message) {
//		for(String red : redTeam) {
//			Player pRed = Bukkit.getPlayerExact(red);
//			pRed.sendMessage(message);
//		}
//	}
//	public static void messageBlueRaw(String message) {
//		for(String red : blueTeam) {
//			Player pRed = Bukkit.getPlayerExact(red);
//			pRed.sendMessage(message);
//		}
//	}
//
//	public static double getKD(String player) {
//		double kd = 1.00;
//		if(getKills(player) == 0 && getDeaths(player) == 0) {
//			return 1.00;
//		} else {
//			kd = utilMath.round(getKills(player) / getDeaths(player), 4);
//		}
//		return kd;
//	}
//
//	public static ArenaTeams getTeam(Player player) {
//		if(redTeam.contains(player.getName())) return ArenaTeams.RED;
//		else if(blueTeam.contains(player.getName())) return ArenaTeams.BLUE;
//		else return ArenaTeams.NONE;
//	}
//
//
//	public static void setTeamName(Player player, String teamName) {
//		Objective obj = board.registerNewObjective("team", "dummy");
//		obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
//		obj.setDisplayName(ChatColor.RED + rankToString(getRank(player.getName())));
//
//	}
//
//	public static void registerScoreboard(Player player) {
//		Team team = board.registerNewTeam("Arena");
//		team.setDisplayName("Arena players");
//		team.setPrefix(ChatColor.BLUE + "Blue: " + ChatColor.WHITE + blueTeam.size() + "/" + bPlayers);
//		team.setPrefix(ChatColor.RED + "Red: " + ChatColor.WHITE + redTeam.size() + "/" + rPlayers);
//	}
//
//	public static void sendPlayerStatus() {
//		String alphaPlayers = "";
//		String bravoPlayers = "";
//		if(allRedTeam.size() == 0) {
//			return;
//		} else {
//			for(String redPlayers : allRedTeam) {
//				Player red = Bukkit.getServer().getPlayer(redPlayers);
//				if(redTeam.contains(red.getName())) alphaPlayers = alphaPlayers + ChatColor.GREEN + red.getName() + ChatColor.YELLOW + ", ";
//				else alphaPlayers = alphaPlayers + ChatColor.RED + red.getName() + ChatColor.YELLOW + ", ";
//			}
//			Bukkit.broadcastMessage(ChatColor.GRAY + "Team Red - " + ChatColor.YELLOW + "[" + ChatColor.GREEN + redTeam.size() + ChatColor.YELLOW + "/" + ChatColor.RED + (allRedTeam.size() - redTeam.size()) + ChatColor.YELLOW + "]" + alphaPlayers);
//		}
//		if(allBlueTeam.size() == 0) {
//			return;
//		} else {
//			for(String bluePlayers : allBlueTeam) {
//				Player blue = Bukkit.getServer().getPlayer(bluePlayers);
//				if(blueTeam.contains(blue.getName())) bravoPlayers = bravoPlayers + ChatColor.GREEN + blue.getName() + ChatColor.YELLOW + ", ";
//				else bravoPlayers = bravoPlayers + ChatColor.RED + blue.getName() + ChatColor.YELLOW + ", ";
//			}
//			Bukkit.broadcastMessage(ChatColor.GRAY + "Team Blue - " + ChatColor.YELLOW + "[" + ChatColor.GREEN + blueTeam.size() + ChatColor.YELLOW + "/" + ChatColor.RED + (allBlueTeam.size() - blueTeam.size()) + ChatColor.YELLOW + "]" + bravoPlayers);
//		}
//	}
//	
//	public static void setRank(String player, ArenaRanks rank) {
//		MySQL.doUpdate("UPDATE arena SET rank=" + MySQL.f(rankToString(rank)));
//		playerMap.get(player).rank = rank;
//	}
//	
//	public static ArenaRanks getRank(String player) {
//		return playerMap.get(player).rank;
//	}
//
//	public static ArenaRanks strToRank(String rank) {
//		if(rank.equalsIgnoreCase("private")) return ArenaRanks.PRIVATE;
//		else if(rank.equalsIgnoreCase("lance corporal")) return ArenaRanks.LANCE_CORPORAL;
//		else if(rank.equalsIgnoreCase("corporal")) return ArenaRanks.CORPORAL;
//		else if(rank.equalsIgnoreCase("sergeant")) return ArenaRanks.SERGEANT;
//		else if(rank.equalsIgnoreCase("staff sergeant")) return ArenaRanks.STAFF_SERGEANT;
//		else if(rank.equalsIgnoreCase("master sergeant")) return ArenaRanks.MASTER_SERGEANT;
//		else if(rank.equalsIgnoreCase("second lieutenant")) return ArenaRanks.SECOND_LIEUTENANT;
//		else if(rank.equalsIgnoreCase("first lieutenant")) return ArenaRanks.FIRST_LIEUTENANT;
//		else if(rank.equalsIgnoreCase("captain")) return ArenaRanks.CAPTAIN;
//		else if(rank.equalsIgnoreCase("major")) return ArenaRanks.MAJOR;
//		else if(rank.equalsIgnoreCase("lieutenant colonel")) return ArenaRanks.LIEUTENANT_COLONEL;
//		else if(rank.equalsIgnoreCase("colonel")) return ArenaRanks.COLONEL;
//		else if(rank.equalsIgnoreCase("brigadier general")) return ArenaRanks.BRIGADIER_GENERAL;
//		else if(rank.equalsIgnoreCase("major general")) return ArenaRanks.MAJOR_GENERAL;
//		else if(rank.equalsIgnoreCase("lieutenant general")) return ArenaRanks.LIEUTENANT_GENERAL;
//		else if(rank.equalsIgnoreCase("general")) return ArenaRanks.GENERAL;
//		else if(rank.equalsIgnoreCase("commander")) return ArenaRanks.COMMANDER;
//		else return ArenaRanks.PRIVATE;
//
//	}
//	
//	public static void sendStats(Player player) {
//		if(player == null) return;
//		if(!playerMap.containsKey(player.getName())) {
//			player.sendMessage(ChatColor.BLUE + "Arena - " + ChatColor.WHITE + "You appear to have no stats, please contact and admin about it");
//			return;
//		}
//		player.sendMessage(ChatColor.BLUE + "Arena Stats - " + ChatColor.YELLOW + player.getName());
//		player.sendMessage(ChatColor.YELLOW + "Exp - " + ChatColor.WHITE + getExp(player.getName()) + ChatColor.YELLOW + " Score - " + ChatColor.WHITE + getScore(player.getName()));
//		player.sendMessage(ChatColor.YELLOW + "Rank - " + ChatColor.WHITE + getRankFormatted(player.getName()));
//		player.sendMessage(ChatColor.YELLOW + "Kills - " + ChatColor.WHITE + getKills(player.getName()) + ChatColor.YELLOW + " Deaths - " + ChatColor.WHITE + getDeaths(player.getName()));
//		player.sendMessage(ChatColor.YELLOW + "KD - " + ChatColor.WHITE + getKD(player.getName()));
//		player.sendMessage(ChatColor.YELLOW + "Position - " + ChatColor.WHITE + Leaderboards.getPosition(player.getName(), LType.SCORE, Leaderboard.ARENA));
//	}
//	
//	public static void sendStatsOther(Player player, String target) {
//		if(player == null) return;
//		Player pTarget = Bukkit.getPlayer(target);
//		if(pTarget != null) {
//			if(!playerMap.containsKey(pTarget.getName())) {
//				player.sendMessage(ChatColor.BLUE + "Arena - " + ChatColor.YELLOW + pTarget.getName() + ChatColor.WHITE + " appears to have no stats, please contact and admin about it");
//				return;
//			}
//			player.sendMessage(ChatColor.BLUE + "Arena Stats - " + ChatColor.YELLOW + pTarget.getName());
//			player.sendMessage(ChatColor.YELLOW + "Exp - " + ChatColor.WHITE + getExp(pTarget.getName()) + ChatColor.YELLOW + " Score - " + ChatColor.WHITE + getScore(pTarget.getName()));
//			player.sendMessage(ChatColor.YELLOW + "Rank - " + ChatColor.WHITE + getRankFormatted(pTarget.getName()));
//			player.sendMessage(ChatColor.YELLOW + "Kills - " + ChatColor.WHITE + getKills(pTarget.getName()) + ChatColor.YELLOW + " Deaths - " + ChatColor.WHITE + getDeaths(pTarget.getName()));
//			player.sendMessage(ChatColor.YELLOW + "KD - " + ChatColor.WHITE + getKD(pTarget.getName()));
//			player.sendMessage(ChatColor.YELLOW + "Position - " + ChatColor.WHITE + Leaderboards.getPosition(pTarget.getName(), LType.SCORE, Leaderboard.ARENA));
//			return;
//		}
//		if(pTarget == null) {
//			if(!playerMap.containsKey(target)) {
//				player.sendMessage(ChatColor.BLUE + "Arena - " + ChatColor.YELLOW + target + ChatColor.WHITE + " appears to have no stats, please contact and admin about it");
//				return;
//			}
//			player.sendMessage(ChatColor.BLUE + "Arena Stats - " + ChatColor.YELLOW + target);
//			player.sendMessage(ChatColor.YELLOW + "Exp - " + ChatColor.WHITE + getExp(target) + ChatColor.YELLOW + " Score - " + ChatColor.WHITE + getScore(target));
//			player.sendMessage(ChatColor.YELLOW + "Rank - " + ChatColor.WHITE + getRankFormatted(target));
//			player.sendMessage(ChatColor.YELLOW + "Kills - " + ChatColor.WHITE + getKills(target) + ChatColor.YELLOW + " Deaths - " + ChatColor.WHITE + getDeaths(target));
//			player.sendMessage(ChatColor.YELLOW + "KD - " + ChatColor.WHITE + getKD(target));
//			player.sendMessage(ChatColor.YELLOW + "Position - " + ChatColor.WHITE + Leaderboards.getPosition(target, LType.SCORE, Leaderboard.ARENA));
//			return;
//		}
//	}
//
//	public static String rankToString(ArenaRanks rank) {
//		if(rank == ArenaRanks.PRIVATE) return "private";
//		else if(rank == ArenaRanks.LANCE_CORPORAL) return "lance corporal";
//		else if(rank == ArenaRanks.CORPORAL) return "corporal";
//		else if(rank == ArenaRanks.SERGEANT) return "sergeant";
//		else if(rank == ArenaRanks.STAFF_SERGEANT) return "staff sergeant";
//		else if(rank == ArenaRanks.MASTER_SERGEANT) return "master sergeant";
//		else if(rank == ArenaRanks.SECOND_LIEUTENANT) return "second lieutenant";
//		else if(rank == ArenaRanks.FIRST_LIEUTENANT) return "first lieutenant";
//		else if(rank == ArenaRanks.CAPTAIN) return "captain";
//		else if(rank == ArenaRanks.MAJOR) return "major";
//		else if(rank == ArenaRanks.LIEUTENANT_COLONEL) return "lieutenant colonel";
//		else if(rank == ArenaRanks.COLONEL) return "colonel";
//		else if(rank == ArenaRanks.BRIGADIER_GENERAL) return "brigadier general";
//		else if(rank == ArenaRanks.MAJOR_GENERAL) return "major general";
//		else if(rank == ArenaRanks.LIEUTENANT_GENERAL) return "lieutenant general";
//		else if(rank == ArenaRanks.GENERAL) return "general";
//		else if(rank == ArenaRanks.COMMANDER) return "commander";
//		else return "private";
//	}
//	public static void handleKill(String player, PlayerDeathEvent event) {
//		Player kPlayer = Bukkit.getPlayer(player);
//		Player killer = event.getEntity().getKiller();
//		event.getDrops().clear();
//		Arena.addKills(killer);
//		Arena.addScore(killer, 100);
//		Arena.addDeaths(kPlayer);
//		Utilities.skipRespawn(kPlayer);
//		Arena.removePlayer(kPlayer);
//		Arena.sendPlayerStatus();
//		rankUp(kPlayer);
//		if(redTeam.size() == 0 && blueTeam.size() > 0) {
//			endNaturally(ArenaTeams.BLUE);
//		}
//		if(blueTeam.size() == 0 && redTeam.size() > 0) {
//			endNaturally(ArenaTeams.RED);
//		}
//		if(getTeam(kPlayer) == ArenaTeams.RED) {
//			
//		}
//		if(getTeam(kPlayer) == ArenaTeams.BLUE) {
//			
//		}
//	}
//	
//	public static void rankUp(String player) {
//		if(getRank(player) == ArenaRanks.PRIVATE && getExp(player) > rankNumber[1]) {
//			setRank(player, ArenaRanks.LANCE_CORPORAL);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.GRAY + "Lance Corporal");
//		}
//		else if(getRank(player) == ArenaRanks.LANCE_CORPORAL && getExp(player) > rankNumber[2]) {
//			setRank(player, ArenaRanks.CORPORAL);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.GRAY + "Corporal");
//		}
//		else if(getRank(player) == ArenaRanks.CORPORAL && getExp(player) > rankNumber[3]) {
//			setRank(player, ArenaRanks.SERGEANT);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.YELLOW + "Sergeant");
//		}
//		else if(getRank(player) == ArenaRanks.SERGEANT && getExp(player) > rankNumber[4]) {
//			setRank(player, ArenaRanks.STAFF_SERGEANT);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.YELLOW + "Staff Sergeant");
//		}
//		else if(getRank(player) == ArenaRanks.STAFF_SERGEANT && getExp(player) > rankNumber[5]) {
//			setRank(player, ArenaRanks.MASTER_SERGEANT);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.YELLOW + "Master Sergeant");
//		}
//		else if(getRank(player) == ArenaRanks.MASTER_SERGEANT && getExp(player) > rankNumber[6]) {
//			setRank(player, ArenaRanks.SECOND_LIEUTENANT);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.YELLOW + "Second Lieutenant");
//		}
//		else if(getRank(player) == ArenaRanks.SECOND_LIEUTENANT && getExp(player) > rankNumber[7]) {
//			setRank(player, ArenaRanks.FIRST_LIEUTENANT);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.YELLOW + "First Lieutenant");
//		}
//		else if(getRank(player) == ArenaRanks.FIRST_LIEUTENANT && getExp(player) > rankNumber[8]) {
//			setRank(player, ArenaRanks.CAPTAIN);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.YELLOW + "Captain");
//		}
//		else if(getRank(player) == ArenaRanks.CAPTAIN && getExp(player) > rankNumber[9]) {
//			setRank(player, ArenaRanks.MAJOR);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.YELLOW + "Major");
//		}
//		else if(getRank(player) == ArenaRanks.MAJOR && getExp(player) > rankNumber[10]) {
//			setRank(player, ArenaRanks.LIEUTENANT_COLONEL);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.YELLOW + "Lieutenant Colonel");
//		}
//		else if(getRank(player) == ArenaRanks.LIEUTENANT_COLONEL && getExp(player) > rankNumber[11]) {
//			setRank(player, ArenaRanks.COLONEL);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.YELLOW + "Colonel");
//		}
//		else if(getRank(player) == ArenaRanks.COLONEL && getExp(player) > rankNumber[12]) {
//			setRank(player, ArenaRanks.BRIGADIER_GENERAL);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.BLUE + "Brigadier General");
//		}
//		else if(getRank(player) == ArenaRanks.BRIGADIER_GENERAL && getExp(player) > rankNumber[13]) {
//			setRank(player, ArenaRanks.MAJOR_GENERAL);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.BLUE + "Major General");
//		}
//		else if(getRank(player) == ArenaRanks.MAJOR_GENERAL && getExp(player) > rankNumber[14]) {
//			setRank(player, ArenaRanks.LIEUTENANT_GENERAL);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.BLUE + "Lietenant General");
//		}
//		else if(getRank(player) == ArenaRanks.LIEUTENANT_GENERAL && getExp(player) > rankNumber[15]) {
//			setRank(player, ArenaRanks.GENERAL);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.BLUE + "General");
//		}
//		else if(getRank(player) == ArenaRanks.GENERAL && getExp(player) > rankNumber[16]) {
//			setRank(player, ArenaRanks.COMMANDER);
//			broadcast(ChatColor.YELLOW + player + ChatColor.WHITE + " ranked up to " + ChatColor.AQUA + "Commander");
//		}
//	}
//
//	public enum ArenaRanks {
//		PRIVATE("Private", 0),
//		LANCE_CORPORAL("Lance Corporal", 1000),
//		CORPORAL("Corporal", 2200),
//		SERGEANT("Sergeant", 3800),
//		STAFF_SERGEANT("Staff Sergeant", 6000),
//		MASTER_SERGEANT("Master Sergeant", 10000),
//		SECOND_LIEUTENANT("Second Lieutenant", 15000),
//		FIRST_LIEUTENANT("First Lieutenant", 21000),
//		CAPTAIN("Captain", 29000),
//		MAJOR("Major", 40000),
//		LIEUTENANT_COLONEL("Lieutenant Colonel", 55000),
//		COLONEL("Colonel", 71000),
//		BRIGADIER_GENERAL("Brigadier General", 87000),
//		MAJOR_GENERAL("Major General", 103000),
//		LIEUTENANT_GENERAL("Lieutenant General", 120000),
//		GENERAL("General", 140000),
//		COMMANDER("Commander", 170000);
//
//		private final String rank;
//		private final int xpReq;
//
//		ArenaRanks(String currRank, int expReq) {
//			rank = currRank;
//			xpReq = expReq;
//		}
//
//		public String getRank() {
//			return rank;
//		}
//
//		public int getXPReq() {
//			return xpReq;
//
//		}
//	}
//
}
