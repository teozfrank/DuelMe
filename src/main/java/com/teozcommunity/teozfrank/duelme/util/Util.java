package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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

    public void storeInventory(Player p){
        ItemStack[] inv = p.getInventory().getContents();
        ItemStack[] arm = p.getInventory().getArmorContents();
        this.inventories.put(p.getName(), inv);
        this.armour.put(p.getName(), arm);
        p.getInventory().clear(-1,-1);
    }

    public void restoreInventory(Player p){
        p.getInventory().clear(-1,-1);
        p.getInventory().setContents(this.inventories.get(p.getName()));
        p.getInventory().setArmorContents(this.armour.get(p.getName()));
        this.inventories.remove(p.getName());
        this.armour.remove(p.getName());
    }

    public void startDuel(Player sender,Player target){
        if(!plugin.inProgress){
            plugin.duelRequests.remove(target.getName());

            String senderWorldIn = plugin.getConfig().getString("duelme.duelsenderloc.world");
            double senderxIn = plugin.getConfig().getDouble("duelme.duelsenderloc.x");
            double senderyIn = plugin.getConfig().getDouble("duelme.duelsenderloc.y");
            double senderzIn = plugin.getConfig().getDouble("duelme.duelsenderloc.z");


            String targetWorldIn = plugin.getConfig().getString("duelme.dueltargetloc.world");
            double targetxIn = plugin.getConfig().getDouble("duelme.dueltargetloc.x");
            double targetyIn = plugin.getConfig().getDouble("duelme.dueltargetloc.y");
            double targetzIn = plugin.getConfig().getDouble("duelme.dueltargetloc.z");


            World senderWorld = Bukkit.getWorld(senderWorldIn);
            World targetWorld = Bukkit.getWorld(targetWorldIn);

            Location senderLoc = new Location(senderWorld,senderxIn,senderyIn,senderzIn);
            Location targetLoc = new Location(targetWorld,targetxIn,targetyIn,targetzIn);

            sender.teleport(senderLoc);
            target.teleport(targetLoc);

            plugin.duelingPlayers.add(sender.getName());
            plugin.duelingPlayers.add(target.getName());

            plugin.util.storeInventory(sender.getPlayer());
            plugin.util.storeInventory(target.getPlayer());

            //plugin.frozenPlayers.add(sender.getName());
            //plugin.frozenPlayers.add(target.getName());

            //Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,new StartDuelThread(plugin,sender.getPlayer(),target.getPlayer()));
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
        String sender = plugin.duelRequests.get(acceptingPlayer.getName());
        Player duelSender = Bukkit.getPlayer(sender);

        if(duelSender!=null){
            this.startDuel(duelSender.getPlayer(),acceptingPlayer.getPlayer());
        }

        else {
            acceptingPlayer.sendMessage(plugin.pluginPrefix+ ChatColor.RED+"The duel sender "+ChatColor.AQUA+sender+ChatColor.RED+" has gone offline!");
            plugin.duelRequests.remove(acceptingPlayer.getName());
        }

    }

    /*
    * Method to deny duel requests
    */
    public void denyDuel(Player p){

    }

    /*
    * Method to spectate a duel in progress
    */
    public void spectateDuel(Player p){

    }

    /*
    * Method to send duel request
    */
    public void sendRequest(Player sender,Player target){

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

    /*
    * Method to leave a duel
    */
    public void leaveDuel(Player leavingPlayer){
        if(plugin.duelingPlayers.contains(leavingPlayer.getName())){
            //TODO finish off this
        }
        else {
            leavingPlayer.sendMessage(plugin.pluginPrefix+ChatColor.RED+"You cannot leave a duel if you are not in one!");
        }

    }
    /*
    * Method to leave a duel
    */
    public void endDuel(){




    }

}
