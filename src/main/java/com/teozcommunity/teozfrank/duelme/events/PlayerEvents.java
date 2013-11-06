package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created with IntelliJ IDEA.
 * User: teoz
 * Date: 06/11/13
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class PlayerEvents implements Listener {

    private DuelMe plugin;

    public PlayerEvents(DuelMe plugin){
       this.plugin = plugin;
       Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerBreakBlock(BlockBreakEvent e){
       //TODO implement this method according to the event
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent e){
       //TODO implement this method according to the event
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent e){
       //TODO implement this method according to the event
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e){
       //TODO implement this method according to the event
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerUseCommand(PlayerCommandPreprocessEvent e){
        //TODO implement this method according to the event
    }
}
