package me.loogeh.Hype;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class TPSListener implements Listener {
	public static Hype plugin;
	
	public TPSListener(Hype instance) {
		plugin = instance;
	}
	
	
	@EventHandler
	public void onCloseInvent(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(!TeleportSkulls.invents.containsKey(player.getName())) return;
		TeleportSkulls.closeInv(player);
	}
	
	@EventHandler
	public void onKickPlayer(PlayerKickEvent event) {
		Player player = event.getPlayer();
		if(!TeleportSkulls.invents.containsKey(player.getName())) return;
		TeleportSkulls.closeInv(player);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(!TeleportSkulls.invents.containsKey(player.getName())) return;
		TeleportSkulls.closeInv(player);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(event.getSlot() == event.getRawSlot()) {
			if(TeleportSkulls.invents.containsKey(player.getName())) {
				if(TeleportSkulls.invents.get(player.getName()).equalsIgnoreCase("teleport")) {
					event.setCancelled(true);
					player.updateInventory();
					ItemStack item = event.getCurrentItem();
					if(item != null) {
						String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						Player target = Bukkit.getPlayerExact(name);
						if(target != null) {
							player.teleport(target);
							player.sendMessage(ChatColor.AQUA + "Teleport - " + ChatColor.WHITE + "You teleported to " + ChatColor.YELLOW + target.getName());
						} else {
							player.sendMessage(ChatColor.AQUA + "Teleport - " + ChatColor.YELLOW + name + ChatColor.WHITE + " is offline");
						}
					}
				}
			}
		}
	}
		
}
