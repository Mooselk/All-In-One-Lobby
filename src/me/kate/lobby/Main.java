package me.kate.lobby;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.commands.ReloadConfigCommand;
import me.kate.lobby.commands.SetSpawnCommand;
import me.kate.lobby.config.Config;
import me.kate.lobby.events.BlockRelatedEvent;
import me.kate.lobby.events.JoinEvent;
import me.kate.lobby.events.MobSpawnEvent;
import me.kate.lobby.events.TouchVoidEvent;
import me.kate.lobby.items.compass.events.CompassClickEvent;
import me.kate.lobby.items.compass.events.GuiClickEvent;
import me.kate.lobby.items.compass.ping.PingServersBackground;

public class Main extends JavaPlugin {
	
	private static Main plugin;
	
	public static Map<String, Map<String, Object>> SERVER_PLACEHOLDERS = new HashMap<>();
	
	public static Main getInstance() {
		return plugin;
	}
	
	@Override
	public void onEnable() {
		plugin = this;
		new PingServersBackground().start();
		Config.createConfig();
		this.registerEvents();
		this.registerCommands();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new JoinEvent(), this);
		getServer().getPluginManager().registerEvents(new TouchVoidEvent(), this);
		getServer().getPluginManager().registerEvents(new BlockRelatedEvent(), this);
		getServer().getPluginManager().registerEvents(new MobSpawnEvent(), this);
		getServer().getPluginManager().registerEvents(new GuiClickEvent(), this);
		getServer().getPluginManager().registerEvents(new CompassClickEvent(), this);
	}
	
	private void registerCommands() {
		this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
		this.getCommand("reloadconfig").setExecutor(new ReloadConfigCommand());
	}
}