package me.loogeh.Hype.donations;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.donations.Morphs.MorphType;
import me.loogeh.Hype.economy.Money;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DonationListener implements Listener {
	public static Hype plugin;
	public DonationListener(Hype instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void death(PlayerDeathEvent event) {
		if(Morphs.isMorphed(event.getEntity().getPlayer())) {
			if(Morphs.morphList.get(event.getEntity().getPlayer()) == MorphType.PIG) {
				Money.subtractMoney(event.getEntity().getPlayer(), 1000);
			} else if(Morphs.morphList.get(event.getEntity().getPlayer()) == MorphType.SQUID) {
				Money.subtractMoney(event.getEntity().getPlayer(), 1500);
			} else if(Morphs.morphList.get(event.getEntity().getPlayer()) == MorphType.COW) {
				Money.subtractMoney(event.getEntity().getPlayer(), 2000);
			} else if(Morphs.morphList.get(event.getEntity().getPlayer()) == MorphType.IRON_GOLEM) {
				Money.subtractMoney(event.getEntity().getPlayer(), 6000);
			} else if(Morphs.morphList.get(event.getEntity().getPlayer()) == MorphType.CAVE_SPIDER) {
				Money.subtractMoney(event.getEntity().getPlayer(), 3500);
			}
		}
	}

}
