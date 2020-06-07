package me.kate.lobby.objects;

import java.util.Map;

import com.google.common.collect.Maps;

import me.kate.lobby.ping.PingResponse;
import me.kate.lobby.ping.ServerPinger;

public class PlaceHolder {
	
	/**
	 * TO-DO
	 * 
	 * Instead of storing a map inside a static map in main
	 * Create a placeholder object for each server
	 * To get playercount, status, etc
	 * 
	 */
	
	private String server;
	private boolean isOnline;
	private int playerCount;
	private int maxPlayers;
	private String motd;
	private Type type;
	
	private final int timeout = 5;
	
	private static final Map<String, PlaceHolder> PLACEHOLDERS = Maps.newHashMap();
	
	public PlaceHolder(Server server, Type type) {
		this.server = server.getServerName();
		try {
			final PingResponse response = ServerPinger.fetchData(server.getAddress(), server.getPort(), timeout);
			this.isOnline = response.isOnline();
			this.motd = response.getMotd();
			this.playerCount = response.getOnlinePlayers();
			this.maxPlayers = response.getMaxPlayers();
		} catch (final Exception e) {
			this.isOnline = false;
		}
		PLACEHOLDERS.put(this.server, this);
	}
	
	public static Map<String, PlaceHolder> getPlaceHolders() {
		return PLACEHOLDERS;
	}
	
	public String getServerName() {
		return this.server;
	}
	
	public boolean isOnline() {
		return this.isOnline;
	}
	
	public int getPlayerCount() {
		return this.playerCount;
	}
	
	public int getMaxPlayers() {
		return this.maxPlayers;
	}
	
	public String getMotd() {
		return this.motd;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public enum Type {
		
		EXTERNAL,
		BUNGEE;
		
	}
	
}
