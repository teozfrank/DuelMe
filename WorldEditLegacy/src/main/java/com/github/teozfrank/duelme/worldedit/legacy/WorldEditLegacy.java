package com.github.teozfrank.duelme.worldedit.legacy;

import com.github.teozfrank.duelme.api.WorldEditSelectionHelper;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import util.WorldEditSelection;

public class WorldEditLegacy implements WorldEditSelectionHelper {

    @Override
    public WorldEditSelection getWorldEditSelection(Player player) {

        WorldEditSelection selectionWE = new WorldEditSelection();

        double pos1x, pos1y, pos1z, pos2x, pos2y, pos2z;
        //String worldName;
        //World selWorld;
        Location pos1;
        Location pos2;

        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        Selection selection = worldEdit.getSelection(player);

        if (selection != null) {
            World world = selection.getWorld();
            Location min = selection.getMinimumPoint();
            Location max = selection.getMaximumPoint();

            pos1x = max.getX();
            pos1y = max.getY();
            pos1z = max.getZ();
            pos1 = new Location(world, pos1x, pos1y, pos1z);
            selectionWE.setPos1(pos1);

            pos2x = min.getX();
            pos2y = min.getY();
            pos2z = min.getZ();
            pos2 = new Location(world, pos2x, pos2y, pos2z);
            selectionWE.setPos2(pos2);
            selectionWE.setSuccess(true);
        } else {
            selectionWE.setSuccess(false);
            return selectionWE;
        }
        return selectionWE;
    }
}
