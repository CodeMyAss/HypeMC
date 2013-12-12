package me.loogeh.Hype.Squads;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.Squads.hSquad.Rank;
import me.loogeh.Hype.util.Utilities;
import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SquadCommands implements CommandExecutor {
	public static Hype plugin;
	public static String[] baseCmds = {"s", "sq", "squad", "squads", "f", "factions", "faction"};
	public static String[] Cmds = {"create", "claim", "unclaim", "disband", "join", "leave", "kick", "demote", "promote", "invite", "home", "list", "delete", "i", "desc", "admin", "tp", 
		"ally", "trust", "enemy", "view", "display", "v"};

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("s") 
				|| commandLabel.equalsIgnoreCase("squad") 
				|| commandLabel.equalsIgnoreCase("sq")
				|| commandLabel.equalsIgnoreCase("squads") 
				|| commandLabel.equalsIgnoreCase("f") 
				|| commandLabel.equalsIgnoreCase("faction")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Squad commands cannot be used from console");
				return true;
			} else if(sender instanceof Player) {
				Player player = (Player) sender;
				if(args.length == 0) {
					if(!Squads.hasSquad(player)) {
						player.sendMessage(ChatColor.GOLD + "Squad: " + ChatColor.GRAY + "You aren't in a squad");
					} else {
						Squads.sendSquadInfo(Squads.getCurrentSquad(player).name, player);
					}
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("help")) {
						if(!Permissions.isAdmin(player)) {
							player.sendMessage(ChatColor.GOLD + "Squad help " + ChatColor.GRAY + " ---------" + ChatColor.YELLOW + "Squad Rank");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.GRAY + " (squad) -" + ChatColor.GREEN + " M");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " create" + " <name> ");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " claim" + ChatColor.GREEN + " A");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " disband" + ChatColor.GREEN + " L");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " join <squad>");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " leave");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " promote <player>" + ChatColor.GREEN + " L");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " demote <player>" + ChatColor.GREEN + " L");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " invite <player>" + ChatColor.GREEN + " A");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " desc <description>" + ChatColor.GREEN + " L");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " kick <player>");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.GRAY + " (set) " + ChatColor.WHITE +"home");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " list");
						} else {
							player.sendMessage(ChatColor.GOLD + "Squad help " + ChatColor.GRAY + " ---------" + ChatColor.YELLOW + "Squad Rank");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.GRAY + " (squad) -" + ChatColor.GREEN + " M");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " create" + " <name> ");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " claim" + ChatColor.GREEN + " A");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " disband" + ChatColor.GREEN + " L");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " join <squad>");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " leave");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " promote <player>" + ChatColor.GREEN + " L");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " demote <player>" + ChatColor.GREEN + " L");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " invite <player>" + ChatColor.GREEN + " A");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " desc <description>" + ChatColor.GREEN + " L");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " kick <player>");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.GRAY + " (set) " + ChatColor.WHITE +"home");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " list");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " admin");
							player.sendMessage(ChatColor.GOLD + "/s" + ChatColor.WHITE + " tp <squad>");
						}
					} else if(args[0].equalsIgnoreCase("disband") || (args[0].equalsIgnoreCase("delete"))) {
						if(!Squads.hasSquad(player)) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.GRAY + "You aren't in a Squad");
							return true;
						}
						if(Squads.getCurrentSquad(player).rank != Rank.LEADER) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.GRAY + "Only the leader can peform this command");
							return true;
						}
						
					} else if(args[0].equalsIgnoreCase("leave")) {
						
					} else if(args[0].equalsIgnoreCase("claim")) {
						
					} else if(args[0].equalsIgnoreCase("home")) {
						
					} else if(args[0].equalsIgnoreCase("list")) {
						
					} else if(args[0].equalsIgnoreCase("admin")) {
						
					} else if(args[0].equalsIgnoreCase("promote")) {
						
					} else if(args[0].equalsIgnoreCase("demote")) {
						
					} else if(args[0].equalsIgnoreCase("invite") || (args[0].equalsIgnoreCase("i"))) {
						
					} else if(args[0].equalsIgnoreCase("kick")) {
						
					} else if(args[0].equalsIgnoreCase("ally")) {
					
					} else if(args[0].equalsIgnoreCase("trust")) {
						
					} else if(args[0].equalsIgnoreCase("enemy")) {
						
					} //else if(args[0].equalsIgnoreCase("display") || (args[0].equalsIgnoreCase("view") || (args[0].equalsIgnoreCase("view"))))
				} else if(args.length == 2) {
					if(args[0].equalsIgnoreCase("create")) {
						if(Squads.hasSquad(player)) {
							player.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "You're already in a Squad");
							return true;
						}
						String name = args[1];
						for(String cmds : Cmds) {
							if(name.equals(cmds)) {
								player.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "Name cannot be a Squad command");
								return true;
							}
						}
						for(String squads : Squads.squadMap.keySet()) {
							if(squads.equalsIgnoreCase(name)) {
								player.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "Squad already exists");
								return true;
							}
						}
						if(Utilities.containsSpecialChar(name)) {
							player.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "Squad name cannot contain " + ChatColor.YELLOW + "Special Characters");
							return true;
						} else {
							if(name.length() < 3 || name.length() > 10) {
								player.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "Name has too few/many characters");
								return true;
							}
							Squads.createSquad(name, player, false);
						}
					}
				} else if(args.length == 3) {
					if(args[0].equalsIgnoreCase("create")) {
						if(Permissions.getRank(player) != Permissions.Ranks.OWNER) {
							utilPlayer.permMsg(player);
							return true;
						} else {
							if(Squads.hasSquad(player)) {
								player.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "You're already in a Squad");
								return true;
							}
							String name = args[1];
							for(String cmds : Cmds) {
								if(name.equals(cmds)) {
									player.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "Name cannot be a Squad command");
									return true;
								}
							}
							for(String squads : Squads.squadMap.keySet()) {
								if(squads.equalsIgnoreCase(name)) {
									player.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "Squad already exists");
									return true;
								}
							}
							if(name.length() < 3 || name.length() > 10) {
								player.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "Name has too few/many characters");
								return true;
							} else {
								if(!args[2].equalsIgnoreCase("true")) {
									player.sendMessage(ChatColor.GOLD + "Squads: " + ChatColor.GRAY + "Incorrect arguments");
									return true;
								}
								Squads.createSquad(name, player, true);
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static void leave(Player player, String[] args) {
		hSquad squad = utilSquads.getSquadByPlayer(player);
		if(squad == null) {
			player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
			return;
		}
	}


}
