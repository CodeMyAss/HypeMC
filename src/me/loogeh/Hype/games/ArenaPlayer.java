package me.loogeh.Hype.games;

import me.loogeh.Hype.armour.Armour.Kit;

public class ArenaPlayer {
	
	private int deaths;
	private Kit kit;
	private long archer;
	private long agility;
	private long specialist;
	private long samurai;
	private long jugg;
	private int archer_kills;
	private int agility_kills;
	private int spec_kills;
	private int samurai_kills;
	private int jugg_kills;
	
	
	
	public ArenaPlayer(int deaths, long archer, long agility, long specialist, long samurai, long jugg, int archer_kills, int agility_kills, int spec_kills, int samurai_kills, int jugg_kills) {
		this.deaths = deaths;
		this.kit = Kit.NONE;
		this.archer = archer;
		this.agility = agility;
		this.specialist = specialist;
		this.samurai = samurai;
		this.jugg = jugg;
		this.archer_kills = archer_kills;
		this.agility_kills = agility_kills;
		this.spec_kills = spec_kills;
		this.samurai_kills = samurai_kills;
		this.jugg_kills = jugg_kills;
	}
	
	public int getDeaths() {
		return this.deaths;
	}
	
	public Kit getKit() {
		return this.kit;
	}
	
	public long getArcherTime() {
		return this.archer;
	}
	
	public long getAgilityTime() {
		return this.agility;
	}
	
	public long getSpecialistTime() {
		return this.specialist;
	}
	
	public long getSamuraiTime() {
		return this.samurai;
	}
	
	public long getJuggTime() {
		return this.jugg;
	}
	
	public int getArcherKills() {
		return this.archer_kills;
	}
	
	public int getAgilityKills() {
		return this.agility_kills;
	}
	
	public int getSpecialistKills() {
		return this.spec_kills;
	}
	
	public int getSamuraiKills() {
		return this.samurai_kills;
	}
	
	public int getJuggKills() {
		return this.jugg_kills;
	}
	
	public long[] getTimeArray() {
		long[] array = {getArcherTime(), getAgilityTime(), getSpecialistTime(), getSamuraiTime(), getJuggTime()};
		return array;
	}
	
	public int[] getKillsArray() {
		int[] array = {getAgilityKills(), getAgilityKills(), getSpecialistKills(), getSamuraiKills(), getJuggKills()};
		return array;
	}
	
	public Kit getMostUsed() {
		long[] time = getTimeArray();
		long last = 0;
		Kit kit = Kit.NONE;
		for(int i = 0; i < time.length; i++) {
			if(time[i] > last) last = time[i];
			kit = Kit.getKitByID(i);
		}
		return kit;
	}
	
	public double getMostUsedPercent() {
		long total = getTotalTime();
		long most = getTimeByKit(getMostUsed());
		double percent = (most / total) * 100;
		return percent;
	}
	
	public long getTimeByKit(Kit kit) {
		if(kit == Kit.ARCHER) return getArcherTime();
		else if(kit == Kit.AGILITY) return getAgilityTime();
		else if(kit == Kit.SPECIALIST) return getSpecialistTime();
		else if(kit == Kit.SAMURAI) return getSamuraiTime();
		else if(kit == Kit.JUGGERNAUT) return getJuggTime();
		else return 0L;
	}
	
	public Kit getMostKills() {
		int[] kills = getKillsArray();
		int last = 0;
		Kit kit = Kit.NONE;
		for(int i = 0; i < kills.length; i++) {
			if(kills[i] > last) last = kills[i];
					kit = Kit.getKitByID(i);
		}
		return kit;
	}
	
	public long getTotalTime() {
		return getArcherTime() + getAgilityTime() + getSpecialistTime() + getSamuraiTime() + getJuggTime();
	}
	
	public int getTotalKills() {
		return getArcherKills() + getAgilityKills() + getSpecialistKills() + getSamuraiKills() + getJuggKills();
	}
	
	public double getMostKillsPercent() {
		return 0.0;
	}
}
