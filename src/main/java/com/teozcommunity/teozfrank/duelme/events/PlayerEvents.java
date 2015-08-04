package com.teozcommunity.teozfrank.duelme.events;

/**
        The MIT License (MIT)

        Copyright (c) 2014 teozfrank

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/

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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
              dm.sendNormalDuelRequest(player, target.getName());//send a duel request
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

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerUseCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if(dm.isInDuel(playerUUID)) {
            if(!this.isAllowedCommand(e.getMessage())) {//if the command is not allowed
                Util.sendMsg(player, ChatColor.RED + "You are not allowed to use this command in a duel!");
                e.setCancelled(true);
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

    /**
     * is the command passed in allowed to be used
     * @param command the command to test
     * @return true if allowed, false if not
     */
    public boolean isAllowedCommand(String command) {
        if(this.allowedCommands.contains(command)) {
            return true;
        }
        return false;
    }
}




