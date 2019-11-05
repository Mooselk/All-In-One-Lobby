package me.kate.lobby.cache;

import org.bukkit.configuration.file.FileConfiguration;

import me.kate.lobby.Main;
import me.kate.lobby.npcs.api.skin.Skin;
import me.kate.lobby.utils.Logger;

public class SkinCache {

	private final FileConfiguration config = Main.getInstance().getConfig();

	public void toConfig(Skin skin, int skinId) {
		if (config.getConfigurationSection(String.valueOf(skinId)) == null) {
			config.set(String.valueOf(skinId), skin.serialize());
			Main.getInstance().saveConfig();
			Main.getInstance().reloadConfig();
		}
	}
	
	public boolean isCached(int skinId) {
		if (config.contains(String.valueOf(skinId))) {
			return true;
		}
		return false;		
	}

	public Skin getSkin(int skinId) {
		String value;
		String signature;
		if (config.getConfigurationSection(String.valueOf(skinId)) != null) {
			value = config.getString(skinId + ".value");
			signature = config.getString(skinId + ".signature");
			return new Skin(value, signature);
		} else {
			Logger.severe("Couldn't load skin '" + skinId + "'");
			Logger.severe("	- Skin was not found or failed to cache");
		}
		return null;
	}
}
