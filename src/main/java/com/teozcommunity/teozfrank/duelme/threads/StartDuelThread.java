package com.teozcommunity.teozfrank.duelme.threads;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
    private int countDown;

    public StartDuelThread(DuelMe plugin,Player sender, Player target) {
        this.plugin = plugin;
        this.sender = sender;
        this.target = target;
        this.countDown = 15;
    }

    @Override
    public void run(){

        plugin.frozenPlayers.add(sender);
        plugin.frozenPlayers.add(target);

        if (this.countDown > 0) {
            switch(this.countDown) {
                case 15:
                    sender.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Starting duel in: "+ChatColor.GOLD+this.countDown);
                    target.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Starting duel in: "+ChatColor.GOLD+this.countDown);
                   break;
                case 10:
                    sender.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Starting duel in: "+ChatColor.GOLD+this.countDown);
                    target.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Starting duel in: "+ChatColor.GOLD+this.countDown);
                    break;
                case 5:
                case 4:
                case 3:
                case 2:
                case 1:
                    sender.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Starting duel in: "+ChatColor.GOLD+this.countDown);
                    target.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Starting duel in: "+ChatColor.GOLD+this.countDown);
                break;
                default:
                break;
            }
            this.countDown--;
        }
        else{

            plugin.frozenPlayers.clear();//let them move
            plugin.duelingPlayers.add(sender);
            plugin.duelingPlayers.add(target);
            sender.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Duel!");
            target.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Duel!");
            this.cancel();
        }
    }
            /*plugin.frozenPlayers.clear();
            plugin.duelingPlayers.add(sender.getPlayer());
            plugin.duelingPlayers.add(target.getPlayer());
            sender.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "Duel!");
            target.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Duel!");
            target.setItemInHand(new ItemStack(Material.IRON_AXE,1));
            sender.setItemInHand(new ItemStack(Material.IRON_AXE,1));
            plugin.duelStatus = "IN PROGRESS";

        }


    }*/
}
