package me.loogeh.Hype.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.server.v1_6_R3.BiomeBase;
import net.minecraft.server.v1_6_R3.BiomeMeta;
import net.minecraft.server.v1_6_R3.EntityTypes;
import net.minecraft.server.v1_6_R3.EntityZombie;
import net.minecraft.server.v1_6_R3.World;

public class ZombieMod extends EntityZombie {
	
	private boolean frozen = true;
	
	public ZombieMod(World world) {
		super(world);
	}
	
	public static void registerEntities() {
		for(ZombieEntityType zombie : ZombieEntityType.values()) {
			try {
				Method a = EntityTypes.class.getDeclaredMethod("a", new Class<?>[]{ Class.class, String.class, int.class });
				a.setAccessible(true);
				a.invoke(null, zombie.getCustomClass(), zombie.getName(), zombie.getID());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		for(BiomeBase biomeBase : BiomeBase.biomes) {
			if(biomeBase == null) break;
			for(String field : new String[] {"K", "J", "L", "M" }) {
				try {
					Field list = BiomeBase.class.getDeclaredField(field);
					list.setAccessible(true);
					@SuppressWarnings("unchecked")
					List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
					for(BiomeMeta meta : mobList) {
						for(ZombieEntityType zombie : ZombieEntityType.values()) {
							if(zombie.getNMSClass().equals(meta.b)) {
								meta.b = zombie.getCustomClass();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void bj() {
		if(!frozen) {
			super.bj();
		}
	}

}
