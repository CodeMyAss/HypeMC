package me.loogeh.Hype.donations;

import java.util.HashSet;
import java.util.Random;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.donations.Donations.Benefit;
import me.loogeh.Hype.util.utilArmour;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Seizure {
	public static Hype plugin;
	
	public static HashSet<String> seizures = new HashSet<String>();
	
	private static Random rand = new Random();
	
	private static boolean running = false;
	
	private static void start() {
		plugin = Hype.plugin;
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				running = true;
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(seizures.contains(player.getName())) {
						if(utilArmour.hasSet(player, "LEATHER")) {
							LeatherArmorMeta hmeta = (LeatherArmorMeta) player.getInventory().getHelmet().getItemMeta();
							LeatherArmorMeta cmeta = (LeatherArmorMeta) player.getInventory().getChestplate().getItemMeta();
							LeatherArmorMeta lmeta = (LeatherArmorMeta) player.getInventory().getLeggings().getItemMeta();
							LeatherArmorMeta bmeta = (LeatherArmorMeta) player.getInventory().getBoots().getItemMeta();
							hmeta.setColor(Color.fromRGB(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
							cmeta.setColor(Color.fromRGB(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
							lmeta.setColor(Color.fromRGB(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
							bmeta.setColor(Color.fromRGB(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
							player.getInventory().getHelmet().setItemMeta(hmeta);
							player.getInventory().getChestplate().setItemMeta(cmeta);
							player.getInventory().getLeggings().setItemMeta(lmeta);
							player.getInventory().getBoots().setItemMeta(bmeta);
						} else {
							seizures.remove(player.getName());
						}
					}
				}
			}
		}, 3L, 4L);
	}
	
	//private static void stop() { Bukkit.getScheduler().cancelTask(taskID); }
	
	
	public static void toggle(Player player) {
		if(player == null) return;
		if(!Donations.hasPermission(player.getName(), Benefit.SEIZURE)) {
			Donations.sendMessage(player, ChatColor.WHITE + "You haven't donated for " + ChatColor.YELLOW + "Seizure");
			return;
		}
		
		if(seizures.contains(player.getName())) {
			removePlayer(player.getName());
			Donations.sendMessage(player, ChatColor.WHITE + "You disabled " + ChatColor.YELLOW + "Seizure");
			return;
		}
		if(!utilArmour.hasSet(player, "LEATHER")) {
			Donations.sendMessage(player, ChatColor.WHITE + "You must be wearing " + ChatColor.LIGHT_PURPLE + "Leather Armour");
			return;
		}
		if(!running) start();
		seizures.add(player.getName());
		Donations.sendMessage(player, ChatColor.WHITE + "You enabled " + ChatColor.YELLOW + "Seizure");
	}
	
	public static void removePlayer(String player) {
		if(!seizures.contains(player)) return;
		seizures.remove(player);
	}
}
