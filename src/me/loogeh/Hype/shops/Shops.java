package me.loogeh.Hype.shops;

import java.util.HashMap;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.economy.Money;
import me.loogeh.Hype.util.utilPlayer;

public class Shops {
	public static Hype plugin;

	public static HashMap<String, Integer> itemsPrice = new HashMap<String, Integer>();
	public static HashMap<String, Integer> sellPrice = new HashMap<String, Integer>();

	public static void addItemsToHashMap() {
		//Buying prices
		//Page 1
		itemsPrice.put("iron_sword", 3500);
		itemsPrice.put("diamond_sword", 14000);
		itemsPrice.put("iron_axe", 2500);
		itemsPrice.put("diamond_axe", 8000);
		itemsPrice.put("diamond_spade", 7000);
		itemsPrice.put("iron_pickaxe", 3000);
		itemsPrice.put("diamond_pickaxe", 12000);
		itemsPrice.put("apple", 200);
		itemsPrice.put("cooked_beef", 300);
		itemsPrice.put("cooked_porkchop", 300);
		//Page 2
		itemsPrice.put("leather_helmet", 3000);
		itemsPrice.put("leather_chestplate", 7000);
		itemsPrice.put("leather_leggings", 5000);
		itemsPrice.put("leather_boots", 3000);
		itemsPrice.put("gold_helmet", 3000);
		itemsPrice.put("gold_chestplate", 7000);
		itemsPrice.put("gold_leggings", 5000);
		itemsPrice.put("gold_boots", 3000);
		itemsPrice.put("iron_helmet", 3000);
		itemsPrice.put("iron_chestplate", 7000);
		//Page 3
		itemsPrice.put("iron_leggings", 5000);
		itemsPrice.put("iron_boots", 3000);
		itemsPrice.put("chain_helmet", 3000);
		itemsPrice.put("chain_chestplate", 7000);
		itemsPrice.put("chain_leggings", 5000);
		itemsPrice.put("chain_boots", 3000);
		itemsPrice.put("diamond_helmet", 3000);
		itemsPrice.put("diamond_chestplate", 7000);
		itemsPrice.put("diamond_leggings", 5000);
		itemsPrice.put("diamond_boots", 3000);
		//Page 4
		itemsPrice.put("bow", 1500);
		itemsPrice.put("arrow", 100);
		itemsPrice.put("web", 750);
		itemsPrice.put("magma_cream", 750);
		itemsPrice.put("iron_ingot", 900);
		itemsPrice.put("gold_ingot", 900);
		itemsPrice.put("gold_axe", 900);
		itemsPrice.put("ender_pearl", 2500);
		itemsPrice.put("fishing_rod", 100);
		itemsPrice.put("redstone", 20);
		//Page 5
		itemsPrice.put("tnt", 16000);
		itemsPrice.put("cookie", 100);
		itemsPrice.put("cooked_porkchop", 300);
		itemsPrice.put("smooth_brick", 170);
		itemsPrice.put("water_bucket", 1000);
		itemsPrice.put("lava_bucket", 15000);
		itemsPrice.put("cake", 1000);
		itemsPrice.put("bread", 300);
		itemsPrice.put("lava_bucket", 15000);
		//Page 6
		itemsPrice.put("coal", 50);
		itemsPrice.put("map", 2000);
		itemsPrice.put("ender_pearl", 3000);
		itemsPrice.put("clock", 1000);
		itemsPrice.put("flint", 1000);
		itemsPrice.put("pumpkin_seeds", 50);
		itemsPrice.put("melon_seeds", 50);

		//Selling prices
		//Page 1
		sellPrice.put("cake", 1000);
		sellPrice.put("melon_block", 300);
		sellPrice.put("pumpkin", 300);
		sellPrice.put("bread", 150);
		sellPrice.put("golden_apple", 700);
		sellPrice.put("raw_fish", 30);
		sellPrice.put("cooked_fish", 200);
		sellPrice.put("magma_cream", 600);
		sellPrice.put("web", 600);
		sellPrice.put("ghast_tear", 2500);
		//Page 2
		sellPrice.put("blaze_rod", 6000);
		sellPrice.put("blaze_powder", 1000);
		sellPrice.put("ender_pearl", 1750);
		sellPrice.put("vine", 100);
		sellPrice.put("potato", 150);
		sellPrice.put("iron_ingot", 750);
		sellPrice.put("gold_ingot", 750);
		sellPrice.put("diamond", 3500);
		sellPrice.put("apple", 200);
		sellPrice.put("wheat", 100);
		//Page 3
		sellPrice.put("tnt", 12000);
		sellPrice.put("coal", 100);
		sellPrice.put("emerald", 1100);
		sellPrice.put("arrow", 60);
		sellPrice.put("nether_brick", 140);
		sellPrice.put("nether_brick_fence", 150);
		sellPrice.put("nether_brick_stairs", 140);
		sellPrice.put("emerald_block", 35000);
		//Page 4
		sellPrice.put("diamond_block", 26000);
		sellPrice.put("spider_eye", 400);
		sellPrice.put("fermented_spider_eye", 550);
		sellPrice.put("raw_fish", 5);
		sellPrice.put("cooked_fish", 100);
		



	}

