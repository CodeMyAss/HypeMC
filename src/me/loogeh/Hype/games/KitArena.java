package me.loogeh.Hype.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.loogeh.Hype.C;
import me.loogeh.Hype.HPlayer;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.M;
import me.loogeh.Hype.MetaItem;
import me.loogeh.Hype.armour.Armour.Kit;
import me.loogeh.Hype.economy.Money;
import me.loogeh.Hype.games.Games.GameStatus;
import me.loogeh.Hype.util.ItemLore;
import me.loogeh.Hype.util.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitArena {
	public static Hype plugin = Hype.plugin;
	
	private static HashMap<String, List<Challenge>> challenges = new HashMap<String, List<Challenge>>();
	private static ItemStack EMPTY_BUCKET = new ItemStack(Material.BUCKET);
	private static ItemStack FILLED_BUCKET = new ItemStack(Material.WATER_BUCKET);
	
	public static Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Kit Selection");
	
	public static void addLoreItem(Inventory inv, ItemStack item, String name, List<String> lore, int slot) {
		MetaItem mitem = new MetaItem(name, item, lore);
		inv.setItem(slot, mitem.getItem());
	}
	
	public static ItemStack getLoreItem(ItemStack item, String name, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		return item;
	}
	
	public static void addNameItem(Inventory inv, ItemStack item, String name, int slot) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		inv.setItem(slot, item);
	}
	
	public static void openKitSelection(Player player) {
		if(Games.games.get("kit_arena").isUsed(Kit.ARCHER, Games.games.get("kit_arena").getTeam(player))) addLoreItem(inv, FILLED_BUCKET, ChatColor.RED + "Unavailable", ItemLore.KIT_ARENA_WOOL_RED, 0);
		else addLoreItem(inv, EMPTY_BUCKET, ChatColor.GREEN + "Available", ItemLore.KIT_ARENA_WOOL_GREEN, 0);
		addNameItem(inv, new ItemStack(Material.LEATHER_HELMET), C.shopItem + "Archer's Helmet", 9);
		addNameItem(inv, new ItemStack(Material.LEATHER_CHESTPLATE), C.shopItem + "Archer's Chest", 18);
		addNameItem(inv, new ItemStack(Material.LEATHER_LEGGINGS), C.shopItem + "Archer's Legs", 27);
		addNameItem(inv, new ItemStack(Material.LEATHER_BOOTS), C.shopItem + "Archer's Boots", 36);
		if(Games.games.get("kit_arena").isUsed(Kit.AGILITY, Games.games.get("kit_arena").getTeam(player))) addLoreItem(inv, FILLED_BUCKET, ChatColor.RED + "Unavailable", ItemLore.KIT_ARENA_WOOL_RED, 2);
		else addLoreItem(inv, EMPTY_BUCKET, ChatColor.GREEN + "Available", ItemLore.KIT_ARENA_WOOL_GREEN, 2);
		addNameItem(inv, new ItemStack(Material.GOLD_HELMET), C.shopItem + "Agility's Helmet", 11);
		addNameItem(inv, new ItemStack(Material.GOLD_CHESTPLATE), C.shopItem + "Agility's Chest", 20);
		addNameItem(inv, new ItemStack(Material.GOLD_LEGGINGS), C.shopItem + "Agility's Legs", 29);
		addNameItem(inv, new ItemStack(Material.GOLD_BOOTS), C.shopItem + "Agility's Boots", 38);
		if(Games.games.get("kit_arena").isUsed(Kit.SPECIALIST, Games.games.get("kit_arena").getTeam(player))) addLoreItem(inv, FILLED_BUCKET, ChatColor.RED + "Unavailable", ItemLore.KIT_ARENA_WOOL_RED, 4);
		else addLoreItem(inv, EMPTY_BUCKET, ChatColor.GREEN + "Available", ItemLore.KIT_ARENA_WOOL_GREEN, 4);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_HELMET), C.shopItem + "Specialist's Helmet", 13);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_CHESTPLATE), C.shopItem + "Specialist's Chest", 22);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_LEGGINGS), C.shopItem + "Specialist's Legs", 31);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_BOOTS), C.shopItem + "Specialist's Boots", 40);
		if(Games.games.get("kit_arena").isUsed(Kit.SAMURAI, Games.games.get("kit_arena").getTeam(player))) addLoreItem(inv, FILLED_BUCKET, ChatColor.RED + "Unavailable", ItemLore.KIT_ARENA_WOOL_RED, 6);
		else addLoreItem(inv, EMPTY_BUCKET, ChatColor.GREEN + "Available", ItemLore.KIT_ARENA_WOOL_GREEN, 6);
		addNameItem(inv, new ItemStack(Material.IRON_HELMET), C.shopItem + "Samurai's Helmet", 15);
		addNameItem(inv, new ItemStack(Material.IRON_CHESTPLATE), C.shopItem + "Samurai's Chest", 24);
		addNameItem(inv, new ItemStack(Material.IRON_LEGGINGS), C.shopItem + "Samurai's Legs", 33);
		addNameItem(inv, new ItemStack(Material.IRON_BOOTS), C.shopItem + "Samurai's Boots", 42);
		if(Games.games.get("kit_arena").isUsed(Kit.JUGGERNAUT, Games.games.get("kit_arena").getTeam(player))) addLoreItem(inv, FILLED_BUCKET, ChatColor.RED + "Unavailable", ItemLore.KIT_ARENA_WOOL_RED, 8);
		else addLoreItem(inv, EMPTY_BUCKET, ChatColor.GREEN + "Available", ItemLore.KIT_ARENA_WOOL_GREEN, 8);
		addNameItem(inv, new ItemStack(Material.DIAMOND_HELMET), C.shopItem + "Jugg's Helmet", 17);
		addNameItem(inv, new ItemStack(Material.DIAMOND_CHESTPLATE), C.shopItem + "Jugg's Chest", 26);
		addNameItem(inv, new ItemStack(Material.DIAMOND_LEGGINGS), C.shopItem + "Jugg's Legs", 35);
		addNameItem(inv, new ItemStack(Material.DIAMOND_BOOTS), C.shopItem + "Jugg's Boots", 44);
		player.openInventory(inv);
	}
	
	public static ItemStack[] getKitSelContents(Player player) {
		Inventory inv = Bukkit.createInventory(null, 54);
		if(get().isUsed(Kit.ARCHER, get().getTeam(player))) inv.setItem(0, getLoreItem(FILLED_BUCKET, ChatColor.RED + "Unavailable", ItemLore.KIT_ARENA_WOOL_RED));
		else inv.setItem(0, getLoreItem(EMPTY_BUCKET, ChatColor.GREEN + "Available", ItemLore.KIT_ARENA_WOOL_GREEN));
		addNameItem(inv, new ItemStack(Material.LEATHER_HELMET), C.shopItem + "Archer's Helmet", 9);
		addNameItem(inv, new ItemStack(Material.LEATHER_CHESTPLATE), C.shopItem + "Archer's Chest", 18);
		addNameItem(inv, new ItemStack(Material.LEATHER_LEGGINGS), C.shopItem + "Archer's Legs", 27);
		addNameItem(inv, new ItemStack(Material.LEATHER_BOOTS), C.shopItem + "Archer's Boots", 36);
		if(get().isUsed(Kit.ARCHER, get().getTeam(player))) inv.setItem(2, getLoreItem(FILLED_BUCKET, ChatColor.RED + "Unavailable", ItemLore.KIT_ARENA_WOOL_RED));
		else inv.setItem(2, getLoreItem(EMPTY_BUCKET, ChatColor.GREEN + "Available", ItemLore.KIT_ARENA_WOOL_GREEN));
		addNameItem(inv, new ItemStack(Material.GOLD_HELMET), C.shopItem + "Agility's Helmet", 11);
		addNameItem(inv, new ItemStack(Material.GOLD_CHESTPLATE), C.shopItem + "Agility's Chest", 20);
		addNameItem(inv, new ItemStack(Material.GOLD_LEGGINGS), C.shopItem + "Agility's Legs", 29);
		addNameItem(inv, new ItemStack(Material.GOLD_BOOTS), C.shopItem + "Agility's Boots", 38);
		if(get().isUsed(Kit.ARCHER, get().getTeam(player))) inv.setItem(4, getLoreItem(FILLED_BUCKET, ChatColor.RED + "Unavailable", ItemLore.KIT_ARENA_WOOL_RED));
		else inv.setItem(4, getLoreItem(EMPTY_BUCKET, ChatColor.GREEN + "Available", ItemLore.KIT_ARENA_WOOL_GREEN));
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_HELMET), C.shopItem + "Specialist's Helmet", 13);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_CHESTPLATE), C.shopItem + "Specialist's Chest", 22);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_LEGGINGS), C.shopItem + "Specialist's Legs", 31);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_BOOTS), C.shopItem + "Specialist's Boots", 40);
		if(get().isUsed(Kit.ARCHER, get().getTeam(player))) inv.setItem(6, getLoreItem(FILLED_BUCKET, ChatColor.RED + "Unavailable", ItemLore.KIT_ARENA_WOOL_RED));
		else inv.setItem(6, getLoreItem(EMPTY_BUCKET, ChatColor.GREEN + "Available", ItemLore.KIT_ARENA_WOOL_GREEN));
		addNameItem(inv, new ItemStack(Material.IRON_HELMET), C.shopItem + "Samurai's Helmet", 15);
		addNameItem(inv, new ItemStack(Material.IRON_CHESTPLATE), C.shopItem + "Samurai's Chest", 24);
		addNameItem(inv, new ItemStack(Material.IRON_LEGGINGS), C.shopItem + "Samurai's Legs", 33);
		addNameItem(inv, new ItemStack(Material.IRON_BOOTS), C.shopItem + "Samurai's Boots", 42);
		if(get().isUsed(Kit.ARCHER, get().getTeam(player))) inv.setItem(8, getLoreItem(FILLED_BUCKET, ChatColor.RED + "Unavailable", ItemLore.KIT_ARENA_WOOL_RED));
		else inv.setItem(8, getLoreItem(EMPTY_BUCKET, ChatColor.GREEN + "Available", ItemLore.KIT_ARENA_WOOL_GREEN));
		addNameItem(inv, new ItemStack(Material.DIAMOND_HELMET), C.shopItem + "Jugg's Helmet", 17);
		addNameItem(inv, new ItemStack(Material.DIAMOND_CHESTPLATE), C.shopItem + "Jugg's Chest", 26);
		addNameItem(inv, new ItemStack(Material.DIAMOND_LEGGINGS), C.shopItem + "Jugg's Legs", 35);
		addNameItem(inv, new ItemStack(Material.DIAMOND_BOOTS), C.shopItem + "Jugg's Boots", 44);
		return inv.getContents();
	}
	
	public static void openKitDescSel(Player player) {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.LIGHT_PURPLE + "Kit Description Selection");
		addNameItem(inv, new ItemStack(Material.LEATHER_HELMET), C.shopItem + "Archer's Helmet", 9);
		addNameItem(inv, new ItemStack(Material.LEATHER_CHESTPLATE), C.shopItem + "Archer's Chest", 18);
		addNameItem(inv, new ItemStack(Material.LEATHER_LEGGINGS), C.shopItem + "Archer's Legs", 27);
		addNameItem(inv, new ItemStack(Material.LEATHER_BOOTS), C.shopItem + "Archer's Boots", 36);
		addNameItem(inv, new ItemStack(Material.GOLD_HELMET), C.shopItem + "Agility's Helmet", 11);
		addNameItem(inv, new ItemStack(Material.GOLD_CHESTPLATE), C.shopItem + "Agility's Chest", 20);
		addNameItem(inv, new ItemStack(Material.GOLD_LEGGINGS), C.shopItem + "Agility's Legs", 29);
		addNameItem(inv, new ItemStack(Material.GOLD_BOOTS), C.shopItem + "Agility's Boots", 38);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_HELMET), C.shopItem + "Specialist's Helmet", 13);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_CHESTPLATE), C.shopItem + "Specialist's Chest", 22);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_LEGGINGS), C.shopItem + "Specialist's Legs", 31);
		addNameItem(inv, new ItemStack(Material.CHAINMAIL_BOOTS), C.shopItem + "Specialist's Boots", 40);
		addNameItem(inv, new ItemStack(Material.IRON_HELMET), C.shopItem + "Samurai's Helmet", 15);
		addNameItem(inv, new ItemStack(Material.IRON_CHESTPLATE), C.shopItem + "Samurai's Chest", 24);
		addNameItem(inv, new ItemStack(Material.IRON_LEGGINGS), C.shopItem + "Samurai's Legs", 33);
		addNameItem(inv, new ItemStack(Material.IRON_BOOTS), C.shopItem + "Samurai's Boots", 42);
		addNameItem(inv, new ItemStack(Material.DIAMOND_HELMET), C.shopItem + "Jugg's Helmet", 17);
		addNameItem(inv, new ItemStack(Material.DIAMOND_CHESTPLATE), C.shopItem + "Jugg's Chest", 26);
		addNameItem(inv, new ItemStack(Material.DIAMOND_LEGGINGS), C.shopItem + "Jugg's Legs", 35);
		addNameItem(inv, new ItemStack(Material.DIAMOND_BOOTS), C.shopItem + "Jugg's Boots", 44);
		player.openInventory(inv);
	}
	
	
	@SuppressWarnings("deprecation")
	public static void updateKitSelection(Player player) {
		if(player.getOpenInventory() == null) return;
		if(player.getOpenInventory().getTopInventory().getTitle().contains("Kit Selection")) player.getOpenInventory().getTopInventory().setContents(getKitSelContents(player));
		player.updateInventory();
	}
	
	public static void updateInventory() {
		for(HumanEntity entities : inv.getViewers()) {
			if(entities instanceof Player) {
				Player player = (Player) entities;
				updateKitSelection(player);
			}
		}
	}
	
	public static int count(Kit kit) {
		int count = 0;
		for(String key : get().getKitMap().keySet()) {
			if(get().getKitMap().get(key) == kit) count++;
		}
		return count;
	}
	
	public static void die(Player player, PlayerDeathEvent event) {
		event.getDrops().clear();
		Utilities.skipRespawn(player);
		if(player.getKiller() == null) message(ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " was eliminated");
		else message(ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " was eliminated by " + ChatColor.YELLOW + player.getKiller());
		player.teleport(Games.getArenaSpec());
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.getInventory().setContents(HPlayer.get(player.getName()).getInventory("kit_arena"));
		player.getInventory().setArmorContents(HPlayer.get(player.getName()).getInventory("kit_arena_armour"));
		if(get().getTeamSize(0) < 2 || get().getTeamSize(1) < 2) {
			if(get().getTeamSize(1) == 0 && get().getTeamSize(2) > 0) {
				for(String keys : get().getPlayers().keySet()) {
					if(get().getPlayers().get(keys) == 2) {
						Player winner = Bukkit.getPlayer(keys);
						winner.teleport(Games.getArenaSpec());
						winner.getInventory().clear();
						winner.getInventory().setArmorContents(null);
						if(HPlayer.get(winner.getName()) != null) {
							winner.getInventory().setContents(HPlayer.get(winner.getName()).getInventory("kit_arena"));
							winner.getInventory().setArmorContents(HPlayer.get(winner.getName()).getInventory("kit_arena_armour"));
						}
						Money.addMoney(winner, get().getCost() * 2);
					} else {
						Player loser = Bukkit.getPlayer(keys);
						loser.teleport(Games.getArenaSpec());
						loser.getInventory().clear();
						loser.getInventory().setArmorContents(null);
						if(HPlayer.get(loser.getName()) != null) {
							loser.getInventory().setContents(HPlayer.get(loser.getName()).getInventory("kit_arena"));
							loser.getInventory().setArmorContents(HPlayer.get(loser.getName()).getInventory("kit_arena_armour"));
						}
					}
					get().getPlayers().clear();
				}
			}
			if(get().getTeamSize(0) > 0 && get().getTeamSize(1) == 0) {
				for(String keys : get().getPlayers().keySet()) {
					if(get().getPlayers().get(keys) == 1) {
						Player winner = Bukkit.getPlayer(keys);
						winner.teleport(Games.getArenaSpec());
						winner.getInventory().clear();
						winner.getInventory().setArmorContents(null);
						if(HPlayer.get(winner.getName()) != null) {
							winner.getInventory().setContents(HPlayer.get(winner.getName()).getInventory("kit_arena"));
							winner.getInventory().setArmorContents(HPlayer.get(winner.getName()).getInventory("kit_arena_armour"));
						}
						Money.addMoney(winner, get().getCost() * 2);
					} else {
						Player loser = Bukkit.getPlayer(keys);
						loser.teleport(Games.getArenaSpec());
						loser.getInventory().clear();
						loser.getInventory().setArmorContents(null);
						if(HPlayer.get(loser.getName()) != null) {
							loser.getInventory().setContents(HPlayer.get(loser.getName()).getInventory("kit_arena"));
							loser.getInventory().setArmorContents(HPlayer.get(loser.getName()).getInventory("kit_arena_armour"));
						}
					}
					get().getPlayers().clear();
				}
			}
		}
	}
	
	public static List<Location> getSpawns() {
		List<Location> list = new ArrayList<Location>();
		list.add(new Location(Bukkit.getWorld("world"), -9.34, 119.1, 2169.5, -45.5F, 0.0F));
		list.add(new Location(Bukkit.getWorld("world"), 52.5, 119.1, 2234.0, 138.1F, 0.0F));
		return list;
	}
	
	public static void sendHelp(Player player) {
		player.sendMessage(ChatColor.AQUA + "Kit Arena - " + ChatColor.WHITE + "Help");
		M.h(player, "/ka join", "Joins the game");
		M.h(player, "/ka leave", "Leaves your current game");
		M.h(player, "/ka spec", "Spectates the current game");
		M.h(player, "/ka challenge <player> <cost>", "Challenge a player at a cost");
		M.h(player, "/ka stats", "View your stats - COMING SOON");
	}
	
	public static Game get() {
		return Games.get("kit_arena");
	}
	
	public static void join(Player player) {
		get().join(player);
	}
	
	public static void leave(Player player) {
		get().removePlayer(player.getName());
	}
	
	public static void sendInfo(Player player) {
		player.sendMessage(ChatColor.BOLD + get().getFormatName());
		player.sendMessage(ChatColor.BOLD + get().getStatus().toString());
		for(int i = 0; i < 2; i++) {
			player.sendMessage("Team " + i + ChatColor.YELLOW + " " + get().getTeamSize(i));
		}
		for(Kit kits : Kit.values()) {
			player.sendMessage(kits.toString() + " usage " + count(kits));
			for(int team = 0; team < get().getTeams(); team++) {
				player.sendMessage(ChatColor.YELLOW + "" + get().getUsedP(kits, team) + "%");
			}
		}
		String playerStr = "";
		for(String players : get().getPlayers().keySet()) {
			playerStr = playerStr + players + ", ";
		}
		player.sendMessage(ChatColor.BOLD + Utilities.removeLastIntChars(playerStr, 2));
		player.sendMessage(ChatColor.YELLOW + "" + get().getBestTeam());
	}
	
	public static void challenge(Player player, Player target, int cost) {
		if(player == null || target == null) return;
		int money = Money.getMoney(player);
		int tMoney = Money.getMoney(target);
		if(cost < 1000) {
			M.v(player, "Kit Arena", ChatColor.WHITE + "The cost must be over " + ChatColor.DARK_GREEN + "$1000");
			return;
		}
		if(cost > money) {
			M.v(player, "Kit Arena", ChatColor.WHITE + "You have insufficient " + ChatColor.YELLOW + "Money");
			return;
		}
		if(cost > tMoney) {
			M.v(player, "Kit Arena", ChatColor.YELLOW + target.getName() + ChatColor.WHITE + " has insufficient " + ChatColor.YELLOW + "Money");
			return;
		}
		if(!challenges.containsKey(target.getName())) {
			List<Challenge> list = new ArrayList<Challenge>();
			list.add(new Challenge("kit_arena", target.getName(), player.getName(), cost));
			challenges.put(target.getName(), list);
			return;
		}
		challenges.get(target.getName()).add(new Challenge("kit_arena", target.getName(), player.getName(), cost));
	}
	
	public static void acceptChallenge(Player player, Player target) {
		if(player ==  null) return;
		if(target == null) {
			M.v(player, "Server", ChatColor.WHITE + "Invalid player");
			return;
		}
		if(getChallenge(player, target) == null) {
			M.v(player, "Kit Arena", ChatColor.YELLOW + target.getName() + ChatColor.WHITE + " has not challenged you");
			return;
		}
		getChallenge(player, target).accept();
	}
	
	public static Challenge getChallenge(Player player, Player target) {
		if(!challenges.containsKey(target.getName())) return null;
		if(!challenges.get(target.getName()).contains(player.getName())) return null;
		for(Challenge c : challenges.get(target.getName())) {
			if(c.getChallenger().equalsIgnoreCase(player.getName())) return c;
		}
		return null;
	}
	
	public static void removeChallenges(String player) {
		for(String challenged : challenges.keySet()) {
			if(challenges.get(challenged).contains(player)) challenges.get(challenged).remove(player);
		}
	}
	
	public static void removeChallenge(String player, String target) {
		if(!challenges.containsKey(target)) return;
		if(!challenges.get(target).contains(player)) return;
		challenges.get(target).remove(player);
	}
	
	public static boolean areFair() {
		int team_one = get().getTeamSize(1);
		int team_two = get().getTeamSize(2);
		if(team_one - team_two != 1 && team_one - team_two != -1 && team_one - team_two != 0) return false;
		return true;
	}
	
	public static void message(String message) {
		for(String keys : get().getPlayers().keySet()) {
			Player player = Bukkit.getPlayer(keys);
			if(player != null) player.sendMessage(ChatColor.BLUE + "Kit Arena - " + message);
		}
	}
	
	
	static class Challenge {
		
		private String gameID;
		private String challenged;
		private String challenger;
		private int cost;
		
		public Challenge(String gameID, String challenged, String challenger, int cost) {
			this.gameID = gameID;
			this.challenged = challenged;
			this.challenger = challenger;
			this.cost = cost;
		}
		
		public String getID() {
			return this.gameID;
		}
		
		public String getChallenger() {
			return this.challenger;
		}
		
		public int getCost() {
			return this.cost;
		}
		
		public String getChallenged() {
			return this.challenged;
		}
		
		public boolean accept() {
			if(!gameID.equals("kit_arena")) return false;
			Player player = Bukkit.getPlayer(challenged);
			Player target = Bukkit.getPlayer(challenger);
			if(player == null) return false;
			if(KitArena.get().getStatus() == GameStatus.LOBBY || KitArena.get().getStatus() == GameStatus.PLAYING) {
				M.v(player, "Kit Arena", ChatColor.YELLOW + "Kit Arena " + ChatColor.WHITE + "is already in game");
				return false;
			}
			if(KitArena.get().getRemainingTime() > 0.0) {
				M.v(player, "Kit Arena", ChatColor.WHITE + "Starting in " + ChatColor.YELLOW + KitArena.get().getRemaining());
				return false;
			}
			if(target == null) {
				M.v(player, "Kit Arena", ChatColor.YELLOW + challenger + ChatColor.WHITE + " is offline");
				return false;
			}
			if(Money.getMoney(player) < getCost()) {
				M.v(player, "Challenge", ChatColor.WHITE + "You have " + ChatColor.YELLOW + "Insufficient Money");
				return false;
			}
			if(Money.getMoney(target) < getCost()) {
				M.v(player, "Challenge", ChatColor.YELLOW + target.getName() + ChatColor.WHITE + " has " + ChatColor.YELLOW + "Insufficient Money");
				return false;
			}
			get().setCost(getCost());
			M.v("Kit Arena", ChatColor.YELLOW + challenged + ChatColor.WHITE + " accepted " + ChatColor.YELLOW + challenger + "'s "  + ChatColor.WHITE + " of " + ChatColor.GREEN + "$" + cost);
			player.teleport(KitArena.get().getLobby());
			target.teleport(KitArena.get().getLobby());
			return true;
		}
	}
}
