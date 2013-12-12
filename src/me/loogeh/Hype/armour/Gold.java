package me.loogeh.Hype.armour;

import java.util.ArrayList;

import me.loogeh.Hype.CTag;
import me.loogeh.Hype.Cooldown;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.util.utilArmour;
import me.loogeh.Hype.util.utilItems;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilServer;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Gold implements Listener{
	public static Hype plugin;
	public static ArrayList<String> dodgecool = new ArrayList<String>();
	public Gold(Hype instance) {
		plugin = instance;
	}

	//dodge
	@SuppressWarnings("deprecation")
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		Action at = event.getAction();
		ItemStack item = player.getItemInHand();
		if(item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.IRON_SWORD || item.getType() == Material.GOLD_SWORD) {
			if(at == Action.RIGHT_CLICK_BLOCK) {
				if(!utilArmour.hasSet(player, "GOLD")) return;
				if(utilItems.isInteractable(event.getClickedBlock().getTypeId())) return;
				if(Cooldown.isCooling(player.getName(), "dodge")) {
					Cooldown.coolDurMessage(player, "dodge");
					return;
				}
				Cooldown.add(player.getName(), "dodge", 8, System.currentTimeMillis());
				player.setVelocity(player.getLocation().getDirection().multiply(13.5D));
				utilPlayer.abilityMessage(player, "Dodge");
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						
					@Override
					public void run() {
						player.setVelocity(player.getVelocity().zero());
					}
				}, 3L);
			}
		}
		if(item.getType() == Material.IRON_AXE || item.getType() == Material.DIAMOND_AXE) {
			if(at == Action.RIGHT_CLICK_AIR) {
			if(!utilArmour.hasSet(player, "GOLD")) return;
			if(Cooldown.isCooling(player.getName(), "rush")) {
				Cooldown.coolDurMessage(player, "rush");
				return;
			}
			utilPlayer.abilityMessage(player, "Rush");
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
			Cooldown.add(player.getName(), "rush", 16, System.currentTimeMillis());
			}
		}
		if(at == Action.RIGHT_CLICK_AIR) {
			if(item.getType() == Material.GOLD_SPADE) {
				if(!utilArmour.hasSet(player, "GOLD")) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
					return;
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
			}
			if(item.getType() == Material.GOLD_HOE) {
				if(!utilArmour.hasSet(player, "GOLD")) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 0));
					return;
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 80, 0));
			}
		}
	}
	@EventHandler
	public void damage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(!utilArmour.hasSet(player, "GOLD")) return;
			if(event.getCause() == EntityDamageEvent.DamageCause.LAVA || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void entDMGent(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			if(event.getDamager() instanceof Player) {
				Player player = (Player) event.getEntity();
				Player damager = (Player) event.getDamager();
				if(utilServer.inLargeSpawn(player.getLocation())) {
					if(!CTag.isTagged(player.getName())) {
						event.setCancelled(true);
						return;
					}
				}
				ItemStack source = damager.getItemInHand();
				if(damager.getItemInHand().getType() == Material.DIAMOND_AXE) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3));
					player.sendMessage(ChatColor.AQUA + damager.getName() + ChatColor.GRAY + " crippled you");
					damager.sendMessage(ChatColor.GRAY + "You crippled " + ChatColor.AQUA + player.getName());
					player.getWorld().playEffect(player.getLocation(), Effect.POTION_BREAK, 11);
				}
				if(source.getType() == Material.GOLD_AXE) {
					if(!utilArmour.hasSet(damager, "GOLD")) {
						player.setFireTicks(50);
						return;
					}
					player.setFireTicks(100);
				}
				if(source.getType() == Material.GOLD_SWORD) {
					if(utilArmour.hasSet(damager, "GOLD")) event.setDamage(7.5);
				}
			}
		}
	}
	
}
