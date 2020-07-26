package me.kate.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.Permissions;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.objects.LobbyNPC;
import me.kate.lobby.utils.Logger;

public class NPCCommand implements CommandExecutor {

	private NPCConfig npcConfig;
	private Messages messages;
	private NPCBuilder builder;
	
	public NPCCommand(Main plugin) {
		this.builder = new NPCBuilder(plugin);
		this.npcConfig = new NPCConfig(plugin);
		this.messages = new Messages();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = null;
		
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		
		if (!command.getName().equalsIgnoreCase("npc")) {
			return true;
		}
		
		switch (args[0]) {
		
		case "help" : {
			
			if (!Permissions.NPC_HELP.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			messages.npcHelp(sender);
			return true;
		}
		
		case "create" : {
			
			if (!Permissions.NPC_CREATE.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			if (!(sender instanceof Player)) {
				Logger.info("This command can only be used by a player");
				break;
			}
			
			if (args.length > 3 || args.length < 3) {
				messages.send("&6Usage: &f/npc create <npc_name> <skin_id>", sender);
				break;
			}
			
			builder.create(args[2], args[1], player.getLocation());
			
			messages.send("&f[&6NPC&f] Created NPC '" + args[1] 
					+ "' with skinId '" + args[2] + "' at"
					+ " X: "+ (int) player.getLocation().getX()
					+ ", Y: " + (int) player.getLocation().getY() 
					+ ", Z: " + (int) player.getLocation().getZ(), player);
			builder.reloadNPC(sender, false);
			return true;
		}
		
		case "delete" : {
			
			if (!Permissions.NPC_DELETE.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			if (args.length < 1 || args.length > 1) {
				messages.send("&6Usage: &f/npc delete <npc_name>", sender);
				break;
			}
			
			if (npcConfig.delete(args[1])) {
				builder.destroy(args[1]);
				messages.send("&f[&6NPC&f] Deleted NPC '" + args[1] + "'.", sender);
				break;
			}
			
			messages.send("&f[&6NPC&f] Error deleting NPC '" + args[1] + "'. NPC does not exist!", sender);
			return true;
		}
		
		case "move" : {
			
			if (!Permissions.NPC_MOVE.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			if (!(sender instanceof Player)) {
				Logger.info("This command can only be used by a player");
				break;
			}
			
			if (args.length < 2 || args.length > 2) {
				messages.send("&6Usage: &f/npc move <npc_name>", sender);
				break;
			}
			
			builder.move(player.getLocation(), args[1], player);
			return true;
		}
		
		case "setskin" : {
			
			if (!Permissions.NPC_SETSKIN.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			if (args.length < 3 || args.length > 3) {
				messages.send("&6Usage: &f/npc setskin <npc_name> <skinId>", sender);
				break;
			}
			
			builder.setSkin(LobbyNPC.getLobbyNPC(args[1], LobbyNPC.Get.NAME), args[2], player);
			return true;
		}
		
		case "list" : {
			
			if (!Permissions.NPC_LIST.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			messages.send("&f[&6NPC&f] Active NPCs:", sender);
			for (String name : builder.listNPCs()) {
				messages.send(" - &6" + name, sender);
			}
			
			return true;
		}
		
		case "reload" : {
			
			if (!Permissions.NPC_RELOAD.has(sender)) {
				messages.noPermission(sender);
				break;
			}
			
			messages.send("&f[&6NPC&f] Reloading NPCs...", sender);
			builder.reloadNPC(sender, true);
		}
		
		
		}
		return true;
	}
}
