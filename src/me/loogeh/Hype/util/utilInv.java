package me.loogeh.Hype.util;

import java.util.HashMap;
import me.loogeh.Hype.Hype;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class utilInv {
	public static Hype plugin;
	public static HashMap<String, ItemStack[]> invents = new HashMap<String, ItemStack[]>();
	public static HashMap<String, ItemStack[]> armours = new HashMap<String, ItemStack[]>();

	
	public static void subtractAmount(Player player) {
		if(player.getItemInHand().getAmount() == 1) {
			player.setItemInHand(null);
		} else {
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
		}
	}

	public static void subtractAmount(Player player, int amount) {
		if(player.getItemInHand().getAmount() <= amount) {
			player.setItemInHand(null);
		} else {
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - amount);
		}
	}

	
	public static void saveContents(Player player) {
		invents.put(player.getName(), player.getInventory().getContents());
		armours.put(player.getName(), player.getInventory().getArmorContents());
	}
	
	public static void retrieveContents(Player player) {
		player.getInventory().setContents(invents.get(player.getName()));
		player.getInventory().setArmorContents(armours.get(player.getName()));
	}
	
	public static int countFilledSlots(Inventory invent) {
		int i = 0;
		for(ItemStack slot : invent.getContents()) {
			if(slot == null) continue;
			i++;
		}
		return i;
	}
}
