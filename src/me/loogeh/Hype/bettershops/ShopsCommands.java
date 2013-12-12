package me.loogeh.Hype.bettershops;

import java.util.ArrayList;

import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopsCommands implements CommandExecutor {

	public static ArrayList<String> debug = new ArrayList<String>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_GREEN + "Shops - " + ChatColor.WHITE + "Shops commands can only be used via player");
		}
		Player player = (Player) sender;
		
		if(commandLabel.equalsIgnoreCase("shops")) {
			if(!Permissions.isAdmin(player)) {
				utilPlayer.permMsg(player);
				return true;
			}
			if(args.length == 0) {
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("spawn")) {
					BetterShops.spawnEntities();
					BetterShops.sendMessage(player, ChatColor.WHITE + "You spawned the shops");
					return true;
				}
				if(args[0].equalsIgnoreCase("debug")) {
					if(debug.contains(player.getName())) {
						debug.remove(player.getName());
						BetterShops.sendMessage(player, ChatColor.YELLOW + "Debug Mode " + ChatColor.WHITE + "disabled");
						return true;
					}
					debug.add(player.getName());
					BetterShops.sendMessage(player, ChatColor.YELLOW + "Debug Mode " + ChatColor.WHITE + "enabled");
				}
			}
		}
		return false;
	}
	
	

}
