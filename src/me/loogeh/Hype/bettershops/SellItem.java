package me.loogeh.Hype.bettershops;

import me.loogeh.Hype.bettershops.BetterShops.ItemType;

public class SellItem {
	
	public int sell_cost;
	public String alternate_name;
	public ItemType type;
	
	public SellItem(int sell_cost, String alternate_name, ItemType type) {
		this.sell_cost = sell_cost;
		this.alternate_name = alternate_name;
		this.type = type;
	}
	
	public SellItem(int sell_cost, ItemType type) {
		this.sell_cost = sell_cost;
		this.type = type;
	}
	
	
	public int getSellCost() {
		return this.sell_cost;
	}
	
	public String getAlternate() {
		return this.alternate_name;
	}
	
	public ItemType getType() {
		return this.type;
	}
}