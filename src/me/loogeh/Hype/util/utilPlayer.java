package me.loogeh.Hype.util;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.Ignore;
import me.loogeh.Hype.M;
import me.loogeh.Hype.RCommands;
import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.armour.Armour.Kit;
import me.loogeh.Hype.moderation.Mute;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;


public class utilPlayer {
	public static Hype plugin;
	public static Set<String> cloakPlayers = new HashSet<String>(); 

	public static boolean isInWater(Player player) {
		if (player.getWorld().getBlockAt(player.getLocation()).getType() == Material.WATER
				|| player.getWorld().getBlockAt(player.getLocation()).getType() == Material.STATIONARY_WATER) {
			return true;
		}
		return false;
	}

	public static boolean isInLiquid(Player player) {
		if (player.getWorld().getBlockAt(player.getLocation()).getType() == Material.WATER
				|| player.getWorld().getBlockAt(player.getLocation()).getType() == Material.STATIONARY_WATER
				|| player.getWorld().getBlockAt(player.getLocation()).getType() == Material.LAVA
				|| player.getWorld().getBlockAt(player.getLocation()).getType() == Material.STATIONARY_LAVA) {
			return true;
		}
		return false;
	}

	public static boolean isInLava(Player player) {
		if (player.getWorld().getBlockAt(player.getLocation()).getType() == Material.LAVA
				|| player.getWorld().getBlockAt(player.getLocation()).getType() == Material.STATIONARY_LAVA) {
			return true;
		}
		return false;
	}

	public static void abilityMessage(Player player, String ability) {
		player.sendMessage(ChatColor.GRAY + "You used " + ChatColor.AQUA + WordUtils.capitalize(ability.toLowerCase()));
	}

	public static boolean hasSwords(Player player) {
		if(player.getItemInHand().getType() == Material.DIAMOND_SWORD ||
				player.getItemInHand().getType() == Material.IRON_SWORD ||
				player.getItemInHand().getType() == Material.STONE_SWORD ||
				player.getItemInHand().getType() == Material.GOLD_SWORD ||
				player.getItemInHand().getType() == Material.WOOD_SWORD) {
			return true;
		}
		return false;

	}
	public static boolean hasShovels(Player player) {
		if(player.getItemInHand().getType() == Material.DIAMOND_SPADE ||
				player.getItemInHand().getType() == Material.IRON_SPADE ||
				player.getItemInHand().getType() == Material.STONE_SPADE ||
				player.getItemInHand().getType() == Material.GOLD_SPADE ||
				player.getItemInHand().getType() == Material.WOOD_SPADE) {
			return true;
		}
		return false;

	}
	public static boolean hasHoes(Player player) {
		if(player.getItemInHand().getType() == Material.DIAMOND_HOE ||
				player.getItemInHand().getType() == Material.IRON_HOE ||
				player.getItemInHand().getType() == Material.STONE_HOE ||
				player.getItemInHand().getType() == Material.GOLD_HOE ||
				player.getItemInHand().getType() == Material.WOOD_HOE) {
			return true;
		}
		return false;

	}
	public static boolean hasPicks(Player player) {
		if(player.getItemInHand().getType() == Material.DIAMOND_PICKAXE ||
				player.getItemInHand().getType() == Material.IRON_PICKAXE ||
				player.getItemInHand().getType() == Material.STONE_PICKAXE ||
				player.getItemInHand().getType() == Material.GOLD_PICKAXE ||
				player.getItemInHand().getType() == Material.WOOD_PICKAXE) {
			return true;
		}
		return false;

	}
	public static boolean hasAxes(Player player) {
		if(player.getItemInHand().getType() == Material.DIAMOND_AXE ||
				player.getItemInHand().getType() == Material.IRON_AXE ||
				player.getItemInHand().getType() == Material.STONE_AXE ||
				player.getItemInHand().getType() == Material.GOLD_AXE ||
				player.getItemInHand().getType() == Material.WOOD_AXE) {
			return true;
		}
		return false;

	}

