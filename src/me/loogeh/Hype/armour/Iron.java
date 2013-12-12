package me.loogeh.Hype.armour;

import java.util.List;

import me.loogeh.Hype.Cooldown;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.util.utilArmour;
import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Iron implements Listener{
	public static Hype plugin;
	public Iron(Hype instace) {
		plugin = instace;
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		Action at = event.getAction();
		if(at == Action.RIGHT_CLICK_BLOCK) {
			if(utilArmour.hasSet(player, "IRON")) {
				if(utilPlayer.hasSwords(player)) {
					if(Cooldown.isCooling(player.getName(), "shockwave")) {
						Cooldown.coolDurMessage(player, "shockwave");
						return;
				}
					utilPlayer.abilityMessage(player, "Shockwave");
					Cooldown.add(player.getName(), "shockwave", 16, System.currentTimeMillis());
					List<Entity> ents = player.getNearbyEntities(7, 7, 7);
					for(Entity e : ents) {
						if(e instanceof Player) {
							Player p = (Player) e;
							if(!Squads.getSquad(p.getName()).equalsIgnoreCase(Squads.getSquad(player.getName())) && !Squads.areAllies(Squads.getSquad(p.getName()), Squads.getSquad(player.getName()))) {
								e.setVelocity(e.getLocation().getDirection().multiply(-2.0));
								p.sendMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " used " + ChatColor.AQUA + "Shockwave");
							}
						}
					}
					ents.clear();
					}
				}
			}
//		if(at == Action.RIGHT_CLICK_AIR) {
//			if(!utilArmour.hasSet(player, "IRON")) return;
//			if(player.getItemInHand().getType() == Material.DIAMOND_AXE || player.getItemInHand().getType() == Material.IRON_AXE) {
//				if(Cooldown.isCooling(player.getName(), "Fade")) {
//					Cooldown.coolDurMessage(player, "Fade");
//					return;
//				}
//				utilPlayer.abilityMessage(player, "Fade");
//				Cooldown.add(player.getName(), "Fade", 16, System.currentTimeMillis());
//				utilPlayer.fade(player);
//				for(int i = 0; i < 10; i++) player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0, 1);
//				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//					
//					public void run() {
//						utilPlayer.fade(player);
//						player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0, 1);
//					}
//				}, 80L);
//			}
//		}
	}
}
