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
 * User: Frank
 * Date: 08/08/13
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public class PlayerDeath implements Listener {
    private DuelMe plugin;

    public PlayerDeath(DuelMe plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        if(plugin.duelingPlayers.size()==2){
           e.getDrops().clear();//drop nothing on death
           e.setDeathMessage(ChatColor.YELLOW+p.getName()+" Was Killed in a Duel!");
           plugin.util.endDuel();//end the duel
        }
        else {
           plugin.util.endDuel();
        }
    }
}
