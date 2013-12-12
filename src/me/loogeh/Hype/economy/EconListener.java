package me.loogeh.Hype.economy;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.armour.Armour.Kit;
import me.loogeh.Hype.util.utilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class EconListener implements Listener {
	public static Hype plugin;
	public EconListener(Hype instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void econJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ResultSet rs = MySQL.doQuery("SELECT player FROM money WHERE player ='" + player.getName() + "'");
		try {
			if(!rs.next()) {
				MySQL.doUpdate("INSERT INTO `money`(`player`, `money`) VALUES ('" + player.getName() + "', 5000)");
				utilPlayer.setKit(player, Kit.ARCHER);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(!Votes.hasVoted(player.getName())) {
			return;
		} else {
			if(Votes.getVotes(player.getName()) == 0) {
				return;
			}
			player.sendMessage(ChatColor.DARK_GREEN + "Votes - " + ChatColor.WHITE + "You have " + ChatColor.DARK_GREEN + Votes.getVotes(player.getName()) + ChatColor.WHITE + " to claim");
			player.sendMessage(ChatColor.DARK_GREEN + "Votes - " + ChatColor.WHITE + "Type " + ChatColor.DARK_GREEN + "/claimvote " + ChatColor.WHITE + "to claim");
		}
		
	}
	
	@EventHandler
	public void votifierEvent(VotifierEvent event) {
		Vote vote = event.getVote();
		Votes.addVote(vote.getUsername(), 1);
		Player vPlayer = Bukkit.getPlayer(vote.getUsername());
		if(vPlayer == null) {
			return;
		}
		vPlayer.sendMessage(ChatColor.DARK_GREEN + "Votes - " + ChatColor.WHITE + "You voted at " + ChatColor.DARK_GREEN + vote.getServiceName());
	}

}
