package me.loogeh.Hype.util;

import net.minecraft.server.v1_6_R3.Packet61WorldEvent;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.loogeh.Hype.Hype;

public class utilWorld {
	public static Hype plugin;


	public static String chunkToStr(Chunk chunk) {
		if(chunk == null) {
			return "";
		} else {
			return chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
		}

	}

	public static String chunkToStrClean(Chunk chunk) {
		if(chunk == null) {
			return "";
		} else {
			return "(" + chunk.getX() + "," + chunk.getZ() + ")";
		}
	}

	public static Chunk strToChunk(String string) {

		try {
			String[] parts = string.split(",");

			return Bukkit.getWorld("world").getChunkAt(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

		} catch (Exception e) {}
		return null;
	}
	
	public static World strToWorld(String string) {
		for(World worlds : Bukkit.getWorlds()) {
			if(worlds.getName().equals(string)) {
				return Bukkit.getWorld(string);
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void blockParticleEffect(int blockID, World world, int x, int y, int z) {
		Packet61WorldEvent packet = new Packet61WorldEvent(2001, x, y, z, blockID, false);

		Location loc = new Location(world, x, y, z);

		for(Player players : Bukkit.getServer().getOnlinePlayers()) {
			((CraftPlayer) players).getHandle().playerConnection.sendPacket(packet);
		}

		loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.getMaterial(blockID));
	}

	@SuppressWarnings("deprecation")
	public static void blockParticleEffect(int blockID, World world, double x, double y, double z) {
		Location loc = new Location(world, x, y, z);
		loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.getMaterial(blockID));
	}

	
	public static Location strToLoc(String string) {
		if (string.length() == 0) return null;
		String[] tokens = string.split(",");
		try {
			for (World cur : plugin.getServer().getWorlds()) {
				if(cur.getName().equalsIgnoreCase(tokens[0])) return new Location(cur, Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
			}
		}
		catch (Exception e) {
			return null;
		}
		return null;
	}

	public static String locToStr(Location loc) {
		if (loc == null) return "";
		return loc.getWorld().getName() + "," + utilMath.round(loc.getX(), 1) + "," + loc.getY() + "," + loc.getZ();
	}
}
