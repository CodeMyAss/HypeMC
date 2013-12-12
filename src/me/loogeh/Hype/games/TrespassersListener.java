package me.loogeh.Hype.games;

import me.loogeh.Hype.Hype;

import org.bukkit.Material;
import org.bukkit.event.Listener;

public class TrespassersListener implements Listener{
	public static Hype plugin;
	public static Material[] possibleItems = {Material.WOOD_SWORD, Material.APPLE, Material.WEB, Material.MELON, Material.BOW, Material.ARROW, Material.STONE_AXE, Material.WOOD_AXE,
		Material.PORK, Material.COOKED_BEEF, Material.RAW_FISH, Material.RAW_CHICKEN, Material.FISHING_ROD};
	public static int[] itemAmounts = {1, 1, 1, 2, 5, 1, 2, 2, 1, 2, 3, 1};
	public TrespassersListener(Hype instance) {
		plugin = instance;
	}
	
//	@EventHandler
//	public void onTrespassersInteract(PlayerInteractEvent event) {
//		Player player = event.getPlayer();
//		if(event.getClickedBlock().getType() != Material.CHEST) {
//			return;
//		}
//		if(!player.getName().equalsIgnoreCase("Loogeh")) {
//			return;
//		}
//		Chest chest = (Chest) event.getClickedBlock().getState();
//		int rand = utilMath.getRandom(0, 11);
//		for(int i = 3; i < 9; i++) {
//			chest.getInventory().addItem(new ItemStack(possibleItems[utilMath.getRandom(0, 11)], rand));
//		}
//	}

}
