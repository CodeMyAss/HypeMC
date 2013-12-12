package me.loogeh.Hype.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerArmourEvent extends Event {

	
	private static HandlerList handlers = new HandlerList();

	//NAKED WITH SPEED II
	
	Player player;
	
	public PlayerArmourEvent(Player player) {
		this.player = player;
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
}
