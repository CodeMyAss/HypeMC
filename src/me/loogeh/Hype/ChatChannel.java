package me.loogeh.Hype;

import java.util.ArrayList;
import java.util.HashMap;

import me.loogeh.Hype.util.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatChannel {

	private String name; //ID used to join
	private HashMap<String, Boolean> members = new HashMap<String, Boolean>();
	private boolean open; //Whether or not any player can join
	private ArrayList<String> invitees = new ArrayList<String>();
	private String creator; //Admin of the chat
	
	public static String[] disallowed_names = {"create", "leave", "join", "squad", "games", "current"};
	
	public ChatChannel(String name, String creator, boolean open) {
		this.name = name;
		this.creator = creator;
		this.open = open;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCreator() {
		return this.creator;
	}
	
	public HashMap<String, Boolean> getMembers() {
		return this.members;
	}
	
	public ArrayList<String> getInvitees() {
		return this.invitees;
	}
	
	public boolean getOpen() {
		return this.open;
	}
	
	public void message(String message) {
		for(String member : members.keySet()) {
			Player player = Bukkit.getPlayer(member);
			if(player != null) {
				player.sendMessage(ChatColor.DARK_AQUA + getName() + ChatColor.AQUA + " " + message);
			}
		}
	}
	
	public void message(Player caller, String message) {
		for(String member : members.keySet()) {
			Player player = Bukkit.getPlayer(member);
			if(player != null) {
				player.sendMessage(ChatColor.DARK_AQUA + getName() + ChatColor.AQUA + " " + message);
			}
		}
	}
	
	public void kick(Player caller, String player) {
		if(!members.containsKey(player)) {
			caller.sendMessage(ChatColor.DARK_AQUA + getName() + ChatColor.AQUA + " " + player + ChatColor.WHITE + " is not in your chat");
			return;
		}
		members.remove(player);
		message(player + ChatColor.WHITE + " was kicked from " + getName());
		Player target = Bukkit.getPlayer(player);
		if(target != null) {
			M.v(target, "Chat", "You were kicked from " + ChatColor.LIGHT_PURPLE + getName() + ChatColor.WHITE + " by " + ChatColor.YELLOW + caller.getName());
		}
	}
	
	public void invite(Player caller, Player player) {
		if(player == null) {
			caller.sendMessage(ChatColor.RED + "Server - " + ChatColor.WHITE + "Invalid player");
			return;
		}
		if(getMembers().containsKey(player.getName())) {
			M.v(caller, "Chat", ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " is already in " + ChatColor.LIGHT_PURPLE + getName());
			return;
		}
		invitees.add(player.getName());
	}
	
	public void join(Player player) {
		if(player == null) return;
		if(getMembers().containsKey(player)) {
			M.v(player, "Chat", ChatColor.WHITE + "You are already in " + ChatColor.YELLOW + getName());
			return;
		}
		if(!invitees.contains(player.getName())) {
			M.v(player, "Chat", ChatColor.WHITE + "You are not invited to " + ChatColor.YELLOW + getName());
			return;
		}
		if(hasChannel(player.getName())) {
			M.v(player, "Chat", ChatColor.WHITE + "You are already in " + ChatColor.YELLOW + get(player.getName()).getName());
			return;
		}
		members.put(player.getName(), true);
		message(player.getName() + ChatColor.WHITE + " joined");
	}
	
	public static boolean inChannel(String channel, String player) {
		if(!InfoHolder.chats.containsKey(channel)) return false;
		if(InfoHolder.chats.get(channel).members.containsKey(player)) return true;
		else return false;
	}
	
	public static boolean hasChannel(String player) {
		for(String channels : InfoHolder.chats.keySet()) {
			if(InfoHolder.chats.get(channels).members.containsKey(player)) return true;
		}
		return false;
	}
	
	public void toggle(String player) {
		if(!hasChannel(player)) return;
		Player chatter = Bukkit.getPlayer(player);
		if(chatter == null) return;
		ChatChannel channel = get(player);
		if(channel.getMembers().get(player) == false) {
			channel.getMembers().put(player, true);
			chatter.sendMessage(ChatColor.DARK_AQUA + "Chat Mode - " + ChatColor.WHITE + "Channel");
			return;
		}
		channel.getMembers().put(player, false);
		chatter.sendMessage(ChatColor.DARK_AQUA + "Chat Mode - " + ChatColor.WHITE + "Global");
	}
	
	public static ChatChannel get(String player) {
		for(String channels : InfoHolder.chats.keySet()) {
			if(InfoHolder.chats.get(channels).members.containsKey(player)) {
				if(InfoHolder.chats.get(channels).members.get(player) == true) {
					return InfoHolder.chats.get(channels);
				}
			}
		}
		return null;
	}
	
	public static ChatChannel getByName(String channel) {
		if(InfoHolder.chats.containsKey(channel)) return InfoHolder.chats.get(channel);
		return null;
	}
	
	public String getMembersStr() {
		String members = "";
		for(String member : getMembers().keySet()) {
			if(getMembers().get(member) == false) {
				members = members + ChatColor.RED + "*" + ChatColor.GRAY + member + ", ";
			} else {
				members = members + ChatColor.GRAY + member + ", ";
			}
		}
		return Utilities.removeLastIntChars(members, 2);
	}
	
	public static boolean allowed(String name) {
		for(int i = 0; i < disallowed_names.length; i++) {
			if(disallowed_names[i].equalsIgnoreCase(name)) return false;
		}
		return true;
	}
	
	public int getSize() {
		return members.size();
	}
	
	public void delete() {
		message(ChatColor.RESET + "Chat has ended");
		InfoHolder.chats.remove(getName());
	}
}
