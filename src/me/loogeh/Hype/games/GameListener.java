package me.loogeh.Hype.games;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.loogeh.Hype.HPlayer;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.M;
import me.loogeh.Hype.Squads.Squads;
import me.loogeh.Hype.event.SquadTerritoryEnterEvent;
import me.loogeh.Hype.games.Games.GameStatus;
import me.loogeh.Hype.games.Games.GameType;
import me.loogeh.Hype.util.utilServer;

public class GameListener implements Listener {
	static Hype plugin = Hype.plugin;
	
	public GameListener(Hype instance) {
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGamePlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(Games.getCurrentGame(player) == null) return;
		Game cur = Games.getCurrentGame(player);
		if(cur.getStatus() != GameStatus.PLAYING) {
			player.teleport(Games.getSpec());
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.getInventory().setContents(HPlayer.get(player.getName()).getInventory(cur.getName()));
			player.getInventory().setArmorContents(HPlayer.get(player.getName()).getInventory(cur.getName() + "_armour"));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGameInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(Games.getCurrentGame(player) == null) return;
		event.setCancelled(false);
		if(event.getAction() == Action.PHYSICAL) {
			if(Games.getCurrentGame(player).getType() == GameType.SPLEGG) {
				player.setVelocity(player.getLocation().getDirection().multiply(3).add(new Vector(0.0F, 1.0F, 0.0F)));
				Splegg.no_fall.add(player.getName());
			}
		}
		if(event.getAction() == Action.RIGHT_CLICK_AIR) {
			if(Games.getCurrentGame(player).getType() == GameType.SPLEGG) {
				if(Games.getCurrentGame(player).getStatus() != GameStatus.PLAYING) return;
				if(player.getItemInHand().getType() == Material.IRON_SPADE) {
					player.launchProjectile(Egg.class);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGameDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Player) {
			Player player = (Player) entity;
			if(event.getCause() == DamageCause.FALL) {
				if(Splegg.no_fall.contains(player.getName())) {
					if(player.getLocation().getY() < 65) {
						Splegg.no_fall.remove(player.getName());
						return;
					}
					event.setCancelled(true);
					Splegg.no_fall.remove(player.getName());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGameCreatureSpawn(CreatureSpawnEvent event) {
		if(utilServer.inGamesTower(event.getLocation())) {
			if(event.getSpawnReason() == SpawnReason.NATURAL) event.setCancelled(true);
			if(event.getSpawnReason() == SpawnReason.EGG) event.setCancelled(true);
		}
		if(Squads.isClaimed(event.getLocation().getChunk())) {
			if(event.getSpawnReason() != SpawnReason.CUSTOM) event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGameProjectileHit(ProjectileHitEvent event) {
		if(event.getEntityType() == EntityType.EGG) {
			Block block = event.getEntity().getLocation().add(0D, -0.4D, 0.0D).getBlock();
			if(block.getType() != Material.WOOL && block.getType() != Material.TNT) return;
			if(block.getType() == Material.TNT) {
				Location loc = block.getLocation();
				block.setType(Material.AIR);
				block.getWorld().createExplosion(loc, 6.0F, false);
				return;
			}
			block.setType(Material.AIR);
			
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGameTerritoryEnter(SquadTerritoryEnterEvent event) {
		Player player = event.getPlayer();
		String from = event.getFrom();
		String to = event.getTo();
		if(to.equalsIgnoreCase("GamesTower")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0), true);
		}
		if(from.equalsIgnoreCase("GamesTower") && !to.equalsIgnoreCase("GamesTower")) {
			if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) player.removePotionEffect(PotionEffectType.NIGHT_VISION);
		}
		if(!from.equalsIgnoreCase("GamesTower") && !to.equalsIgnoreCase("GamesTower")) {
			if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) player.removePotionEffect(PotionEffectType.NIGHT_VISION);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGameEntityDamage(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			if(Games.getCurrentGame(damager) == null) return;
			if(Games.getCurrentGame(player) == null) return;
			if(Games.getCurrentGame(damager).getStatus() == GameStatus.LOBBY) event.setCancelled(true);
			if(Games.getCurrentGame(damager).getType() == GameType.SPLEGG) event.setCancelled(true);
			if(Games.getCurrentGame(player).getType() == GameType.KIT_ARENA) {
				if(Games.getCurrentGame(player).getTeam(player) != Games.getCurrentGame(damager).getTeam(damager)) {
					event.setCancelled(false);
					return;
				}
				event.setCancelled(true);
				M.v(damager, "Game", ChatColor.WHITE + "You may not harm your " + ChatColor.YELLOW + "Team mate");
				return;
			}
		}
		if(event.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) event.getDamager();
			if(!(proj.getShooter() instanceof Player)) return;
			Player shooter = (Player) proj.getShooter();
			if(Games.getCurrentGame(shooter) == null) return;
			if(Games.getCurrentGame(player) == null) return;
			Game cur = Games.getCurrentGame(shooter);
			Game curp = Games.getCurrentGame(player);
			if(cur.getType() == GameType.KIT_ARENA) {
				if(cur.getTeam(shooter) != curp.getTeam(player)) {
					event.setCancelled(false);
					return;
				}
				event.setCancelled(true);
				M.v(shooter, "Game", ChatColor.WHITE + "You may not harm your " + ChatColor.YELLOW + "Team mate");
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGamePlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if(Games.getCurrentGame(player) == null) return;
		if(Games.getCurrentGame(player).getType() == GameType.SPLEGG) {
			Splegg.die(player);
		}
		if(Games.getCurrentGame(player).getType() == GameType.KIT_ARENA) {
			KitArena.die(player, event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGameBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(Games.getCurrentGame(player) == null) return;
		Game cur = Games.getCurrentGame(player);
		if(cur.getType() == GameType.KIT_ARENA) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGameBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(Games.getCurrentGame(player) == null) return;
		Game cur = Games.getCurrentGame(player);
		if(cur.getType() == GameType.KIT_ARENA) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGameFoodLevelChange(FoodLevelChangeEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(Games.getCurrentGame(player) == null) return;
		Game cur = Games.getCurrentGame(player);
		if(cur.getType() == GameType.KIT_ARENA) event.setCancelled(true);
	}
}
