package me.loogeh.Hype;


import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.util.utilItems;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilServer;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Give implements CommandExecutor {
	public static Hype plugin;
	private int DEF_AMT = 1;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if (commandLabel.equalsIgnoreCase("gh") && (Permissions.isAdmin(player))) {
			if (sender instanceof Player) {
				if (args.length == 1) {
					ItemStack blockType = player.getItemInHand();
					Player tPlayer1 = player.getServer().getPlayer(args[0]);
					if (tPlayer1 == null) {
						player.sendMessage(ChatColor.RED + "Invalid player!");
						return true;
					} else {
						tPlayer1.getInventory().addItem(blockType);
						player.sendMessage(ChatColor.YELLOW + "You gave " + ChatColor.RED + tPlayer1.getDisplayName() + ChatColor.YELLOW + " the item in your hand!");
						return true;
					}
				}
			}
			return false;
		}
		if (commandLabel.equalsIgnoreCase("g") && (Permissions.isAdmin(player))) {
			if(args.length == 0) {
				player.sendMessage(ChatColor.RED + "/g " + ChatColor.LIGHT_PURPLE + " <player>" + ChatColor.GRAY + " (all) " + ChatColor.LIGHT_PURPLE + "<itemname> <amount>");
				return true;
			}
			if(args.length == 3) {
				int am = Integer.parseInt(args[2]);
				String item = args[1].toLowerCase().replaceAll(" ", "_");
				Material mItem = Material.matchMaterial(item);
				if(args[0].equalsIgnoreCase("all")) {
					if(mItem == null) {
						player.sendMessage(ChatColor.AQUA + "Give: " + ChatColor.GRAY + "That item doesn't exist");
					} else {
						Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " gave all players " + ChatColor.GREEN + am + " " + WordUtils.capitalize(item.toLowerCase().replaceAll("_", " ")));
						for(Player all : utilServer.getPlayers())
							all.getInventory().addItem(new ItemStack(Material.matchMaterial(item), am));
							}
						} else if(!args[0].equalsIgnoreCase("all")) {
							Player tPlayer1 = player.getServer().getPlayer(args[0]);
							tPlayer1.getInventory().addItem(new ItemStack(Material.matchMaterial(item), am));
							Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " gave " + ChatColor.YELLOW + tPlayer1.getName() + ChatColor.GREEN + " " + am + " " + WordUtils.capitalize(item.toLowerCase().replaceAll("_", " ")));
							return true;
				}
			}
			if(args.length == 2) {
				String item = args[0].toLowerCase().replaceAll(" ", "_");
				if(item.equalsIgnoreCase("stone_brick")) {
					item = "smooth_stone";
				}
				int am = Integer.parseInt(args[1]);
				player.getInventory().addItem(new ItemStack(Material.matchMaterial(item), am));
				Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " gave himself" + ChatColor.GREEN + " " + am + " " + WordUtils.capitalize(item.toLowerCase().replaceAll("_", " ")));
			}
		}
		if(commandLabel.equalsIgnoreCase("give")) {
			//PUT /G IN HERE
			if(!Permissions.isAdmin(player)) {
				utilPlayer.permMsg(player);
				return true;
			}
			if(args.length == 0 || args.length > 2) {
				M.message(player, M.mType.HELP, "/give " + ChatColor.GRAY + "<item:data> (amount)", ChatColor.RED + "[ADMIN]");
				M.message(player, M.mType.HELP, "/give " + ChatColor.GRAY + "<player> <item:data> (amount)", ChatColor.RED + "[ADMIN]");
				M.message(player, M.mType.HELP, "/give " + ChatColor.GRAY + "help", ChatColor.RED + "[ADMIN]");
				M.message(player, M.mType.HELP, "/kit " + ChatColor.GRAY + "<player> <kit>", ChatColor.RED + "[ADMIN]");
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("help")) {
					M.message(player, M.mType.HELP, "/give " + ChatColor.GRAY + "<item:data> (amount)", ChatColor.RED + "[ADMIN]");
					M.message(player, M.mType.HELP, "/give " + ChatColor.GRAY + "<player> <item:data> (amount)", ChatColor.RED + "[ADMIN]");
					M.message(player, M.mType.HELP, "/give " + ChatColor.GRAY + "help", ChatColor.RED + "[ADMIN]");
					M.message(player, M.mType.HELP, "/kit " + ChatColor.GRAY + "<player> <kit>", ChatColor.RED + "[ADMIN]");
					return true;
				}
				if(!args[0].equals("help")) {
					if(args[2].isEmpty()) {
						Material item = Material.matchMaterial(args[0]);
						if(item.toString().contains(":")) {
							String dataItem[] = item.toString().split(":");
							short data = Short.valueOf(dataItem[1]);
							item = Material.matchMaterial(dataItem[0]);
							utilItems.giveDataItem(player, player, DEF_AMT, item, data);
							return true;
						}
						utilItems.giveItem(player, player, DEF_AMT, item);
						return true;
					}
					
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("kit") && (Permissions.isAdmin(player))) {
			if(args.length == 0) {
				player.sendMessage(ChatColor.DARK_GREEN + "/kit <player>(all) <type> " + ChatColor.GREEN + "Leather, Iron, Diamond, Chain, Gold");
			}
			else if(args[0].equalsIgnoreCase("all")) {
				if(args[1].equalsIgnoreCase("leather") || args[1].equalsIgnoreCase("archer")) {
					Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave all players an " + ChatColor.GREEN + "Archer Kit");
					for(Player all : utilServer.getPlayers()) {
						utilPlayer.kitArcher(all);
					}
				} else if(args[1].equalsIgnoreCase("iron") || args[1].equalsIgnoreCase("samurai")) {
					Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave all players a " + ChatColor.GREEN + "Samuri Kit");
					for(Player all : utilServer.getPlayers()) {
						utilPlayer.kitSamuri(all);
					}
				} else if(args[1].equalsIgnoreCase("diamond") || args[1].equalsIgnoreCase("jugg") || args[1].equalsIgnoreCase("juggernaut")) {
					Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave all players a " + ChatColor.GREEN + "Juggernaut Kit");
					for(Player all : utilServer.getPlayers()) {
						utilPlayer.kitJuggernaut(all);
					}
				} else if(args[1].equalsIgnoreCase("chain") || args[1].equalsIgnoreCase("spec") || args[1].equalsIgnoreCase("specialist")) {
					Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave all players a " + ChatColor.GREEN + "Specialist Kit");
					for(Player all : utilServer.getPlayers()) {
						utilPlayer.kitSpecialist(all);
					}
				} else if(args[1].equalsIgnoreCase("gold") || args[1].equalsIgnoreCase("agility")) {
					Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave all players an " + ChatColor.GREEN + "Agility Kit");
					for(Player all : utilServer.getPlayers()) {
						utilPlayer.kitAgility(all);
					}
				}
			} else if(!args[0].equalsIgnoreCase("all")) {
				Player target = player.getServer().getPlayer(args[0]);
				if(args[1].equalsIgnoreCase("leather") || args[1].equalsIgnoreCase("archer")) {
					utilPlayer.kitArcher(target);
					target.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " gave you an " + ChatColor.GREEN + "Archer Kit");
					player.sendMessage(ChatColor.GRAY + "You gave an " + ChatColor.GREEN + "Archer Kit " + ChatColor.GRAY + "to " + ChatColor.GREEN + target.getName());
					return true;
				} else if(args[1].equalsIgnoreCase("iron") || args[1].equalsIgnoreCase("samurai")) {
					utilPlayer.kitSamuri(target);
					target.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " gave you a " + ChatColor.GREEN + "Samuri Kit");
					player.sendMessage(ChatColor.GRAY + "You gave a " + ChatColor.GREEN + "Samuri Kit " + ChatColor.GRAY + "to " + ChatColor.GREEN + target.getName());
					return true;
				} else if(args[1].equalsIgnoreCase("diamond") || args[1].equalsIgnoreCase("jugg") || args[1].equalsIgnoreCase("juggernaut")) {
					utilPlayer.kitJuggernaut(target);
					target.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " gave you a " + ChatColor.GREEN + "Juggernaut Kit");
					player.sendMessage(ChatColor.GRAY + "You gave a " + ChatColor.GREEN + "Juggernaut Kit " + ChatColor.GRAY + "to " + ChatColor.GREEN + target.getName());
					return true;
				} else if(args[1].equalsIgnoreCase("chain") || args[1].equalsIgnoreCase("spec") || args[1].equalsIgnoreCase("specialist")) {
					utilPlayer.kitSpecialist(target);
					target.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " gave you a " + ChatColor.GREEN + "Specialist Kit");
					player.sendMessage(ChatColor.GRAY + "You gave a " + ChatColor.GREEN + "Specialist Kit " + ChatColor.GRAY + "to " + ChatColor.GREEN + target.getName());
					return true;
				} else if(args[1].equalsIgnoreCase("gold") || args[1].equalsIgnoreCase("agility")) {
					utilPlayer.kitAgility(target);
					target.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " gave you an " + ChatColor.GREEN + "Agility Kit");
					player.sendMessage(ChatColor.GRAY + "You gave an " + ChatColor.GREEN + "Agility Kit " + ChatColor.GRAY + "to " + ChatColor.GREEN + target.getName());
					return true;
				}
			}
		}
		return false;
	}
}
