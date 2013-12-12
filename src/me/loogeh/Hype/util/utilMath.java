package me.loogeh.Hype.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import me.loogeh.Hype.Hype;


public class utilMath {
	public static Hype plugin;
	public static Random rand = new Random();
	 
	public static int getRandom(int min, int max) {
	    return rand.nextInt(max - min) + min;
	}
	
	public static double round(double unrounded, int precision) {
		String format = "#.#";
		
		for(int i = 1; i < precision; i++) {
			format = format + "#";
		}
		DecimalFormat twoDec = new DecimalFormat(format);
		return Double.valueOf(twoDec.format(unrounded)).doubleValue();
	}
	
	
	public List<Location> getSphere(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<Location>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        Location l = new Location(loc.getWorld(), x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
}
