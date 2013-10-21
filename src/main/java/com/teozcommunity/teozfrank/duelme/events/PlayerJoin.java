package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 18/08/13
 * Time: 00:11
 * Project: DuelMe
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */

public class PlayerJoin implements Listener {
    private DuelMe plugin;

    public PlayerJoin(DuelMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.isOp() || p.hasPermission("duelme.update.notify")) {
            if (plugin.updateChecker.updateAvailable) {
               p.sendMessage(ChatColor.GREEN+"There is an update available for "+ChatColor.GOLD+"DuelMe "+ChatColor.GREEN+"please download it here: "+
               ChatColor.GOLD+"http://dev.bukkit.org/bukkit-plugins/duelme/");
            }
        }
    }
}