	public static boolean hasTools(Player player) {
		return ((hasAxes(player)) || (hasShovels(player)) || (hasPicks(player)));
	}

	public static boolean hasArmourInHand(Player player) {
		Material item = player.getItemInHand().getType();
		return (item == Material.DIAMOND_HELMET ||
				item == Material.DIAMOND_CHESTPLATE ||
				item == Material.DIAMOND_LEGGINGS || 
				item == Material.DIAMOND_BOOTS ||
				item == Material.IRON_HELMET ||
				item == Material.IRON_CHESTPLATE ||
				item == Material.IRON_LEGGINGS ||
				item == Material.IRON_BOOTS ||
				item == Material.GOLD_HELMET ||
				item == Material.GOLD_CHESTPLATE ||
				item == Material.GOLD_LEGGINGS ||
				item == Material.GOLD_BOOTS ||
				item == Material.CHAINMAIL_HELMET ||
				item == Material.CHAINMAIL_CHESTPLATE ||
				item == Material.CHAINMAIL_LEGGINGS ||
				item == Material.CHAINMAIL_BOOTS ||
				item == Material.LEATHER_HELMET ||
				item == Material.LEATHER_CHESTPLATE ||
				item == Material.LEATHER_LEGGINGS ||
				item == Material.LEATHER_BOOTS);
	}

	public static void addSpecialist(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
		player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
	}




	public static void addSamuri(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
		player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
	}




