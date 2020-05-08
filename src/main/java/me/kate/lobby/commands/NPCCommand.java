package me.kate.lobby.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.kate.lobby.Messages;
import me.kate.lobby.data.Config;
import me.kate.lobby.data.files.NPCConfig;
import me.kate.lobby.npcs.NPCBuilder;
import me.kate.lobby.objects.LobbyNPC;

public class NPCCommand extends NPCBuilder implements CommandExecutor {

	private Config npcConfig = new NPCConfig();
	
	public NPCCommand(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		final Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("npc")) {
			if (args.length == 0) {
				Messages.npcHelp(player);
			} else {
				if (args[0].equalsIgnoreCase("help")) {
					if (player.hasPermission("lobby.npc.help")) {
						Messages.npcHelp(player);
					} else {
						Messages.noPermission(player);
					}
				}
				if (args[0].equalsIgnoreCase("create")) {
					if (player.hasPermission("lobby.npc.create")) {
						if (args.length < 3) {
							Messages.send("&6Usage: &f/npc create <npc_name> <skin_id>", player);
						} else {
							this.npcToConfig(args[1], args[2], player.getLocation(), player);
							Messages.send("&f[&6NPC&f] Created NPC '" + args[1] + "' with skinId '" + args[2] + "' at"
									+ " X: "+ (int) player.getLocation().getX()
									+ ", Y: " + (int) player.getLocation().getY() 
									+ ", Z: " + (int) player.getLocation().getZ(), player);
							reloadNPCs(player, false);
						}
					} else {
						Messages.noPermission(player);
					}
				}

				if (args[0].equalsIgnoreCase("move")) {
					if (player.hasPermission("lobby.npc.move")) {
						if (args.length < 2) {
							Messages.send("&6Usage: &f/npc move <npc_name>", player);
						} else {
							move(player.getLocation(), args[1], player);
						}
					} else {
						Messages.noPermission(player);
					}
				}
				
				if (args[0].equalsIgnoreCase("setskin")) {
					if (player.hasPermission("lobby.npc.move")) {
						if (args.length < 3) {
							Messages.send("&6Usage: &f/npc setskin <npc_name> <skinId>", player);
						} else {
							setSkin(LobbyNPC.getByName(args[1]), Integer.valueOf(args[2]), player);
						}
					} else {
						Messages.noPermission(player);
					}
				}

				if (args[0].equalsIgnoreCase("delete")) {
					if (player.hasPermission("lobby.npc.delete")) {
						if (args.length < 2) {
							Messages.send("&6Usage: &f/npc delete <npc_name>", player);
						}
						if (args.length == 2) {
							if (npcConfig.getConfig().getString("npcs." + args[1]) != null) {
								Messages.send("&f[&6NPC&f] Deleted NPC '" + args[1] + "'.", player);
								npcConfig.getConfig().set("npcs." + args[1], null);
								npcConfig.save();
								npcConfig.reload();
								destroy(args[1]);
								reloadNPCs(player, false);
							} else {
								Messages.send("&f[&6NPC&f] Error deleting NPC '" + args[1] + "'. NPC does not exist!", player);
							}
						}
					} else {
						Messages.noPermission(player);
					}
				}
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (player.hasPermission("lobby.npc.reload")) {
					Messages.send("&f[&6NPC&f] Reloading NPCs...", player);
					reloadNPCs(player, true);
				} else {
					Messages.noPermission(player);
				}
			}
			if (args[0].equalsIgnoreCase("list")) {
				if (player.hasPermission("lobby.npc.list")) {
					Messages.send("&f[&6NPC&f] Loaded NPCs:", player);
					for (String name : listNPCs()) {
						Messages.send(" - &6" + name, player);
					}
				} else {
					Messages.noPermission(player);
				}
			}
		}
		return true;

	}

	private void npcToConfig(String id, String skin, Location loc, Player player) {
		create(Integer.valueOf(skin), id, loc.add(0.5, 0, 0.5), player);
	}
}
