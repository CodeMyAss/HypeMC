package me.loogeh.Hype;

import me.loogeh.Hype.SQL.Permissions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommandHandler implements CommandExecutor {
	public static Hype plugin;

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){        
		if (!(sender instanceof Player)){
//			if(args.length == 1){
//				Player player = sender.getServer().getPlayer(args[0]);
//				if(player == null){
//					sender.sendMessage(ChatColor.RED + "Invalid Player");
//					return true;
//				}
//				else{
//					Gamemode.toggleGamemodeOther(player, sender);
//					return true;
//				}
//			}
//			else{
//				sender.sendMessage(ChatColor.RED + "Incorrect syntax!");
//				return true;
//			}
		} else if (sender instanceof Player){
			Player player = (Player) sender;
        	if(args.length == 0){
        		Gamemode.toggleGamemode(player);
        		return true;
        	}
        		
        	else if(args.length == 1){
        		if(Permissions.getLevel(player).equalsIgnoreCase("owner")){
        				
        			Player targetPlayer = player.getServer().getPlayer(args[0]);
        			if(targetPlayer == null){
        				player.sendMessage(ChatColor.RED +  "Invalid Player");
        				return true;
       				}    				
       				else{
       					Gamemode.toggleGamemodeOther(targetPlayer, sender);
       					return true;
       				}
       			}
   				else{    					
   					player.sendMessage(ChatColor.RED + "You don't have permissions to change other people's game mode");
    				return true;
    			}
        	}
        		
        	else{
        		player.sendMessage(ChatColor.RED +  "Incorrect syntax!");
        		return true;
        	}
        }
		return false;

	}

}
