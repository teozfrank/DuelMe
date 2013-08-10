package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

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


}
