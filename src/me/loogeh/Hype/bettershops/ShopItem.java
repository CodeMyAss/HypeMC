package me.loogeh.Hype.bettershops;

import me.loogeh.Hype.bettershops.BetterShops.ItemType;
import me.loogeh.Hype.bettershops.BetterShops.ShopType;

import org.bukkit.Material;

public class ShopItem {

	private Material mat;
	private String item;
	private int id;
	private String alt;
	private int price;
	private ShopType type;
	private ItemType iType;
	
	
	public ShopItem(Material mat, String item, int id, String alt, int price, ShopType type, ItemType iType) {
		this.mat = mat;
		this.item = item;
		this.id = id;
		this.alt = alt;
		this.price = price;
		this.type = type;
		this.iType = iType;
	}
	
	public Material getMaterial() {
		return this.mat;
	}
	
	public String getItem() {
		return this.item;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getAlternative() {
		return this.alt;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public ShopType getType() {
		return this.type;
	}
	
	public ItemType getIType() {
		return this.iType;
	}
}
