package me.loogeh.Hype.economy;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.util.Utilities;
import me.loogeh.Hype.util.utilPlayer;

public class Auctions implements CommandExecutor {
	public static Hype plugin;
	public static HashMap<String, Integer> auction = new HashMap<String, Integer>();
	public static boolean auctionAvailable = false;
	public static String itemName;
	public static String curBidder;
	public static int highestBid; 
	public static ItemStack item;
	
	public static void startAuction(Player starter, int startingPrice, ItemStack item, String name) {
		if(auctionAvailable == true) {
			starter.sendMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "Auction already in progress");
		} else {
			Bukkit.broadcastMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "Auction started for " + ChatColor.GREEN + name);
			Bukkit.broadcastMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "Type " + ChatColor.YELLOW + "/bid <amount> " + ChatColor.GRAY + "to bid!");
		}
	}
	
	public static void bid(Player player, int amount) {
		if(auctionAvailable == false) {
			player.sendMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "There is no active auction");
		} else if(auctionAvailable == true) {
			if(auction.isEmpty()) {
				auction.put(player.getName(), amount);
			} else if(!auction.isEmpty()) {
				if(amount > auction.get(player.getName())) {
					if(Money.getMoney(player) < amount) {
						player.sendMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "You have insufficient funds");
					} else if(Money.getMoney(player) > amount) {
					auction.clear();
					auction.put(player.getName(), amount);
					highestBid = amount;
					Bukkit.broadcastMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "A new highest bid of " + ChatColor.GREEN + "$" + amount);
					}
				} else if(amount < auction.get(player.getName())) {
					player.sendMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "You need to beat a highest bid of " + ChatColor.GREEN + "$" + highestBid);
				
				}
			}
		}
	}
	
	public static void stopAuctionWithoutWinner() {
		if(auctionAvailable == false) {
			System.out.println("Auction: There is no auction to end");
		} else {
			auctionAvailable = false;
		if(auction.isEmpty()) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "Auction for " + ChatColor.GREEN + itemName + ChatColor.GRAY + " has been ended");
		} else if(!auction.isEmpty()) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "The auction for " + ChatColor.GREEN + itemName + ChatColor.GRAY + " has been ended");
			auction.clear();
			}
		}
		
	}
	
	public static void stopAuction() {
		if(auctionAvailable == false) {
			System.out.println("Auction: There is no auction to end");
		} else {
			auctionAvailable = false;
			if(auction.isEmpty()) {
				Bukkit.broadcastMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "Auction for " + ChatColor.YELLOW + itemName + ChatColor.GRAY + " has ended");
			} else {
				Bukkit.broadcastMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "The winner of " + ChatColor.YELLOW + itemName + ChatColor.GRAY + " is " + ChatColor.GOLD + curBidder);
				Player winner = Bukkit.getServer().getPlayerExact(curBidder);
				winner.getInventory().addItem(item);
				auction.clear();
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(commandLabel.equalsIgnoreCase("auction")) {
				if(args.length == 0) {
					if(!Permissions.isAdmin(player)) {
						player.sendMessage(ChatColor.RED + "/auction" + ChatColor.LIGHT_PURPLE + " status");
						player.sendMessage(ChatColor.RED + "/bid" + ChatColor.LIGHT_PURPLE + " <amount>");
					} else {
						player.sendMessage(ChatColor.RED + "/auction" + ChatColor.LIGHT_PURPLE + " start" + ChatColor.GRAY + " <start price> <name>");
						player.sendMessage(ChatColor.RED + "/auction" + ChatColor.LIGHT_PURPLE + " stop");
						player.sendMessage(ChatColor.RED + "/auction" + ChatColor.LIGHT_PURPLE + " status");
						player.sendMessage(ChatColor.RED + "/bid" + ChatColor.LIGHT_PURPLE + " <amount>");
					}
				}
				else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("status")) {
						if(!Permissions.isAdmin(player)) {
							if(auctionAvailable == false) {
								player.sendMessage(ChatColor.GOLD + "In progress: " + ChatColor.WHITE + "False");
								player.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.WHITE + "No auction available");
								player.sendMessage(ChatColor.GOLD + "Highest bid: " + ChatColor.WHITE + "No auction available");
								return true;
							} else {
								if(auction.isEmpty()) {
									player.sendMessage(ChatColor.GOLD + "In progress: " + ChatColor.WHITE + "True");
									player.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.WHITE + itemName);
									player.sendMessage(ChatColor.GOLD + "Highest bid: " + ChatColor.WHITE + "Empty auction");
									player.sendMessage(ChatColor.GOLD + "Bidder: " + ChatColor.WHITE + "Empty auction");
									return true;
								} else {
									player.sendMessage(ChatColor.GOLD + "In progress: " + ChatColor.WHITE + "True");
									player.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.WHITE + itemName);
									player.sendMessage(ChatColor.GOLD + "Bidder: " + ChatColor.WHITE + curBidder);
									player.sendMessage(ChatColor.GOLD + "Highest bid: " + ChatColor.WHITE + "$" + getHighestBid());
									return true;
								}
							}
						}
					} else if(args[0].equalsIgnoreCase("stop")) {
						if(!Permissions.isAdmin(player)) {
							utilPlayer.permMsg(player);
						} else {
							if(auctionAvailable = false) {
								player.sendMessage(ChatColor.GOLD + "Auction: " + ChatColor.GRAY + "No auction available");
							} else {
								stopAuctionWithoutWinner();
							}
						}
					}
				} else if(args.length >= 3) {
					if(args[0].equalsIgnoreCase("start")) {
						if(!Permissions.isAdmin(player)) {
							utilPlayer.permMsg(player);
						} else {
							int startPrice = Integer.parseInt(args[1]);
							ItemStack bidItem = player.getItemInHand();
							String iName = Utilities.argsToString3(args);
							startAuction(player, startPrice, bidItem, iName);
							item = bidItem;
							itemName = iName;
							}
						}
					}
				}
			}
		return false;
	}
	
	
	public static int getHighestBid() {
		if(auction.isEmpty()) {
			return 0;
		} else {
			return auction.get(curBidder);
		}
	}
}
