package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.mysql.FieldName;
import com.teozcommunity.teozfrank.duelme.mysql.MySql;
import com.teozcommunity.teozfrank.duelme.util.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

/**
 The MIT License (MIT)

 Copyright (c) 2014 teozfrank

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
public class EntityDamage implements Listener {

    private DuelMe plugin;

    public EntityDamage(DuelMe plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();

        if (!(entity instanceof Player) ) {//if the damage did not occur from a player to a player
            return;
        }

        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();
        MySql mySql = plugin.getMySql();
        Player player = (Player) entity;
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();

        if (dm.isInDuel(playerUUID)) {// if the player is in a duel
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Player Health: " + player.getHealth());
                SendConsoleMessage.debug("Damage to player: " + e.getDamage());
                SendConsoleMessage.debug("Health - damage: " + (player.getHealth() - e.getDamage()));
            }
            DuelArena playersArena = dm.getPlayersArenaByUUID(playerUUID);
            if (playersArena.getDuelState() == DuelState.STARTING) {//if the duel state is starting
                e.setCancelled(true); //cancel the event
                return;
            } else if (playersArena.getDuelState() == DuelState.STARTED
                    && (player.getHealth() - e.getDamage()) < 1) {

                if(plugin.isDebugEnabled()) {
                    SendConsoleMessage.debug("player killed!");
                }
                if (fm.isMySqlEnabled()) {
                    mySql.addPlayerKillDeath(playerUUID, playerName, FieldName.DEATH);
                }
                Util.broadcastMessage(fm.getPrefix() + ChatColor.AQUA + player.getName() + ChatColor.RED + " was killed in a duel!");
                dm.endDuel(player);
                e.setCancelled(true);
            }
        }
    }
}


