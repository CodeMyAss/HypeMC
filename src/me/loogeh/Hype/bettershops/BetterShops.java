package me.loogeh.Hype.bettershops;


import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.loogeh.Hype.C;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.economy.Money;
import me.loogeh.Hype.util.utilItems;

public class BetterShops {
	public static Hype plugin;
	
	public static HashMap<String, Integer> buyPrice = new HashMap<String, Integer>();
	public static HashMap<String, SellItem> sellPrice = new HashMap<String, SellItem>();
	
	public static HashMap<String, ShopLog> log = new HashMap<String, ShopLog>();
	
	public static HashMap<String, Zombie> shops = new HashMap<String, Zombie>();
	
	public Inventory inv;
	
	public void addItem(Inventory inv, ItemStack item, int slot, String desc) {
		ItemMeta meta = item.getItemMeta();
		String itemStr = item.getType().toString().replaceAll("_", " ");
		meta.setDisplayName(WordUtils.capitalize(itemStr.toLowerCase()));
		meta.setLore(Arrays.asList(ChatColor.GREEN + "$" + getCost(item.getType()), desc));
		item.setItemMeta(meta);
		inv.setItem(slot, item);
	}
	
	public void addItem(Inventory inv, ItemStack item, int slot, String title, String desc) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(title);
		meta.setLore(Arrays.asList(ChatColor.GREEN + "$" + getCost(item.getType()), desc));
		item.setItemMeta(meta);
		inv.setItem(slot, item);
	}
	
	public void addItem(Inventory inv, ItemStack item, int slot) {
		ItemMeta meta = item.getItemMeta();
		String itemStr = item.getType().toString().replaceAll("_", " ");
		meta.setDisplayName(WordUtils.capitalize(itemStr.toLowerCase()));
		meta.setLore(Arrays.asList(ChatColor.GREEN + "$" + getCost(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(slot, item);
	}
	
	public void addItem(Inventory inv, ItemStack item, String title, int slot) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(title);
		meta.setLore(Arrays.asList(ChatColor.GREEN + "$" + getCost(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(slot, item);
	}
	
	public void addItemNoPrice(Inventory inv, ItemStack item, int slot, String desc) {
		ItemMeta meta = item.getItemMeta();
		String itemStr = item.getType().toString().replaceAll("_", " ");
		meta.setDisplayName(WordUtils.capitalize(itemStr.toLowerCase()));
		meta.setLore(Arrays.asList(desc));
		item.setItemMeta(meta);
		inv.setItem(slot, item);
	}
	
	public void addItemNoPrice(Inventory inv, ItemStack item, int slot, String title, String desc) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(title);
		meta.setLore(Arrays.asList(desc));
		item.setItemMeta(meta);
		inv.setItem(slot, item);
	}
	
	@SuppressWarnings("deprecation")
	public void addEnchantmentItem(Inventory inv, ItemStack item, Enchantment enchantment, int slot) {
		item.addEnchantment(enchantment, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(utilItems.toCommon(enchantment.getId()));
		meta.setLore(Arrays.asList(ChatColor.GREEN + "$" + getEnchantmentPrice(enchantment)));
		item.setItemMeta(meta);
		inv.setItem(slot, item);
	}
	
	public void openShop(Player player, ItemType iType) {
		Inventory inv;
		log.put(player.getName(), new ShopLog(player.getName(), 0));
		if(iType == ItemType.BAKER) {
			inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Shop Baker");
			addItemNoPrice(inv, new ItemStack(Material.ENCHANTED_BOOK), 0, "Tier 1", "Food which heals 2 hunger");
			addItem(inv, new ItemStack(Material.APPLE), 9);
			addItem(inv, new ItemStack(Material.GOLDEN_APPLE), 18);
			addItemNoPrice(inv, new ItemStack(Material.ENCHANTED_BOOK), 2, "Tier 2", "Food which heals 2.5 hunger");
			addItem(inv, new ItemStack(Material.COOKED_FISH), 11);
			addItem(inv, new ItemStack(Material.BREAD), 20);
			addItemNoPrice(inv, new ItemStack(Material.ENCHANTED_BOOK), 4, "Tier 3", "Food which heals 3 hunger");
			addItem(inv, new ItemStack(Material.COOKED_CHICKEN), 13);
			addItem(inv, new ItemStack(Material.BAKED_POTATO), 22);
			addItem(inv, new ItemStack(Material.GOLDEN_CARROT), 31);
			addItemNoPrice(inv, new ItemStack(Material.ENCHANTED_BOOK), 6, "Tier 4", "Food which heals 4 hunger");
			addItem(inv, new ItemStack(Material.COOKED_BEEF), 15);
			addItem(inv, new ItemStack(Material.GRILLED_PORK), 24);
			addItem(inv, new ItemStack(Material.PUMPKIN_PIE), 33);
			addItem(inv, new ItemStack(Material.MUSHROOM_SOUP), 42);
			addItemNoPrice(inv, new ItemStack(Material.ENCHANTED_BOOK), 8, "Tier 5", "Food which heals 6 hunger");
			addItem(inv, new ItemStack(Material.CAKE), 17);
			player.openInventory(inv);
		}
		if(iType == ItemType.WEAPONRY) {
			inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Shop Weaponry");
			addItemNoPrice(inv, new ItemStack(Material.EMERALD), 4, ChatColor.GREEN + "Information", ChatColor.GRAY + "Juggernaut is not necessarily the best");
			addItem(inv, new ItemStack(Material.LEATHER_HELMET), C.shopItem + "Archers Helmet", 9);
			addItem(inv, new ItemStack(Material.LEATHER_CHESTPLATE), C.shopItem + "Archers Chestplate", 10);
			addItem(inv, new ItemStack(Material.LEATHER_LEGGINGS), C.shopItem + "Archers Leggings", 11);
			addItem(inv, new ItemStack(Material.LEATHER_BOOTS), C.shopItem + "Archers Boots", 12);
			addItem(inv, new ItemStack(Material.CHAINMAIL_HELMET), C.shopItem + "Specialists Helmet", 18);
			addItem(inv, new ItemStack(Material.CHAINMAIL_CHESTPLATE), C.shopItem + "Specialists Chestplate", 19);
			addItem(inv, new ItemStack(Material.CHAINMAIL_LEGGINGS), C.shopItem + "Specialists Leggings", 20);
			addItem(inv, new ItemStack(Material.CHAINMAIL_BOOTS), C.shopItem + "Specialists Boots", 21);
			addItem(inv, new ItemStack(Material.GOLD_HELMET), C.shopItem + "Agilitys Helmet", 27);
			addItem(inv, new ItemStack(Material.GOLD_CHESTPLATE), C.shopItem + "Agilitys Chestplate", 28);
			addItem(inv, new ItemStack(Material.GOLD_LEGGINGS), C.shopItem + "Agilitys Leggings", 29);
			addItem(inv, new ItemStack(Material.GOLD_BOOTS), C.shopItem + "Agilitys Boots", 30);
			addItem(inv, new ItemStack(Material.IRON_HELMET), C.shopItem + "Samurai's Helmet", 36);
			addItem(inv, new ItemStack(Material.IRON_CHESTPLATE), C.shopItem + "Samurai's Chestplate", 37);
			addItem(inv, new ItemStack(Material.IRON_LEGGINGS), C.shopItem + "Samurai's Leggings", 38);
			addItem(inv, new ItemStack(Material.IRON_BOOTS), C.shopItem + "Samurai's Boots", 39);
			addItem(inv, new ItemStack(Material.DIAMOND_HELMET), C.shopItem + "Jugg's Helmet", 45);
			addItem(inv, new ItemStack(Material.DIAMOND_CHESTPLATE), C.shopItem + "Jugg's Chestplate", 46);
			addItem(inv, new ItemStack(Material.DIAMOND_LEGGINGS), C.shopItem + "Jugg's Leggings", 47);
			addItem(inv, new ItemStack(Material.DIAMOND_BOOTS), C.shopItem + "Jugg's Boots", 48);
			addItem(inv, new ItemStack(Material.IRON_SWORD), 44);
			addItem(inv, new ItemStack(Material.DIAMOND_SWORD), 53);
			addItem(inv, new ItemStack(Material.WEB), 43, ChatColor.AQUA + "Web Blanket", ChatColor.GRAY + "Shoot webs at players!");
			addItem(inv, new ItemStack(Material.DIAMOND_HOE), 8, ChatColor.AQUA + "Lightning Sceptre", ChatColor.GRAY + "Cast a lightning strike upon command");
			addItem(inv, new ItemStack(Material.GOLD_HOE), 7, ChatColor.AQUA + "Evasion", ChatColor.GRAY + "Go invisible and get Jump VIII");
			addItem(inv, new ItemStack(Material.GOLD_SPADE), 35, ChatColor.AQUA + "Staff of Speed", ChatColor.GRAY + "Give a player and yourself Speed II");
			addItem(inv, new ItemStack(Material.GOLD_AXE), 25, ChatColor.AQUA + "Combustion Axe", ChatColor.GRAY + "Set people on fire");
			addItem(inv, new ItemStack(Material.DIAMOND_AXE), 26, ChatColor.AQUA + "Velocity Scythe", ChatColor.GRAY + "Give people Slowness I");
			addItem(inv, new ItemStack(Material.BOW), 16);
			addItem(inv, new ItemStack(Material.ARROW), 17);
			player.openInventory(inv);
			
		}
		if(iType == ItemType.BUILDER) {
			inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Shop Builder");
			addItem(inv, new ItemStack(Material.COBBLESTONE), 9);
			addItem(inv, new ItemStack(Material.DIRT), 10);
			addItem(inv, new ItemStack(Material.SAND), 11);
			addItem(inv, new ItemStack(Material.SANDSTONE), 12);
			addItem(inv, new ItemStack(Material.LOG, 1, (short) 0), ChatColor.WHITE + "Oak Wood", 18);
			addItem(inv, new ItemStack(Material.LOG, 1, (short) 1), ChatColor.WHITE + "Spruce Wood", 19);
			addItem(inv, new ItemStack(Material.LOG, 1, (short) 2), ChatColor.WHITE + "Birch Wood", 20);
			addItem(inv, new ItemStack(Material.LOG, 1, (short) 3), ChatColor.WHITE + "Jungle Wood", 21);
			addItem(inv, new ItemStack(Material.SMOOTH_BRICK, 1, (short) 0), ChatColor.WHITE + "Stone Brick", 27);
			addItem(inv, new ItemStack(Material.SMOOTH_BRICK, 1, (short) 2), ChatColor.WHITE + "Cracked Stone Brick", 28);
			addItem(inv, new ItemStack(Material.SMOOTH_BRICK, 1, (short) 3), ChatColor.WHITE + "Chiseled Stone Brick", 29);
			addItem(inv, new ItemStack(Material.BRICK), 30);
			addItem(inv, new ItemStack(Material.GLASS), 36);
			addItem(inv, new ItemStack(Material.WOOL), 37);
			addItem(inv, new ItemStack(Material.PISTON_BASE), ChatColor.WHITE + "Piston", 38);
			addItem(inv, new ItemStack(Material.PISTON_STICKY_BASE), ChatColor.WHITE + "Sticky Piston", 39);
			player.openInventory(inv);
		}
		if(iType == ItemType.MINER) {
			inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Shop Miner");
			addItem(inv, new ItemStack(Material.IRON_INGOT), 9);
			addItem(inv, new ItemStack(Material.GOLD_INGOT), 10);
			addItem(inv, new ItemStack(Material.DIAMOND_PICKAXE), 18);
			addItem(inv, new ItemStack(Material.IRON_PICKAXE), 19);
			addItem(inv, new ItemStack(Material.GOLD_PICKAXE), 20);
			addItem(inv, new ItemStack(Material.STONE_PICKAXE), 21);
			addItem(inv, new ItemStack(Material.DIAMOND_SPADE), 27);
			addItem(inv, new ItemStack(Material.IRON_SPADE), 28);
			addItem(inv, new ItemStack(Material.STONE_SPADE), 29);
			addItem(inv, new ItemStack(Material.REDSTONE), 11);
			player.openInventory(inv);
		}
		if(iType == ItemType.MISCELLANEOUS) {
			inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Shop Miscellaneous");
			addItem(inv, new ItemStack(Material.TNT), ChatColor.WHITE + "TNT", 9);
			addItem(inv, new ItemStack(Material.FLINT_AND_STEEL), 10);
			addItem(inv, new ItemStack(Material.MAP), 18);
			addItem(inv, new ItemStack(Material.BREWING_STAND), 19);
			addItem(inv, new ItemStack(Material.LAVA_BUCKET), 27);
			addItem(inv, new ItemStack(Material.WATER_BUCKET), 28);
			addItem(inv, new ItemStack(Material.DIAMOND_SPADE), ChatColor.WHITE + "Diamond Shovel", 29);
			addItem(inv, new ItemStack(Material.IRON_DOOR), 36);
			addItem(inv, new ItemStack(Material.SADDLE), 37);
			player.openInventory(inv);
		}
		if(iType == ItemType.ENCHANTER) {
			inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Shop Enchanter");
			addItemNoPrice(inv, new ItemStack(Material.EMERALD), 4, ChatColor.GREEN + "Information", ChatColor.GRAY + "Enchantments are very expensive, use them wisely");
			addEnchantmentItem(inv, new ItemStack(Material.DIAMOND_SWORD), Enchantment.DAMAGE_ALL, 9);
			addEnchantmentItem(inv, new ItemStack(Material.DIAMOND_SWORD), Enchantment.LOOT_BONUS_MOBS, 10);
			addEnchantmentItem(inv, new ItemStack(Material.DIAMOND_SWORD), Enchantment.KNOCKBACK, 11);
			addEnchantmentItem(inv, new ItemStack(Material.DIAMOND_SWORD), Enchantment.DAMAGE_UNDEAD, 18);
			addEnchantmentItem(inv, new ItemStack(Material.DIAMOND_SWORD), Enchantment.DAMAGE_ARTHROPODS, 19);
			addEnchantmentItem(inv, new ItemStack(Material.DIAMOND_SWORD), Enchantment.FIRE_ASPECT, 20);
			addEnchantmentItem(inv, new ItemStack(Material.IRON_PICKAXE), Enchantment.LOOT_BONUS_BLOCKS, 27);
			addEnchantmentItem(inv, new ItemStack(Material.IRON_PICKAXE), Enchantment.DURABILITY, 28);
			addEnchantmentItem(inv, new ItemStack(Material.IRON_PICKAXE), Enchantment.DIG_SPEED, 29);
			addEnchantmentItem(inv, new ItemStack(Material.BOW), Enchantment.ARROW_DAMAGE, 45);
			addEnchantmentItem(inv, new ItemStack(Material.BOW), Enchantment.ARROW_KNOCKBACK, 46);
			addEnchantmentItem(inv, new ItemStack(Material.BOW), Enchantment.ARROW_FIRE, 47);
			addEnchantmentItem(inv, new ItemStack(Material.BOW), Enchantment.ARROW_INFINITE, 48);
			addEnchantmentItem(inv, new ItemStack(Material.IRON_HELMET), Enchantment.PROTECTION_EXPLOSIONS, 15);
			addEnchantmentItem(inv, new ItemStack(Material.IRON_CHESTPLATE), Enchantment.PROTECTION_EXPLOSIONS, 24);
			addEnchantmentItem(inv, new ItemStack(Material.IRON_LEGGINGS), Enchantment.PROTECTION_EXPLOSIONS, 33);
			addEnchantmentItem(inv, new ItemStack(Material.IRON_BOOTS), Enchantment.PROTECTION_EXPLOSIONS, 42);
			addEnchantmentItem(inv, new ItemStack(Material.GOLD_HELMET), Enchantment.THORNS, 16);
			addEnchantmentItem(inv, new ItemStack(Material.GOLD_CHESTPLATE), Enchantment.THORNS, 25);
			addEnchantmentItem(inv, new ItemStack(Material.GOLD_LEGGINGS), Enchantment.THORNS, 34);
			addEnchantmentItem(inv, new ItemStack(Material.GOLD_BOOTS), Enchantment.THORNS, 43);
			player.openInventory(inv);
		}
	}
	
	public static void spawnEntities() {
		World world = Bukkit.getWorld("world");
		Zombie builder = (Zombie) world.spawnEntity(new Location(world, 797.5, 69.3, 437.5, -90.0F, 0.0F), EntityType.ZOMBIE);
		builder.setCustomName(ChatColor.GREEN + "Builder");
		builder.setCustomNameVisible(true);
		builder.setCanPickupItems(false);
		builder.setBaby(false);
		builder.setRemoveWhenFarAway(false);
		builder.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
		builder.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		Zombie weaponry = (Zombie) world.spawnEntity(new Location(world, 822.6, 69.3, 431.5, 0.0F, 0.0F), EntityType.ZOMBIE);
		weaponry.setCustomName(ChatColor.GREEN + "Weaponry");
		weaponry.setCustomNameVisible(true);
		weaponry.setCanPickupItems(false);
		weaponry.setBaby(false);
		weaponry.setRemoveWhenFarAway(false);
		weaponry.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
		weaponry.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		EntityEquipment equip = weaponry.getEquipment();
		equip.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		equip.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		equip.setBoots(new ItemStack(Material.IRON_BOOTS));
		equip.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
		Zombie baker = (Zombie) world.spawnEntity(new Location(world, 796.6, 69.3, 463.5, -90.0F, 0.0F), EntityType.ZOMBIE);
		baker.setCustomName(ChatColor.GREEN + "Baker");
		baker.setCustomNameVisible(true);
		baker.setCanPickupItems(false);
		baker.setBaby(false);
		baker.setRemoveWhenFarAway(false);
		baker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
		baker.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		Zombie misc = (Zombie) world.spawnEntity(new Location(world, 819.4, 69.3, 465.4, -90.0F, 0.0F), EntityType.ZOMBIE);
		misc.setCustomName(ChatColor.GREEN + "Miscellaneous");
		misc.setCustomNameVisible(true);
		misc.setCanPickupItems(false);
		misc.setBaby(false);
		misc.setRemoveWhenFarAway(false);
		misc.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
		misc.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		Zombie miner = (Zombie) world.spawnEntity(new Location(world, 799.5, 69.3, 457.5, 0.0F, 0.0F), EntityType.ZOMBIE);
		miner.setCustomName(ChatColor.GREEN + "Miner");
		miner.setCustomNameVisible(true);
		miner.setCanPickupItems(false);
		miner.setBaby(false);
		miner.setRemoveWhenFarAway(false);
		miner.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
		miner.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		Zombie enchanter = (Zombie) world.spawnEntity(new Location(world, 819.3, 69.3, 468.2, -90.0F, 0.0F), EntityType.ZOMBIE);
		enchanter.setCustomName(ChatColor.GREEN + "Enchanter");
		enchanter.setCustomNameVisible(true);
		enchanter.setCanPickupItems(false);
		enchanter.setBaby(false);
		enchanter.setRemoveWhenFarAway(false);
		enchanter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
		enchanter.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		shops.put("builder", builder);
		shops.put("miner", miner);
		shops.put("weaponry", weaponry);
		shops.put("baker", baker);
		shops.put("misc", misc);
		shops.put("enchanter", enchanter);
	}
	
	public static void spawnShop(ItemType shop) {
		World world = Bukkit.getWorld("world");
		if(shop == ItemType.BAKER) {
			Zombie baker = (Zombie) world.spawnEntity(new Location(world, 796.6, 69.3, 463.5, -90.0F, 0.0F), EntityType.ZOMBIE);
			baker.setCustomName(ChatColor.GREEN + "Baker");
			baker.setCustomNameVisible(true);
			baker.setCanPickupItems(false);
			baker.setBaby(false);
			baker.setRemoveWhenFarAway(false);
			baker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
			baker.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		}
		if(shop == ItemType.BUILDER) {
			Zombie builder = (Zombie) world.spawnEntity(new Location(world, 797.5, 69.3, 437.5, -90.0F, 0.0F), EntityType.ZOMBIE);
			builder.setCustomName(ChatColor.GREEN + "Builder");
			builder.setCustomNameVisible(true);
			builder.setCanPickupItems(false);
			builder.setBaby(false);
			builder.setRemoveWhenFarAway(false);
			builder.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
			builder.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		}
		if(shop == ItemType.ENCHANTER) {
			Zombie enchanter = (Zombie) world.spawnEntity(new Location(world, 819.3, 69.3, 468.2, -90.0F, 0.0F), EntityType.ZOMBIE);
			enchanter.setCustomName(ChatColor.GREEN + "Enchanter");
			enchanter.setCustomNameVisible(true);
			enchanter.setCanPickupItems(false);
			enchanter.setBaby(false);
			enchanter.setRemoveWhenFarAway(false);
			enchanter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
			enchanter.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		}
		if(shop == ItemType.MINER) {
			Zombie miner = (Zombie) world.spawnEntity(new Location(world, 799.5, 69.3, 457.5, 0.0F, 0.0F), EntityType.ZOMBIE);
			miner.setCustomName(ChatColor.GREEN + "Miner");
			miner.setCustomNameVisible(true);
			miner.setCanPickupItems(false);
			miner.setBaby(false);
			miner.setRemoveWhenFarAway(false);
			miner.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
			miner.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		}
		if(shop == ItemType.WEAPONRY) {
			Zombie weaponry = (Zombie) world.spawnEntity(new Location(world, 822.6, 69.3, 431.5, 0.0F, 0.0F), EntityType.ZOMBIE);
			weaponry.setCustomName(ChatColor.GREEN + "Weaponry");
			weaponry.setCustomNameVisible(true);
			weaponry.setCanPickupItems(false);
			weaponry.setBaby(false);
			weaponry.setRemoveWhenFarAway(false);
			weaponry.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
			weaponry.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -10));
		}
	}
	
	
	public static void killShops() {
		if(shops.containsKey("builder")) shops.get("builder").setHealth(0.0D);
		if(shops.containsKey("miner")) shops.get("miner").setHealth(0.0D);
		if(shops.containsKey("weaponry")) shops.get("weaponry").setHealth(0.0D);
		if(shops.containsKey("baker")) shops.get("baker").setHealth(0.0D);
		if(shops.containsKey("misc")) shops.get("misc").setHealth(0.0D);
	}
	
	public static void ticksReset() {
		for(String shops : BetterShops.shops.keySet()) {
			BetterShops.shops.get(shops).setTicksLived(5);
		}
	}
	
	
	public static void spawnEntities(Player player) {
		World world = Bukkit.getWorld("world");
		Zombie builder = (Zombie) world.spawnEntity(player.getLocation(), EntityType.ZOMBIE);
		builder.setCustomName(ChatColor.GREEN + "Builder");
		builder.setCustomNameVisible(true);
		builder.setCanPickupItems(false);
		builder.setBaby(false);
		Zombie weaponsmith = (Zombie) world.spawnEntity(player.getLocation(), EntityType.ZOMBIE);
		weaponsmith.setCustomName(ChatColor.GREEN + "Weaponry");
		weaponsmith.setCustomNameVisible(true);
		weaponsmith.setCanPickupItems(false);
		weaponsmith.setBaby(false);
		EntityEquipment equip = weaponsmith.getEquipment();
		equip.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		equip.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		equip.setBoots(new ItemStack(Material.IRON_BOOTS));
		equip.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
		Zombie baker = (Zombie) world.spawnEntity(player.getLocation(), EntityType.ZOMBIE);
		baker.setCustomName(ChatColor.GREEN + "Baker");
		baker.setCustomNameVisible(true);
		baker.setCanPickupItems(false);
		baker.setBaby(false);
		Zombie misc = (Zombie) world.spawnEntity(player.getLocation(), EntityType.ZOMBIE);
		misc.setCustomName(ChatColor.GREEN + "Miscellaneous");
		misc.setCustomNameVisible(true);
		misc.setCanPickupItems(false);
		misc.setBaby(false);
		Zombie miner = (Zombie) world.spawnEntity(player.getLocation(), EntityType.ZOMBIE);
		miner.setCustomName(ChatColor.GREEN + "Miner");
		miner.setCustomNameVisible(true);
		miner.setCanPickupItems(false);
		miner.setBaby(false);
		
	}

	public static int getCost(String item) {
		if(!buyPrice.containsKey(item)) return 0;
		return buyPrice.get(item);
	}
	
	public static int getCost(Material mat) {
		if(!buyPrice.containsKey(mat.toString().toLowerCase())) return 0;
		return buyPrice.get(mat.toString().toLowerCase());
	}
	
	public static ItemType getType(String name) {
		if(name.equalsIgnoreCase("Enchanter")) return ItemType.ENCHANTER;
		else if(name.equalsIgnoreCase("Miner")) return ItemType.MINER;
		else if(name.equalsIgnoreCase("Baker")) return ItemType.BAKER;
		else if(name.equalsIgnoreCase("Builder")) return ItemType.BUILDER;
		else if(name.equalsIgnoreCase("Weaponry")) return ItemType.WEAPONRY;
		else return null;
	}
	
	public static void sendMessage(Player player, String message) {
		if(player == null) {
			return;
		}
		player.sendMessage(ChatColor.DARK_GREEN + "Shops - " + message);
	}
	
	public static void sendLog(Player player) {
		if(player == null) return;
		if(!log.containsKey(player.getName())) {
			return;
		}
		int money = Money.getMoney(player);
		int cost = log.get(player.getName()).cost;
		if(cost == 0) {
			return;
		}
		for(String keys : log.get(player.getName()).items.keySet()) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shop Log " + ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "[" + ChatColor.GRAY + utilItems.toCommon(keys) + " x" + log.get(player.getName()).items.get(keys) + ChatColor.DARK_GREEN + "] - $" + ChatColor.GRAY + getCost(keys) * log.get(player.getName()).items.get(keys));
		}
		if(cost < 0) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shop Log " + ChatColor.GRAY + " - You gained " + ChatColor.DARK_GREEN + "$" + Math.abs(cost) + ChatColor.GRAY + " that session");
		}
		if(cost > 0) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shop Log " + ChatColor.GRAY + " - You spent " + ChatColor.DARK_GREEN + "$" + cost + ChatColor.GRAY + " that session");
		}
		player.sendMessage(ChatColor.DARK_GREEN + "Shop Log " + ChatColor.GRAY + " - New balance " + ChatColor.DARK_GREEN + "$" + money);
	}
	
	@SuppressWarnings("deprecation")
	public static void sendELog(Player player) {
		if(player == null) return;
		int money = Money.getMoney(player);
		int cost = log.get(player.getName()).cost;
		if(cost == 0) return;
		for(Enchantment keys : log.get(player.getName()).enchantments.keySet()) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shop Log " + ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "[" + ChatColor.GRAY + utilItems.toCommon(keys.getId()) + " x" + log.get(player.getName()).enchantments.get(keys) + ChatColor.DARK_GREEN + "] - $" + ChatColor.GRAY + getEnchantmentPrice(keys) * log.get(player.getName()).enchantments.get(keys));
		}
		if(cost < 0) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shop Log " + ChatColor.GRAY + " - You gained " + ChatColor.DARK_GREEN + "$" + Math.abs(cost) + ChatColor.GRAY + " that session");
		}
		if(cost > 0) {
			player.sendMessage(ChatColor.DARK_GREEN + "Shop Log " + ChatColor.GRAY + " - You spent " + ChatColor.DARK_GREEN + "$" + cost + ChatColor.GRAY + " that session");
		}
		player.sendMessage(ChatColor.DARK_GREEN + "Shop Log " + ChatColor.GRAY + " - New balance " + ChatColor.DARK_GREEN + "$" + money);
		
	}

	public static void populateHashMap() {
		//Buying prices
		buyPrice.put("iron_sword", 3500);
		buyPrice.put("diamond_sword", 14000);
		buyPrice.put("iron_axe", 2500);
		buyPrice.put("diamond_axe", 8000);
		buyPrice.put("diamond_spade", 7000);
		buyPrice.put("iron_pickaxe", 3000);
		buyPrice.put("diamond_pickaxe", 12000);
		buyPrice.put("leather_helmet", 3000);
		buyPrice.put("leather_chestplate", 7000);
		buyPrice.put("leather_leggings", 5000);
		buyPrice.put("leather_boots", 3000);
		buyPrice.put("gold_helmet", 3000);
		buyPrice.put("gold_chestplate", 7000);
		buyPrice.put("gold_leggings", 5000);
		buyPrice.put("gold_boots", 3000);
		buyPrice.put("iron_helmet", 3000);
		buyPrice.put("iron_chestplate", 7000);
		buyPrice.put("iron_leggings", 5000);
		buyPrice.put("iron_boots", 3000);
		buyPrice.put("chainmail_helmet", 3000);
		buyPrice.put("chainmail_chestplate", 7000);
		buyPrice.put("chainmail_leggings", 5000);
		buyPrice.put("chainmail_boots", 3000);
		buyPrice.put("diamond_helmet", 3000);
		buyPrice.put("diamond_chestplate", 7000);
		buyPrice.put("diamond_leggings", 5000);
		buyPrice.put("diamond_boots", 3000);
		buyPrice.put("bow", 1500);
		buyPrice.put("arrow", 100);
		buyPrice.put("web", 750);
		buyPrice.put("magma_cream", 750);
		buyPrice.put("iron_ingot", 900);
		buyPrice.put("gold_ingot", 900);
		buyPrice.put("gold_axe", 900);
		buyPrice.put("ender_pearl", 2500);
		buyPrice.put("fishing_rod", 100);
		buyPrice.put("redstone", 20);
		buyPrice.put("tnt", 16000);
		buyPrice.put("smooth_brick", 170);
		buyPrice.put("water_bucket", 1000);
		buyPrice.put("lava_bucket", 15000);
		buyPrice.put("coal", 50);
		buyPrice.put("map", 2000);
		buyPrice.put("ender_pearl", 3000);
		buyPrice.put("clock", 1000);
		buyPrice.put("flint", 1000);
		buyPrice.put("pumpkin_seeds", 50);
		buyPrice.put("melon_seeds", 50);
		buyPrice.put("log", 120);
		buyPrice.put("gold_hoe", 1200);
		buyPrice.put("gold_spade", 1000);
		buyPrice.put("diamond_hoe", 12000);
		buyPrice.put("cobblestone", 4);
		buyPrice.put("dirt", 1);
		buyPrice.put("sand", 6);
		buyPrice.put("sandstone", 8);
		buyPrice.put("flint_and_steel", 200);
		buyPrice.put("gold_pickaxe", 1000);
		buyPrice.put("stone_pickaxe", 100);
		buyPrice.put("iron_spade", 800);
		buyPrice.put("stone_spade", 100);
		buyPrice.put("piston_base", 1500);
		buyPrice.put("piston_sticky_base", 2500);
		buyPrice.put("wool", 70);
		buyPrice.put("glass", 100);
		buyPrice.put("brick", 120);
		buyPrice.put("iron_door", 1000);
		buyPrice.put("saddle", 1300);
		buyPrice.put("brewing_stand", 48000);
		
		//Food
		buyPrice.put("golden_apple", 2400);
		buyPrice.put("cookie", 100);
		buyPrice.put("cake", 1000);
		buyPrice.put("bread", 300);
		buyPrice.put("apple", 200);
		buyPrice.put("cooked_beef", 350);
		buyPrice.put("grilled_pork", 350);
		buyPrice.put("mushroom_soup", 300);
		buyPrice.put("cooked_fish", 150);
		buyPrice.put("baked_potato", 200);
		buyPrice.put("cooked_chicken", 200);
		buyPrice.put("golden_carrot", 220);
		buyPrice.put("pumpkin_pie", 350);
		
		
		
		//Selling prices
		sellPrice.put("cake", new SellItem(1000, ItemType.BAKER));
		sellPrice.put("melon_block", new SellItem(300, ItemType.BAKER));
		sellPrice.put("pumpkin", new SellItem(300, ItemType.BAKER));
		sellPrice.put("bread", new SellItem(150, ItemType.BAKER));
		sellPrice.put("golden_apple", new SellItem(700, ItemType.BAKER));
		sellPrice.put("magma_cream", new SellItem(600, ItemType.WEAPONRY));
		sellPrice.put("web", new SellItem(600, ItemType.WEAPONRY));
		sellPrice.put("ghast_tear", new SellItem(4500, ItemType.MISCELLANEOUS));
		sellPrice.put("blaze_rod", new SellItem(2500, ItemType.MISCELLANEOUS));
		sellPrice.put("blaze_powder", new SellItem(1000, ItemType.MISCELLANEOUS));
		sellPrice.put("ender_pearl", new SellItem(1750, ItemType.MISCELLANEOUS));
		sellPrice.put("vine", new SellItem(100, ItemType.MISCELLANEOUS));
		sellPrice.put("potato", new SellItem(150, ItemType.BAKER));
		sellPrice.put("iron_ingot", new SellItem(750, ItemType.MINER));
		sellPrice.put("gold_ingot", new SellItem(750, ItemType.MINER));
		sellPrice.put("diamond", new SellItem(3500, ItemType.MINER));
		sellPrice.put("apple", new SellItem(200, ItemType.BAKER));
		sellPrice.put("wheat", new SellItem(100, ItemType.BAKER));
		sellPrice.put("tnt", new SellItem(12000, ItemType.MISCELLANEOUS));
		sellPrice.put("coal", new SellItem(100, ItemType.MINER));
		sellPrice.put("emerald", new SellItem(5000, ItemType.MINER));
		sellPrice.put("arrow", new SellItem(60, ItemType.WEAPONRY));
		sellPrice.put("log", new SellItem(80, ItemType.BUILDER));
		sellPrice.put("wood", new SellItem(20, ItemType.BUILDER));
		sellPrice.put("nether_brick", new SellItem(140, ItemType.BUILDER));
		sellPrice.put("nether_brick_fence", new SellItem(150, ItemType.BUILDER));
		sellPrice.put("nether_brick_stairs", new SellItem(150, ItemType.BUILDER));
		sellPrice.put("emerald_block", new SellItem(50000, ItemType.BUILDER));
		sellPrice.put("diamond_block", new SellItem(35000, ItemType.BUILDER));
		sellPrice.put("spider_eye", new SellItem(400, ItemType.BAKER));
		sellPrice.put("fermented_spider_eye", new SellItem(550, ItemType.BAKER));
		sellPrice.put("raw_fish", new SellItem(2, ItemType.BAKER));
		sellPrice.put("cooked_fish", new SellItem(70, ItemType.BAKER));
		sellPrice.put("nether_stalk", new SellItem(50, ItemType.MISCELLANEOUS));
		sellPrice.put("quartz", new SellItem(700, ItemType.MISCELLANEOUS));
	}
	
	@SuppressWarnings("deprecation")
	public static void buyEnchantment(Player player, Enchantment enchantment) {
		int money = Money.getMoney(player);
		int price = getEnchantmentPrice(enchantment);
		if(money < price) {
			sendMessage(player, ChatColor.WHITE + "You have insufficient money");
			return;
		}
		ItemStack item = player.getItemInHand();
		if(item == null) { sendMessage(player, ChatColor.WHITE + "You must be holding an item"); return; }
		if(!enchantment.canEnchantItem(item)) { sendMessage(player, ChatColor.WHITE + "You cannot add " + ChatColor.LIGHT_PURPLE + utilItems.toCommon(enchantment.getId()) + ChatColor.WHITE + " to " + ChatColor.YELLOW + utilItems.toCommon(item.getType().toString().toLowerCase())); return; }
		if(item.getEnchantmentLevel(enchantment) >= enchantment.getMaxLevel()) { sendMessage(player, ChatColor.WHITE + "You have reached the max level for " + utilItems.toCommon(enchantment.getId())); return; }
		if(!item.containsEnchantment(enchantment)) {
			Money.subtractMoney(player, price);
			item.addEnchantment(enchantment, 1);
			if(!log.get(player.getName()).enchantments.containsKey(enchantment)) {
				log.get(player.getName()).enchantments.put(enchantment, 1);
				log.get(player.getName()).cost += price;
				return;
			}
			int amount = log.get(player.getName()).enchantments.get(enchantment) + 1;
			log.get(player.getName()).enchantments.put(enchantment, amount);
			log.get(player.getName()).cost += price;
			return;
		}
		Money.subtractMoney(player, price);
		item.addEnchantment(enchantment, item.getEnchantmentLevel(enchantment) + 1);
		if(!log.get(player.getName()).enchantments.containsKey(enchantment)) {
			log.get(player.getName()).enchantments.put(enchantment, 1);
			log.get(player.getName()).cost += price;
			return;
		}
		int amount = log.get(player.getName()).enchantments.get(enchantment) + 1;
		log.get(player.getName()).enchantments.put(enchantment, amount);
		log.get(player.getName()).cost += price;
		
		
	}
	
	@SuppressWarnings("deprecation")
	public static void addEnchantLevel(Player player, Enchantment enchantment) {
		ItemStack item = player.getItemInHand();
		if(item == null) { sendMessage(player, ChatColor.WHITE + "You must be holding an item"); return; }
		if(!enchantment.canEnchantItem(item)) { sendMessage(player, ChatColor.WHITE + "You cannot add " + ChatColor.LIGHT_PURPLE + utilItems.toCommon(enchantment.getId()) + ChatColor.WHITE + " to " + ChatColor.YELLOW + utilItems.toCommon(item.getType().toString().toLowerCase())); return; }
		if(!item.containsEnchantment(enchantment)) { item.addEnchantment(enchantment, 1); return; }
		if(item.getEnchantmentLevel(enchantment) >= enchantment.getMaxLevel()) { sendMessage(player, ChatColor.WHITE + "You have reached the max level for " + utilItems.toCommon(enchantment.getId())); }
		item.addEnchantment(enchantment, item.getEnchantmentLevel(enchantment) + 1);
	}
	
	public static int getEnchantmentPrice(Enchantment enchantment) {
		if(enchantment.getName().equalsIgnoreCase("ARROW_DAMAGE")) return 140000;
		if(enchantment.getName().equalsIgnoreCase("ARROW_FIRE")) return 210000;
		if(enchantment.getName().equalsIgnoreCase("ARROW_INFINITE")) return 140000;
		if(enchantment.getName().equalsIgnoreCase("ARROW_KNOCKBACK")) return 75000;
		if(enchantment.getName().equalsIgnoreCase("DAMAGE_ALL")) return 280000;
		if(enchantment.getName().equalsIgnoreCase("DAMAGE_ARTHROPODS") || enchantment.getName().equalsIgnoreCase("DAMAGE_UNDEAD")) return 25000;
		if(enchantment.getName().equalsIgnoreCase("DIG_SPEED")) return 40000;
		if(enchantment.getName().equalsIgnoreCase("DURABILITY")) return 45000;
		if(enchantment.getName().equalsIgnoreCase("FIRE_ASPECT")) return 170000;
		if(enchantment.getName().equalsIgnoreCase("KNOCKBACK")) return 85000;
		if(enchantment.getName().equalsIgnoreCase("LOOT_BONUS_BLOCKS")) return 110000;
		if(enchantment.getName().equalsIgnoreCase("LOOT_BONUS_MOBS")) return 75000;
		if(enchantment.getName().equalsIgnoreCase("PROTECTION_EXPLOSIONS")) return 30000;
		if(enchantment.getName().equalsIgnoreCase("THORNS")) return 50000;
		return 1000000000;
	}
	
	public enum ShopType {
		BUY,
		SELL;
	}
	
	public enum ItemType {
		WEAPONRY,
		BUILDER,
		ENCHANTER,
		MISCELLANEOUS,
		MINER,
		BAKER;
	}
	
	public class ShopLog {
		
		public HashMap<String, Integer> items = new HashMap<String, Integer>();
		public HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
		public int cost = 0;
		public String player;
		
		public ShopLog(String player, int cost) {
			this.player = player;
			this.cost = cost;
		}
		
		public int getCost() {
			return this.cost;
		}
		
		public String getPlayer() {
			return this.player;
		}
		
		public boolean bought(Material item) { return items.containsKey(item.toString().toLowerCase()); }
		
		public void addSpent(Material item, int amount) {
			if(!bought(item)) {
				items.put(item.toString().toLowerCase(), amount);
				return;
			}
			int cur = items.get(item.toString().toLowerCase());
			items.put(item.toString().toLowerCase(), cur + amount);
		}
		
	}
}


