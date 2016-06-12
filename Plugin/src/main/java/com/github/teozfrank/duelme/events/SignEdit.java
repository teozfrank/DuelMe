package com.github.teozfrank.duelme.events;

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.DuelArena;
import com.github.teozfrank.duelme.util.DuelManager;
import com.github.teozfrank.duelme.util.FileManager;
import com.github.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

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

public class SignEdit implements Listener {

    private DuelMe plugin;

    public SignEdit(DuelMe plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onSignEdit(SignChangeEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        String line1 = e.getLine(0);
        String line2 = e.getLine(1);
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if(line1 == null) {
            return;
        }

        if(line1.equalsIgnoreCase("[duelme]")) {
            if(!player.hasPermission("duelme.admin.signs.create")) {
                Util.sendMsg(player, ChatColor.RED + "You do not have the permission duelme.admin.signs.create!");
                return;
            }
            if(line2 == null || line2.equals("")) {
                Util.sendMsg(player, ChatColor.RED + "You must provide an arena name to create a duel status sign!.");
                block.breakNaturally();
                return;
            }

            DuelArena arena = dm.getDuelArenaByName(line2);

            if(arena == null) {
                Util.sendMsg(player, ChatColor.RED + "Duel arena " + line2 + " does not exist!");
                block.breakNaturally();
                return;
            }

            e.setLine(0, ChatColor.GREEN + "[DuelMe]");
            e.setLine(1, arena.getName());
            e.setLine(2, ChatColor.AQUA + arena.getDuelState().toString());
            e.setLine(3, arena.getPlayers().size() + "/2");

            fm.saveArenaSign(arena.getName(), block.getWorld().getName(), block.getX(), block.getY(), block.getZ());

            Util.sendMsg(player, ChatColor.GREEN + "Successfully created a duel arena status sign!");

        }

    }

}
