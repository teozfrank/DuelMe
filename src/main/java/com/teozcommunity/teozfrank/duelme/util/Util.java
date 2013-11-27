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

    /**
     * hashmap to store players inventories
     */
    private static HashMap<String, ItemStack[]> inventories;

    /**
     * hashmap to store players armour
     */
    private static HashMap<String, ItemStack[]> armour;

    public static String UNKNOWN_CMD = ChatColor.RED+"Unknown Command!";
    public static String NO_PERMS = ChatColor.RED+"You do not have permission!";
    public static String NO_ARENAS = ChatColor.RED + "There are no arenas to start the duel! Please notify a member of staff!";


    public Util(DuelMe plugin) {
        this.plugin = plugin;
        this.inventories = new HashMap<String, ItemStack[]>();
        this.armour = new HashMap<String, ItemStack[]>();
        this.rand = new Random();
    }

    /**
    * Method to store a players inventory
    * @param p the player to store the inventory of
    */
    public static void storeInventory(Player p) {
        ItemStack[] inv = p.getInventory().getContents();
        ItemStack[] arm = p.getInventory().getArmorContents();
        inventories.put(p.getName(), inv);
        armour.put(p.getName(), arm);
        p.getInventory().clear(-1, -1);
        Util.sendMsg(p,ChatColor.GREEN + "Your inventory has been stored and will be restored after the Duel.");
    }

    /**
    * Method to restore a players inventory
    * @param p the player to restore the inventory to
    */
    public static void restoreInventory(Player p) {
        p.getInventory().clear(-1, -1);// clear their inventory completely
        if (inventories.containsKey(p.getName()) && armour.containsKey(p.getName())) {
            p.getInventory().setContents(inventories.get(p.getName()));
            p.getInventory().setArmorContents(armour.get(p.getName()));
            inventories.remove(p.getName());
            armour.remove(p.getName());
            Util.sendMsg(p,ChatColor.GREEN + "Your inventory has been restored.");
        } else {
            Util.sendMsg(p,ChatColor.RED + "There was an error restoring your inventory!");
        }
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

    public static void sendMsg(Player player, String message){
        player.sendMessage(ChatColor.GOLD+"[DuelMe] " + message);
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