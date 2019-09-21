package me.kate.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.data.files.NPCFile;
import me.kate.lobby.npcs.NPCBuilder;

public class NPCCommand implements CommandExecutor {

	private NPCBuilder npcb = new NPCBuilder();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("npc")) {
			if (args.length == 0) {
				this.helpMessage(p);
				return true;
			} else {
				if (args[0].equalsIgnoreCase("help")) {
					if (p.hasPermission("lobby.npc.help")) {
						this.helpMessage(p);
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"You do not have permission to use this command."));
					}
				}
				if (args[0].equalsIgnoreCase("create")) {
					if (p.hasPermission("lobby.npc.delete")) {
						if (args.length < 3) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									"&6Usage: &f/npc create <npc_name> <skin_id>"));
						} else {
							this.npcToConfig(args[1], args[2], p.getLocation(), p);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									"&f[&6NPC&f] Created NPC '" + args[1] + "' with skinId '" + args[2] + "' at X: "
											+ (int) p.getLocation().getX() + ", Y: " + (int) p.getLocation().getY()
											+ ", Z: " + (int) p.getLocation().getZ()));
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"You do not have permission to use this command."));
					}
				}

				if (args[0].equalsIgnoreCase("move")) {
					if (p.hasPermission("lobby.npc.move")) {
						if (args.length < 2) {
							p.sendMessage(
									ChatColor.translateAlternateColorCodes('&', "&6Usage: &f/npc move <npc_name>"));
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"You do not have permission to use this command."));
					}
				}

				if (args[0].equalsIgnoreCase("delete")) {
					if (p.hasPermission("lobby.npc.delete")) {
						if (args.length < 2) {
							p.sendMessage(
									ChatColor.translateAlternateColorCodes('&', "&6Usage: &f/npc delete <npc_name>"));
						}
						if (args.length == 2) {
							if (NPCFile.getNPCConfig().getString("npcs." + args[1]) != null) {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&f[&6NPC&f] Deleted NPC '" + args[1] + "'."));
								NPCFile.getNPCConfig().set("npcs." + args[1], null);
								NPCFile.save();
								NPCFile.reload();
								npcb.destroy(args[1]);
								npcb.reloadNPCs(p, npcb, false);
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&f[&6NPC&f] Error deleting NPC '" + args[1] + "'. NPC does not exist!"));
							}
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"You do not have permission to use this command."));
					}
				}
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (p.hasPermission("lobby.npc.reload")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6NPC&f] Reloading NPCs..."));
					npcb.reloadNPCs(p, npcb, true);
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"You do not have permission to use this command."));
				}
			}
		}
		return true;

	}

	private void helpMessage(Player p) {
		p.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&m-------------&r[ &l&6NPC Help&r ]&m-------------"));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6/npc create <&fnpc_name&6> <&fskin_id&6> &f- Creates NPC."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6/npc move <&fnpc_name&6> &f- Move NPC to your location."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6/npc delete <&fnpc_name&6> &f- Lists all portal commands."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6/npc setskin <&fskin_id&6> &f- Change a specfic NPCs skin."));
		p.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&6/npc reload &f- Reloads NPC config and respawns NPCs."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6Note: &f&oSkin ids can be otained from https://mineskin.org/"));
	}

	private void npcToConfig(String id, String skin, Location loc, Player p) {
		npcb.create(Integer.valueOf(skin), id, loc, p);
	}
}
