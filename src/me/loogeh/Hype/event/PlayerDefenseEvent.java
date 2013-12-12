package me.loogeh.Hype.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerDefenseEvent extends Event {
	private static HandlerList handlers = new HandlerList();

	private Player player;
	private ItemStack item;
	
	public PlayerDefenseEvent(Player player, ItemStack item) {
		this.player = player;
		this.item = item;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public ItemStack getItem() {
		return this.item;
	}
	
	

}
