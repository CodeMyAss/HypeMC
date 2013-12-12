package me.loogeh.Hype.Squads2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import me.loogeh.Hype.Handlers;
import me.loogeh.Hype.Hype;
import me.loogeh.Hype.M;
import me.loogeh.Hype.RCommands;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.SQL.Permissions.Ranks;
import me.loogeh.Hype.Squads.hSquad.Rank;
import me.loogeh.Hype.Squads2.Squads.SquadType;
import me.loogeh.Hype.util.Utilities;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilServer;
import me.loogeh.Hype.util.utilWorld;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SquadsCommands implements CommandExecutor {
	public static Hype plugin;
	public static int MAX_LENGTH = 10;
	public static int MIN_LENGTH = 3;
	private HashSet<String> password_cooldown = new HashSet<String>();

	public static String[] Cmds = {"create", "claim", "unclaim", "disband", "join", "leave", "kick", "demote", "promote", "invite", "home", "list", "delete", "i", "desc", "admin", "tp", 
		"ally", "trust", "enemy", "view", "display", "v", "Nether", "Nether2", "Spawn", "Shops", "Arena", "Games", "set", "money", "chat", "load", "save", "wilderness", "wildernes", "wildernness", "wildernesss", "wildurness"
		, "neutral", "neut", "chat"};

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Squads commands cannot be used from console");
			return true;
		}
		Player player = (Player) sender;
		if(commandLabel.equalsIgnoreCase("squads") ||
				commandLabel.equalsIgnoreCase("s") ||
				commandLabel.equalsIgnoreCase("squad") ||
				commandLabel.equalsIgnoreCase("factions") ||
				commandLabel.equalsIgnoreCase("f") ||
				commandLabel.equalsIgnoreCase("faction")) {
			if(args.length == 0) {
				if(!Squads.hasSquad(player.getName())) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
					return true;
				}
				String pSquad = Squads.getSquad(player.getName());
				Squads.sendSquadDetails(player, pSquad);
				return true;
			}
			if(args.length == 1) {
				String pSquad = Squads.getSquad(player.getName());
				if(args[0].equalsIgnoreCase("leave")) {
					if(Squads.getRank(player.getName()) == Rank.LEADER) {
						if(Squads.getMemberSize(pSquad) > 1) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must pass " + ChatColor.YELLOW + "Leadership " + ChatColor.WHITE + "before leaving");
							return true;
						}
					}
					if(Squads.getMemberSize(pSquad) == 1) {
						if(Squads.squadMap.get(pSquad).admin) {
							Squads.leave(player.getName(), pSquad);
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You left " + ChatColor.YELLOW + pSquad);
							return true;
						}
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must disband instead");
						return true;
					}
					if(Squads.getLand(pSquad) >= Squads.getMaxLand(pSquad)) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must unclaim a chunk to leave");
						return false;
					}
					Squads.leave(player.getName(), pSquad);
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You left " + ChatColor.YELLOW + pSquad);
					Squads.sendMembersMsg(pSquad, ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " left your Squad");
					return true;
				}
				if(args[0].equalsIgnoreCase("disband")) {
					if(!Squads.hasSquad(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
						return true;
					}
					if(Squads.getRank(player.getName()) != Rank.LEADER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be the " + ChatColor.YELLOW + "Leader " + ChatColor.WHITE + "to disband");
						return true;
					}
					Squads.disband(player, pSquad);
					Bukkit.broadcastMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " disbanded " + ChatColor.YELLOW + pSquad);
				}
				if(args[0].equalsIgnoreCase("home")) {
					if(!Squads.hasSquad(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
						return true;
					}
					if(!utilServer.isInNewSpawn(player)) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be in the " + ChatColor.YELLOW + "Spawn Centre");
						return true;
					}
					if(Handlers.last_respawn.containsKey(player.getName())) {
						if((System.currentTimeMillis() - Handlers.last_respawn.get(player.getName()) > 120000)) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + "2 Minutes " + ChatColor.WHITE + "has passed since you respawned");
							return true;
						}
					}
					if(!Handlers.last_respawn.containsKey(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You haven't " + ChatColor.YELLOW + "Respawned " + ChatColor.WHITE + "before");
						return true;
					}
					Squads.goHome(player, pSquad);

				}
				if(args[0].equalsIgnoreCase("create")) {
					M.message(player, M.mType.HELP, "/s create <name>", ChatColor.GRAY + "Creates a new Squad");
					return true;
				}
				if(args[0].equalsIgnoreCase("help")) {
					Squads.sendHelpMenu(player, 1);
					return true;
				}
				if(args[0].equalsIgnoreCase("claim")) {
					String chunk = utilWorld.chunkToStr(player.getLocation().getChunk());
					if(!Squads.hasSquad(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
						return true;
					}
					if(player.getWorld().getEnvironment() != Environment.NORMAL) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You can only claim in the " + ChatColor.YELLOW + "Overworld");
						return true;
					}
					if(Squads.getLand(pSquad) >= Squads.getMaxLand(pSquad)) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You have no more " + ChatColor.YELLOW + "Available Land");
						return true;
					}
					if(utilServer.isInSpawn(player.getLocation()) && Squads.getType(pSquad) != SquadType.ADMIN) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot claim in " + ChatColor.YELLOW + "Spawn");
						return true;
					}
					if(Squads.getRank(player.getName()) != Rank.ADMIN && Squads.getRank(player.getName()) != Rank.LEADER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be atleast an " + ChatColor.YELLOW + "Admin " + ChatColor.WHITE + "to claim");
						return true;
					}
					if(Squads.isClaimed(chunk)) {
						String owner = Squads.getOwner(chunk);
						if(Squads.getPower(owner) < 1) {
							Squads.stealLand(pSquad, owner, player.getLocation().getChunk());
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You stole land from " + ChatColor.YELLOW + owner);
							return true;
						}
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Chunk already claimed by " + ChatColor.YELLOW + Squads.getOwner(chunk));
						return true;
					}
					if(Squads.getType(pSquad) == SquadType.ADMIN) {
						Squads.claim(pSquad, chunk, true);
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Admin chunk claimed");
						return true;
					}
					Squads.claim(pSquad, chunk, false);
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Chunk claimed");
					return true;
				}
				if(args[0].equalsIgnoreCase("unclaim") || args[0].equalsIgnoreCase("uc")) {
					String chunk = utilWorld.chunkToStr(player.getLocation().getChunk());
					if(!Squads.hasSquad(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
						return true;
					}
					if(!Squads.isClaimed(chunk)) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Chunk is not claimed");
						return true;
					}
					if(!Squads.getOwner(chunk).equals(pSquad)) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You don't own this chunk");
						return true;
					}
					if(Squads.getRank(player.getName()) != Rank.ADMIN && Squads.getRank(player.getName()) != Rank.LEADER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be atleast an " + ChatColor.YELLOW + "Admin " + ChatColor.WHITE + "to claim");
						return true;
					}
					if(Squads.getType(pSquad) == SquadType.ADMIN) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Admin chunk unclaimed");
						Squads.unclaim(pSquad, chunk);
						return true;
					}
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Chunk unclaimed");
					Squads.unclaim(pSquad, chunk);
					return true;
				}
				if(args[0].equalsIgnoreCase("list")) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Squad list");
					Squads.sendSquadList(player);
					return true;
				}
				if(args[0].equalsIgnoreCase("join")) {
					M.v(player, "Squads", ChatColor.WHITE + "/s join <player>");
					return true;
				}
				if(args[0].equalsIgnoreCase("load")) {
					if(Permissions.getRank(player) != Ranks.OWNER) {
						utilPlayer.permMsg(player);
						return true;
					}
					Squads.load();
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Successfully loaded from Database");
					return true;
				}
				if(args[0].equalsIgnoreCase("save")) {
					if(Permissions.getRank(player) != Ranks.OWNER) {
						utilPlayer.permMsg(player);
						return true;
					}
					Squads.save();
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Successfully saved to Database");
					return true;
				}
				if(args[0].equalsIgnoreCase("neut") || args[0].equalsIgnoreCase("neutral")) {
					M.v(player, "Squads", ChatColor.WHITE + "/s neutral <squad>");
					return true;
				}
				if(args[0].equalsIgnoreCase("a")) {
					if(Permissions.getRank(player) != Ranks.OWNER) {
						utilPlayer.permMsg(player);
						return true;
					}
					Squads.playerAdmin(player);
				}
				if(args[0].equalsIgnoreCase("map")) M.v(player, "Squads", ChatColor.WHITE + "Maps coming soon");
				
				if(args[0].equalsIgnoreCase("enemy")) M.v(player, "Squads", ChatColor.WHITE + "/s enemy <squad>");
				
				if(args[0].equalsIgnoreCase("kick")) M.v(player, "Squads", ChatColor.WHITE + "/s kick <player>");
				
				if(args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("i")) M.v(player, "Squads", ChatColor.WHITE + "/s invite <player>");
				
				if(!args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("claim") && 
						!args[0].equalsIgnoreCase("unclaim") && !args[0].equalsIgnoreCase("disband") &&
						!args[0].equalsIgnoreCase("join") && !args[0].equalsIgnoreCase("leave") &&
						!args[0].equalsIgnoreCase("kick") && !args[0].equalsIgnoreCase("demote") &&
						!args[0].equalsIgnoreCase("promote") && !args[0].equalsIgnoreCase("invite") &&
						!args[0].equalsIgnoreCase("home") && !args[0].equalsIgnoreCase("set") &&
						!args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("delete") &&
						!args[0].equalsIgnoreCase("i") && !args[0].equalsIgnoreCase("desc") &&
						!args[0].equalsIgnoreCase("admin") && !args[0].equalsIgnoreCase("tp") &&
						!args[0].equalsIgnoreCase("ally") && !args[0].equalsIgnoreCase("trust") &&
						!args[0].equalsIgnoreCase("enemy") && !args[0].equalsIgnoreCase("view") &&
						!args[0].equalsIgnoreCase("display") && !args[0].equalsIgnoreCase("v") &&
						!args[0].equalsIgnoreCase("Nether") && !args[0].equalsIgnoreCase("Spawn") &&
						!args[0].equalsIgnoreCase("nether") && !args[0].equalsIgnoreCase("spawn") &&
						!args[0].equalsIgnoreCase("Nether2") && !args[0].equalsIgnoreCase("Shops") &&
						!args[0].equalsIgnoreCase("nether2") && !args[0].equalsIgnoreCase("shops") &&
						!args[0].equalsIgnoreCase("Arena") && !args[0].equalsIgnoreCase("Games") &&
						!args[0].equalsIgnoreCase("arena") && !args[0].equalsIgnoreCase("games") &&
						!args[0].equalsIgnoreCase("money") && !args[0].equalsIgnoreCase("chat") &&
						!args[0].equalsIgnoreCase("chunk") && !args[0].equalsIgnoreCase("map") &&
						!args[0].equalsIgnoreCase("neut") && !args[0].equalsIgnoreCase("neutral") &&
						!args[0].equalsIgnoreCase("load") && !args[0].equalsIgnoreCase("save") &&
						!args[0].equalsIgnoreCase("search") && !args[0].equalsIgnoreCase("allies")
						&& !args[0].equalsIgnoreCase("regen") && !args[0].equalsIgnoreCase("a")) {

					//{"create", "claim", "unclaim", "disband", "join", "leave", "kick", "demote", "promote", "invite", "home", "list", "delete", "i", "desc", "admin", "tp", 
					//					"ally", "trust", "enemy", "view", "display", "v", "Nether", "Nether2", "Spawn", "Shops", "Arena", "Games"};
					String search = args[0];
					if(Squads.squadMap.containsKey(search)) {
						search = Squads.squadMap.get(search).name;
						Squads.sendSquadDetails(player, search);
						return true;
					} else {
						Player target = Bukkit.getServer().getPlayer(search);
						if(target == null) {
							if(Squads.memberMap.containsKey(search)) {
								search = Squads.memberMap.get(search);
								Squads.sendSquadDetails(player, search);
								return true;
							}
						} else {
							search = Squads.getSquad(target.getName());
							Squads.sendSquadDetails(player, search);
							return true;
						}
					}
					LinkedList<String> matchList = new LinkedList<String>();
					String matches = "";
					for(String name : Squads.squadMap.keySet()) {
						if(name.contains(search)) {
							matchList.add(name);
							matches = matches + ChatColor.YELLOW + name + ", ";
						}
					}
					if(matchList.size() == 0 || matchList.isEmpty()) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "No matches were found for " + ChatColor.YELLOW + args[0]);
						return true;
					}
					player.sendMessage(ChatColor.BLUE + "Search - " + Utilities.removeLastIntChars(matches, 2));
				}
			}

			/**
			 * ARGS 2 STARTS HERE
			 */
			/**
			 * ARGS 2 STARTS HERE
			 */
			/**
			 * ARGS 2 STARTS HERE
			 */
			/**
			 * ARGS 2 STARTS HERE
			 */
			/**
			 * ARGS 2 STARTS HERE
			 *//**
			 * ARGS 2 STARTS HERE
			 */

			/**
			 * ARGS 2 STARTS HERE
			 */


			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("chunk")) {
					String chunk1 = "";
					String chunk2 = "";
					if(args[1].equalsIgnoreCase("1")) {
						chunk1 = utilWorld.chunkToStr(player.getLocation().getChunk());
						player.sendMessage("Chunk 1 = " + chunk1);
						return true;
					}
					if(args[1].equalsIgnoreCase("2")) {
						chunk2 = utilWorld.chunkToStr(player.getLocation().getChunk());
						player.sendMessage("Chunk 2 = " + chunk2);
						if(chunk1.equals(chunk2)) {
							player.sendMessage("true - they match");
							return true;
						} else if(!chunk1.equals(chunk2)) {
							player.sendMessage("false - they don't match");
							return true;
						}
					}

				}
				if(args[0].equalsIgnoreCase("create")) {
					String name = args[1];
					if(Squads.hasSquad(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are already in a Squad");
						return true;
					}
					if(Squads.squadMap.containsKey(name)) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "A Squad with that name already exists");
						return true;
					}
					if(Utilities.stringEqualsFromArray(name, Cmds) && !RCommands.adminMode.containsKey(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Name cannot be a command");
						return true;
					}
					if(Utilities.containsSpecialChar(name)) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Squad name cannot have special characters");
						return true;
					}
					if(name.length() < 3) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Squad name must have atleast 3 characters");
						return true;
					}
					if(name.length() > 10) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Squad name cannot have over 10 characters");
						return true;
					}
					if(RCommands.adminMode.containsKey(player.getName())) {
						Squads.create(name, player.getName(), true);
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You created the admin squad " + ChatColor.YELLOW + name);
						return true;
					}
					Squads.create(name, player.getName(), false);
					Bukkit.broadcastMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " formed the Squad " + ChatColor.YELLOW + name);
				}
				if(args[0].equalsIgnoreCase("set")) {
					if(args[1].equalsIgnoreCase("home")) {
						if(!Squads.hasSquad(player.getName())) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
							return true;
						}
						if(Squads.getRank(player.getName()) != Rank.LEADER) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be the " + ChatColor.YELLOW + "Leader " + ChatColor.WHITE + "to set home");
							return true;
						}
						if(utilServer.isInSpawn(player)) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot set home in " + ChatColor.YELLOW + "Spawn");
							return true;
						}
						if(Squads.isClaimed(utilWorld.chunkToStr(player.getLocation().getChunk()))) {
							if(!Squads.getOwner(utilWorld.chunkToStr(player.getLocation().getChunk())).equals(Squads.getSquad(player.getName()))) {
								player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You can only set home in your " + ChatColor.YELLOW + "Land");
								return true;
							}
						}
						if(Squads.getOwner(utilWorld.chunkToStr(player.getLocation().getChunk())).equalsIgnoreCase("None")) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You can only set home in your " + ChatColor.YELLOW + "Land");
							return true;
						}
						Squads.setHome(Squads.getSquad(player.getName()), player.getLocation());
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You set your Squad's home to your location");
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("help")) {
					if(!Utilities.isInteger(args[1])) {
						M.v(player, "Squads", ChatColor.WHITE + "Invalid page number");
						return true;
					}
					int page = Integer.parseInt(args[1]);
					Squads.sendHelpMenu(player, page);
					return true;
				}
				if(args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("i")) {
					if(Squads.getRank(player.getName()) != Rank.ADMIN && Squads.getRank(player.getName()) != Rank.LEADER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be atleast an " + ChatColor.YELLOW + "Admin " + ChatColor.WHITE + "to invite");
						return true;
					}
					Player iPlayer = Bukkit.getServer().getPlayer(args[1]);
					if(iPlayer == null) {
						Squads.invite(player, args[1]);
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You invited " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " to your Squad");
						return true;
					}
					if(Squads.getSquad(iPlayer.getName()).equalsIgnoreCase(Squads.getSquad(player.getName()))) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "That player is already in your Squad");
						return true;
					}
					if(Squads.isInvited(iPlayer.getName(), Squads.getSquad(player.getName()))) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "That player is already invited");
						return true;
					}
					Squads.invite(player, iPlayer.getName());
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You invited " + ChatColor.YELLOW + iPlayer.getName() + ChatColor.WHITE + " to your Squad");
					iPlayer.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You were invited to join " + ChatColor.YELLOW + Squads.getSquad(player.getName()));
					return true;
				}
				if(args[0].equalsIgnoreCase("deinivite") || args[0].equalsIgnoreCase("uninvite")) {
					if(Squads.getRank(player.getName()) != Rank.ADMIN && Squads.getRank(player.getName()) != Rank.LEADER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be atleast an " + ChatColor.YELLOW + "Admin " + ChatColor.WHITE + "to de-invite");
						return true;
					}
					Player dPlayer = Bukkit.getServer().getPlayer(args[1]);
					if(dPlayer == null) {
						if(!Squads.isInvited(args[0], Squads.getSquad(player.getName()))) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " is not already invited");
							return true;
						}
						Squads.deinvite(args[0], Squads.getSquad(player.getName()));
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You de-invited " + ChatColor.YELLOW + args[0]);
						return true;
					}
					if(!Squads.isInvited(dPlayer.getName(), Squads.getSquad(player.getName()))) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + dPlayer.getName() + ChatColor.WHITE + " is not already invited");
						return true;
					} 
					Squads.deinvite(dPlayer.getName(), Squads.getSquad(player.getName()));
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You de-invited " + ChatColor.YELLOW + dPlayer.getName());
					return true;
				}
				if(args[0].equalsIgnoreCase("promote")) {
					if(!Squads.hasSquad(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
						return true;
					}
					if(Squads.getRank(player.getName()) == Rank.MEMBER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be atleast an " + ChatColor.YELLOW + "Admin " + ChatColor.WHITE + "to promote players");
						return true;
					}
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null) {
						if(!Squads.getSquad(player.getName()).equalsIgnoreCase(Squads.getSquad(args[1]))) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "That player is not in your Squad");
							return true;
						}
						if(Squads.getRank(args[1]) == Rank.LEADER) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot promote the " + ChatColor.YELLOW + "Leader");
							return true;
						}
						if(Squads.getRank(args[1]) != Rank.MEMBER) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " is not a " + ChatColor.YELLOW + "Member");
							return true;
						}
						Squads.promote(args[1]);
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You promoted " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " to " + ChatColor.YELLOW + "Admin");
						return true;
					}
					if(!Squads.getSquad(player.getName()).equalsIgnoreCase(Squads.getSquad(target.getName()))) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + target.getName() + ChatColor.WHITE + " is not in your Squad");
						return true;
					}
					if(Squads.getRank(target.getName()) == Rank.LEADER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot promote the " + ChatColor.YELLOW + "Leader");
						return true;
					}
					if(Squads.getRank(target.getName()) != Rank.MEMBER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + target.getName() + ChatColor.WHITE + " is not a " + ChatColor.YELLOW + "Member");
						return true;
					}
					Squads.promote(target.getName());
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You promoted " + ChatColor.YELLOW + target.getName() + ChatColor.WHITE + " to " + ChatColor.YELLOW + "Admin");
					return true;
				}
				if(args[0].equalsIgnoreCase("demote")) {
					if(!Squads.hasSquad(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
						return true;
					}
					if(Squads.getRank(player.getName()) != Rank.LEADER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be the " + ChatColor.YELLOW + "Leader " + ChatColor.WHITE + "to demote");
						return true;
					}
					Player target = Bukkit.getServer().getPlayer(args[1]);
					if(target == null) {
						if(!Squads.getSquad(player.getName()).equals(Squads.getSquad(args[1]))) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "That player is not in your Squad");
							return true;
						}
						if(args[1].equals(player.getName())) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot " + ChatColor.YELLOW + "Demote " + ChatColor.WHITE + "yourself");
							return true;
						}
						if(Squads.getRank(args[1]) == Rank.LEADER) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot " + ChatColor.YELLOW + "Demote " + ChatColor.WHITE + " the " + ChatColor.YELLOW + "Leader");
							return true;
						}
						if(Squads.getRank(args[1]) == Rank.MEMBER) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot " + ChatColor.YELLOW + "Demote " + ChatColor.WHITE + "a " + ChatColor.YELLOW + "Member");
							return true;
						}
 						Squads.demote(args[1]);
 						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You demoted " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " to " + ChatColor.YELLOW + "Member");
						return true;
					}
					if(!Squads.getSquad(player.getName()).equals(Squads.getSquad(target.getName()))) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "That player is not in your Squad");
						return true;
					}
					if(Squads.getRank(args[1]) == Rank.LEADER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot " + ChatColor.YELLOW + "Demote " + ChatColor.WHITE + " the " + ChatColor.YELLOW + "Leader");
						return true;
					}
					if(Squads.getRank(args[1]) == Rank.MEMBER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot " + ChatColor.YELLOW + "Demote " + ChatColor.WHITE + "fa " + ChatColor.YELLOW + "Member");
						return true;
					}
					Squads.demote(target.getName());
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You demoted " + ChatColor.YELLOW + target.getName() + ChatColor.WHITE + " to " + ChatColor.YELLOW + "Member");
					return true;
				}
				if(args[0].equalsIgnoreCase("leader")) {
					if(!Squads.hasSquad(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
						return true;
					}
					if(Squads.getRank(player.getName()) != Rank.LEADER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be the " + ChatColor.YELLOW + "Leader " + ChatColor.WHITE + "to set Leader");
						return true;
					}
					Player target = Bukkit.getServer().getPlayer(args[1]);
					if(target == null) {
						if(!Squads.getSquad(player.getName()).equals(Squads.getSquad(args[1]))) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "That player is not in your Squad");
							return true;
						}
						if(args[1].equals(player.getName())) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are already the " + ChatColor.YELLOW + "Leader");
							return true;
						}
						Squads.setRank(args[1], Rank.LEADER);
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You gave " + ChatColor.YELLOW + "Leadership " + ChatColor.WHITE + "to " + ChatColor.YELLOW + args[1]);
					}
					if(!Squads.getSquad(player.getName()).equals(Squads.getSquad(target.getName()))) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "That player is not in your Squad");
						return true;
					}
					if(target.getName().equals(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are already the " + ChatColor.YELLOW + "Leader");
						return true;
					}
					Squads.setRank(player.getName(), Rank.ADMIN);
					Squads.setRank(target.getName(), Rank.LEADER);
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You gave " + ChatColor.YELLOW + "Leadership " + ChatColor.WHITE + "to " + ChatColor.YELLOW + target.getName());
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("unclaim") || args[0].equalsIgnoreCase("uc")) {
				if(args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("a")) {
					if(!Squads.hasSquad(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
						return true;
					}
					if(Squads.getRank(player.getName()) != Rank.LEADER) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be the " + ChatColor.YELLOW + "Leader " + ChatColor.WHITE + "to unclaim all");
						return true;
					}
					Squads.unclaimAll(Squads.getSquad(player.getName()));
					Squads.sendMembersMsg(Squads.getSquad(player.getName()), ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " unclaimed all " + ChatColor.YELLOW + "Land");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("kick")) {
				if(!Squads.hasSquad(player.getName())) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
					return true;
				}
				if(Squads.getRank(player.getName()) != Rank.LEADER) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be the " + ChatColor.YELLOW + "Leader " + ChatColor.WHITE + " to kick");
					return true;
				}
				Player target = Bukkit.getServer().getPlayer(args[1]);
				if(target == null) {
					if(!Squads.getSquad(args[1]).equals(Squads.getSquad(player.getName()))) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "That player is not in your Squad");
						return true;
					}
					Squads.leave(args[1], Squads.getSquad(player.getName()));
					Squads.sendMembersMsg(Squads.getSquad(player.getName()), ChatColor.YELLOW + args[1] + ChatColor.WHITE + " was kicked from your Squad");
					return true;
				}
				if(!Squads.getSquad(target.getName()).equals(Squads.getSquad(player.getName()))) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "That player is not in your Squad");
					return true;
				}
				if(target.getName().equals(player.getName())) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot " + ChatColor.YELLOW + "kick " + ChatColor.WHITE + "yourself");
					return true;
				}
				String tSquad = Squads.getSquad(player.getName());
				Squads.leave(target.getName(), Squads.getSquad(player.getName()));
				Squads.sendMembersMsg(Squads.getSquad(player.getName()), ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " kicked " + ChatColor.YELLOW + target.getName() + ChatColor.WHITE + " from your Squad");
				target.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You were kicked from " + ChatColor.YELLOW + tSquad);
				if(Squads.getPower(tSquad) > Squads.getMaxPower(tSquad)) {
					Squads.squadMap.get(tSquad).power = Squads.getMaxPower(tSquad);
					return true;
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("ally")) {
				if(Squads.getRank(player.getName()) == Rank.MEMBER) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be atleast an " + ChatColor.YELLOW + "Admin " + ChatColor.WHITE + " to ally");
					return true;
				}
				if(!Squads.hasSquad(player.getName())) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You aren't in a Squad");
					return true;
				}
				String squad = Squads.getSquad(player.getName());
				if(Squads.requestAllyMap.containsValue(squad)) {
					for(String keys : Squads.requestAllyMap.keySet()) {
						if(keys.equalsIgnoreCase(args[1])) {
							if(Squads.requestAllyMap.get(keys).equalsIgnoreCase(squad)) {
								Squads.addAlly(squad, args[1]);
								Squads.sendMembersMsg(squad, ChatColor.WHITE + "You are now allies with " + ChatColor.YELLOW + args[1]);
								Squads.sendMembersMsg(args[1], ChatColor.WHITE + "You are now allies with " + ChatColor.YELLOW + squad);
								return true;
							}
						}
					}
				}
				if(squad.equals(args[1])) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot ally yourself");
					return true;
				}
				if(!Squads.squadMap.containsKey(args[1])) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " does not exist");
					return true;
				}
				if(Squads.areAllies(squad, args[1])) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are already " + ChatColor.YELLOW + "allies " + ChatColor.WHITE + "with " + ChatColor.YELLOW + args[1]);
					return true;
				}
				if(Squads.areEnemies(squad, args[1])) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot " + ChatColor.YELLOW + "Request Alliance " + ChatColor.WHITE + "with an enemy");
					return true;
				}
				if(Squads.requestAllyMap.containsKey(squad)) {
					for(String keys : Squads.requestAllyMap.keySet()) {
						if(keys.equalsIgnoreCase(squad)) {
							if(Squads.requestAllyMap.get(keys).equalsIgnoreCase(args[1])) {
								player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You've already " + ChatColor.YELLOW + "Requested Alliance " + ChatColor.WHITE + "with " + ChatColor.YELLOW + args[1]);
								return true;
							}
						}
					}
				}
				Squads.requestAlliance(squad, args[1]);
				Squads.sendMembersMsg(squad, ChatColor.WHITE + "You requested " + ChatColor.YELLOW + "Alliance " + ChatColor.WHITE + "with " + ChatColor.YELLOW + args[1]);
				Squads.sendMembersMsg(args[1], ChatColor.YELLOW + squad + ChatColor.WHITE + " requested " + ChatColor.YELLOW + "Alliance " + ChatColor.WHITE + "with you");
			}
			if(args[0].equalsIgnoreCase("enemy")) {
				String pSquad = Squads.getSquad(player.getName());
				if(Squads.getRank(player.getName()) == Rank.MEMBER) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You must be atleast an " + ChatColor.YELLOW + "Admin " + ChatColor.WHITE + "to enemy");
					return true;
				}
				if(!Squads.squadMap.containsKey(args[1])) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " doesn't exist");
					return true;
				}
				if(Squads.areEnemies(pSquad, args[1])) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are already enemies with " + ChatColor.YELLOW + args[1]);
					return true;
				}
				if(Squads.areAllies(pSquad, args[1])) {
					Squads.unally(pSquad, args[1]);
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You un-allied " + ChatColor.YELLOW + args[1]);
					Squads.sendMembersMsg(pSquad, ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " un-allied " + ChatColor.YELLOW + args[1]);
					return true;
				}
				if(!Squads.areAllies(pSquad, args[1]) && !Squads.areEnemies(pSquad, args[1])) {
					Squads.addEnemy(pSquad, args[1]);
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You enemied " + ChatColor.YELLOW + args[1]);
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("trust")) {
				String pSquad = Squads.getSquad(player.getName());
				String tSquad = args[1];
				if(Squads.getRank(player.getName()) == Rank.MEMBER) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You need to be alteast an " + ChatColor.YELLOW + "Admin " + ChatColor.WHITE + "to trust");
					return true;
				}
				if(!Squads.squadMap.containsKey(tSquad)) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Squad " + ChatColor.YELLOW + tSquad + ChatColor.WHITE + " doesn't exist");
					return true;
				}
				if(pSquad.equalsIgnoreCase(tSquad)) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot trust yourself");
					return true;
				}
				if(!Squads.areAllies(pSquad, tSquad)) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not allies with " + ChatColor.YELLOW + tSquad);
					return true;
				}
				if(Squads.oneTrust(pSquad, tSquad) || Squads.areTrusted(pSquad, tSquad)) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You have already gave trust to " + ChatColor.YELLOW + tSquad);
					return true;
				}
				Squads.addTrust(pSquad, tSquad);
				Squads.sendMembersMsg(pSquad, ChatColor.WHITE + "You gave trust to " + ChatColor.YELLOW + tSquad);
				Squads.sendMembersMsg(tSquad, ChatColor.YELLOW + pSquad + ChatColor.WHITE + " gave trust to you");
			}
			if(args[0].equalsIgnoreCase("untrust") || args[0].equalsIgnoreCase("revoketrust")) {
				String pSquad = Squads.getSquad(player.getName());
				String tSquad = args[1];
				if(Squads.getRank(player.getName()) == Rank.MEMBER) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You need to be alteast an " + ChatColor.YELLOW + "Admin " + ChatColor.WHITE + "to revoke trust");
					return true;
				}
				if(pSquad.equalsIgnoreCase(tSquad)) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot revoke trust from yourself");
					return true;
				}
				if(!Squads.oneTrust(pSquad, tSquad)) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You haven't given trust to " + ChatColor.YELLOW + tSquad);
					return true;
				}
				Squads.revokeTrust(pSquad, tSquad);
				player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You revoked trust from " + ChatColor.YELLOW + tSquad);
			}
			if(args[0].equalsIgnoreCase("neutral") || args[0].equalsIgnoreCase("neut")) {
				if(!Squads.hasSquad(player.getName())) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not in a Squad");
					return true;
				}
				String pSquad = Squads.getSquad(player.getName());
				if(Squads.getRank(player.getName()) == Rank.MEMBER) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You need to be alteast an " + ChatColor.YELLOW + "Admin " + ChatColor.WHITE + "to neutral");
					return true;
				}
				if(!Squads.squadMap.containsKey(args[1])) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "The Squad " + ChatColor.YELLOW + args[1] + ChatColor.WHITE + " doesn't exist");
					return true;
				}
				if(Squads.areAllies(pSquad, args[1])) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Type /s enemy <squad> to " + ChatColor.YELLOW + "Unally " + ChatColor.WHITE + "them");
					return true;
				}
				if(!Squads.areAllies(pSquad, args[1]) && !Squads.areEnemies(pSquad, args[1])) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are already neutral with " + ChatColor.YELLOW + args[1]);
					return true;
				}
				if(Squads.requestNeutralMap.containsValue(pSquad)) {
					for(String keys : Squads.requestNeutralMap.keySet()) {
						if(keys.equalsIgnoreCase(args[1])) {
							if(Squads.requestNeutralMap.get(keys).equalsIgnoreCase(pSquad)) {
								Squads.setNeutral(pSquad, keys);
								Squads.sendMembersMsg(pSquad, ChatColor.WHITE + "You accepted neutral with " + ChatColor.YELLOW + args[1]);
								Squads.sendMembersMsg(args[1], ChatColor.YELLOW + pSquad + ChatColor.WHITE + " accepted to neutral you");
								return true;
							}
						}
					}
				}
				if(Squads.requestNeutralMap.containsKey(pSquad)) {
					for(String keys : Squads.requestNeutralMap.keySet()) {
						if(Squads.requestNeutralMap.get(keys).equalsIgnoreCase(args[1])) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You've already " + ChatColor.YELLOW + "Requested Neutral " + ChatColor.WHITE + "with " + ChatColor.YELLOW + args[1]);
							return true;
						}
					}
				}
				Squads.requestNeutral(pSquad, args[1]);
				Squads.sendMembersMsg(pSquad, ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " requested neutral with " + ChatColor.YELLOW + args[1]);
				Squads.sendMembersMsg(args[1], ChatColor.YELLOW + pSquad + ChatColor.WHITE + " requested neutral with you");
			}
			if(args[0].equalsIgnoreCase("regen")) {
				if(args[1].equalsIgnoreCase("clear")) {
					Iterator<String> it = Squads.regenMap.keySet().iterator();
					String key = it.next();
					if(Squads.getPower(key) <= Squads.getMaxPower(key)) Squads.addPower(key, 1);
					Squads.regenMap.clear();
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Cleared " + ChatColor.YELLOW + "Regen List");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("join")) {
				Player target = Bukkit.getServer().getPlayer(args[1]);
				if(target == null) {
					player.sendMessage(ChatColor.BLUE + "Server - " + ChatColor.WHITE + "Invalid player");
					return true;
				}
				String squad = Squads.getSquad(target.getName());
				if(Squads.memberMap.containsKey(player.getName())) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are in a Squad already");
					return true;
				}
				if(!Squads.isInvited(player.getName(), squad) && !RCommands.adminMode.containsKey(player.getName())) {
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are not invited to " + ChatColor.YELLOW + squad);
					return true;
				}
				if(RCommands.adminMode.containsKey(player.getName())) {
					if(Squads.hasSquad(player.getName())) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You are already in a Squad");
						return true;
					}
					Squads.addPlayer(player.getName(), squad, Rank.ADMIN);
					player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You joined " + ChatColor.YELLOW + squad + ChatColor.WHITE + " via " + ChatColor.YELLOW + "Admin mode");
					return true;
				}
				Squads.sendMembersMsg(squad, ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " joined your Squad");
				Squads.addPlayer(player.getName(), squad, Rank.MEMBER);
				if(Squads.isInvited(player.getName(), squad)) {
					Squads.deinvite(player.getName(), squad);
					return true;
				}
				if(args[0].equalsIgnoreCase("search")) {
					String searchSquad = Squads.searchSquad(args[1]);
					if(searchSquad.equalsIgnoreCase("None")) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "Found no matches for " + ChatColor.YELLOW + args[1]);
						return true;
					}
					Squads.sendSquadDetails(player, searchSquad);
					return true;
				}
				return true;
			}
			if(args.length == 3) {
				if(args[0].equalsIgnoreCase("a")) {
					if(!Permissions.isAdmin(player)) {
						utilPlayer.permMsg(player);
						return true;
					}
					if(args[1].equalsIgnoreCase("disband")) {
						String squad = args[2];
						if(!Squads.squadMap.containsKey(squad)) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + squad + ChatColor.WHITE + " doesn't exist");
							return true;
						}
						Squads.disbandCheck(squad);
						M.v(player, "Squads", ChatColor.WHITE + "You force-disbanded " + ChatColor.YELLOW + squad);
					}
					if(args[1].equalsIgnoreCase("home")) {
						String squad = args[2];
						if(!Squads.squadMap.containsKey(squad)) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + squad + ChatColor.WHITE + " doesn't exist");
							return true;
						}
						if(Squads.getHome(squad) == null) {
							player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.YELLOW + squad + ChatColor.WHITE + " doesn't have a home");
							return true;
						}
					}
					if(args[1].equalsIgnoreCase("clear")) {
						if(!player.getName().equalsIgnoreCase("Loogeh")) {
							utilPlayer.permMsg(player);
							return true;
						}
						if(args[2].equalsIgnoreCase("all")) {
							final Player finalPlayer = player;
							password_cooldown.add(finalPlayer.getName());
							M.v(finalPlayer, "Squads", ChatColor.WHITE + "You have " + ChatColor.YELLOW + "10 Seconds " + ChatColor.WHITE + "to enter the password");
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								
								@Override
								public void run() {
									if(password_cooldown.contains("Loogeh")) {
										password_cooldown.remove("Loogeh");
										M.v(finalPlayer, "Security", ChatColor.WHITE + "Password timed-out");
										return;
									}
									
									
								}
							}, 200L);
						} 
					}
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("password")) {
			if(Permissions.getRank(player) != Ranks.OWNER) {
				utilPlayer.permMsg(player);
				return true;
			}
			if(args.length < 2) {
				M.v(player, "Command", ChatColor.WHITE + "/password <protocol> <password>");
				return true;
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("squads_clear")) {
					if(!password_cooldown.contains(player.getName())) {
						M.v(player, "Security", ChatColor.WHITE + "You must activate the password first");
						return true;
					}
					if(args[1].equalsIgnoreCase("w8qiU4ae")) {
						
					}
				}
			}
		}
		return false;
	}

}
