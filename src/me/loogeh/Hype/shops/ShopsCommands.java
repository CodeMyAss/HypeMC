package me.loogeh.Hype.shops;


import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.economy.Money;
import me.loogeh.Hype.util.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopsCommands implements CommandExecutor {
	public static Hype plugin;


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if(commandLabel.equalsIgnoreCase("nameitem")) {
			if(args.length == 0) {
				player.sendMessage(ChatColor.RED + "/nameitem <name>");
			} else {
				if(Money.getMoney(player) < 5000) {
					player.sendMessage(ChatColor.GRAY + "You don't have enough " + ChatColor.DARK_GREEN + "money");
				} else {
					Money.subtractMoney(player, 5000);
					String name = Utilities.argsToString(args).replaceFirst(" ", "");
					ItemStack ih = player.getItemInHand();
					ItemMeta im = ih.getItemMeta();
					im.setDisplayName(ChatColor.AQUA + name);
					ih.setItemMeta(im);
					player.sendMessage(ChatColor.DARK_GREEN + "Economy: " + ChatColor.GRAY + "You named your item " + ChatColor.YELLOW + name + ChatColor.GRAY + " for " + ChatColor.DARK_GREEN + "$5000");
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("shops") || (commandLabel.equalsIgnoreCase("shop"))) {
			if(args.length == 0) {
				if(!Permissions.isAdmin(player)) {
					player.sendMessage(ChatColor.DARK_GREEN + "/buy" + ChatColor.GRAY + " <item> (amount)");
					player.sendMessage(ChatColor.DARK_GREEN + "/sell");
					player.sendMessage(ChatColor.DARK_GREEN + "/<buy/sell>" + ChatColor.YELLOW + " list " + ChatColor.GRAY + "<page #>");
					player.sendMessage(ChatColor.DARK_GREEN + "/shops" + ChatColor.YELLOW + " help");
					return true;
				} else if(Permissions.isAdmin(player)) {
					player.sendMessage(ChatColor.DARK_GREEN + "/buy" + ChatColor.GRAY + " <item> (amount)");
					player.sendMessage(ChatColor.DARK_GREEN + "/sell");
					player.sendMessage(ChatColor.DARK_GREEN + "/<buy/sell>" + ChatColor.YELLOW + " list " + ChatColor.GRAY + "<page #>");
					player.sendMessage(ChatColor.DARK_GREEN + "/shops" + ChatColor.YELLOW + " help");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("help")) {
				if(!Permissions.isAdmin(player)) {
					player.sendMessage(ChatColor.DARK_GREEN + "/buy" + ChatColor.GRAY + " <item> (amount)");
					player.sendMessage(ChatColor.DARK_GREEN + "/sell");
					player.sendMessage(ChatColor.DARK_GREEN + "/<buy/sell>" + ChatColor.YELLOW + " list " + ChatColor.GRAY + "<page #>");
					player.sendMessage(ChatColor.DARK_GREEN + "/shops" + ChatColor.YELLOW + " help");
					return true;
				} else if(Permissions.isAdmin(player)) {
					player.sendMessage(ChatColor.DARK_GREEN + "/buy" + ChatColor.GRAY + " <item> (amount)");
					player.sendMessage(ChatColor.DARK_GREEN + "/sell");
					player.sendMessage(ChatColor.DARK_GREEN + "/<buy/sell>" + ChatColor.YELLOW + " list " + ChatColor.GRAY + "<page #>");
					player.sendMessage(ChatColor.DARK_GREEN + "/shops" + ChatColor.YELLOW + " help");
					return true;
				}
			} else if(args[0].equalsIgnoreCase("sell")) {
				if(args.length == 1) {
					if(!Shops.isInShops(player)) {
						player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.GRAY + "You aren't in shops");
					} else {
						String itemName = player.getItemInHand().getType().toString();
						int quantity = player.getItemInHand().getAmount();
						Shops.sellItem(player, itemName, quantity);
					}
				} else if(args.length == 2) {
					if(!Shops.isInShops(player)) {
						player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.GRAY + "You aren't in shops");
					} else {
						String itemName = player.getItemInHand().getType().toString();
						int quantity = Integer.parseInt(args[1]);
						Shops.sellItem(player, itemName, quantity);
					}
				}
			} else if(args[0].equalsIgnoreCase("buy")) {
				if(args.length == 3) {
					if(!Shops.isInShops(player)) {
						player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.GRAY + "You aren't in shops");
					} else {
						String itemName = args[1].toUpperCase();
						int quantity = Integer.parseInt(args[2]);
						Shops.buyItem(player, itemName, quantity);
					}
				} else {
					player.sendMessage(ChatColor.RED + "/shops buy <item> <quantity>");
				}
			} else if(args[0].equalsIgnoreCase("load")) {
				Shops.addItemsToHashMap();
			}


		}
		if(commandLabel.equalsIgnoreCase("buy")) {
			if(args.length == 0) {
				player.sendMessage(ChatColor.DARK_GREEN + "/buy" + ChatColor.GRAY + " <item> (amount)");
				player.sendMessage(ChatColor.DARK_GREEN + "/buy" + ChatColor.YELLOW + " list <page #>");
				return true;
			}
			if(args.length == 2) {
				if(!Shops.isInShops(player)) {
					player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.GRAY + "You aren't in shops");
					return true;
				}
				String itemName = args[0].toUpperCase();
				int quantity = Integer.parseInt(args[1]);
				Shops.buyItem(player, itemName, quantity);
			} else {
				player.sendMessage(ChatColor.RED + "/shops buy <item> <quantity>");
			}
			if(args[0].equalsIgnoreCase("list")) {
				int pageNum = Integer.parseInt(args[1]);
				if(args[1] != null) {
					Shops.pageList(player, "buy", pageNum);
				} else {
					Shops.pageList(player, "sell", 1);
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("sell")) {
			if(args.length == 0) {
				String itemName = player.getItemInHand().getType().toString();
				int quantity = player.getItemInHand().getAmount();
				Shops.sellItem(player, itemName, quantity);
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("list")) {
					player.sendMessage(ChatColor.RED + "/sell list <page #>");
					return true;
				}
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("list")) {
					int pageNum = Integer.parseInt(args[1]);
					Shops.pageList(player, "sell", pageNum);
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("enchantment") || commandLabel.equalsIgnoreCase("ench")) {
			if(args.length == 0) {
				player.sendMessage(ChatColor.DARK_GREEN + "/ench " + ChatColor.GRAY + "buy " + ChatColor.YELLOW + "<ench>");
				player.sendMessage(ChatColor.DARK_GREEN + "/ench " + ChatColor.GRAY + "list <page #>");
			}
			if(args.length == 2) {
				int page = Integer.parseInt(args[1]);
				if(args[0].equalsIgnoreCase("list")) {
					if(page == 1) {
						player.sendMessage(ChatColor.DARK_GREEN + "Enchantment List " + ChatColor.WHITE + " - " + ChatColor.GRAY + "Page 1");
						player.sendMessage(ChatColor.YELLOW + "Efficiency " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "35000");
						player.sendMessage(ChatColor.YELLOW + "Silk Touch " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "45000");
						player.sendMessage(ChatColor.YELLOW + "Unbreaking " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "40000");
						player.sendMessage(ChatColor.YELLOW + "Fortune " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "110000");
						player.sendMessage(ChatColor.YELLOW + "Power " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "110000");
						player.sendMessage(ChatColor.YELLOW + "Punch " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "75000");
						player.sendMessage(ChatColor.YELLOW + "Flame " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "180000");
						player.sendMessage(ChatColor.YELLOW + "Sharpness " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "150000");
						player.sendMessage(ChatColor.YELLOW + "Fire Aspect " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "200000");
						player.sendMessage(ChatColor.YELLOW + "Bane of Arthropods " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "25000");
					} else if(page == 2) {
						player.sendMessage(ChatColor.DARK_GREEN + "Enchantment Buy List " + ChatColor.WHITE + " - " + ChatColor.GRAY + "Page 2");
						player.sendMessage(ChatColor.YELLOW + "Smite " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "25000");
						player.sendMessage(ChatColor.YELLOW + "Knockback " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "85000");
						player.sendMessage(ChatColor.YELLOW + "Looting " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "45000");
						player.sendMessage(ChatColor.YELLOW + "Blast Protection " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "60000");
						player.sendMessage(ChatColor.YELLOW + "Aqua Affinity " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "30000");
						player.sendMessage(ChatColor.YELLOW + "Thorns " + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "60000");
					} else if(page > 2) {
						player.sendMessage(ChatColor.DARK_GREEN + "Enchantment List " + ChatColor.WHITE + " - " + ChatColor.GRAY + "Page " + page + " does not exist");
					}
				} else if(args[0].equalsIgnoreCase("buy")) {
					player.sendMessage(ChatColor.RED + "/ench buy <enchantment> <level>");
				}
			} else if(args.length == 3) {
				if(args[0].equalsIgnoreCase("buy")) {
					if(!Shops.isInEnchShop(player)) {
						Shops.enchMsg(player, ChatColor.GRAY + "You aren't in the enchantment shop");
					} else {
						int level = Integer.parseInt(args[2]);
						if(args[1].equalsIgnoreCase("sharpness")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "damage_all", level);
						} else if(args[1].equalsIgnoreCase("fireaspect")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "fire_aspect", level);
						} else if(args[1].equalsIgnoreCase("knockback")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "knockback", level);
						} else if(args[1].equalsIgnoreCase("smite")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "damage_undead", level);
						} else if(args[1].equalsIgnoreCase("bane")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "damage_arthropods", level);
						} else if(args[1].equalsIgnoreCase("efficiency")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "dig_speed", level);
						} else if(args[1].equalsIgnoreCase("silktouch")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "silk_touch", level);
						} else if(args[1].equalsIgnoreCase("fortune")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "loot_bonus_blocks", level);
						} else if(args[1].equalsIgnoreCase("power")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "arrow_damage", level);
						} else if(args[1].equalsIgnoreCase("punch")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "arrow_knockback", level);
						} else if(args[1].equalsIgnoreCase("flame")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "arrow_fire", level);
						} else if(args[1].equalsIgnoreCase("unbreaking")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "durability", level);
						} else if(args[1].equalsIgnoreCase("looting")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "loot_bonus_mobs", level);
						} else if(args[1].equalsIgnoreCase("aquaaffinity")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "water_worker", level);
						} else if(args[1].equalsIgnoreCase("blastprotection")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "protection_explosions", level);
						} else if(args[1].equalsIgnoreCase("thorns")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "thorns", level);
						} else if(args[1].equalsIgnoreCase("infinity")) {
							Shops.buyEnchantment(player, player.getItemInHand(), "arrow_infinite", level);
						} 
					} 
				} 
			} 
		}
		return false;
	}

}
