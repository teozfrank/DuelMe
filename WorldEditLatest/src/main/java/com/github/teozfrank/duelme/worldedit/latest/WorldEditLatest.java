package com.github.teozfrank.duelme.worldedit.latest;

import com.github.teozfrank.duelme.api.WorldEditSelectionHelper;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionOwner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import util.WorldEditSelection;

public class WorldEditLatest implements WorldEditSelectionHelper {

    @Override
    public WorldEditSelection getWorldEditSelection(Player player) {

        WorldEditSelection selection = new WorldEditSelection();

        double pos1x, pos1y, pos1z, pos2x, pos2y, pos2z;
        //String worldName;
        //World selWorld;
        Location pos1;
        Location pos2;

        SessionOwner sessionOwner = BukkitAdapter.adapt(player);
        LocalSession playerSession = WorldEdit.getInstance().getSessionManager().get(sessionOwner);
        Region playerSelection;

        try {
            playerSelection = playerSession.getSelection(playerSession.getSelectionWorld());
        } catch (Exception e) {
            selection.setSuccess(false);
            return selection;
        }
        BlockVector3 minimumPoint = playerSelection.getMinimumPoint();
        BlockVector3 maximumPoint = playerSelection.getMaximumPoint();

        World world = Bukkit.getWorld(playerSession.getSelectionWorld().getName());

        pos1x = maximumPoint.getX();
        pos1y = maximumPoint.getY();
        pos1z = maximumPoint.getZ();
        pos1 = new Location(world, pos1x, pos1y, pos1z);
        selection.setPos1(pos1);

        pos2x = minimumPoint.getX();
        pos2y = minimumPoint.getY();
        pos2z = minimumPoint.getZ();
        pos2 = new Location(world, pos2x, pos2y, pos2z);
        selection.setPos2(pos2);
        selection.setSuccess(true);

        return selection;
    }
}
