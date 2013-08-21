package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 21/08/13
 * Time: 02:17
 * To change this template use File | Settings | File Templates.
 */
public class PlayerHitsPlayer implements Listener {

    private DuelMe plugin;

    public PlayerHitsPlayer(DuelMe plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(EntityDamageByEntityEvent e){

        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){//if the damager is a player and target is a player
           Player damager = (Player) e.getDamager();
           if(plugin.spectatingPlayers.contains(damager.getPlayer())){
              damager.sendMessage(plugin.pluginPrefix+ChatColor.RED+"You are not allowed to do that while spectating!");
              e.setCancelled(true);
           }
        }

    }
}
