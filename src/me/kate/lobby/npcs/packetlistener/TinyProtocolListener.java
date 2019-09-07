package me.kate.lobby.npcs.packetlistener;

import me.kate.lobby.npcs.NPC;
import me.kate.lobby.npcs.NPCApi;
import me.kate.lobby.npcs.event.NPCInteractEvent;
import me.kate.lobby.npcs.tinyprotocol.Reflection;
import me.kate.lobby.npcs.tinyprotocol.TinyProtocol;
import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class TinyProtocolListener implements PacketListener{

    private static TinyProtocol protocol = null;

    private static Class<?> EntityInteractClass = Reflection.getClass("{nms}.PacketPlayInUseEntity");
    private static Reflection.FieldAccessor<Integer> EntityID = Reflection.getField(EntityInteractClass, int.class, 0);
    private static ArrayList<Player> playerswhointeract = new ArrayList<Player>();


    @Override
    public void startListening(Plugin plugin){
        if(protocol==null) {
            protocol = new TinyProtocol(plugin) {
                @Override
                public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                    if(EntityInteractClass.isInstance(packet)){
                        if(!playerswhointeract.contains(sender)){
                            for(NPC npc : NPCApi.getNPCs()){
                                if(npc.getEntityID(sender)==EntityID.get(packet)){
                                    NPCInteractEvent event = new NPCInteractEvent(sender, npc);
                                    Bukkit.getPluginManager().callEvent(event);
                                    break;
                                }
                            }
                            playerswhointeract.add(sender);
                            Bukkit.getScheduler().runTaskLaterAsynchronously(NPCApi.getPlugin(), new Runnable(){
                                @Override
                                public void run() {
                                    playerswhointeract.remove(sender);
                                }
                            }, 2);
                        }
                    }
                    return super.onPacketInAsync(sender, channel, packet);
                }
            };
        }
    }

}