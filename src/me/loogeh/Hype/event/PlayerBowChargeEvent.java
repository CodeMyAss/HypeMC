package me.loogeh.Hype.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBowChargeEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	private Player player;
	private int stage = 0;
	
	public PlayerBowChargeEvent(Player player, int stage) {
		this.player = player;
		this.stage = stage;
	}

	public HandlerList getHandlers() {
		return handlers;
	}
	
	public HandlerList getHandlerList() {
		return handlers;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public int getStage() {
		return this.stage;
	}
	
}
