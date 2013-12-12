package me.loogeh.Hype;

import java.util.Calendar;
import java.util.logging.Logger;

import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.Spawn.InfoCentre;
import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.Squads2.SquadsCommands;
import me.loogeh.Hype.Squads2.SquadsListener;
import me.loogeh.Hype.armour.Archer;
import me.loogeh.Hype.armour.Chain;
import me.loogeh.Hype.armour.Gold;
import me.loogeh.Hype.armour.Iron;
import me.loogeh.Hype.bettershops.BetterShops;
import me.loogeh.Hype.bettershops.ShopListener;
import me.loogeh.Hype.bettershops.ShopsCommands;
import me.loogeh.Hype.donations.DonationCommands;
import me.loogeh.Hype.donations.DonationListener;
import me.loogeh.Hype.donations.Morphs;
import me.loogeh.Hype.donations.Seizure;
import me.loogeh.Hype.economy.Auctions;
import me.loogeh.Hype.economy.EconListener;
import me.loogeh.Hype.economy.EconomyCommands;
import me.loogeh.Hype.entity.NPCManager;
import me.loogeh.Hype.games.GameCommands;
import me.loogeh.Hype.games.GameListener;
import me.loogeh.Hype.games.KACommands;
import me.loogeh.Hype.games.KAListener;
import me.loogeh.Hype.games.Games;
import me.loogeh.Hype.games.TrespassersListener;
import me.loogeh.Hype.moderation.Ban;
import me.loogeh.Hype.moderation.ModerationCommands;
import me.loogeh.Hype.moderation.ModerationListener;
import me.loogeh.Hype.moderation.Mute;
import me.loogeh.Hype.util.utilServer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
//2UNN9KKYPI
public class Hype extends JavaPlugin {
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Hype plugin;
	
	public static long sysTime;
	public static int loopThread;
	public static int reloadThread;
	
	public Ride ride;
	public Teleport tp;
	public GamemodeAll gma;
	public RCommands rc;
	public Say say;
	public Enchant enc;
	public Message msg;
	public Give give;
	public EconomyCommands mny;
	public Auctions act;
	public KACommands arena;
	public ModerationCommands mc;
	public SquadsCommands sc;
	public DonationCommands dc;
	public TeleportSkulls tps;
	public ShopsCommands shops;
	public Client client;
	public Spawn spawn;
	public Seizure seizure = new Seizure();
	public UnknownCommand uc;
	public NPCManager npcmanager;
	public GameCommands gc;
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		Bukkit.getServer().getWorld("world").save();
		Bukkit.getServer().getWorld("world_nether").save();
//		if(getServer().getIp().equalsIgnoreCase("119.252.190.191")) 
		Squads.save();
	//	HPlayer.write(); Fix this - ConcurrentModificationException
		this.logger.info(pdfFile.getName() + " has been disabled");
		saveConfig();
		Hype.plugin = null;
		
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " has been enabled");
		Hype.plugin = this;
		
		if(Bukkit.getOnlineMode() == false) {
			System.out.println("System in offline mode. Server automatically shutdown. Date - " + Calendar.getInstance().getTime());
			Bukkit.shutdown();
		}
		
		try {
			MySQL.setupMySql();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		utilServer.lockCheck();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		
//		if(getServer().getIp().equalsIgnoreCase("119.252.190.191")) {
			Squads.load();
			Mute.load();
			Ban.load();
//		}
		Morphs.setupDisguiseCraft();
		BetterShops.populateHashMap();
		Games.init();
		
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new SquadsListener (this), this);
		pm.registerEvents(new Listener (this), this);
		pm.registerEvents(new Chain (this), this);
		pm.registerEvents(new Gold (this), this);
		pm.registerEvents(new Archer (this), this);
		pm.registerEvents(new Iron (this), this);