	public static void addAgility(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
		player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
		player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
		player.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS));
	}




	public static void addArcher(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
		player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
		player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
		player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
	}



	public static void addJuggernaut(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
	}

	public static void arenaRed(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 14));
		player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
	}

	public static void arenaBlue(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 11));
		player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
	}

	public static void arenaInv(Player player) {
		player.getInventory().clear();
		player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
		player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(new ItemStack(Material.BOW));
		player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
		player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 8));
	}

	@SuppressWarnings("deprecation")
	public static Location isLocationNearBlock(Location loc, List<Integer> blocks, int radius) {
		World world = loc.getWorld();
		int x = (int) loc.getX(), y = (int) loc.getY(), z = (int) loc.getZ();

		for (int ox = -radius; ox <= radius; ox++) {
			for (int oy = -radius; oy <= radius; oy++) {
				for (int oz = -radius; oz <= radius; oz++) {
					if ((ox^2 + oy^2 + oz^2) <= radius){
						if (blocks.contains(world.getBlockAt(x + ox, y + oy, z + oz).getTypeId())) {
							return world.getBlockAt(x + ox, y + oy, z + oz).getLocation();
						}
					}
				}
			}
		}
		return null;
	}

	public static void clearInvent(Player player) {
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
	}


	public static Player getRandomPlayer(){
		Player[] player = Bukkit.getOnlinePlayers();
		Player randomPlayer = player[new Random().nextInt(player.length - 1)];
		return randomPlayer;
	}

	public static void leap(Player player, int x) {
		player.setVelocity(player.getVelocity().multiply(x));
	}

	public static void reverseLeap(Player player, int x) {
		player.setVelocity(player.getVelocity().multiply(- x));
	}


	public static void tph(Player player, Player tPlayer) {
		if(tPlayer == null) {
			player.sendMessage(ChatColor.GREEN + "Player" + ChatColor.GRAY + " is offline.");
		} else {
			Location l = player.getLocation();
			tPlayer.teleport(l);
			player.sendMessage(ChatColor.GREEN + tPlayer.getName() + ChatColor.GRAY + " has been teleported to you.");
			tPlayer.sendMessage(ChatColor.GRAY + "You have teleported to " + ChatColor.GREEN + player.getName());
		}
	}

	public static void setKit(Player player, Kit kit) {
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
	}

	public static void kitArcher(Player player) {
		player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET));
		player.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
		player.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS));
		player.getInventory().addItem(new ItemStack(Material.LEATHER_BOOTS));
		player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
		player.getInventory().addItem(new ItemStack(Material.IRON_AXE, 1));
		player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 6));
		player.getInventory().addItem(new ItemStack(Material.BOW, 1));
		player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
	}
	
	public static void kitJuggernaut(Player player) {
		player.getInventory().addItem(new ItemStack(Material.DIAMOND_HELMET));
		player.getInventory().addItem(new ItemStack(Material.DIAMOND_CHESTPLATE));
		player.getInventory().addItem(new ItemStack(Material.DIAMOND_LEGGINGS));
		player.getInventory().addItem(new ItemStack(Material.DIAMOND_BOOTS));
		player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
		player.getInventory().addItem(new ItemStack(Material.IRON_AXE, 1));
		player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 6));
	}

	public static void kitSamuri(Player player) {
		player.getInventory().addItem(new ItemStack(Material.IRON_HELMET));
		player.getInventory().addItem(new ItemStack(Material.IRON_CHESTPLATE));
		player.getInventory().addItem(new ItemStack(Material.IRON_LEGGINGS));
		player.getInventory().addItem(new ItemStack(Material.IRON_BOOTS));
		player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
		player.getInventory().addItem(new ItemStack(Material.IRON_AXE, 1));
		player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 6));
	}

	public static void kitAgility(Player player) {
		player.getInventory().addItem(new ItemStack(Material.GOLD_HELMET));
		player.getInventory().addItem(new ItemStack(Material.GOLD_CHESTPLATE));
		player.getInventory().addItem(new ItemStack(Material.GOLD_LEGGINGS));
		player.getInventory().addItem(new ItemStack(Material.GOLD_BOOTS));
		player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
		player.getInventory().addItem(new ItemStack(Material.IRON_AXE, 1));
		player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 6));
	}

	public static void kitSpecialist(Player player) {
		player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_HELMET));
		player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_BOOTS));
		player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
		player.getInventory().addItem(new ItemStack(Material.IRON_AXE, 1));
		player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 6));
	}


	public static void tptp(Player player, String[] args) {
		Player tplayer = player.getServer().getPlayer(args[0]);
		if(tplayer == null) {
			player.sendMessage(ChatColor.GREEN + "Player" + ChatColor.GRAY + " is offline.");
		} else {
			Location tl = tplayer.getLocation();
			player.teleport(tl);
			player.sendMessage(ChatColor.GRAY + "You teleported to " + ChatColor.GREEN + tplayer.getName());
			tplayer.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.GRAY + " teleported to you.");
		}
	}





	public static void tpplayertoplayer(CommandSender sender, String[] args) {
		Player tplayer = sender.getServer().getPlayer(args[0]);
		Player tplayer2 = sender.getServer().getPlayer(args[1]);
		if(tplayer == null || tplayer2 == null) {
			sender.sendMessage(ChatColor.GRAY + "Offline player/s");
		} else {
			Location tplayer2loc = tplayer2.getLocation();
			tplayer.teleport(tplayer2loc);
			tplayer.sendMessage(ChatColor.GRAY + "You were teleported to " + ChatColor.GREEN + tplayer2.getName() + ChatColor.GRAY + " by " + ChatColor.GREEN + sender.getName());
			sender.sendMessage(ChatColor.GREEN + tplayer.getName() + ChatColor.GRAY + " has been teleported to " + ChatColor.GREEN + tplayer2.getName());

		}
	}





	public static void tpCoords(String[] args, CommandSender sender, boolean isPlayer) {
		if (isPlayer){
			Player player = (Player) sender;
			Integer x = Utilities.stringToInt(args[0]);
			Integer y = Utilities.stringToInt(args[1]);
			Integer z = Utilities.stringToInt(args[2]);
			if (!(x == null || y == null || z == null)){
				player.teleport(new Location(player.getWorld(), (double) x, (double) y, (double) z));
				player.sendMessage(ChatColor.GRAY + "Teleported to coords: " + ChatColor.GREEN + x + ", " + y + ", " + z);
			}
			else{
				player.sendMessage(ChatColor.RED + "Invalid Coordinates");
			}
		}
		else{
			Player player = sender.getServer().getPlayer(args[3]);
			if (!(player == null)){
				Integer x = Utilities.stringToInt(args[0]);
				Integer y = Utilities.stringToInt(args[1]);
				Integer z = Utilities.stringToInt(args[2]);
				if (!(x == null || y == null || z == null)){
					player.teleport(new Location(player.getWorld(), (double) x, (double) y, (double) z));
					if (sender instanceof Player){
						if ((Player) sender == player){
							sender.sendMessage(ChatColor.GRAY + "Teleported to coords: " + ChatColor.GREEN + x + ", " + y + ", " + z);
						}
						else{
							sender.sendMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.GRAY + " was teleported to coords: " + ChatColor.GREEN + x + ", " + y + ", " + z);
							player.sendMessage(ChatColor.GRAY + "You were teleported to coords: " + ChatColor.GREEN + x + ", " + y + ", " + z + ChatColor.GRAY + " by " + ChatColor.GREEN + sender.getName());
						}
					}
					else{
						sender.sendMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.GRAY + " was teleported to coords: " + ChatColor.GREEN + x + ", " + y + ", " + z);
						player.sendMessage(ChatColor.GRAY + "You were teleported to coords: " + ChatColor.GREEN + x + ", " + y + ", " + z + ChatColor.GRAY + " by " + ChatColor.GREEN + sender.getName());
					}
				}
				else{
					sender.sendMessage(ChatColor.RED + "Incorrect syntax!");
				}
			}
			else{
				sender.sendMessage(ChatColor.RED + "Invalid Player");
			}
		}

	}




	public static void tpToSpawn(Player player) {
		Location loc = player.getWorld().getSpawnLocation();
		player.teleport(loc);
		player.sendMessage(ChatColor.GRAY + "You teleported to" + ChatColor.GREEN + " spawn");
	}




	public static void tpToWorld(CommandSender sender, String[] args, Player tplayer, World wn) {
		tplayer = sender.getServer().getPlayer(args[1]);
		Player player = (Player) sender;
		wn = Bukkit.getServer().getWorld(args[2]);
		Location wnl = wn.getSpawnLocation();
		if(tplayer == null) {
			player.teleport(wnl);
			player.sendMessage(ChatColor.GRAY + "You teleported to world " + ChatColor.GREEN + wn.getName());
		} else {
			tplayer.teleport(wnl);
			tplayer.sendMessage(ChatColor.GRAY + "You were teleported to " + ChatColor.GREEN + wn.getName() + ChatColor.GRAY + " by " + ChatColor.GREEN + player.getName());
			player.sendMessage(ChatColor.GRAY + "You teleported " + ChatColor.GREEN + tplayer.getName() + ChatColor.GRAY + " to " + ChatColor.GREEN + wn.getName());
		}
	}

	public static void permMsg(Player player) {
		M.v(player, "Server - ", ChatColor.WHITE + "You must be a higher " + ChatColor.GREEN + "Admin Rank");
	}

	public static void tpall(Player player) {
		Location t = player.getLocation();
		for(Player pls : utilServer.getPlayers()) {
			pls.teleport(t);
		}
	}

	public static void heal(Player player) {
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setRemainingAir(300);
		if(player.hasPotionEffect(PotionEffectType.POISON)){
			player.removePotionEffect(PotionEffectType.POISON);
		}
		player.sendMessage(ChatColor.GRAY + "You have been" + ChatColor.GREEN + " healed");

	}

	public static void healOther(Player player, String string) {
		Player targetPlayer = player.getServer().getPlayer(string);
		if(targetPlayer == null){
			player.sendMessage(ChatColor.GREEN + "Invalid player");
		}
		else{
			targetPlayer.setHealth(20);
			targetPlayer.setFoodLevel(20);
			targetPlayer.setFireTicks(0);
			targetPlayer.setRemainingAir(300);
			if(targetPlayer.hasPotionEffect(PotionEffectType.POISON)){
				targetPlayer.removePotionEffect(PotionEffectType.POISON);
			}
			player.sendMessage(ChatColor.GRAY + "You healed " + ChatColor.GREEN + targetPlayer.getDisplayName());
			targetPlayer.sendMessage(ChatColor.GRAY + "You have been" + ChatColor.GREEN + " healed" + ChatColor.GRAY + " by " + ChatColor.GREEN + player.getDisplayName());
		}
	}

	public static void healOtherConsole(CommandSender sender, String target) {
		Player targetPlayer = sender.getServer().getPlayer(target);
		if(targetPlayer == null){
			sender.sendMessage(ChatColor.GREEN + "Invalid Player");
		}
		else{
			targetPlayer.setHealth(20);
			targetPlayer.setFoodLevel(20);
			targetPlayer.setFireTicks(0);
			targetPlayer.setRemainingAir(300);
			if(targetPlayer.hasPotionEffect(PotionEffectType.POISON)){
				targetPlayer.removePotionEffect(PotionEffectType.POISON);
			}
			sender.sendMessage(ChatColor.GRAY + "You healed " + ChatColor.GREEN + targetPlayer.getDisplayName());
			targetPlayer.sendMessage(ChatColor.GRAY + "You have been" + ChatColor.GREEN + " healed" + ChatColor.GRAY + " by " + ChatColor.GREEN + sender.getName());
		}
	}

	public static boolean hasArmour(Player player) {
		return (utilArmour.hasSet(player, "DIAMOND") || (utilArmour.hasSet(player, "IRON") || (utilArmour.hasSet(player, "GOLD") || (utilArmour.hasSet(player, "CHAIN") || (utilArmour.hasSet(player, "LEATHER"))))));
	}

	public static boolean isInArea(Player player, int smallX, int bigX, int smallZ, int bigZ) {
		return (player.getLocation().getX() >= smallX && player.getLocation().getX() <= bigX && player.getLocation().getZ() >= smallZ && player.getLocation().getZ() <= bigZ);
	}
	public static boolean isInAreaY(Player player, int smallX, int bigX, int smallY, int bigY, int smallZ, int bigZ) {
		return (player.getLocation().getX() >= smallX && player.getLocation().getX() <= bigX && player.getLocation().getY() >= smallY && player.getLocation().getY() <= bigY && player.getLocation().getZ() >= smallZ && player.getLocation().getZ() <= bigZ);
	}

	public static void cloak(Player player) {
		if(cloakPlayers.contains(player.getName())) {
			for(Player players : Bukkit.getOnlinePlayers()) {
				players.showPlayer(player);
			}
			cloakPlayers.remove(player.getName());
			player.sendMessage(ChatColor.AQUA + "Cloak: " + ChatColor.WHITE + "Disabled");
		} else {
			for(Player players : Bukkit.getOnlinePlayers()) {
				players.hidePlayer(player);
			}
			cloakPlayers.add(player.getName());
			player.sendMessage(ChatColor.AQUA + "Cloak: " + ChatColor.WHITE + "Enabled");
		}
	}

	public static boolean adminMode(Player player) {
		return RCommands.adminMode.containsKey(player.getName());
	}

	public static void coolMessage(Player player, String variable) {
		if(player == null) {
			return;
		}
		player.sendMessage(ChatColor.GRAY + "You can now use " + ChatColor.AQUA + WordUtils.capitalize(variable.toLowerCase()));
	}
	
	public static String damageCauseToString(DamageCause cause) {
		if(cause == DamageCause.BLOCK_EXPLOSION) return "TNT";
		if(cause == DamageCause.CONTACT) return "Cactus";
		if(cause == DamageCause.DROWNING) return "Drown";
		if(cause == DamageCause.FALL) return "Fall";
		if(cause == DamageCause.FALLING_BLOCK) return "Suffocated";
		if(cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK) return "Fire";
		if(cause == DamageCause.LAVA) return "Lava";
		if(cause == DamageCause.LIGHTNING) return "Lightning";
		if(cause == DamageCause.MAGIC) return "Potion";
		if(cause == DamageCause.POISON) return "Poison";
		if(cause == DamageCause.STARVATION) return "Starvation";
		if(cause == DamageCause.SUICIDE) return "Suicide";
		if(cause == DamageCause.VOID) return "Void";
		if(cause == DamageCause.WITHER) return "Wither";
		return "Death";
	}
	
	public static void openInv(Player viewer, Player target) {
		if(target == null) return;
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.AQUA + ChatColor.BOLD.toString() + target.getName() + "'s inventory");
		for(int i = 0; i < 40; i++) {
			ItemStack item = target.getInventory().getItem(i);
			if(item != null) {
				if(i == 39) inv.setItem(41, item);
				else if(i == 38) inv.setItem(42, item);
				else if(i == 37) inv.setItem(43, item);
				else if(i == 36) inv.setItem(44, item);
				//if(slotTypeByInt(i) == SlotType.CRAFTING) continue;
				if(i == 0) inv.setItem(45, item);
				else if(i == 1) inv.setItem(46, item);
				else if(i == 2) inv.setItem(47, item);
				else if(i == 3) inv.setItem(48, item);
				else if(i == 4) inv.setItem(49, item);
				else if(i == 5) inv.setItem(50, item);
				else if(i == 6) inv.setItem(51, item);
				else if(i == 7) inv.setItem(52, item);
				else if(i == 8) inv.setItem(53, item);
				if(slotTypeByInt(i) == SlotType.CONTAINER) inv.setItem(i, item);
			}
		}
		if(viewer != null) {
			viewer.openInventory(inv);
			viewer.sendMessage(ChatColor.BLUE + "Inv - " + ChatColor.WHITE + "You are viewing " + ChatColor.AQUA + target.getName() + "'s Inventory");
		}
	}
	
	public static ItemStack getItemBySlot(Inventory inv, int slot) {
		return inv.getItem(slot);
	}
	
	public static SlotType slotTypeByInt(int slot) {
		if(slot >= 0 || slot < 5) return SlotType.CRAFTING;
		if(slot > 4 || slot < 9) return SlotType.ARMOR;
		if(slot > 8 || slot < 36) return SlotType.CONTAINER;
		if(slot > 35 || slot < 45) return SlotType.QUICKBAR;
		else return null;
	}
	
	public static void fade(Player player) {
		for(Player players : Bukkit.getOnlinePlayers()) {
			if(!players.canSee(player)) {
				players.showPlayer(player);
				return;
			}
			players.hidePlayer(player);
		}
	}
	
	public static void sendChat(Player player, String chat) {
		if(RCommands.chatdisable) {
			M.v(player, "Chat", ChatColor.YELLOW + "Chat " + ChatColor.WHITE + "is disabled");
			return;
		}
		if(Mute.isMuted(player)) {
			String rem = Mute.getRemaining(player.getName());
			String reason = Mute.getReason(player.getName());
			Player p = Bukkit.getPlayer(player.getName());
			if(p == null) {
				return;
			}
			p.sendMessage(ChatColor.GOLD + "Mute - " + ChatColor.WHITE + "Muted [" + ChatColor.GREEN + rem + ChatColor.WHITE + "]" + " for " + ChatColor.GREEN + reason);
			return;
		}
		HashSet<String> receivers = new HashSet<String>();
		for(Player players : Bukkit.getServer().getOnlinePlayers()) {
			if(!Ignore.isIgnored(player.getName(), players.getName())) {
				receivers.add(players.getName());
			}
		}
		for(String rec : receivers) {
			Player rPlayer = Bukkit.getPlayer(rec);
			String rSquad = "";
			String squad = Squads.getSquad(player.getName());
			if(Squads.hasSquad(player.getName())) {
				rSquad  = Squads.getSquad(rPlayer.getName());
				if(rPlayer != null) {
					rPlayer.sendMessage(Squads.getRelationColour(squad, rSquad) + " " + player.getName() + " " + ChatColor.WHITE + chat);
				}
			} else {
				rPlayer.sendMessage(ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " " + chat);
			}
		}
	}
}
