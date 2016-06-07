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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by Frank on 24/07/2014.
 */
public class DuelTimeThread extends BukkitRunnable {

    private DuelMe plugin;
    private Player sender;
    private Player target;
    private DuelArena duelArena;
    private int duelTime;

    public DuelTimeThread(DuelMe plugin, Player sender, Player target,DuelArena duelArena, int duelTime) {
        this.plugin = plugin;
        this.sender = sender;
        this.target = target;
        this.duelTime = duelTime;
        this.duelArena = duelArena;
    }

    @Override
    public void run() {
        DuelManager dm = plugin.getDuelManager();
        MessageManager mm = plugin.getMessageManager();
        int duelSize = duelArena.getPlayers().size();

        if(duelArena.getPlayers().size() == 1) {
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("cancelling duel time thread as player size is now 1");
            }
            this.cancel();// just need to cancel if arena size gets to 1, start duel thread will handle the rewards.
        }

        if (this.duelTime > 0 && duelSize == 2) {
            //Util.setTime(sender, target, this.duelTime);
            String duelEndActionBar = mm.getDuelRemainingActionBarMessage();
            duelEndActionBar = duelEndActionBar.replaceAll("%seconds%", String.valueOf(this.duelTime));
            plugin.getTitleActionbar().sendActionbar(sender, target, duelEndActionBar);
            this.duelTime--;
        } else {
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Ending duel time thread, time is up!");
            }
            if(duelSize == 2) {
                //Util.setTime(sender, target, this.duelTime);
            }

            dm.endDuel(duelArena);
            this.cancel();
        }
    }
}

