package me.loogeh.Hype;


import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode {
	public static Hype plugin;

	public static void toggleGamemode(Player player) {
		if(player.getGameMode() == GameMode.CREATIVE){
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage(ChatColor.RED + "Gamemode:" + ChatColor.WHITE + " Survival mode");
		}
		else if(player.getGameMode() == GameMode.SURVIVAL){
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage(ChatColor.RED + "Gamemode:" + ChatColor.WHITE + " Creative mode");
		}
	}

	public static void toggleGamemodeOther(Player player, CommandSender sender) {
		if(player.getGameMode() == GameMode.CREATIVE){
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage(ChatColor.RED + "Gamemode: " + ChatColor.WHITE + "Survival mode");
			sender.sendMessage(ChatColor.RED + player.getDisplayName() + "'s Gamemode:" + ChatColor.WHITE + " Survival mode");
		}
		else if(player.getGameMode() == GameMode.SURVIVAL){
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage(ChatColor.RED + "Gamemode: " + ChatColor.WHITE + "Creative mode");
			sender.sendMessage(ChatColor.RED + player.getDisplayName() + "'s Gamemode:" + ChatColor.WHITE + " Creative mode");
		}
	}
}