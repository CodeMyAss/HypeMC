package me.loogeh.Hype.event;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SquadTerritoryEnterEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private Player player;
	private String fromSquad;
	private String toSquad;
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public SquadTerritoryEnterEvent(Player player, String fromSquad, String toSquad) {
		this.player = player;
		this.fromSquad = fromSquad;
		this.toSquad = toSquad;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public String getFrom() {
		return this.fromSquad;
	}
	
	public String getTo() {
		return this.toSquad;
	}
	
}
