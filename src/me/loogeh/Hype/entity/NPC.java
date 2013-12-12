package me.loogeh.Hype.entity;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.loogeh.Hype.armour.Armour.Kit;
import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.ItemStack;
import net.minecraft.server.v1_6_R3.Packet5EntityEquipment;

public class NPC {

	private Entity entity;

	public NPC(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

	public void removeFromWorld() {
		try {
			entity.world.removeEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updatePackets() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			for(int i = 0; i < 5; i++) {
				if(getEntity().getEquipment()[i] != null) {
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(new Packet5EntityEquipment(getBukkitEntity().getEntityId(), i, getEntity().getEquipment()[i]));
				}
			}
		}
	}
	
	public void updatePackets(int i) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(new Packet5EntityEquipment(getBukkitEntity().getEntityId(), i, getEntity().getEquipment()[i]));
		}
	}

	public org.bukkit.entity.Entity getBukkitEntity() {
		return entity.getBukkitEntity();
	}

	public void moveTo(Location l) {
		getBukkitEntity().teleport(l);
	}
	
	public void setEquipment(int i, ItemStack item) {
		getEntity().setEquipment(i, item);
	}
	
	public CraftPlayer getCraftPlayer() {
		try {
			return (CraftPlayer) getBukkitEntity();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void addPotionEffect(PotionEffect effect) {
		getCraftPlayer().addPotionEffect(effect);
	}
	
//	public void updatePackets() {
//		Packet5EntityEquipment packet;
//		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
//			for(int i = 0; i < 5; i++) {
//				((CraftPlayer) player).getHandle().playerConnection.sendPacket(new Packet5EntityEquipment(getBukkitEntity().getEntityId(), i, (ItemStack) getCraftPlayer().getInventory().getItem(i)));
//			}
//		}
//	}
	
	public void setGameMode(GameMode gamemode) {
		setGameMode(gamemode);
	}
	
	public void setKit(Kit kit) {
		getCraftPlayer().getInventory().clear();
		if(kit == Kit.ARCHER) {
			getCraftPlayer().getInventory().setHelmet(new org.bukkit.inventory.ItemStack(Material.LEATHER_HELMET));
			getCraftPlayer().getInventory().setChestplate(new org.bukkit.inventory.ItemStack(Material.LEATHER_CHESTPLATE));
			getCraftPlayer().getInventory().setLeggings(new org.bukkit.inventory.ItemStack(Material.LEATHER_LEGGINGS));
			getCraftPlayer().getInventory().setBoots(new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS));
			getCraftPlayer().getInventory().setItem(0, new org.bukkit.inventory.ItemStack(Material.IRON_SWORD));
			for(int i = 1; i < 6; i++) getCraftPlayer().getInventory().setItem(i, new org.bukkit.inventory.ItemStack(Material.MUSHROOM_SOUP));
			getCraftPlayer().getInventory().setItem(6, new org.bukkit.inventory.ItemStack(Material.IRON_AXE));
			getCraftPlayer().getInventory().setItem(7, new org.bukkit.inventory.ItemStack(Material.COOKED_BEEF, 8));
			getCraftPlayer().getInventory().setItem(8, new org.bukkit.inventory.ItemStack(Material.BOW));
			getCraftPlayer().getInventory().setItem(9, new org.bukkit.inventory.ItemStack(Material.ARROW, 24));
		}
		if(kit == Kit.AGILITY) {
			getCraftPlayer().getInventory().setHelmet(new org.bukkit.inventory.ItemStack(Material.GOLD_HELMET));
			getCraftPlayer().getInventory().setChestplate(new org.bukkit.inventory.ItemStack(Material.GOLD_CHESTPLATE));
			getCraftPlayer().getInventory().setLeggings(new org.bukkit.inventory.ItemStack(Material.GOLD_LEGGINGS));
			getCraftPlayer().getInventory().setBoots(new org.bukkit.inventory.ItemStack(Material.GOLD_BOOTS));
			getCraftPlayer().getInventory().setItem(0, new org.bukkit.inventory.ItemStack(Material.IRON_SWORD));
			for(int i = 1; i < 7; i++) getCraftPlayer().getInventory().setItem(i, new org.bukkit.inventory.ItemStack(Material.MUSHROOM_SOUP));
			getCraftPlayer().getInventory().setItem(7, new org.bukkit.inventory.ItemStack(Material.GOLD_AXE));
			getCraftPlayer().getInventory().setItem(8, new org.bukkit.inventory.ItemStack(Material.COOKED_BEEF, 8));
		}
		if(kit == Kit.SPECIALIST) {
			getCraftPlayer().getInventory().setHelmet(new org.bukkit.inventory.ItemStack(Material.CHAINMAIL_HELMET));
			getCraftPlayer().getInventory().setChestplate(new org.bukkit.inventory.ItemStack(Material.CHAINMAIL_CHESTPLATE));
			getCraftPlayer().getInventory().setLeggings(new org.bukkit.inventory.ItemStack(Material.CHAINMAIL_LEGGINGS));
			getCraftPlayer().getInventory().setBoots(new org.bukkit.inventory.ItemStack(Material.CHAINMAIL_BOOTS));
			getCraftPlayer().getInventory().setItem(0, new org.bukkit.inventory.ItemStack(Material.IRON_SWORD));
			for(int i = 1; i < 7; i++) getCraftPlayer().getInventory().setItem(i, new org.bukkit.inventory.ItemStack(Material.MUSHROOM_SOUP));
			getCraftPlayer().getInventory().setItem(7, new org.bukkit.inventory.ItemStack(Material.IRON_AXE));
			getCraftPlayer().getInventory().setItem(8, new org.bukkit.inventory.ItemStack(Material.COOKED_BEEF, 8));
		}
		if(kit == Kit.SAMURAI) {
			getCraftPlayer().getInventory().setHelmet(new org.bukkit.inventory.ItemStack(Material.IRON_HELMET));
			getCraftPlayer().getInventory().setChestplate(new org.bukkit.inventory.ItemStack(Material.IRON_CHESTPLATE));
			getCraftPlayer().getInventory().setLeggings(new org.bukkit.inventory.ItemStack(Material.IRON_LEGGINGS));
			getCraftPlayer().getInventory().setBoots(new org.bukkit.inventory.ItemStack(Material.IRON_BOOTS));
			getCraftPlayer().getInventory().setItem(0, new org.bukkit.inventory.ItemStack(Material.IRON_SWORD));
			for(int i = 1; i < 7; i++) getCraftPlayer().getInventory().setItem(i, new org.bukkit.inventory.ItemStack(Material.MUSHROOM_SOUP));
			getCraftPlayer().getInventory().setItem(7, new org.bukkit.inventory.ItemStack(Material.IRON_AXE));
			getCraftPlayer().getInventory().setItem(8, new org.bukkit.inventory.ItemStack(Material.COOKED_BEEF, 8));
		}
		if(kit == Kit.JUGGERNAUT) {
			getCraftPlayer().getInventory().setHelmet(new org.bukkit.inventory.ItemStack(Material.DIAMOND_HELMET));
			getCraftPlayer().getInventory().setChestplate(new org.bukkit.inventory.ItemStack(Material.DIAMOND_CHESTPLATE));
			getCraftPlayer().getInventory().setLeggings(new org.bukkit.inventory.ItemStack(Material.DIAMOND_LEGGINGS));
			getCraftPlayer().getInventory().setBoots(new org.bukkit.inventory.ItemStack(Material.DIAMOND_BOOTS));
			getCraftPlayer().getInventory().setItem(0, new org.bukkit.inventory.ItemStack(Material.IRON_SWORD));
			for(int i = 1; i < 6; i++) getCraftPlayer().getInventory().setItem(i, new org.bukkit.inventory.ItemStack(Material.MUSHROOM_SOUP));
			getCraftPlayer().getInventory().setItem(6, new org.bukkit.inventory.ItemStack(Material.IRON_AXE));
			getCraftPlayer().getInventory().setItem(7, new org.bukkit.inventory.ItemStack(Material.COOKED_BEEF, 8));
			getCraftPlayer().getInventory().setItem(8, new org.bukkit.inventory.ItemStack(Material.BOW));
			getCraftPlayer().getInventory().setItem(9, new org.bukkit.inventory.ItemStack(Material.ARROW, 24));
		}
	}
	
	public boolean removePotionEffect(PotionEffectType type) {
		if(getCraftPlayer().hasPotionEffect(type)) {
			getCraftPlayer().removePotionEffect(type);
			return true;
		}
		else return false;
	}
}
