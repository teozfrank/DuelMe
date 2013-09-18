package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 08/08/13
 * Time: 16:33
 * Project: DuelMe
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class PlayerDeath implements Listener {
    private DuelMe plugin;

    public PlayerDeath(DuelMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (plugin.duelingPlayers.size() == 2) {
            if (p.getKiller() instanceof Player) {
                Player killer = p.getKiller();
                e.getDrops().clear();//drop nothing on death

                if (plugin.getConfig().getBoolean("duelme.announce.deaths")) {
                    e.setDeathMessage(plugin.pluginPrefix + ChatColor.YELLOW + p.getName() + ChatColor.AQUA + " Was Killed in a Duel by " +
                            ChatColor.YELLOW + killer.getName());
                }
                plugin.util.endDuel();//end the duel
            } else {
                e.getDrops().clear();//drop nothing on death
                if (plugin.getConfig().getBoolean("duelme.announce.deaths")) {
                    e.setDeathMessage(plugin.pluginPrefix + ChatColor.YELLOW + p.getName() + ChatColor.AQUA + " Was Killed in a Duel!");
                }
                e.setKeepLevel(true);
                plugin.util.endDuel();//end the duel
            }
        } else {
            plugin.util.endDuel();
        }
    }
}
