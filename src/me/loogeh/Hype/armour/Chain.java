package me.loogeh.Hype.armour;

import java.util.ArrayList;
import java.util.HashMap;

import me.loogeh.Hype.Cooldown;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.util.utilArmour;
import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Chain implements Listener{
	public static Hype plugin;
	public static ArrayList<String> beserker = new ArrayList<String>();
	public static ArrayList<String> beserkercool = new ArrayList<String>();
	public static HashMap<String, Long> shirk = new HashMap<String, Long>();// IF BLOCKING GIVE PLAYER SPEED II 6 SECONDS
	public Chain(Hype instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void damage(EntityDamageEvent event) {
	if(event.getEntity() instanceof Player) {
		Player player = (Player) event.getEntity();
		if(!utilArmour.hasSet(player, "CHAIN")) {
			return;
		} else {
			if(event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		Action a = event.getAction();
		final Player player = event.getPlayer();
		if((a == Action.RIGHT_CLICK_AIR) && (player.getItemInHand().getType() == Material.IRON_AXE || player.getItemInHand().getType() == Material.DIAMOND_AXE) && (utilArmour.hasSet(player, "CHAIN"))) {
			if(Cooldown.isCooling(player.getName(), "beserker")) {
				Cooldown.coolDurMessage(player, "beserker");
				return;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0));
			utilPlayer.abilityMessage(player, "Beserker");
			Cooldown.add(player.getName(), "beserker", 20, System.currentTimeMillis());
		}
	}
	
	@EventHandler
	public void hunger(FoodLevelChangeEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(utilArmour.hasSet(player, "CHAIN")) event.setFoodLevel(20);
	}
}
