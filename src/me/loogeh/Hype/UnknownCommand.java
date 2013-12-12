package me.loogeh.Hype;

import java.lang.reflect.Field;

import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

public class UnknownCommand {
	public static Hype plugin = Hype.plugin;
	
	private static SimpleCommandMap commands = null;
	
	public UnknownCommand() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		UnknownCommand.commands = getCommandMap(plugin.getServer());
	}
	
	private SimpleCommandMap getCommandMap(Server server) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if(server.getPluginManager() instanceof SimplePluginManager) {
			Field f = SimplePluginManager.class.getDeclaredField("commandMap");
			f.setAccessible(true);
			
			return (SimpleCommandMap) f.get(server.getPluginManager());
		}
		return null;
	}
	
	public static boolean commandExists(String name) {
		return commands.getCommand(name) != null;
	}
	
	
}
