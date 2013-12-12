package me.loogeh.Hype.donations;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.donations.Morphs.MorphType;
import me.loogeh.Hype.economy.Money;
import me.loogeh.Hype.util.utilInv;
import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DonationCommands implements CommandExecutor {
	public static Hype plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Console cannot use Donation commands");
			return true;
		}
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(commandLabel.equalsIgnoreCase("d") || (commandLabel.equalsIgnoreCase("donations") || (commandLabel.equalsIgnoreCase("donation")))) {
				if(args.length == 0) {
					if(!Donations.donationsMap.containsKey(player.getName())) {
						player.sendMessage(ChatColor.LIGHT_PURPLE + "Donation - " + ChatColor.WHITE + "You have not donated");
					}	
				}
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("seizure") || args[0].equalsIgnoreCase("seiz")) {
						Seizure.toggle(player);
						return true;
					}
				}
			}
			if(commandLabel.equalsIgnoreCase("morph")) {
				if(!Donations.donationsMap.containsKey(player.getName())) {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Donation - " + ChatColor.WHITE + "You have not donated");
					return true;
				}
				if(Morphs.isMorphed(player)) {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.WHITE + "You are already morphed as " + ChatColor.LIGHT_PURPLE + Morphs.getType(player));
					return true;
				}
				if(utilInv.countFilledSlots(player.getInventory()) > 1) {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.WHITE + "You have too many items to morph");
					return true;
				}
				if(args[0].equalsIgnoreCase("pig")) {
					if(Money.getMoney(player) < 1000) {
						player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.WHITE + "You need " + ChatColor.DARK_GREEN + "$1000 " + ChatColor.WHITE + "to morph into a " + ChatColor.LIGHT_PURPLE + "Pig");
						return true;
					}
					if(player.getItemInHand().getType() != Material.PORK) {
						player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.WHITE + "You must be holding " + ChatColor.YELLOW + "Raw Pork " + ChatColor.WHITE + "to morph as a " + ChatColor.LIGHT_PURPLE + "Pig");
						return true;
					}
					Morphs.morph(player, MorphType.PIG);
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.YELLOW + "You morphed as a " + ChatColor.LIGHT_PURPLE + "Pig");
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.YELLOW + "Type /morph " + ChatColor.LIGHT_PURPLE + "to unmorph");
					return true;
				}
				if(args[0].equalsIgnoreCase("squid")) {
					if(player.getItemInHand().getType() != Material.INK_SACK) {
						player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.WHITE + "You must be holding " + ChatColor.YELLOW + "Ink Sack " + ChatColor.WHITE + "to morph as a " + ChatColor.LIGHT_PURPLE + "Pig");
						return true;
					}
					if(!utilPlayer.isInWater(player)) {
						player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.WHITE + "You must be in water to morph as " + ChatColor.LIGHT_PURPLE + "Squid");
						return true;
					}
					Morphs.morph(player, MorphType.SQUID);
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.YELLOW + "You morphed as a " + ChatColor.LIGHT_PURPLE + "Squid");
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.YELLOW + "Type /unmorph " + ChatColor.LIGHT_PURPLE + "to unmorph");
					return true;
				} else {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Morph - " + ChatColor.WHITE + "That Morph doesn't exist");
				}
				
			}
			if(commandLabel.equalsIgnoreCase("unmorph")) {
				if(!Morphs.isMorphed(player)) {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Donation - " + ChatColor.WHITE + "You aren't morphed");
					return true;
				}
				Morphs.unmorph(player);
				player.sendMessage(ChatColor.LIGHT_PURPLE + "Donation - " + ChatColor.WHITE + "You unmorphed");
			}
		}
		return false;
	}
	


}
