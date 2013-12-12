package me.loogeh.Hype;

import java.util.HashMap;

import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.util.utilTime;
import me.loogeh.Hype.util.utilTime.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class CTag implements Listener {
	public static Hype plugin;
	public static HashMap<String, TagPlayer> ctags = new HashMap<String, TagPlayer>();
	public static HashMap<String, Entity> villages = new HashMap<String, Entity>();
	
	public CTag(Hype instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onCTagDamage(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player) || (!(event.getDamager() instanceof Player))) return;
		if(event.isCancelled()) return;
		Player damagee = (Player) event.getEntity();
		Player damager = (Player) event.getDamager();
		if(Squads.getSquad(damagee.getName()).equalsIgnoreCase(Squads.getSquad(damager.getName()))) return;
		ctags.put(damager.getName(), new TagPlayer(damager.getLocation(), damager.getInventory().getContents(), damager.getInventory().getArmorContents(), damager.getWorld().getName()));
		start(damager.getName());
		
	}
	
	public static double getRemInt(String player) {
		if(!isTagged(player)) {
			return 0.0;
		}
		return utilTime.convert((System.currentTimeMillis() - ctags.get(player).init), TimeUnit.SECONDS, 1);
	}
	
	private static boolean dropContents(String player) {
		Player bPlayer = Bukkit.getPlayer(player);
		if(bPlayer == null) {
			World world = Bukkit.getWorld(ctags.get(player).world);
			for(ItemStack items : ctags.get(player).contents) {
				world.dropItemNaturally(ctags.get(player).loc, items);
			}
			for(ItemStack items : ctags.get(player).armour) {
				world.dropItemNaturally(ctags.get(player).loc, items);
			}
			M.v("Combat Log", ChatColor.YELLOW + player + ChatColor.WHITE + " dropped their inventory for " + ChatColor.LIGHT_PURPLE + "Combat Logging");
			MySQL.doUpdate("UPDATE player_info SET clear_inv=true WHERE player='" + player + "'");
			return true;
		}
		return false;
		
	}
	
	public static boolean isTagged(String player) {
		return ctags.containsKey(player);
	}
	
	public static void spawnVillage(Player player) {
		//Villager villager = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
	}
	
	public static void start(final String player) {
		plugin = Hype.plugin;
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			public void run() {
				dropContents(player);
			}
		}, 1200L);
	}
	
	class TagPlayer {
		
		private Location loc;
		private ItemStack[] contents;
		private ItemStack[] armour;
		private long init;
		private String world;
		
		public TagPlayer(Location loc, ItemStack[] contents, ItemStack[] armour, String world) {
			this.loc = loc;
			this.contents = contents;
			this.armour = armour;
			this.init = System.currentTimeMillis();
			this.world = world;
		}
		
		public Location getLocation() {
			return this.loc;
		}
		
		public ItemStack[] getContents() {
			return this.contents;
		}
		
		public ItemStack[] getArmour() {
			return this.armour;
		}
		
		public long getInit() {
			return this.init;
		}
		
		public String getWorld() {
			return this.world;
		}
		
		public void setInit(long millis) {
			this.init = millis;
		}
	}
}
