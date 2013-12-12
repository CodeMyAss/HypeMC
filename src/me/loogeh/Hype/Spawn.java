package me.loogeh.Hype;

import java.util.List;

import me.loogeh.Hype.armour.Armour.Kit;
import me.loogeh.Hype.entity.NPC;
import me.loogeh.Hype.util.ItemLore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Spawn implements CommandExecutor {
	//private static Hype plugin = Hype.plugin;
	
	private static NPC diamond;
	private static NPC iron;
	private static NPC chain;
	private static NPC gold;
	private static NPC leather;
	private static NPC squads;
	private static NPC arena;
	private static NPC games;
	private static NPC donate;
	private static NPC npc_owner_brodie;
	private static NPC npc_owner_loogeh;
	private static NPC notch;
	private static NPC jeb_;
	private static Location armour_diamond = new Location(Bukkit.getWorld("world"), 880.5, 71.0, 424.5, 0.0F, 0.0F);
	private static Location armour_iron = new Location(Bukkit.getWorld("world"), 882.5, 71.0, 424.5, 0.0F, 0.0F);
	private static Location armour_chain = new Location(Bukkit.getWorld("world"), 884.5, 71.0, 424.5, 0.0F, 0.0F);
	private static Location armour_gold = new Location(Bukkit.getWorld("world"), 886.5, 71.0, 424.5, 0.0F, 0.0F);
	private static Location armour_leather = new Location(Bukkit.getWorld("world"), 888.5, 71.0, 424.5, 0.0F, 0.0F);
	private static Location info_squads = new Location(Bukkit.getWorld("world"), 875.5, 67.0, 428.5, 0.0F, 0.0F);
	private static Location info_games = new Location(Bukkit.getWorld("world"), 902.5, 61.0, 450.5, 90.0F, 0.0F);
	private static Location info_arena = new Location(Bukkit.getWorld("world"), 871.5, 67.0, 451.5, -90.0F, 0.0F);
	private static Location info_donate = new Location(Bukkit.getWorld("world"), 898.5, 67.0, 429.5, 0.0F, 0.0F);
	private static Location owner_brodie = new Location(Bukkit.getWorld("world"), 880.5, 73.0, 457.5, -180.0F, 0.0F);
	private static Location owner_loogeh = new Location(Bukkit.getWorld("world"), 888.5, 73.0, 457.5, -180.0F, 0.0F);
	private static Location notch_lurker = new Location(Bukkit.getWorld("world"), 889.95, 84.0, 436.0, 40.0F, 65.74F);
	private static String[] book_info = {"Welcome to HypeMC! This book is about what this server is, why it’s better than other servers and all of its main features. First of all, you are in our spawn. Go to the armours and right click one to view its skills!",
	"Please visit our forums at some point. In the forums there are many threads about the server and one very large thread called ‘The Ultimate guide to Squads’. In that thread you will learn about every feature of Squads in detail.",
			"Have you ever played a factions server before? Well, whether you have or haven’t, Squads is our custom version of Factions. Having our own version of Factions allows us to implement it with other features such as arena, games, and shops with ease.",
			"The basic overview of Squads is that there is a team and people can be invited to join this team. If they join, they gain the ability to open chests, use crafting tables, furnaces, levers and all other interactable items within the game.",
			"The best feature to Squads is claiming. Your squad has an amount of chunks which they can claim to be their own. If the chunk is owned by your squad, you and your squad can edit that chunk and you are the only people who can use interactable items",
			"and inventory based items such as chests in that chunk.",
			"As you will have noticed if you had clicked on the armours in spawn, the armours all have abilities/skills. The abilities vary from armour to armour and some types armour have a theme such as Archer, which has arrow types.",
			"You will have to experiment with each type to find the one which you play the best in. One important thing to note is that while Diamond Armour is the toughest, it’s not necessarily the best.",
			"The armours aren’t finished yet because Loogeh is trying to come up with the best abilities possible instead of just making simple, boring ones. If you have any suggestions please post them on the forums.",
			"One thing which the server’s coder (Loogeh) is working on right now is pvp arenas and other games. The games will be things such as Kit Arena, Capture the Flag, Spleef, Splegg and many other of your favourite games.",
			"Loogeh strives to make HypeMC as far from the other servers as possible to make it as fun and unique as it can possibly be and will always take into consideration suggestions which are posted on the forums.",
			"I hope this book has given you a bit of an insight into what our server is about and how it is supposed to be played. You can read much more on the forums so please go there to learn more. hypemc.enjin.com"};
	
	public static void spawnEntity(InfoCentre entity) {
		if(entity == InfoCentre.ALL) {
			diamond = InfoHolder.npcmanager.spawnHumanNPC("Juggernaut", armour_diamond);
			iron = InfoHolder.npcmanager.spawnHumanNPC("Samurai", armour_iron);
			chain = InfoHolder.npcmanager.spawnHumanNPC("Specialist", armour_chain);
			gold = InfoHolder.npcmanager.spawnHumanNPC("Agility", armour_gold);
			leather = InfoHolder.npcmanager.spawnHumanNPC("Archer", armour_leather);
			squads = InfoHolder.npcmanager.spawnHumanNPC("§cSquads Info", info_squads);
			arena = InfoHolder.npcmanager.spawnHumanNPC("§cArena Info", info_arena);
			games = InfoHolder.npcmanager.spawnHumanNPC("§cGames Info", info_games);
			donate = InfoHolder.npcmanager.spawnHumanNPC("§cDonate Info", info_donate);
			npc_owner_brodie = InfoHolder.npcmanager.spawnHumanNPC("§4_BrodieC", owner_brodie);
			npc_owner_loogeh = InfoHolder.npcmanager.spawnHumanNPC("§4Loogeh", owner_loogeh);
			notch = InfoHolder.npcmanager.spawnHumanNPC("§aNotch", notch_lurker);
			jeb_ = InfoHolder.npcmanager.spawnHumanNPC("§ajeb_", notch_lurker);
			notch.getCraftPlayer().setPassenger(jeb_.getBukkitEntity());
			diamond.setKit(Kit.JUGGERNAUT);
			iron.setKit(Kit.SAMURAI);
			chain.setKit(Kit.SPECIALIST);
			gold.setKit(Kit.AGILITY);
			leather.setKit(Kit.ARCHER);
			diamond.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			iron.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			chain.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			gold.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			leather.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			squads.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 0));
			arena.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 0));
			games.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 0));
			donate.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 0));
			npc_owner_brodie.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 0));
			npc_owner_loogeh.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 0));
			notch.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 0));
		}
	}
	
	public static void addLoreItem(Inventory inv, ItemStack item, String name, List<String> lore, int slot) {
		MetaItem mitem = new MetaItem(name, item, lore);
		inv.setItem(slot, mitem.getItem());
	}
	
	public static void addNameItem(Inventory inv, ItemStack item, String name, int slot) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		inv.setItem(slot, item);
	}
	
	public static void openInventory(Player player, InfoCentre type) {
		Inventory inv = null;
		if(type == InfoCentre.ARMOUR_DIAMOND) {
			inv = Bukkit.createInventory(null, 54, ChatColor.LIGHT_PURPLE + "Juggernaut Abilities");
//			addNameItem(inv, new ItemStack(Material.IRON_SWORD), ChatColor.LIGHT_PURPLE + "Sword Ability", 0);
//			addLoreItem(inv, new ItemStack(Material.ANVIL), ChatColor.AQUA + "Stomp", ItemLore.D_ABILITY_SWORD, 1);
//			addNameItem(inv, new ItemStack(Material.IRON_AXE), ChatColor.LIGHT_PURPLE + "Axe Ability", 9);
//			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "N/A", ItemLore.U_BROKEN_ANVIL_ABILITY, 10);
////			addNameItem(inv, new ItemStack(Material.BOW), ChatColor.LIGHT_PURPLE + "Bow Ability", 18);
////			addLoreItem(inv, new ItemStack(Material.ARROW), ChatColor.AQUA + "Critical Arrow", ItemLore.D_ABILITY_CRITICAL, 19);
//			addNameItem(inv, new ItemStack(Material.FISHING_ROD), ChatColor.LIGHT_PURPLE + "Rod Ability", 27);
//			addLoreItem(inv, new ItemStack(Material.ANVIL), ChatColor.AQUA + "Drag", ItemLore.D_ABILITY_ROD, 28);
//			addNameItem(inv, new ItemStack(Material.IRON_INGOT), ChatColor.LIGHT_PURPLE + "Passive I", 36);
//			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "N/A", ItemLore.U_BROKEN_ANVIL_ABILITY, 37);
//			addNameItem(inv, new ItemStack(Material.GOLD_INGOT), ChatColor.LIGHT_PURPLE + "Passive II", 45);
//			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "N/A", ItemLore.U_BROKEN_ANVIL_ABILITY, 46);
			addNameItem(inv, new ItemStack(Material.BOOK), ChatColor.GREEN + "Currently disabled - Fixing Issues", 22);
		}
		if(type == InfoCentre.ARMOUR_IRON) {
			inv = Bukkit.createInventory(null, 54, ChatColor.LIGHT_PURPLE + "Samurai Abilities");
			addNameItem(inv, new ItemStack(Material.IRON_SWORD), ChatColor.LIGHT_PURPLE + "Sword Ability", 0);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "Shockwave", ItemLore.I_ABILITY_SWORD, 1);
			addNameItem(inv, new ItemStack(Material.IRON_AXE), ChatColor.LIGHT_PURPLE+ "Axe Ability", 9);
//			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "Fade", ItemLore.I_ABILITY_AXE, 10);
			addNameItem(inv, new ItemStack(Material.IRON_INGOT), ChatColor.LIGHT_PURPLE + "Passive I", 36);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "N/A", ItemLore.U_BROKEN_ANVIL_ABILITY, 37);
			addNameItem(inv, new ItemStack(Material.GOLD_INGOT), ChatColor.LIGHT_PURPLE + "Passive II", 45);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "N/A", ItemLore.U_BROKEN_ANVIL_ABILITY, 46);
		}
		if(type == InfoCentre.ARMOUR_CHAIN) {
			inv = Bukkit.createInventory(null, 54, ChatColor.LIGHT_PURPLE + "Specialist Abilities");
			addNameItem(inv, new ItemStack(Material.IRON_SWORD), ChatColor.LIGHT_PURPLE + "Sword Ability", 0);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "N/A", ItemLore.U_BROKEN_ANVIL_ABILITY, 1);
			addNameItem(inv, new ItemStack(Material.IRON_AXE), ChatColor.LIGHT_PURPLE + "Axe Ability", 9);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "Beserker", ItemLore.I_ABILITY_AXE, 10);
			addNameItem(inv, new ItemStack(Material.IRON_INGOT), ChatColor.LIGHT_PURPLE + "Passive I", 36);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "Breathing", ItemLore.C_PASSIVE_I, 37);
			addNameItem(inv, new ItemStack(Material.GOLD_INGOT), ChatColor.LIGHT_PURPLE + "Passive II", 45);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "N/A", ItemLore.U_BROKEN_ANVIL_ABILITY, 46);
		}
		if(type == InfoCentre.ARMOUR_GOLD) {
			inv = Bukkit.createInventory(null, 54, ChatColor.LIGHT_PURPLE + "Agility Abilities");
			addNameItem(inv, new ItemStack(Material.IRON_SWORD), ChatColor.LIGHT_PURPLE + "Sword Ability", 0);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "Dodge", ItemLore.G_ABILITY_SWORD, 1);
			addNameItem(inv, new ItemStack(Material.IRON_AXE), ChatColor.LIGHT_PURPLE + "Axe Ability", 9);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "Rush", ItemLore.G_ABILITY_AXE, 10);
			addNameItem(inv, new ItemStack(Material.IRON_INGOT), ChatColor.LIGHT_PURPLE + "Passive I", 36);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "Gold Proficiency", ItemLore.G_PASSIVE_I, 37);
			addNameItem(inv, new ItemStack(Material.GOLD_INGOT), ChatColor.LIGHT_PURPLE + "Passive II", 45);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "Anti Inferno", ItemLore.G_PASSIVE_II, 46);
		}
		if(type == InfoCentre.ARMOUR_LEATHER) {
			inv = Bukkit.createInventory(null, 54, ChatColor.LIGHT_PURPLE + "Archer Abilities");
			addNameItem(inv, new ItemStack(Material.IRON_SWORD), ChatColor.LIGHT_PURPLE + "Sword Ability", 0);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "N/A", ItemLore.U_BROKEN_ANVIL_ABILITY, 1);
			addNameItem(inv, new ItemStack(Material.IRON_AXE), ChatColor.LIGHT_PURPLE + "Axe Ability", 9);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "Leap", ItemLore.L_ABILITY_AXE, 10);
			addNameItem(inv, new ItemStack(Material.BOW), ChatColor.LIGHT_PURPLE + "Bow Ability", 18);
			addLoreItem(inv, new ItemStack(Material.ARROW), ChatColor.AQUA + "Repulse Arrow", ItemLore.L_ABILITY_REPULSE, 19);
			addLoreItem(inv, new ItemStack(Material.ARROW), ChatColor.AQUA + "Poison Arrow", ItemLore.L_ABILITY_POISON, 20);
			addLoreItem(inv, new ItemStack(Material.ARROW), ChatColor.AQUA + "Paralyzing Arrow", ItemLore.L_ABILITY_PARALYZE, 21);
			addNameItem(inv, new ItemStack(Material.IRON_INGOT), ChatColor.LIGHT_PURPLE + "Passive I", 36);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "Broken Fall", ItemLore.L_PASSIVE_I, 37);
			addNameItem(inv, new ItemStack(Material.GOLD_INGOT), ChatColor.LIGHT_PURPLE + "Passive II", 45);
			addLoreItem(inv, new ItemStack(Material.ANVIL, 1, (short) 3), ChatColor.AQUA + "N/A", ItemLore.U_BROKEN_ANVIL_ABILITY, 46);
		}
		if(type == InfoCentre.ALL) {
			inv = Bukkit.createInventory(null, 54, ChatColor.LIGHT_PURPLE + "Info");
			addLoreItem(inv, new ItemStack(Material.ENCHANTED_BOOK), ChatColor.LIGHT_PURPLE + "Info", ItemLore.U_SPAWN_WIP, 22);
			addLoreItem(inv, new ItemStack(Material.ENCHANTED_BOOK), ChatColor.LIGHT_PURPLE + "Info", ItemLore.U_SPAWN_WIP, 31);
		}
		player.openInventory(inv);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.GREEN + "Console can't use spawn commands");
			return true;
		}
		Player player = (Player) sender;
		if(commandLabel.equalsIgnoreCase("spawn")) {
			if(args.length == 0) {
				//help
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("npcs")) {
					spawnEntity(InfoCentre.ALL);
					M.v(player, "Spawn", ChatColor.WHITE + "You spawned all " + ChatColor.YELLOW + "NPCs");
					return true;
				}
			}
			
		}
		return false;
	}
	
	public static ItemStack getBook() {
		ItemStack book = new ItemStack(Material.BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Server Overview");
		meta.setTitle("Welcome!");
		meta.setAuthor("Hype");
		meta.setPages("Intro", "Forums", "Squads", "Squads", "Squads", "Squads", "Armours", "Armours", "Armours", "Games", "Games", "End");
		for(int i = 0; i < book_info.length; i++) {
			meta.setPage(i, book_info[i]);
		}
		book.setItemMeta(meta);
		return book;
	}
	
	public enum InfoCentre {
		ALL,
		ARMOUR_DIAMOND,
		ARMOUR_IRON,
		ARMOUR_GOLD,
		ARMOUR_CHAIN,
		ARMOUR_LEATHER,
		INFO_ARENA,
		INFO_GAMES,
		INFO_SQUADS,
		INFO_DONATIONS,
		OWNER_LOOGEH,
		OWNER_BRODIE;
	}
}
