package me.loogeh.Hype.Squads2;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.RCommands;
import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.event.SquadTerritoryEnterEvent;
import me.loogeh.Hype.games.Games;
import me.loogeh.Hype.util.utilItems;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilServer;
import me.loogeh.Hype.util.utilWorld;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SquadsListener implements Listener {
	public static Hype plugin;
	public SquadsListener(Hype instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSquadMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		String chunkFrom = utilWorld.chunkToStr(event.getFrom().getChunk());
		String chunkTo = utilWorld.chunkToStr(event.getTo().getChunk());
		String owner = Squads.getOwner(chunkTo);
		String pSquad = Squads.getSquad(player.getName());
		int fx = event.getFrom().getBlockX() >> 4;
		int fz = event.getFrom().getBlockZ() >> 4;
		int tx = event.getTo().getBlockX() >> 4;
		int tz = event.getTo().getBlockZ() >> 4;
		if((fx != tx) || (fz != tz)) {
			if(Squads.getOwner(chunkFrom).equalsIgnoreCase(owner)) {
				return;
			}
			String ter = owner;
			if(ter.equalsIgnoreCase("None")) ter = "Wilderness";
			plugin.getServer().getPluginManager().callEvent(new SquadTerritoryEnterEvent(player, Squads.getOwner(chunkFrom), ter));
			if(!Squads.hasSquad(player.getName())) {
				if(owner.equalsIgnoreCase("None")) {
					player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.YELLOW + "Wilderness");
					return;
				}
				player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.WHITE + owner);
				return;
			}
			if(Squads.hasSquad(player.getName())) {
				if(owner.equalsIgnoreCase("None")) {
					player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.YELLOW + "Wilderness");
					return;
				}

				if(owner.equals(pSquad)) {
					player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.WHITE + pSquad);
					return;
				}
				if(!Squads.areAllies(owner, pSquad) && !Squads.areEnemies(owner, pSquad)) {
					player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.WHITE + owner);
					return;
				}
				if(Squads.areAllies(owner, pSquad)) {
					if(!Squads.oneTrust(owner, pSquad)) {
						player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.GREEN + owner);
						return;
					}
					player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.DARK_GREEN + owner);
					return;
				}
				if(Squads.areEnemies(owner, pSquad)) {
					player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.RED + owner);
					return;
				}
			}
			if(Squads.getOwner(chunkFrom) == null) {
				if(owner != null && Squads.getOwner(chunkFrom) == null) return;
				if(owner == null) return;
				if(owner.equals(pSquad)) {
					player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.WHITE + pSquad);
					return;
				}
				if(owner.equalsIgnoreCase("None")) {
					player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.YELLOW + "Wilderness");
					return;
				}
				if(Squads.areAllies(owner, pSquad)) {
					if(!Squads.areTrusted(owner, pSquad)) {
						player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.DARK_GREEN + owner);
						return;
					}
					player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.GREEN + owner);
					return;
				}
				if(Squads.areEnemies(owner, pSquad)) {
					player.sendMessage(ChatColor.BLUE + "Territory - " + ChatColor.RED + owner);
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSquadDamageByEntity(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		if(!(event.getDamager() instanceof Player) && (!(event.getDamager() instanceof Projectile))) {
			return;
		}
		Player player = (Player) event.getEntity();
		if(player == null) return;
		if(utilServer.inLargeSpawn(player.getLocation())) {
			event.setCancelled(true);
			return;
		}
		if(event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			if(Games.getCurrentGame(player) != null || Games.getCurrentGame(damager) != null) return;
			if(utilServer.inLargeSpawn(damager.getLocation())) {
				event.setCancelled(true);
				damager.sendMessage(ChatColor.WHITE + "You cannot damage people in " + ChatColor.YELLOW + "Spawn");
				return;
			}
			if(!Squads.hasSquad(player.getName())) return;
			if(!Squads.hasSquad(damager.getName())) return;
			String pSquad = Squads.getSquad(player.getName());
			String dSquad = Squads.getSquad(damager.getName());
			if(pSquad.equals(dSquad)) {
				event.setCancelled(true);
				damager.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot damage " + ChatColor.YELLOW + player.getName());
				return;
			}
			if(Squads.areAllies(pSquad, dSquad)) {
				event.setCancelled(true);
				damager.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot damage " + ChatColor.YELLOW + player.getName());
			}
			return;
		}
		if(event.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) event.getDamager();
			if(proj ==  null) {
				return;
			}
			if(proj.getShooter() == null) return;
			if(!(proj.getShooter() instanceof Player)) return;
			Player damager = (Player) proj.getShooter();
			if(Games.getCurrentGame(player) != null || Games.getCurrentGame(damager) != null) return;
			String pSquad = Squads.getSquad(player.getName());
			String dSquad = Squads.getSquad(damager.getName());
			if(pSquad.equals(dSquad)) {
				event.setCancelled(true);
				damager.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot damage " + ChatColor.YELLOW + player.getName());
			}
			if(Squads.areAllies(pSquad, dSquad)) {
				event.setCancelled(true);
				damager.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot damage " + ChatColor.YELLOW + player.getName());
			}
			return;
		}
	}


	@EventHandler(priority = EventPriority.HIGH)
	public void onSquadBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String bChunk = utilWorld.chunkToStr(event.getBlock().getChunk());
		if(!Squads.isClaimed(bChunk)) {
			return;
		}
		if(Squads.isClaimed(bChunk)) {
			String owner = Squads.getOwnerChunk(event.getBlock().getChunk());
			String pSquad = Squads.getSquad(player.getName());
			if(RCommands.adminMode.containsKey(player.getName())) {
				if(player.getItemInHand().getType() != Material.WOOD_AXE) {
					event.setCancelled(false);
					return;
				}
				return;
			}
			if(owner.equals(pSquad)) {
				return;
			}
			event.setCancelled(true);
			player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot break in " + ChatColor.YELLOW + owner);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSquadPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		String bChunk = utilWorld.chunkToStr(event.getBlock().getChunk());
		if(!Squads.isClaimed(bChunk)) {
			return;
		}
		String owner = Squads.getOwner(bChunk);
		String pSquad = Squads.getSquad(player.getName());
		if(RCommands.adminMode.containsKey(player.getName())) {
			event.setCancelled(false);
			return;
		}
		if(owner.equalsIgnoreCase(pSquad)) return;
		if(event.getBlock().getType() == Material.WATER_BUCKET || event.getBlock().getType() == Material.LAVA_BUCKET) return;
		event.setCancelled(true);
		player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot place in " + ChatColor.YELLOW + owner);
	}

	@EventHandler
	public void onEntityBreak(EntityBreakDoorEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onEntityBlockChange(EntityChangeBlockEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSquadDeath(PlayerDeathEvent event) {
		Player player = event.getEntity().getPlayer();
		String squad = Squads.getSquad(player.getName());
		if(Games.getCurrentGame(player) != null) return;
		if(!Squads.hasSquad(player.getName())) return;
		if(Squads.getPower(squad) > -3) {
			Squads.subtractPower(squad, 1);
			Squads.regenMap.put(squad, System.currentTimeMillis());
		}
	}

	@EventHandler
	public void onSquadChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		utilPlayer.sendChat(player, event.getMessage());
		event.setCancelled(true);
		String sqlMsg = event.getMessage().replaceAll("'", "");
		MySQL.doUpdate("INSERT INTO `chat_log`(`player`, `message`) VALUES ('" + event.getPlayer().getName() + "','" + sqlMsg.toString() + "')");
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onSquadInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(!Squads.isClaimed(utilWorld.chunkToStr(event.getClickedBlock().getChunk()))) {
				return;
			}
			String owner = Squads.getOwner(utilWorld.chunkToStr(event.getClickedBlock().getChunk())); //CHANGED TO CLICKED BLOCK
			if(RCommands.adminMode.containsKey(player.getName())) {
				event.setCancelled(false);
				return;
			}
			if(Squads.hasSquad(player.getName())) {
				String pSquad = Squads.getSquad(player.getName());
				if(pSquad.equalsIgnoreCase(owner)) return;
				if(Squads.oneTrust(owner, pSquad)) {
					if(event.getClickedBlock().getType() == Material.STONE_BUTTON
							|| event.getClickedBlock().getType() == Material.WOOD_BUTTON
							|| event.getClickedBlock().getType() == Material.WOODEN_DOOR
							|| event.getClickedBlock().getType() == Material.LEVER
							|| event.getClickedBlock().getType() == Material.WORKBENCH
							|| event.getClickedBlock().getType() == Material.FURNACE
							|| event.getClickedBlock().getType() == Material.TRAP_DOOR) {
						event.setCancelled(false);
						return;
					}
				}
			}
			if(owner.equalsIgnoreCase("Shops") || owner.equalsIgnoreCase("GamesTower")) {
				if(Games.getCurrentGame(player) == null) event.setCancelled(true);
				else event.setCancelled(false);
				return;
			}
			if(utilItems.isInteractable(event.getClickedBlock().getTypeId())) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot interact in " + ChatColor.YELLOW + owner);
				return;
			}
		}
	}
	
//	@EventHandler
//	public void onSquadCreatureSpawn(CreatureSpawnEvent event) {
//		Entity entity = event.getEntity();
//		if(Squads.isClaimed(utilWorld.chunkToStr(event.getEntity().getLocation().getChunk()))) {
//			if(event.getEntityType() == EntityType.ZOMBIE) {
//				Zombie zombie = (Zombie) entity;
//				if(zombie.getCustomName() == null) {
//					event.setCancelled(true);
//					System.err.println("It's the null");
//					return;
//				}
//				String name = ChatColor.stripColor(zombie.getCustomName());
//				if(name.equalsIgnoreCase("Builder") || name.equalsIgnoreCase("Baker") 
//						|| name.equalsIgnoreCase("Miner") || name.equalsIgnoreCase("Miscellaneous") || name.equalsIgnoreCase("Weaponry")
//						|| name.equalsIgnoreCase("Enchanter")) {
//					event.setCancelled(false);
//					System.err.println("It's the names");
//					return;
//				}
//			}
//			event.setCancelled(true);
//		}
//	}
}
