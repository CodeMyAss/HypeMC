package me.loogeh.Hype.fields;

import org.bukkit.Location;
import org.bukkit.Material;

public class FieldBlock {
	
	private Material material;
	private long last_destroy;
	private Location location;
	private int least_drops;
	private int most_drops;
	
	public FieldBlock(Material material, long last_destroy, Location location, int least_drops, int most_drops) {
		this.material = material;
		this.last_destroy = last_destroy;
		this.location = location;
		this.least_drops = least_drops;
		this.most_drops = most_drops;
	}
	
	public Material getType() {
		return this.material;
	}
	
	public long getLastDestroy() {
		return last_destroy;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public int getLeastDrops() {
		return this.least_drops;
	}
	
	public int getMostDrops() {
		return this.most_drops;
	}

}
