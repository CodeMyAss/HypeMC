package me.loogeh.Hype.games;

import me.loogeh.Hype.Hype;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KACommands implements CommandExecutor {
	public static Hype plugin;

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.BLUE + "Kit Arena - " + ChatColor.YELLOW + "Console " + ChatColor.WHITE + "cannot use " + ChatColor.YELLOW + "Kit Arena " + ChatColor.WHITE + "commands");
			return true;
		}
		Player player = (Player) sender;
//		if(!Permissions.isStaff(player)) {
//			M.v(player, "Kit Arena", ChatColor.YELLOW + "Kit Arena " + ChatColor.WHITE + "is currently in beta, only staff allowed");
//			return true;
//		}
		if(commandLabel.equalsIgnoreCase("ka")) {
			if(args.length == 0) {
				KitArena.sendHelp(player);
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("join")) {
					KitArena.join(player);
					return true;
				}
				if(args[0].equalsIgnoreCase("start")) {
					GameManager.getKA().initiate();
				}
				if(args[0].equalsIgnoreCase("info")) {
					KitArena.sendInfo(player);
				}
			}
		}
		return false;
	}
}
