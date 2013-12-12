package me.loogeh.Hype;

import me.loogeh.Hype.shops.Shops;
import me.loogeh.Hype.util.utilServer;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageListener {
	public static Hype plugin;
	public DamageListener(Hype instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			final Player player = (Player) event.getDamager();
			if(player.getItemInHand().getType() == Material.DIAMOND_AXE) {
				if(Cooldown.isCooling(player.getName(), "velocityscythe")) {
					Cooldown.coolDurMessage(player, "velocityscythe");
					return;
				}
				Player tP = (Player) event.getEntity();
				World w = player.getServer().getWorld("world");
				tP.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
				w.playEffect(tP.getLocation(), Effect.SMOKE, 40, 40);
			}
			if(event.getEntity() instanceof Player) {
				if(utilServer.isInNewSpawn(player)) {
					event.setCancelled(true);
					return;
				}
				if(Shops.isInShops(player)) {
					if(CTag.isTagged(player.getName())) {
						event.setCancelled(false);
						return;
					} else {
						event.setCancelled(true);
					}
					if(event.getEntity() instanceof Player) {
						event.setCancelled(true);
						return;
					} else if(!(event.getEntity() instanceof Player)) {
						event.setCancelled(true);
						return;
					} else if(event.getDamager() instanceof Player) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.DARK_GREEN + "Shops - " + ChatColor.WHITE + "You cannot damage players in shops");
						return;
					}
				}
			}
		}
		if(!(event.getDamager() instanceof Player) || (!(event.getEntity() instanceof Player))) {
			return;
		} else if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player damaged = (Player) event.getEntity();
			if(RCommands.god.contains(damaged.getName())) {
				event.setCancelled(true);
			}
		}
	}
//	
//	@EventHandler
//	public void damage(EntityDamageEvent event) {
//		if(!(event.getEntity() instanceof Player)) {
//			return;
//		}
//		if(event.getEntity() instanceof Player) {
//			Player player = (Player) event.getEntity();
//			if(Shops.isInShops(player)) { event.setCancelled(true); return; }
//			if(cTagged.containsKey(player.getName())) {
//				return;
//			} else {
//				} else if(!Arena.isPlaying(player) && !Spleef.isSpleefing(player) && !ArcticBrawl.isArcting(player)) {
//					long currTime = System.currentTimeMillis();
//					cTagged.put(player.getName(), currTime);
//					while(combatTagged(player) == true) {
//						Long taggedTime = cTagged.get(player.getName());
//						Long curTime = System.currentTimeMillis();
//						if((curTime - taggedTime < 60000)) {
//							return;
//						} else {
//							cTagged.remove(player.getName());
//							player.sendMessage(ChatColor.GRAY + "You are now" + ChatColor.YELLOW + " untagged");
//						}
//					}
//				}
//			}
//			if(RCommands.god.contains(player.getName())) {
//				event.setCancelled(true);
//			} else if(Arena.inLobby) {
//				if(Arena.getTeam(player) == ArenaTeams.BLUE || Arena.getTeam(player) == ArenaTeams.RED) {
//					event.setCancelled(true);
//				}
//			}
//		}
//	}
//	
//	public static boolean combatTagged(Player player) {
//			return (cTagged.containsKey(player.getName()));
//	}
}
