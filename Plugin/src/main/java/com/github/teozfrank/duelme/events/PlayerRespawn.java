package com.github.teozfrank.duelme.events;

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.DuelManager;
import com.github.teozfrank.duelme.util.FileManager;
import com.github.teozfrank.duelme.util.PlayerData;
import com.github.teozfrank.duelme.util.SendConsoleMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

/**
 * Created by Frank on 04/05/2015.
 */
public class PlayerRespawn implements Listener {

    private DuelMe plugin;

    public PlayerRespawn(DuelMe plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if(plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("Respawn Event Fired");
        }
        final Player player = e.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        final DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if(dm.isDeadPlayer(playerUUID)){
            PlayerData playerData = dm.getPlayerDataByUUID(playerUUID);
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Player respawn location for " + playerName + ": " + playerData.getLocaton());
            }
            e.setRespawnLocation(playerData.getLocaton());
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.info("restoring playerdata for player " + playerName);
            }

            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {//give the player a second to respawn, then restore the players data.
                @Override
                public void run() {
                    dm.restorePlayerData(player);
                }
            }, 20L);

            dm.removeDeadPlayer(playerUUID);// remove the player as a "dead" player
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug(dm.getPlayerData().toString());
            }
        }
    }

}
