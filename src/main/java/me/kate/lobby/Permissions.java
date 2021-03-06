package me.kate.lobby;

import org.bukkit.command.CommandSender;

public enum Permissions {

	LOBBY_RELOAD("lobby.reload"),
	LOBBY_HELP("lobby.help"),
	LOBBY_SETSPAWN("lobby.setspawn"),
	LOBBY_SPAWN("lobby.spawn"),
	
	NPC_HELP("lobby.npc.help"),
	NPC_CREATE("lobby.npc.create"),
	NPC_DELETE("lobby.npc.delete"),
	NPC_MOVE("lobby.npc.move"),
	NPC_SETSKIN("lobby.npc.setskin"),
	NPC_LIST("lobby.npc.reload"),
	NPC_RELOAD("lobby.npc.reload"),
	
	PORTAL_HELP("lobby.portal.help"),
	PORTAL_WAND("lobby.portal.wand"),
	PORTAL_SELECT("lobby.portal.select"),
	PORTAL_CREATE("lobby.portal.create"),
	PORTAL_DELETE("lobby.portal.delete"),
	PORTAL_CLEAR("lobby.portal.clear"),
	PORTAL_SHOW("lobby.portal.show"),
	PORTAL_SET("lobby.portal.set"),
	PORTAL_RELOAD("lobby.portal.reload"),
	
	LOBBY_BUILD_BREAK("lobby.build.break"),
	LOBBY_BUILD_PLACE("lobby.build.place"),
	
	TEST_PERMISSION("lobby.test");
	
	private String permission;
	
	Permissions(final String permission) {
		this.permission = permission;
	}
	
	public String get() {
		return permission;
	}
	
	public boolean has(CommandSender sender) {
		return sender.hasPermission(this.get());
	}
	
	public boolean hasPermission(CommandSender sender) {
		
		if (sender.hasPermission(this.get())) {
			return true;
		}
		
		Messages.get().noPermission(sender);
		return false;
	}

}
