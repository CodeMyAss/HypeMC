package me.loogeh.Hype;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.SQL.Permissions.Ranks;
import me.loogeh.Hype.entity.NPC;
import me.loogeh.Hype.util.Utilities;
import me.loogeh.Hype.util.utilInv;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilServer;
import me.loogeh.Hype.util.utilTime;
import me.loogeh.Hype.util.utilTime.TimeUnit;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftSlime;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class RCommands implements CommandExecutor {
	public static Hype plugin = Hype.plugin;
	public static boolean locked = false;
	public static boolean chatdisable = false;
	public static Set<String> adminmode = new HashSet<String>();
	public static Set<String> invis = new HashSet<String>();
	public static Set<String> god = new HashSet<String>();
	public static HashMap<String, Long> adminMode = new HashMap<String, Long>();
	public static Set<String> blockEffects = new HashSet<String>();
	public static Set<String> spyMode = new HashSet<String>();
	public static HashMap<String, Chunk> claimMap = new HashMap<String, Chunk>();
	public static HashMap<String, String> ignoreMap = new HashMap<String, String>();
	public static boolean effect = false;
	public static boolean bossLiving = false;
	public static String bossType = "";
	public static int bossHealth = 0;
	public static UUID bossUUID;
	public static String[] bossArray = {"Slime of Grief", "Iron Golem of The Underworld", "Snowman of Satan"};

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		final Player player = (Player) sender;
		if(commandLabel.equalsIgnoreCase("server")) {
			if(Permissions.getRank(player) != Permissions.Ranks.OWNER) {
				utilPlayer.permMsg(player);
				return true;
			}
			if(args.length < 1) {
				M.message(player, M.mType.HELP, "/server lock " + ChatColor.GRAY + "<reason>", ChatColor.DARK_RED + "[OWNER]");
				M.message(player, M.mType.HELP, "/server unlock", ChatColor.DARK_RED + "[OWNER]");
				M.message(player, M.mType.HELP, "/server chat " + ChatColor.GRAY + "<on/off>", ChatColor.RED + "[ADMIN]");
				M.message(player, M.mType.HELP, "/server status", ChatColor.RED + "[ADMIN]");
				return true;
			}
			if(args[0].equalsIgnoreCase("lock")) {
				String reason = Utilities.argsToString2(args);
				if(locked) {
					player.sendMessage(ChatColor.RED + "Server: " + ChatColor.GRAY + "Server is already locked");
					return true;
				}
				if(reason == null) {
					player.sendMessage(ChatColor.RED + "Server: " + ChatColor.GRAY + "You must provide a reason");
					return true;
				}
				locked = true;
				MySQL.doUpdate("UPDATE server_options SET `locked`='true', `lock_reason`='" + reason + "'");
				for(Player pls : utilServer.getPlayers()) {
					if(!Permissions.isStaff(player)) {
						pls.kickPlayer(ChatColor.RED + "Locked " + ChatColor.WHITE + "-" + ChatColor.YELLOW + " The server was locked for" + ChatColor.GREEN + reason);
					}
				}
				Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " locked the server for" + ChatColor.GREEN + reason);
				return true;
			}
			if(args[0].equalsIgnoreCase("unlock")) {
				if(Permissions.getRank(player) != Permissions.Ranks.OWNER) {
					utilPlayer.permMsg(player);
					return true;
				}
				if(!locked) {
					player.sendMessage(ChatColor.RED + "Server: " + ChatColor.GRAY + "Server isn't locked");
					return true;
				}
				if(args.length > 1) {
					M.message(player, M.mType.HELP, "/server lock " + ChatColor.GRAY + "<reason>", ChatColor.DARK_RED + "[OWNER]");
					M.message(player, M.mType.HELP, "/server unlock", ChatColor.DARK_RED + "[OWNER]");
					M.message(player, M.mType.HELP, "/server chat " + ChatColor.GRAY + "<on/off>", ChatColor.RED + "[ADMIN]");
					M.message(player, M.mType.HELP, "/server status", ChatColor.RED + "[ADMIN]");
					return true;
				}
				locked = false;
				MySQL.doUpdate("UPDATE server_options SET `locked`='false', `lock_reason`='unlocked'");
				Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + " unlocked the server");
			}
			if(args[0].equalsIgnoreCase("chat")) {
				if(!Permissions.isAdmin(player)) {
					utilPlayer.permMsg(player);
					return true;
				}
				if(args.length != 2) {
					M.message(player, M.mType.HELP, "/server lock " + ChatColor.GRAY + "<reason>", ChatColor.DARK_RED + "[OWNER]");
					M.message(player, M.mType.HELP, "/server unlock", ChatColor.DARK_RED + "[OWNER]");
					M.message(player, M.mType.HELP, "/server chat " + ChatColor.GRAY + "<on/off>", ChatColor.RED + "[ADMIN]");
					M.message(player, M.mType.HELP, "/server status", ChatColor.RED + "[ADMIN]");
					return true;
				}
				if(args[1].equalsIgnoreCase("on")) {
					if(!chatdisable) {
						player.sendMessage(ChatColor.RED + "Server - " + ChatColor.GRAY + "Chat is already enabled");
						return true;
					}
					chatdisable = true;
					MySQL.doUpdate("UPDATE server_options SET `chatdisabled`='true'");
					Bukkit.broadcastMessage(ChatColor.RED + "Chat " + ChatColor.WHITE + "was disabled");
					return true;
				}
				if(args[1].equalsIgnoreCase("off")) {
					if(chatdisable) {
						player.sendMessage(ChatColor.RED + "Server: " + ChatColor.GRAY + "Chat is already disabled");
						return true;
					}
					chatdisable = false;
				}
			}
			if(args[0].equalsIgnoreCase("status")) {
				if(!Permissions.isAdmin(player)) {
					utilPlayer.permMsg(player);
					return true;
				}
				player.sendMessage(ChatColor.GOLD + "Server Status");
				player.sendMessage(ChatColor.YELLOW + "Locked " + ChatColor.WHITE + " - " + WordUtils.capitalize(String.valueOf(locked)));
				player.sendMessage(ChatColor.YELLOW + "Chat " + ChatColor.WHITE + " - " + WordUtils.capitalize(String.valueOf(chatdisable)));
				player.sendMessage(ChatColor.YELLOW + "Boss " + ChatColor.WHITE + " - " + WordUtils.capitalize(String.valueOf(bossLiving)));
			}
		}
		if(commandLabel.equalsIgnoreCase("ar")) {
			if(!Permissions.isAdmin(player)) {
				utilPlayer.permMsg(player);
				return true;
			}
			if(args.length == 0) {
				M.v(player, "Command", ChatColor.WHITE + "/ar <kit>");
				return true;
			}
			if(args[0].equalsIgnoreCase("leather")) {
				utilPlayer.addArcher(player);
				player.sendMessage(ChatColor.GRAY + "Kit: " + ChatColor.GREEN + "Archer");
			}
			else if(args[0].equalsIgnoreCase("gold")) {
				utilPlayer.addAgility(player);
				player.sendMessage(ChatColor.GRAY + "Kit: " + ChatColor.GREEN + "Agility");
			}
			else if(args[0].equalsIgnoreCase("diamond")) {
				utilPlayer.addJuggernaut(player);
				player.sendMessage(ChatColor.GRAY + "Kit: " + ChatColor.GREEN + "Juggernaut");
			}
			else if(args[0].equalsIgnoreCase("iron")) {
				utilPlayer.addSamuri(player);
				player.sendMessage(ChatColor.GRAY + "Kit: " + ChatColor.GREEN + "Samurai");
			}
			else if(args[0].equalsIgnoreCase("chain")) {
				utilPlayer.addSpecialist(player);
				player.sendMessage(ChatColor.GRAY + "Kit: " + ChatColor.GREEN + "Specialist");
			}
			else if(args[0].equalsIgnoreCase("kit")) {
				player.sendMessage(ChatColor.GRAY + "Kits: " + ChatColor.DARK_GREEN + "Archer: " + ChatColor.GREEN + "Leather "
						+ ChatColor.DARK_GREEN + "Samurai: " + ChatColor.GREEN + "Iron "
						+ ChatColor.DARK_GREEN + "Juggernaut: " + ChatColor.GREEN + "Diamond "
						+ ChatColor.DARK_GREEN + "Agility: " + ChatColor.GREEN + "Gold "
						+ ChatColor.DARK_GREEN + "None: " + ChatColor.GREEN + "Clear");
				if(args[1].equalsIgnoreCase("iron")) {
					utilPlayer.kitSamuri(player);
				} else if(args[1].equalsIgnoreCase("chain")) {
					utilPlayer.kitSpecialist(player);
				} else if(args[1].equalsIgnoreCase("gold")) {
					utilPlayer.kitAgility(player);
				} else if(args[1].equalsIgnoreCase("diamond")) {
					utilPlayer.kitJuggernaut(player);
				} else if(args[1].equalsIgnoreCase("leather")) {
					utilPlayer.kitArcher(player);
				}
			} else if(args[0].equalsIgnoreCase("clear")) {
				player.getInventory().setHelmet(null);
				player.getInventory().setChestplate(null);
				player.getInventory().setLeggings(null);
				player.getInventory().setBoots(null);
				player.sendMessage(ChatColor.GRAY + "Kit: " + ChatColor.GREEN + "None");
			}
		}
		if(commandLabel.equalsIgnoreCase("kick") && (Permissions.isStaff(player)) && (args.length >= 2)) {
			Player tp = player.getServer().getPlayer(args[0]);
			String msg = Utilities.argsToString2(args);
			Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " Kicked " + ChatColor.GOLD + tp.getName() + ChatColor.GRAY + " Reason:" + ChatColor.GREEN + msg);
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Leave - " + ChatColor.GRAY + tp.getName());
			tp.kickPlayer(ChatColor.GRAY + "You have been kicked for: " + ChatColor.WHITE + "> " + ChatColor.YELLOW + msg);
		} else if((commandLabel.equalsIgnoreCase("kick")) && (args.length == 0)) {
			player.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.RED + "/kick <player>");
		}
		if(commandLabel.equalsIgnoreCase("chat") && (Permissions.isAdmin(player))) {
			if(args[0].equalsIgnoreCase("disable")) {
				chatdisable = true;
				Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Chat:" + ChatColor.WHITE + " Disabled");
			}
			if(args[0].equalsIgnoreCase("enable")) {
				chatdisable = false;
				Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Chat:" + ChatColor.WHITE + " Enabled");
			}
		}
		if(commandLabel.equalsIgnoreCase("fly")) {
			if(!Permissions.isAdmin(player)) {
				utilPlayer.permMsg(player);
				return true;
			}
			if(args.length == 0) {
				player.setFlying(true);
				player.setFlySpeed(0.26F);
				player.setAllowFlight(true);
			}
			if(args.length == 1) {
				try {
					float speed = Float.parseFloat(args[0]);
					player.setFlying(true);
					player.setAllowFlight(true);
					player.setFlySpeed(speed);
				} catch(NumberFormatException e) {
					M.v(player, "Fly", ChatColor.WHITE + "You must input a speed");
				}
				
				
			}
		}
		if(commandLabel.equalsIgnoreCase("flyoff") && (Permissions.isAdmin(player))) {
			player.setFlying(false);
			player.setFlySpeed(0.15F);
		}
		if(commandLabel.equalsIgnoreCase("help")) {
			if(!Permissions.isStaff(player)) {
				if(args.length == 0) {
					player.sendMessage(ChatColor.WHITE + "Page: " + ChatColor.BLUE + "1/1");
					player.sendMessage(ChatColor.AQUA + "/s" + ChatColor.GRAY + " Shows squads menu");
					player.sendMessage(ChatColor.AQUA + "/s help" + ChatColor.GRAY + " Squad help menu");
					player.sendMessage(ChatColor.AQUA + "/m <player> <message>" + ChatColor.GRAY + " Message a player");
					player.sendMessage(ChatColor.AQUA + "/r <message>" + ChatColor.GRAY + " Reply to last messaged player");
					player.sendMessage(ChatColor.AQUA + "/rules" + ChatColor.GRAY + " Shows rules");
					player.sendMessage(ChatColor.AQUA + "/a <message>" + ChatColor.GRAY + " Sends all online staff a message");
					return true;
				} else if(args[0].equalsIgnoreCase("2")) {
					
				}
			} else if(Permissions.isStaff(player)) {
				if(args.length == 0) {
					player.sendMessage(ChatColor.WHITE + "Page: " + ChatColor.BLUE + "1/6");
					player.sendMessage(ChatColor.BLUE + "<>" + ChatColor.WHITE + " = Required, " + ChatColor.BLUE + "() " + ChatColor.WHITE + "= Optional ");
					player.sendMessage(ChatColor.AQUA + "/m" + ChatColor.GRAY + " Message a player");
					player.sendMessage(ChatColor.AQUA + "/vote" + ChatColor.GRAY + " Displays vote links");
					player.sendMessage(ChatColor.AQUA + "/st" + ChatColor.GRAY + " Retrieves your stats");
					//player.sendMessage(ChatColor.AQUA + "/a" + ChatColor.GRAY + " Displays arena commands");
					player.sendMessage(ChatColor.AQUA + "/rules" + ChatColor.GRAY + " Shows rules");
					player.sendMessage(ChatColor.AQUA + "/forum" + ChatColor.GRAY + " Displays forum link");
				} else if(args[0].equalsIgnoreCase("2")) {
					player.sendMessage(ChatColor.WHITE + "Page: " + ChatColor.BLUE + "2/6");
					player.sendMessage(ChatColor.AQUA + "/ban <player> <hours> <reason>" + ChatColor.GRAY + " Temp bans a player" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/ban <player> <reason>" + ChatColor.GRAY + " Bans a player" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/kick <player> <reason>" + ChatColor.GRAY + " Kicks a player" + ChatColor.GREEN + " [MOD]");
					player.sendMessage(ChatColor.AQUA + "/mute <player> <amt in mins>" + ChatColor.GRAY + " Temp mutes/mutes a player" + ChatColor.GREEN + " [MOD]");
					player.sendMessage(ChatColor.AQUA + "/unmute <player>" + ChatColor.GRAY + " unmutes a player" + ChatColor.GREEN + " [MOD]");
					player.sendMessage(ChatColor.AQUA + "/lock" + ChatColor.GRAY + " locks the server" + ChatColor.DARK_RED + " [OWNER]");
				} else if(args[0].equalsIgnoreCase("3")) {
					player.sendMessage(ChatColor.WHITE + "Page: " + ChatColor.BLUE + "3/6");
					player.sendMessage(ChatColor.AQUA + "/unlock" + ChatColor.GRAY + " unlocks the server" + ChatColor.DARK_RED + " [OWNER]");
					player.sendMessage(ChatColor.AQUA + "/checkip <player>" + ChatColor.GRAY + " Checks a players IP" + ChatColor.GREEN + " [MOD]");
					player.sendMessage(ChatColor.AQUA + "/checkban <player>" + ChatColor.GRAY + " Checks a players entries" + ChatColor.GREEN + " [MOD]");
					player.sendMessage(ChatColor.AQUA + "/dupeip <player>" + ChatColor.GRAY + " Checks a players ip to other players" + ChatColor.GREEN + " [MOD]");
					player.sendMessage(ChatColor.AQUA + "/rload" + ChatColor.GRAY + " Reloads the server" + ChatColor.GREEN + " [MOD]");
					player.sendMessage(ChatColor.AQUA + "/unban <player>" + ChatColor.GRAY + " Unbans a player" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/ar" + ChatColor.GRAY + " (clear) (kits)" + ChatColor.AQUA + " <type>" + ChatColor.GRAY + " Gives armour set" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/chat" + ChatColor.GRAY + " <enable/disable>" + ChatColor.GRAY + " Enables or disables chat" + ChatColor.RED + " [ADMIN]");
				} else if(args[0].equalsIgnoreCase("4")) {
					player.sendMessage(ChatColor.WHITE + "Page: " + ChatColor.BLUE + "4/6");
					player.sendMessage(ChatColor.AQUA + "/fly(off)" + ChatColor.GRAY + " Enables or disables flying" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/ride <player>" + ChatColor.GRAY + " Rides a player" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/rideme <player>" + ChatColor.GRAY + " Makes a player ride you" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/rideother <tplayer> <player>" + ChatColor.GRAY + " Makes a target player ride another" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/g" + ChatColor.GRAY + " Displays give commands" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/gh <player>" + ChatColor.GRAY + " Gives a player item in your hand" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/tp" + ChatColor.GRAY + " Displays teleport commands" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/say <message>" + ChatColor.GRAY + " Broadcasts a message" + ChatColor.GREEN + " [MOD]");
				} else if(args[0].equalsIgnoreCase("5")) {
					player.sendMessage(ChatColor.WHITE + "Page: " + ChatColor.BLUE + "5/6");
					player.sendMessage(ChatColor.AQUA + "/heal (player)" + ChatColor.GRAY + " Heals a player" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/ci (player)" + ChatColor.GRAY + " clears a players invent" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/warp (warp name)" + ChatColor.GRAY + " Displays warps" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/spleef" + ChatColor.GRAY + " Shows spleef commands" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.AQUA + "/gmalls" + ChatColor.GRAY + " Sets all players gamemode as surivival" + ChatColor.DARK_RED + " [OWNER]");
					player.sendMessage(ChatColor.AQUA + "/gmallc" + ChatColor.GRAY + " Sets all players gamemode as creative" + ChatColor.DARK_RED + " [OWNER]");
					player.sendMessage(ChatColor.AQUA + "/gmalla" + ChatColor.GRAY + " Sets all players gamemode as adventure" + ChatColor.DARK_RED + " [OWNER]");
					player.sendMessage(ChatColor.AQUA + "/time help" + ChatColor.GRAY + " Displays time menu" + ChatColor.GREEN + " [MOD]");
				} else if(args[0].equalsIgnoreCase("6")) {
					player.sendMessage(ChatColor.WHITE + "Page: " + ChatColor.BLUE + "6/6");
					player.sendMessage(ChatColor.AQUA + "/w help" + ChatColor.GRAY + " Displays time menu" + ChatColor.GREEN + " [MOD]");
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("rload")) {
			if(Permissions.isStaff(player)) {
				Bukkit.broadcastMessage(ChatColor.RED + "HypeMC - " + ChatColor.GRAY + "Reloading...");
				Bukkit.getServer().reload();
				Bukkit.broadcastMessage(ChatColor.RED + "HypeMC - " + ChatColor.GRAY + "Reload complete!");
			} else if(!Permissions.isStaff(player)) {
				utilPlayer.permMsg(player);
				return true;
			}
		}
		if(commandLabel.equalsIgnoreCase("i") && (Permissions.getLevel(player).equalsIgnoreCase("owner"))) {
			int one = 1;
			if(one == 1) {
				player.sendMessage(Boolean.toString(utilServer.isInSpawn(player.getLocation())));
				return true;
			}
			if(args[0].equalsIgnoreCase("s")) {
				utilInv.saveContents(player);
				player.sendMessage(ChatColor.RED + "Saved");
				return true;
			} else if(args[0].equalsIgnoreCase("r")) {
				utilInv.retrieveContents(player);
				player.sendMessage(ChatColor.RED + "Received");
				return true;
			}
		}
		if(commandLabel.equalsIgnoreCase("heal")) {
			if (!(sender instanceof Player)) {
				if(args.length == 1){
					utilPlayer.healOtherConsole(sender, args[0]);
					return true;
				}
				else{
					sender.sendMessage(ChatColor.RED + "/heal <player>");
					return true;
				}
			}
			else if (sender instanceof Player) {
				Player player1 = (Player) sender;
				if((args.length == 0) && (Permissions.isAdmin(player1))){
					utilPlayer.heal(player1);
					return true;
				}

				else if((args.length == 1) && (Permissions.isAdmin(player1))){
					utilPlayer.healOther(player1, args[0]);
					return true;
				}
				else{
					player1.sendMessage(ChatColor.RED +  "/heal <player>");
					return true;
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("setspawn")) {
			if(Permissions.getLevel(player).equalsIgnoreCase("owner")) {
				utilServer.setSpawn(player);
				return true;
			} else {
				utilPlayer.permMsg(player);
				return true;
			}
		}
		if(commandLabel.equalsIgnoreCase("time")) {
			if(!Permissions.isStaff(player)) {
				utilPlayer.permMsg(player);
			} else if(Permissions.isStaff(player)) {
				if(args.length == 0) {
					player.sendMessage(ChatColor.RED + "/time" + ChatColor.LIGHT_PURPLE + " day");
					player.sendMessage(ChatColor.RED + "/time" + ChatColor.LIGHT_PURPLE + " night");
					player.sendMessage(ChatColor.RED + "/time" + ChatColor.LIGHT_PURPLE + " set <int>");
					return true;
				}
				if(args[0].equalsIgnoreCase("day")) {
					player.getWorld().setTime(2000L);
					player.sendMessage(ChatColor.GRAY + "You set the " + ChatColor.GREEN + "time " + ChatColor.GRAY + "to " + ChatColor.GREEN + "day");
					return true;
				} else if(args[0].equalsIgnoreCase("night")) {
					player.getWorld().setTime(14000L);
					player.sendMessage(ChatColor.GRAY + "You set the " + ChatColor.GREEN + "time " + ChatColor.GRAY + "to " + ChatColor.GREEN + "night");
					return true;
				} else if(args[0].equalsIgnoreCase("set")) {
					long setTime = Long.parseLong(args[1]);
					player.getWorld().setTime(setTime);
					player.sendMessage(ChatColor.GRAY + "You set the " + ChatColor.GREEN + "time " + ChatColor.GRAY + "to " + ChatColor.GREEN + setTime);
					return true;
				}
				
			}
		}
		if(commandLabel.equalsIgnoreCase("god")) {
			if(!player.getName().equalsIgnoreCase("Loogeh")) {
				player.sendMessage("Unknown command. Type " + "'help'" + " for help.");
			} else {
				if(god.contains(player.getName())) {
					god.clear();
					player.sendMessage(ChatColor.GREEN + "God - " + ChatColor.WHITE + "Disabled");
					return true;
				} else if(!god.contains(player.getName())) {
					god.add(player.getName());
					player.sendMessage(ChatColor.GREEN + "God - " + ChatColor.WHITE + "Enabled");
					return true;
				}

			}
		}
		if(commandLabel.equalsIgnoreCase("rules")) {
			player.sendMessage(ChatColor.AQUA + "Rules:");
			player.sendMessage(ChatColor.GOLD + "1. " + ChatColor.WHITE + "No hacking/scripting of any form");
			player.sendMessage(ChatColor.GOLD + "2. " + ChatColor.WHITE + "No excessive abuse");
			player.sendMessage(ChatColor.GOLD + "3. " + ChatColor.WHITE + "No racism");
			player.sendMessage(ChatColor.GOLD + "4. " + ChatColor.WHITE + "No spawn killing");
			player.sendMessage(ChatColor.GOLD + "5. " + ChatColor.WHITE + "No glitching");
			player.sendMessage(ChatColor.GOLD + "6. " + ChatColor.WHITE + "Respect everyone");
			player.sendMessage(ChatColor.GOLD + "7. " + ChatColor.WHITE + "No spamming");
			player.sendMessage(ChatColor.GOLD + "8. " + ChatColor.WHITE + "No advertising");
			player.sendMessage(ChatColor.GOLD + "9. " + ChatColor.WHITE + "No giving out personal information");
			player.sendMessage(ChatColor.GOLD + "10. " + ChatColor.WHITE + "No asking for any staff ranks");
		}
		if(commandLabel.equalsIgnoreCase("admin")) {
			if(!Permissions.isAdmin(player)) {
				utilPlayer.permMsg(player);
			} else {
				if(args.length == 0) {
					M.message(player, M.mType.HELP, "/admin toggle", ChatColor.RED + "[ADMIN]");
					M.message(player, M.mType.HELP, "/admin spy", ChatColor.RED + "[ADMIN]");
					M.message(player, M.mType.HELP, "/admin cloak ", ChatColor.RED + "[ADMIN]");
					M.message(player, M.mType.HELP, "/admin status", ChatColor.RED + "[ADMIN]");
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("toggle")) {
						if(adminMode.containsKey(player.getName())) {
							adminMode.remove(player.getName());
							player.sendMessage(ChatColor.RED + "Admin:" + ChatColor.GRAY + " Disabled");
							player.setGameMode(GameMode.SURVIVAL);
						} else {
							adminMode.put(player.getName(), System.currentTimeMillis());
							player.sendMessage(ChatColor.RED + "Admin:" + ChatColor.GRAY + " Enabled");
							player.setGameMode(GameMode.CREATIVE);
						}
					} else if(args[0].equalsIgnoreCase("list")) {
						player.sendMessage(ChatColor.RED + "Admin mode -" + ChatColor.GRAY + "Player list");
						for(Player players : Bukkit.getOnlinePlayers()) {
							String adminPlayers = "";
							if(adminMode.containsKey(players.getName())) {
								adminPlayers = adminPlayers + ChatColor.YELLOW + players.getName() + ",";
							}
							player.sendMessage(adminPlayers);
						}
					} else if(args[0].equalsIgnoreCase("status")) {
						if(adminMode.containsKey(player.getName())) {
							Long now = System.currentTimeMillis();
							Long dur = (now - adminMode.get(player.getName())) / 1000;
							Long dur1 = adminMode.get(player.getName());
							player.sendMessage(ChatColor.RED + "Admin:" + ChatColor.GRAY + " Enabled");
							if(dur < 60) {
								player.sendMessage(ChatColor.RED + "Duration: " + ChatColor.WHITE + utilTime.convertString(dur1, TimeUnit.BEST, 1));
							}
//							} else if(dur > 60 && dur < 3600) {
//								dur = dur / 60;
//								player.sendMessage(ChatColor.RED + "Duration: " + ChatColor.WHITE + dur + " Minutes");
//							} else if(dur > 60 && dur < 3600) {
//								dur = dur / 60;
//								player.sendMessage(ChatColor.RED + "Duration: " + ChatColor.WHITE + dur + " Hours");
//							}
							if(utilPlayer.cloakPlayers.contains(player.getName())) player.sendMessage(ChatColor.RED + "Cloak:" + ChatColor.GRAY + " Enabled");
							else player.sendMessage(ChatColor.RED + "Cloak:" + ChatColor.GRAY + " Disabled");
						} else {
							player.sendMessage(ChatColor.RED + "Admin:" + ChatColor.GRAY + " Disabled");
							player.sendMessage(ChatColor.RED + "Duration:" + ChatColor.GRAY + " 0");
							if(utilPlayer.cloakPlayers.contains(player.getName())) player.sendMessage(ChatColor.RED + "Cloak:" + ChatColor.GRAY + " Enabled");
							else player.sendMessage(ChatColor.RED + "Cloak:" + ChatColor.GRAY + " Disabled");
							if(spyMode.contains(player.getName())) player.sendMessage(ChatColor.RED + "Spy: " + ChatColor.WHITE + "Enabled");
							else player.sendMessage(ChatColor.RED + "Spy: " + ChatColor.WHITE + "Disabled");
						}
					} else if(args[0].equalsIgnoreCase("cloak")) {
						utilPlayer.cloak(player);
					} else if(args[0].equalsIgnoreCase("spy")) {
						if(spyMode.contains(player.getName())) {
							spyMode.remove(player.getName());
							player.sendMessage(ChatColor.RED + "Spy: " + ChatColor.GRAY + "Disabled");
						} else {
							spyMode.add(player.getName());
							player.sendMessage(ChatColor.RED + "Spy: " + ChatColor.GRAY + "Enabled");
						}
					} else if(args[0].equalsIgnoreCase("help")) {
						M.message(player, M.mType.HELP, "/admin toggle", ChatColor.RED + "[ADMIN]");
						M.message(player, M.mType.HELP, "/admin spy", ChatColor.RED + "[ADMIN]");
						M.message(player, M.mType.HELP, "/admin cloak ", ChatColor.RED + "[ADMIN]");
						M.message(player, M.mType.HELP, "/admin status", ChatColor.RED + "[ADMIN]");
					} else if(!args[0].equalsIgnoreCase("status") && !args[0].equalsIgnoreCase("toggle") && 
							!args[0].equalsIgnoreCase("cloak") && !args[0].equalsIgnoreCase("help") && 
							!args[0].equalsIgnoreCase("list")) {
						M.message(player, M.mType.HELP, "/admin toggle", ChatColor.RED + "[ADMIN]");
						M.message(player, M.mType.HELP, "/admin spy", ChatColor.RED + "[ADMIN]");
						M.message(player, M.mType.HELP, "/admin cloak ", ChatColor.RED + "[ADMIN]");
						M.message(player, M.mType.HELP, "/admin status", ChatColor.RED + "[ADMIN]");
					}
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("pl") || commandLabel.equalsIgnoreCase("playerlist") || commandLabel.equalsIgnoreCase("who")) {
			String playerList = "";
			for(Player players : Bukkit.getServer().getOnlinePlayers()) {
				if(Permissions.getLevel(players).equalsIgnoreCase("mod")) {
					playerList = playerList + ChatColor.GREEN + players.getName() + ", ";
				} else if(Permissions.getLevel(players).equalsIgnoreCase("admin")) {
					playerList = playerList + ChatColor.RED + players.getName() + ", ";
				} else if(Permissions.getLevel(players).equalsIgnoreCase("owner")) {
					playerList = playerList + ChatColor.DARK_RED + players.getName() + ", ";
				} else if(Permissions.isDonator(players)){
					playerList = playerList + ChatColor.GOLD + players.getName() + ", ";
				} else {
					playerList = playerList + ChatColor.YELLOW + players.getName() + ", ";
				}
			}
			player.sendMessage(ChatColor.GRAY + "Player list - " + ChatColor.DARK_RED + "Owner, " + ChatColor.RED + "Admin, " + ChatColor.GREEN + "Mod, " + ChatColor.GOLD + "Donator");
			player.sendMessage(playerList + ChatColor.AQUA + "(" + Bukkit.getOnlinePlayers().length + ")");
		}
		if(commandLabel.equalsIgnoreCase("mob")) {
			if(!Permissions.isAdmin(player)) {
				utilPlayer.permMsg(player);
			} else {
				if(args.length == 0) {

				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("list")) {
						player.sendMessage(ChatColor.DARK_AQUA + "Entity List:");
						for(Entry<String, Integer> creatures : Utilities.EntityList.entrySet()) {
							String creatureName = WordUtils.capitalize(creatures.getKey().toLowerCase().replaceAll("_", " "));
							int id = creatures.getValue();
							player.sendMessage(ChatColor.DARK_AQUA + creatureName + " - " + ChatColor.AQUA + id);
						}
					}
				} else if(args.length == 3) {
					if(args[0].equalsIgnoreCase("spawn")) {
						@SuppressWarnings("deprecation")
						EntityType eType = EntityType.fromName(args[1].toLowerCase());
						int amount = Integer.parseInt(args[2]);
						@SuppressWarnings("deprecation")
						Location spawnLoc = new Location(player.getWorld(), player.getTargetBlock(null, 200).getX(), player.getTargetBlock(null, 200).getY() + 1, player.getTargetBlock(null, 200).getZ());
						if(amount > 500) {
							player.sendMessage(ChatColor.DARK_AQUA + "Mob: " + ChatColor.GRAY + "You went over the " + ChatColor.DARK_AQUA + "Mob spawn limit" + ChatColor.AQUA + "(500)");
						} else {
							if(eType == null) {
								player.sendMessage(ChatColor.DARK_AQUA + "Mob: " + ChatColor.GRAY + "That creature doesn't exist");
							}
							if(amount == 0) {
								player.sendMessage(ChatColor.DARK_AQUA + "Mob: " + ChatColor.GRAY + "Amount set to default " + ChatColor.DARK_AQUA + "(1)");
								player.getWorld().spawnEntity(spawnLoc, eType);
							} else {
								for(int i = 0; i < amount; i++) {
									player.getWorld().spawnEntity(spawnLoc, eType);
								}
								String eTypeStr = WordUtils.capitalize(eType.toString().toLowerCase().replaceAll("_", " "));
								player.sendMessage(ChatColor.DARK_AQUA + "Mob: " + ChatColor.WHITE + "You spawned " + ChatColor.DARK_AQUA + amount + " " + ChatColor.AQUA + eTypeStr);
							}
						}
					}
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("boss")) {
			int x = 0;
			int y = 0;
			int z = 0;
			if(args.length == 0) {
				if(!Permissions.getLevel(player).equalsIgnoreCase("owner")) {
					utilPlayer.permMsg(player);
				} else {
					player.sendMessage(ChatColor.GREEN + "/boss" + ChatColor.GRAY + " spawn <type> (size)" + ChatColor.DARK_RED + " [OWNER]");
					player.sendMessage(ChatColor.GREEN + "/boss" + ChatColor.GRAY + " list" + ChatColor.DARK_RED + " [OWNER]");
					player.sendMessage(ChatColor.GREEN + "/boss" + ChatColor.GRAY + " status" + ChatColor.RED + " [ADMIN]");
					player.sendMessage(ChatColor.GREEN + "/boss" + ChatColor.GRAY + " kill" + ChatColor.DARK_RED + " [OWNER]");
					return true;
				}
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("status")) {
					if(!Permissions.isAdmin(player)) {
						utilPlayer.permMsg(player);
						return true;
					} else {
						if(!bossLiving) {
							player.sendMessage(ChatColor.DARK_AQUA + "Boss: " + ChatColor.GRAY + "There is no boss alive");
							return true;
						} else {
							player.sendMessage(ChatColor.DARK_AQUA + "Boss Status ---");
							player.sendMessage(ChatColor.DARK_AQUA + "Type: " + ChatColor.WHITE + bossType);
							player.sendMessage(ChatColor.DARK_AQUA + "Health: " + ChatColor.WHITE + bossHealth);
							player.sendMessage(ChatColor.DARK_AQUA + "X/Y/Z: " + ChatColor.AQUA + x + ", " + y + ", " + z);
							return true;
						}
					}
				}
				if(args[0].equalsIgnoreCase("list")) {
					if(!Permissions.getLevel(player).equalsIgnoreCase("owner")) {
						utilPlayer.permMsg(player);
						return true;
					} else {
						player.sendMessage(ChatColor.DARK_AQUA + "Boss Types " + ChatColor.AQUA + "----");
						player.sendMessage(ChatColor.DARK_AQUA + "Slime -" + ChatColor.GRAY + " Spawns a giant slime which deals enormous amounts of damage per hit and has insane amounts of health");
						player.sendMessage(ChatColor.AQUA + "       Health -" + ChatColor.WHITE + " 900.0 " + ChatColor.AQUA + "DPH - " + ChatColor.WHITE + "15 Hearts");
						player.sendMessage(ChatColor.DARK_AQUA + "Snowman -" + ChatColor.GRAY + " Spawns a regular sized snowman which has abilities and extreme amounts of health");
						player.sendMessage(ChatColor.DARK_AQUA + "Iron Golem -" + ChatColor.GRAY + " Spawns a normal Iron Golem which has the ability to throw people into the air");
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("kill")) {
					if(!Permissions.getLevel(player).equalsIgnoreCase("owner")) {
						utilPlayer.permMsg(player);
						return true;
					} else {
						if(!bossLiving) {
							player.sendMessage(ChatColor.DARK_AQUA + "Boss: " + ChatColor.GRAY + "There is no boss alive");
							return true;
						} else {
							for(Entity ents : player.getWorld().getEntities()) {
								if(ents.getUniqueId() == bossUUID) {
									ents.remove();
									if(ents.getType().toString().toLowerCase().equalsIgnoreCase("slime")) {
										Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Boss: " + ChatColor.AQUA + bossArray[0] + ChatColor.WHITE + " was killed by an admin");
										bossLiving = false;
									} else if(ents.getType().toString().toLowerCase().equalsIgnoreCase("iron_golem")) {
										Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Boss: " + ChatColor.AQUA + bossArray[1] + ChatColor.WHITE + " was killed by an admin");
										bossLiving = false;
									} else {
										Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Boss: " + ChatColor.AQUA + bossArray[2] + ChatColor.WHITE + " was killed by an admin");
										bossLiving = false;
									}
								}
							}
						}
					}
				}
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("spawn")) {
					if(!Permissions.getLevel(player).equalsIgnoreCase("owner")) {
						utilPlayer.permMsg(player);
						return true;
					} else {
						String type = args[1].toLowerCase();
						if(type.equalsIgnoreCase("slime")) {
							if(bossLiving) {
								player.sendMessage(ChatColor.DARK_AQUA + "Boss: " + ChatColor.GRAY + "A " + ChatColor.AQUA + bossType + ChatColor.GRAY + " is already living");
								return true;
							} else {
								bossType = WordUtils.capitalize(type.toLowerCase().replaceAll("_", " "));
								bossLiving = true;
								@SuppressWarnings("deprecation")
								Location loc = new Location(player.getWorld(), player.getTargetBlock(null, 50).getX(), player.getTargetBlock(null, 50).getY() + 1, player.getTargetBlock(null, 50).getZ());
								Entity slime = player.getWorld().spawnEntity(loc, EntityType.SLIME);
								bossUUID = slime.getUniqueId();
								((CraftSlime)slime).getHandle().setSize(30);
								x = (int) slime.getLocation().getX();
								y = (int) slime.getLocation().getY();
								z = (int) slime.getLocation().getZ();
								bossHealth = (int) ((CraftSlime)slime).getHandle().getHealth();
							}
						}
					}
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("systime")) {
			player.sendMessage(ChatColor.RED + "Systime: " + ChatColor.WHITE + System.currentTimeMillis());
		}
		if(commandLabel.equalsIgnoreCase("ignore")) {
			if(args.length == 0) {
				M.h(player, "/ignore <player>", "Toggles ignore a player");
				M.h(player, "/ignore status <player>", "Gets if you're ignoring a player");
				return true;
			}
			if(args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Server - " + ChatColor.WHITE + "Queried player is offline");
					return true;
				}
				if(Ignore.isIgnored(player.getName(), target.getName())) {
					Ignore.remove(player.getName(), target.getName());
					M.v(player, "Ignore", ChatColor.WHITE + "You un-ignored " + ChatColor.YELLOW + target.getName());
					return true;
				}
				Ignore.remove(player.getName(), target.getName());
				M.v(player, "Ignore", ChatColor.WHITE + "You ignored " + ChatColor.YELLOW + target.getName());
				return true;
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("status")) {
					
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("gm") || commandLabel.equalsIgnoreCase("gamemode")) {
			if(args.length > 1) {
				player.sendMessage(ChatColor.RED + "Gamemode: " + ChatColor.WHITE + "Incorrect arguments");
				return true;
			}
			if(Permissions.getRank(player) != Ranks.OWNER && Permissions.getRank(player) != Ranks.ADMIN) {
				utilPlayer.permMsg(player);
				return true;
			}
			if(args.length == 0) {
				Gamemode.toggleGamemode(player);
				return true;
			}
			Player target = Bukkit.getServer().getPlayer(args[0]);
			Gamemode.toggleGamemodeOther(target, sender);
		}
		if(commandLabel.equalsIgnoreCase("w") || commandLabel.equalsIgnoreCase("weather")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.AQUA + "Weather commands can only be used in game");
				return true;
			}
			if(!Permissions.isStaff(player)) {
				utilPlayer.permMsg(player);
				return true;
			}
			if(args.length == 0) {
				player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.WHITE + "- Displays help menu");
				player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "off " + ChatColor.WHITE + "- Turns all weather off");
				player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "<rain/thunder> " + ChatColor.WHITE + "- Turns rain or thunder on");
				player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "<rain/thunder> off " + ChatColor.WHITE + "- Turns rain or thunder off");
				player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "help " + ChatColor.WHITE + "- Displays help menu");
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("rain")) {
					if(player.getWorld().hasStorm()) {
						player.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.AQUA + "World already has rain");
						return true;
					}
					player.getWorld().setStorm(true);
					player.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.WHITE + "You set the weather to " + ChatColor.AQUA + "Raining");
					return true;
				}
				if(args[0].equalsIgnoreCase("thunder")) {
					if(!player.getWorld().hasStorm()) {
						player.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.AQUA + "World needs to have rain");
						return true;
					}
					if(player.getWorld().isThundering()) {
						player.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.AQUA + "Already thundering");
						return true;
					}
					player.getWorld().setThundering(true);
					player.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.WHITE + "You set the weather to " + ChatColor.AQUA + "Thundering");
					return true;
				}
				if(args[0].equalsIgnoreCase("off")) {
					player.getWorld().setStorm(false);
					player.getWorld().setThundering(false);
					player.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.WHITE + "You turned all weather off");
					return true;
				}
				if(args[0].equalsIgnoreCase("help")) {
					player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.WHITE + "- Displays help menu");
					player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "off " + ChatColor.WHITE + "- Turns all weather off");
					player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "<rain/thunder> " + ChatColor.WHITE + "- Turns rain or thunder on");
					player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "<rain/thunder> off " + ChatColor.WHITE + "- Turns rain or thunder off");
					player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "help " + ChatColor.WHITE + "- Displays help menu");
					return true;
				}
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("rain")) {
					if(!args[1].equalsIgnoreCase("off")) {
						player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.WHITE + "- Displays help menu");
						player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "off " + ChatColor.WHITE + "- Turns all weather off");
						player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "<rain/thunder> " + ChatColor.WHITE + "- Turns rain or thunder on");
						player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "<rain/thunder> off " + ChatColor.WHITE + "- Turns rain or thunder off");
						player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "help " + ChatColor.WHITE + "- Displays help menu");
						return true;
					}
					if(player.getWorld().hasStorm()) {
						player.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.AQUA + "World doesn't have rain");
						return true;
					}
					player.getWorld().setStorm(false);
					player.getWorld().setThundering(false);
					player.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.WHITE + "You turned " + ChatColor.AQUA + "Rain " + ChatColor.WHITE + "off");
					return true;
				}
				if(args[0].equalsIgnoreCase("thunder")) {
					if(!args[1].equalsIgnoreCase("off")) {
						player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.WHITE + "- Displays help menu");
						player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "off " + ChatColor.WHITE + "- Turns all weather off");
						player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "<rain/thunder> " + ChatColor.WHITE + "- Turns rain or thunder on");
						player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "<rain/thunder> off " + ChatColor.WHITE + "- Turns rain or thunder off");
						player.sendMessage(ChatColor.DARK_AQUA + "/weather " + ChatColor.AQUA + "help " + ChatColor.WHITE + "- Displays help menu");
						return true;
					}
					if(!player.getWorld().isThundering()) {
						player.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.AQUA + "World doesn't have thunder");
						return true;
					}
					player.getWorld().setThundering(false);
					player.sendMessage(ChatColor.DARK_AQUA + "Weather - " + ChatColor.WHITE + "You turned " + ChatColor.AQUA + "Thunder " + ChatColor.WHITE + "off");
					return true;
				}
				if(args[0].equalsIgnoreCase("light")) {
					if(!Utilities.isInteger(args[1])) {
						player.sendMessage("Invalid level");
						return true;
					}
					int level = Integer.parseInt(args[1]);
					utilServer.forceBlockLight(player.getWorld(), (int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ(), level);
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("arr")) {
//			for(String keys : Arena.playerMap.keySet()) {
//				for(ArenaPlayer values : Arena.playerMap.values()) {
//					player.sendMessage(keys + " - " + values.kills + " - " + values.deaths + " - " + values.exp + " - " + Arena.rankToString(values.rank));
//				}
//			}
		}
		if(commandLabel.equalsIgnoreCase("advertisement43155726")) {
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Visit our forum at " + ChatColor.AQUA + "http://hypemc.enjin.com/forum " + ChatColor.DARK_AQUA + "for all the latest news and updates");
		}
		if(commandLabel.equalsIgnoreCase("bugmsg12314321431")) {
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "The plugin still may have some bugs, if you find any please report them at the website or to an admin! Thanks");
		}
		if(commandLabel.equalsIgnoreCase("vote")) {
			player.sendMessage(ChatColor.DARK_GREEN + "Voting Sites - " + ChatColor.WHITE + " HypeMC");
			player.sendMessage(ChatColor.DARK_GREEN + "Minestatus - " + ChatColor.WHITE + "http://adf.ly/TEWWs");
			player.sendMessage(ChatColor.DARK_GREEN + "PMC - " + ChatColor.WHITE + "http://adf.ly/TEWUJ");
		}
		if(commandLabel.equalsIgnoreCase("arenatest")) {
			return true;
		}
		if(commandLabel.equalsIgnoreCase("pitch")) {
			player.sendMessage("" + player.getLocation().getPitch());
		}
		if(commandLabel.equalsIgnoreCase("cooldown")) {
			if(args.length == 0) {
				return true;
			}
			if(args.length == 1) {
//				player.sendMessage("" + ChatColor.GRAY + Cooldown.getRemaining(player.getName(), args[0]));
				Cooldown.coolDurMessage(player, args[0]);
				return true;
			}
			if(args.length == 2) {
				Cooldown.add(player.getName(), args[0].toLowerCase(), Integer.parseInt(args[1]), System.currentTimeMillis());
				utilPlayer.abilityMessage(player, args[0]);
				return true;
			}
		}
		if(commandLabel.equalsIgnoreCase("kill")) {
			if(args.length == 0) {
				player.setHealth(0);
				return true;
			}
			if(args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if(!Permissions.isAdmin(player)) {
					utilPlayer.permMsg(player);
					return true;
				}
				if(target == null) {
					player.sendMessage(ChatColor.GRAY + "Offline player");
					return true;
				}
				if(!Permissions.outranks(player.getName(), target.getName())) {
					player.sendMessage(ChatColor.RED + "Perm - " + ChatColor.WHITE + "You must outrank " + ChatColor.YELLOW + target.getName() + ChatColor.WHITE + " to kill them");
					return true;
				}
				target.setHealth(0);
				player.sendMessage(ChatColor.YELLOW + "You killed " + ChatColor.GOLD + target.getName());
				return true;
			}
		}
		if(commandLabel.equalsIgnoreCase("inv")) {
			if(!Permissions.isAdmin(player)) { utilPlayer.permMsg(player); return true; }
			if(args.length == 0) {
				player.sendMessage(ChatColor.BLUE + "Inv - " + ChatColor.WHITE + "/inv <player>");
				return true;
			}
			if(args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					player.sendMessage(ChatColor.BLUE + "Inv - " + ChatColor.WHITE + "Offline player");
					return true;
				}
				player.openInventory(target.getInventory());
			}
			if(args.length == 2) {
				Player target = Bukkit.getPlayer(args[1]);
				if(args[0].equalsIgnoreCase("enderchest")) {
					if(target == null) {
						player.sendMessage(ChatColor.BLUE + "Inv - " + ChatColor.WHITE + "Offline player");
						return true;
					}
					player.openInventory(target.getEnderChest());
				}
				if(args[0].equalsIgnoreCase("current") || args[0].equalsIgnoreCase("cur")) {
					if(target == null) {
						player.sendMessage(ChatColor.BLUE + "Inv - " + ChatColor.WHITE + "Offline player");
						return true;
					}
					if(player.getOpenInventory() == null) {
						player.sendMessage(ChatColor.BLUE + "Inv - " + ChatColor.YELLOW + target.getName() + ChatColor.WHITE + " isn't viewing an " + ChatColor.YELLOW + "Inventory");
						return true;
					}
					//Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GRAY + "Player " + target.getName());
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("ef") || commandLabel.equalsIgnoreCase("effects")) {
			if(args.length == 0) {
				player.getInventory().addItem(Spawn.getBook());
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("start")) {
					if(!InfoHolder.effects.containsKey(player.getName())) {
						player.sendMessage(ChatColor.GOLD + "Effects - " + ChatColor.WHITE + "You haven't added any effects");
						return true;
					}
					Effects.start(player);
				}
			}
			if(args.length == 2) {
				return true;
			}
//			if(args.length == 3) {
//				if(args[0].equalsIgnoreCase("add")) {
//					if(!Utilities.isInteger(args[1])) {
//						player.sendMessage(ChatColor.GOLD + "Effects - " + ChatColor.WHITE + "You have to input a speed");
//						return true;
//					}
//					int speed = Integer.parseInt(args[1]);
//					if(!args[2].contains(",")) {
//						if(!Utilities.isInteger(args[2])) {
//						player.sendMessage(ChatColor.GOLD + "Effects - " + ChatColor.WHITE + "You have to input a block/s");
//						return true;
//					}
//					int block = Integer.parseInt(args[2]);
//					List<Integer> blocks = new ArrayList<Integer>();
//					blocks.add(block);
//					List<EffectType> effects = Arrays.asList(EffectType.ORBITAL);
//					InfoHolder.effects.put(player.getName(), new EffectPlayer(speed, blocks, effects));
//					}
//				}
//			}
		}
		if(commandLabel.equalsIgnoreCase("npc")) {
			NPC npc = null;
			for(NPC npcs : InfoHolder.npcmanager.getNPCs()) {
				if(npcs.getCraftPlayer().getName().equalsIgnoreCase("Archer")) npc = npcs;
			}
			try {
				int i = Integer.parseInt(args[0]);
				npc.updatePackets(i);
			 	player.sendMessage(npc.getEntity().getEquipment().length + " Length");
			} catch (NumberFormatException e) {
				return false;
			}
			
		}
		if(commandLabel.equalsIgnoreCase("cc")) {
			if(args.length == 0) {
				if(!ChatChannel.hasChannel(player.getName())) {
					M.v(player, "Chat", ChatColor.WHITE + "You're not in a chat channel");
					return false;
				}
				if(ChatChannel.get(player.getName()) == null) {
					player.sendMessage("null");
					return true;
				}
				ChatChannel.get(player.getName()).toggle(player.getName());
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("current") || args[0].equalsIgnoreCase("c")) {
					if(!ChatChannel.hasChannel(player.getName())) {
						M.v(player, "Chat", ChatColor.WHITE + "You're not in a chat channel");
						return false;
					}
					ChatChannel current = ChatChannel.get(player.getName());
					player.sendMessage(ChatColor.DARK_AQUA + "Current Channel");
					player.sendMessage(ChatColor.DARK_AQUA + "Name " + ChatColor.WHITE + current.getName());
					player.sendMessage(ChatColor.DARK_AQUA + "Owner " + ChatColor.WHITE + current.getCreator());
					player.sendMessage(ChatColor.DARK_AQUA + "Members " + current.getMembersStr() + ChatColor.GRAY + " [" + ChatColor.WHITE + current.getSize() + ChatColor.GRAY + "]");
				}
				if(args[0].equalsIgnoreCase("leave")) {
					if(!ChatChannel.hasChannel(player.getName())) {
						M.v(player, "Chat", ChatColor.WHITE + "You're not in a chat channel");
						return false;
					}
				}
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("create")) {
					if(ChatChannel.hasChannel(player.getName())) {
						M.v(player, "Chat", ChatColor.WHITE + "You must leave your current channel");
						return false;
					}
					String name = args[1];
					if(InfoHolder.chats.containsKey(name.toLowerCase())) {
						M.v(player, "Chat", ChatColor.YELLOW + name + ChatColor.WHITE + " already exists");
						return false;
					}
					if(Utilities.containsSpecialChar(name)) {
						M.v(player, "Chat", ChatColor.YELLOW + "Channel Name " + ChatColor.WHITE + "cannot contain special characters");
						return false;
					}
					if(name.length() < 3) {
						M.v(player, "Chat", ChatColor.YELLOW + name + ChatColor.WHITE + " is too short");
						return false;
					}
					if(name.length() > 12) {
						M.v(player, "Chat", ChatColor.YELLOW + name + ChatColor.WHITE + " is too long");
						return false;
					}
					InfoHolder.chats.put(name, new ChatChannel(name, player.getName(), false));
					InfoHolder.chats.get(name).getMembers().put(player.getName(), true);
					M.v(player, "Chat", ChatColor.WHITE + "You created the chat channel " + ChatColor.YELLOW + name);
					return true;
				}
			if(args[0].equalsIgnoreCase("invite")) {
				if(!ChatChannel.hasChannel(player.getName())) {
					M.v(player, "Chat", ChatColor.WHITE + "You're not in a chat channel");
					return true;
				}
				ChatChannel channel = ChatChannel.get(player.getName());
				if(!channel.getCreator().equals(player.getName())) {
					M.v(player, "Chat", ChatColor.WHITE + "You must be the " + ChatColor.YELLOW + "Creator "  + ChatColor.WHITE + "to invite people");
					return true;
				}
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					player.sendMessage(ChatColor.RED + "Server - " + ChatColor.WHITE + "Invalid player");
					return true;
				}
				channel.invite(player, target);
			}
			if(args[0].equalsIgnoreCase("kick")) {
				if(!ChatChannel.hasChannel(player.getName())) {
					M.v(player, "Chat", ChatColor.WHITE + "You're not in a chat channel");
					return true;
				}
				ChatChannel channel = ChatChannel.get(player.getName());
				if(!channel.getCreator().equals(player.getName())) {
					M.v(player, "Chat", ChatColor.WHITE + "You must be the " + ChatColor.YELLOW + "Creator "  + ChatColor.WHITE + "to kick members");
					return true;
				}
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					channel.kick(player, args[0]);
					return true;
				}
				channel.kick(player, target.getName());
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("meta")) {
			if(args.length == 0) {
				player.sendMessage(HInv.invToStr(player.getInventory()));
				player.sendMessage(ChatColor.BOLD + ChatColor.GREEN.toString() + plugin.getServer().getIp());	
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("store")) {
//					HInv.storeInventory(player.getName() + "_test", HInv.invToStr(player.getInventory()));
				}
				if(args[0].equalsIgnoreCase("get")) {
					player.getInventory().clear();
//					Inventory inv = HInv.parseInv(HInv.getInventory(player.getName(), "test"));
//					Inventory inv2 = HInv.parseInv(player, HInv.getInventory(player.getName(), "_test")[0], armour)
//					player.getInventory().setContents(inv.getContents());
				}
				if(args[0].equalsIgnoreCase("head")) {
					Handlers.deathHead(player, player.getLocation());
				}
			}
		}
		return false;
	}
}