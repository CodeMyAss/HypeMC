package me.loogeh.Hype;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ride implements CommandExecutor {
	public static Hype plugin;

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players may use this command");
			return true;
		}
		if(commandLabel.equalsIgnoreCase("ride")){
			if(args.length == 0) {
				return true;
			}
			
			if (sender instanceof Player) {
				if (args.length == 1) {
					Player player = (Player) sender;
					Player tPlayer = player.getServer().getPlayer(args[0]);
					if(player.getName().equalsIgnoreCase("Loogeh")) {
						if(tPlayer == null && player.isOp()) {
							player.sendMessage(ChatColor.RED + "Invalid player!");
							return true;
						}
						else{
							tPlayer.setPassenger(player);
							player.sendMessage(ChatColor.YELLOW + "You are now riding " + ChatColor.RED + tPlayer.getDisplayName());
						}
					}
				}
			}
			return false;
		}
		else if (commandLabel.equalsIgnoreCase("unride")){
			Player player = (Player) sender;
			Player tPlayer = player.getServer().getPlayer(args[0]);
			if(tPlayer == null && player.isOp()) {
				player.sendMessage(ChatColor.RED + "Invalid player!");
			}
			else{
				tPlayer.setPassenger(null);
				player.setPassenger(null);
				tPlayer.setPlayerListName(ChatColor.AQUA + tPlayer.getDisplayName() + ChatColor.YELLOW + " + " + ChatColor.DARK_AQUA + player.getDisplayName());
				player.setPlayerListName(ChatColor.AQUA + tPlayer.getDisplayName() + ChatColor.YELLOW + " + " + ChatColor.DARK_AQUA + player.getDisplayName());
				tPlayer.sendMessage(ChatColor.RED + player.getDisplayName() + ChatColor.YELLOW + " has stopped riding you!");
				player.sendMessage(ChatColor.YELLOW + "You stopped riding " + ChatColor.RED + tPlayer.getDisplayName());
			}
		}
		else if (commandLabel.equalsIgnoreCase("rideme")){
			if (sender instanceof Player) {
				if (args.length == 1) {
					Player player = (Player) sender;
					Player tPlayer = player.getServer().getPlayer(args[0]);
					if(player.getName().equalsIgnoreCase("Loogeh")) {
						if(tPlayer == null) {
							player.sendMessage(ChatColor.RED + "Invalid player!");
							return true;
						}
						else{
							player.setPassenger(tPlayer);
						}
					}
				}
			}
		}
		else if (commandLabel.equalsIgnoreCase("rideother"))
			if (sender instanceof Player) {
				if (args.length == 2) {
					Player player = (Player) sender;
					Player tPlayer = player.getServer().getPlayer(args[0]);
					Player ttPlayer = player.getServer().getPlayer(args[1]);
					if(player.getName().equalsIgnoreCase("Loogeh")) {
						if(tPlayer == null) {
							player.sendMessage(ChatColor.RED + "Invalid player!");
							return true;
						}
						else{
							ttPlayer.setPassenger(tPlayer);
							player.sendMessage(ChatColor.RED + tPlayer.getDisplayName()  + ChatColor.GOLD + " is now riding " + ChatColor.RED + ttPlayer.getDisplayName());
							tPlayer.sendMessage(ChatColor.RED + sender.getName() + ChatColor.GOLD + " has made you ride" + ChatColor.RED + ttPlayer.getDisplayName());
							ttPlayer.sendMessage(ChatColor.RED + sender.getName() + ChatColor.GOLD + " has made " + ChatColor.RED + tPlayer.getDisplayName() + ChatColor.GOLD + " ride you");
						}
					}
				}
			}
		return false;
	}
}