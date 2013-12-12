package me.loogeh.Hype;

public class Charge {
	
	private String name;
	private int limit;
	private int level;
	private long time;
	private long lastRun;
	private boolean used;
	
	public Charge(String name, int limit, long lastRun) {
		this.name = name;
		this.limit = limit;
		this.level = 0;
		this.used = false;
		this.time = System.currentTimeMillis();
		this.lastRun = lastRun;
	}
	
	public String getName() {
		return this.name;
	}
	
	public long getInit() {
		return this.time;
	}
	
	public long getLastRun() {
		return this.lastRun;
	}
	
	public void setLastRun(long millis) {
		this.lastRun = millis;
	}
	
	public int getLimit() {
		return this.limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void addLevel() {
		this.level++;
	}
	
	public boolean isUsed() {
		return this.used;
	}
	
	public void setUsed(boolean used) {
		this.used = used;
	}
	

}
