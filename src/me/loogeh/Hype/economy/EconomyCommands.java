package me.loogeh.Hype.economy;


import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommands implements CommandExecutor {
	public static Hype plugin;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if(commandLabel.equalsIgnoreCase("money")) {
			if(args.length == 0) {
				player.sendMessage(ChatColor.DARK_GREEN + "Money: " + ChatColor.WHITE + Money.getMoney(player));
				return true;
			} else if(args.length == 3) {
				if(args[0].equalsIgnoreCase("pay")) {
					Player target = Bukkit.getPlayer(args[1]);
					if(target != null) {
						int amount = Integer.parseInt(args[2]);
						Money.subtractMoney(player, amount);
						Money.addMoney(target, amount);
						player.sendMessage(ChatColor.DARK_GREEN + "Money: " + ChatColor.WHITE + "You payed " + ChatColor.AQUA + target.getName() + ChatColor.DARK_GREEN + " $" + amount);
						target.sendMessage(ChatColor.DARK_GREEN + "Money: " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " payed you " + ChatColor.DARK_GREEN + "$" + amount);
					} else {
						player.sendMessage(ChatColor.DARK_GREEN + "Money: " + ChatColor.GRAY + "That player isn't online");
					}
				}
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("top")) {
					player.sendMessage(ChatColor.YELLOW + "Richest players:");
					Money.sendTopList(player);
				} else {
					Player target = Bukkit.getPlayer(args[0]);
					if(target != null) {
						player.sendMessage(ChatColor.DARK_GREEN + target.getName() + "'s Money: " + ChatColor.WHITE + Money.getMoney(target));
					} else {
						String money = Integer.toString(Money.getMoneyFromStr(args[0]));
						if(money != null) {
							player.sendMessage(ChatColor.DARK_GREEN + args[0] + "'s Money: " + ChatColor.WHITE + Money.getMoneyFromStr(args[0]));
						} else {
							player.sendMessage(ChatColor.DARK_GREEN + args[0] + "'s Money: " + ChatColor.GRAY + "That player is offline, please use their full username");
						}
					}
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("econ") || (commandLabel.equalsIgnoreCase("economy"))) {
			if(!Permissions.getLevel(player).equalsIgnoreCase("owner")) {
				utilPlayer.permMsg(player);
			} else if(Permissions.getLevel(player).equalsIgnoreCase("owner")) {
				if(args.length == 0) {
					player.sendMessage(ChatColor.RED + "/economy" + ChatColor.LIGHT_PURPLE + " add" + ChatColor.GRAY + " <player> <amount>");
					player.sendMessage(ChatColor.RED + "/economy" + ChatColor.LIGHT_PURPLE + " sub" + ChatColor.GRAY + " <player> <amount>");
					player.sendMessage(ChatColor.RED + "/economy" + ChatColor.LIGHT_PURPLE + " set " + ChatColor.GRAY + "<player> <amount>");
				} else if(args.length == 3) {
					Player tp = player.getServer().getPlayer(args[1]);
					int amt = Integer.parseInt(args[2]);
					if(args[0].equalsIgnoreCase("add")) {
						Money.addMoney(tp, amt);
						player.sendMessage(ChatColor.DARK_GREEN + "Economy: " + ChatColor.GRAY + "You added " + ChatColor.DARK_GREEN + "$" + amt + ChatColor.GRAY + " to " + ChatColor.YELLOW + tp.getName() + "'s balance");
						tp.sendMessage(ChatColor.DARK_GREEN + "Economy: " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " added " + ChatColor.DARK_GREEN + "$" + amt + ChatColor.GRAY + " to your balance");
					} else if(args[0].equalsIgnoreCase("sub")) {
						Money.addMoney(tp, amt);
						player.sendMessage(ChatColor.DARK_GREEN + "Economy: " + ChatColor.GRAY + "You subtracted " + ChatColor.DARK_GREEN + "$" + amt + ChatColor.GRAY + " to " + ChatColor.YELLOW + tp.getName() + "'s balance");
						tp.sendMessage(ChatColor.DARK_GREEN + "Economy: " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " subtracted " + ChatColor.DARK_GREEN + "$" + amt + ChatColor.GRAY + " from your balance");
					} else if(args[0].equalsIgnoreCase("set")) {
						Money.setMoney(player, amt);
						player.sendMessage(ChatColor.DARK_GREEN + "Economy: " + ChatColor.GRAY + "You set " + ChatColor.YELLOW + tp.getName() + "'s " + ChatColor.GRAY + "money to " + ChatColor.DARK_GREEN + "$" + amt);
					}
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("claimvote")) {
			if(!Votes.hasVoted(player.getName())) {
				player.sendMessage(ChatColor.DARK_GREEN + "Votes - " + ChatColor.WHITE + "You have not voted");
				return true;
			}
			int votes = Votes.getVotes(player.getName());
			Votes.withdrawVote(player.getName());
			if(votes < 1) {
				player.sendMessage(ChatColor.DARK_GREEN + "Votes - " + ChatColor.WHITE + "You have not voted");
				return true;
			}
			if(votes == 1) {
				player.sendMessage(ChatColor.DARK_GREEN + "Votes - " + ChatColor.WHITE + "You reveived " + ChatColor.DARK_GREEN + "$" + votes * 5000 + ChatColor.WHITE + " from " + ChatColor.YELLOW + votes + " vote");
				return true;
			}
			player.sendMessage(ChatColor.DARK_GREEN + "Votes - " + ChatColor.WHITE + "You reveived " + ChatColor.DARK_GREEN + "$" + votes * 5000 + ChatColor.WHITE + " from " + ChatColor.YELLOW + votes + " votes");
		}
		if(commandLabel.equalsIgnoreCase("votes")) {
			if(!Votes.hasVoted(player.getName())) {
				player.sendMessage(ChatColor.DARK_GREEN + "Votes - " + ChatColor.WHITE + "You have not voted");
				return true;
			}
			int votes = Votes.getVotes(player.getName());
			int totalVotes = Votes.getTotalVotes(player.getName());
			player.sendMessage(ChatColor.DARK_GREEN + "Vote Info - " + ChatColor.WHITE + "Player " + player.getName());
			player.sendMessage(ChatColor.DARK_GREEN + "Votes - " + ChatColor.WHITE + votes);
			player.sendMessage(ChatColor.DARK_GREEN + "Available Money - " + ChatColor.WHITE + "$" + votes * 5000);
			player.sendMessage(ChatColor.DARK_GREEN + "Total Votes - " + ChatColor.WHITE + totalVotes);
			player.sendMessage(ChatColor.DARK_GREEN + "Total Claimed - " + ChatColor.WHITE + "$" + (totalVotes - votes) * 5000);
		
		}
		return false;
	}

}
