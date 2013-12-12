package me.loogeh.Hype;

import java.util.List;

import me.loogeh.Hype.util.utilWorld;

import org.bukkit.entity.Player;

public class Effects {
	public static Hype plugin = Hype.plugin;
	
	public static void start(final Player player) {
		final EffectPlayer effect = InfoHolder.effects.get(player.getName());
		final int interval = 1000 / effect.getSpeed();
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				if((System.currentTimeMillis() - effect.getLastRun()) > 1000 / interval) {
					if(effect.getEffects().contains(EffectType.ORBITAL)) {
						for(int i = 0; i < 360; i++) {
							double x = Math.cos(i);
							double z = Math.sin(i);
							utilWorld.blockParticleEffect(1, player.getWorld(), player.getLocation().getX() + x, player.getEyeHeight(), player.getLocation().getZ() + z);
						}
					}
				}
			}
		}, 1L, 1L);
	}
	
	public class EffectPlayer {
		
		private int speed;
		private List<Integer> blocks;
		private List<EffectType> effects;
		private long lastRun;
		
		public EffectPlayer(int speed, List<Integer> blocks, List<EffectType> effects) {
			this.speed = speed;
			this.blocks = blocks;
			this.effects = effects;
			this.lastRun = System.currentTimeMillis();
		}
		
		public int getSpeed() {
			return this.speed;
		}
		
		public List<Integer> getBlocks() {
			return this.blocks;
		}
		
		public List<EffectType> getEffects() {
			return this.effects;
		}
		
		public long getLastRun() {
			return this.lastRun;
		}
	}
	
	public enum EffectType {
		ORBITAL(0),
		yORBITAL(1);
		
		private int id;
		
		EffectType(int id) {
			this.id = id;
		}
		
		public int getID() {
			return this.id;
		}
		
		public static EffectType getType(int id) {
			if(id == 0) return ORBITAL;
			else if(id == 1) return yORBITAL;
			else return null;
		}
	}

}
