package me.loogeh.Hype.armour;

import java.util.HashSet;
import java.util.UUID;

import me.loogeh.Hype.Cooldown;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.InfoHolder;
import me.loogeh.Hype.M;
import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.event.BowUnchargeEvent;
import me.loogeh.Hype.util.utilArmour;
import me.loogeh.Hype.util.utilItems;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilWorld;
import net.minecraft.server.v1_6_R3.EntityItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftItem;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Jugg implements Listener {
	public static Hype plugin = Hype.plugin;
	
	public Jugg(Hype instance) {
		plugin = instance;
	}
	
	private HashSet<String> stomp = new HashSet<String>();
	private HashSet<UUID> crit_arrow = new HashSet<UUID>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJuggInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if(!utilArmour.hasSet(player, "DIAMOND")) return;
		if(utilItems.isAbilitySword(player.getItemInHand().getTypeId())) {
			if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
			if(Cooldown.isCooling(player.getName(), "Stomp")) {
				Cooldown.coolDurMessage(player, "Stomp");
				return;
			}
			Cooldown.add(player.getName(), "Stomp", 12, System.currentTimeMillis());
			utilPlayer.abilityMessage(player, "Stomp");
			player.setVelocity(new Vector(0, 2, 0));
			stomp.add(player.getName());
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				public void run() {
					player.setVelocity(new Vector(0, -7, 0));
				}
			}, 10L);
		}
//		if(player.getItemInHand().getType() == Material.BOW) {
//			if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
//			if(!player.getInventory().contains(Material.ARROW)) return;
//			if(Cooldown.isCooling(player.getName(), "critical")) {
//				Cooldown.coolDurMessage(player, "critical");
//				return;
//			}
//			final String name = player.getName();
//			final Charge charge = new Charge("critical", 12, System.currentTimeMillis());
//			Cooldown.add(player.getName(), "critical", 16, System.currentTimeMillis());
//			InfoHolder.player_charge.put(player.getName(), charge);
//			new BukkitRunnable() {
//				
//				@SuppressWarnings("unused")
//				@Override
//				public void run() {
//					if(player == null) {
//						InfoHolder.player_charge.remove(name);
//						this.cancel();
//					}
//					if(player.getItemInHand().getType() != Material.BOW) {
//						player.setLevel(0);
//						this.cancel();
//						return;
//					}
//					if(charge.isUsed()) {
//						player.setLevel(0);
//						this.cancel();
//						return;
//					}
//					if(player.getLevel() < charge.getLimit()) {
//						InfoHolder.player_charge.get(player.getName()).setLastRun(System.currentTimeMillis());
//						InfoHolder.player_charge.get(player.getName()).addLevel();
//						player.setLevel(player.getLevel() + 1);
//					} else {
//						M.v(player, "Jugg", ChatColor.YELLOW + "Critical " + ChatColor.WHITE + "fully charged");
//						this.cancel();
//					}
//				}
//			}.runTaskTimer(plugin, 10L, 10L);
//		}
		if(player.getItemInHand().getType() == Material.FIRE) {
			for(double angle = 0.0; angle < 360.0; angle += 18.0) {
				double x = Math.cos(angle);
				double z = Math.sin(angle);
				Vector direction = new Vector(x, 0.2, z);
				Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.FIRE));
				item.setVelocity(direction);
				item.setMetadata("fire_ability", new FixedMetadataValue(plugin, Boolean.valueOf(true)));
				((EntityItem) ((CraftItem) item).getHandle()).age = 5900;
			}
		}
	}
	
	@EventHandler
	public void onJuggBowShoot(EntityShootBowEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		if(!(event.getProjectile() instanceof Arrow)) return;
		Player player = (Player) event.getEntity();
		Arrow arrow = (Arrow) event.getProjectile();
		if(player.getLevel() == 0) return;
		if(InfoHolder.player_charge.containsKey(player.getName())) {
			player.setLevel(0);
			crit_arrow.add(arrow.getUniqueId());
		}
		
	}
	
	@EventHandler
	public void onJuggDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(!utilArmour.hasSet(player, "DIAMOND")) return;
		if(event.getCause() == DamageCause.FALL) {
			if(stomp.contains(player.getName())) {
				event.setCancelled(true);
				stomp.remove(player.getName());
				player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_METAL, 1.0F, 1.0F);
				for(Entity entity : player.getNearbyEntities(5, 5, 5)) {
					if(entity instanceof Player) {
						Player p = (Player) entity;
						player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, 3));
						p.sendMessage(ChatColor.AQUA + player.getName() + ChatColor.GRAY + " used " + ChatColor.DARK_AQUA + "Stomp");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onJuggFish(PlayerFishEvent event) {
		Player player = event.getPlayer();
		if(!utilArmour.hasSet(player, "DIAMOND")) return;
		if(event.getState() == State.CAUGHT_ENTITY) {
			if(Cooldown.isCooling(player.getName(), "Drag")) {
				Cooldown.coolDurMessage(player, "Drag");
				return;
			}
			if(Squads.isClaimed(utilWorld.chunkToStr(event.getHook().getLocation().getChunk()))) {
				M.v(player, "Jugg", ChatColor.WHITE + "You cannot use " + ChatColor.YELLOW + "Drag " + ChatColor.WHITE + "in " + ChatColor.LIGHT_PURPLE + Squads.getOwnerChunk(event.getHook().getLocation().getChunk()));
				return;
			}
			player.teleport(event.getHook());
			utilPlayer.abilityMessage(player, "Drag");
			Cooldown.add(player.getName(), "Drag", 8, System.currentTimeMillis());
			
		}
	}
	
	@EventHandler
	public void onJuggUncharge(BowUnchargeEvent event) {
		Player player = event.getPlayer();
		if(event.getReason() == BowUnchargeEvent.UnchargeReason.SHOOT) {
			if(InfoHolder.player_charge.containsKey(player.getName())) {
				InfoHolder.player_charge.get(player.getName()).setUsed(true);
				player.setLevel(0);
			}
			return;
		}
		if(InfoHolder.player_charge.containsKey(player.getName())) {
			InfoHolder.player_charge.remove(player.getName());
			M.v(player, "Jugg", ChatColor.YELLOW + "Critical Charge " + ChatColor.WHITE + "cancelled");
			player.setLevel(0);
		}
	}
	
	@EventHandler
	public void onJuggEntityDamage(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Arrow)) return;
		Arrow arrow = (Arrow) event.getDamager();
		if(!(arrow.getShooter() instanceof Player)) return;
		Player player = (Player) arrow.getShooter();
		if(!InfoHolder.player_charge.containsKey(player.getName())) return;
		int level = InfoHolder.player_charge.get(player.getName()).getLevel();
		if(level == 0) return;
		if(crit_arrow.contains(arrow.getUniqueId())) {
			event.setDamage((level / 1.5) + 2.5);
			if(event.getEntity() instanceof Player) {
				Player damaged = (Player) event.getEntity();
				M.v(damaged, "Jugg", ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " shot you with a " + ChatColor.LIGHT_PURPLE + "Critical Arrow " + level);
				InfoHolder.player_charge.remove(player.getName());
			}
		}
	}
	
	@EventHandler
	public void onJuggProjHit(ProjectileHitEvent event) {
		if(!(event.getEntity() instanceof Arrow)) return;
		Arrow arrow = (Arrow) event.getEntity();
		if(!(arrow.getShooter() instanceof Player)) return;
		final Player player = (Player) arrow.getShooter();
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if(InfoHolder.player_charge.containsKey(player.getName())) InfoHolder.player_charge.remove(player.getName());
			}
		}, 5L);
		
	}
}
