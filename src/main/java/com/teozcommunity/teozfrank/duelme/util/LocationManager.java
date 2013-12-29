package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 08/08/13
 * Time: 17:11
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class LocationManager {

    private DuelMe plugin;

    public LocationManager(DuelMe plugin) {
        this.plugin = plugin;
    }

    /**
     * gets the lobby spawn location from locations configuration file
     * @return lobby spawn location
     */
    public Location lobbySpawnLocation() {
        FileManager fm = plugin.getFileManager();
        String WorldIn = fm.getLocations().getString("locations.lobbyspawn.world");
        double targetxIn = fm.getLocations().getDouble("locations.lobbyspawn.x");
        double targetyIn = fm.getLocations().getDouble("locations.lobbyspawn.y");
        double targetzIn = fm.getLocations().getDouble("locations.lobbyspawn.z");

        World targetWorld = Bukkit.getWorld(WorldIn);

        Location lobbySpawnLoc = new Location(targetWorld, targetxIn, targetyIn + 0.5, targetzIn);

        return lobbySpawnLoc;
    }


    /**
     * sets the lobby spawn location to disk and reloads configuration file
     * @param p the player thats setting the lobby spawn location
     */
    public void setLobbySpawnLocation(Player p) {
        FileManager fm = plugin.getFileManager();
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getX();
        double senderyIn = loc.getY();
        double senderzIn = loc.getZ();

        fm.getLocations().set("locations.lobbyspawn.world", world);
        fm.getLocations().set("locations.lobbyspawn.x", senderxIn);
        fm.getLocations().set("locations.lobbyspawn.y", senderyIn);
        fm.getLocations().set("locations.lobbyspawn.z", senderzIn);
        fm.saveLocations();
        fm.reloadLocations();
        Util.sendMsg(p, ChatColor.GREEN + "Lobby Spawn Location set!");
    }
}