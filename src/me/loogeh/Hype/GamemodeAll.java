package me.loogeh.Hype;

import me.loogeh.Hype.SQL.Permissions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeAll implements CommandExecutor {
	public static Hype plugin;

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;
		if(commandLabel.equalsIgnoreCase("gmalls") && (Permissions.getLevel(p).equalsIgnoreCase("owner"))) {
			for(Player players : Bukkit.getOnlinePlayers()) {
				if(Permissions.getRank(players) != Permissions.Ranks.OWNER) {
					if(players.getGameMode() != GameMode.SURVIVAL) {
						players.setGameMode(GameMode.SURVIVAL);
					}
				}
			}
			Bukkit.broadcastMessage(ChatColor.GRAY + "All players gamemodes were set to " + ChatColor.GREEN + "Survival!");
			return true;

		}
		if(commandLabel.equalsIgnoreCase("gmallc") && (Permissions.getLevel(p).equalsIgnoreCase("owner"))) {
			for(Player players  : Bukkit.getOnlinePlayers()) {
				if(Permissions.getRank(players) != Permissions.Ranks.OWNER) {
					if(players.getGameMode() != GameMode.CREATIVE) {
						players.setGameMode(GameMode.CREATIVE);
					}
				}
			}
			Bukkit.broadcastMessage(ChatColor.GRAY + "All players gamemodes were set to " + ChatColor.GREEN + "Creative!");
			return true;
		}
		if(commandLabel.equalsIgnoreCase("gmalla") && (Permissions.getLevel(p).equalsIgnoreCase("owner"))) {
			for(Player players : Bukkit.getOnlinePlayers()) {
				if(Permissions.getRank(players) == Permissions.Ranks.OWNER) {
					if(players.getGameMode() != GameMode.ADVENTURE) {
						players.setGameMode(GameMode.ADVENTURE);
					}
				}
			}
			Bukkit.broadcastMessage(ChatColor.GRAY + "All players gamemodes were set to " + ChatColor.GREEN + "Adventure!");
			return true;
		}
		return false;
	}
}
