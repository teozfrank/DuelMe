package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.mysql.FieldName;
import com.teozcommunity.teozfrank.duelme.mysql.MySql;
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
import java.util.UUID;

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
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();

        if(player.hasPermission("duelme.admin.update.notify")) {
            if(UpdateChecker.isUpdateAvailable() && plugin.getConfig().getBoolean("duelme.checkforupdates")) {
                Util.sendMsg(player, ChatColor.GREEN + "There is an update available for" +
                        " for this plugin get it on bukkit dev page: " +
                        ChatColor.AQUA + "http://dev.bukkit.org/bukkit-plugins/duelme/");
              }
        }
        
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
              dm.sendNormalDuelRequest(player , target.getName());//send a duel request
              return;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        Player dueler = e.getPlayer();
        DuelManager dm = plugin.getDuelManager();

        if(dm.isInDuel(dueler.getUniqueId())){
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent e) {
       Player player = e.getEntity();
       String playerName = player.getName();
       UUID playerUUID = player.getUniqueId();

       DuelManager dm = plugin.getDuelManager();
       FileManager fm = plugin.getFileManager();
       MySql mySql = plugin.getMySql();

       if(dm.isInDuel(playerUUID)){
           dm.addDeadPlayer(playerUUID);

           if(fm.isMySqlEnabled()) {
               mySql.addPlayerKillDeath(playerUUID, playerName, FieldName.DEATH);
           }

           if(e.getEntity().getKiller() instanceof Player){
               Player killer = e.getEntity().getKiller();
               String killerName = killer.getName();
               if(fm.isMySqlEnabled()) {
                   mySql.addPlayerKillDeath(playerUUID, killerName, FieldName.KILL);
               }

               if(!fm.isDropItemsOnDeathEnabled()) {
                   if(plugin.isDebugEnabled()) {
                       SendConsoleMessage.debug("Item drops disabled, clearing.");
                   }
                   e.getDrops().clear();
               }

               if(!fm.isDeathMessagesEnabled()){
                   e.setDeathMessage("");
                   return;
               }
               e.setDeathMessage(ChatColor.GOLD + "[DuelMe] " + ChatColor.AQUA + player.getName() + ChatColor.RED + " was killed in a duel by "
                       + ChatColor.AQUA + killer.getName());
           }  else {
               if(!fm.isDeathMessagesEnabled()){
                   e.setDeathMessage("");
                   return;
               }
               e.setDeathMessage(ChatColor.GOLD + "[DuelMe] " + ChatColor.AQUA + player.getName() + ChatColor.RED + " was killed in a duel!");
           }
           dm.endDuel(player);

       }

    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if(dm.isDeadPlayer(playerUUID)){
            PlayerData playerData = dm.getPlayerDataByUUID(playerUUID);

            e.setRespawnLocation(playerData.getLocaton());
            dm.restorePlayerData(player);
            dm.removedDeadPlayer(playerUUID);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();

        DuelManager dm = plugin.getDuelManager();

        if(dm.isInDuel(playerUUID)){
            dm.endDuel(player);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerUseCommand(PlayerCommandPreprocessEvent e) {

        Player player = e.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        DuelManager dm = plugin.getDuelManager();

        if (dm.isInDuel(playerUUID)) {
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
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        DuelManager dm = plugin.getDuelManager();
        if (dm.isFrozen(playerUUID)) {


            Location loc = player.getLocation();
            if (locations.get(player) == null) {
                locations.put(player, loc.toVector());
            }

            if (loc.getBlockX() != locations.get(player).getBlockX() || loc.getBlockZ() != locations.get(player).getBlockZ()) {
                if(plugin.isDebugEnabled()) {
                    SendConsoleMessage.debug("Frozen player in duel moved, teleporting back!");
                }

                loc.setX(locations.get(player).getBlockX());
                loc.setZ(locations.get(player).getBlockZ());
                loc.setPitch(loc.getPitch());

                loc.setYaw(loc.getYaw());

                player.teleport(loc);
            }
        }
    }
}




