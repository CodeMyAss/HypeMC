package me.loogeh.Hype;

import java.util.Arrays;
import java.util.HashMap;

import me.loogeh.Hype.Squads2.Squads;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class TeleportSkulls implements CommandExecutor {
	public static Hype plugin;
	
	public static HashMap<String, String> invents = new HashMap<String, String>();
	
	public static void openInv(Player player) {
		int lines = 0;
		Player[] players = Bukkit.getOnlinePlayers();
		while(lines * 9 < players.length - 1) {
			lines++;
		}
		if(lines > 6) {
			lines = 6;
		}
		Inventory inv = Bukkit.createInventory(null, lines * 9, ChatColor.LIGHT_PURPLE + "Teleport");
		int slot = 0;
		for(int i = 0; i < players.length; i++) {
			Player p = players[i];
			if(p != player) {
				ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
//				ItemMeta meta = item.getItemMeta();
				SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
				skullmeta.setOwner(p.getName());
				skullmeta.setDisplayName(ChatColor.YELLOW + p.getName());
				skullmeta.setLore(Arrays.asList(ChatColor.YELLOW + "Squad - " + ChatColor.WHITE + Squads.getSquad(p.getName())));
//				meta.setDisplayName(ChatColor.GOLD + p.getName());
//				item.setItemMeta(meta);
				item.setItemMeta(skullmeta);
				inv.setItem(slot, item);
				slot++;
			}
		}
		player.openInventory(inv);
		invents.put(player.getName(), "Teleport");
	}
	
	public static void closeInv(Player player) {
		if(!invents.containsKey(player.getName())) {
			return;
		}
		invents.remove(player.getName());
		player.closeInventory();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You can only teleport as a player");
			return true;
		}
		if(commandLabel.equalsIgnoreCase("invtp")) {
			Player player = (Player) sender;
			openInv(player);
			return true;
		}
		return false;
	}
	


}
