package me.kate.lobby.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.utils.Messages;

public class NPCCommand implements CommandExecutor {

	private final NPCBuilder npcb = new NPCBuilder();

	private final Messages msgs = new Messages();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("npc")) {
			if (args.length == 0) {
				msgs.npcHelp(p);
				return true;
			} else {
				if (args[0].equalsIgnoreCase("help")) {
					if (p.hasPermission("lobby.npc.help")) {
						msgs.npcHelp(p);
					} else {
						msgs.noPermission(p);
					}
				}
				if (args[0].equalsIgnoreCase("create")) {
					if (p.hasPermission("lobby.npc.create")) {
						if (args.length < 3) {
							msgs.send("&6Usage: &f/npc create <npc_name> <skin_id>", p);
						} else {
							this.npcToConfig(args[1], args[2], p.getLocation(), p);
							msgs.send("&f[&6NPC&f] Created NPC '" + args[1] + "' with skinId '" + args[2] + "' at"
									+ " X: "+ (int) p.getLocation().getX()
									+ ", Y: " + (int) p.getLocation().getY() 
									+ ", Z: " + (int) p.getLocation().getZ(), p);
							npcb.reloadNPCs(p, npcb, false);
						}
					} else {
						msgs.noPermission(p);
					}
				}

				if (args[0].equalsIgnoreCase("move")) {
					if (p.hasPermission("lobby.npc.move")) {
						if (args.length < 2) {
							msgs.send("&6Usage: &f/npc move <npc_name>", p);
						}
					} else {
						msgs.noPermission(p);
					}
				}

				if (args[0].equalsIgnoreCase("delete")) {
					if (p.hasPermission("lobby.npc.delete")) {
						if (args.length < 2) {
							msgs.send("&6Usage: &f/npc delete <npc_name>", p);
						}
						if (args.length == 2) {
							if (NPCConfig.getNPCConfig().getString("npcs." + args[1]) != null) {
								msgs.send("&f[&6NPC&f] Deleted NPC '" + args[1] + "'.", p);
								NPCConfig.getNPCConfig().set("npcs." + args[1], null);
								NPCConfig.save();
								NPCConfig.reload();
								npcb.destroy(args[1]);
								npcb.reloadNPCs(p, npcb, false);
							} else {
								msgs.send("&f[&6NPC&f] Error deleting NPC '" + args[1] + "'. NPC does not exist!", p);
							}
						}
					} else {
						msgs.noPermission(p);
					}
				}
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (p.hasPermission("lobby.npc.reload")) {
					msgs.send("&f[&6NPC&f] Reloading NPCs...", p);
					npcb.reloadNPCs(p, npcb, true);
				} else {
					msgs.noPermission(p);
				}
			}
		}
		return true;

	}

	private void npcToConfig(String id, String skin, Location loc, Player p) {
		npcb.create(Integer.valueOf(skin), id, loc, p);
	}
}
