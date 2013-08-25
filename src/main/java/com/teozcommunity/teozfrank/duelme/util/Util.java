package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.threads.StartDuelThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 08/08/13
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    private DuelMe plugin;

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
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+" your inventory has been stored and will be restored after the Duel.");
    }

    public void restoreInventory(Player p){
        p.getInventory().clear(-1,-1);
        p.getInventory().setContents(this.inventories.get(p.getName()));
        p.getInventory().setArmorContents(this.armour.get(p.getName()));
        this.inventories.remove(p.getName());
        this.armour.remove(p.getName());
        p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+" your inventory has been restored.");

    }

    public void storeExpLevel(Player p){
        int level = p.getLevel();
        this.exp.put(p.getName(),level);
    }

    public void restoreExpLevel(Player p){
        if(this.exp.containsKey(p.getName())){
            int level = this.exp.get(p.getName());
            p.setLevel(level);
        }
        else{
            p.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"there was an error trying to restore your exp level!");
        }
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

                //plugin.frozenPlayers.add(sender.getPlayer());
                //plugin.frozenPlayers.add(target.getPlayer());
                sender.setGameMode(GameMode.SURVIVAL);
                target.setGameMode(GameMode.SURVIVAL);

                this.storeInventory(sender.getPlayer());
                this.storeInventory(target.getPlayer());

                this.storeExpLevel(sender.getPlayer());
                this.storeExpLevel(target.getPlayer());

                sender.teleport(plugin.locations.senderSpawnLocation());
                target.teleport(plugin.locations.targetSpawnLocation());



                //plugin.frozenPlayers.clear();
                plugin.duelingPlayers.add(sender.getPlayer());
                plugin.duelingPlayers.add(target.getPlayer());
                sender.sendMessage(plugin.pluginPrefix + ChatColor.YELLOW + "Duel!");
                target.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"Duel!");
                target.setItemInHand(new ItemStack(Material.IRON_AXE,1));
                sender.setItemInHand(new ItemStack(Material.IRON_AXE,1));

                //Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,new StartDuelThread(plugin,sender.getPlayer(),target.getPlayer()));
            }
        }
        else {
            sender.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"A duel is already in progress, please try again later");
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
            aPlayer.sendMessage(plugin.pluginPrefix+ChatColor.YELLOW+"You do not have any pending duel requests to accept!");
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
            p.teleport(plugin.locations.spectateSpawnLocation());
            plugin.spectatingPlayers.add(p.getPlayer());
            this.storeInventory(p.getPlayer());
            for(Player pl: Bukkit.getOnlinePlayers()){
                pl.hidePlayer(p.getPlayer());//hide the player from everyone else
            }
            p.setAllowFlight(true);//let them fly
            p.sendMessage(plugin.pluginPrefix+ChatColor.GREEN+"Successfully teleported to duel spectate area.");
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
                    p.teleport(plugin.locations.lobbySpawnLocation());//teleport them to lobby location
                    this.restoreExpLevel(p.getPlayer());//restore their exp level
                }
            }
            if(plugin.spectatingPlayers.contains(p.getPlayer())){
                plugin.spectatingPlayers.remove(p.getPlayer());
                this.restoreInventory(p.getPlayer());
                p.teleport(plugin.locations.lobbySpawnLocation());
                for(Player pl:Bukkit.getOnlinePlayers()){
                    pl.showPlayer(p.getPlayer());
                }
                p.setAllowFlight(false);
            }
            plugin.inProgress = false;
        }
    }

    public void cancelRequest(Player p){
        if(plugin.duelRequests.containsKey(p.getName())){
            plugin.duelRequests.remove(p.getName());
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





}
