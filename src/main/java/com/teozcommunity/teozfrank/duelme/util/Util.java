package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.threads.StartDuelThread;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 08/08/13
 * Time: 15:51
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class Util {

    /**
     * duelme plugin
     */
    private DuelMe plugin;

    /**
     * random class
     */
    Random rand;

    public static String UNKNOWN_CMD = ChatColor.RED+"Unknown Command!";
    public static String NO_PERMS = ChatColor.RED+"You do not have permission!";
    public static String NO_ARENAS = ChatColor.RED + "There are no arenas to start the duel! Please notify a member of staff!";
    public static final String LINE_BREAK = ChatColor.LIGHT_PURPLE + "=====================================================";


    public Util(DuelMe plugin) {
        this.plugin = plugin;
        this.rand = new Random();
    }


    /**
    * Method to broadcast a plugin message to all online players
    * @param message message to send to all players
    */
    public void broadcastMessage(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Util.sendMsg(p, message);
        }
    }

    /**
     * method to handle mob disguises if server is using it
     * @param sender the duel sender
     * @param target the duel acceptor
     */
    public void handleDisguise(Player sender, Player target) {
        try {
            if (plugin.getServer().getPluginManager().getPlugin("DisguiseCraft") != null) {
                DisguiseCraftAPI dcAPI = DisguiseCraft.getAPI();

                if (dcAPI.isDisguised(sender)) {
                    dcAPI.undisguisePlayer(sender);
                }
                if (dcAPI.isDisguised(target)) {
                    dcAPI.undisguisePlayer(target);
                }
            }

        } catch (Exception e) {
            //server must not be using disguisecraft so we wont output an error
        }
    }

    /**
     * sends a message to the recipient with the plugin prefix
     * @param sender the recipient to send the message to
     * @param message the message to send the sender
     */
    public static void sendMsg(CommandSender sender, String message) {
        if (sender == null) {
            return;
        }

        Player p = null;

        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (p == null) {
            Bukkit.getLogger().info(ChatColor.GOLD+"[DuelMe] " + ChatColor.stripColor(message));
        } else {
            p.sendMessage(ChatColor.GOLD+"[DuelMe] " + message);
        }
    }

    /**
     * sends plugin credits to sender of command
     * @param sender the command sender
     */
    public static void sendCredits(CommandSender sender){
        Util.sendEmptyMsg(sender,ChatColor.GOLD + "                             V"+DuelMe.getVersion()+" by TeOzFrAnK ");
        Util.sendEmptyMsg(sender,ChatColor.GOLD + "             http://dev.bukkit.org/bukkit-plugins/duelme/");
    }

    /**
     * send a plugin message to a player
     * @param player the player to send the message to
     * @param message the message to send the player
     */
    public static void sendMsg(Player player, String message){
        player.sendMessage(ChatColor.GOLD+"[DuelMe] " + message);
    }

    /**
     * send the same message to two players
     * @param sender the duel sender
     * @param target the duel acceptor
     * @param message the message to send to the two players
     */
    public static void sendMsg(Player sender, Player target, String message){
        sender.sendMessage(ChatColor.GOLD+"[DuelMe] " + message);
        target.sendMessage(ChatColor.GOLD+"[DuelMe] " + message);
    }

    /**
     * sends a empty message to the recipient with the plugin prefix
     * @param sender the recipient to send the message to
     * @param message the message to send the sender
     */
    public static void sendEmptyMsg(CommandSender sender, String message) {
        if (sender == null) {
            return;
        }

        Player p = null;

        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (p == null) {
            Bukkit.getLogger().info(ChatColor.stripColor(message));
        } else {
            p.sendMessage(message);
        }
    }




}