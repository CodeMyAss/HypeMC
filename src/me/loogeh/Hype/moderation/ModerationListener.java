package me.loogeh.Hype.moderation;

import me.loogeh.Hype.Hype;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class ModerationListener implements Listener {
	public static Hype plugin;
	public ModerationListener(Hype instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void chatHandler(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(Mute.isMuted(player)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.YELLOW + "Muted (" + ChatColor.GREEN + Mute.getRemaining(player.getName()) + ChatColor.YELLOW + ") for " + ChatColor.GOLD + Mute.getReason(player.getName()));
		}
	}
	
	@EventHandler
	public void loginEvent(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if(Ban.isBanned(player.getName())) {
			event.disallow(Result.KICK_OTHER, ChatColor.RED + "Banned " + ChatColor.WHITE + " - " + ChatColor.YELLOW + "for " + ChatColor.GREEN + Ban.getReason(player.getName()) + ChatColor.WHITE + " - " + ChatColor.GREEN + Ban.getRemaining(player.getName()));
		}
	}

}
