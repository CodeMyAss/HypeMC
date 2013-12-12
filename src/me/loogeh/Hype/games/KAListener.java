package me.loogeh.Hype.games;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.M;
import me.loogeh.Hype.Spawn;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.Spawn.InfoCentre;
import me.loogeh.Hype.armour.Armour.Kit;
import me.loogeh.Hype.games.Games.GameType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class KAListener implements Listener {
	public static Hype plugin = Hype.plugin;
	public KAListener(Hype instance) {
		plugin = instance;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onKAInventoryInteract(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)) return;
		Player player = (Player) event.getWhoClicked();
		if(event.getView().getTopInventory().getName().contains("Kit Selection")) {
			ItemStack item = event.getCurrentItem();
			event.setCancelled(true);
			player.updateInventory();
			if(item == null) return;
			if(item.getItemMeta() == null) return;
			if(item.getItemMeta().getDisplayName().contains("Archer")) {
				if(Games.games.get("kit_arena").isUsed(Kit.ARCHER, Games.games.get("kit_arena").getTeam(player))) {
					M.v(player, "Kit Arena", ChatColor.YELLOW + "Leather " + ChatColor.WHITE + "is already being used by 20% of your team");
					return;
				}
				Games.games.get("kit_arena").setKit(player, Kit.ARCHER);
				M.v(player, "Kit Arena", ChatColor.WHITE + "You selected " + ChatColor.YELLOW + "Archer");
				return;
			}
			if(item.getItemMeta().getDisplayName().contains("Agility")) {
				if(Games.games.get("kit_arena").isUsed(Kit.AGILITY, Games.games.get("kit_arena").getTeam(player))) {
					M.v(player, "Kit Arena", ChatColor.YELLOW + "Agility " + ChatColor.WHITE + "is already being used by 20% of your team");
					return;
				}
				Games.games.get("kit_arena").setKit(player, Kit.AGILITY);
				M.v(player, "Kit Arena", ChatColor.WHITE + "You selected " + ChatColor.YELLOW + "Agility");
				return;
			}
			if(item.getItemMeta().getDisplayName().contains("Specialist")) {
				if(Games.games.get("kit_arena").isUsed(Kit.SPECIALIST, Games.games.get("kit_arena").getTeam(player))) {
					M.v(player, "Kit Arena", ChatColor.YELLOW + "Specialist " + ChatColor.WHITE + "is already being used by 20% of your team");
					return;
				}
				Games.games.get("kit_arena").setKit(player, Kit.SPECIALIST);
				M.v(player, "Kit Arena", ChatColor.WHITE + "You selected " + ChatColor.YELLOW + "Specialist");
				return;
			}
			if(item.getItemMeta().getDisplayName().contains("Samurai")) {
				if(Games.games.get("kit_arena").isUsed(Kit.SAMURAI, Games.games.get("kit_arena").getTeam(player))) {
					M.v(player, "Kit Arena", ChatColor.YELLOW + "Samurai " + ChatColor.WHITE + "is already being used by 20% of your team");
					return;
				}
				Games.games.get("kit_arena").setKit(player, Kit.SAMURAI);
				M.v(player, "Kit Arena", ChatColor.WHITE + "You selected " + ChatColor.YELLOW + "Samurai");
				return;
			}
			if(item.getItemMeta().getDisplayName().contains("Jugg")) {
				if(Games.games.get("kit_arena").isUsed(Kit.JUGGERNAUT, Games.games.get("kit_arena").getTeam(player))) {
					M.v(player, "Kit Arena", ChatColor.YELLOW + "Juggernaut " + ChatColor.WHITE + "is already being used by 20% of your team");
					return;
				}
				Games.games.get("kit_arena").setKit(player, Kit.JUGGERNAUT);
				M.v(player, "Kit Arena", ChatColor.WHITE + "You selected " + ChatColor.YELLOW + "Juggernaut");
				return;
			}
			player.closeInventory();
			KitArena.openKitSelection(player);
		}
		if(event.getView().getTopInventory().getName().contains("Kit Description Selection")) {
			ItemStack item = event.getCurrentItem();
			event.setCancelled(true);
			player.updateInventory();
			if(item == null) return;
			if(item.getItemMeta() == null) return;
			else if(item.getItemMeta().getDisplayName().contains("Jugg")) Spawn.openInventory(player, InfoCentre.ARMOUR_DIAMOND);
			else if(item.getItemMeta().getDisplayName().contains("Samurai")) Spawn.openInventory(player, InfoCentre.ARMOUR_IRON);
			else if(item.getItemMeta().getDisplayName().contains("Specialist")) Spawn.openInventory(player, InfoCentre.ARMOUR_CHAIN);
			else if(item.getItemMeta().getDisplayName().contains("Agility")) Spawn.openInventory(player, InfoCentre.ARMOUR_GOLD);
			else if(item.getItemMeta().getDisplayName().contains("Archer")) Spawn.openInventory(player, InfoCentre.ARMOUR_LEATHER);
		}
		if(event.getView().getTopInventory().getName().contains("Abilities")) {
			event.setCancelled(true);
			player.updateInventory();
		}
	}
	 
	@EventHandler
	public void onKAInventory(InventoryOpenEvent event) {
		if(!(event.getPlayer() instanceof Player)) return;
		Player player = (Player) event.getPlayer();
		if(Games.getCurrentGame(player) == null) {
			if(event.getInventory().getType() == InventoryType.ENCHANTING) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Server - " + ChatColor.WHITE + "You must buy " + ChatColor.YELLOW + "Enchantments");
			}
			if(event.getInventory().getType() == InventoryType.ANVIL) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Server - " + ChatColor.YELLOW + "Anvils " + ChatColor.WHITE + "are disabled");
			}
			if(event.getInventory().getType() == InventoryType.CREATIVE) {
				if(!Permissions.isAdmin(player)) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Server - " + ChatColor.WHITE + "You must be atleast " + ChatColor.RED + "Admin " + ChatColor.WHITE + "to use " + ChatColor.YELLOW + "Creative");
				}
			}
			return;
		}
		if(Games.getCurrentGame(player).getType() == GameType.KIT_ARENA) {
			if(event.getInventory().getType() == InventoryType.ENCHANTING) {
				event.setCancelled(true);
				KitArena.openKitSelection(player);
			}
			if(event.getInventory().getType() == InventoryType.WORKBENCH) {
				event.setCancelled(true);
				KitArena.openKitDescSel(player);
			}
		}
	}
	
	@EventHandler
	public void onKAItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if(Games.getCurrentGame(player) == null) return;
		if(Games.getCurrentGame(player).getType() == GameType.KIT_ARENA) event.setCancelled(true);
	}
	
	@EventHandler
	public void onKAItemPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if(Games.getCurrentGame(player) == null) return;
		if(Games.getCurrentGame(player).getType() == GameType.KIT_ARENA) event.setCancelled(true);
	}
}
