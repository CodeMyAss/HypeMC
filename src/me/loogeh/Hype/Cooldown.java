package me.loogeh.Hype;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilTime;
import me.loogeh.Hype.util.utilTime.TimeUnit;

public class Cooldown {
	public static Hype plugin;
	
	public static HashMap<String, AbilityCooldown> cooldownPlayers = new HashMap<String, AbilityCooldown>();
	
	public static void add(String player, String ability, long seconds, long systime) {
		if(!cooldownPlayers.containsKey(player)) cooldownPlayers.put(player, new AbilityCooldown(player));
		if(isCooling(player, ability)) return;
		cooldownPlayers.get(player).cooldownMap.put(ability, new AbilityCooldown(player, seconds * 1000, System.currentTimeMillis()));
	}
	
	public static boolean isCooling(String player, String ability) {
		if(!cooldownPlayers.containsKey(player)) return false;
		if(!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) return false;
		return true;
	}
	
	public static double getRemaining(String player, String ability) {
		if(!cooldownPlayers.containsKey(player)) return 0.0;
		if(!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) return 0.0;
		return utilTime.convert((cooldownPlayers.get(player).cooldownMap.get(ability).seconds + cooldownPlayers.get(player).cooldownMap.get(ability).systime) - System.currentTimeMillis(), TimeUnit.BEST, 1);
	}
	
	public static void coolDurMessage(Player player, String ability) {
		if(player == null) {
			return;
		}
		if(!isCooling(player.getName(), ability)) {
			return;
		}
		player.sendMessage(ChatColor.GRAY + WordUtils.capitalize(ability.toLowerCase()) + " Cooldown " + ChatColor.AQUA + getRemaining(player.getName(), ability) + " Seconds");
	}
	
	public static void removeCooldown(String player, String ability) {
		if(!cooldownPlayers.containsKey(player)) {
			return;
		}
		if(!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) {
			return;
		}
		cooldownPlayers.get(player).cooldownMap.remove(ability);
		Player cPlayer = Bukkit.getPlayer(player);
		if(player != null) {
			utilPlayer.coolMessage(cPlayer, ability);
		}
	}
	
	public static void handleCooldowns() {
		if(cooldownPlayers.isEmpty()) {
			return;
		}
		for(Iterator<String> it = cooldownPlayers.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			for(Iterator<String> iter = cooldownPlayers.get(key).cooldownMap.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				if(getRemaining(key, name) <= 0.0) {
					removeCooldown(key, name);
				}
			}
		}
	}
}
