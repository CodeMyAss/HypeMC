package me.loogeh.Hype;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.economy.Money;
import me.loogeh.Hype.moderation.Ban;
import me.loogeh.Hype.moderation.Mute;
import me.loogeh.Hype.util.utilTime;
import me.loogeh.Hype.util.utilTime.TimeUnit;

public class HPlayer {
	private static Hype plugin = Hype.plugin;
	
	private String name;
	private Long playTime;
	private Long lastJoin;
	private Long lastReward;
	private int loginCount;
	private int banCount;
	private boolean banned;
	private boolean muted;
	private HashMap<String, ItemStack[]> stored_inv = new HashMap<String, ItemStack[]>();	
	
	public HPlayer(String name, long playTime, long lastJoin, long lastReward, int loginCount, int banCount, boolean banned, boolean muted) {
		this.name = name;
		this.playTime = playTime;
		this.lastJoin = lastJoin;
		this.lastReward = lastReward;
		this.loginCount = loginCount;
		this.banCount = banCount;
		this.banned = banned;
		this.muted = muted;
	}
	
	public HPlayer(String name, long lastJoin, long lastReward) {
		this.name = name;
		this.lastJoin = lastJoin;
		this.lastReward = lastReward;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Long getPlayTime() {
		return this.playTime;
	}
	
	public String getPlayTimeStr() {
		String playTimeStr = utilTime.millisToReadable(getPlayTime(), false);
		return playTimeStr;
	}

	public long getLastJoin() {
		return this.lastJoin;
	}
	
	public boolean getBanned() {
		return this.banned;
	}
	
	public boolean getMuted() {
		return this.muted;
	}
	
	public int getLoginCount() {
		return this.loginCount;
	}
	
	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	
	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}
	
	public void addLoginCount() {
		this.loginCount += 1;
	}
	
	public long getLastReward() {
		return this.lastReward;
	}
	
	public void setLastReward(long lastReward) {
		this.lastReward = lastReward;
	}
	
	public void setLastJoin(long lastJoin) {
		this.lastJoin = lastJoin;
	}
	
	public int getBanCount() {
		return this.banCount;
	}
	
	public void addBanCount() {
		this.banCount += 1;
	}
	
	public void setBanCount(int banCount) {
		this.banCount = banCount;
	}
	
	public void setPlayTime(long playTime) {
		this.playTime = playTime;
	}

	public void updatePlayTime(long playTime) {
		BigInteger cur = new BigInteger("" + getPlayTime());
		BigInteger bplayTime = new BigInteger("" + playTime);
		setPlayTime(cur.add(bplayTime).longValue());
	}
	
	public void updatePlayTime() {
		this.playTime += getSession();
	}
	
	public String getSessionTime() {
		if(getLastJoin() == 0) {
			return "0.0 Seconds";
		}
		return utilTime.convertString(System.currentTimeMillis() - getLastJoin(), TimeUnit.BEST, 1);
	}
	
	public long getSession() {
		return System.currentTimeMillis() - getLastJoin();
	}
	
	public void updateLastLoginSQL(String player) {
		MySQL.doUpdate("UPDATE player_info SET lastLogin=" + System.currentTimeMillis() + " WHERE player='" + player + "'");
	}
	
	public void sendInfo(Player player) {
		player.sendMessage(ChatColor.BOLD + "Player " + player.getName());
		player.sendMessage(ChatColor.BOLD.toString() + ChatColor.YELLOW + "Session " + ChatColor.WHITE + getSessionTime());
		player.sendMessage(ChatColor.BOLD.toString() + ChatColor.YELLOW + "Tagged " + ChatColor.WHITE + CTag.getRemInt(player.getName()) + " Seconds");
		player.sendMessage(ChatColor.BOLD + "Last Join " + getLastJoin());
		player.sendMessage(ChatColor.BOLD + "this.playTime " + this.playTime);
		updatePlayTime();
		player.sendMessage(ChatColor.BOLD.toString() + ChatColor.YELLOW + "Play Time " + ChatColor.WHITE + getPlayTimeStr());
	}
	
	public static void sendInfo(Player caller, String target) {
		get(target).updatePlayTime();
		caller.sendMessage(ChatColor.BOLD + "Player " + target);
		caller.sendMessage(ChatColor.BOLD + "Session " + ChatColor.WHITE + get(target).getSessionTime());
		caller.sendMessage(ChatColor.BOLD + "Ban Count " + ChatColor.WHITE + get(target).getBanCount());
		caller.sendMessage(ChatColor.BOLD + "Login Count " + ChatColor.WHITE + get(target).getLoginCount());
		//caller.sendMessage(ChatColor.BOLD + "Play Time " + ChatColor.WHITE + get(target).getPlayTimeStr());
	}
	
