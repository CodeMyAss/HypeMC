package me.loogeh.Hype.bettershops;

import java.util.ArrayList;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.Spawn;
import me.loogeh.Hype.Spawn.InfoCentre;
import me.loogeh.Hype.armour.Archer;
import me.loogeh.Hype.bettershops.BetterShops.ItemType;
import me.loogeh.Hype.economy.Money;
import me.loogeh.Hype.util.utilArmour;
import me.loogeh.Hype.util.utilItems;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ShopListener implements Listener {
	public static Hype plugin;
	
	private BetterShops shops = new BetterShops();
	
	public ShopListener(Hype instance) {
		plugin = instance;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(Archer.arrowList.containsKey(player.getName())) {
			if(!utilArmour.hasSet(player, "LEATHER")) Archer.arrowList.remove(player.getName());
		}
		if(ShopsCommands.debug.contains(player.getName())) { player.sendMessage("Slot - " + event.getRawSlot() + " Type - " + event.getSlotType()); event.setCancelled(true); player.updateInventory(); return; }
		if(event.getRawSlot() < 0) return;
		if(event.getInventory().getName().contains("Shop") && event.getRawSlot() < 54 && !event.getInventory().getName().contains("Enchanter")) {
			event.setCancelled(true);
			player.updateInventory();
			if(player.getInventory().firstEmpty() < 0) {
				BetterShops.sendMessage(player, ChatColor.WHITE + "Your inventory is full");
				return;
			}
			if(event.getCurrentItem() == null) {
				return;
			}
			ItemStack item = event.getCurrentItem();
			Short data = item.getDurability();
			if(!BetterShops.buyPrice.containsKey(item.getType().toString().toLowerCase())) {
				return;
			}
			int money = Money.getMoney(player);
			if(!utilItems.notStackable(item.getTypeId())) {
				if(event.getClick().isShiftClick()) {
					int totalCost = BetterShops.getCost(item.getType()) * 64;
					if(totalCost > money) {
						player.sendMessage(ChatColor.DARK_GREEN + "Money - " + ChatColor.WHITE + "You have insufficient money");
						return;
					}
					Money.subtractMoney(player, totalCost);
					player.getInventory().addItem(new ItemStack(item.getType(), 64, data));
					BetterShops.log.get(player.getName()).cost += totalCost;
					BetterShops.log.get(player.getName()).addSpent(item.getType(), 64);
					return;
				}
			}
			int totalCost = BetterShops.getCost(item.getType()) * item.getAmount();
			if(totalCost > money) {
				player.sendMessage(ChatColor.DARK_GREEN + "Money - " + ChatColor.WHITE + "You have insufficient money");
				return;
			}
			Money.subtractMoney(player, totalCost);
			player.getInventory().addItem(new ItemStack(item.getType(), item.getAmount(), data));
			BetterShops.log.get(player.getName()).cost += totalCost;
			BetterShops.log.get(player.getName()).addSpent(item.getType(), 1);
		}
		if(event.getInventory().getName().contains("Shop") && event.getRawSlot() > 53 && event.getRawSlot() < 90) {
			int slot = event.getSlot();
			event.setCancelled(true);
			player.updateInventory();
			if(event.getCurrentItem() == null) {
				return;
			}
			ItemStack item = event.getCurrentItem();
			String name = item.getType().toString().toLowerCase();
			if(!BetterShops.sellPrice.containsKey(item.getType().toString().toLowerCase()) && !name.equalsIgnoreCase("air")) {
				BetterShops.sendMessage(player, ChatColor.YELLOW + utilItems.toCommon(name) + ChatColor.WHITE + " is not sellable");
				return;
			}
			String[] cur = event.getView().getTopInventory().getTitle().split(" ");
			if(!BetterShops.sellPrice.containsKey(item.getType().toString().toLowerCase())) return;
			String type = BetterShops.sellPrice.get(item.getType().toString().toLowerCase()).type.toString().toLowerCase();
			if(!cur[1].equalsIgnoreCase(type)) {
				BetterShops.sendMessage(player, ChatColor.YELLOW + utilItems.toCommon(name) + ChatColor.WHITE + " is sold at " + ChatColor.LIGHT_PURPLE + WordUtils.capitalize(type));
				return;
			}
			int sp = BetterShops.sellPrice.get(item.getType().toString().toLowerCase()).sell_cost;
			if(event.getClick().isRightClick()) {
				return;
//				int count = 0;
//				for(int i = 54; i < 90; i++) {
//					ItemStack sel = event.getView().getBottomInventory().getItem(i);
//					if(sel == item && sel.getDurability() == item.getDurability()) {
//						event.getView().getBottomInventory().setItem(i, null);
//						count += sel.getAmount();
//					}
//				}
//				int totalCost = sp * count;
//				Money.addMoney(player, totalCost);
//				shops.sendMessage(player, ChatColor.WHITE + "You sold " + ChatColor.LIGHT_PURPLE + count + " " + WordUtils.capitalize(name) + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + ChatColor.GRAY + totalCost);
//				return;
			}
			int amount = item.getAmount();
			if(event.getClick().isShiftClick()) {
				player.getInventory().setItem(slot, null);
				int totalPrice = sp * amount;
				Money.addMoney(player, totalPrice);
				BetterShops.sendMessage(player, ChatColor.WHITE + "You sold " + ChatColor.LIGHT_PURPLE + amount + " " + utilItems.toCommon(name) + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + ChatColor.GRAY + totalPrice);
				BetterShops.log.get(player.getName()).cost -= totalPrice;
				return;
			}
			if(event.getClick().isLeftClick()) {
				if(amount > 1) {
					item.setAmount(amount - 1);
					Money.addMoney(player, sp);
					BetterShops.sendMessage(player, ChatColor.WHITE + "You sold " + ChatColor.LIGHT_PURPLE + "1 " + utilItems.toCommon(name) + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + ChatColor.GRAY + sp);
					BetterShops.log.get(player.getName()).cost -= sp;
					return;
				}
				player.getInventory().setItem(slot, null);
				Money.addMoney(player, sp);
				BetterShops.sendMessage(player, ChatColor.WHITE + "You sold " + ChatColor.LIGHT_PURPLE + "1 " + utilItems.toCommon(name) + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + ChatColor.GRAY + sp);
				BetterShops.log.get(player.getName()).cost -= sp;
				return;
			}
		}
		if(event.getInventory().getName().contains("Enchanter") && event.getRawSlot() < 54) {
			ItemStack item = event.getCurrentItem();
			event.setCancelled(true);
			player.updateInventory();
			if(item == null) return;
			if(player.getItemInHand() == null) {
				BetterShops.sendMessage(player, ChatColor.WHITE + "You must hold your " + ChatColor.YELLOW + "Desired Item");
				return;
			}
			Enchantment enchantment = null;
			for(Enchantment ench : item.getEnchantments().keySet()) {
				ArrayList<Enchantment> eList = new ArrayList<Enchantment>();
				eList.add(ench);
				enchantment = eList.get(0);
				eList.clear();
			}
			BetterShops.buyEnchantment(player, enchantment);
		}
		if(event.getView().getTopInventory().getName().contains("Abilities") || event.getView().getTopInventory().getName().contains("Info")) {
			event.setCancelled(true);
			player.updateInventory();
		}
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Zombie) {
			Zombie zombie = (Zombie) entity;
			if(zombie.getCustomName() == null) {
				return;
			}
			String name = ChatColor.stripColor(zombie.getCustomName());
			if(name.equalsIgnoreCase("Builder") || name.equalsIgnoreCase("Baker") || name.equalsIgnoreCase("Weaponry") || name.equalsIgnoreCase("Enchanter") || name.equalsIgnoreCase("Miner") || name.equalsIgnoreCase("Miscellaneous")) {
				event.setCancelled(true);
				event.setTarget(null);
			}
		}
	}
	
	@EventHandler
	public void onInventClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(event.getInventory().getName().contains("Shop") && !event.getInventory().getName().contains("Enchanter")) BetterShops.sendLog(player);
		if(event.getView().getTopInventory().getName().contains("Enchanter")) BetterShops.sendELog(player);
	}
	
	
	@EventHandler
	public void craft(CraftItemEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(event.getRecipe().getResult().getType() == Material.BREWING_STAND || event.getRecipe().getResult().getType() == Material.BREWING_STAND_ITEM) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.GREEN + "Server - " + ChatColor.LIGHT_PURPLE + "Brewing Stands " + ChatColor.WHITE + "can only be bought");
		}
		if(event.getRecipe().getResult().getType() == Material.ENCHANTMENT_TABLE) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.GREEN + "Server - " + ChatColor.LIGHT_PURPLE + "Enchantments " + ChatColor.WHITE + "can only be bought");
		}
		if(event.getRecipe().getResult().getType() == Material.ANVIL) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.GREEN + "Server - " + ChatColor.LIGHT_PURPLE + "Anvils " + ChatColor.WHITE + "are disabled");
		}
	}
	
