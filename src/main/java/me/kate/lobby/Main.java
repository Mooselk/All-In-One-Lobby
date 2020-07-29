package me.kate.lobby;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.cache.CacheStorage;
import me.kate.lobby.commands.DebugCommand;
import me.kate.lobby.commands.LobbyCommand;
import me.kate.lobby.commands.NPCCommand;
import me.kate.lobby.commands.PortalCommand;
import me.kate.lobby.data.files.JumpPadConfig;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.ToggleConfig;
import me.kate.lobby.listeners.InteractNPCListener;
import me.kate.lobby.listeners.InventoryListener;
import me.kate.lobby.listeners.PlayerJoinListener;
import me.kate.lobby.listeners.PlayerQuitListener;
import me.kate.lobby.listeners.world.BlockRelatedListener;
import me.kate.lobby.listeners.world.MobSpawnListener;
import me.kate.lobby.listeners.world.PlantGrowthListener;
import me.kate.lobby.listeners.world.TouchVoidListener;
import me.kate.lobby.listeners.world.WeatherBlockListener;
import me.kate.lobby.modules.jumppads.JumpPadInteractEvent;
import me.kate.lobby.modules.portals.Portal;
import me.kate.lobby.modules.portals.listeners.PlayerPortalListener;
import me.kate.lobby.modules.portals.listeners.WandInteractListener;
import me.kate.lobby.modules.selector.Selector;
import me.kate.lobby.modules.selector.gui.listeners.SelectorGUIListener;
import me.kate.lobby.modules.selector.listeners.SelectorInteractListener;
import me.kate.lobby.modules.tablist.TabList;
import me.kate.lobby.modules.toggleplayers.events.TogglePlayersEvent;
import me.kate.lobby.nms.v1_10_R1.TabList_v1_10_R1;
import me.kate.lobby.nms.v1_11_R1.TabList_v1_11_R1;
import me.kate.lobby.nms.v1_12_R1.TabList_v1_12_R1;
import me.kate.lobby.nms.v1_13_R1.TabList_v1_13_R1;
import me.kate.lobby.nms.v1_13_R2.TabList_v1_13_R2;
import me.kate.lobby.nms.v1_14_R1.TabList_v1_14_R1;
import me.kate.lobby.nms.v1_8_R2.TabList_v1_8_R2;
import me.kate.lobby.nms.v1_8_R3.TabList_v1_8_R3;
import me.kate.lobby.nms.v1_9_R1.TabList_v1_9_R1;
import me.kate.lobby.nms.v1_9_R2.TabList_v1_9_R2;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.npcs.NPCLib;
import me.kate.lobby.npcs.internal.MinecraftVersion;
import me.kate.lobby.servers.ServerManager;
import me.kate.lobby.tasks.NPCTask;
import me.kate.lobby.tasks.SelectorUpdateTask;
import me.kate.lobby.tasks.Task;
import me.kate.lobby.tasks.bungee.BungeeMessenger;
import me.kate.lobby.tasks.bungee.BungeePingTask;
import me.kate.lobby.utils.Logger;

public class Main extends JavaPlugin {

	public static final boolean DEBUG = false;

	private static Main instance;
	private static NPCLib npclib;
	private TabList tablist;
	private Portal portals;
	private Selector selector;
	private SelectorUpdateTask task;
	
	public static final ExecutorService threadPool = Executors.newSingleThreadExecutor();
	
	public static Main getInstance() {
		return instance;
	}
	
	public static NPCLib getNPCLib() {
		return npclib;
	}
	
	public TabList getTabList() {
		return tablist;
	}
	
	public Selector getSelector() {
		return selector;
	}
	
	public MinecraftVersion getMCVersion() {
		return MinecraftVersion.valueOf(getVersion().toUpperCase());
	}
	
