package me.kate.lobby.data.files.interfaces;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public interface IHidePlayerSettings {

	void create();

	void load();
	
	void save();

	boolean reload();

	String getHideMessage();

	String getUnhideMessage();

	String getHideDisplayName();

	String getUnhideDisplayName();

	List<String> getHideLore();

	List<String> getUnhideLore();

	String getHideMaterial();

	String getUnHideMaterial();
	
	String getCooldownMessage(int timeleft);
	
	int getCooldownLength();

	FileConfiguration getHideSettings();

}
