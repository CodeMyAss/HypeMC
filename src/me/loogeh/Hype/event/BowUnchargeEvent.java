package me.loogeh.Hype.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BowUnchargeEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();

	private Player player;
	private UnchargeReason reason;
	
	public BowUnchargeEvent(Player player, UnchargeReason reason) {
		this.player = player;
		this.reason = reason;
	}
	
	public HandlerList getHandlerList() {
		return handlers;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public UnchargeReason getReason() {
		return this.reason;
	}
	
	public static enum UnchargeReason {
		ITEM_CHANGE,
		PLAYER_LEAVE,
		SHOOT,
		TELEPORT;
		
	}
}
