package me.loogeh.Hype.util;

import me.loogeh.Hype.Hype;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class utilArmour {
	public static Hype plugin;

	public static boolean hasSet(Player player, String type) {
		for (ItemStack is : player.getInventory().getArmorContents()) {
			if (!is.getType().name().startsWith(type.toUpperCase()))
				return false;
		}

		return true;
	}

}