package me.kate.lobby.data;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.kate.lobby.Messages;

public abstract class Config {

	public abstract void create();
	
	public abstract void save();
	
	public abstract boolean reload();
	
	public abstract FileConfiguration getConfig();
	
	public abstract Set<String> get(String key);
	
	public abstract ConfigurationSection getSection(String section);
	
	public abstract String getName();
	
	public void reloadConfig(Config config, CommandSender sender) {
		if (config.reload()) {
			Messages.get().send("&f[&6Lobby&f] Successfully reloaded &6" + config.getName() + "&f!", sender);
		} else {
			Messages.get().send("&f[&6Lobby&f] Failed to reload &6" + config.getName() + "&f!", sender);
		}
	}
}
