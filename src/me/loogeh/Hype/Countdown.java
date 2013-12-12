package me.loogeh.Hype;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown {
	public static Hype plugin = Hype.plugin;
	
	private String name;
	private int seconds;
	private int begin_seconds;
	private CountdownType type;
	private Countdown countdown;
	private Player player;
	private boolean toCancel = false;
	
	public Countdown(String name, int seconds, CountdownType type, final Player player) {
		this.name = name;
		this.seconds = seconds;
		this.begin_seconds = seconds;
		this.countdown = this;
		this.player = player;
		plugin.getServer().getPluginManager().callEvent(new CountdownStartEvent(countdown));
		new BukkitRunnable() {
			int seconds = getSeconds();
			@Override
			public void run() {
				if(toCancel) {
					this.cancel(); 
					return;
				}
				if(player == null) Bukkit.broadcastMessage("Countdown" + seconds);
				else player.sendMessage("Test " + seconds);
				seconds--;
				if(seconds == 0) {
					plugin.getServer().getPluginManager().callEvent(new CountdownCompleteEvent(countdown));
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 20, 20);
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSeconds() {
		return this.seconds;
	}
	
	public int getBeginSeconds() {
		return this.begin_seconds;
	}
	
	public CountdownType getType() {
		return this.type;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void cancel() {
		toCancel = true;
	}
	
	public enum CountdownType {
		PRIVATE,
		BROADCAST;
	}
	
	
	class CountdownCompleteEvent extends Event {
		
		private HandlerList handlers = new HandlerList();
		
		private Countdown countdown;
		
		public CountdownCompleteEvent(Countdown countdown) {
			this.countdown = countdown;
		}
		
		public HandlerList getHandlerList() {
			return handlers;
		}

		public HandlerList getHandlers() {
			return handlers;
		}
		
		public Countdown getCountdown() {
			return this.countdown;
		}
		
	}

	class CountdownStartEvent extends Event {
		
		private HandlerList handlers = new HandlerList();

		private Countdown countdown;
		
		public CountdownStartEvent(Countdown countdown) {
			this.countdown = countdown;
		}
		
		public HandlerList getHandlerList() {
			return handlers;
		}

		public HandlerList getHandlers() {
			return handlers;
		}
		
		public Countdown getCountdown() {
			return this.countdown;
		}
	}
}

