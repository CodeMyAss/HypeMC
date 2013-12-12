package me.loogeh.Hype.armour;

public class Armour {

	
	
	public enum Kit {
		NONE(-1, "None"),
		ARCHER(0, "Archer"),
		AGILITY(1, "Agility"),
		SPECIALIST(2, "Specialist"),
		SAMURAI(3, "Samurai"),
		JUGGERNAUT(4, "Juggernaut");
		
		private int id;
		private String name;
		
		Kit(int id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public int getID() {
			return this.id;
		}
		
		public String getName() {
			return this.name;
		}
		
		public static Kit getKitByID(int id) {
			if(id == -1) return NONE;
			else if(id == 0) return ARCHER;
			else if(id == 1) return AGILITY;
			else if(id == 2) return SPECIALIST;
			else if(id == 3) return SAMURAI;
			else if(id == 4) return JUGGERNAUT;
			else return NONE;
		}
	}
}
