package me.loogeh.Hype;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.util.Utilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HInv {
	
	public static String itemToStr(ItemStack item, int slot) {
		String format = item.getType().toString() + "/" + item.getAmount() + "/" + slot;
		return format;
		
	}
	
	public static String invToStr(Inventory inv) {
		String formatted = "";
		for(int i = 0; i < inv.getSize(); i++) {
			if(inv.getItem(i) != null) {
				formatted = formatted + itemToStr(inv.getItem(i), i) + ", ";
			}
		}
		return formatted;
	}
	
	public static String armourToStr(PlayerInventory inv) {
		String contents = "";
		for(ItemStack armour : inv.getArmorContents()) {
			if(armour.getType().toString().contains("HELMET")) contents = contents + itemToStr(armour, 5) + ", ";
			if(armour.getType().toString().contains("CHESTPLATE")) contents = contents + itemToStr(armour, 6) + ", ";
			if(armour.getType().toString().contains("LEGGINGS")) contents = contents + itemToStr(armour, 7) + ", ";
			if(armour.getType().toString().contains("BOOTS")) contents = contents + itemToStr(armour, 8) + ", ";
		}
		return contents;
	}
	
	public static InvItem strToItem(String item) {
		String[] tokens = item.split("/");
		if(!Utilities.isInteger(tokens[1]) || !Utilities.isInteger(tokens[2])) return null;
		int amount = Integer.parseInt(tokens[1]);
		int slot = Integer.parseInt(tokens[2]);
		ItemStack stack = new ItemStack(Material.getMaterial(tokens[0]), amount);
		return new InvItem(stack, slot);
	}
	
	public static Inventory parseInv(Player player, String contents, String armour) {
		PlayerInventory inv = player.getInventory();
		String[] tokens = contents.split(", ");
		for(int i = 0; i < tokens.length; i++) {;
			InvItem item = strToItem(tokens[i]);
			inv.setItem(item.getSlot(), item.getItemStack());
		}
		String[] atokens = armour.split(", ");
		for(int i = 0; i < atokens.length; i++) {
			InvItem item = strToItem(atokens[i]);
			if(atokens[i].contains("HELMET")) inv.setHelmet(item.getItemStack());
			else if(atokens[i].contains("CHEST")) inv.setChestplate(item.getItemStack());
			else if(atokens[i].contains("LEG")) inv.setLeggings(item.getItemStack());
			else if(atokens[i].contains("BOOTS")) inv.setLeggings(item.getItemStack());
		}
		return inv;
	}
	
	public static String[] getInventory(String player, String name) {
		ResultSet rs = MySQL.doQuery("SELECT contents, armour FROM stored_inventories WHERE name='" + player + "_" + name + "'");
		String[] contents = new String[1];
		try {
			if(rs.next()) {
				contents[0] = rs.getString(1);
				contents[1] = rs.getString(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void storeInventory(String name, String contents, String armour) {
		MySQL.doUpdate("INSERT INTO `stored_inventories`(`name`, `contents`, `armour`) VALUES ('" + name + "','" + contents + "', '" + armour + "')");
	}
	
	public static void sendContents(Player player, ItemStack[] contents) {
		if(player == null) return;
		String contentsStr = "";
		for(ItemStack items : contents) {
			if(items != null) {
				contentsStr = items.toString() + " ";
			}
		}
		player.sendMessage(contentsStr);
	}
	
	static class InvItem {
		
		private ItemStack item;
		private int slot;
		
		public InvItem(ItemStack item, int slot) {
			this.item = item;
			this.slot = slot;
		}
		
		public ItemStack getItemStack() {
			return this.item;
		}
		
		public int getSlot() {
			return this.slot;
		}
	}
}
