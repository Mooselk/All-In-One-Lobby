package me.kate.lobby.tasks;

import java.io.IOException;
import java.net.Socket;

import me.kate.lobby.Main;

public class HeartBeat {

	public static void fetchServerStatusFromAsync(String address, int port, Callback callback) {
    	Main.threadPool.execute(() -> {
            try {
            	callback.call(checkServerStatus(address, port));
            } catch (Exception exception) {
                exception.printStackTrace();
                callback.failed();
            }
        });
    }

    public interface Callback {

        void call(ServerStatus status);

        default void failed() {
        	
        }
    }
	
	private static ServerStatus checkServerStatus(String SERVER_ADDRESS, int TCP_SERVER_PORT) {
	    try (Socket s = new Socket(SERVER_ADDRESS, TCP_SERVER_PORT)) {
	    	s.close();
	        return ServerStatus.ONLINE;
	    } catch (IOException ex) { /* ignore */ }
	    return ServerStatus.OFFLINE;
	}
}
