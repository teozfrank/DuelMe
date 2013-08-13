package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        if(plugin.duelingPlayers.contains(p.getPlayer())){
            plugin.duelingPlayers.remove(p.getPlayer());
            p.teleport(plugin.locations.lobbySpawnLocation());// teleport to lobby spawn
            plugin.util.restoreInventory(p.getPlayer());
            plugin.util.broadcastMessage(ChatColor.RED+ p.getName()+" has ended a duel by quitting!");
            if(plugin.duelingPlayers.size()<=1){
                plugin.util.endDuel();
            }
        }
        if(plugin.spectatingPlayers.contains(p.getPlayer())){
            plugin.spectatingPlayers.remove(p.getPlayer());
            plugin.util.restoreInventory(p.getPlayer());
            for(Player pl: Bukkit.getOnlinePlayers()){
              pl.showPlayer(p.getPlayer());
            }
            p.teleport(plugin.locations.lobbySpawnLocation());
            p.setAllowFlight(false);
        }

    }

}
