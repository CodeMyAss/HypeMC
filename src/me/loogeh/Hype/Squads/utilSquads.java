package me.loogeh.Hype.Squads;

//import org.bukkit.Location;

import java.util.LinkedList;

import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilWorld;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class utilSquads {


	public static hSquad getSquadPlayer(Player player, String squad, boolean inform) {
		LinkedList<hSquad> squadMatchList = new LinkedList<hSquad>();
		for(hSquad sq : Squads.squadMap.values()) {
			if(sq.name.equalsIgnoreCase(squad)) {
				return sq;
			}
			if(sq.name.toLowerCase().contains(squad.toLowerCase())) {
				squadMatchList.add(sq);
			}
		}
		if(squadMatchList.size() == 1) {
			return (hSquad)squadMatchList.get(0);
		}
		String squadMatch = "None";
		if(squadMatchList.size() > 1) {
			for(hSquad sq : squadMatchList) {
				squadMatch = squadMatchList + sq.name + " ";
			}
		}
		player.sendMessage(ChatColor.AQUA + "Search results - " + ChatColor.WHITE + squadMatch);
		return null;
	}

	public static enum SquadCheck {
		OWN,
		ALLY,
		ALLY_TRUST,
		ENEMY,
		ADMIN,
		SAFE;
	}

	public static boolean isSafe(Player players) {
		return false;
	}


	public static hSquad searchSquad(Player player, String name, boolean inform) {
		LinkedList<hSquad> matchList = new LinkedList<hSquad>();
		for(hSquad cur : Squads.squadMap.values()) {
			if(cur.name.equalsIgnoreCase(name)) {
				return cur;
			}
			if(cur.name.toLowerCase().contains(name.toLowerCase())) {
				matchList.add(cur);
			}
		}
		if(matchList.size() != 1) {
			if(!inform) {
				return null;
			}
			player.sendMessage(ChatColor.RED + "More than 1");
		}
		if(matchList.size() > 0) {
			String matchString = "";
			for(hSquad cur : matchList) {
				matchString = matchString + cur.name + " ";
			}
		}
		return null;
	}

	//	public static SquadCheck relPP(String pA, String pB) {
	//		return rel(GetClanP(pA), getClanP(pB));
	//	}

	public static hSquad getSquadByName(String squad) { //getClanC
		return Squads.getSquad(squad);
	}

	public static hSquad getSquadByPlayer(Player player) { //getClanP
		return getSquadP(player.getName());
	}

	public static boolean isSafe2(Player player) {
		return isSafe(player.getLocation());
	}

	public static boolean isSafe(Location loc) {
		for(Region reg : Squads.regionSet) {
			if(reg.contains(loc)) {
				return true;
			}
		}
		if(!Squads.claimMap.containsKey(utilWorld.chunkToStr(loc.getChunk()))) {
			return false;
		}
		return((Claim)Squads.claimMap.get(utilWorld.chunkToStr(loc.getChunk()))).safe;
	}

	public static boolean isChunkHome(hSquad squad, Chunk chunk) {
		if(squad == null) {
			return false;
		}
		if(squad.home == null) {
			return false;
		}
		return squad.home.getChunk().equals(chunk);
	}

	public static Claim getClaim(Location loc) {
		String chunk = utilWorld.chunkToStr(loc.getChunk());
		return(Claim)Squads.claimMap.get(chunk);
	}

	public static Claim acquireClaim(String chunk) {
		return(Claim)Squads.claimMap.get(chunk);
	}

	public static Claim getOwner(String chunk) {
		return (Claim)Squads.claimMap.get(chunk);
	}

	public static hSquad getOwner2(String chunk) {
		Claim claim = acquireClaim(chunk);
		if(claim == null) {
			return null;
		}
		return acquireOwner(claim);
	}

	public static hSquad getOwner(Location loc) {
		Claim claim = getClaim(loc);
		if(claim == null) {
			return null;
		}
		return acquireOwner(claim);
	}

	public static hSquad acquireOwner(Claim claim) {
		return getSquadByName(claim.owner);
	}

	public static String getOwnerStr(Location loc) {
		if(!isClaimed(loc)) {
			return "Wilderness";
		}
		return getClaim(loc).owner;
	}

	public static String getOwnerStringRel(Location loc, String player) {
		SquadCheck rel = relPT(player, utilWorld.chunkToStr(loc.getChunk()));
		return mRel(rel, getOwnerStr(loc));
	}

	public static boolean isClaimed(Location loc) {
		String chunk = utilWorld.chunkToStr(loc.getChunk());
		return Squads.claimMap.containsKey(chunk);
	}

	//	public static boolean isAlliance(String player, Location loc) {
	//		Player bPlayer = Bukkit.getServer().getPlayer(player);
	//		if(!Squads.hasSquad(bPlayer)) {
	//			return false;
	//		}
	//		if(!isClaimed(loc)) {
	//			return false;
	//		}
	//		return getOwner(getClaim(loc).isAlly(getSquadByPlayer(player)).clan.name);
	//	}

	public static boolean isAdmin(Location loc) {
		if(!isClaimed(loc)) {
			return false;
		}
		return getOwner(loc).admin;
	}

	public static boolean isSpecial(Location loc, String special) {
		if(!isClaimed(loc)) {
			return false;
		}
		if(!isAdmin(loc)) {
			return false;
		}
		return getOwner(loc).name.toLowerCase().contains(special.toLowerCase());
	}

	public static SquadCheck getAccess(Player player, Location loc) {
		if(utilPlayer.adminMode(player)) {
			return SquadCheck.OWN;
		}
		if(!isClaimed(loc)) {
			return SquadCheck.OWN;
		}

		hSquad claimSquad = getOwner(loc);

		if(claimSquad == null) {
			return SquadCheck.OWN;
		}
		if(claimSquad.isMember(player.getName())) {
			return SquadCheck.OWN;
		}
		hSquad playerSquad = getSquadByPlayer(player);

		if((playerSquad != null) && (claimSquad.getTrust(playerSquad.name))) {
			return SquadCheck.ALLY_TRUST;
		}

		if((playerSquad != null) && (claimSquad.isAlly(playerSquad.name))) {
			return SquadCheck.ALLY;
		}
		return SquadCheck.ENEMY;
	}
	public static SquadCheck rel(hSquad sA, hSquad sB) {
		if((sA == null) || (sB == null)) {
			return SquadCheck.ENEMY;
		}
		if((sA.admin) || (sB.admin)) {
			return SquadCheck.ADMIN;
		}
		if(sA.name.equals(sB.name)) {
			return SquadCheck.OWN;
		}
		if(sA.getTrust(sB.name)) {
			return SquadCheck.ALLY_TRUST;
		}
		if(sA.isAlly(sB.name)) {
			return SquadCheck.ALLY;
		}
		return SquadCheck.ENEMY;
	}

	public static SquadCheck relationPlayerPlayer(String pA, String pB) {
		return rel(getSquadP(pA), getSquadP(pB));
	}

	public static SquadCheck relSquadSquad(String sA, String sB) {
		return rel(searchSquad(null, sA, false), searchSquad(null, sB, false));
	}

	public static SquadCheck relationTer(String tA, String tB) {
		return rel(getOwner2(tA), getOwner2(tB));
	}

	public static SquadCheck relPS(String pA, String sB) {
		return rel(getSquadP(pA), searchSquad(null, sB, false));
	}

	public static SquadCheck relPS(String pA, hSquad sB) {
		return rel(getSquadP(pA), sB);
	}

	public static SquadCheck relPT(String pA, String tB) {
		Claim claim = acquireClaim(tB);
		if((claim != null) && (claim.safe)) {
			return SquadCheck.SAFE;
		}
		return rel(getSquadP(pA), getOwner2(tB));
	}

	public static SquadCheck relST(String sA, String tB) {
		Claim claim = acquireClaim(tB);
		if((claim != null) && (claim.safe)) {
			return SquadCheck.SAFE;
		}
		return rel(searchSquad(null, sA, false), getOwner2(tB));
	}

	public static String relationColour(SquadCheck check) {
		if(check == SquadCheck.SAFE) return ChatColor.LIGHT_PURPLE + "SAFE";
		if(check == SquadCheck.ADMIN) return ChatColor.WHITE + "";
		if(check == SquadCheck.OWN) return ChatColor.YELLOW + "";
		if(check == SquadCheck.ALLY_TRUST) return ChatColor.GREEN + "";
		if(check == SquadCheck.ALLY) return ChatColor.DARK_GREEN + "";
		return ChatColor.RED + "";
	}

	public static String mRel(SquadCheck relation, String message) {
		return relationColour(relation) + message + ChatColor.WHITE;
	}

	public static boolean playerSelf(String pA, String pB) {
		hSquad sA = getSquadP(pA);
		hSquad sB = getSquadP(pB);

		if((pA == null) || (pB == null)) {
			return false;
		}
		return sA.isSelf(sB.name);
	}

	public static boolean playerAlly(String pA, String pB) {
		hSquad sA = getSquadP(pA);
		hSquad sB = getSquadP(pB);
		if((pA == null) || (pB == null)) {
			return false;
		}
		return sA.isAlly(sB.name);
	}

	public static boolean playerEnemy(String pA, String pB)
	{
		hSquad sA = getSquadP(pA);
		hSquad sB = getSquadP(pB);

		if ((sA == null) || (sB == null)) {
			return true;
		}
		return (!sA.isAlly(sB.name)) && (!sA.isSelf(sB.name));
	}
	
	public static boolean canDamage(Player damagee, Player damager) {
		if(isSafe2(damagee)) {
			return false;
		}
		if(damager == null) {
			return true;
		}
		SquadCheck check = relationPlayerPlayer(damagee.getName(), damager.getName());
		if((check == SquadCheck.ALLY) || (check == SquadCheck.ALLY_TRUST) || (check == SquadCheck.OWN)) {
			return false;
		}
		return true;
	}
	
	public static hSquad getSquadP(String name) {
		return searchSquad(null, name, false);
	}


}
