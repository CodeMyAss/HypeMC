package me.loogeh.Hype.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerNakedEvent extends Event {

	
	private static HandlerList handlers = new HandlerList();

	Player player;
	
	public PlayerNakedEvent(Player player) {
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
