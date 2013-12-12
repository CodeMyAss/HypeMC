package me.loogeh.Hype;

import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilWorld;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Door;

@SuppressWarnings("deprecation")
public class IronDoor implements Listener {
	public static Hype plugin;
	
	public IronDoor(Hype instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onIronDoor(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		boolean admin = utilPlayer.adminMode(player);
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		try {
		Block blockY = event.getClickedBlock().getRelative(0, -1, 0);
		Block block = event.getClickedBlock();
		if(event.getClickedBlock().getType() == Material.IRON_DOOR_BLOCK) {
			if(Squads.isClaimed(utilWorld.chunkToStr(block.getChunk()))) {
				if(!Squads.getOwnerChunk(block.getChunk()).equalsIgnoreCase(Squads.getSquad(player.getName())) && !admin) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot interact in " + ChatColor.YELLOW + Squads.getOwnerChunk(block.getChunk()));
					player.getWorld().playSound(block.getLocation(), Sound.ZOMBIE_WOOD, 10.0F, 0.1F);
					return;
				}
			}
			BlockState state = block.getState();
			Door door = (Door) state.getData();
			if(door.isOpen()) door.setOpen(false);
			else door.setOpen(true);
			state.update();
				
		}
		if(blockY.getType() == Material.IRON_DOOR_BLOCK) {
			if(Squads.isClaimed(utilWorld.chunkToStr(block.getChunk()))) {
				if(!Squads.getOwnerChunk(block.getChunk()).equalsIgnoreCase(Squads.getSquad(player.getName())) && !admin) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot interact in " + ChatColor.YELLOW + Squads.getOwnerChunk(block.getChunk()));
					player.getWorld().playSound(blockY.getLocation(), Sound.IRONGOLEM_HIT, 10.0F, 1.0F);
					return;
				}
			}
			BlockState state = blockY.getState();
			Door door = (Door) state.getData();
			if(door.isOpen()) door.setOpen(false);
			else door.setOpen(true);
			state.update();
			}
		} catch(Exception e) {
			return;
		}
	}

}
