package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private List<String> allowedCommands;

    public PlayerEvents(DuelMe plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.allowedCommands = new ArrayList<String>();
        this.setupAllowedCommands();
    }

    private void setupAllowedCommands() {
        this.allowedCommands.add("/duel leave");
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
       Player player = e.getEntity();
       String playerName = player.getName();

       DuelManager dm = plugin.getDuelManager();
       FileManager fm = plugin.getFileManager();
       ItemManager im = plugin.getItemManager();

       if(dm.isInDuel(playerName)){
           DuelArena arena = dm.getPlayersArena(playerName);//get the duel arena the player is in
           arena.removePlayer(playerName);//remove the player from the arena
           dm.addDeadPlayer(playerName);//add the player as a dead player


           im.rewardPlayer(arena);

           if(!fm.isDeathMessagesEnabled()){
             e.setDeathMessage("");
             return;
           }
           if(e.getEntity().getKiller() instanceof Player){
               Player killer = e.getEntity().getKiller();
               e.setDeathMessage(ChatColor.GOLD + "[DuelMe] " + ChatColor.AQUA + player.getName() + ChatColor.RED + " was killed in a duel by "
                       + ChatColor.AQUA + killer.getName());
              return;
           }  else {
               e.setDeathMessage(ChatColor.GOLD + "[DuelMe] " + ChatColor.AQUA + player.getName() + ChatColor.RED + " was killed in a duel!");
           }

       }

    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if(dm.isDeadPlayer(playerName)){
            e.setRespawnLocation(fm.getLobbySpawnLocation());
            dm.restoreInventory(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();

        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();
        ItemManager im = plugin.getItemManager();

        if(dm.isInDuel(playerName)){
           DuelArena arena = dm.getPlayersArena(playerName);
           arena.removePlayer(playerName);
           player.teleport(fm.getLobbySpawnLocation());
           dm.restoreInventory(player);


           //im.rewardPlayer(arena);

        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerUseCommand(PlayerCommandPreprocessEvent e) {

        Player player = e.getPlayer();
        String playerName = player.getName();
        DuelManager dm = plugin.getDuelManager();

        if (dm.isInDuel(playerName)) {
            for (String allowedCommands : this.allowedCommands) {
                if (!(e.getMessage().equalsIgnoreCase(allowedCommands))) {
                    e.setCancelled(true);
                    Util.sendMsg(player, ChatColor.RED + "You may not use this command during a duel, use " +
                            ChatColor.AQUA + "/duel leave" + ChatColor.RED + " to leave.");
                    return;
                }
            }
        }

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




