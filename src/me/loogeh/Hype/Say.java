package me.loogeh.Hype;

import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.util.Utilities;
import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Say implements CommandExecutor {
	public static Hype plugin;
	
	public static String[] banned_words = {"swag", "sw4g", "sw3g", "nigga", "nigger", "faggot", "niga", "yolo", "y0l0", "yol0", "y0lo"};

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("say")) {
			String msg = Utilities.argsToString(args);
			if(!(sender instanceof Player)) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.RED + "/say <message>");
					} else if(args.length > 0) {
						if(msg.contains("8World saved.")) msg = "Worlds saved";
						Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "HypeMC: " + ChatColor.GREEN + "Console" + ChatColor.WHITE + " - " + ChatColor.GREEN + msg);
					}
			} else if(sender instanceof Player) {
				Player player = (Player) sender;
				if(!Permissions.isStaff(player)) {
					utilPlayer.permMsg(player);
					return true;
				}
				if(args.length == 0) {
					player.sendMessage(ChatColor.RED + "/say <message>");
					return true;
				}
				if(args.length > 0) {
					Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "HypeMC: " + ChatColor.GREEN + player.getName() + ChatColor.WHITE + " -" + ChatColor.GREEN + msg);
					return true;
				}
			}
		}
		return false;
	}
}
