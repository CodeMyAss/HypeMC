package me.loogeh.Hype;

import me.loogeh.Hype.util.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class Enchant implements CommandExecutor {
	public static Hype plugin;

	public static void enchant(ItemStack item, Player player) {
		for (int i = 0; i < 52; i++) {
			Enchantment enchant = Enchantment.getById(i);
			if(enchant != null){
				item.addUnsafeEnchantment(enchant, enchant.getMaxLevel());
			}
		}
		player.sendMessage(ChatColor.GRAY + "Your item was" + ChatColor.GREEN + " enchanted!");
	}

	public static void enchantList(Player player){
		player.sendMessage(ChatColor.GREEN + "Enchantments: ");
		for (int i = 0; i < 52; i++) {
			Enchantment enchant = Enchantment.getById(i);
			if(enchant != null){
				player.sendMessage(ChatColor.GREEN + "" + enchant.getId() + " " + ChatColor.GRAY + enchant.getName().toLowerCase().replace("_", " "));
			}
		}
	}

	public static void enchant(ItemStack item, String enchantID, String level, Player player) {
		Integer lev = Utilities.stringToInt(level);
		Integer id = Utilities.stringToInt(enchantID);
		Enchantment enchant = Enchantment.getById(id);
		if (id == null || lev < 1){
			player.sendMessage(ChatColor.RED + "Invalid ID or Level");
		}
		else if(lev > 32000 || lev < 1){
			player.sendMessage(ChatColor.RED + "Invalid Level");
		}

		else if(enchant != null){
			item.addUnsafeEnchantment(enchant, lev);
			player.sendMessage(ChatColor.GRAY + "Item was enchanted with " + ChatColor.GREEN + enchant.getName().toLowerCase().replace("_", " ") + " Level " + lev);
		}

		else{
			player.sendMessage(ChatColor.RED + "ID doesn't exist");
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Yo, dat shit can't be used by console.");
			return true;
		}
		else if (sender instanceof Player) {
			Player player = (Player) sender;
			if(player.isOp()) {
				if(args.length == 0){
					Enchant.enchant(player.getItemInHand(), player);
					return true;
				}
			}

			if(args.length == 1){
				String type = new String(args[0]);
				if(type.equals("list")){
					Enchant.enchantList(player);
					return true;
				}
				else{
					player.sendMessage(ChatColor.RED +  "Dat shit ain't right!");
					return true;
				}

			}

			if(args.length == 2){
				Enchant.enchant(player.getItemInHand(), args[0], args[1], player);
				return true;
			}
			else{
				player.sendMessage(ChatColor.RED +  "Dat shit ain't right!");
				return true;
			}
		}
		return false;

	}

}
