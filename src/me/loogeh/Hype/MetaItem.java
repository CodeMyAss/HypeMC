package me.loogeh.Hype;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MetaItem extends ItemStack {
	private String name;
	private String disp_name;
	private ItemStack item;
	private List<String> lore;
	
	public MetaItem(String name, String disp_name, ItemStack item, List<String> lore) {
		this.name = name;
		this.disp_name = disp_name;
		this.item = item;
		ItemMeta meta = this.item.getItemMeta();
		meta.setLore(lore);
		meta.setDisplayName(disp_name);
		this.item.setItemMeta(meta);
	}
	
	public MetaItem(String name, ItemStack item, List<String> lore) {
		this.name = name;
		this.disp_name = name;
		this.item = item;
		ItemMeta meta = this.item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		this.item.setItemMeta(meta);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDisplayName() {
		return this.disp_name;
	}
	
	public ItemStack getItem() {
		return this.item;
	}
	
	public int getAmount() {
		return this.item.getAmount();
	}
	
	public short getMIData() { // MI = MetaItem
		return this.item.getDurability();
	}
	
	public List<String> getLore() {
		return this.lore;
	}
	
	public String getLore(int index) {
		return this.lore.get(index);
	}
	
	public Material getMaterial() {
		return this.item.getType();
	}
}
