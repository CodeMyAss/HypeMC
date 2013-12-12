package me.loogeh.Hype;

import java.util.HashMap;
import java.util.UUID;

import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.util.utilInv;
import me.loogeh.Hype.util.utilServer;
import me.loogeh.Hype.util.utilWorld;

import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class SpecialItems implements Listener {
	public static Hype plugin = Hype.plugin;
	public SpecialItems(Hype instance) {
		plugin = instance;
	}
	public static HashMap<String, UUID> web = new HashMap<String, UUID>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void projHit(ProjectileHitEvent event) {
		final Projectile proj = event.getEntity();
		if(!(proj instanceof Arrow)) return;
		Arrow arrow = (Arrow) proj;
		if(web.containsValue(arrow.getUniqueId())) {
			web.remove(arrow.getUniqueId());
			for(int x = (int) arrow.getLocation().getX(); x < arrow.getLocation().getX() + 1; x++) {
				for(int y = (int) arrow.getLocation().getY(); y < arrow.getLocation().getY() + 1; y++) {
					for(int z = (int) arrow.getLocation().getZ(); z < arrow.getLocation().getZ() + 1; z++) {
						final int xx = x;
						final int yy = y;
						final int zz = z;
						final Location bLoc = new Location(proj.getWorld(), xx, yy, zz);
						if(bLoc.getBlock().getTypeId() == 0 || bLoc.getBlock().getTypeId() == 31 || bLoc.getBlock().getTypeId() == 32) {
							bLoc.getBlock().setType(Material.WEB);
							arrow.remove();
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

								@Override
								public void run() {
									bLoc.getBlock().setType(Material.AIR);
								}
							}, 80L);
						}
					}
				}
			}
		}
		if(arrow.hasMetadata("blaze_shell")) {
			for(int x = (int) arrow.getLocation().getX(); x < arrow.getLocation().getX() + 1; x++) {
				for(int z = (int) arrow.getLocation().getZ(); z < arrow.getLocation().getZ() + 1; z++) {
					final Location bLoc = new Location(proj.getWorld(), x, arrow.getLocation().getY(), z);
					if(Squads.isClaimed(utilWorld.chunkToStr(bLoc.getChunk()))) return;
					if(bLoc.getBlock().getTypeId() == 0 || bLoc.getBlock().getTypeId() == 31 || bLoc.getBlock().getTypeId() == 32) {
						bLoc.getBlock().setType(Material.FIRE);
						arrow.remove();
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

							@Override
							public void run() {
								bLoc.getBlock().setType(Material.AIR);
							}
						}, 80L);
					}
				}
			}
		}
	}

	@EventHandler
	public void onSpecialItemInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if(utilServer.isInNewSpawn(player.getLocation())) return;
		if(event.getAction() == Action.RIGHT_CLICK_AIR) {
			if(player.getItemInHand().getType() == Material.WEB) {
				utilInv.subtractAmount(player);
				Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getEyeLocation().getY() + 0.9, player.getLocation().getZ());
				Arrow arrow = player.getWorld().spawnArrow(loc, player.getLocation().getDirection().multiply(1.3F), 1.3F, 0.0F);
				UUID id = arrow.getUniqueId();
				web.put(player.getName(), id);
				arrow.getWorld().playSound(arrow.getLocation(), Sound.CLICK, 0.5F, 0.5F);
				player.sendMessage(ChatColor.GRAY + "You shot a" + ChatColor.GREEN + " Web Blanket");
			}
			if(player.getItemInHand().getType() == Material.MAGMA_CREAM) {
				utilInv.subtractAmount(player);
				Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getEyeLocation().getY() + 0.9, player.getLocation().getZ());
				Arrow arrow = player.getWorld().spawnArrow(loc, player.getLocation().getDirection().multiply(1.3F), 1.3F, 0.0F);
				arrow.getWorld().playSound(arrow.getLocation(), Sound.CLICK, 0.5F, 0.5F);
				arrow.setMetadata("blaze_shell", new FixedMetadataValue(plugin, Boolean.valueOf(true)));
				M.v(player, "Special", ChatColor.WHITE + "You shot a " + ChatColor.LIGHT_PURPLE + "Blaze Shell");
			}
		}
		if(player.getItemInHand().getType() == Material.FIREWORK) {
			ItemStack firework = player.getItemInHand();
			FireworkMeta meta = (FireworkMeta) firework.getItemMeta();
			meta.addEffect(FireworkEffect.builder().trail(false).flicker(false).build());
			meta.setPower(10);
			firework.setItemMeta(meta);
		}
		if(player.getItemInHand().getType() == Material.DIAMOND_HOE) {
			if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;
			short durability = player.getItemInHand().getDurability();
			if(Cooldown.isCooling(player.getName(), "lightning Sceptre")) {
				Cooldown.coolDurMessage(player, "lightning Sceptre");
				return;
			}
			player.getItemInHand().setDurability((short) (durability + 77));
			Cooldown.add(player.getName(), "lightning Sceptre", 14, System.currentTimeMillis());
			@SuppressWarnings("deprecation")
			Location location = player.getTargetBlock(null, 50).getLocation();
			player.getWorld().strikeLightning(location);
		}
	}
}
