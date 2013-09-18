package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 06/08/13
 * Time: 20:48
 * Project: DuelMe
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class PlayerMove implements Listener {

    private static HashMap<Player, Vector> Locations = new HashMap<Player, Vector>();

    private DuelMe plugin;

    public PlayerMove(DuelMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        if (plugin.frozenPlayers.contains(p)) {
            Location loc = p.getLocation();

            if (Locations.get(p) == null) {
                Locations.put(p, loc.toVector());
            }

            if (loc.getBlockX() != Locations.get(p).getBlockX() || loc.getBlockZ() != Locations.get(p).getBlockZ()) {
                loc.setX(Locations.get(p).getBlockX());
                loc.setZ(Locations.get(p).getBlockZ());
                loc.setPitch(loc.getPitch());
                loc.setYaw(loc.getYaw());
                p.teleport(loc);
            }

        }

    }
}
