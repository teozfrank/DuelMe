package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: teoz
 * Date: 06/11/13
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class PlayerEvents implements Listener {

    private DuelMe plugin;
    private static HashMap<Player, Vector> locations = new HashMap<Player, Vector>();

    public PlayerEvents(DuelMe plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRightClickToDuel(PlayerInteractEntityEvent e) {

        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if(!fm.isRightClickToDuelEnabled()){
            return;
        }

        if(entity instanceof Player){
            Player target = (Player) entity;
            if(player.isSneaking() && player.getItemInHand().equals(new ItemStack(Material.DIAMOND_SWORD))){//if the player is sneaking and has a diamond sword
              dm.sendRequest(player , target.getName());//send a duel request
              return;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        Player dueler = e.getPlayer();
        DuelManager dm = plugin.getDuelManager();

        if(dm.isInDuel(dueler.getName())){
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent e) {
        //TODO implement this method according to the event
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        //TODO implement this method according to the event
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        //TODO implement this method according to the event
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerUseCommand(PlayerCommandPreprocessEvent e) {
        //TODO implement this method according to the event
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        DuelManager dm = plugin.getDuelManager();
        if (dm.isFrozen(p.getName())) {

            Location loc = p.getLocation();
            if (locations.get(p) == null) {
                locations.put(p, loc.toVector());
            }

            if (loc.getBlockX() != locations.get(p).getBlockX() || loc.getBlockZ() != locations.get(p).getBlockZ()) {

                loc.setX(locations.get(p).getBlockX());
                loc.setZ(locations.get(p).getBlockZ());
                loc.setPitch(loc.getPitch());

                loc.setYaw(loc.getYaw());

                p.teleport(loc);
            }
        }
    }
}




