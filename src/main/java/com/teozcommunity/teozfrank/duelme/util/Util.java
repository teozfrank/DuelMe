package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.threads.StartDuelThread;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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
    private HashMap<String, ItemStack[]> inventories;

    /**
     * hashmap to store players armour
     */
    private HashMap<String, ItemStack[]> armour;


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
    public void storeInventory(Player p) {
        ItemStack[] inv = p.getInventory().getContents();
        ItemStack[] arm = p.getInventory().getArmorContents();
        this.inventories.put(p.getName(), inv);
        this.armour.put(p.getName(), arm);
        p.getInventory().clear(-1, -1);
        p.sendMessage(plugin.pluginPrefix + ChatColor.GREEN + "Your inventory has been stored and will be restored after the Duel.");
    }

    /**
    * Method to restore a players inventory
    * @param p the player to restore the inventory to
    */
    public void restoreInventory(Player p) {
        p.getInventory().clear(-1, -1);// clear their inventory completely
        if (this.inventories.containsKey(p.getName()) && this.armour.containsKey(p.getName())) {
            p.getInventory().setContents(this.inventories.get(p.getName()));
            p.getInventory().setArmorContents(this.armour.get(p.getName()));
            this.inventories.remove(p.getName());
            this.armour.remove(p.getName());
            p.sendMessage(plugin.pluginPrefix + ChatColor.GREEN + "Your inventory has been restored.");
        } else {
            p.sendMessage(plugin.pluginPrefix + ChatColor.RED + "There was an error restoring your inventory!");
        }
    }

    /**
    * Method to teleport dueling players to their spawn location
    * @param sender the duel requester
    * @param target the duel recipient
    */
    public int teleportPlayers(Player sender, Player target) {

        try {
            sender.teleport(plugin.locations.senderSpawnLocation());
            target.teleport(plugin.locations.targetSpawnLocation());
            return 1;
        } catch (Exception e) {
            sender.sendMessage("there was an error attempting to teleport to start the duel! Have the spawn locations been set? Duel cancelled!");
            target.sendMessage("there was an error attempting to teleport to start the duel! Have the spawn locations been set? Duel cancelled!");
            return -1;
        }
    }

    /**
    * Method to start duel between two players
    * @param sender the duel requester
    * @param target the duel recipient
    */
    public void startDuel(Player sender, Player target) {
        if (!plugin.inProgress) {
            plugin.duelRequests.remove(target.getName());

            int success = this.teleportPlayers(sender.getPlayer(), target.getPlayer());
            if (success == 1) {


                plugin.inProgress = true;
                System.out.print(sender.getLocation());
                System.out.print(target.getLocation());

                sender.setGameMode(GameMode.SURVIVAL);
                target.setGameMode(GameMode.SURVIVAL);

                if(plugin.seperateInventories){
                    this.storeInventory(sender.getPlayer());
                    this.storeInventory(target.getPlayer());
                }

                this.handleDisguise(target.getPlayer(), sender.getPlayer());

                //sender.teleport(plugin.locations.senderSpawnLocation());
                //target.teleport(plugin.locations.targetSpawnLocation());

                if (plugin.getConfig().getBoolean("duelme.announce.duelstart")) {
                    this.broadcastMessage(ChatColor.AQUA + "A Duel is beginning! " + ChatColor.GOLD + sender.getName() + ChatColor.AQUA +
                            " VS " + ChatColor.GOLD + target.getName());
                }
                sender.setHealth(20);
                target.setHealth(20);
                //TODO add config file for items
                int randSender = rand.nextInt(5);
                int randTarget = rand.nextInt(5);
                target.setItemInHand(this.randomItem(randTarget));
                sender.setItemInHand(this.randomItem(randSender));

                new StartDuelThread(plugin, sender.getPlayer(), target.getPlayer()).runTaskTimer(plugin, 0L, 20L);//start our countdown thread
            }
        } else {
            sender.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "A duel is already in progress, please try again later");
            target.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "A duel is already in progress, please try again later");
            plugin.duelRequests.remove(target.getName());//remove the players from the duel requests so they can duel again
        }
    }

    /**
    * Method to accept duel requests
    * @param acceptingPlayer player accepting the duel
    */
    public void acceptDuel(Player acceptingPlayer) {
        Player aPlayer = acceptingPlayer.getPlayer();
        String aPlayerName = acceptingPlayer.getName();

        if (plugin.duelRequests.containsKey(aPlayerName)) {
            String dSender = plugin.duelRequests.get(aPlayerName);
            Player dSenderPlayer = Bukkit.getPlayer(dSender);
            if (dSenderPlayer != null) {
                plugin.util.startDuel(dSenderPlayer, aPlayer);
            } else {
                aPlayer.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "The duel sender " + dSender + " has gone offline!");
                plugin.duelRequests.remove(aPlayerName);
            }
        } else {
            aPlayer.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "You do not have any pending duel requests to accept!, if you just received a request the player may have cancelled it!");
        }

    }

    /**
    * Method to deny duel requests
    * @param p duel denying player
    */
    public void denyDuel(Player p) {
        p.sendMessage(plugin.pluginPrefix + ChatColor.GREEN + "Will be implemented soon, for now just ignore the request you were sent.");
    }

    /**
    * Method to spectate a duel in progress
    * @param p Spectating player
    */
    public void spectateDuel(Player p) {
        if (plugin.inProgress) {
            if(plugin.frozenPlayers.contains(p)){
                p.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "You cannot spectate while in a duel!");
                return;
            } else if (!plugin.duelingPlayers.contains(p.getPlayer())) {
                p.teleport(plugin.locations.spectateSpawnLocation());
                plugin.spectatingPlayers.add(p.getPlayer());
                this.storeInventory(p.getPlayer());
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    pl.hidePlayer(p.getPlayer());//hide the player from everyone else
                }
                p.setAllowFlight(true);//let them fly
                p.sendMessage(plugin.pluginPrefix + ChatColor.GREEN + "Successfully teleported to duel spectate area.");
            } else {
                p.sendMessage(plugin.pluginPrefix + ChatColor.RED + "You cannot spectate while dueling!");
            }
        } else {
            p.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "You cannot spectate when a duel is not in progress!");
        }
    }

    /**
    * Method to send duel request
    * @param sender the request sender
    * @param target the request recipient
    */
    public void sendRequest(Player sender, Player target) {

        if (sender != target) {

            if (!plugin.duelRequests.containsValue(sender.getName())) {
                plugin.duelRequests.put(target.getName(), sender.getName());
                sender.sendMessage(plugin.pluginPrefix + ChatColor.GREEN + "You have sent a duel request to " + ChatColor.AQUA + target.getName());
                target.sendMessage(plugin.pluginPrefix + ChatColor.GREEN + "You have been sent a duel request from \n" +
                        ChatColor.AQUA + sender.getName() + ChatColor.GREEN + " use " + ChatColor.AQUA + "/duel accept" + ChatColor.GREEN + " to accept the request.");
            } else {
                sender.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "You have already sent a duel request to another player!");
            }
        } else {
            sender.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "You cannot duel yourself!");
        }

    }

    /**
    * Method to leave a duel
    * @param leavingPlayer the leaving player
    */
    public void leaveDuel(Player leavingPlayer) {
        if (plugin.spectatingPlayers.contains(leavingPlayer.getPlayer())) {
            plugin.spectatingPlayers.remove(leavingPlayer.getPlayer());
            this.restoreInventory(leavingPlayer.getPlayer());
            leavingPlayer.teleport(plugin.locations.lobbySpawnLocation());
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.showPlayer(leavingPlayer.getPlayer());
            }
            leavingPlayer.setAllowFlight(false);
        } else if (plugin.duelingPlayers.contains(leavingPlayer.getPlayer())) {
            plugin.util.broadcastMessage(ChatColor.RED + leavingPlayer.getName() + " has ended the duel by leaving!!");
            plugin.util.endDuel();
        } else {
            leavingPlayer.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "You cannot leave a duel if you are not in one!");
        }

    }

    /**
    * Method to end a duel
    */
    public void endDuel() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (plugin.duelingPlayers.contains(p.getPlayer())) {
                if (!p.getPlayer().isDead()) {
                    plugin.duelingPlayers.remove(p.getPlayer());//remove them from the dueling players
                    if(plugin.seperateInventories){
                        this.restoreInventory(p.getPlayer());//restore their inventory
                    }
                    p.setHealth(20);
                    p.teleport(plugin.locations.lobbySpawnLocation());//teleport them to lobby location
                }
            }
            if (plugin.spectatingPlayers.contains(p.getPlayer())) {
                plugin.spectatingPlayers.remove(p.getPlayer());
                this.restoreInventory(p.getPlayer());
                p.teleport(plugin.locations.lobbySpawnLocation());
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    pl.showPlayer(p.getPlayer());
                }
                p.setHealth(20);
                p.setAllowFlight(false);
            }
            plugin.inProgress = false;
        }
    }

    /**
    * Method to cancel a duel request
    * @param p Player that cancels the request
    */
    public void cancelRequest(Player p) {
        if (plugin.duelRequests.containsValue(p.getName())) {
            for (Map.Entry<String, String> values : plugin.duelRequests.entrySet()) {
                if (values.getValue().equals(p.getName())) {
                    plugin.duelRequests.remove(values.getKey());
                }
            }
            p.sendMessage(plugin.pluginPrefix + ChatColor.GREEN + "You have cancelled your sent duel request!");
        } else {
            p.sendMessage(plugin.pluginPrefix + ChatColor.RED + "You do not have any duel requests to cancel!");
        }
    }

    /**
    * Method to broadcast a plugin message to all online players
    * @param message message to send to all players
    */
    public void broadcastMessage(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(plugin.pluginPrefix + message);
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

                if (dcAPI.isDisguised(sender.getPlayer())) {
                    dcAPI.undisguisePlayer(sender.getPlayer());
                }
                if (dcAPI.isDisguised(target.getPlayer())) {
                    dcAPI.undisguisePlayer(target.getPlayer());
                }
            }

        } catch (Exception e) {
            //server must not be using disguisecraft so we wont output an error
        }
    }

    /**
    * Method to return a random itemstack when a duel begins, this will be removed soon
    * @return a random ItemStack depending on the number passed in
    * @param rand random integer passed in ranges from 1 = 5
    */
    public ItemStack randomItem(int rand) {

        if (rand == 1) {
            return new ItemStack(Material.WOOD_AXE, 1);
        } else if (rand == 2) {
            return new ItemStack(Material.WOOD_SWORD, 1);
        } else if (rand == 3) {
            return new ItemStack(Material.IRON_AXE, 1);
        } else if (rand == 4) {
            return new ItemStack(Material.DIAMOND_HOE, 1);
        } else if (rand == 5) {
            return new ItemStack(Material.STONE_SWORD, 1);
        } else {
            return new ItemStack(Material.IRON_SPADE, 1);
        }

    }
    /**
     * Method to update a sign in a given location
     * @param world Signs world
     * @param signLoc Signs Location in the world
     * @param line1 sets Signs first line
     * @param line2 sets Signs second line
     * @param line3 sets Signs third line
     */
    public void updateSign(String world,Location signLoc,String line1,String line2,String line3){

        Block block = Bukkit.getWorld(world).getBlockAt(signLoc);

        try{
            if(block.getType() == Material.SIGN || block.getType() == Material.WALL_SIGN){
               //TODO finish adding code to get sign and set its contents
            }

        }
        catch(Exception e){

        }

    }
}