	public static HPlayer get(String player) {
		return InfoHolder.hype_players.get(player);
	}
	
	public static void write() {
		for(String player : InfoHolder.hype_players.keySet()) {
			write(player);
		}
	}
	
	public static void load() {
		for(Player players : Bukkit.getOnlinePlayers()) {
			ResultSet rs = MySQL.doQuery("SELECT player,playTime,lastJoin,lastReward,loginCount,banCount FROM player_info WHERE player='" + players.getName() + "'");
			try {
				while(rs.next()) {
					InfoHolder.hype_players.put(rs.getString(1), new HPlayer(rs.getString(1), rs.getLong(2), rs.getLong(3), rs.getLong(4), rs.getInt(5), rs.getInt(6), Ban.banMap.containsKey(rs.getString(1)), Mute.muteMap.containsKey(rs.getString(1))));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void load(String player) {
		ResultSet rs = MySQL.doQuery("SELECT player,playTime,lastJoin,lastReward,loginCount,banCount FROM player_info WHERE player='" + player + "'");
		try {
			if(rs.next()) {
				InfoHolder.hype_players.put(rs.getString(1), new HPlayer(rs.getString(1), rs.getLong(2), rs.getLong(3), rs.getLong(4), rs.getInt(5), rs.getInt(6), Ban.banMap.containsKey(rs.getString(1)), Mute.muteMap.containsKey(rs.getString(1))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void write(String player) {
		if(!InfoHolder.hype_players.containsKey(player)) return;
		//long playTime = InfoHolder.hype_players.get(player).playTime;
		long lastJoin = InfoHolder.hype_players.get(player).lastJoin;
		long lastReward = InfoHolder.hype_players.get(player).lastReward;
		int loginCount = InfoHolder.hype_players.get(player).loginCount;
		long banCount = InfoHolder.hype_players.get(player).banCount;
		MySQL.doUpdate("UPDATE `player_info` SET `player`='" + player + "',`playTime`=" + (System.currentTimeMillis() - lastJoin) + 0 + ",`lastReward`=" + lastReward + ",`loginCount`=" + loginCount + ",`banCount`=" + banCount + " WHERE player='" + player + "'");
		InfoHolder.hype_players.remove(player);
	}
	
	public static void holdPlayer(String player, long playTime, long lastJoin, long lastReward, int loginCount, int banCount, boolean banned, boolean muted) {
		InfoHolder.hype_players.put(player, new HPlayer(player, playTime, lastJoin, lastReward, loginCount, banCount, banned, muted));
	}
	
	public static void newPlayer(String player) {
		InfoHolder.hype_players.put(player, new HPlayer(player, 0, System.currentTimeMillis(), System.currentTimeMillis(), 1, 0, false, false));
	}
	
	public static void insertPlayer(String player) {
		
	}
	
	public static boolean handleTime(Player player) {
		if(!InfoHolder.hype_players.containsKey(player.getName())) holdPlayer(player.getName(), 0, 0, System.currentTimeMillis(), 1, 0, true, true);
		if(InfoHolder.hype_players.get(player.getName()).lastReward == 0) return false;
		if((System.currentTimeMillis() - InfoHolder.hype_players.get(player.getName()).lastReward) > 1200000) {
			InfoHolder.hype_players.get(player.getName()).setLastReward(System.currentTimeMillis());
			Money.addMoney(player, 2000);
			player.sendMessage(ChatColor.BOLD + ChatColor.GREEN.toString() + "Loyalty Reward - 20 minutes");
			player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Reward " + ChatColor.WHITE + "$2000");
			return true;
		}
		return false;
	}
	
	public static void handleThread() {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				for(Player players : Bukkit.getOnlinePlayers()) {
					handleTime(players);
				}
			}
		}, 400L, 400L);
	}
	
	public static boolean hasPlayed(String player) {
		ResultSet rs = MySQL.doQuery("SELECT player FROM player_info WHERE player=" + MySQL.f(player));
		try {
			if(rs.next()) return true;
			else return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void addPlayer(String player) {
		if(hasPlayed(player)) return;
		MySQL.doUpdate("INSERT INTO `player_info`(`player`, `playTime`, `lastJoin`, `lastReward`, `loginCount`, `banCount`) VALUES (" + MySQL.fc(player) + 0 + ", " + System.currentTimeMillis() + ", " + 0 + ", " + 1 + ", " + 0 + ")");
		InfoHolder.hype_players.put(player, new HPlayer(player, 0, System.currentTimeMillis(), 0, 1, 0, false, false));
	}
	
	public ItemStack[] getInventory(String name) {
		if(!stored_inv.containsKey(name)) return null;
		return stored_inv.get(name);
	}
	
	public void storeInventory(String name, ItemStack[] contents) {
		stored_inv.put(name, contents);
	}
}
