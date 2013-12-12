package me.loogeh.Hype.Squads;

import org.bukkit.Location;

public class Region
{
	private String world;
	private int lowX;
	private int lowY;
	private int lowZ;
	private int highX;
	private int highY;
	private int highZ;

	public Region(String world, int lowX, int lowY, int lowZ, int highX, int highY, int highZ)
	{
		this.world = world;
		this.lowX = lowX;
		this.lowY = lowY;
		this.lowZ = lowZ;
		this.highX = highX;
		this.highY = highY;
		this.highZ = highZ;
	}

	public boolean contains(Location loc)
	{
		return (this.world.equals(loc.getWorld().getName())) && 
				(loc.getX() > this.lowX) && 
				(loc.getX() < this.highX) && 
				(loc.getY() > this.lowY) && 
				(loc.getY() < this.highY) && 
				(loc.getZ() > this.lowZ) && 
				(loc.getZ() < this.highZ);
	}
}