package com.teozcommunity.teozfrank.duelme.threads;

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
import com.teozcommunity.teozfrank.duelme.util.*;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class StartDuelThread extends BukkitRunnable {

    private DuelMe plugin;
    private Player sender;
    private Player target;
    private DuelArena duelArena;
    private int countDown;

    public StartDuelThread(DuelMe plugin, Player sender, Player target, DuelArena duelArena) {
        this.plugin = plugin;
        this.sender = sender;
        this.target = target;
        this.countDown = plugin.getFileManager().getDuelCountdownTime();
        this.duelArena = duelArena;
    }

    @Override
    public void run() {
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();
        MessageManager mm = plugin.getMessageManager();
        int duelTime = fm.getDuelTime();
        String senderName = sender.getName();
        String targetName = target.getName();
        UUID senderUUID = sender.getUniqueId();
        UUID targetUUID = target.getUniqueId();
        int duelSize = duelArena.getPlayers().size();

        if (plugin.isDebugEnabled()) {
            SendConsoleMessage.debug("Duel size: " + duelSize);
        }

        if (duelSize == 0) {
            dm.endDuel(duelArena);
            this.cancel();
        }


        if (this.countDown > 0 && duelSize == 2) {
            String duelStartActionBar = mm.getDuelStartingActionBarMessage();
            duelStartActionBar = duelStartActionBar.replaceAll("%seconds%", String.valueOf(this.countDown));
            Util.sendActionBarMessage(sender, target, duelStartActionBar);
            this.countDown--;
        } else {
            if(duelSize == 2) {
                Util.setTime(sender, target, this.countDown);
                Util.sendMsg(sender, target, ChatColor.YELLOW + "Duel!");
                duelArena.setDuelState(DuelState.STARTED);
                dm.surroundLocation(duelArena.getSpawnpoint1(), Material.AIR);
                dm.surroundLocation(duelArena.getSpawnpoint2(), Material.AIR);
                dm.updateDuelStatusSign(duelArena);
            }

            //dm.removeFrozenPlayer(senderUUID);
            //dm.removeFrozenPlayer(targetUUID);

            if (plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Stopping duel start thread.");
            }
            this.cancel();

            if (duelTime != 0 && duelSize == 2) {
                if (plugin.isDebugEnabled()) {
                    SendConsoleMessage.debug("Duel time limit is set, starting countdown task.");
                }
                new DuelTimeThread(plugin, sender, target, duelArena, duelTime).runTaskTimer(plugin, 20L, 20L);
            }
        }
    }
}
