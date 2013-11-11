package com.teozcommunity.teozfrank.duelme.threads;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
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
    private int countDown;

    public StartDuelThread(DuelMe plugin, Player sender, Player target) {
        this.plugin = plugin;
        this.sender = sender;
        this.target = target;
        this.countDown = 15;
    }

    @Override
    public void run() {

        /* plugin.frozenPlayers.add(sender);
        plugin.frozenPlayers.add(target);

        if (this.countDown > 0) {
            switch (this.countDown) {
                case 15:
                    Util.sendMsg(sender,ChatColor.YELLOW + "Starting duel in: " + ChatColor.GOLD + this.countDown);
                    target.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "Starting duel in: " + ChatColor.GOLD + this.countDown);
                    break;
                case 10:
                    sender.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "Starting duel in: " + ChatColor.GOLD + this.countDown);
                    target.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "Starting duel in: " + ChatColor.GOLD + this.countDown);
                    break;
                case 5:
                case 4:
                case 3:
                case 2:
                case 1:
                    sender.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "Starting duel in: " + ChatColor.GOLD + this.countDown);
                    target.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "Starting duel in: " + ChatColor.GOLD + this.countDown);
                    break;
                default:
                    break;
            }
            this.countDown--;
        } else {

            plugin.frozenPlayers.clear();//let them move
            plugin.duelingPlayers.add(sender);
            plugin.duelingPlayers.add(target);
            sender.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "Duel!");
            target.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "Duel!");
            plugin.duelStatus = "IN PROGRESS";
            this.cancel();
        }*/
    }
}
