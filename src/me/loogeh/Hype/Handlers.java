package me.loogeh.Hype;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.SQL.Permissions.Ranks;
import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.bettershops.BetterShops;
import me.loogeh.Hype.bettershops.BetterShops.ItemType;
import me.loogeh.Hype.donations.Morphs;
import me.loogeh.Hype.donations.Seizure;
import me.loogeh.Hype.economy.Money;
import me.loogeh.Hype.event.BowUnchargeEvent;
import me.loogeh.Hype.event.PlayerArmourEvent;
import me.loogeh.Hype.event.PlayerNakedEvent;
import me.loogeh.Hype.games.Games;
import me.loogeh.Hype.moderation.Ban;
import me.loogeh.Hype.util.utilItems;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilServer;
import me.loogeh.Hype.util.utilWorld;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Handlers {
	public static Hype plugin = Hype.plugin;
	
	private static HashSet<ItemStack> banned_blocks = new HashSet<ItemStack>();
	public static HashMap<String, Long> last_respawn = new HashMap<String, Long>();
	
	public static void handleCreatureSpawn(final CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		if(event.getSpawnReason() != SpawnReason.CUSTOM) {
			if(utilServer.isInSpawn(event.getLocation())) event.setCancelled(true);
		}
		if(event.getSpawnReason() == SpawnReason.CUSTOM) {
			if(event.getEntityType() == EntityType.ZOMBIE) {
				final Zombie zombie = (Zombie) entity;
				if(utilServer.isInSpawn(zombie)) {
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
						@Override
						public void run() {
							if(zombie.getCustomName() == null) {
								event.setCancelled(true);
								return;
							}
							String name = ChatColor.stripColor(zombie.getCustomName());
							if(name.equalsIgnoreCase("Builder") || name.equalsIgnoreCase("Baker") 
									|| name.equalsIgnoreCase("Miner") || name.equalsIgnoreCase("Miscellaneous") || name.equalsIgnoreCase("Weaponry")
									|| name.equalsIgnoreCase("Enchanter")) {
								event.setCancelled(false);
								return;
							}
						}
					}, 20L);
				}
			}
			if(Squads.isClaimed(utilWorld.chunkToStr(event.getLocation().getChunk()))) {
				String owner = Squads.getOwnerChunk(event.getLocation().getChunk());
				if(owner.equalsIgnoreCase("Shops") || owner.equalsIgnoreCase("OuterShops") || owner.equalsIgnoreCase("Shops2") || owner.equalsIgnoreCase("Arena")) return;
				event.setCancelled(true);
			}
		}
		if(event.getEntityType() == EntityType.ZOMBIE) {
			Zombie zombie = (Zombie) event.getEntity();
			EntityEquipment equip = zombie.getEquipment();
			equip.setArmorContents(null);
			equip.setItemInHand(null);
			zombie.setBaby(false);
		}
		if(event.getEntityType() == EntityType.SKELETON) {
			Skeleton skele = (Skeleton) event.getEntity();
			EntityEquipment equip = skele.getEquipment();
			equip.setArmorContents(null);
			if(skele.getCustomName() == null) {
				equip.setItemInHand(new ItemStack(Material.BOW));
			}
		}
		if(event.getEntityType() == EntityType.PIG_ZOMBIE) {
			PigZombie pigz = (PigZombie) event.getEntity();
			EntityEquipment equipment = pigz.getEquipment();
			equipment.setArmorContents(null);
			equipment.setItemInHand(new ItemStack(Material.GOLD_SWORD));
		}
	}
	
	public static void handlePlayerPortal(PlayerPortalEvent event) {
//		Player player = event.getPlayer();
//		if(event.getCause() == TeleportCause.END_PORTAL) {
//			if(utilServer.inGamesTower(player.getLocation())) Games.toNextLevel(player);
//			event.setCancelled(true);
//		}
	}
	
	public static void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(!CTag.isTagged(player.getName())) {
			if(utilServer.isInNewSpawn(player)) event.setCancelled(true);
		}
	}
	
	public static void handleLoginEvent(PlayerLoginEvent event) {
		if(RCommands.locked) {
			if(!Permissions.isStaff(event.getPlayer())) {
				String reason = "";
				ResultSet rs = MySQL.doQuery("SELECT lock_reason FROM server_options");
				try {
					if(rs.next()) {
						reason = rs.getString(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				event.disallow(Result.KICK_OTHER, ChatColor.RED + "Locked - " + ChatColor.WHITE + "The server is locked because " + ChatColor.YELLOW + reason);
				return;
			}
		}
		if(Ban.isBanned(event.getPlayer().getName())) {
			if(Ban.getRemInt(event.getPlayer().getName()) <= 0.0) Ban.unban(event.getPlayer().getName());
			else event.disallow(Result.KICK_BANNED, ChatColor.RED + "Banned - " + ChatColor.WHITE + "You are banned [" + ChatColor.YELLOW + Ban.getRemaining(event.getPlayer().getName()) + ChatColor.WHITE + "] for " + ChatColor.YELLOW + Ban.getReason(event.getPlayer().getName()));
		}
		if(event.getResult() == Result.KICK_FULL) {
			if(!Permissions.isDonator(event.getPlayer()) && !Permissions.isStaff(event.getPlayer())) {
				event.disallow(Result.KICK_FULL, ChatColor.GOLD + "Server full " + ChatColor.WHITE + " - " + ChatColor.GRAY + "Donate for a reserved slot");
				return;
			}
			event.allow();
		}
	}
	
	public static void handleJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(player.getName().equalsIgnoreCase("Loogeh")) event.setJoinMessage(null);
		if(CTag.isTagged(player.getName())) event.setJoinMessage(ChatColor.DARK_GRAY + "Join - " + ChatColor.GRAY + player.getName() + " (" + ChatColor.GREEN + "Tag" + ChatColor.GRAY + ")");
		if(!CTag.isTagged(player.getName()) && !player.getName().equalsIgnoreCase("Loogeh")) event.setJoinMessage(ChatColor.DARK_GRAY + "Join - " + ChatColor.GRAY + player.getName());
		if(RCommands.locked) player.sendMessage(ChatColor.RED + "Server - " + ChatColor.WHITE + "The server is locked");
		if(Permissions.getRank(player) == Ranks.OWNER) player.setPlayerListName(ChatColor.DARK_RED + player.getName());
		else if(Permissions.getRank(player) == Ranks.ADMIN) player.setPlayerListName(ChatColor.RED + player.getName());
		else if(Permissions.getRank(player) == Ranks.MODERATOR) player.setPlayerListName(ChatColor.GREEN + player.getName());
		else if(Permissions.getRank(player) == Ranks.CONTRIBUTOR) player.setPlayerListName(ChatColor.YELLOW + player.getName());
		else if(Permissions.getRank(player) == Ranks.DONATOR) player.setPlayerListName(ChatColor.DARK_AQUA + player.getName());
		else player.setPlayerListName(ChatColor.WHITE + player.getName());
		player.setScoreboard(ScoreboardHandler.getScoreboard());
		if(Permissions.isStaff(player)) {
			ScoreboardHandler.addStaff(player, Permissions.getRank(player));
			if(!Message.onlineStaff.contains(player.getName())) Message.onlineStaff.add(player.getName());
		}
		for(Player players : Bukkit.getOnlinePlayers()) players.showPlayer(player);
		HPlayer.load(player.getName());
//		HPlayer.get(player.getName()).setLastJoin(System.currentTimeMillis());
		if(player.getLevel() != 0) player.setLevel(0);
		player.setExp(0.99F);
	}
	
	public static void handleLeaveEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(player.getName().equalsIgnoreCase("Loogeh")) event.setQuitMessage(null);
		else if(CTag.isTagged(player.getName())) event.setQuitMessage(ChatColor.DARK_GRAY + "Leave - " + ChatColor.GRAY + player.getName() + " (" + ChatColor.RED + "Tag" + ChatColor.GRAY + ")");
		else event.setQuitMessage(ChatColor.DARK_GRAY + "Leave - " + ChatColor.GRAY + player.getName());
		if(Message.onlineStaff.contains(player.getName())) Message.onlineStaff.remove(player.getName());
		Message.removeReplies(player.getName());
		HPlayer.write(player.getName());
		Seizure.removePlayer(player.getName());
		Morphs.unmorph(player);
		if(InfoHolder.hype_players.containsKey(player.getName())) InfoHolder.hype_players.remove(player.getName());
		Bukkit.getServer().getPluginManager().callEvent(new BowUnchargeEvent(player, BowUnchargeEvent.UnchargeReason.PLAYER_LEAVE));
	}
	
	public static void handleBlockPlaceEvent(BlockPlaceEvent event) {
		if(banned_blocks.isEmpty()) {
			banned_blocks.add(new ItemStack(Material.ENCHANTMENT_TABLE));
			banned_blocks.add(new ItemStack(Material.ANVIL));
			banned_blocks.add(new ItemStack(Material.OBSIDIAN));
		}
		ItemStack block = event.getItemInHand();
		Player player = event.getPlayer();
		if(banned_blocks.contains(block) && !utilPlayer.adminMode(player)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "Server - " + ChatColor.YELLOW + WordUtils.capitalize(block.getType().toString().toLowerCase().replaceAll("_", " ")) + ChatColor.WHITE + " is disabled");
			return;
		}
		if(utilServer.isInSpawn(player)) {
			if(utilPlayer.adminMode(player)) {
				event.setCancelled(false);
				return;
			}
			event.setCancelled(true);
			if(!Squads.isClaimed(utilWorld.chunkToStr(event.getBlock().getChunk()))) player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot place in " + ChatColor.YELLOW + "Spawn");
		}
	}
	
	public static void handleBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(utilPlayer.adminMode(player)) {
			event.setCancelled(false);
			return;
		}
		if(utilServer.isInSpawn(player)) {
			event.setCancelled(true);
			if(!Squads.isClaimed(utilWorld.chunkToStr(event.getBlock().getChunk()))) {
				player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot break in " + ChatColor.YELLOW + "Spawn");
			}
		}
	}
	
	public static void handlePlayerDeathEvent(PlayerDeathEvent event) {
		String prefix = "§8Death - ";
		Player player = event.getEntity();
		EntityDamageEvent cause = player.getLastDamageCause();
		Player killer = player.getKiller();
		if(Games.getCurrentGame(player) != null) return;
		if(killer != null) {
			ItemStack item = killer.getItemInHand();
			int e = utilItems.countLevels(item);
			if(item == null) {
				event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + killer.getName() + ChatColor.GRAY + " with " + ChatColor.LIGHT_PURPLE + "Fists");
				return;
			}
			String itemName = utilItems.toCommon(item.getType().toString().toLowerCase());
			if(e < 1) {
				event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + killer.getName() + ChatColor.GRAY + " with " + ChatColor.LIGHT_PURPLE + itemName);
				return;
			}
			event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + killer.getName() + ChatColor.GRAY + " with " + ChatColor.LIGHT_PURPLE + "L" + e + " " + itemName);
		}
		if(cause instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
			if(damager instanceof Arrow || damager instanceof CraftArrow) {
				Arrow arrow = (Arrow) damager;
				if(arrow.getShooter() == null) return;
				Entity shooter = arrow.getShooter();
				if(shooter instanceof Skeleton) {
					event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was shot by a " + ChatColor.YELLOW + "Skeleton");
				}
				if(shooter instanceof Player) {
					Player pShooter = (Player) shooter;
					ItemStack item = pShooter.getItemInHand();
					int e = utilItems.countLevels(item);
					if(e < 1) {
						event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was shot by " + ChatColor.YELLOW + pShooter.getName() + ChatColor.GRAY + " with " + ChatColor.YELLOW + WordUtils.capitalize(item.getType().toString().toLowerCase().replaceAll("_", " ")));
						deathHead(player, player.getLocation());
						return;
					}
					event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was shot by " + ChatColor.YELLOW + pShooter.getName() + ChatColor.GRAY + " with " + ChatColor.YELLOW + "L" + e + " " + WordUtils.capitalize(item.getType().toString().toLowerCase().replaceAll("_", " ")));
					deathHead(player, player.getLocation());
				}
			}
			if(damager.getType() == EntityType.CREEPER) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was blow up by a " + ChatColor.YELLOW + "Creeper");
			if(damager instanceof Egg) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.YELLOW + "Egg");
			if(damager instanceof EnderDragon) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was hit by " + ChatColor.YELLOW + "Ender Dragon");
			if(damager instanceof Fireball) {
				Fireball fireball = (Fireball) damager;
				if(fireball.getShooter() instanceof Ghast) {
					event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " blown up by " + ChatColor.YELLOW + "Ghast");
				}
			}
			if(damager instanceof Giant) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was crushed by " + ChatColor.YELLOW + "Giant");
			if(damager instanceof IronGolem) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was thrown by " + ChatColor.YELLOW + "Iron Golem");
			if(damager instanceof MagmaCube) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was burnt by " + ChatColor.YELLOW + "Magma Cube");
			if(damager instanceof PigZombie) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.YELLOW + "Zombie Pigman");
			if(damager instanceof Silverfish) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was bitten by " + ChatColor.YELLOW + "Silverfish");
			if(damager.getType() == EntityType.SKELETON) {
				Skeleton skele = (Skeleton) damager;
				if(skele.getSkeletonType() == SkeletonType.NORMAL) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.YELLOW + "Skeleton");
				if(skele.getSkeletonType() == SkeletonType.WITHER) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was destroyed by " + ChatColor.YELLOW + "Wither Skeleton");
			}
			if(damager instanceof Slime) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was swallowed by " + ChatColor.YELLOW + "Slime");
			if(damager instanceof Snowball) {
				Snowball snowball = (Snowball) damager;
				if(snowball.getShooter() instanceof Player) {
					Player shooter = (Player) snowball.getShooter();
					event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was snowballed by " + ChatColor.YELLOW + shooter.getName());
					return;
				}
				event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was snowballed");
			}
			if(damager instanceof Snowman) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was frozen by " + ChatColor.YELLOW + "Snowman");
			if(damager.getType() == EntityType.SPIDER) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.YELLOW + "Spider");
			if(damager instanceof Witch) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was Harry Pottered by " + ChatColor.YELLOW + "Witch");
			if(damager.getType() == EntityType.WITHER) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.YELLOW + "Wither");
			if(damager instanceof Wolf) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was mauled by " + ChatColor.YELLOW + "Wolf");
			if(damager instanceof Zombie) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.YELLOW + "Zombie");
			if(damager instanceof ThrownPotion) {
				ThrownPotion potion = (ThrownPotion) damager;
				if(potion.getShooter() instanceof Witch) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was Harry Pottered by " + ChatColor.YELLOW + "Witch");
			}
			//else event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.YELLOW + WordUtils.capitalize(damager.toString().toLowerCase().replaceAll("_", " ")));
			else event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + " died");
		}
		if(!(cause instanceof EntityDamageByEntityEvent)) {
			if(cause.getCause() == DamageCause.CONTACT) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + "Cactus");
			if(cause.getCause() == DamageCause.DROWNING) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " drowned");
			if(cause.getCause() == DamageCause.FALL) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + "Fall");
			if(cause.getCause() == DamageCause.FIRE || cause.getCause() == DamageCause.FIRE_TICK) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + "Fire");
			if(cause.getCause() == DamageCause.LAVA) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + "Lava");
			if(cause.getCause() == DamageCause.LIGHTNING) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + "struck by " + ChatColor.YELLOW + "Lightning");
			if(cause.getCause() == DamageCause.MAGIC) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + "Damage Potion");
			if(cause.getCause() == DamageCause.POISON) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + "Poison");
			if(cause.getCause() == DamageCause.STARVATION) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + "Starvation");
			if(cause.getCause() == DamageCause.SUFFOCATION) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " suffocated");
			if(cause.getCause() == DamageCause.SUICIDE) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed by " + ChatColor.YELLOW + "Suicide");
			if(cause.getCause() == DamageCause.VOID) event.setDeathMessage(prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " fell through the " + ChatColor.YELLOW + "Void");
		}
	}
	
	public static void handleEntityDeath(EntityDeathEvent event) {
		if(event.getEntityType() == EntityType.ZOMBIE) {
			Zombie zombie = (Zombie) event.getEntity();
			if(zombie.getCustomName() == null) return;
			String name = ChatColor.stripColor(zombie.getCustomName());
			ItemType type = BetterShops.getType(name);
			BetterShops.spawnShop(type);
		}
	}
	
	public static void handleBlockDecay(BlockFadeEvent event) {
		if(utilServer.isInSpawn(event.getBlock().getLocation())) {
			if(event.getBlock().getType() == Material.LEAVES) {
				event.setCancelled(true);
			}
		}
	}
	
	public static void handleBlockTo(BlockFromToEvent event) {
		if(utilServer.isInSpawn(event.getBlock().getLocation())) {
			if(event.getBlock().getType() == Material.LEAVES && event.getToBlock().getType() == Material.AIR) {
				event.setCancelled(true);
			}
		}
	}
	
	public static void handleRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		last_respawn.put(player.getName(), System.currentTimeMillis());
	}
	
	public static void handleBlockSpread(BlockSpreadEvent event) {
		if(event.getBlock().getType() == Material.FIRE) event.setCancelled(true);
	}
	
	public static void handleItemChange(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if(player == null) return;
		if(player.getInventory().getItem(event.getPreviousSlot()) == null) {
			Bukkit.getServer().getPluginManager().callEvent(new BowUnchargeEvent(player, BowUnchargeEvent.UnchargeReason.ITEM_CHANGE));
			return;
		}
		if(player.getInventory().getItem(event.getPreviousSlot()).getType() == null) {
			Bukkit.getServer().getPluginManager().callEvent(new BowUnchargeEvent(player, BowUnchargeEvent.UnchargeReason.ITEM_CHANGE));
			return;
		}
		if(player.getInventory().getItem(event.getPreviousSlot()).getType() == Material.BOW) plugin.getServer().getPluginManager().callEvent(new BowUnchargeEvent(player, BowUnchargeEvent.UnchargeReason.ITEM_CHANGE));
	}
	
	public static void handleArrowShoot(ProjectileLaunchEvent event) {
		if(!(event.getEntity() instanceof Arrow)) return;
		if(!(event.getEntity().getShooter() instanceof Player)) return;
		Player player = (Player) event.getEntity().getShooter();
		Bukkit.getServer().getPluginManager().callEvent(new BowUnchargeEvent(player, BowUnchargeEvent.UnchargeReason.SHOOT));
	}
	
	public static void handleTeleport(PlayerTeleportEvent event) {
		if(event.getPlayer().getItemInHand().getType() == Material.BOW) Bukkit.getServer().getPluginManager().callEvent(new BowUnchargeEvent(event.getPlayer(), BowUnchargeEvent.UnchargeReason.TELEPORT));
	}
	
	public static void handleEntityDamage(EntityDamageEvent event) {
		if(event.getEntityType() == EntityType.ZOMBIE) {
			Zombie zombie = (Zombie) event.getEntity();
			if(zombie.getCustomName() == null) return;
			event.setCancelled(true);
			zombie.setFireTicks(0);
		}
		if(event.getEntityType() == EntityType.SKELETON) {
			Skeleton skele = (Skeleton) event.getEntity();
			if(skele.getCustomName() == null) return;
			event.setCancelled(true);
			skele.setFireTicks(0);
		}
	}
	
	public static void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getEntityType() == EntityType.ZOMBIE) {
			Zombie zombie = (Zombie) event.getEntity();
			if(zombie.getCustomName() == null) return;
			event.setCancelled(true);
		}
		if(event.getEntityType() == EntityType.SKELETON) {
			Skeleton skele = (Skeleton) event.getEntity();
			if(skele.getCustomName() == null) return;
			event.setCancelled(true);
		}
	}
	
	public static void handleInventoryClick(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)) return;
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		SlotType slot = event.getSlotType();
		if(item == null) return;
		if(slot != SlotType.ARMOR) return;
		if(player.getGameMode() == GameMode.CREATIVE) {
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Owner: " + ChatColor.AQUA + player.getName(), ChatColor.GRAY + "Source: " + ChatColor.AQUA + "Creative"));
			item.setItemMeta(meta);
		}
		for(ItemStack a : player.getInventory().getArmorContents()) {
			if(a != null) {
				Bukkit.getServer().getPluginManager().callEvent(new PlayerArmourEvent(player));
				return;
			}
			Bukkit.getServer().getPluginManager().callEvent(new PlayerNakedEvent(player));
		}
		if(event.getView().getTopInventory().getName().contains("'s Inventory")) {
			if(event.getCurrentItem() == null) return;
			ItemStack citem = event.getCurrentItem();
			Player holder = (Player) event.getInventory().getHolder();
			holder.getInventory().remove(citem);
		}
	}
	
	public static void handleLeavesDecay(LeavesDecayEvent event) {
		if(utilServer.isInSpawn(event.getBlock().getLocation())) event.setCancelled(true);
	}
	
	
	public static void deathHead(Player player, Location location) {
		if(!utilPlayer.hasArmour(player)) return;
		if(Money.getMoney(player) < 35000) return;
		int pMoney = Money.getMoney(player);
		Item item = location.getWorld().dropItemNaturally(location, new ItemStack(Material.SKULL_ITEM, 1, (short) 3));
		SkullMeta meta = (SkullMeta) item.getItemStack().getItemMeta();
		meta.setOwner(player.getName());
		meta.setDisplayName(player.getName());
		int price;
		if(pMoney < 200000) price = Math.round((int) (pMoney * 0.05));
		else price = 20000;
		meta.setLore(Arrays.asList(ChatColor.GREEN + "$" + price));
	}
}
