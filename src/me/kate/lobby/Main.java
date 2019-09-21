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
import me.kate.lobby.commands.SpawnCommand;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.HidePlayersFile;
import me.kate.lobby.data.files.NPCFile;
import me.kate.lobby.data.files.PlayerSettingsFile;
import me.kate.lobby.data.files.PortalsFile;
import me.kate.lobby.data.files.SelectorFile;
import me.kate.lobby.data.files.interfaces.IHidePlayerSettings;
import me.kate.lobby.data.files.interfaces.IPlayerSettings;
import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.events.InteractNPCEvent;
import me.kate.lobby.events.PlayerJoinEvents;
import me.kate.lobby.events.world.BlockRelatedEvent;
import me.kate.lobby.events.world.MobSpawnEvent;
import me.kate.lobby.events.world.TouchVoidEvent;
import me.kate.lobby.items.hideplayers.events.HidePlayersInteractEvent;
import me.kate.lobby.items.portals.Cuboid;
import me.kate.lobby.items.portals.Portal;
import me.kate.lobby.items.portals.events.PlayerPortalEvent;
import me.kate.lobby.items.portals.events.WandInteractEvent;
import me.kate.lobby.items.selector.events.SelectorClickEvent;
import me.kate.lobby.items.selector.events.SelectorGuiEvents;
import me.kate.lobby.npcs.NPCLib;
import me.kate.lobby.npcs.api.NPC;

public class Main extends JavaPlugin {
	
	/* * * * * TO-DO * * * * * 
	 * 
	 * Finish /npc create
	 * Jump pads
	 * Join / Leave messages
	 * Remove debug messages
	 * Portal commands
	 * 
	 * * * * * * * * * * * * */

	private static Main instance;
	private NPCLib npclib;
	
	private IPlayerSettings playerSettings = new PlayerSettingsFile();
	private ISelectorSettings selectorSettings = new SelectorFile();
	private IHidePlayerSettings hideSettings = new HidePlayersFile();
	private Portal portals = new Portal();
	
	public static final Map<String, Map<String, Object>> SERVER_PLACEHOLDERS = new HashMap<>();
	
	public static final Map<String, String> NPCINFO = new HashMap<>();
	public static final ArrayList<NPC> NPCS = new ArrayList<>();
	
	public static final Map<UUID, Integer> COOLDOWNS = new HashMap<>();
	public static final Map<UUID, BukkitTask> TASKS = new HashMap<>();
	
	public static final Map<String, Cuboid> PORTALS = new HashMap<>();
	public static final Map<String, Location> SELECTIONS = new HashMap<>();

	public static Main getInstance() {
		return instance;
	}
	
	public NPCLib getNPCLib() {
		return npclib;	
	}

	@Override
	public void onEnable() {
		instance = this;
		this.npclib = new NPCLib(this);
		this.playerSettings.create();
		this.selectorSettings.create();
		this.hideSettings.create();
		this.portals.load();
		//new PingServersBackground().start();
		PortalsFile.create();
		NPCFile.create();
		Config.createConfig();
		this.registerEvents();
		this.registerChannel();
		this.registerCommands();
	}

	@Override
	public void onDisable() {

	}

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new PlayerJoinEvents(), this);
		this.getServer().getPluginManager().registerEvents(new TouchVoidEvent(), this);
		this.getServer().getPluginManager().registerEvents(new BlockRelatedEvent(), this);
		this.getServer().getPluginManager().registerEvents(new MobSpawnEvent(), this);
		this.getServer().getPluginManager().registerEvents(new SelectorGuiEvents(), this);
		this.getServer().getPluginManager().registerEvents(new SelectorClickEvent(), this);
		this.getServer().getPluginManager().registerEvents(new HidePlayersInteractEvent(), this);
		this.getServer().getPluginManager().registerEvents(new InteractNPCEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerPortalEvent(), this);
		this.getServer().getPluginManager().registerEvents(new WandInteractEvent(), this);
	}

	private void registerCommands() {
		this.getCommand("setspawn").setExecutor(new SpawnCommand());
		this.getCommand("spawn").setExecutor(new SpawnCommand());
		this.getCommand("lobby").setExecutor(new LobbyCommand());
		this.getCommand("npc").setExecutor(new NPCCommand());
		this.getCommand("portal").setExecutor(new PortalCommand());
	}
	
	private void registerChannel() {
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}
}