package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 10/08/13
 * Time: 22:10
 * To change this template use File | Settings | File Templates.
 */
public class PlayerBreakBlock implements Listener {
    private DuelMe plugin;

    public PlayerBreakBlock(DuelMe plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerBreakBlock(BlockBreakEvent e){
        Player p = e.getPlayer();
        if(plugin.duelingPlayers.contains(p.getPlayer())){
            e.setCancelled(true);
            p.sendMessage(plugin.pluginPrefix+ ChatColor.RED+" Not allowed during a Duel!");
        }
    }




}
