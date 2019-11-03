package me.kate.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

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
import me.kate.lobby.npcs.NPCLib;
import me.kate.lobby.npcs.api.NPC;
import me.kate.lobby.threads.PingNPCBackground;
import me.kate.lobby.threads.PingSelectorBackground;
import me.kate.lobby.utils.BungeeChannelApi;

public class Main extends JavaPlugin {
	
	/* * * * * TO-DO * * * * * 
	 * 
	 * Fix portal selections (Make them per player)
	 * Jump pads (WIP)
	 * NPC move
	 * Edit NPCs with commands
	 * 
	 * * * * * * * * * * * * */

	public static final boolean DEBUG = true;
	
	private static Main instance;
	private NPCLib npclib;
	
	private IPlayerSettings playerSettings = new PlayerSettingsConfig();
	private ISelectorSettings selectorSettings = new SelectorConfig();
	private IHidePlayerSettings hideSettings = new HidePlayersConfig();
	private Portal portals = new Portal();
	
	public static final Map<String, Map<String, Object>> SELECTOR_PLACEHOLDERS = new HashMap<>();
	public static final Map<String, Map<String, Object>> NPC_PLACEHOLDERS = new HashMap<>();
	
	public static final Map<String, String> NPCINFO = new HashMap<>();
	public static final ArrayList<NPC> NPCS = new ArrayList<>();
	public static final Map<String, NPC> NPCS_OBJECT = new HashMap<>();
	public static final Map<NPC, ArrayList<String>> HOLOTEXT = new HashMap<>();
	
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
	
	public BungeeChannelApi api;
	
	@Override
	public void onEnable() {
		instance = this;
		PortalsConfig.create();
		JumpPadConfig.create();
		NPCConfig.create();
		Config.createConfig();
		this.playerSettings.create();
		this.selectorSettings.create();
		this.hideSettings.create();
		this.registerEvents();
		this.registerChannel();
		this.registerCommands();
		this.api = BungeeChannelApi.of(this);
		this.startThreads();
		this.npclib = new NPCLib(this);
		this.portals.load();
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
	
	private void registerChannel() {
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}
	
	
	private void startThreads() {
		new PingSelectorBackground().start();
		new PingNPCBackground().start();
	}
}