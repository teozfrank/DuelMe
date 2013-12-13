package com.teozcommunity.teozfrank.duelme.threads;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.DuelState;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 06/08/13
 * Time: 20:38
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class StartDuelThread extends BukkitRunnable {

    private DuelMe plugin;
    private Player sender;
    private Player target;
    private DuelArena duelArena;
    private int countDown;

    public StartDuelThread(DuelMe plugin, Player sender, Player target,DuelArena duelArena) {
        this.plugin = plugin;
        this.sender = sender;
        this.target = target;
        this.countDown = 15;
        this.duelArena = duelArena;
    }

    @Override
    public void run() {
        DuelManager dm = plugin.getDuelManager();
        String senderName = sender.getName();
        String targetName = target.getName();

        dm.addFrozenPlayer(senderName, targetName); // stop the players from moving

        if (this.countDown > 0) {
            switch (this.countDown) {
                case 15:
                    Util.sendMsg(sender, target, ChatColor.YELLOW + "Starting duel in: " + ChatColor.GOLD + this.countDown);
                    break;
                case 10:
                    Util.sendMsg(sender, target, ChatColor.YELLOW + "Starting duel in: " + ChatColor.GOLD + this.countDown);
                    break;
                case 5:
                case 4:
                case 3:
                case 2:
                case 1:
                    Util.sendMsg(sender, target, ChatColor.YELLOW + "Starting duel in: " + ChatColor.GOLD + this.countDown);
                    break;
                default:
                    break;
            }
            this.countDown--;
        } else {


            Util.sendMsg(sender, target, ChatColor.YELLOW + "Duel!");
            duelArena.setDuelState(DuelState.STARTED);
            this.cancel();
        }
    }
}
