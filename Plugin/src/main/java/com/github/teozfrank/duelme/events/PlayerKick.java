package com.github.teozfrank.duelme.events;

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.DuelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.UUID;

/**
 * Created by Frank on 18/01/2015.
 */
public class PlayerKick implements Listener {

    private DuelMe plugin;

    public PlayerKick(DuelMe plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();

        DuelManager dm = plugin.getDuelManager();

        if(dm.isInDuel(playerUUID)){
            dm.endDuel(player);
        }
    }
}
