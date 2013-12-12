package me.loogeh.Hype;

import java.util.HashMap;

import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teleport implements CommandExecutor {
	public static Hype plugin;
	public static HashMap<String, Location> backLoc = new HashMap<String, Location>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("tp")) {
			if (Permissions.isAdmin(player)) {
				if(args.length == 0) {
					player.sendMessage(ChatColor.DARK_GREEN + "/tp" + ChatColor.WHITE + " <player>" + ChatColor.GRAY + " (all)");
					player.sendMessage(ChatColor.DARK_GREEN + "/tp" + ChatColor.WHITE + " h" + ChatColor.GRAY + " <player>");
					player.sendMessage(ChatColor.DARK_GREEN + "/tp" + ChatColor.WHITE + " <player> <player>");
					player.sendMessage(ChatColor.DARK_GREEN + "/tp" + ChatColor.WHITE + " <int, int, int>");
					player.sendMessage(ChatColor.DARK_GREEN + "/tp" + ChatColor.WHITE + " spawn");
					player.sendMessage(ChatColor.DARK_GREEN + "/tp" + ChatColor.WHITE + " back");
					return true;
				} else if(args.length == 2) {
					Player tp = Bukkit.getServer().getPlayer(args[0]);
					Player tp2 = Bukkit.getServer().getPlayer(args[1]);
					utilPlayer.tpplayertoplayer(sender, args);
					player.sendMessage(ChatColor.GRAY + "You teleported " + ChatColor.GREEN + tp.getName() + ChatColor.GRAY + " to " + ChatColor.GREEN + tp2.getName());
					return true;
				} else if((args.length == 1) && (args[0].equalsIgnoreCase("all"))) {
					utilPlayer.tpall(player);
					Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ChatColor.GRAY + " teleported all players to him");
					return true;
				} else if((args.length == 1) && (args[0].equalsIgnoreCase("h"))) {
					Player tp = Bukkit.getServer().getPlayer(args[0]);
					utilPlayer.tph(player, player.getServer().getPlayer(args[1]));
					player.sendMessage(ChatColor.GRAY + "You teleported " + ChatColor.GREEN + tp.getName() + ChatColor.GRAY + " here");
					return true;
				} else if((args.length == 1) && args[0].equalsIgnoreCase("spawn")) {
					utilPlayer.tpToSpawn(player);
				} else if((args.length == 1) && (args[0].equalsIgnoreCase("back"))) {
					if(!backLoc.containsKey(player.getName())) {
						Location loc = backLoc.get(player.getName());
						player.teleport(loc);
						player.sendMessage(ChatColor.DARK_PURPLE + "Teleport: " + ChatColor.GRAY + "You teleported to your last " + ChatColor.YELLOW + "Death Location");
						return true;
					} else {
						player.sendMessage(ChatColor.DARK_PURPLE + "Teleport: " + ChatColor.GRAY + "You haven't died");
						return true;
					}
				} else if(args.length == 1) {
					utilPlayer.tptp(player, args);
					//		player.sendMessage(ChatColor.GRAY + "You teleported to " + ChatColor.GREEN + player.getServer().getPlayer(args[0]));
				} else if(args.length == 3) {
					utilPlayer.tpCoords(args, sender, true);
				} else if((args.length == 2) && (args[0].equalsIgnoreCase("world") || (args[0].equalsIgnoreCase("w")))) {
					Player tplayer = player.getServer().getPlayer(args[1]);
					World wn = player.getServer().getWorld(args[2]);
					utilPlayer.tpToWorld(player, args, tplayer, wn);
				}
			} else if(!Permissions.isAdmin(player)) {
				utilPlayer.permMsg(player);
			}
		}
		if(label.equalsIgnoreCase("warp")) {
			if(!Permissions.isAdmin(player)) {
				utilPlayer.permMsg(player);
				return true;
			} else {
				if(args.length == 0) {
					player.sendMessage(ChatColor.DARK_PURPLE + "/warp" + ChatColor.WHITE + " <name>");
					player.sendMessage(ChatColor.DARK_PURPLE + "/warp" + ChatColor.WHITE + " list");
					player.sendMessage(ChatColor.DARK_PURPLE + "/warp" + ChatColor.WHITE + " details" + ChatColor.GRAY +" <name>");
					player.sendMessage(ChatColor.DARK_PURPLE + "/warp" + ChatColor.WHITE + " set " + ChatColor.GRAY + "(int x, int y, int z)");
					player.sendMessage(ChatColor.DARK_PURPLE + "/warp" + ChatColor.WHITE + " create <name>");
					player.sendMessage(ChatColor.DARK_PURPLE + "/warp" + ChatColor.WHITE + " del <name>");
					return true;
				}
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("list")) {
						Warp.sendWarpList(player);
						return true;
					} else {
						String warp = args[0].toLowerCase();
						Warp.warp(player, warp);
					}
				}
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("create")) {
						String name = args[1].toLowerCase();
						Warp.saveWarp(player, name);
					} else if(args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")) {
						String name = args[1].toLowerCase();
						Warp.delWarp(player, name);
					} else if(args[0].equalsIgnoreCase("details") || args[0].equalsIgnoreCase("info")) {
						String name = args[1].toLowerCase();
						Warp.sendWarpDetails(player, name);
					} else if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("reset")) {
						String name = args[1].toLowerCase();
						Warp.resetWarp(player, name, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw());
					}
				}
				if(args.length == 6) {
					if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("reset")) {
						String name = args[1].toLowerCase();
						String[] coords = args[2].split(",");
						float yaw = Float.parseFloat(args[3]);
						if(args[1] == null || args[2] == null || args[3] == null || args[3] == null || args[4] == null || args[5] == null) {
							player.sendMessage(ChatColor.DARK_PURPLE + "/warp " + ChatColor.WHITE + "set <x,y,z> yaw");
						}
						Warp.resetWarp(player, name, Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), yaw);
					}
				}
			}
		}
		return false;
	}

}
