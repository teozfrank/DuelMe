package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 10/08/13
 * Time: 22:10
 * Project: DuelMe
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class PlayerBreakBlock implements Listener {
    private DuelMe plugin;

    public PlayerBreakBlock(DuelMe plugin) {
        this.plugin = plugin;
    }

    /**
     * method to check block breaking events, cancels them if needed
     * @param e block break event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (plugin.duelingPlayers.contains(p)||plugin.frozenPlayers.contains(p)) {
            e.setCancelled(true);
            p.sendMessage(plugin.pluginPrefix + ChatColor.RED + " Not allowed during a Duel!");
        }
        if (plugin.spectatingPlayers.contains(p)) {
            e.setCancelled(true);
            p.sendMessage(plugin.pluginPrefix + ChatColor.RED + " Not allowed while spectating a duel!");
        }
    }
}