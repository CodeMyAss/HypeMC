package me.loogeh.Hype;

import java.util.ArrayList;
import java.util.HashMap;

import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.util.Utilities;
import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message implements CommandExecutor {
	public static Hype plugin;
	public static HashMap<String, String> replies = new HashMap<String, String>();
	public static ArrayList<String> onlineStaff = new ArrayList<String>();

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players may use this command");
		}
		Player p = (Player) sender;
		if(commandLabel.equalsIgnoreCase("m") || commandLabel.equalsIgnoreCase("msg") || commandLabel.equalsIgnoreCase("message") || commandLabel.equalsIgnoreCase("tell") || commandLabel.equalsIgnoreCase("whisper")) {
			if (sender instanceof Player) {
				if(args.length == 0){
					p.sendMessage(ChatColor.RED + "/m" + ChatColor.LIGHT_PURPLE + " <player>" + ChatColor.GRAY + "<message>");
					p.sendMessage(ChatColor.RED + "/r" + ChatColor.LIGHT_PURPLE + " <message>" + ChatColor.GRAY + " - Reply to last player messaged");
					p.sendMessage(ChatColor.RED + "/a" + ChatColor.LIGHT_PURPLE + " <message>" + ChatColor.GRAY + " - Message all online staff");
					return true;
				}
				if(args.length == 1) {
					p.sendMessage(ChatColor.RED + "/m" + ChatColor.LIGHT_PURPLE + " <player>" + ChatColor.GRAY + "<message>");
					p.sendMessage(ChatColor.RED + "/r" + ChatColor.LIGHT_PURPLE + " <message>" + ChatColor.GRAY + " - Reply to last player messaged");
					p.sendMessage(ChatColor.RED + "/a" + ChatColor.LIGHT_PURPLE + " <message>" + ChatColor.GRAY + " - Message all online staff");
					return true;
				}
				if(args.length > 1) {
					Player player = Bukkit.getPlayer(args[0]);
					if(player == null) {
						M.v(p, "Server", ChatColor.WHITE + "Invalid player");
						return true;
					}
					String message = Utilities.join(1, args);
					player.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + sender.getName() + ChatColor.GREEN + " > " + ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + player.getName() + " " + ChatColor.WHITE + ChatColor.BOLD.toString() + message);
					sender.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + sender.getName() + ChatColor.GREEN + " > " + ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + player.getName() + " " + ChatColor.WHITE + ChatColor.BOLD.toString() + message);
					replies.put(sender.getName(), player.getName());
					for(String spys : RCommands.spyMode) {
						Player spy = Bukkit.getPlayer(spys);
						if(spy != null) spy.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + sender.getName() + ChatColor.GREEN + " > " + ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + player.getName() + " " + ChatColor.WHITE + ChatColor.BOLD.toString() + message);
					}
					
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("r")) {
			if(!replies.containsKey(sender.getName())) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Message - " + ChatColor.WHITE + "You have no one to reply to");
				return true;
			}
			String message = Utilities.argsToString(args);
			Player reciever = Bukkit.getPlayerExact(replies.get(sender.getName()));
			if(reciever == null) {
				sender.sendMessage(ChatColor.GREEN + "Message - " + ChatColor.WHITE + "You have no one to reply to");
				replies.remove(sender.getName());
				return true;
			}
			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "/r <message>");
				return false;
			}
			if(!replies.containsValue(reciever.getName())) {
				sender.sendMessage(ChatColor.RED + "That player is offline");
				return true;
			}

			reciever.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + sender.getName() + ChatColor.GREEN + " > " + ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + reciever.getName() + " " + ChatColor.WHITE + ChatColor.BOLD.toString() + message);
			sender.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + sender.getName() + ChatColor.GREEN + " > " + ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + reciever.getName() + " " + ChatColor.WHITE + ChatColor.BOLD.toString() + message);
			for(String spys : RCommands.spyMode) {
				Player spy = Bukkit.getPlayer(spys);
				spy.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + sender.getName() + ChatColor.GREEN + " > " + ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + reciever.getName() + " " + ChatColor.WHITE + ChatColor.BOLD.toString() + message);
			}
			return true;
		}
		if(commandLabel.equalsIgnoreCase("a")) {
			if(args.length == 0) {
				M.v(p, "Command", ChatColor.WHITE + "/a <message>");
				return true;
			}
			if(args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("reply")) {
				if(!Permissions.isStaff(p)) {
					utilPlayer.permMsg(p);
					return true;
				}
				if(args.length < 2) {
					M.v(p, "Command", ChatColor.WHITE + "/a r <player> <message>");
					return true;
				}
				Player target = Bukkit.getPlayer(args[2]);
				if(target == null) {
					M.v(p, "Server", ChatColor.WHITE + "Invalid player");
					return true;
				}
				String msg = Utilities.argsToString2(args);
				target.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + p.getName() + ChatColor.WHITE + ChatColor.BOLD.toString() + " > " + ChatColor.AQUA + ChatColor.BOLD.toString() + target.getName() + ChatColor.WHITE + " " + msg);
				for(String staffs : onlineStaff) {
					Player staff = Bukkit.getPlayer(staffs);
					if(staff != null) staff.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + p.getName() + ChatColor.WHITE + ChatColor.BOLD.toString() + " > " + ChatColor.AQUA + ChatColor.BOLD.toString() + target.getName() + ChatColor.WHITE + " " + msg);
				}
				return true;
			}
			if(onlineStaff.size() == 0) {
				p.sendMessage(ChatColor.RED + "Server - " + ChatColor.WHITE + "There are no staff online");
				return true;
			}
			String msg = Utilities.argsToString(args);
			adminMsg(p, msg);
		}
		return false;
	}

	public static void adminMsg(Player sender, String message) {
		for(String p : onlineStaff) {
			Player staff = Bukkit.getPlayerExact(p);
			if(staff != null) {
				staff.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + sender.getName() + ChatColor.WHITE + ChatColor.BOLD.toString() + " > " + ChatColor.AQUA + ChatColor.BOLD.toString() + "Admin " + ChatColor.WHITE + message);
			}
		}
		if(!Permissions.isStaff(sender)) {
			sender.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + sender.getName() + ChatColor.WHITE + ChatColor.BOLD.toString() + " > " + ChatColor.AQUA + ChatColor.BOLD.toString() + "Admin " + ChatColor.WHITE + message);
		}
	}
	
	public static void removeReplies(String player) {
		replies.remove(player);
		for(String keys : replies.keySet()) if(replies.get(keys).equalsIgnoreCase(player)) replies.remove(keys);
	}
}