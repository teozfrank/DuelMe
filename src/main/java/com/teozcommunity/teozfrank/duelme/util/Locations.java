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
        String senderWorldIn = plugin.getConfig().getString("duelme.duelsenderloc.world");
        double senderxIn = plugin.getConfig().getDouble("duelme.duelsenderloc.x");
        double senderyIn = plugin.getConfig().getDouble("duelme.duelsenderloc.y");
        double senderzIn = plugin.getConfig().getDouble("duelme.duelsenderloc.z");

        World senderWorld = Bukkit.getWorld(senderWorldIn);

        Location senderSpawnLocation = new Location(senderWorld,senderxIn,senderyIn,senderzIn);

        return senderSpawnLocation;
    }

    public Location targetSpawnLocation(){
        String targetWorldIn = plugin.getConfig().getString("duelme.dueltargetloc.world");
        double targetxIn = plugin.getConfig().getDouble("duelme.dueltargetloc.x");
        double targetyIn = plugin.getConfig().getDouble("duelme.dueltargetloc.y");
        double targetzIn = plugin.getConfig().getDouble("duelme.dueltargetloc.z");

        World targetWorld = Bukkit.getWorld(targetWorldIn);

        Location targetSpawnLoc = new Location(targetWorld,targetxIn,targetyIn,targetzIn);

        return targetSpawnLoc;
    }

    public Location lobbySpawnLocation(){
        String WorldIn = plugin.getConfig().getString("duelme.lobbyspawnloc.world");
        double targetxIn = plugin.getConfig().getDouble("duelme.lobbyspawnloc.x");
        double targetyIn = plugin.getConfig().getDouble("duelme.lobbyspawnloc.y");
        double targetzIn = plugin.getConfig().getDouble("duelme.lobbyspawnloc.z");

        World targetWorld = Bukkit.getWorld(WorldIn);

        Location lobbySpawnLoc = new Location(targetWorld,targetxIn,targetyIn,targetzIn);

        return lobbySpawnLoc;
    }

    public Location spectateSpawnLocation(){
        String WorldIn = plugin.getConfig().getString("duelme.spectatespawnloc.world");
        double targetxIn = plugin.getConfig().getDouble("duelme.spectatespawnloc.x");
        double targetyIn = plugin.getConfig().getDouble("duelme.spectatespawnloc.y");
        double targetzIn = plugin.getConfig().getDouble("duelme.spectatespawnloc.z");

        World targetWorld = Bukkit.getWorld(WorldIn);

        Location specatateSpawnLoc = new Location(targetWorld,targetxIn,targetyIn,targetzIn);

        return specatateSpawnLoc;
    }

    public void setSenderSpawnLocation(Player p){
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getBlockX();
        double senderyIn = loc.getBlockY();
        double senderzIn = loc.getBlockZ();

        plugin.getConfig().set("duelme.duelsenderloc.world",world);
        plugin.getConfig().set("duelme.duelsenderloc.x", senderxIn);
        plugin.getConfig().set("duelme.duelsenderloc.y",senderyIn);
        plugin.getConfig().set("duelme.duelsenderloc.z",senderzIn);
        plugin.saveConfig();
        plugin.reloadConfig();
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Sender Spawn Location set!");

    }

    public void setTargetSpawnLocation(Player p){
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getBlockX();
        double senderyIn = loc.getBlockY();
        double senderzIn = loc.getBlockZ();

        plugin.getConfig().set("duelme.dueltargetloc.world",world);
        plugin.getConfig().set("duelme.dueltargetloc.x", senderxIn);
        plugin.getConfig().set("duelme.dueltargetloc.y",senderyIn);
        plugin.getConfig().set("duelme.dueltargetloc.z",senderzIn);
        plugin.saveConfig();
        plugin.reloadConfig();
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Target Spawn Location set!");

    }

    public void setLobbySpawnLocation(Player p){
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getBlockX();
        double senderyIn = loc.getBlockY();
        double senderzIn = loc.getBlockZ();

        plugin.getConfig().set("duelme.lobbyspawnloc.world",world);
        plugin.getConfig().set("duelme.lobbyspawnloc.x", senderxIn);
        plugin.getConfig().set("duelme.lobbyspawnloc.y",senderyIn);
        plugin.getConfig().set("duelme.lobbyspawnloc.z",senderzIn);
        plugin.saveConfig();
        plugin.reloadConfig();
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Lobby Spawn Location set!");

    }

    public void setSpectateLocation(Player p){
        Location loc = p.getLocation();
        String world = p.getWorld().getName();

        double senderxIn = loc.getBlockX();
        double senderyIn = loc.getBlockY();
        double senderzIn = loc.getBlockZ();

        plugin.getConfig().set("duelme.spectatespawnloc.world",world);
        plugin.getConfig().set("duelme.spectatespawnloc.x", senderxIn);
        plugin.getConfig().set("duelme.spectatespawnloc.y",senderyIn);
        plugin.getConfig().set("duelme.spectatespawnloc.z",senderzIn);
        plugin.saveConfig();
        plugin.reloadConfig();
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Spectate Spawn Location set!");

    }


}
