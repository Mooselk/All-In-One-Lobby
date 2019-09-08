package me.kate.lobby.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kate.lobby.Main;
//import me.kate.lobby.data.files.SelectorFile;
//import me.kate.lobby.data.files.interfaces.ISelectorSettings;
import me.kate.lobby.npcs.api.NPC;
import net.md_5.bungee.api.ChatColor;

public class ReloadConfigCommand implements CommandExecutor {

	// private ISelectorSettings sf = new SelectorFile();
	// private NPCLib npclib;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("reloadselector")) {
			if (p.isOp()) {
				
				this.createFactions(p);
				this.createSkyblock(p);
//				sf.reload();
//				if (!sf.reloadFailed()) {
//					p.sendMessage(ChatColor.RED + "Reloaded selector! ");
//				} else {
//					p.sendMessage(ChatColor.RED + "Reload failed, Check console for errors! ");
//				}
			}
		}
		return true;
	}
	
	public void createFactions(Player p) {
		NPC npc = Main.getInstance().getNPCLib().createNPC(Arrays.asList(ChatColor.WHITE + "Hi there (Factions)", ChatColor.YELLOW + "Click on me!"));
		npc.setLocation(new Location(Bukkit.getWorld("world"), -13.5 + 1, 61, 0.5 + 1));
        Main.NPCS.put(npc.getId(), "Factions");
        Bukkit.getLogger().info("IDS: " +  Main.IDS);
        npc.create();
        npc.show(p);
        //showToAllPlayers(npc);
	}
	
	public void createSkyblock(Player p) {
		NPC npc = Main.getInstance().getNPCLib().createNPC(Arrays.asList(ChatColor.WHITE + "Hi there (SkyBlock)", ChatColor.YELLOW + "Click on me!"));
		npc.setLocation(new Location(Bukkit.getWorld("world"), -13.5, 61, 0.5));
        Main.NPCS.put(npc.getId(), "SkyBlock");
        Bukkit.getLogger().info("IDS: " +  Main.NPCS);
        npc.create();
        npc.show(p);
        //showToAllPlayers(npc);
	}
	
	public void showToAllPlayers(NPC npc) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (!npc.isShown(online)) {
				npc.show(online);
			}
		}
	}
}
