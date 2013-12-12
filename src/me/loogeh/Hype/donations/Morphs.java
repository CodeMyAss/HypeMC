package me.loogeh.Hype.donations;

import java.util.HashMap;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;
import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;
import me.loogeh.Hype.Hype;

public class Morphs {
	public static Hype plugin;
	
	public static HashMap<String, MorphType> morphList = new HashMap<String, MorphType>();
	
	
	public static DisguiseCraftAPI dcAPI;
	public static void setupDisguiseCraft() {
		dcAPI = DisguiseCraft.getAPI();
	}
	
	public static MorphType getCurrent(Player player) {
		return morphList.get(player.getName());
	}
	
	public static void morph(Player player, MorphType type) {
		if(isMorphed(player)) {
			MorphType morphType = morphList.get(player.getName());
			player.sendMessage(ChatColor.YELLOW + "You are already morphed as " + ChatColor.LIGHT_PURPLE + WordUtils.capitalize(morphType.toString().toLowerCase().replaceAll("_", " ")));
			return;
		}
		if(type == MorphType.PIG) {
			dcAPI.disguisePlayer(player, new Disguise(dcAPI.newEntityID(), DisguiseType.Pig));
			morphList.put(player.getName(), type);
			return;
		}
		if(type == MorphType.COW) {
			dcAPI.disguisePlayer(player, new Disguise(dcAPI.newEntityID(), DisguiseType.Cow));
			morphList.put(player.getName(), type);
			return;
		}
		if(type == MorphType.IRON_GOLEM) {
			dcAPI.disguisePlayer(player, new Disguise(dcAPI.newEntityID(), DisguiseType.IronGolem));
			morphList.put(player.getName(), type);
			return;
		}
		if(type == MorphType.CAVE_SPIDER) {
			dcAPI.disguisePlayer(player, new Disguise(dcAPI.newEntityID(), DisguiseType.CaveSpider));
			morphList.put(player.getName(), type);
			return;
		}
		if(type == MorphType.SQUID) {
			dcAPI.disguisePlayer(player, new Disguise(dcAPI.newEntityID(), DisguiseType.Squid));
			morphList.put(player.getName(), type);
			return;
		}
	}
	
	public static boolean isMorphed(Player player) {
		return morphList.containsKey(player.getName());
	}
	
	public static String getType(Player player) {
		if(!isMorphed(player)) {
			return "None";
		}
		if(morphList.get(player.getName()) == MorphType.PIG) { //ID = 1
			return "Pig";
		}
		if(morphList.get(player.getName()) == MorphType.SQUID) { //ID = 2
			return "Squid";
		}
		if(morphList.get(player.getName()) == MorphType.COW) { //ID = 3
			return "Cow";
		}
		if(morphList.get(player.getName()) == MorphType.IRON_GOLEM) {//ID = 4
			return "Iron Golem";
		}
		if(morphList.get(player.getName()) == MorphType.CAVE_SPIDER) {//ID = 5
			return "Cave Spider";
		}
		if(morphList.get(player.getName()) == MorphType.WOLF) {//ID = 6;
			return "Wolf";
		}
		return "None";
	}
	
	public static void unmorph(Player player) {
		if(!isMorphed(player)) {
			return;
		}
		dcAPI.undisguisePlayer(player);
		morphList.remove(player.getName());
	}
	
	public static enum MorphType {
		NONE,
		PIG,
		COW,
		IRON_GOLEM,
		CAVE_SPIDER,
		SQUID,
		WOLF;
		
		
	}
	
}
