package me.loogeh.Hype.entity;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.v1_6_R3.CraftServer;

import net.minecraft.server.v1_6_R3.DedicatedPlayerList;
import net.minecraft.server.v1_6_R3.DedicatedServer;
import net.minecraft.server.v1_6_R3.MinecraftServer;
import net.minecraft.server.v1_6_R3.PropertyManager;
import net.minecraft.server.v1_6_R3.WorldServer;

public class BServer {
	private static BServer ins;
	private MinecraftServer mcServer;
	private CraftServer cServer;
	private Server server;
	private HashMap<String, BWorld> worlds = new HashMap<String, BWorld>();
	
	private BServer() {
		server = Bukkit.getServer();
		
		try {
			cServer = (CraftServer) server;
			mcServer = cServer.getServer();
		} catch (Exception e) {
			Logger.getLogger("Minecraft").log(Level.SEVERE, null, e);
		}
	}
	
	public void disablePlugins() {
		cServer.disablePlugins();
	}
	
	public void dispatchCommand(CommandSender sender, String message) {
		cServer.dispatchCommand(sender, message);
	}
	
	public DedicatedPlayerList getHandle() {
		return cServer.getHandle();
	}
	
	public ConsoleReader getReader() {
		return cServer.getReader();
	}
	
	public void loadPlugins() {
		cServer.loadPlugins();
	}
	
	public void stop() {
		mcServer.safeShutdown();
	}
	
	public void sendConsoleCommand(String command) {
		if(mcServer.isRunning()) {
			((DedicatedServer) mcServer).issueCommand(command, mcServer);
		}
	}
	
	public Logger getLogger() {
		return cServer.getLogger();
	}
	
	public List<WorldServer> getWorldServers() {
		return mcServer.worlds;
	}
	
	public int getSpawnProtectionRadius() {
		return mcServer.server.getSpawnRadius();
	}
	
	public PropertyManager getPropertyManager() {
		return mcServer.getPropertyManager();
	}
	
	public Server getServer() {
		return server;
	}
	
	public BWorld getWorld(String world) {
		if(worlds.containsKey(world)) return worlds.get(world);
		BWorld bworld = new BWorld(this, world);
		worlds.put(world, bworld);
		return bworld;
	}
	
	public static BServer getInstance() {
		if(ins == null) ins = new BServer();
		return ins;
	}
	
	public MinecraftServer getMCServer() {
		return mcServer;
	}
}
