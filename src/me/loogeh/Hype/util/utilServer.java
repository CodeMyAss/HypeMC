package me.loogeh.Hype.util;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import me.loogeh.Hype.Cooldown;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.Message;
import me.loogeh.Hype.RCommands;
import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.bettershops.BetterShops;
import me.loogeh.Hype.moderation.Mute;
import net.minecraft.server.v1_6_R3.EnumSkyBlock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class utilServer {
	public static Hype plugin = Hype.plugin;
	
	public static HashMap<EntityType, Integer> spawnable = new HashMap<EntityType, Integer>();
	
	public static Player[] getPlayers() {
		
		return Bukkit.getServer().getOnlinePlayers();
	}
	
	  public void saveWorlds() {
	    long ctime = System.currentTimeMillis();
	    for (World w : plugin.getServer().getWorlds()) {
	      w.save();
	    }
	    System.out.println("Saved Worlds. Took " + (System.currentTimeMillis() - ctime) + " milliseconds.");
	  }

	public static void setSpawn(Player player) {
			Location newSpawn = player.getLocation();
			int x = (int) newSpawn.getX();
	     	int y = (int) newSpawn.getY();
	     	int z = (int) newSpawn.getZ();
	     	player.getWorld().setSpawnLocation(x, y, z);
	     	player.getWorld().getSpawnLocation().setYaw(player.getLocation().getYaw());
		 	player.sendMessage(ChatColor.GREEN + "Spawn set" + ChatColor.GRAY + " to current location");
	}
	
	public static void loadSchematic(String n, World w, int x, int y, int z){
        try {
            File fl = new File("plugins" + File.separator + "WorldEdit" + File.separator + "schematics" + File.separator + n + ".schematic");
            SchematicFormat sf = SchematicFormat.getFormat(fl);
            CuboidClipboard cc = sf.load(fl);            
            LocalWorld lw = BukkitUtil.getLocalWorld(w);
            EditSession es = new EditSession(lw, 1000000);
            Vector v = new Vector(x, y, z);
            cc.paste(es, v, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static boolean isInSpawn(Player player) {
		return (player.getLocation().getX() >= 726 && player.getLocation().getX() <= 1009 && player.getLocation().getZ() >= 317 && player.getLocation().getZ() <= 555);
	}
	
	public static boolean isInSpawn(Location loc) {
		return (loc.getX() >= 726 && loc.getX() <= 1009 && loc.getZ() >= 317 && loc.getZ() <= 555);
	}
	
	public static boolean isInSpawn(Entity ent) {
		return (ent.getLocation().getX() >= 726 && ent.getLocation().getX() <= 1009 && ent.getLocation().getZ() >= 317 && ent.getLocation().getZ() <= 555);
	}
	
	public static boolean isInNewSpawn(Player player) {
		return utilPlayer.isInArea(player, 857, 916, 416, 463);
	}
	
	public static boolean isInNewSpawn(Location loc) {
		return (loc.getX() < 917 && loc.getX() > 856 && loc.getZ() < 464 && loc.getZ() > 416);
	}
	
	public static boolean isInNewSpawnY(Player player) {
		return utilPlayer.isInAreaY(player, 862, 904, 63, 69, 423, 455);
	}
	
	public static boolean inGamesTower(Location loc) {
		return (loc.getX() > -31 && loc.getX() < 73 && loc.getZ() > 2151 && loc.getZ() < 2254);
	}
	
	public static boolean inLargeSpawn(Location loc) {
		return (loc.getX() > 788 && loc.getX() < 923 && loc.getZ() > 424 && loc.getZ() < 484);
	}
	
	public static void spawnAnimal(World world, Location loc, int amount, EntityType ent) {
		if(spawnable.isEmpty()) {
			spawnable.put(EntityType.BAT, 850);
			spawnable.put(EntityType.BLAZE, 350);
			spawnable.put(EntityType.CAVE_SPIDER, 400);
			spawnable.put(EntityType.CHICKEN, 750);
			spawnable.put(EntityType.COW, 500);
			spawnable.put(EntityType.CREEPER, 300);
			spawnable.put(EntityType.ENDER_DRAGON, 3);
			spawnable.put(EntityType.ENDERMAN, 200);
			spawnable.put(EntityType.GHAST, 5);
			spawnable.put(EntityType.GIANT, 1);
			spawnable.put(EntityType.HORSE, 300);
			spawnable.put(EntityType.IRON_GOLEM, 100);
			spawnable.put(EntityType.MAGMA_CUBE, 300);
			spawnable.put(EntityType.MUSHROOM_COW, 400);
			spawnable.put(EntityType.OCELOT, 500);
			spawnable.put(EntityType.PIG_ZOMBIE, 300);
			spawnable.put(EntityType.SHEEP, 400);
			spawnable.put(EntityType.SILVERFISH, 600);
			spawnable.put(EntityType.SKELETON, 200);
			spawnable.put(EntityType.SLIME, 300);
			spawnable.put(EntityType.SNOWMAN, 300);
			spawnable.put(EntityType.SPIDER, 350);
			spawnable.put(EntityType.SQUID, 500);
			spawnable.put(EntityType.WITCH, 50);
			spawnable.put(EntityType.WITHER, 7);
			spawnable.put(EntityType.WOLF, 200);
			spawnable.put(EntityType.ZOMBIE, 400);
		}
		if(!spawnable.containsKey(ent)) {
			return;
		}
		for(int i = 0; i < amount; i++) {
			world.spawnEntity(loc, ent);
		}
	}
	
	public static void killAnimals(String type, World world) {
		for(Iterator<Entity> it = world.getEntities().iterator(); it.hasNext();) {
			Entity ent = (Entity) it.next();
			if(type.equalsIgnoreCase("hostile")) {
			} else if(type.equalsIgnoreCase("nice")) {
			} else {
				ent.remove();
			}
		}
	}
	
	public static void forceBlockLight(World world, int x, int y, int z, int level) {
		net.minecraft.server.v1_6_R3.World w = ((CraftWorld) world).getHandle();
		w.b(EnumSkyBlock.BLOCK, x, y, z, level);
	}

	
	public static void startCheckThread() {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				Mute.checkMute();
				Squads.handlePower();
				BetterShops.ticksReset();
			}
		}, 20L, 20L);
	}
	
	public static void startCooldownThread() {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				Cooldown.handleCooldowns();
			}
		}, 1L, 1L);
	}
	
	public static void addStaff() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p = Bukkit.getPlayerExact(p.getName());
			if(p != null) {
				if(Permissions.isStaff(p)) {
					if(!Message.onlineStaff.contains(p.getName())) {
						Message.onlineStaff.add(p.getName());
					} else if(Message.onlineStaff.contains(p.getName())) {
						System.out.println("Online Staff already contains [" + p.getName() + "]");
					}
				}
			}
		}
	}
	
	public static void startMessageThread() {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "The plugin still may have some bugs, if you find any please report them at the website or to an admin! Thanks");
				Bukkit.broadcastMessage(ChatColor.AQUA + "Forums - " + ChatColor.WHITE + "http://hypemc.enjin.com/");
			}
		}, 12000L, 12000L);
	}
	
	public static void lockCheck() {
		ResultSet rs = MySQL.doQuery("SELECT locked FROM `server_options`");
		try {
			if(rs.next()) {
				if(rs.getString(1).equalsIgnoreCase("false")) {
					RCommands.locked = false;
				} else if(rs.getString(1).equalsIgnoreCase("true")) {
							RCommands.locked = true;
				}
			} else if(!rs.next()) {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
