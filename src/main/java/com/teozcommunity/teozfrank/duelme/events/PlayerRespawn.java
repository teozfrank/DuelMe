package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.FileManager;
import com.teozcommunity.teozfrank.duelme.util.PlayerData;
import com.teozcommunity.teozfrank.duelme.util.SendConsoleMessage;
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
        Player player = e.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if(dm.isDeadPlayer(playerUUID)){
            PlayerData playerData = dm.getPlayerDataByUUID(playerUUID);
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Player respawn location for " + playerName + ": " + playerData.getLocaton());
            }
            e.setRespawnLocation(playerData.getLocaton());
            dm.restorePlayerData(player);
            dm.removeDeadPlayer(playerUUID);
        }
    }

}
