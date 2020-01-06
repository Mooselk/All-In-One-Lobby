package me.kate.lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.kate.lobby.cache.CacheStorage;
import me.kate.lobby.commands.LobbyCommand;
import me.kate.lobby.commands.NPCCommand;
import me.kate.lobby.commands.PortalCommand;
import me.kate.lobby.data.files.JumpPadConfig;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.data.files.PlayerSettingsConfig;
import me.kate.lobby.data.files.PluginConfig;
import me.kate.lobby.data.files.PortalsConfig;
import me.kate.lobby.data.files.SelectorConfig;
import me.kate.lobby.data.files.ToggleConfig;
import me.kate.lobby.listeners.InteractNPCEvent;
import me.kate.lobby.listeners.PlayerJoinEvents;
import me.kate.lobby.listeners.PlayerLeaveEvents;
import me.kate.lobby.listeners.world.BlockRelatedEvent;
import me.kate.lobby.listeners.world.MobSpawnEvent;
import me.kate.lobby.listeners.world.PlantGrowthEvent;
import me.kate.lobby.listeners.world.TouchVoidEvent;
import me.kate.lobby.listeners.world.WeatherBlockEvent;
import me.kate.lobby.modules.jumppads.JumpPadInteractEvent;
import me.kate.lobby.modules.portals.Portal;
import me.kate.lobby.modules.portals.events.PlayerPortalEvent;
import me.kate.lobby.modules.portals.events.WandInteractEvent;
import me.kate.lobby.modules.portals.utils.Cuboid;
import me.kate.lobby.modules.selector.events.SelectorClickEvent;
import me.kate.lobby.modules.selector.events.SelectorGuiEvents;
import me.kate.lobby.modules.tablist.TabList;
import me.kate.lobby.modules.toggleplayers.events.TogglePlayersEvent;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.npcs.NPCLib;
import me.kate.lobby.npcs.NPCRegistry;
import me.kate.lobby.npcs.nms.v1_10_R1.TabList_v1_10_R1;
import me.kate.lobby.npcs.nms.v1_11_R1.TabList_v1_11_R1;
import me.kate.lobby.npcs.nms.v1_12_R1.TabList_v1_12_R1;
import me.kate.lobby.npcs.nms.v1_13_R1.TabList_v1_13_R1;
import me.kate.lobby.npcs.nms.v1_13_R2.TabList_v1_13_R2;
import me.kate.lobby.npcs.nms.v1_14_R1.TabList_v1_14_R1;
import me.kate.lobby.npcs.nms.v1_8_R2.TabList_v1_8_R2;
import me.kate.lobby.npcs.nms.v1_8_R3.TabList_v1_8_R3;
import me.kate.lobby.npcs.nms.v1_9_R1.TabList_v1_9_R1;
import me.kate.lobby.npcs.nms.v1_9_R2.TabList_v1_9_R2;
import me.kate.lobby.servers.Servers;
import me.kate.lobby.utils.Logger;

public class Main extends JavaPlugin {

	/*
	 * * * * * TO-DO * * * * *
	 * 
	 * NPC setskin
	 * Edit NPCs with commands
	 * 
	 * * * * * * * * * * * *
	 */

	public static final boolean DEBUG = true;

	private static Main instance;
	private NPCLib npclib;
	private TabList tablist;
	private NPCRegistry registry;
	
	public final Map<String, Map<String, Object>> placeholders = new HashMap<>();

	private Portal portals = new Portal();
	
	public static final Map<UUID, Integer> COOLDOWNS = new HashMap<>();
	public final Map<UUID, BukkitTask> tasks = new HashMap<>();
	public static final Map<String, BukkitTask> ALTTASKS = new HashMap<>();

	public final Map<String, Cuboid> portal = new HashMap<>();

	public static Main getInstance() {
		return instance;
	}
	
	public TabList getTabList() {
		return tablist;
	}

