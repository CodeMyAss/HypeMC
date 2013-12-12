package me.loogeh.Hype.util;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.loogeh.Hype.Hype;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

public class Utilities {
	public static Hype plugin;
	public static HashMap<String, Integer> EntityList = new HashMap<String, Integer>();
	

	public static String join(int start, String[] array) {
		String string = "";
		for(int i = start; i < array.length; i++) {
			string = string + array[i];
		}
		return string;
	}
	
	public static String argsToString(String[] stringarray) {
		String str = "";
		for (int i = 0; i < stringarray.length; i++) {
			str = str + " " + stringarray[i];
		}
		return str;
	}
	public static String argsToString2(String[] stringarray) {
		String str = "";
		for (int i = 1; i < stringarray.length; i++) {
			str = str + " " + stringarray[i];
		}
		return str;
	}

	public static String argsToString3(String[] stringarray) {
		String str = "";
		for (int i = 2; i < stringarray.length; i++) {
			str = str + " " + stringarray[i];
		}
		return str;
	}

	public static Integer stringToInt(String str) {
		int num = -1;
		try {
			num = Integer.valueOf(str);
		} catch (NumberFormatException n) {
			return null;
		}
		return num;

	}

	public static boolean hasArmorType(ItemStack item, Material type) {
		return (item == null ? false : item.getType() == type);
	}

	public static String getTime() {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		//System.out.println(sdf.format(cal.getTime()));
		return sdf.format(cal.getTime());
	}

	public static String removeLastChar(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		return s.substring(0, s.length()-1);
	}
	public static TreeSet<String> sortKey(Set<String> toSort) {
		TreeSet<String> sortedSet = new TreeSet<String>();
		for (String cur : toSort) {
			sortedSet.add(cur);
		}
		return sortedSet;
	}
	
	public static String removeLastIntChars(String s, int amount) {
		if (s == null || s.length() == 0) {
			return s;
		}
		return s.substring(0, s.length() - amount);
	}
	public static TreeSet<String> sortKey1(Set<String> toSort) {
		TreeSet<String> sortedSet = new TreeSet<String>();
		for (String cur : toSort) {
			sortedSet.add(cur);
		}
		return sortedSet;
	}

	public static boolean containsSpecialChar(String input) {
		Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		if(matcher.find()) return true;
		else return false;
	}

	public static String capitalizeFirst(String input) {
		input = input.substring(0, 1).toUpperCase() + input.substring(1);
		return input;
	}
	
	public static boolean isPlural(String input) {
		char[] charArray = input.toCharArray();
		if(charArray[charArray.length] == 's' && charArray[charArray.length - 1] != 's') return true;
		return false;
	}

	public static boolean stringEqualsFromArray(String inputString, String[] items)
	{
		for(int i =0; i < items.length; i++)
		{
			if(inputString.toLowerCase().equalsIgnoreCase(items[i].toLowerCase()))
			{
				return true;
			}
		}
		return false;
	}
	
	
	public static void skipRespawn(Player player) {
		PacketContainer packet = new PacketContainer(Packets.Client.CLIENT_COMMAND);
		packet.getIntegers().write(0, 1);
		 
		try {
			ProtocolLibrary.getProtocolManager().recieveClientPacket(player, packet);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isInteger(String toParse) {
		try {
			Integer.parseInt(toParse);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static List<Integer> getIntList(String toList) {
		if(!toList.contains(",")) return null;
		String[] split = toList.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for(String args : split) {
			try {
				int i = Integer.parseInt(args);
				list.add(i);
			} catch(NumberFormatException e) {
				return null;
			}
		}
		return list;
	}
}
