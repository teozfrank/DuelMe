package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 08/08/13
 * Time: 17:11
 * To change this template use File | Settings | File Templates.
 */
public class Locations {

    private DuelMe plugin;

    public Locations(DuelMe plugin){
        this.plugin = plugin;
    }

    public Location senderSpawnLocation(){
        String senderWorldIn = plugin.fileManager.getLocations().getString("locations.duelsender.world");
        double senderxIn = plugin.fileManager.getLocations().getDouble("locations.duelsender.x");
        double senderyIn = plugin.fileManager.getLocations().getDouble("locations.duelsender.y");
        double senderzIn = plugin.fileManager.getLocations().getDouble("locations.duelsender.z");

        World senderWorld = Bukkit.getWorld(senderWorldIn);

        Location senderSpawnLocation = new Location(senderWorld,senderxIn,senderyIn+0.5,senderzIn);

        return senderSpawnLocation;
    }

    public Location targetSpawnLocation(){
        String targetWorldIn = plugin.fileManager.getLocations().getString("locations.dueltarget.world");
        double targetxIn = plugin.fileManager.getLocations().getDouble("locations.dueltarget.x");
        double targetyIn = plugin.fileManager.getLocations().getDouble("locations.dueltarget.y");
        double targetzIn = plugin.fileManager.getLocations().getDouble("locations.dueltarget.z");

        World targetWorld = Bukkit.getWorld(targetWorldIn);

        Location targetSpawnLoc = new Location(targetWorld,targetxIn,targetyIn+0.5,targetzIn);

        return targetSpawnLoc;
    }

    public Location lobbySpawnLocation(){
        String WorldIn = plugin.fileManager.getLocations().getString("locations.lobbyspawn.world");
        double targetxIn = plugin.fileManager.getLocations().getDouble("locations.lobbyspawn.x");
        double targetyIn = plugin.fileManager.getLocations().getDouble("locations.lobbyspawn.y");
        double targetzIn = plugin.fileManager.getLocations().getDouble("locations.lobbyspawn.z");

        World targetWorld = Bukkit.getWorld(WorldIn);

        Location lobbySpawnLoc = new Location(targetWorld,targetxIn,targetyIn+0.5,targetzIn);

        return lobbySpawnLoc;
    }

    public Location spectateSpawnLocation(){
        String WorldIn = plugin.fileManager.getLocations().getString("locations.spectatespawn.world");
        double targetxIn = plugin.fileManager.getLocations().getDouble("locations.spectatespawn.x");
        double targetyIn = plugin.fileManager.getLocations().getDouble("locations.spectatespawn.y");
        double targetzIn = plugin.fileManager.getLocations().getDouble("locations.spectatespawn.z");

        World targetWorld = Bukkit.getWorld(WorldIn);

        Location specatateSpawnLoc = new Location(targetWorld,targetxIn,targetyIn+0.5,targetzIn);

        return specatateSpawnLoc;
    }

    public void setSenderSpawnLocation(Player p){
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getX();
        double senderyIn = loc.getY();
        double senderzIn = loc.getZ();



        plugin.fileManager.getLocations().set("locations.duelsender.world",world);
        plugin.fileManager.getLocations().set("locations.duelsender.x", senderxIn);
        plugin.fileManager.getLocations().set("locations.duelsender.y",senderyIn);
        plugin.fileManager.getLocations().set("locations.duelsender.z",senderzIn);
        plugin.fileManager.saveLocations();
        plugin.fileManager.reloadLocations();
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Sender Spawn Location set!");

    }

    public void setTargetSpawnLocation(Player p){
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getX();
        double senderyIn = loc.getY();
        double senderzIn = loc.getZ();

        plugin.fileManager.getLocations().set("locations.dueltarget.world",world);
        plugin.fileManager.getLocations().set("locations.dueltarget.x", senderxIn);
        plugin.fileManager.getLocations().set("locations.dueltarget.y",senderyIn);
        plugin.fileManager.getLocations().set("locations.dueltarget.z",senderzIn);
        plugin.fileManager.saveLocations();
        plugin.fileManager.reloadLocations();
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Target Spawn Location set!");

    }

    public void setLobbySpawnLocation(Player p){
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getX();
        double senderyIn = loc.getY();
        double senderzIn = loc.getZ();

        plugin.fileManager.getLocations().set("locations.lobbyspawn.world",world);
        plugin.fileManager.getLocations().set("locations.lobbyspawn.x", senderxIn);
        plugin.fileManager.getLocations().set("locations.lobbyspawn.y",senderyIn);
        plugin.fileManager.getLocations().set("locations.lobbyspawn.z",senderzIn);
        plugin.fileManager.saveLocations();
        plugin.fileManager.reloadLocations();
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Lobby Spawn Location set!");

    }

    public void setSpectateLocation(Player p){
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getX();
        double senderyIn = loc.getY();
        double senderzIn = loc.getZ();

        plugin.fileManager.getLocations().set("locations.spectatespawn.world",world);
        plugin.fileManager.getLocations().set("locations.spectatespawn.x", senderxIn);
        plugin.fileManager.getLocations().set("locations.spectatespawn.y",senderyIn);
        plugin.fileManager.getLocations().set("locations.spectatespawn.z",senderzIn);
        plugin.fileManager.saveLocations();
        plugin.fileManager.reloadLocations();
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Spectate Spawn Location set!");

    }


}