//	@EventHandler
//	public void onEntityDamage(EntityDamageEvent event) {
//		Entity entity = event.getEntity();
//		if(entity.getType() == EntityType.ZOMBIE) {
//			Zombie zombie = (Zombie) entity;
//			if(zombie.getCustomName() == null) {
//				return;
//			}
//			String name = ChatColor.stripColor(zombie.getCustomName());
//			if(name.equalsIgnoreCase("Builder")) {
//				event.setCancelled(true);
//				zombie.setFireTicks(0);
//			}
//			if(name.equalsIgnoreCase("Baker")) {
//				event.setCancelled(true);
//				zombie.setFireTicks(0);
//			}
//			if(name.equalsIgnoreCase("Weaponry")) {
//				event.setCancelled(true);
//				zombie.setFireTicks(0);
//			}
//			if(name.equalsIgnoreCase("Miner")) {
//				event.setCancelled(true);
//				zombie.setFireTicks(0);
//			}
//			if(name.equalsIgnoreCase("Miscellaneous")) {
//				event.setCancelled(true);
//				zombie.setFireTicks(0);
//			}
//			if(name.equalsIgnoreCase("Enchanter")) {
//				event.setCancelled(true);
//				zombie.setFireTicks(0);
//			}
//		}
//	}
//	
//	@EventHandler
//	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
//		if(!(event.getEntity() instanceof Player)) {
//			return;
//		}
//		if(event.getDamager() instanceof Zombie) {
//			Zombie zombie = (Zombie) event.getDamager();
//			if(zombie.getCustomName() == null) {
//				return;
//			}
//			String name = ChatColor.stripColor(zombie.getCustomName());
//			if(name.equalsIgnoreCase("Builder")) {
//				event.setCancelled(true);
//			} else if(name.equalsIgnoreCase("Weaponry")) {
//				event.setCancelled(true);
//			} else if(name.equalsIgnoreCase("Enchanter")) {
//				event.setCancelled(true);
//			} else if(name.equalsIgnoreCase("Baker")) {
//				event.setCancelled(true);
//			} else if(name.equalsIgnoreCase("Miner")) {
//				event.setCancelled(true);
//			} else if(name.equalsIgnoreCase("Miscellaneous")) {
//				event.setCancelled(true);
//			}
//		}
//	}
	
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if(entity instanceof CraftPlayer) {
			CraftPlayer npc = (CraftPlayer) entity;
			if(npc.getName().equalsIgnoreCase("Juggernaut")) Spawn.openInventory(player, InfoCentre.ARMOUR_DIAMOND);
			if(npc.getName().equalsIgnoreCase("Samurai")) Spawn.openInventory(player, InfoCentre.ARMOUR_IRON);
			if(npc.getName().equalsIgnoreCase("Specialist")) Spawn.openInventory(player, InfoCentre.ARMOUR_CHAIN);
			if(npc.getName().equalsIgnoreCase("Agility")) Spawn.openInventory(player, InfoCentre.ARMOUR_GOLD);
			if(npc.getName().equalsIgnoreCase("Archer")) Spawn.openInventory(player, InfoCentre.ARMOUR_LEATHER);
			if(npc.getName().contains("Games") || npc.getName().contains("Arena") || npc.getName().contains("Squads") || npc.getName().contains("Donate")) Spawn.openInventory(player, InfoCentre.ALL);
		}
		if(entity.getType() == EntityType.ZOMBIE) {
			Zombie zombie = (Zombie) entity;
			if(zombie.getCustomName() == null) {
				return;
			}
			String name = ChatColor.stripColor(zombie.getCustomName());
			if(name.equalsIgnoreCase("Baker")) shops.openShop(player, ItemType.BAKER);
			else if(name.equalsIgnoreCase("Builder")) shops.openShop(player, ItemType.BUILDER);
			else if(name.equalsIgnoreCase("Weaponry")) shops.openShop(player, ItemType.WEAPONRY);
			else if(name.equalsIgnoreCase("Miner")) shops.openShop(player, ItemType.MINER);
			else if(name.equalsIgnoreCase("Miscellaneous")) shops.openShop(player, ItemType.MISCELLANEOUS);
			else if(name.equalsIgnoreCase("Enchanter")) shops.openShop(player, ItemType.ENCHANTER);
		}
	}
}
