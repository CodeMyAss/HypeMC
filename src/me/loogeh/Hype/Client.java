package me.loogeh.Hype;

import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Client implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Console cannot use client commands");
			return true;
		}
		Player player = (Player) sender;
 		if(commandLabel.equalsIgnoreCase("c") || commandLabel.equalsIgnoreCase("client")) {
 			if(!player.getName().equalsIgnoreCase("Loogeh")) {
 				utilPlayer.permMsg(player);
 				return true;
 			}
			if(args.length == 0) {
				HPlayer p = InfoHolder.hype_players.get(player.getName());
//				player.sendMessage(C.clientInfo + "Session " + ChatColor.WHITE + p.getSessionTime());
//				player.sendMessage(C.clientInfo + "Last Reward " + ChatColor.WHITE + p.getLastReward());
				p.sendInfo(player);
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("playtime")) {
					InfoHolder.hype_players.get(player.getName()).updatePlayTime();
					long playTime = InfoHolder.hype_players.get(player.getName()).getPlayTime();
					player.sendMessage(playTime + " Millis");
					return true;
				}
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					if(!InfoHolder.hype_players.containsKey(args[0])) {
						player.sendMessage(ChatColor.RED + "Server - " + ChatColor.WHITE + "Invalid player");
						return true;
					}
					HPlayer.sendInfo(player, args[0]);
					return true;
				}
			}
		}
		return false;
	}

}
