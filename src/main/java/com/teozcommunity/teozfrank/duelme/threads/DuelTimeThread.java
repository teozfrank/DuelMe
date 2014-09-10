package com.teozcommunity.teozfrank.duelme.threads;

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
        String senderName = sender.getName();
        String targetName = target.getName();
        UUID senderUUID = sender.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        if(duelArena.getPlayers().size() == 1) {
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("cancelling duel time thread as player size is now 1");
            }
            this.cancel();// just need to cancel if arena size gets to 1, start duel thread will handle the rewards.
        }

        if (this.duelTime > 0) {
            Util.setTime(sender, target, this.duelTime);
            this.duelTime--;
        } else {
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Ending duel, time is up!");
            }
            Util.setTime(sender, target, this.duelTime);
            dm.endDuel(duelArena);
            this.cancel();
        }
    }
}

