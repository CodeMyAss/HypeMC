package me.loogeh.Hype.armour;

import java.util.HashMap;

import me.loogeh.Hype.Cooldown;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.util.utilArmour;
import me.loogeh.Hype.util.utilMath;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilWorld;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Archer implements Listener {
	public static Hype plugin;
	public static HashMap<String, ArrowType> arrowList = new HashMap<String, ArrowType>();
	public Archer(Hype instance) {
		plugin = instance;
	}

	@EventHandler
	public void onSquadDamageByEntity(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		if(!(event.getDamager() instanceof Arrow)) return;
		Player player = (Player) event.getEntity();
		if(event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if(arrow ==  null) return;
			if(arrow.getShooter() == null) return;
			if(!(arrow.getShooter() instanceof Player)) return;
			Player damager = (Player) arrow.getShooter();
			String pSquad = Squads.getSquad(player.getName());
			String dSquad = Squads.getSquad(damager.getName());
			if(pSquad.equalsIgnoreCase(dSquad)) {
				event.setCancelled(true);
				damager.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot damage " + ChatColor.YELLOW + player.getName());
				return;
			}
			if(!arrowList.containsKey(damager.getName())) return;
			if(arrowList.get(damager.getName()) == ArrowType.REPULSE) {
				player.setVelocity(damager.getLocation().getDirection().multiply(2.5));
				player.sendMessage(ChatColor.BLUE + "Archer - " + ChatColor.LIGHT_PURPLE + damager.getName() + ChatColor.WHITE + " shot you with a " + ChatColor.LIGHT_PURPLE + "Repulse Arrow");
				return;
			}
			if(arrowList.get(damager.getName()) == ArrowType.POISON_TIP) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 1));
				player.sendMessage(ChatColor.BLUE + "Archer - " + ChatColor.LIGHT_PURPLE + damager.getName() + ChatColor.WHITE + " shot you with a " + ChatColor.LIGHT_PURPLE + "Posion Arrow");
				return;
			}
			if(arrowList.get(damager.getName()) == ArrowType.CRIPPLING_ARROW) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
				player.sendMessage(ChatColor.BLUE + "Archer - " + ChatColor.LIGHT_PURPLE + damager.getName() + ChatColor.WHITE + " shot you with a " + ChatColor.LIGHT_PURPLE + "Paralyzing Arrow");
				return;
			}
			return;
		}
	}

	@EventHandler
	public void damage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
//			if(event instanceof EntityDamageByEntityEvent) {
//				EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
//				if(e.getEntity() instanceof LivingEntity) {
//					LivingEntity entity = (LivingEntity)e.getEntity();
//					Entity damager = e.getDamager();
//					if(damager instanceof Arrow) {
//						Entity shooter = ((Projectile)damager).getShooter();
//						if(shooter instanceof Player) {
//							Player arrowShooter = (Player) shooter;
//							if(entity instanceof Player) {
//								if(!arrowList.containsKey(arrowShooter.getName())) {
//									return;
//								}
//								if(arrowList.get(player.getName()) == ArrowType.REPULSE) {
//									entity.setVelocity(arrowShooter.getLocation().getDirection().multiply(2.5));
//									((Player) entity).sendMessage(ChatColor.BLUE + "Archer - " + ChatColor.LIGHT_PURPLE + arrowShooter.getName() + ChatColor.WHITE + " shot you with a " + ChatColor.LIGHT_PURPLE + "Repulse Arrow");
//									return;
//								}
//								if(arrowList.get(player.getName()) == ArrowType.POISON_TIP) {
//									((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 1));
//									((Player) entity).sendMessage(ChatColor.BLUE + "Archer - " + ChatColor.LIGHT_PURPLE + arrowShooter.getName() + ChatColor.WHITE + " shot you with a " + ChatColor.LIGHT_PURPLE + "Posion Arrow");
//									return;
//								}
//								if(arrowList.get(player.getName()) == ArrowType.CRIPPLING_ARROW) {
//									((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
//									((Player) entity).sendMessage(ChatColor.BLUE + "Archer - " + ChatColor.LIGHT_PURPLE + arrowShooter.getName() + ChatColor.WHITE + " shot you with a " + ChatColor.LIGHT_PURPLE + "Posion Arrow");
//									return;
//								}
//							}
//						}
//					}
//				}
//			}
			if(!utilArmour.hasSet(player, "LEATHER")) {
				return;
			} else {
				if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void interact(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		Action at = event.getAction();
		if(at == Action.RIGHT_CLICK_AIR) {
			if(player.getItemInHand().getType() != Material.IRON_AXE) return;
			if(!utilArmour.hasSet(player, "LEATHER")) return;
			if(Cooldown.isCooling(player.getName(), "leap")) {
				Cooldown.coolDurMessage(player, "leap");
				return;
			}
			player.setVelocity(player.getLocation().getDirection().multiply(1.7D));
			utilWorld.blockParticleEffect(utilMath.getRandom(1, 15), player.getWorld(), (int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ());
			utilPlayer.abilityMessage(player, "Leap");
			Cooldown.add(player.getName(), "leap", 8, System.currentTimeMillis());
		}
		if(at == Action.LEFT_CLICK_AIR) {
			if(player.getItemInHand().getType() != Material.BOW) return;
			if(!utilArmour.hasSet(player, "LEATHER")) return;
			if(getArrowType(player) == ArrowType.NONE) setArrowType(player, ArrowType.REPULSE);
			else if(getArrowType(player) == ArrowType.REPULSE) setArrowType(player, ArrowType.POISON_TIP);
			else if(getArrowType(player) == ArrowType.POISON_TIP) setArrowType(player, ArrowType.CRIPPLING_ARROW);
			else setArrowType(player, ArrowType.REPULSE);
		}
	}

	@EventHandler
	public void onProjShoot(EntityShootBowEvent event) {
		if(!(event.getEntity() instanceof Arrow)) return;
		Arrow arrow = (Arrow) event.getEntity();
		if(!(arrow.getShooter() instanceof Player)) return;
		Player player = (Player) arrow.getShooter();
		if(arrowList.containsKey(player.getName())) {
			if(!utilArmour.hasSet(player, "LEATHER")) arrowList.remove(player.getName());
		}
	}

	private ArrowType getArrowType(Player player) {
		if(player == null) return null;
		if(!arrowList.containsKey(player.getName())) return ArrowType.NONE;
		return arrowList.get(player.getName());
	}

	private void setArrowType(Player player, ArrowType type) {
		if(player == null) return;
		arrowList.put(player.getName(), type);
		player.sendMessage(ChatColor.BLUE + "Archer - " + ChatColor.GREEN + toString(type) + " Arrow");
	}

	private String toString(ArrowType type) {
		if(type == ArrowType.NONE) return "None";
		else if(type == ArrowType.REPULSE) return "Repulse";
		else if(type == ArrowType.POISON_TIP) return "Poison";
		else return "Paralyzing";
	}

	public enum ArrowType {
		NONE,
		REPULSE,
		POISON_TIP,
		CRIPPLING_ARROW;
	}

}