//		pm.registerEvents(new Jugg (this), this);
		pm.registerEvents(new KAListener(this), this);
		pm.registerEvents(new GameListener(this), this);
		pm.registerEvents(new ModerationListener (this), this);
		pm.registerEvents(new SpecialItems (this), this);
		pm.registerEvents(new EconListener (this), this);
		pm.registerEvents(new DonationListener (this), this);
		//pm.registerEvents(new CTag (this), this);
		pm.registerEvents(new TrespassersListener(this), this);
		pm.registerEvents(new TPSListener(this), this);
		pm.registerEvents(new ShopListener(this), this);
		pm.registerEvents(new IronDoor(this), this);
		
		
		// Command Executors
		ride = new Ride();
		tp = new Teleport();
		rc = new RCommands();
		gma = new GamemodeAll();
		say = new Say();
		enc = new Enchant();
		msg = new Message();
		give = new Give();
		mny = new EconomyCommands();
		act = new Auctions();
		arena = new KACommands();
		mc = new ModerationCommands();
		sc = new SquadsCommands();
		dc = new DonationCommands();
		tps = new TeleportSkulls();
		shops = new ShopsCommands();
		client = new Client();
		spawn = new Spawn();
		gc = new GameCommands();
		npcmanager = new NPCManager(this);
		getCommand("ride").setExecutor(ride);
		getCommand("unride").setExecutor(ride);
		getCommand("rideme").setExecutor(ride);
		getCommand("rideother").setExecutor(ride);
		getCommand("tp").setExecutor(tp);
		getCommand("warp").setExecutor(tp);
		getCommand("gm").setExecutor(rc);
		getCommand("gmalls").setExecutor(gma);
		getCommand("gmallc").setExecutor(gma);
		getCommand("gmalla").setExecutor(gma);
		getCommand("server").setExecutor(rc);
		getCommand("ar").setExecutor(rc);
		getCommand("kick").setExecutor(rc);
		getCommand("chat").setExecutor(rc);
		getCommand("fly").setExecutor(rc);
		getCommand("flyoff").setExecutor(rc);
		getCommand("help").setExecutor(rc);
		getCommand("rload").setExecutor(rc);
		getCommand("i").setExecutor(rc);
		getCommand("heal").setExecutor(rc);
		getCommand("check").setExecutor(rc);
		getCommand("setspawn").setExecutor(rc);
		getCommand("time").setExecutor(rc);
		getCommand("say").setExecutor(say);
		getCommand("god").setExecutor(rc);
		getCommand("rules").setExecutor(rc);
		getCommand("admin").setExecutor(rc);
		getCommand("pl").setExecutor(rc);
		getCommand("playerlist").setExecutor(rc);
		getCommand("who").setExecutor(rc);
		getCommand("mob").setExecutor(rc);
		getCommand("boss").setExecutor(rc);
		getCommand("systime").setExecutor(rc);
		getCommand("w").setExecutor(rc);
		getCommand("weather").setExecutor(rc);
		getCommand("arr").setExecutor(rc);
		getCommand("advertisement43155726").setExecutor(rc);
		getCommand("bugmsg12314321431").setExecutor(rc);
		getCommand("vote").setExecutor(rc);
		getCommand("arenatest").setExecutor(rc);
		getCommand("pitch").setExecutor(rc);
		getCommand("cooldown").setExecutor(rc);
		getCommand("inv").setExecutor(rc);
		getCommand("npc").setExecutor(rc);
		getCommand("ignore").setExecutor(rc);
		getCommand("meta").setExecutor(rc);
		getCommand("cc").setExecutor(rc);
		getCommand("e").setExecutor(enc);
		getCommand("m").setExecutor(msg);
		getCommand("msg").setExecutor(msg);
		getCommand("message").setExecutor(msg);
		getCommand("tell").setExecutor(msg);
		getCommand("whisper").setExecutor(msg);
		getCommand("r").setExecutor(msg);
		getCommand("a").setExecutor(msg);
		getCommand("g").setExecutor(give);
		getCommand("give").setExecutor(give);
		getCommand("gh").setExecutor(give);
		getCommand("kit").setExecutor(give);
		getCommand("money").setExecutor(mny);
		getCommand("econ").setExecutor(mny);
		getCommand("economy").setExecutor(mny);
		getCommand("claimvote").setExecutor(mny);
		getCommand("votes").setExecutor(mny);
		getCommand("auction").setExecutor(act);
		getCommand("bid").setExecutor(act);
		getCommand("mute").setExecutor(mc);
		getCommand("unmute").setExecutor(mc);
		getCommand("ban").setExecutor(mc);
		getCommand("unban").setExecutor(mc);
		getCommand("s").setExecutor(sc);
		getCommand("sq").setExecutor(sc);
		getCommand("squad").setExecutor(sc);
		getCommand("squads").setExecutor(sc);
		getCommand("f").setExecutor(sc);
		getCommand("faction").setExecutor(sc);
		getCommand("factions").setExecutor(sc);
		getCommand("donations").setExecutor(dc);
		getCommand("donation").setExecutor(dc);
		getCommand("d").setExecutor(dc);
		getCommand("morph").setExecutor(dc);
		getCommand("unmorph").setExecutor(dc);
		getCommand("invtp").setExecutor(tps);
		getCommand("shops").setExecutor(shops);
		getCommand("c").setExecutor(client);
		getCommand("client").setExecutor(client);
		getCommand("spawn").setExecutor(spawn);
		getCommand("ef").setExecutor(rc);
		getCommand("effects").setExecutor(rc);
		getCommand("ka").setExecutor(arena);
		getCommand("splegg").setExecutor(gc);
		
		Spawn.spawnEntity(InfoCentre.ALL);
		
		try {
			uc = new UnknownCommand();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		if(getServer().getIp().equalsIgnoreCase("119.252.190.191")) {
			Squads.startSaveThread();
			utilServer.startCheckThread();
//		}
		
		utilServer.startCooldownThread();
		utilServer.startMessageThread();
		utilServer.addStaff();
		HPlayer.load();
	}
}
