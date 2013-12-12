package me.loogeh.Hype;

import me.loogeh.Hype.util.Utilities;
import me.loogeh.Hype.util.utilItems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BetterGive {
	public static Hype plugin;
	
	
	public static void give(Player player, String item, int amount) {
		if(player == null) return;
		if(player.getInventory().firstEmpty() == -1) {
			M.v(player, "Give", ChatColor.WHITE + "Your inventory is full");
			return;
		}
		M.v(player, "Give", ChatColor.WHITE + "You gave yourself " + amount + utilItems.toCommon(item.toString().toLowerCase()));
	}
	
	public static ItemStack parseItem(String item) {
		if(!item.contains(":")) {
			if(!Utilities.isInteger(item)) {
			}
		}
		return null;
	}
	/*
	 * parseCommon - e.g stone_brick to smooth_brick
	 */
	
	public static ItemStack parseCommon(String item, int amount) {
		if(item.equalsIgnoreCase("stone_brick")) return new ItemStack(Material.SMOOTH_BRICK, amount);
		if(item.equalsIgnoreCase("mossy_stone_brick")) return new ItemStack(Material.SMOOTH_BRICK, amount, (short) 1);
		if(item.equalsIgnoreCase("cracked_stone_brick")) return new ItemStack(Material.SMOOTH_BRICK, amount, (short) 2);
		if(item.equalsIgnoreCase("chiseled_stone_brick")) return new ItemStack(Material.SMOOTH_BRICK, amount, (short) 3);
		if(item.equalsIgnoreCase("piston")) return new ItemStack(Material.PISTON_BASE, amount);
		if(item.equalsIgnoreCase("sticky_piston")) return new ItemStack(Material.PISTON_STICKY_BASE, amount);
		if(item.equalsIgnoreCase("awkward_potion")) return new ItemStack(Material.POTION, amount, (short) 16);
		if(item.equalsIgnoreCase("thick_potion")) return new ItemStack(Material.POTION, amount, (short) 32);
		if(item.equalsIgnoreCase("mundane_potion")) return new ItemStack(Material.POTION, amount, (short) 64);
		if(item.equalsIgnoreCase("regen_potion")) return new ItemStack(Material.POTION, amount, (short) 8193);
		if(item.equalsIgnoreCase("swiftness_potion")) return new ItemStack(Material.POTION, amount, (short) 8194);
		return null;
	}
}
