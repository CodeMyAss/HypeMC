package me.loogeh.Hype;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Ignore {
	public static Hype plugin;
	
	public static HashMap<String, String> ignores = new HashMap<String, String>();
	
	
	public static void add(String player, String ignored) {
		if(isIgnored(player, ignored)) return;
		ignores.put(player, ignored);
	}
	
	public static void remove(String player, String ignored) {
		if(!isIgnored(player, ignored)) return;
		Iterator<String> it = ignores.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			if(key.equalsIgnoreCase(player)) {
				if(ignores.get(key).equalsIgnoreCase(ignored)) {
					it.remove();
				}
			}
		}
	}
	public static boolean isIgnored(String player, String ignored) {
		if(!ignores.containsKey(player) || !ignores.containsValue(ignored)) return false;
		for(Entry<String, String> entry : ignores.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(player)) {
				if(entry.getValue().equalsIgnoreCase(ignored)) {
					return true;
				}
			}
		}
		return false;
	}
}
