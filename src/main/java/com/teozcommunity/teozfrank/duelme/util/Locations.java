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
public class Locations {

    private DuelMe plugin;

    public Locations(DuelMe plugin) {
        this.plugin = plugin;
    }

    /**
     * gets the senders spawn location from locations configuration file
     * @return senders spawn location
     */
    public Location senderSpawnLocation() {
        FileManager fm = plugin.getFileManager();
        String senderWorldIn = fm.getLocations().getString("locations.duelsender.world");
        double senderxIn = fm.getLocations().getDouble("locations.duelsender.x");
        double senderyIn = fm.getLocations().getDouble("locations.duelsender.y");
        double senderzIn = fm.getLocations().getDouble("locations.duelsender.z");

        World senderWorld = Bukkit.getWorld(senderWorldIn);

        Location senderSpawnLocation = new Location(senderWorld, senderxIn, senderyIn + 0.5, senderzIn);

        return senderSpawnLocation;
    }

    /**
     * gets the targets spawn location from locations configuration file
     * @return targets spawn location
     */
    public Location targetSpawnLocation() {
        FileManager fm = plugin.getFileManager();
        String targetWorldIn = fm.getLocations().getString("locations.dueltarget.world");
        double targetxIn = fm.getLocations().getDouble("locations.dueltarget.x");
        double targetyIn = fm.getLocations().getDouble("locations.dueltarget.y");
        double targetzIn = fm.getLocations().getDouble("locations.dueltarget.z");

        World targetWorld = Bukkit.getWorld(targetWorldIn);

        Location targetSpawnLoc = new Location(targetWorld, targetxIn, targetyIn + 0.5, targetzIn);

        return targetSpawnLoc;
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
     * gets the spectators spawn location from locations configuration file
     * @return spectators spawn location
     */
    public Location spectateSpawnLocation() {
        FileManager fm = plugin.getFileManager();
        String WorldIn = fm.getLocations().getString("locations.spectatespawn.world");
        double targetxIn = fm.getLocations().getDouble("locations.spectatespawn.x");
        double targetyIn = fm.getLocations().getDouble("locations.spectatespawn.y");
        double targetzIn = fm.getLocations().getDouble("locations.spectatespawn.z");

        World targetWorld = Bukkit.getWorld(WorldIn);

        Location specatateSpawnLoc = new Location(targetWorld, targetxIn, targetyIn + 0.5, targetzIn);

        return specatateSpawnLoc;
    }

    /**
     * sets the senders spawn location to disk and reloads configuration file
     * @param p the player thats setting the duel sender spawn location
     */
    public void setSenderSpawnLocation(Player p) {
        FileManager fm = plugin.getFileManager();
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getX();
        double senderyIn = loc.getY();
        double senderzIn = loc.getZ();

        fm.getLocations().set("locations.duelsender.world", world);
        fm.getLocations().set("locations.duelsender.x", senderxIn);
        fm.getLocations().set("locations.duelsender.y", senderyIn);
        fm.getLocations().set("locations.duelsender.z", senderzIn);
        fm.saveLocations();
        fm.reloadLocations();
        Util.sendMsg(p,ChatColor.GREEN + "Sender Spawn Location set!");

    }

    /**
     * sets the targets spawn location to disk and reloads configuration file
     * @param p the player thats setting the duel target spawn location
     */
    public void setTargetSpawnLocation(Player p) {
        FileManager fm = plugin.getFileManager();
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getX();
        double senderyIn = loc.getY();
        double senderzIn = loc.getZ();

        fm.getLocations().set("locations.dueltarget.world", world);
        fm.getLocations().set("locations.dueltarget.x", senderxIn);
        fm.getLocations().set("locations.dueltarget.y", senderyIn);
        fm.getLocations().set("locations.dueltarget.z", senderzIn);
        fm.saveLocations();
        fm.reloadLocations();
        Util.sendMsg(p,ChatColor.GREEN + "Target Spawn Location set!");

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

    /**
     * sets the spectators spawn location to disk and reloads configuration file
     * @param p the player thats setting the spectators spawn location
     */
    public void setSpectateLocation(Player p) {
        FileManager fm = plugin.getFileManager();
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getX();
        double senderyIn = loc.getY();
        double senderzIn = loc.getZ();

        fm.getLocations().set("locations.spectatespawn.world", world);
        fm.getLocations().set("locations.spectatespawn.x", senderxIn);
        fm.getLocations().set("locations.spectatespawn.y", senderyIn);
        fm.getLocations().set("locations.spectatespawn.z", senderzIn);
        fm.saveLocations();
        fm.reloadLocations();
        Util.sendMsg(p, ChatColor.GREEN + "Spectate Spawn Location set!");
    }
}