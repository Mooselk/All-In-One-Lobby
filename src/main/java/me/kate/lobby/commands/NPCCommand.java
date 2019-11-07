package me.kate.lobby.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
import me.kate.lobby.Messages;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.utils.Logger;

public class NPCCommand implements CommandExecutor {

	private final NPCBuilder builder = new NPCBuilder();
	private final Messages msgs = new Messages();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		final Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("npc")) {
			if (args.length == 0) {
				msgs.npcHelp(player);
				return true;
			} else {
				if (args[0].equalsIgnoreCase("help")) {
					if (player.hasPermission("lobby.npc.help")) {
						msgs.npcHelp(player);
					} else {
						msgs.noPermission(player);
					}
				}
				if (args[0].equalsIgnoreCase("print")) {
					Logger.info("NPCS_OBJECT: " + Main.getRegistry().getNPCObjects());
					Logger.info("NPCSINFOT: " + Main.getRegistry().getNPCInfo());
				}
				if (args[0].equalsIgnoreCase("create")) {
					if (player.hasPermission("lobby.npc.create")) {
						if (args.length < 3) {
							msgs.send("&6Usage: &f/npc create <npc_name> <skin_id>", player);
						} else {
							this.npcToConfig(args[1], args[2], player.getLocation(), player);
							msgs.send("&f[&6NPC&f] Created NPC '" + args[1] + "' with skinId '" + args[2] + "' at"
									+ " X: "+ (int) player.getLocation().getX()
									+ ", Y: " + (int) player.getLocation().getY() 
									+ ", Z: " + (int) player.getLocation().getZ(), player);
							builder.reloadNPCs(player, false);
						}
					} else {
						msgs.noPermission(player);
					}
				}

				if (args[0].equalsIgnoreCase("move")) {
					if (player.hasPermission("lobby.npc.move")) {
						if (args.length < 2) {
							msgs.send("&6Usage: &f/npc move <npc_name>", player);
						} else {
							builder.move(player.getLocation(), args[1], player);
						}
						
					} else {
						msgs.noPermission(player);
					}
				}

				if (args[0].equalsIgnoreCase("delete")) {
					if (player.hasPermission("lobby.npc.delete")) {
						if (args.length < 2) {
							msgs.send("&6Usage: &f/npc delete <npc_name>", player);
						}
						if (args.length == 2) {
							if (NPCConfig.getNPCConfig().getString("npcs." + args[1]) != null) {
								msgs.send("&f[&6NPC&f] Deleted NPC '" + args[1] + "'.", player);
								NPCConfig.getNPCConfig().set("npcs." + args[1], null);
								NPCConfig.save();
								NPCConfig.reload();
								builder.destroy(args[1]);
								builder.reloadNPCs(player, false);
							} else {
								msgs.send("&f[&6NPC&f] Error deleting NPC '" + args[1] + "'. NPC does not exist!", player);
							}
						}
					} else {
						msgs.noPermission(player);
					}
				}
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (player.hasPermission("lobby.npc.reload")) {
					msgs.send("&f[&6NPC&f] Reloading NPCs...", player);
					builder.reloadNPCs(player, true);
				} else {
					msgs.noPermission(player);
				}
			}
		}
		return true;

	}

	private void npcToConfig(String id, String skin, Location loc, Player p) {
		builder.create(Integer.valueOf(skin), id, loc, p);
	}
}