	public static void buyItem(Player player, String item, int amount) {
		if(sellPrice.isEmpty() || itemsPrice.isEmpty()) {
			addItemsToHashMap();
			return;
		}
		if(!item.equalsIgnoreCase("list") && !itemsPrice.containsKey(item.toLowerCase())) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.WHITE + "Shops don't sell that item");
			return;
		}
		if(item.equalsIgnoreCase("list")) {
			return;
		}
		int totalPrice = itemsPrice.get(item.toLowerCase()) * amount;
		int pMoney = Money.getMoney(player);
		String itemF = WordUtils.capitalize(item.toLowerCase().replaceAll("_", " "));
		if(pMoney < totalPrice) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.WHITE + "You don't have enough money for " + ChatColor.YELLOW + amount + " " + itemF);
			return;
		}
		Material mItem = Material.getMaterial(item);
		if(mItem == null) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.WHITE + "Shops don't sell that item");
			return;
		}
		Money.subtractMoney(player, totalPrice);
		player.getInventory().addItem(new ItemStack(mItem, amount));
		player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.WHITE + "You bought " + ChatColor.YELLOW + amount + " " + itemF.replaceAll("_", " ") + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + totalPrice);
	}

	public static void sellItem(Player player, String item, int amount) {
		if(sellPrice.isEmpty() || itemsPrice.isEmpty()) {
			addItemsToHashMap();
			return;
		}
		if(!sellPrice.containsKey(item.toLowerCase())) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.WHITE + "Shops don't buy that item");
			return;
		}
		int totalPrice = sellPrice.get(item.toLowerCase()) * amount;
		Material mItem = Material.getMaterial(item);
		if(mItem == null) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.WHITE + "Shops don't sell that item");
			return;
		}
		Money.addMoney(player, totalPrice);
		player.getInventory().removeItem(new ItemStack(mItem, amount));
		String itemF2 = WordUtils.capitalize(item.toLowerCase());
		player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.WHITE + "You sold " + ChatColor.YELLOW + amount + " " + itemF2.replaceAll("_", " ") + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + totalPrice);
	}

	public static void pageList(Player player, String listID, int page) {
		if(listID.equalsIgnoreCase("buy")) {
			Object[] keys = itemsPrice.entrySet().toArray();
			int listItemsFrom = 0;
			int listItemsTo = 0;
			if(page == 0) {
				page = 1;
			} else if(page == 1) {
				listItemsFrom = 0;
				listItemsTo = 10;
			} else if(page > 1) {
				listItemsFrom = (page * 10) - 10;
				listItemsTo = page * 10;
			}
			if(page > 5) {
				player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.GRAY + "Page doesn't exist");
				return;
			}
			player.sendMessage(ChatColor.DARK_GREEN + "Buy list " + ChatColor.WHITE + " - " + ChatColor.GRAY + "Page " + page);
			for(int i = listItemsFrom; i < listItemsTo; i++) {
				String item = keys[i].toString().replace("=", ChatColor.WHITE + " - " + ChatColor.DARK_GREEN);
				if(item != null) {
					player.sendMessage(ChatColor.YELLOW + WordUtils.capitalize(item.replaceAll("_", " ")));
				} else if(item == null) {
					player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.GRAY + "Page doesn't exist");
				}
			}
		} else if(listID.equalsIgnoreCase("sell")) {
			Object[] keys = sellPrice.entrySet().toArray();
			int listItemsFrom = 0;
			int listItemsTo = 0;
			if(page == 0) {
				page = 1;
			} else if(page == 1) {
				listItemsFrom = 0;
				listItemsTo = 10;
			} else if(page > 1) {
				listItemsFrom = (page * 10) - 10;
				listItemsTo = page * 10;
			}
			if(page > 4) {
				player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.GRAY + "Page doesn't exist");
				return;
			}
			player.sendMessage(ChatColor.DARK_GREEN + "Sell list " + ChatColor.WHITE + " - " + ChatColor.GRAY + "Page " + page);
			for(int i = listItemsFrom; i < listItemsTo; i++) {
				if(keys.length < i) {
					return;
				}
				String item = keys[i].toString().replace("=", ChatColor.WHITE + " - " + ChatColor.DARK_GREEN);
				if(item != null) {
					player.sendMessage(ChatColor.YELLOW + WordUtils.capitalize(item.replaceAll("_", " ")));
				} else if(item == null) {
					player.sendMessage(ChatColor.DARK_GREEN + "Shops: " + ChatColor.GRAY + "Page doesn't exist");
				}
			}
		}
	}


	public static boolean isInShops(Player player) {
		return (player.getLocation().getX() >= 789 && player.getLocation().getX() <= 833 && player.getLocation().getZ() >= 424 && player.getLocation().getZ() <= 476);
	}

	public static boolean isInShops(Location loc) {
		return (loc.getX() >= 789 && loc.getX() <= 833 && loc.getZ() >= 424 && loc.getZ() <= 476);
	}

	public static boolean isInOuterShops(Player player) {
		return (player.getLocation().getX() >= 776 && player.getLocation().getX() <= 845 && player.getLocation().getZ() >= 339 && player.getLocation().getZ() <= 517);
	}

	public static int maxEnchLevel(String enchantment) {
		if(enchantment.equalsIgnoreCase("dig_speed") || enchantment.equalsIgnoreCase("arrow_damage") || enchantment.equalsIgnoreCase("damage_all") || enchantment.equalsIgnoreCase("damage_undead") || enchantment.equalsIgnoreCase("damage_arthropods")) {
			return 5;
		} else if(enchantment.equalsIgnoreCase("loot_bonus_mobs") || enchantment.equalsIgnoreCase("thorns") || enchantment.equalsIgnoreCase("durability") || enchantment.equalsIgnoreCase("loot_bonus_blocks")) {
			return 3;
		} else if(enchantment.equalsIgnoreCase("silk_touch") || enchantment.equalsIgnoreCase("arrow_infinite") || enchantment.equalsIgnoreCase("arrow_fire") || enchantment.equalsIgnoreCase("water_worker")) {
			return 1;
		} else if(enchantment.equalsIgnoreCase("fire_aspect") || enchantment.equalsIgnoreCase("knockback")) {
			return 2;
		} else {
			return 4;
		}
	}
	public static int enchCost(String enchantment) {
		if(enchantment.equalsIgnoreCase("damage_all")) {
			return 150000;
		} else if(enchantment.equalsIgnoreCase("fire_aspect")) {
			return 170000;
		} else if(enchantment.equalsIgnoreCase("damage_arthropods") || enchantment.equalsIgnoreCase("damage_undead")) {
			return 25000;
		} else if(enchantment.equalsIgnoreCase("knockback")) {
			return 85000;
		} else if(enchantment.equalsIgnoreCase("loot_bonus_mobs") || enchantment.equalsIgnoreCase("silk_touch")) {
			return 45000;
		} else if(enchantment.equalsIgnoreCase("dig_speed")) {
			return 35000;
		} else if(enchantment.equalsIgnoreCase("durability")) {
			return 40000;
		} else if(enchantment.equalsIgnoreCase("loot_bonus_blocks") || enchantment.equalsIgnoreCase("arrow_damage")) {
			return 110000;
		} else if(enchantment.equalsIgnoreCase("arrow_knockback")) {
			return 75000;
		} else if(enchantment.equalsIgnoreCase("arrow_fire")) {
			return 180000;
		} else if(enchantment.equalsIgnoreCase("arrow_infinite")) {
			return 100000;
		} else if(enchantment.equalsIgnoreCase("protection_explosions") || enchantment.equals("thorns")) {
			return 60000;
		} else {
			return 30000;
		}
	}

	public static void buyEnchantment(Player player, ItemStack item, String type, int level) {
		int pMoney = Money.getMoney(player);
		int cost = enchCost(type) * level;
		item = player.getItemInHand();
		if(type.equalsIgnoreCase("damage_all")) {
			if(level > 5) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasSwords(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a sword for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.DAMAGE_ALL, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Sharpness " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("damage_all") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("fire_aspect")) {
			if(level != 1) {
				enchMsg(player, ChatColor.GRAY + "Invalid level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasSwords(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a sword for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.FIRE_ASPECT, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Fire Aspect " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("fire_aspect") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("damage_arthropods")) {
			if(level > 5) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasSwords(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a sword for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Bane of Arthropods " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("damage_arthropods") * level);

					}
				}
			}
		} else if(type.equalsIgnoreCase("knockback")) {
			if(level > 2) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasSwords(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a sword for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.KNOCKBACK, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Knockback " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("knockback") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("loot_bonus_mobs")) {
			if(level > 5) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasSwords(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a sword for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.LOOT_BONUS_MOBS, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Looting " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("loot_bonus_mobs") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("silk_touch")) {
			if(level != 1) {
				enchMsg(player, ChatColor.GRAY + "Invalid level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasPicks(player) && !utilPlayer.hasAxes(player) && !utilPlayer.hasShovels(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a tool for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.SILK_TOUCH, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Silk Touch " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("silk_touch") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("damage_undead")) {
			if(level > 5) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasSwords(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a sword for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.DAMAGE_UNDEAD, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Smite " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("damage_undead") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("dig_speed")) {
			if(level > 5) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasPicks(player) && !utilPlayer.hasAxes(player) && !utilPlayer.hasShovels(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a tool for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.DIG_SPEED, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Efficiency " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("dig_speed") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("durability")) {
			if(level > 3) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasPicks(player) && !utilPlayer.hasAxes(player) && !utilPlayer.hasShovels(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a tool for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.DURABILITY, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Unbreaking " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("durability") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("loot_bonus_blocks")) {
			if(level > 3) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasPicks(player) && !utilPlayer.hasAxes(player) && !utilPlayer.hasShovels(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a tool for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Fortune " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("loot_bonus_blocks") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("arrow_damage")) {
			if(level > 5) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(item.getType() != Material.BOW) {
						enchMsg(player, ChatColor.GRAY + "You must have a bow for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.ARROW_DAMAGE, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Power " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("arrow_damage") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("arrow_knockback")) {
			if(level > 2) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(item.getType() != Material.BOW) {
						enchMsg(player, ChatColor.GRAY + "You must have a bow for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.ARROW_KNOCKBACK, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Punch " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("arrow_knockback") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("arrow_fire")) {
			if(level != 1) {
				enchMsg(player, ChatColor.GRAY + "Invalid level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(item.getType() != Material.BOW) {
						enchMsg(player, ChatColor.GRAY + "You must have a bow for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.ARROW_FIRE, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Flame " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("arrow_fire") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("arrow_infinite")) {
			if(level != 1) {
				enchMsg(player, ChatColor.GRAY + "Invalid level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(item.getType() != Material.BOW) {
						enchMsg(player, ChatColor.GRAY + "You must have a bow for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.ARROW_FIRE, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Infinity " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("arrow_infinite") * level);

					}
				}
			}
		} else if(type.equalsIgnoreCase("thorns")) {
			if(level > 3) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasArmourInHand(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a piece of armour for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.THORNS, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Thorns " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("thorns") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("protection_explosions")) {
			if(level > 4) {
				enchMsg(player, ChatColor.GRAY + "Level exceeded enchantment's max level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasArmourInHand(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a piece of armour for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Blast Protection " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("protection_explosions") * level);
					}
				}
			}
		} else if(type.equalsIgnoreCase("water_worker")) {
			if(level != 1) {
				enchMsg(player, ChatColor.GRAY + "Invalid level");
			} else {
				if(pMoney < cost) {
					enchMsg(player, ChatColor.GRAY + "You have insufficient money for this");
				} else {
					if(!utilPlayer.hasArmourInHand(player)) {
						enchMsg(player, ChatColor.GRAY + "You must have a piece of armour for this");
					} else {
						Money.subtractMoney(player, cost);
						item.addEnchantment(Enchantment.WATER_WORKER, level);
						Shops.enchMsg(player, ChatColor.WHITE + "You bought " + ChatColor.YELLOW + "Aqua Affinity " + level + ChatColor.WHITE + " for " + ChatColor.DARK_GREEN + "$" + Shops.enchCost("water_worker") * level);
					}
				}
			}
		}
	}

	public static void enchMsg(Player player, String message) {
		player.sendMessage(ChatColor.DARK_GREEN + "Enchantment: " + message);
	}

	public static boolean isInEnchShop(Player player) {
		return (utilPlayer.isInArea(player, 818, 827, 456, 470));
	}

}
