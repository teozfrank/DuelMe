package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 08/08/13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class PlayerQuit implements Listener {

    private DuelMe plugin;

    public PlayerQuit(DuelMe plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(plugin.duelingPlayers.contains(p.getName())){
            plugin.duelingPlayers.remove(p.getName());
            plugin.util.restoreInventory(p.getPlayer());
            if(plugin.duelingPlayers.size()<=1){
                //TODO end duel if one player leaves
            }
        }

    }

}
