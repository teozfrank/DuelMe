package com.teozcommunity.teozfrank.duelme.menus;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.SendConsoleMessage;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 The MIT License (MIT)

 Copyright (c) 2015 teozfrank

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
public class AcceptMenu implements Listener {

    private DuelMe plugin;
    private Inventory acceptMenu;
    private String senderName;
    private ItemStack accept, ignore;

    public AcceptMenu(DuelMe plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        acceptMenu = Bukkit.getServer().createInventory(null, 9 , ChatColor.translateAlternateColorCodes('&', "&cDuel request"));
        accept = Util.createMenuItem(DyeColor.GREEN, "Accept", "Click this item to accept a duel request.");
        ignore = Util.createMenuItem(DyeColor.RED, "Ignore", "Click this item to ignore this duel request.");

        acceptMenu.setItem(0, accept);
        acceptMenu.setItem(1, ignore);
    }

    /**
     * opens a duel accept menu
     * @param sender the duel sender
     * @param target the duel target
     */
    public void openNormalDuelAccept(Player sender, Player target){
        senderName = sender.getName();
        target.openInventory(acceptMenu);
    }

    /**
     * opens up a duel bet request accept
     * @param sender the duel sender
     * @param target the duel target
     * @param amount the amount for the duel bet
     */
    public void openDuelBetAccept(Player sender, Player target, double amount){
        List<String> acceptLore = new ArrayList<String>();
        acceptLore.add("Click this item to accept the");
        acceptLore.add("duel request for the amount of " + amount);
        senderName = sender.getName();
        acceptMenu = Bukkit.getServer().createInventory(null, 9 , ChatColor.translateAlternateColorCodes('&', "&cBet from " + sender.getName()));
        accept = Util.createMenuItem(DyeColor.GREEN, "Accept", acceptLore);
        ignore = Util.createMenuItem(DyeColor.RED, "Ignore", "Click this item to ignore this duel request.");

        acceptMenu.setItem(0, accept);
        acceptMenu.setItem(1, ignore);
        target.openInventory(acceptMenu);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent e){
        if(!e.getInventory().getName().equalsIgnoreCase(acceptMenu.getName())){
            return;
        }

        if(e.getCurrentItem() == null ) {
            return;
        }

        if(!e.getCurrentItem().hasItemMeta()){
            e.setCancelled(true);
        }

        if(plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("Accept Duel Inventory Clicked.");
        }

        if(e.getCurrentItem().hasItemMeta()){
            Player clicker = (Player) e.getWhoClicked();
            if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Accept")){
                e.setCancelled(true);
                DuelManager dm = plugin.getDuelManager();
                dm.acceptRequest(clicker, senderName);
                e.getWhoClicked().closeInventory();
            }
            if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Ignore")){
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
            }
        }
    }
}