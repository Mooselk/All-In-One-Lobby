package me.kate.lobby.npcs;

import me.kate.lobby.npcs.nms.*;
import me.kate.lobby.npcs.packetlistener.PacketListener;
import me.kate.lobby.npcs.packetlistener.ProtocolLibListener;
import me.kate.lobby.npcs.packetlistener.TinyProtocolListener;
import me.kate.lobby.npcs.protocollib.NPC_ProtocolLib;
import me.kate.lobby.npcs.skin.NPCSkin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.logging.Level;

public class NPCApi {
	
    private static String version;
    private static Plugin plugin;
    private static ArrayList<NPC> npcs = new ArrayList<NPC>();
    private static Boolean cache = true;

    private static PacketListener packetListener = null;

    public static Plugin getPlugin(){
        return plugin;
    }

    public static Boolean getCache(){
        return cache;
    }

    public static void useCache(boolean bol){
        cache = bol;
    }

    private static void setupVersion(){
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public static void removeCacheFile(){
        if (version.equals("v1_8_R3")) {
            NPC_v1_8_r3.removeCacheFile();
        }else if (version.equals("v1_9_R1")) {
            NPC_v1_9_r1.removeCacheFile();
        }else if (version.equals("v1_9_R2")) {
            NPC_v1_9_r2.removeCacheFile();
        }else if (version.equals("v1_10_R1")) {
            NPC_v1_10_r1.removeCacheFile();
        }else if (version.equals("v1_11_R1")) {
            NPC_v1_11_r1.removeCacheFile();
        }else if (version.equals("v1_12_R1")) {
            NPC_v1_12_r1.removeCacheFile();
        }
    }

    public static ArrayList<NPC> getNPCs(){
        ArrayList<NPC> list = new ArrayList<NPC>();
        for(NPC npc : npcs){
            if(!npc.isDeleted()){
                list.add(npc);
            }
        }
        return list;
    }

    /**
     * Create a NPC
     * @param plugin
     * @param location NPC Location
     * @param skin NPC skin ussing a playername
     */

    public static NPC createNPC(Plugin plugin, Location location, NPCSkin skin){
        NPCApi.plugin = plugin;
        if(version==null){
            setupVersion();
        }
        if(packetListener==null){
            if(Bukkit.getPluginManager().getPlugin("ProtocolLib")!=null){
                packetListener = new ProtocolLibListener();
                Bukkit.getLogger().log(Level.INFO, ChatColor.YELLOW+"ProtocolLib founded. NPCs listening and ussing it :)");
            }else{
                packetListener = new TinyProtocolListener();
            }
            packetListener.startListening(plugin);
        }
        if(packetListener instanceof ProtocolLibListener){
            NPC_ProtocolLib.startTask(plugin);
            NPC npc = new NPC_ProtocolLib(location, skin);
            npcs.add(npc);
            return npc;
        }
        if (version.equals("v1_8_R3")) {
            NPC_v1_8_r3.startTask(plugin);
            NPC npc = new NPC_v1_8_r3(location, skin);
            npcs.add(npc);
            return npc;
        }else if (version.equals("v1_9_R1")) {
            NPC_v1_9_r1.startTask(plugin);
            NPC npc = new NPC_v1_9_r1(location, skin);
            npcs.add(npc);
            return npc;
        }else if (version.equals("v1_9_R2")) {
            NPC_v1_9_r2.startTask(plugin);
            NPC npc = new NPC_v1_9_r2(location, skin);
            npcs.add(npc);
            return npc;
        }else if (version.equals("v1_10_R1")) {
            NPC_v1_10_r1.startTask(plugin);
            NPC npc = new NPC_v1_10_r1(location, skin);
            npcs.add(npc);
            return npc;
        }else if (version.equals("v1_11_R1")) {
            NPC_v1_11_r1.startTask(plugin);
            NPC npc = new NPC_v1_11_r1(location, skin);
            npcs.add(npc);
            return npc;
        }else if (version.equals("v1_12_R1")) {
            NPC_v1_12_r1.startTask(plugin);
            NPC npc = new NPC_v1_12_r1(location, skin);
            npcs.add(npc);
            return npc;
        }else{
            Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED+"Unsopported server version.");
            return null;
        }
    }

}