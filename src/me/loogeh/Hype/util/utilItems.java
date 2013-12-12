package me.loogeh.Hype.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.loogeh.Hype.Hype;

public class utilItems {
	public static Hype plugin;
	public static HashSet<Integer> interactableItems = new HashSet<Integer>();
	public static HashSet<Integer> removableItems = new HashSet<Integer>();
	public static HashMap<Integer, String> customNames = new HashMap<Integer, String>();
	public static HashMap<Integer, List<String>> customMeta = new HashMap<Integer, List<String>>();

	public static void giveItem(Player player, Player caller, int amount, Material material) {
		if(material == null) {
			caller.sendMessage(ChatColor.DARK_AQUA + "Give: " + ChatColor.GRAY + "The item doesn't exist");
			return;
		}
		if(amount < 1) {
			caller.sendMessage(ChatColor.DARK_AQUA + "Give: " + ChatColor.GRAY + "Invalid Amount");
			return;
		}
		player.getInventory().addItem(new ItemStack(material, amount));
		if(player.getName().equals(caller.getName())) {
			Bukkit.broadcastMessage(ChatColor.YELLOW + caller.getName() + ChatColor.GRAY + " gave themself " + ChatColor.YELLOW + amount + WordUtils.capitalize(material.toString().toLowerCase().replaceAll("_", " ")));
		} else {
			Bukkit.broadcastMessage(ChatColor.YELLOW + caller.getName() + ChatColor.GRAY + " gave " + player.getName() + ChatColor.YELLOW + amount + WordUtils.capitalize(material.toString().toLowerCase().replaceAll("_", " ")));
		}
	}

	public static void giveDataItem(Player player, Player caller, int amount, Material material, short data) {
		if(material == null) {
			caller.sendMessage(ChatColor.DARK_AQUA + "Give: " + ChatColor.GRAY + "The item doesn't exist");
			return;
		}
		if(data < 1) {
			caller.sendMessage(ChatColor.DARK_AQUA + "Give: " + ChatColor.GRAY + "Invalid Data");
			return;
		}
		if(amount < 1) {
			caller.sendMessage(ChatColor.DARK_AQUA + "Give: " + ChatColor.GRAY + "Invalid Amount");
			return;
		}
		player.getInventory().addItem(new ItemStack(material, amount, (short) data));
		if(player.getName().equals(caller.getName())) {
			Bukkit.broadcastMessage(ChatColor.YELLOW + caller.getName() + ChatColor.GRAY + " gave themself " + ChatColor.YELLOW + amount + WordUtils.capitalize(material.toString().toLowerCase().replaceAll("_", " ")));
		} else {
			Bukkit.broadcastMessage(ChatColor.YELLOW + caller.getName() + ChatColor.GRAY + " gave " + player.getName() + ChatColor.YELLOW + amount + WordUtils.capitalize(material.toString().toLowerCase().replaceAll("_", " ")));
		}
	}

	public static int countLevels(ItemStack item) {
		int total = 0;

		for(Iterator<Integer> enchCounter = item.getEnchantments().values().iterator(); enchCounter.hasNext(); ) {
			int enchLvl = ((Integer)enchCounter.next()).intValue();
			total += enchLvl;
		}
		return total;
	}

	public static ItemStack removeEnchantments(ItemStack i){
		for(Entry<Enchantment, Integer> e : i.getEnchantments().entrySet()){
			i.removeEnchantment(e.getKey());
		}
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static String getName(int id) {
		if(!hasCustomName(id)) return WordUtils.capitalize(Material.getMaterial(id).toString().toLowerCase().replaceAll("_", " "));
		return customNames.get(id);
	}
	
	public static boolean hasCustomName(int id) {
		if(customNames.isEmpty()) {
			customNames.put(286, "Combustion Axe");
			customNames.put(294, "Parkour Stick");
			customNames.put(293, "Lightning Sceptre");
			customNames.put(284, "Staff of Speed");
			customNames.put(279, "Velocity Scythe");
			customNames.put(30, "Web Grenade");
		}
		return customNames.containsKey(id);
	}
	
	public static boolean hasCustomMeta(int id) {
		if(customMeta.isEmpty()) {
			customMeta.put(286, Arrays.asList("Set a player on Fire"));
			customMeta.put(294, Arrays.asList("Evade a player"));
			customMeta.put(293, Arrays.asList("Cast a Lightning Strike"));
			customMeta.put(284, Arrays.asList("Adrenalin Rush"));
			customMeta.put(279, Arrays.asList("Punish with Slowness"));
			customMeta.put(30, Arrays.asList("Trap someone in Web"));
		}
		return customNames.containsKey(id);
	}
	
	public static void changeMeta(ItemStack item, String name, String line1, String line2, String line3) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		if(line1  == null && line2 == null && line3 == null) {
			item.setItemMeta(meta);
			return;
		}
		if(line1 != null && line2 == null && line3 == null) {
			meta.setLore(Arrays.asList(line1));
		}
		if(line1 != null && line2 != null && line3 == null) {
			meta.setLore(Arrays.asList(line1, line2));
		}
		if(line1 != null && line2 != null && line3 != null) {
			meta.setLore(Arrays.asList(line1, line2, line3));
		}
		item.setItemMeta(meta);
	}

