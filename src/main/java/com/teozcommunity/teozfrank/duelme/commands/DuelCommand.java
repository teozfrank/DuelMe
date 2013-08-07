package com.teozcommunity.teozfrank.duelme.commands;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.threads.StartDuelThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 06/08/13
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class DuelCommand implements CommandExecutor {

    private DuelMe plugin;

    public DuelCommand(DuelMe plugin){
      this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            int length = args.length;
            String version = plugin.getDescription().getVersion();

            if(length<1){
              p.sendMessage(ChatColor.GREEN + "0o--------------- " + plugin.pluginPrefix + ChatColor.GOLD + "PVP for fun" + ChatColor.GREEN + " ---------------o0\n\n" +

                      ChatColor.GREEN + "/duel <player> " + ChatColor.GOLD + "- Duel a specified player \n" +
                      ChatColor.GREEN + "/duel accept " + ChatColor.GOLD + "- Accept a duel request \n" +
                      ChatColor.GREEN + "/duel deny " + ChatColor.GOLD + "- Deny a duel request \n" +
                      ChatColor.GREEN + "/duel spectate " + ChatColor.GOLD + "- Spectate a duel in progress \n\n" +

                      ChatColor.GREEN + "0o--------------" + ChatColor.GOLD + " V" + version + " BETA by TeOzFrAnK " + ChatColor.GREEN + "-------------o0\n" +
                      ChatColor.GREEN + "0o-----" + ChatColor.GOLD + " http://dev.bukkit.org/bukkit-plugins/duelme/ " + ChatColor.GREEN + "------o0"

              );
                return true;
            }

            else if(length==1){
                if(args[0].equals("accept")){
                  this.acceptDuel(p.getPlayer());
                }
                else if(args[0].equals("deny")){
                  this.denyDuel(p.getPlayer());
                }
                else if(args[0].equals("spectate")){
                    this.spectateDuel(p.getPlayer());
                }
                else if(args[0].equals("leave")){
                    this.leaveDuel(p.getPlayer());
                }
                else {

                    Player targetPlayer = Bukkit.getPlayer(args[0]);
                    if(targetPlayer!=null){
                      this.sendRequest(p.getPlayer(),targetPlayer.getPlayer());
                    }
                    else{
                        p.sendMessage(plugin.pluginPrefix+ChatColor.RED+"Player "+ChatColor.GOLD+args[0]+ChatColor.RED+" is not online! Did you type it correctly?");
                    }

                }

                return true;
            }

            else {
                p.sendMessage(plugin.pluginPrefix+ChatColor.RED+"Unknown command!");
                return true;
            }


        }
        else {
            sender.sendMessage("Players can only execute this command!");
            return true;
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
           acceptingPlayer.sendMessage(plugin.pluginPrefix+ChatColor.RED+"The duel sender "+ChatColor.AQUA+sender+ChatColor.RED+" has gone offline!");
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

    public void startDuel(Player sender,Player target){
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

        plugin.frozenPlayers.add(sender.getName());
        plugin.frozenPlayers.add(target.getName());

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,new StartDuelThread(plugin,sender.getPlayer(),target.getPlayer()));
    }

}