	@Override
	public void onEnable() {
		
		instance = this;
		npclib = new NPCLib(this);
		
		this.loadConfigs();
		this.registerEvents();
		this.registerCommands();
		this.loadNPCs();
		this.registerChannel();
		this.setupServers();
		this.startTasks();
		
		portals = new Portal(this);
		portals.load(false);
		
		selector = new Selector();
		
		selector.addContents();
		selector.setupContents();
		selector.update();
		
		task = new SelectorUpdateTask(this);
		task.start();
		
		if (getConfig().getBoolean("tablist.enabled")) {
			if (setupTablist()) {
				Logger.info("[Lobby] Loaded TabList for version " + getVersion());
			} else {
				Logger.severe("[Lobby] Failed to load tablist for " + getVersion() + " unsupported version.");
			}
		}
	}

	@Override
	public void onDisable() {
		instance = null;
		task.stop();
	}

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		this.getServer().getPluginManager().registerEvents(new TouchVoidListener(this), this);
		this.getServer().getPluginManager().registerEvents(new BlockRelatedListener(this), this);
		this.getServer().getPluginManager().registerEvents(new MobSpawnListener(this), this);
		this.getServer().getPluginManager().registerEvents(new SelectorInteractListener(this), this);
		this.getServer().getPluginManager().registerEvents(new TogglePlayersEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new InteractNPCListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerPortalListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
		this.getServer().getPluginManager().registerEvents(new WandInteractListener(this), this);
		this.getServer().getPluginManager().registerEvents(new WeatherBlockListener(this), this);
		this.getServer().getPluginManager().registerEvents(new JumpPadInteractEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new PlantGrowthListener(this), this);
		this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
		this.getServer().getPluginManager().registerEvents(new SelectorGUIListener(), this);
	}
	
	private void registerCommands() {
		this.getCommand("lobby").setExecutor(new LobbyCommand(this));
		this.getCommand("npc").setExecutor(new NPCCommand(this));
		this.getCommand("portal").setExecutor(new PortalCommand(this));
		this.getCommand("debug").setExecutor(new DebugCommand(this));
	}
	
	private void loadConfigs() {
		new SelectorConfig(this).create();
		new PortalsConfig(this).create();
		new CacheStorage(this).create();
		new JumpPadConfig(this).create();
		new NPCConfig(this).create();
		new PluginConfig(this).create();
		new ToggleConfig(this).create();
	}
	
	private void loadNPCs() {
		NPCBuilder builder = new NPCBuilder(this);
		builder.buildNPC();
	}

	private void setupServers() {
		ServerManager servers = new ServerManager(this);
		servers.loadServers();
	}
	
	private void startTasks() {
		Task npctask = new NPCTask(this);
		Task bungee = new BungeePingTask(this);
		bungee.start();
		npctask.start();
	}
	
	private void registerChannel() {
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeMessenger());
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}
	
	public String getVersion() {
		return getServer()
				.getClass()
				.getPackage()
				.getName()
				.split("\\.")[3];
	}
	
	private boolean setupTablist() {
		if (getVersion().equals("v1_8_R2")) { tablist = new TabList_v1_8_R2(this); }
		if (getVersion().equals("v1_8_R3")) { tablist = new TabList_v1_8_R3(this); }
		if (getVersion().equals("v1_9_R1")) { tablist = new TabList_v1_9_R1(this); }
		if (getVersion().equals("v1_9_R2")) { tablist = new TabList_v1_9_R2(this); }
		if (getVersion().equals("v1_10_R1")) { tablist = new TabList_v1_10_R1(this); }
		if (getVersion().equals("v1_11_R1")) { tablist = new TabList_v1_11_R1(this); }
		if (getVersion().equals("v1_12_R1")) { tablist = new TabList_v1_12_R1(this); }
		if (getVersion().equals("v1_13_R1")) { tablist = new TabList_v1_13_R1(this); }
		if (getVersion().equals("v1_13_R2")) { tablist = new TabList_v1_13_R2(this); }
		if (getVersion().equals("v1_14_R1")) { tablist = new TabList_v1_14_R1(this); }
		return tablist != null;
	}
}