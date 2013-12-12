package me.loogeh.Hype.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.loogeh.Hype.HPlayer;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.M;
import me.loogeh.Hype.armour.Armour.Kit;
import me.loogeh.Hype.economy.Money;
import me.loogeh.Hype.games.Games.GameStatus;
import me.loogeh.Hype.games.Games.GameType;

public class Game {
	private static Hype plugin = Hype.plugin;
	
	private String name;
	private GameType type;
	private int id;
	private int cost;
	private Location lobbyLoc;
	private List<Location> spawns;
	private GameStatus status;
	private long lastRun;
	private HashMap<String, Integer> players;
	private boolean teleport; //teleport to spawn on start
	private HashMap<String, Kit> kits = new HashMap<String, Kit>();
	
	private boolean countdown = false;
	
	public Game(String name, GameType type, int id, int cost, List<Location> spawns, Location lobbyLoc, GameStatus status, long lastRun, boolean teleport) {
		this.name = name;
		this.type = type;
		this.id = id;
		this.cost = cost;
		this.spawns = spawns;
		this.lobbyLoc = lobbyLoc;
		this.status = status;
		this.lastRun = lastRun;
		this.players = new HashMap<String, Integer>();
		this.teleport = teleport;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFormatName() {
		return WordUtils.capitalize(this.name.toLowerCase());
	}
	
	public GameType getType() {
		return this.type;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	public Location getSpawn(int team) {
		return this.spawns.get(team);
	}
	
	public Location getLobby() {
		return this.lobbyLoc;
	}
	
	public GameStatus getStatus() {
		return this.status;
	}
	
	public long getLastRun() {
		return this.lastRun;
	}
	
	public boolean getTeleport() {
		return this.teleport;
	}
	
	public int getTeam(Player player) {
		if(!this.players.containsKey(player.getName())) return -1;
		else return this.players.get(player.getName());
	}
	
	public void setStatus(GameStatus status) {
		this.status = status;
	}
	
	public void setLastRun(long millis) {
		this.lastRun = millis;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public HashMap<String, Integer> getPlayers() {
		return this.players;
	}
	
	public void join(Player player) {
		if(player == null) return;
		if(Games.getCurrentGame(player) != null) {
			M.v(player, "Game", ChatColor.WHITE + "You are already playing " + ChatColor.YELLOW + Games.getCurrentGame(player).getFormatName());
			return;
		}
		if(Money.getMoney(player) < getCost()) {
			M.v(player, "Game", ChatColor.WHITE + "You have " + ChatColor.YELLOW + "Insufficient Money " + ChatColor.WHITE + "to join " + ChatColor.YELLOW + getFormatName());
			return;
		}
		if(getStatus() == GameStatus.ENDED || getStatus() == GameStatus.STOPPED) {
			if(getRemainingTime() < 0.1) {
				initiate();
			} else {
				M.v(player, getFormatName(), ChatColor.WHITE + "Will start in " + ChatColor.GREEN + getRemaining());
				return;	
			}
		}
		if(getStatus() == GameStatus.LOBBY) {
			player.teleport(getLobby());
			M.v(player, getFormatName(), ChatColor.WHITE + "You joined " + ChatColor.GREEN + getFormatName());
			HPlayer.get(player.getName()).storeInventory(getName(), player.getInventory().getContents());
			HPlayer.get(player.getName()).storeInventory(getName() + "_armour", player.getInventory().getArmorContents());
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.setGameMode(GameMode.SURVIVAL);
			if(getType() == GameType.KIT_ARENA) this.players.put(player.getName(), getBestTeamTwo());
			else this.players.put(player.getName(), getBestTeam());
			if(!countdown) {
				countdown = true;
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					public void run() {
						start();
					}
				}, 1200L);
			}
			return;
		}
		if(getStatus() == GameStatus.PLAYING) {
			M.v(player, getFormatName(), ChatColor.WHITE + "You cannot join while a game is " + ChatColor.YELLOW + "in progress");
			return;
		}
		if(getRemainingTime() > 0.0) {
			M.v(player, getFormatName(), ChatColor.WHITE + "Starting in " + ChatColor.GREEN + getRemaining());
			return;
		}
		player.teleport(getLobby());
		HPlayer.get(player.getName()).storeInventory(getName(), player.getInventory().getContents());
		HPlayer.get(player.getName()).storeInventory(getName() + "_armour", player.getInventory().getArmorContents());
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setGameMode(GameMode.SURVIVAL);
	}
	
	public HashMap<String, Kit> getKitMap() {
		return this.kits;
	}
	
	public int getBestTeam() {
		int zero = 0, one = 0, two = 0, three = 0, four = 0;
		for(String players : getPlayers().keySet()) {
			if(getPlayers().get(players) == 0) zero++;
			else if(getPlayers().get(players) == 1) one++;
			else if(getPlayers().get(players) == 2) two++;
			else if(getPlayers().get(players) == 3) three++;
			else if(getPlayers().get(players) == 4) four++;
		}
		if(zero > one && zero > two && zero > three && zero > four) return 0;
		else if(one > two && one > three && one > four && one > zero) return 1;
		else if(two > one && two > three && two > four && two > zero) return 2;
		else if(three > one && three > two && three > four && three > zero) return 3;
		else if(four > one && four > two && four > three && four > zero) return 4;
		return 1;
	}
	
	public int getBestTeamTwo() {
		if(GameManager.isFFA(getType())) return 0;
		if(getTeamSize(1) > getTeamSize(2)) return 2;
		else if(getTeamSize(1) < getTeamSize(2)) return 1;
		else return 1;
	}
	
	public void removePlayer(String player) {
		if(getPlayers().containsKey(player)) this.players.remove(player);
		Player bPlayer = Bukkit.getPlayerExact(player);
		if(bPlayer != null) {
			bPlayer.teleport(new Location(Bukkit.getWorld("world"), -21.4, 119.0, 2201.4, -90.0F, 0.0F));
		}
	}
	
	public int getTeams() {
		List<Integer> games = new ArrayList<Integer>();
		for(String players : getPlayers().keySet()) {
			if(!games.contains(getPlayers().get(players))) {
				games.add(getPlayers().get(players));
			}
		}
		return games.size();
	}
	
	public int getTeamSize(int team) {
		int count = 0;
		for(String players : getPlayers().keySet()) {
			if(getPlayers().get(players) == team) count++;
		}
		return count;
	}
	
	public List<Kit> getAvailableKits(int team) {
		List<Kit> list = new ArrayList<Kit>();
		for(Kit kits : Kit.values()) {
			if(!isUsed(kits, team)) list.add(kits);
		}
		return list;
	}
	
	public int getKitUsage(Kit kit, int team) {
		int count = 0;
		for(String members : getPlayers().keySet()) {
			if(getPlayers().get(members) == team) {
				if(getKit(members) == kit) count++;
			}
		}
		return count;
	}
	
	public boolean isUsed(Kit kit, int team) {
		return getPercentInt(getTeamSize(team), getKitUsage(kit, team)) > 19;
	}
	
	public double getUsedP(Kit kit, int team) {
		return getPercentInt(getTeamSize(team), getKitUsage(kit, team));
	}
	
	public double getPercent(int total, int most) {
		if(total == 0 || most == 0) return 0.0D;
		double tot = (double) total;
		double fmost = (double) most;
		double percent = (tot / fmost) * 100;
		return percent;
	}
	
	public int getPercentInt(int total, int most) {
		if(total == 0 || most == 0) return 0;
		double percent = (total / most) * 100;
		return (int) percent;
	}
	
	public Kit getKit(String player) {
		if(!kits.containsKey(player)) return Kit.NONE;
		return kits.get(player);
	}
	
	public void start() {
		if(getPlayers().size() < 2) {
			M.v("Game", ChatColor.YELLOW + getFormatName() + ChatColor.WHITE + " ended (" + ChatColor.LIGHT_PURPLE + "Insufficient Players" + ChatColor.WHITE + ")");
			for(String players : getPlayers().keySet()) {
				Player player = Bukkit.getPlayer(players);
				if(player != null) {
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					player.teleport(Games.getArenaSpec());
					player.getInventory().setContents(HPlayer.get(players).getInventory(getName()));
					player.getInventory().setArmorContents(HPlayer.get(players).getInventory(getName() + "_armour"));
				}
			}
			return;
		}
		for(String players : getPlayers().keySet()) {
			Player player = Bukkit.getPlayerExact(players);
			if(player != null) {
				Money.subtractMoney(player, getCost());
				if(getTeleport()) {
					player.teleport(getSpawn(getTeam(player) - 1));
				}
			} else {
				getPlayers().remove(players); //might have to change - make method removePlayer()
			}
		}
		//M.v(getFormatName(), ChatColor.WHITE + "Game has begun!");
	}
	
	public void setKit(Player player, Kit kit) {
		if(getType() != GameType.KIT_ARENA) return;
		player.getInventory().clear();
		if(kit == Kit.ARCHER) {
			player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
			player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
			player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
			player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
			player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
			for(int i = 1; i < 6; i++) player.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
			player.getInventory().setItem(6, new ItemStack(Material.IRON_AXE));
			player.getInventory().setItem(7, new ItemStack(Material.COOKED_BEEF, 8));
			player.getInventory().setItem(8, new ItemStack(Material.BOW));
			player.getInventory().setItem(9, new ItemStack(Material.ARROW, 24));
		}
		if(kit == Kit.AGILITY) {
			player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
			player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
			player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
			player.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS));
			player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
			for(int i = 1; i < 7; i++) player.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
			player.getInventory().setItem(7, new ItemStack(Material.GOLD_AXE));
			player.getInventory().setItem(8, new ItemStack(Material.COOKED_BEEF, 8));
		}
		if(kit == Kit.SPECIALIST) {
			player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
			player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
			player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
			player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
			for(int i = 1; i < 7; i++) player.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
			player.getInventory().setItem(7, new ItemStack(Material.IRON_AXE));
			player.getInventory().setItem(8, new ItemStack(Material.COOKED_BEEF, 8));
		}
		if(kit == Kit.SAMURAI) {
			player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
			player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
			player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
			for(int i = 1; i < 7; i++) player.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
			player.getInventory().setItem(7, new ItemStack(Material.IRON_AXE));
			player.getInventory().setItem(8, new ItemStack(Material.COOKED_BEEF, 8));
		}
		if(kit == Kit.JUGGERNAUT) {
			player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
			player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
			player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
			player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
			for(int i = 1; i < 6; i++) player.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
			player.getInventory().setItem(6, new ItemStack(Material.IRON_AXE));
			player.getInventory().setItem(7, new ItemStack(Material.COOKED_BEEF, 8));
			player.getInventory().setItem(8, new ItemStack(Material.BOW));
			player.getInventory().setItem(9, new ItemStack(Material.ARROW, 24));
		}
		kits.put(player.getName(), kit);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setRemainingAir(300);
	}
	
	public void initiate() {
		//M.v(getFormatName(), ChatColor.YELLOW + getFormatName() + ChatColor.WHITE + "is enabled");
		setStatus(GameStatus.LOBBY);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(Hype.plugin, new Runnable() {
			
			public void run() {
				start();
			}
		}, 1200L);
	}
	
	public String getRemaining() {
		//return utilTime.convertString((getLastRun() + 7200000) - System.currentTimeMillis(), TimeUnit.BEST, 1);
		return "0.0 Seconds";
	}
	
	public double getRemainingTime() {
		//return utilTime.convert((getLastRun() + 7200000) - System.currentTimeMillis(), TimeUnit.BEST, 1);
		return -1.0;
	}
}