	public static boolean isInteractable(int id) {
		if(interactableItems.isEmpty()) {
			interactableItems.add(23);
			interactableItems.add(26);
			interactableItems.add(54);
			interactableItems.add(61);
			interactableItems.add(62);
			interactableItems.add(64);
			interactableItems.add(69);
			interactableItems.add(77);
			interactableItems.add(84);
			interactableItems.add(93);
			interactableItems.add(94);
			interactableItems.add(95);
			interactableItems.add(96);
			interactableItems.add(116);
			interactableItems.add(117);
			interactableItems.add(118);
			interactableItems.add(120);
			interactableItems.add(130);
			interactableItems.add(137);
			interactableItems.add(145);
			interactableItems.add(146);
			interactableItems.add(149);
			interactableItems.add(150);
			interactableItems.add(158);
			interactableItems.add(324);
			interactableItems.add(328);
			interactableItems.add(329);
			interactableItems.add(333);
			interactableItems.add(355);
			interactableItems.add(356);
			interactableItems.add(379);
			interactableItems.add(380);
			interactableItems.add(404);
		}
		return interactableItems.contains(id);
	}

	public static boolean isRemovable(int id) {
		if(interactableItems.isEmpty()) {
			interactableItems.add(23);
			interactableItems.add(26);
			interactableItems.add(54);
			interactableItems.add(61);
			interactableItems.add(62);
			interactableItems.add(64);
			interactableItems.add(69);
			interactableItems.add(77);
			interactableItems.add(84);
			interactableItems.add(93);
			interactableItems.add(94);
			interactableItems.add(95);
			interactableItems.add(96);
			interactableItems.add(116);
			interactableItems.add(117);
			interactableItems.add(118);
			interactableItems.add(120);
			interactableItems.add(130);
			interactableItems.add(137);
			interactableItems.add(145);
			interactableItems.add(146);
			interactableItems.add(149);
			interactableItems.add(150);
			interactableItems.add(158);
			interactableItems.add(324);
			interactableItems.add(328);
			interactableItems.add(329);
			interactableItems.add(333);
			interactableItems.add(355);
			interactableItems.add(356);
			interactableItems.add(379);
			interactableItems.add(380);
			interactableItems.add(404);
		}
		return interactableItems.contains(id);
	}
	
	public static boolean isSword(int id) {
		return id == 267 || id == 268 || id == 272 || id == 283 || id == 276;
	}
	
	public static boolean isAbilitySword(int id) {
		return id == 267 || id == 276;
	}
	
	public static boolean notStackable(int id) {
		return id == 26 || id > 255 && id < 260 || id == 261 || id > 266 && id < 280 || id > 281 && id < 287 || id > 289 && id < 295
				|| id > 297 && id < 318 || id > 324 && id < 331 || id == 333 || id == 335 || id == 342 || id == 343 || id == 346
				|| id == 354 || id == 355 || id == 359 || id == 395 || id == 398 || id == 403 || id == 407 || id == 408 || id < 420 && id > 416
				|| id < 2268 && id > 2255;
	}
	
	public static String toCommon(String item) {
		if(item.equalsIgnoreCase("smooth_brick")) return "Stone Brick";
		else if(item.contains("spade")) return WordUtils.capitalize(item.toLowerCase().replaceAll("_", " ").replaceAll("spade", "shovel"));
		else if(item.equalsIgnoreCase("tnt")) return "TNT";
		else if(item.equalsIgnoreCase("piston_base")) return "Piston";
		else if(item.equalsIgnoreCase("piston_sticky_base")) return "Sticky Piston";
		else if(item.equalsIgnoreCase("command")) return "Command Block";
		else if(item.equalsIgnoreCase("smooth_stairs")) return "Stone Brick Stairs";
		else if(item.contains("chainmail")) return WordUtils.capitalize(item.toLowerCase().replaceAll("_", " ").replace("chainmail", "Chain"));
		else if(item.equalsIgnoreCase("nether_stalk")) return "Nether Wart";
		return WordUtils.capitalize(item.toLowerCase().replaceAll("_", " "));
	}
	
	public static String toCommon(int id) {
		if(id == 48) return "Power";
		if(id == 50) return "Flame";
		if(id == 51) return "Infinity";
		if(id == 49) return "Punch";
		if(id == 16) return "Sharpness";
		if(id == 18) return "Bane of Arthropods";
		if(id == 17) return "Smite";
		if(id == 32) return "Efficiency";
		if(id == 34) return "Unbreaking";
		if(id ==  20) return "Fire Aspect";
		if(id == 19) return "Knockback";
		if(id == 35) return "Fortune";
		if(id == 21) return "Looting";
		if(id == 5) return "Respiration";
		if(id == 0) return "Protection";
		if(id == 3) return "Blast Protection";
		if(id == 2) return "Feather Falling";
		if(id == 1) return "Fire Protection";
		if(id == 4) return "Projectile Protection";
		if(id == 33) return "Silk Touch";
		if(id == 7) return "Thorns";
		if(id == 6) return "Aqua Affinity";
		return "None";
	}
	
//	public static Enchantment getType(int id) {
//		if(id == 0) return Enchantment.PROTECTION_ENVIRONMENTAL;
//		if(id == 1) return Enchantment.PROTECTION_FIRE;
//		if(id == 2) return Enchantment.PROTECTION_FALL;
//		if(id == 3) return Enchantment.PROTECTION_EXPLOSIONS;
//		if(id == 4) return Enchantment.PROTECTION_PROJECTILE;
//		if(id == 5) return Enchantment.OXYGEN;
//		if(id == 6) return Enchantment.
//	}
//	
	public static int countChar(String toCount, String target, boolean caseSensitive) {
		int count = 0;
		for(Character chars : toCount.toCharArray()) {
			if(caseSensitive) {
				if(chars.toString().equals(target)) count++;
			} else if(!caseSensitive) {
				if(chars.toString().equalsIgnoreCase(target)) count++;
			}
		}
		return count;
	}
}
