package me.loogeh.Hype.Squads;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.Squads.utilSquads.SquadCheck;

public class SquadsListener {
	public static Hype plugin;
	
	public SquadsListener(Hype instance) {
		plugin = instance;
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOWEST)
	public void squadBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled()) {
			return;
		} else {
			if(event.getPlayer().getWorld().getEnvironment() != World.Environment.NORMAL) {
				return;
			}
			if(Squads.canBreak(event.getBlock().getTypeId())) {
				return;
			}
			if(utilSquads.getAccess(event.getPlayer(), event.getBlock().getLocation()) == utilSquads.SquadCheck.OWN) {
				return;
			}
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Squads -" + ChatColor.WHITE + "You cannot break in " + ChatColor.AQUA + utilSquads.getOwnerStringRel(event.getBlock().getLocation(), event.getPlayer().getName()));
		}

	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void squadDamageEvent(EntityDamageByEntityEvent event) {
		if(!(event instanceof EntityDamageByEntityEvent)) {
			return;
		}
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		if(event.getDamager() instanceof Player) {
			Player damaged = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			
			if(!utilSquads.canDamage(damaged, damager)) {
				event.setCancelled(true);
				
				if(damager != null) {
					damager.sendMessage(ChatColor.DARK_AQUA + "Squads - " + ChatColor.WHITE + "You cannot damage " + ChatColor.AQUA + damaged.getName());
				}
			}
		} else if(event.getDamager() instanceof Projectile) {
			Player damaged = (Player) event.getEntity();
			Projectile proj = (Projectile) event.getDamager();
			
			if(proj == null) {
				return;
			}
			if(proj.getShooter() == null) {
				return;
			}
			if(!(proj.getShooter() instanceof Player)) {
				return;
			}
			Player damager = (Player) proj.getShooter();
			
			if(utilSquads.canDamage(damaged, damager)) {
				if(damager != null) {
					damager.sendMessage(ChatColor.DARK_AQUA + "Squads - " + ChatColor.WHITE + "You cannot damage " + ChatColor.AQUA + damaged.getName());
				}
			}
		}
	}
	
	@EventHandler
	public void squadDeathEvent(PlayerDeathEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		
		hSquad squad = utilSquads.getSquadByPlayer(player);
		if(squad == null) {
			return;
		}
		
		squad.changePower(-1);
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public static void squadsInteract(PlayerInteractEvent event) {
		if(event.getPlayer().getWorld().getEnvironment() != World.Environment.NORMAL) {
			return;
		}
		if((event.getAction() != Action.RIGHT_CLICK_BLOCK) && (event.getAction() != Action.LEFT_CLICK_BLOCK)) {
			return;
		}
		SquadCheck access = utilSquads.getAccess(event.getPlayer(), event.getClickedBlock().getLocation());
		if(Squads.disallowInteract(event.getClickedBlock().getTypeId())) {
			if(access == SquadCheck.ENEMY) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Squads - " + ChatColor.WHITE + "You cannot use that in " + ChatColor.AQUA + utilSquads.getOwnerStringRel(event.getClickedBlock().getLocation(), event.getPlayer().getName()));
			}
			return;
		}
		
		if((!Squads.allowInteract(event.getClickedBlock().getTypeId())) || (access != SquadCheck.ALLY_TRUST)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Squads - " + ChatColor.WHITE + "You cannot use that in " + ChatColor.AQUA + utilSquads.getOwnerStringRel(event.getClickedBlock().getLocation(), event.getPlayer().getName()));
			return;
		}
		if((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getItemInHand().getType() != Material.AIR)
				&& (Squads.denyUsePlace(event.getPlayer().getItemInHand().getTypeId())) && (!Squads.allowUsePlace(event.getPlayer().getItemInHand().getTypeId()))) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Squads - " + ChatColor.WHITE + "You cannot use that in " + utilSquads.getOwnerStringRel(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation(), event.getPlayer().getName()));
		}
	}
}
