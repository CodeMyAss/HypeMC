package me.loogeh.Hype;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class M {
	public static Hype plugin;
	
	public static void message(Player reciever, mType type, String message, String variable) {
		if(type == mType.HELP) reciever.sendMessage(ChatColor.GREEN + message + " " + variable);
		else if(type == mType.COOLDOWN_COMPLETE) reciever.sendMessage(ChatColor.LIGHT_PURPLE + variable + " Cooldown - " + ChatColor.YELLOW + "Completed");
		else if(type == mType.COOLDOWN_INIT) reciever.sendMessage(ChatColor.YELLOW + "You used " + ChatColor.LIGHT_PURPLE + WordUtils.capitalize(variable));
	}
	
	public static enum mType {
		HELP,
		COMMAND_ERROR,
		COOLDOWN_COMPLETE,
		COOLDOWN_INIT,
		OFFLINE_PLAYER;
	}
	
	public static void v(Player player, String v, String message) {
		if(player == null) return;
		player.sendMessage(ChatColor.BLUE + v + " - " + message);
	}
	
	public static void v(String v, String message) {
		Bukkit.broadcastMessage(ChatColor.BLUE + v + " - " + message);
	}
	
	public static void h(Player player, String c, String desc) {
		if(player == null) return;
		player.sendMessage(ChatColor.AQUA + c + ChatColor.WHITE + " " + desc);
	}
}
