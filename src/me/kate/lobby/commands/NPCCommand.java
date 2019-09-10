package me.kate.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.npcs.NPCBuilder;

public class NPCCommand implements CommandExecutor {

	private NPCBuilder npcb = new NPCBuilder();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("npc")) {
			if (args[0].equalsIgnoreCase("test")) {
				p.sendMessage("Building npcs...");
				p.sendMessage("Sending packets...");
				npcb.build(p);
			}

			if (args[0].equalsIgnoreCase("reload")) {
				p.sendMessage("Reloading NPCs...");
				npcb.reloadNPCs(p, npcb);
			}

			if (args[0].equalsIgnoreCase("create")) {
				if (args.length < 3) {
					p.sendMessage("Usage: /npc create <npc_name> <skin_id>");
				}
			}

			if (args[0].equalsIgnoreCase("move")) {
				if (args.length < 2) {
					p.sendMessage("Usage: /npc move <npc_name>");
				}
			}

			if (args[0].equalsIgnoreCase("delete")) {
				if (args.length < 2) {
					p.sendMessage("Usage: /npc delete <npc_name>");
				}
			}
		}
		return true;
	}
}
