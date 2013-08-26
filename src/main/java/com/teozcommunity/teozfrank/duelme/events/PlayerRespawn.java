package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 10/08/13
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class PlayerRespawn implements Listener {

    private DuelMe plugin;

    public PlayerRespawn(DuelMe plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        if(plugin.duelingPlayers.contains(p.getPlayer())){
           e.setRespawnLocation(plugin.locations.lobbySpawnLocation());
           plugin.duelingPlayers.remove(p.getPlayer());
           plugin.util.restoreInventory(p.getPlayer());
           plugin.util.restoreExpLevel(p.getPlayer());//restore their exp level
        }

    }


}
