package me.kate.lobby.objects;

import java.util.Map;

import com.google.common.collect.Maps;

public class Server {

	private static final Map<String, Server> SERVERS = Maps.newHashMap();
	
	private String serverName;
	private String address;
	private int port;
	
	public Server(String serverName, String address, int port) {
		
		this.serverName = serverName;
		this.address = address;
		this.port = port;
		
		SERVERS.put(serverName, this);
	}
	
	public static Server getServer(String name) {
		return SERVERS.get(name);
	}
	
	public Map<String, Server> getServers() {
		return SERVERS;
	}
	
	public String getServerName() {
		return this.serverName;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public int getPort() {
		return this.port;
	}
}
