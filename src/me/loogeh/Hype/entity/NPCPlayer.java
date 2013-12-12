package me.loogeh.Hype.entity;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_6_R3.ControllerJump;
import net.minecraft.server.v1_6_R3.ControllerLook;
import net.minecraft.server.v1_6_R3.ControllerMove;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.EntitySenses;
import net.minecraft.server.v1_6_R3.EnumGamemode;
import net.minecraft.server.v1_6_R3.GenericAttributes;
import net.minecraft.server.v1_6_R3.MinecraftServer;
import net.minecraft.server.v1_6_R3.PlayerInteractManager;
import net.minecraft.server.v1_6_R3.World;

public class NPCPlayer extends EntityPlayer {
	
	protected ControllerJump c_jump;
	protected ControllerLook c_look;
	protected ControllerMove c_move;
	protected EntitySenses e_senses;
	
	public NPCPlayer(MinecraftServer minecraftServer, World world, String s, PlayerInteractManager interactionManager) {
		super(minecraftServer, world, s, interactionManager);
//		this.aW().b(GenericAttributes.b).setValue(16);
		this.aX().b(GenericAttributes.b).setValue(16);
		interactionManager.setGameMode(EnumGamemode.CREATIVE);
		this.noDamageTicks = Integer.MAX_VALUE;
		this.Y = 1;
		this.fauxSleeping = true;
	}
	
	
	public Inventory getInventory() {
		return this.getBukkitEntity().getInventory();
	}
	
	public void setItemInHand(ItemStack item) {
		setItemInHand(item);
	}
	
	@Override
	public void l_() {
		this.yaw = this.az;
		super.l_();
		this.setInvisible(false);
	}
}
