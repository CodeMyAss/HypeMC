package me.loogeh.Hype.moderation;

import java.util.HashMap;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.util.Utilities;
import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModerationCommands implements CommandExecutor {
	public static Hype plugin;
	public static HashMap<String, String> banTrueList = new HashMap<String, String>();
	public static HashMap<String, Double> banDurList = new HashMap<String, Double>();
	public static HashMap<String, String> banReasonList = new HashMap<String, String>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You cannot use server moderation commands from the console");
			return true;
		}
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(commandLabel.equalsIgnoreCase("mute")) {
				if(!Permissions.isStaff(player)) {
					utilPlayer.permMsg(player);
					return true;
				} else {
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "Mute: " + ChatColor.GRAY + "/mute <player> <duration> <reason>");
					return true;
				} else {
					double dur = Double.parseDouble(args[1]);
					String reason = Utilities.argsToString3(args);
					String reasonFormat = reason.substring(0, 1).toUpperCase() + reason.substring(1);
					Player mutee = Bukkit.getServer().getPlayer(args[0]);
					if(args[2] == null) {
						player.sendMessage(ChatColor.GOLD + "Mute: " + ChatColor.GRAY + "/mute <player> <duration> <reason>");
						return true;
					}
					if(Permissions.getRank(player) == Permissions.Ranks.MODERATOR) {
						if(dur > 24) {
							player.sendMessage(ChatColor.GREEN + "Moderators " + ChatColor.GRAY + "can mute for a maximum of " + ChatColor.GREEN + "24 Hours");
							return true;
						}
					}
					Mute.mute(mutee.getName(), player, dur, reasonFormat);
					return true;
					}
				}
			}
			if(commandLabel.equalsIgnoreCase("unmute")) {
				if(!Permissions.isStaff(player)) {
					utilPlayer.permMsg(player);
					return true;
				}
				if(args.length != 1) {
					player.sendMessage(ChatColor.GOLD + "Mute: " + ChatColor.GRAY + "/unmute <player>");
					return true;
				}
				Player mutee = Bukkit.getServer().getPlayer(args[0]);
				if(mutee == null) {
					player.sendMessage(ChatColor.GOLD + "Mute: " + ChatColor.GRAY + "That player is offline");
				}
				Mute.unmute(mutee, player);
				return true;
				
			}
			if(commandLabel.equalsIgnoreCase("ban")) {
				if(!Permissions.isStaff(player)) {
					utilPlayer.permMsg(player);
					return true;
				}
				if(args[0].equalsIgnoreCase("yes")) {
					if(!banTrueList.containsKey(player.getName())) {
						player.sendMessage(ChatColor.GRAY + "There is nobody to ban");
						return true;
					}
					Ban.banOfflinePlayer(banTrueList.get(player.getName()), player, banDurList.get(player.getName()), banReasonList.get(player.getName()));
					banTrueList.remove(player.getName());
					banDurList.remove(player.getName());
					banReasonList.remove(player.getName());
					return true;
				}
				if(args[0].equalsIgnoreCase("no")) {
					if(!banTrueList.containsKey(player.getName())) {
						player.sendMessage(ChatColor.GRAY + "There is nobody to ban");
						return true;
					}
					player.sendMessage(ChatColor.GRAY + "You chose not to ban " + ChatColor.DARK_AQUA + banDurList.get(player.getName()));
					banTrueList.remove(player.getName());
					banDurList.remove(player.getName());
					banReasonList.remove(player.getName());
					return true;
				}
				if(args.length < 2) {
					player.sendMessage(ChatColor.GOLD + "Ban: " + ChatColor.GRAY + "/ban <player> <duration> <reason>");
					return true;
				}
				if(args[2] == null) {
					player.sendMessage(ChatColor.GOLD + "Ban: " + ChatColor.GRAY + "/ban <player> <duration> <reason>");
					return true;
				}
				Player banee = Bukkit.getServer().getPlayer(args[0]);
				double dur = Double.parseDouble(args[1]);
				String reason = Utilities.argsToString3(args);
				String reasonFormat = Utilities.capitalizeFirst(reason);
				if(Permissions.getRank(player) == Permissions.Ranks.MODERATOR) {
					if(dur > 8) {
						player.sendMessage(ChatColor.GREEN + "Moderators " + ChatColor.GRAY + "can ban for a maximum of " + ChatColor.GREEN + "8 Hours");
						return true;
					}
				}
				if(banee == null) {
					banTrueList.put(player.getName(), args[0]);
					banDurList.put(player.getName(), dur);
					banReasonList.put(player.getName(), reasonFormat);
					player.sendMessage(ChatColor.DARK_AQUA + "Player offline. " + ChatColor.GRAY + "Type " + ChatColor.YELLOW + "/ban yes " + ChatColor.GRAY + "if you want to ban " + ChatColor.WHITE + "[" + ChatColor.AQUA + args[0] + ChatColor.WHITE + "]");
					return true;
				}
				Ban.banPlayer(banee.getName(), player, dur, reasonFormat);
				return true;
				
			}
			if(commandLabel.equalsIgnoreCase("unban")) {
				if(!Permissions.isAdmin(player)) {
					utilPlayer.permMsg(player);
					return true;
				}
				if(args.length != 1) {
					player.sendMessage(ChatColor.GOLD + "Ban: " + ChatColor.GRAY + "/unban <player>");
					return true;
				}
				String unbanee = args[0];
				Player pUnbanee = Bukkit.getServer().getPlayer(unbanee);
				if(pUnbanee != null) {
					Ban.unban(pUnbanee.getName(), player);
					return true;
				}
				Ban.unban(unbanee, player);
			}
		}
		return false;
	}

}
