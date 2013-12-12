package me.loogeh.Hype;

import me.loogeh.Hype.SQL.Permissions.Ranks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class ScoreboardHandler {
	public static Hype plugin = Hype.plugin;
	
	private static ScoreboardManager manager = plugin.getServer().getScoreboardManager();
	private static Scoreboard board = manager.getNewScoreboard();
	private static Team owner =  board.registerNewTeam("owners");
	private static Team admin = board.registerNewTeam("admins");
	private static Team mod = board.registerNewTeam("mods");
	
	public static void addStaff(Player player, Ranks rank) {
		if(rank == Ranks.OWNER) {
			if(owner.getPrefix() == null) owner.setPrefix(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "OWNER");			
			owner.addPlayer(player);
		}
		if(rank == Ranks.ADMIN) {
			if(admin.getPrefix() == null) admin.setPrefix(ChatColor.RED + ChatColor.BOLD.toString() + "ADMIN");
			admin.addPlayer(player);
		}
		if(rank == Ranks.MODERATOR) {
			if(mod.getPrefix() == null) mod.setPrefix(ChatColor.GREEN + ChatColor.BOLD.toString() + "MOD");
			mod.addPlayer(player);
		}
	}
	
	public static Scoreboard getScoreboard() {
		return board;
	}
}
