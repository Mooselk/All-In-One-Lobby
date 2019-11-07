package me.kate.lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.cache.CacheStorage;
import me.kate.lobby.commands.LobbyCommand;
import me.kate.lobby.commands.NPCCommand;
import me.kate.lobby.commands.PortalCommand;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.HidePlayersConfig;
import me.kate.lobby.data.files.JumpPadConfig;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.data.files.PlayerSettingsConfig;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;
import me.kate.lobby.data.files.interfaces.IPlayerSettings;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.events.InteractNPCEvent;
import me.kate.lobby.events.PlayerJoinEvents;
import me.kate.lobby.events.PlayerLeaveEvents;
import me.kate.lobby.events.world.BlockRelatedEvent;
import me.kate.lobby.events.world.MobSpawnEvent;
import me.kate.lobby.events.world.PlantGrowthEvent;
import me.kate.lobby.events.world.TouchVoidEvent;
import me.kate.lobby.events.world.WeatherBlockEvent;
import me.kate.lobby.items.pads.JumpPadInteractEvent;
import me.kate.lobby.items.portals.Portal;
import me.kate.lobby.items.portals.Position;
import me.kate.lobby.items.portals.events.PlayerPortalEvent;
import me.kate.lobby.items.portals.events.WandInteractEvent;
import me.kate.lobby.items.portals.utils.Cuboid;
import me.kate.lobby.items.selector.events.SelectorClickEvent;
import me.kate.lobby.items.selector.events.SelectorGuiEvents;
import me.kate.lobby.items.toggleplayers.events.TogglePlayersEvent;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.npcs.NPCLib;
import me.kate.lobby.npcs.NPCRegistry;
import me.kate.lobby.ping.Bungee;
import me.kate.lobby.threads.PingNPCBackground;
import me.kate.lobby.threads.PingSelectorBackground;

public class Main extends JavaPlugin {

	/*
	 * * * * * TO-DO * * * * *
	 * 
	 * Reload configs
	 * Fix portal selections (Make them per player) 
	 * NPC move 
	 * NPC states in config 
	 * Edit NPCs with commands
	 * 
	 * * * * * * * * * * * *
	 */

	public static final boolean DEBUG = true;

	private static Main instance;
	private NPCLib npclib;
	private static NPCRegistry registry;

	private IPlayerSettings playerSettings = new PlayerSettingsConfig();
	private ISelectorSettings selectorSettings = new SelectorConfig();
	private IHidePlayerSettings hideSettings = new HidePlayersConfig();
	private Portal portals = new Portal();

	public static final Map<String, Map<String, Object>> SELECTOR_PLACEHOLDERS = new HashMap<>();
	public static final Map<String, Map<String, Object>> NPC_PLACEHOLDERS = new HashMap<>();

	public static final Map<UUID, Integer> COOLDOWNS = new HashMap<>();
	public static final Map<UUID, BukkitTask> TASKS = new HashMap<>();
	public static final Map<String, BukkitTask> ALTTASKS = new HashMap<>();

	public static final Map<String, Cuboid> PORTALS = new HashMap<>();
	public static final Map<UUID, Map<Position, Location>> PLAYER_SELECTIONS = new HashMap<>();
	public static final Map<Position, Location> SELECTIONS = new HashMap<>();

	public static Main getInstance() {
		return instance;
	}

	public NPCLib getNPCLib() {
		return npclib;
	}
	
	public static NPCRegistry getRegistry() {
		return registry;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		registry = new NPCRegistry();
		this.loadConfigs();
		this.registerEvents();
		this.registerChannel();
		this.registerCommands();
		this.startThreads();
		this.npclib = new NPCLib(this);
		this.portals.load();
		this.loadNPCs();
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new PlayerJoinEvents(), this);
		this.getServer().getPluginManager().registerEvents(new TouchVoidEvent(), this);
		this.getServer().getPluginManager().registerEvents(new BlockRelatedEvent(), this);
		this.getServer().getPluginManager().registerEvents(new MobSpawnEvent(), this);
		this.getServer().getPluginManager().registerEvents(new SelectorGuiEvents(), this);
		this.getServer().getPluginManager().registerEvents(new SelectorClickEvent(), this);
		this.getServer().getPluginManager().registerEvents(new TogglePlayersEvent(), this);
		this.getServer().getPluginManager().registerEvents(new InteractNPCEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerPortalEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLeaveEvents(), this);
		this.getServer().getPluginManager().registerEvents(new WandInteractEvent(), this);
		this.getServer().getPluginManager().registerEvents(new WeatherBlockEvent(), this);
		this.getServer().getPluginManager().registerEvents(new JumpPadInteractEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PlantGrowthEvent(), this);
	}

	private void registerCommands() {
		this.getCommand("lobby").setExecutor(new LobbyCommand());
		this.getCommand("npc").setExecutor(new NPCCommand());
		this.getCommand("portal").setExecutor(new PortalCommand());
	}
	
	private void loadConfigs() {
		PortalsConfig.create();
		CacheStorage.create();
		JumpPadConfig.create();
		NPCConfig.create();
		Config.createConfig();
		this.playerSettings.create();
		this.selectorSettings.create();
		this.hideSettings.create();
	}
	
	private void loadNPCs() {
		final NPCBuilder builder = new NPCBuilder();
		builder.build();
	}

	private void registerChannel() {
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Bungee());
	}

	private void startThreads() {
		new PingSelectorBackground().start();
		new PingNPCBackground().start();
	}
}