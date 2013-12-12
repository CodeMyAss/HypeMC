package me.loogeh.Hype.games;

import me.loogeh.Hype.Hype;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommands implements CommandExecutor {
	public static Hype plugin = Hype.plugin;
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Console cannot use Games commands");
			return true;
		}
		Player player = (Player) sender;
		if(commandLabel.equalsIgnoreCase("game")) {
		}
		if(commandLabel.equalsIgnoreCase("splegg")) {
			if(args.length == 0) {
				
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("join")) {
					Splegg.join(player);
				}
				if(args[0].equalsIgnoreCase("leave")) {
					Splegg.leave(player);
				}
				if(args[0].equalsIgnoreCase("cur")) {
					if(Games.getCurrentGame(player) == null) {
						player.sendMessage("None");
						return true;
					}
					player.sendMessage(Games.getCurrentGame(player).getType().toString());
					return true;
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("spleef")) {
			
		}
		if(commandLabel.equalsIgnoreCase("ctf")) {
			
		}
		
		return false;
	}

	
	
}
