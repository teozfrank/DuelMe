package com.teozcommunity.teozfrank.duelme.threads;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 06/08/13
 * Time: 20:38
 * To change this template use File | Settings | File Templates.
 */
public class StartDuelThread extends BukkitRunnable {

    private DuelMe plugin;
    private Player sender;
    private Player target;

    public StartDuelThread(DuelMe plugin,Player sender, Player target) {
        this.plugin = plugin;
        this.sender = sender;
        this.target = target;
    }

    @Override
    public void run() {
        for(int x=10;x>=1;x--){
            sender.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Duel Starting in: "+x);
            target.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Duel Starting in: "+x);

            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException e){
                plugin.getLogger().severe("there was an error while counting down the start of a duel!");
            }

        }
        plugin.frozenPlayers.clear();
        plugin.duelingPlayers.add(sender.getPlayer());
        plugin.duelingPlayers.add(target.getPlayer());
        sender.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Duel!");
        target.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Duel!");
        plugin.duelStatus = "IN PROGRESS";

    }
}
