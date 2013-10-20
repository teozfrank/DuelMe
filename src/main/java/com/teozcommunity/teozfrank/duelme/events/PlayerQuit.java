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
 * Original Author: teozfrank
 * Date: 08/08/13
 * Time: 16:18
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class PlayerQuit implements Listener {

    private DuelMe plugin;

    public PlayerQuit(DuelMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (plugin.duelingPlayers.contains(p.getPlayer())) {
            plugin.duelingPlayers.remove(p.getPlayer());
            p.teleport(plugin.locations.lobbySpawnLocation());// teleport to lobby spawn
            if(plugin.seperateInventories){
                plugin.util.restoreInventory(p.getPlayer());
            }
            plugin.util.broadcastMessage(ChatColor.RED + p.getName() + " has ended a duel by quitting!");
            if (plugin.duelingPlayers.size() <= 1) {
                plugin.util.endDuel();
            }
        }
        if (plugin.spectatingPlayers.contains(p.getPlayer())) {
            if(plugin.seperateInventories){
                plugin.util.restoreInventory(p.getPlayer());
            }
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.showPlayer(p.getPlayer());
            }
            p.teleport(plugin.locations.lobbySpawnLocation());
            p.setAllowFlight(false);
        }
        if (plugin.duelRequests.containsKey(p.getPlayer())) {// if target leaves
            plugin.duelRequests.remove(p.getPlayer());// duel request is cancelled
        }
    }
}
