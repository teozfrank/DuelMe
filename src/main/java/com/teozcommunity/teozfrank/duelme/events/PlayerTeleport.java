package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 10/08/13
 * Time: 22:02
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class PlayerTeleport implements Listener {

    private DuelMe plugin;

    public PlayerTeleport(DuelMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (!plugin.duelingPlayers.isEmpty()) {
            for (Player pl : plugin.duelingPlayers) {
                if (e.getTo().equals(pl.getLocation())) {
                    p.sendMessage(plugin.pluginPrefix + ChatColor.RED + "You cannot teleport to dueling players!");
                    e.setCancelled(true);
                    return;
                }
            }
        }

        if (plugin.duelingPlayers.contains(p.getPlayer())) {// if the player is dueling
            e.setCancelled(true);//cancel the teleport event
            p.sendMessage(plugin.pluginPrefix + ChatColor.RED + " Teleportation is disabled during a duel!");
        }
        if (plugin.spectatingPlayers.contains(p.getPlayer())) {
            e.setCancelled(true);
            p.sendMessage(plugin.pluginPrefix + ChatColor.RED + " Teleportation is disabled while spectating a duel, " +
                    "use /duel leave to leave the spectating area.");
        }

    }

}