	public NPCLib getNPCLib() {
		return npclib;
	}
	
	public NPCRegistry getRegistry() {
		return registry;
	}
	
	public Map<UUID, BukkitTask> getTasks() {
		return tasks;
	}
	
	public Map<String, Map<String, Object>> getPlaceholders() {
		return placeholders;
	}
	
	public Map<String, Cuboid> getPortals() {
		return portal;
	}
	
	
	@Override
	public void onEnable() {
		final String version = getServer()
				.getClass()
				.getPackage()
				.getName()
				.split("\\.")[3];
		instance = this;
		registry = new NPCRegistry();
		this.loadConfigs();
		this.registerEvents();
		this.registerCommands();
		this.npclib = new NPCLib(this);
		this.loadNPCs();
		this.registerChannel();
		this.setupServers();
		portals.load();
		if (this.getConfig().getBoolean("tablist.enabled")) {
			if (setupTablist()) {
				Logger.info("[Lobby] Loaded TabList for version " + version);
			} else {
				Logger.severe("[Lobby] Failed to load tablist for " + version + " unsupported version.");
			}
		}
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new PlayerJoinEvents(this), this);
		this.getServer().getPluginManager().registerEvents(new TouchVoidEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new BlockRelatedEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new MobSpawnEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new SelectorGuiEvents(), this);
		this.getServer().getPluginManager().registerEvents(new SelectorClickEvent(), this);
		this.getServer().getPluginManager().registerEvents(new TogglePlayersEvent(), this);
		this.getServer().getPluginManager().registerEvents(new InteractNPCEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerPortalEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLeaveEvents(this), this);
		this.getServer().getPluginManager().registerEvents(new WandInteractEvent(), this);
		this.getServer().getPluginManager().registerEvents(new WeatherBlockEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new JumpPadInteractEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PlantGrowthEvent(this), this);
	}

	private void registerCommands() {
		this.getCommand("lobby").setExecutor(new LobbyCommand(this));
		this.getCommand("npc").setExecutor(new NPCCommand(this));
		this.getCommand("portal").setExecutor(new PortalCommand());
	}
	
	private void loadConfigs() {
		new SelectorConfig().create();
		new PortalsConfig().create();
		new CacheStorage().create();
		new JumpPadConfig().create();
		new NPCConfig().create();
		new PluginConfig().create();
		new PlayerSettingsConfig().create();
		new ToggleConfig().create();
	}
	
	private void loadNPCs() {
		final NPCBuilder builder = new NPCBuilder(this);
		builder.buildNPC();
	}

	private void setupServers() {
		final Servers servers = new Servers(this);
		servers.loadServers();
		servers.getCountAsync();
		servers.startNPCTask();
	}
	
	private void registerChannel() {
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}
	
	private boolean setupTablist() {
		String versionName;
		try {
			versionName = this.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
		if (versionName.equals("v1_8_R2")) { tablist = new TabList_v1_8_R2(); }
		if (versionName.equals("v1_8_R3")) { tablist = new TabList_v1_8_R3(); }
		if (versionName.equals("v1_9_R1")) { tablist = new TabList_v1_9_R1(); }
		if (versionName.equals("v1_9_R2")) { tablist = new TabList_v1_9_R2(); }
		if (versionName.equals("v1_10_R1")) { tablist = new TabList_v1_10_R1(); }
		if (versionName.equals("v1_11_R1")) { tablist = new TabList_v1_11_R1(); }
		if (versionName.equals("v1_12_R1")) { tablist = new TabList_v1_12_R1(); }
		if (versionName.equals("v1_13_R1")) { tablist = new TabList_v1_13_R1(); }
		if (versionName.equals("v1_13_R2")) { tablist = new TabList_v1_13_R2(); }
		if (versionName.equals("v1_14_R1")) { tablist = new TabList_v1_14_R1(); }
		return tablist !=null;
	}
}