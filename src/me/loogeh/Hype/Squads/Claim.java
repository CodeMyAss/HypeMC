package me.loogeh.Hype.Squads;

public class Claim {

	public String owner = "";
	public String chunk = "";
	
	public boolean safe = false;
	
	public Claim(String owner, String chunk, boolean safe) {
		this.owner = owner;
		this.chunk = chunk;
		this.safe = safe;
	}
	
	public String getOwner() {
		hSquad squad = utilSquads.getSquadByName(this.owner);
		if((squad == null) || (!squad.admin)) {
			return this.owner;
		}
		return this.owner;
	}
}
