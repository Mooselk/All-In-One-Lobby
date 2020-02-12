package me.kate.lobby.tasks;

import java.util.UUID;

public abstract class Task {

	public abstract void start();
	
	public abstract void stop();
	
	public abstract boolean isRunning();
	
	public abstract UUID getTaskID();
	
}
