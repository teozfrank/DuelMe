package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.threads.StartDuelThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
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
 * User: Frank
 * Date: 08/08/13
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    private DuelMe plugin;
    Random rand = new Random();


    public Util(DuelMe plugin){
        this.plugin = plugin;
    }

    public HashMap<String, ItemStack[]> inventories = new HashMap <String, ItemStack[]>();
    public HashMap <String, ItemStack[]> armour = new HashMap <String, ItemStack[]>();
    public HashMap <String, Integer> exp = new HashMap <String, Integer>();

    public void storeInventory(Player p){
        ItemStack[] inv = p.getInventory().getContents();
        ItemStack[] arm = p.getInventory().getArmorContents();
        this.inventories.put(p.getName(), inv);
        this.armour.put(p.getName(), arm);
        p.getInventory().clear(-1,-1);
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Your inventory has been stored and will be restored after the Duel.");
    }

    public void restoreInventory(Player p){
        p.getInventory().clear(-1,-1);
        p.getInventory().setContents(this.inventories.get(p.getName()));
        p.getInventory().setArmorContents(this.armour.get(p.getName()));
        this.inventories.remove(p.getName());
        this.armour.remove(p.getName());
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Your inventory has been restored.");

    }

    public int teleportPlayers(Player sender, Player target){

        try{
            sender.teleport(plugin.locations.senderSpawnLocation());
            target.teleport(plugin.locations.targetSpawnLocation());
            return 1;
        }
        catch(Exception e){
           sender.sendMessage("there was an error attempting to teleport to start the duel! Have the spawn locations been set? Duel cancelled!");
           target.sendMessage("there was an error attempting to teleport to start the duel! Have the spawn locations been set? Duel cancelled!");
            return -1;
        }
    }

    public void startDuel(Player sender,Player target){
        if(!plugin.inProgress){
            plugin.duelRequests.remove(target.getName());

            int success = this.teleportPlayers(sender.getPlayer(),target.getPlayer());
            if(success==1){


                plugin.inProgress = true;

                sender.setGameMode(GameMode.SURVIVAL);
                target.setGameMode(GameMode.SURVIVAL);

                this.storeInventory(sender.getPlayer());
                this.storeInventory(target.getPlayer());

                this.handleDisguise(target.getPlayer(),sender.getPlayer());

                //sender.teleport(plugin.locations.senderSpawnLocation());
                //target.teleport(plugin.locations.targetSpawnLocation());



                if(plugin.getConfig().getBoolean("duelme.announce.duelstart")){
                    this.broadcastMessage(ChatColor.AQUA+"A Duel is beginning! "+ChatColor.GOLD+sender.getName()+ChatColor.AQUA+
                            " VS "+ChatColor.GOLD+target.getName());
                }


                sender.setHealth(20);
                target.setHealth(20);
                //TODO add config file for items
                int randSender = rand.nextInt(5);
                int randTarget = rand.nextInt(5);
                target.setItemInHand(this.randomItem(randTarget));
                sender.setItemInHand(this.randomItem(randSender));

                new StartDuelThread(plugin, sender.getPlayer(), target.getPlayer()).runTaskTimer(plugin, 0L, 20L);

            }
        }
        else {
            sender.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "A duel is already in progress, please try again later");
            target.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"A duel is already in progress, please try again later");
            plugin.duelRequests.remove(target.getName());
        }
    }

    /*
    * Method to accept duel requests
    */
    public void acceptDuel(Player acceptingPlayer){
        Player aPlayer = acceptingPlayer.getPlayer();
        String aPlayerName = acceptingPlayer.getName();

        if(plugin.duelRequests.containsKey(aPlayerName)){
           String dSender = plugin.duelRequests.get(aPlayerName);
           Player dSenderPlayer = Bukkit.getPlayer(dSender);
           if(dSenderPlayer!=null){
              plugin.util.startDuel(dSenderPlayer,aPlayer);
           }
           else {
               aPlayer.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"The duel sender "+dSender+" has gone offline!");
               plugin.duelRequests.remove(aPlayerName);
           }
        }

        else {
            aPlayer.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"You do not have any pending duel requests to accept!, if you just received a request the player may have cancelled it!");
        }

    }

    /*
    * Method to deny duel requests
    */
    public void denyDuel(Player p){
       p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Will be implemented soon, for now just ignore the request you were sent.");
    }

    /*
    * Method to spectate a duel in progress
    */
    public void spectateDuel(Player p){
        if(plugin.inProgress){
            if(!plugin.duelingPlayers.contains(p.getPlayer())){
                p.teleport(plugin.locations.spectateSpawnLocation());
                plugin.spectatingPlayers.add(p.getPlayer());
                this.storeInventory(p.getPlayer());
                for(Player pl: Bukkit.getOnlinePlayers()){
                    pl.hidePlayer(p.getPlayer());//hide the player from everyone else
                }
                p.setAllowFlight(true);//let them fly
                p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Successfully teleported to duel spectate area.");
            }
            else{
                p.sendMessage(plugin.pluginPrefix+ChatColor.RED+"You cannot spectate while dueling!");
            }
        }
        else {
            p.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"You cannot spectate when a duel is not in progress!");
        }
    }

    /*
    * Method to send duel request
    */
    public void sendRequest(Player sender,Player target){

        if(sender!=target){

            if(!plugin.duelRequests.containsValue(sender.getName())){
                plugin.duelRequests.put(target.getName(),sender.getName());
                sender.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"You have sent a duel request to "+ChatColor.AQUA+target.getName());
                target.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"You have been sent a duel request from \n"+
                ChatColor.AQUA+sender.getName()+ChatColor.GREEN+" use "+ChatColor.AQUA+"/duel accept"+ChatColor.GREEN+" to accept the request.");
            }
            else {
                sender.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"You have already sent a duel request to another player!");
            }
        }
        else{
            sender.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"You cannot duel yourself!");
        }

    }

    /*
    * Method to leave a duel
    */
    public void leaveDuel(Player leavingPlayer){
        if(plugin.spectatingPlayers.contains(leavingPlayer.getPlayer())){
            plugin.spectatingPlayers.remove(leavingPlayer.getPlayer());
            this.restoreInventory(leavingPlayer.getPlayer());
            leavingPlayer.teleport(plugin.locations.lobbySpawnLocation());
            for(Player pl:Bukkit.getOnlinePlayers()){
                pl.showPlayer(leavingPlayer.getPlayer());
            }
            leavingPlayer.setAllowFlight(false);
        }
        else if(plugin.duelingPlayers.contains(leavingPlayer.getPlayer())){
            plugin.util.broadcastMessage(ChatColor.RED+leavingPlayer.getName()+ " has ended the duel by leaving!!");
            plugin.util.endDuel();
        }
        else {
            leavingPlayer.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"You cannot leave a duel if you are not in one!");
        }

    }
    /*
    * Method to end a duel
    */
    public void endDuel(){

        for(Player p: Bukkit.getOnlinePlayers()){
            if(plugin.duelingPlayers.contains(p.getPlayer())){
                if(!p.getPlayer().isDead()){
                    plugin.duelingPlayers.remove(p.getPlayer());//remove them from the dueling players
                    this.restoreInventory(p.getPlayer());//restore their inventory
                    p.setHealth(20);
                    p.teleport(plugin.locations.lobbySpawnLocation());//teleport them to lobby location
                }
            }
            if(plugin.spectatingPlayers.contains(p.getPlayer())){
                plugin.spectatingPlayers.remove(p.getPlayer());
                this.restoreInventory(p.getPlayer());
                p.teleport(plugin.locations.lobbySpawnLocation());
                for(Player pl:Bukkit.getOnlinePlayers()){
                    pl.showPlayer(p.getPlayer());
                }
                p.setHealth(20);
                p.setAllowFlight(false);
            }
            plugin.inProgress = false;
        }
    }

    public void cancelRequest(Player p){
        if(plugin.duelRequests.containsValue(p.getName())){
            for(Map.Entry<String, String > values: plugin.duelRequests.entrySet()){
                if(values.getValue().equals(p.getName())){
                    plugin.duelRequests.remove(values.getKey());
                }
            }
            p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"You have cancelled your sent duel request!");


        }
        else {
            p.sendMessage(plugin.pluginPrefix+ChatColor.RED+"You do not have any duel requests to cancel!");
        }
    }

    public void broadcastMessage(String message){
        for(Player p: Bukkit.getOnlinePlayers()){
            p.sendMessage(plugin.pluginPrefix+ message);
        }
    }
    /*
     * method to handle mob disguises if server is using it
     */

    public void handleDisguise(Player sender,Player target){
        try{
            if (plugin.getServer().getPluginManager().getPlugin("DisguiseCraft") != null) {
                DisguiseCraftAPI dcAPI = DisguiseCraft.getAPI();

                if(dcAPI.isDisguised(sender.getPlayer())){
                    dcAPI.undisguisePlayer(sender.getPlayer());
                }
                if(dcAPI.isDisguised(target.getPlayer())){
                    dcAPI.undisguisePlayer(target.getPlayer());
                }
            }

        }

        catch(Exception e){
            //server must not be using disguisecraft so we wont output an error
        }

    }

    public ItemStack randomItem(int rand){

        if(rand == 1){
          return new ItemStack(Material.WOOD_AXE,1);
        }
        else if(rand == 2){
          return new ItemStack(Material.WOOD_SWORD,1);
        }
        else if(rand == 3){
          return new ItemStack(Material.IRON_AXE,1);
        }
        else if(rand == 4){
          return new ItemStack(Material.DIAMOND_HOE,1);
        }
        else if(rand == 5){
            return new ItemStack(Material.STONE_SWORD,1);
        }
        else{
          return new ItemStack(Material.IRON_SPADE,1);
        }

    }





}
