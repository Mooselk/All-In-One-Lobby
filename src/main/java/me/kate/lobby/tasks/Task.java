package me.kate.lobby.tasks;

import java.util.UUID;

public interface Task {

	public void start();
	
	public void stop();
	
	public boolean isRunning();
	
	public UUID getTaskID();
	
}
