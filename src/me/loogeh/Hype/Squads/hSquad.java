package me.loogeh.Hype.Squads;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
import org.bukkit.Location;
//import org.bukkit.entity.Player;

import me.loogeh.Hype.Hype;
import me.loogeh.Hype.SQL.MySQL;
//import me.loogeh.Hype.util.Utilities;
//import me.loogeh.Hype.util.utilTime;
//import me.loogeh.Hype.util.utilTime.TimeUnit;
import me.loogeh.Hype.util.utilWorld;

public class hSquad {
	public static Hype plugin;

	public String name = "";
	public Location home = null;

	public String date = "";

	public boolean admin = false;

	public int power = 0;
	public long powerTime = 0L;

	public long systime = 0L;

	public long lastOnline = 0L;

	public HashMap<String, Boolean> allyMap = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> enemyMap = new HashMap<String, Boolean>(); //Stores Squad - Enemy
	public HashMap<String, hSquad.Rank> memberMap = new HashMap<String, hSquad.Rank>();
	public HashSet<String> claimSet = new HashSet<String>();

	public HashMap<String, Long> inviteeMap = new HashMap<String, Long>();
	public HashMap<String, String> inviterMap = new HashMap<String, String>();

	public HashMap<String, Long> requestMap = new HashMap<String, Long>();

	public hSquad(String name, int power, String home, boolean admin, String date, long systime) {
		this.name = name;
		this.power = power;
		this.home = utilWorld.strToLoc(home);
		this.admin = admin;
		this.date = date;
		this.systime = systime;
	}

	public static enum Rank {
		LEADER(3),
		ADMIN(2),
		MEMBER(1),
		NONE(0);

		private int i;

		private Rank(int value) {
			this.i = value;
		}

		public int toInt() {
			return this.i;
		}
	}


	public int getPower() {
		if(this.admin) {
			return 10000;
		}
		return getPowerMax() - this.power;
	}

	public int getPowerMax() {
		if(this.admin) {
			return 10000;
		} else {
			int maxPower = 2 + this.memberMap.size();

			if(maxPower > 10) {
				maxPower = 10;
			}
			return maxPower;
		}
	}

	public int getClaims() {
		return this.claimSet.size();
	}

	public int getClaimsMax() {
		if(this.admin) {
			return 10000;
		} else {
			return 2 + this.memberMap.size();
		}
	}

	public int getAlliesMax() {
		if(this.admin) {
			return 10000;
		} else {
			return 8 + this.memberMap.size();
		}
	}

	public int getAllies() {
		return this.allyMap.size();
	}

	public boolean isRequested(String squad) {
		if(!this.requestMap.containsKey(squad)) {
			return false;
		} else {
			return (this.requestMap.containsKey(squad));
		}
	}

	public boolean isInvited(String player) {
		return this.inviteeMap.containsKey(player);
	}

	public boolean isMember(String squad) {
		return this.memberMap.containsKey(squad);
	}

	public boolean isAlly(String squad) {
		return this.allyMap.containsKey(squad);
	}

	public boolean isSelf(String squad) {
		return this.name.equals(squad);
	}

	public boolean isEnemy(String squad) {
		return (!isAlly(squad)) && (!isSelf(squad));
	}

	public void changePower(int change) {
		int newPower = this.power - change;
		setPower(newPower);
	}

	public void setPower(int pwr) {
		this.power = pwr;

		if(this.power < 0) {
			this.power = 0;
		}
		if(this.power > getPowerMax()) {
			this.power = getPowerMax();
		}
	}

	public boolean getTrust(String squad) {
		if(!this.allyMap.containsKey(squad)) {
			return false;
		} else {
			return ((Boolean)this.allyMap.get(squad).booleanValue());
		}
	}

	public void write() {
		MySQL.doUpdate("REPLACE INTO squad (squad,desc,power,home,admin,date,systime) VALUES ('" + this.name + "', '" + "', " + this.power + ", '" + utilWorld.locToStr(home)
				+ "', " + this.admin + ", '" + this.date + "', " + this.systime + ")");
	}

	//	  public LinkedList<String> mDetails(String player) {
	//		  LinkedList<String> stringList = new LinkedList<String>();
	//		  
	//		  stringList.add("Squads: " + this.name + "Info");
	//		  stringList.add("Desc: " + this.desc);
	//		  stringList.add("Power: " + getPower() + "/" + getPowerMax());
	//		  if(this.power > 0) {
	//			  stringList.add("Power Gain: " + utilTime.convertString(Squads.powerTime - this.powerTime, TimeUnit.BEST, 1));
	//		  } else {
	//			  stringList.add("Power Gain: Full Power");
	//		  }
	//		  stringList.add("Territory" + getClaims() + "/" + getClaimsMax());
	//		  stringList.add("Home" + utilWorld.locToStr(this.home));
	//		  stringList.add("Created" + this.date);
	//		  
	//		  String allies = "";
	//		  for(String cur : Utilities.sortKey(this.allyMap.keySet())) {
	////			  allies = allies + utilSquads.mRel(utilSquads.relPC(player (hSquad)Squads.squadMap.get(cur)), cur) + ", ";
	//		  }
	//		  stringList.add("Allies: " + allies);
	//		  
	//		  String members = "";
	//		  for(String cur : Utilities.sortKey(this.memberMap.keySet())) {
	//			  String name = ChatColor.RED + cur;
	//			  if(Bukkit.getServer().getPlayer(cur).isOnline()) {
	//				  name = ChatColor.GREEN + cur;
	//			  }
	//			  
	//			  if(this.memberMap.get(cur) == hSquad.Rank.LEADER) {
	//				  members = members + ChatColor.YELLOW + "L-" + name + ChatColor.YELLOW + ", ";
	//			  }
	//			  if(this.memberMap.get(cur) == hSquad.Rank.ADMIN) {
	//				  members = members + ChatColor.YELLOW + "A-" + name + ChatColor.YELLOW + ", ";
	//			  }
	//			  if(this.memberMap.get(cur) == hSquad.Rank.MEMBER) {
	//				  members = members + ChatColor.YELLOW + "M-" + name + ChatColor.YELLOW + ", ";
	//			  }
	//		  }
	//		  
	//	  }

	public LinkedList<String> mTerritory() {
		LinkedList<String> stringList = new LinkedList<String>();
		stringList.add("Squads: " + this.name + "Territory:");

		for(String cur : this.claimSet) {
			stringList.add(cur);
		}
		return stringList;
	}
}
