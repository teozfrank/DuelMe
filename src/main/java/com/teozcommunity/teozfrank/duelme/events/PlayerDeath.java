package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.mysql.FieldName;
import com.teozcommunity.teozfrank.duelme.mysql.MySql;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.FileManager;
import com.teozcommunity.teozfrank.duelme.util.SendConsoleMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

/**
 * Created by Frank on 04/05/2015.
 */
public class PlayerDeath implements Listener {

    private DuelMe plugin;

    public PlayerDeath(DuelMe plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player loser = e.getEntity();
        String playerName = loser.getName();
        UUID playerUUID = loser.getUniqueId();

        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();
        MySql mySql = plugin.getMySql();
        if(dm.isInDuel(playerUUID)){
            dm.addDeadPlayer(playerUUID);

            if(fm.isMySqlEnabled()) {
                mySql.addPlayerKillDeath(playerUUID, playerName, FieldName.DEATH);
            }

            if(e.getEntity().getKiller() instanceof Player){
                Player killer = e.getEntity().getKiller();
                String killerName = killer.getName();
                if(fm.isMySqlEnabled()) {
                    mySql.addPlayerKillDeath(playerUUID, killerName, FieldName.KILL);
                }

                if(!fm.isDropItemsOnDeathEnabled()) {
                    if(plugin.isDebugEnabled()) {
                        SendConsoleMessage.debug("Item drops disabled, clearing.");
                    }
                    e.getDrops().clear();
                }

                if(!fm.isDeathMessagesEnabled()){
                    e.setDeathMessage("");
                    return;
                }
                e.setDeathMessage(fm.getPrefix() + " " + ChatColor.AQUA + loser.getName() + ChatColor.RED + " was killed in a duel by "
                        + ChatColor.AQUA + killer.getName()  + ChatColor.RED +" in arena " + ChatColor.GOLD + dm.getPlayersArenaName(playerUUID));
            }  else {
                if(!fm.isDeathMessagesEnabled()){
                    e.setDeathMessage("");
                    return;
                }
                e.setDeathMessage(fm.getPrefix() + ChatColor.AQUA + loser.getName() + ChatColor.RED + " was killed in a duel!");
            }
            dm.endDuel(loser);
            loser.spigot().respawn();
        }
    }

}
