package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Location;
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent e){

        Player p = e.getPlayer();

        //if(!plugin.frozenPlayers.isEmpty()){
           // if(plugin.frozenPlayers.contains(p.getPlayer())){
                System.out.print(p.getName());
                int x = e.getFrom().getBlockX()-1;
                int y = e.getFrom().getBlockY();
                int z = e.getFrom().getBlockZ()-1;
                Location from = new Location(p.getWorld(),x,y,z);
                e.setTo(from);
            //}
        //}

    }
}
