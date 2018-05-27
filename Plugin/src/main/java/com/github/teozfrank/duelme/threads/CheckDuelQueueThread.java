package com.github.teozfrank.duelme.threads;

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CheckDuelQueueThread extends BukkitRunnable {

    private DuelMe plugin;

    public CheckDuelQueueThread(DuelMe plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();
        MessageManager mm = plugin.getMessageManager();

        if(dm.getQueuedPlayersSize() == 0) {
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("No players to start an automated duel.");
            }
            return;
        }

        if(dm.getQueuedPlayersSize() == 1) {
            Util.sendMsg(dm.getQueuedPlayerUUIDs().get(0), ChatColor.YELLOW + "There is not enough players to start a duel yet.");
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("There is one player in the queue.");
            }
        }

        while(dm.getQueuedPlayersSize() >= 2) {

            Player player1 = Bukkit.getPlayer(dm.getQueuedPlayerUUIDs().get(0));
            Player player2 = Bukkit.getPlayer(dm.getQueuedPlayerUUIDs().get(1));
            boolean success = dm.startDuel(player1, player2, null);

            if(success && player1 != null && player2 != null) {
                dm.removeQueuedPlayer(player1.getUniqueId());
                dm.removeQueuedPlayer(player2.getUniqueId());
                if(plugin.isDebugEnabled()) {
                    SendConsoleMessage.debug("Removing first pair of players from queue.");
                }
            }
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("Player size is >= 2");
                SendConsoleMessage.debug("Player 1 is: " + player1.getName());
                SendConsoleMessage.debug("Player 2 is: " + player2.getName());
                SendConsoleMessage.debug("Successful start: " + success);
            }
        }
    }
}
