package me.kate.lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.commands.NPCCommand;
import me.kate.lobby.commands.ReloadConfigCommand;
import me.kate.lobby.commands.SetSpawnCommand;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.HidePlayersFile;
import me.kate.lobby.data.files.NPCFile;
import me.kate.lobby.data.files.PlayerSettingsFile;
import me.kate.lobby.data.files.SelectorFile;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;
import me.kate.lobby.data.files.interfaces.IPlayerSettings;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.events.BlockRelatedEvent;
import me.kate.lobby.events.InteractNPCEvent;
import me.kate.lobby.events.JoinEvent;
import me.kate.lobby.events.MobSpawnEvent;
import me.kate.lobby.events.TouchVoidEvent;
import me.kate.lobby.items.hideplayers.events.HidePlayersInteractEvent;
import me.kate.lobby.items.selector.events.SelectorClickEvent;
import me.kate.lobby.items.selector.events.SelectorGuiEvents;
// import me.kate.lobby.items.selector.ping.PingServersBackground;
import me.kate.lobby.npcs.NPCLib;

public class Main extends JavaPlugin {

	private static Main instance;
	private NPCLib npclib;
	
	private IPlayerSettings playerSettings = new PlayerSettingsFile();
	private ISelectorSettings selectorSettings = new SelectorFile();
	private IHidePlayerSettings hideSettings = new HidePlayersFile();
	
	public static final Map<String, Map<String, Object>> SERVER_PLACEHOLDERS = new HashMap<>();
	public static final SortedSet<String> IDS = new TreeSet<>();
	public static final Map<String, String> NPCS = new HashMap<>();
	public static final Map<UUID, Integer> COOLDOWNS = new HashMap<>();
	public static final Map<UUID, BukkitTask> TASKS = new HashMap<>();

	public static Main getInstance() {
		return instance;
	}
	
	public NPCLib getNPCLib() {
		return npclib;	
	}

	@Override
	public void onEnable() {
		npclib = new NPCLib(this);
		instance = this;
		playerSettings.create();
		selectorSettings.create();
		hideSettings.create();
		// new PingServersBackground().start();
		NPCFile.create();
		Config.createConfig();
		this.registerEvents();
		this.registerCommands();
	}

	@Override
	public void onDisable() {

	}

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
		this.getServer().getPluginManager().registerEvents(new TouchVoidEvent(), this);
		this.getServer().getPluginManager().registerEvents(new BlockRelatedEvent(), this);
		this.getServer().getPluginManager().registerEvents(new MobSpawnEvent(), this);
		this.getServer().getPluginManager().registerEvents(new SelectorGuiEvents(), this);
		this.getServer().getPluginManager().registerEvents(new SelectorClickEvent(), this);
		this.getServer().getPluginManager().registerEvents(new HidePlayersInteractEvent(), this);
		this.getServer().getPluginManager().registerEvents(new InteractNPCEvent(), this);
		
	}

	private void registerCommands() {
		this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
		this.getCommand("reloadselector").setExecutor(new ReloadConfigCommand());
		this.getCommand("npc").setExecutor(new NPCCommand());
	}
}