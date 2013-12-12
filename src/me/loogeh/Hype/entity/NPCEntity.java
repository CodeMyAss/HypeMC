package me.loogeh.Hype.entity;

import net.minecraft.server.v1_6_R3.*;

import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;

public class NPCEntity extends EntityPlayer {

	private int lastTargetId;
	private long lastBounceTick;
	private int lastBounceId;

	public NPCEntity(NPCManager npcManager, BWorld world, String s, PlayerInteractManager itemInWorldManager) {
		super(npcManager.getServer().getMCServer(), world.getWorldServer(), s, itemInWorldManager);

		itemInWorldManager.b(EnumGamemode.SURVIVAL);

		playerConnection = new NPCPlayerConnection(npcManager, this);
		lastTargetId = -1;
		lastBounceId = -1;
		lastBounceTick = 0;
		noDamageTicks = Integer.MAX_VALUE;
		fauxSleeping = true;
	}
	

	@Override
	public String getName() {
		return name;
	}

	public void setBukkitEntity(org.bukkit.entity.Entity entity) {
		bukkitEntity = (CraftEntity) entity;
	}

	@Override
	public boolean a(EntityHuman entity) {
		return super.a(entity);
	}

	@Override
	public void b_(EntityHuman entity) {
		if ((lastBounceId != entity.id || System.currentTimeMillis() - lastBounceTick > 1000) && entity.getBukkitEntity().getLocation().distanceSquared(getBukkitEntity().getLocation()) <= 1) {

			lastBounceTick = System.currentTimeMillis();
			lastBounceId = entity.id;
		}

		if (lastTargetId == -1 || lastTargetId != entity.id) {
			lastTargetId = entity.id;
		}

		super.b_(entity);
	}

	@Override
	public void c(Entity entity) {
		if (lastBounceId != entity.id || System.currentTimeMillis() - lastBounceTick > 1000) {
			lastBounceTick = System.currentTimeMillis();
		}

		lastBounceId = entity.id;

		super.c(entity);
	}

	@Override
	public void move(double arg0, double arg1, double arg2) {
		setPosition(arg0, arg1, arg2);
	}
}