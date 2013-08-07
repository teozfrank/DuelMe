package com.teozcommunity.teozfrank.duelme.listeners;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 06/08/13
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
public class PlayerMove implements Listener {

    private DuelMe plugin;

    public PlayerMove(DuelMe plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e){

        Player p = e.getPlayer();

        if(!plugin.frozenPlayers.isEmpty()){
            if(plugin.frozenPlayers.contains(p.getName())){
                System.out.print(p.getName());
                e.setTo(e.getFrom());
            }
        }

    }
}
