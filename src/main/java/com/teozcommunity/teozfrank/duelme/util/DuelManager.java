package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: teoz
 * Date: 26/10/13
 * Time: 18:18
 * To change this template use File | Settings | File Templates.
 */
public class DuelManager {

    private DuelMe plugin;

    /**
     * map to keep track of the dueling requests
     * the key is the duel sender
     * the value is the player who has been sent a request
     */
    public Map<String, String> duelRequests;

    /**
     * list to hold the current spectating players
     */
    public List<String> spectatingPlayers;

    /**
     * list to hold the frozen players (before a duel starts)
     */
    public List<String> frozenPlayers;

    /**
     * list to hold arena objects
     */
    public List<DuelArena> duelArenas;

    public DuelManager(DuelMe plugin){
        this.plugin = plugin;
        this.duelRequests = new HashMap<String, String>();
        this.spectatingPlayers = new ArrayList<String>();
        this.frozenPlayers = new ArrayList<String>();
        this.duelArenas = new ArrayList<DuelArena>();
    }

    /**
     * gets a list of the arena objects
     * @return list of arenas
     */
    public List<DuelArena> getDuelArenas(){
        return duelArenas;
    }

    /**
     * if a player is in a duel
     * @param playerName the players name
     * @return true if is in a duel, false if not
     */
    public boolean isInDuel(String playerName){
        for(DuelArena a: this.getDuelArenas()){
            if(a.getPlayers().contains(playerName)){
                return true;
            }
        }
        return false;
    }

    /**
     * get the arena name that a player is in
     * @param playerName the players name
     * @return the arena name that the plater is in,
     * returns null if the player is not in an arena
     */
    public String getPlayersArenaName(String playerName){
        for(DuelArena a: this.getDuelArenas()){
            if(a.getPlayers().contains(playerName)){
                return a.getName();
            }
        }
        return null;
    }

    /**
     * gets the arena of two players
     * @param player1 the first player
     * @param player2 the second player
     * @return the arena that the players are in
     * , null if both players are not in the same arena.
     */
    public DuelArena getPlayersArena(String player1,String player2) {

        for(DuelArena a: this.getDuelArenas()){
            List<String> players = a.getPlayers();
            if(players.contains(player1) && players.contains(player2)){
                return a;
            }
        }
        return null;
    }

    public boolean isFrozen(String playerIn){
        if(this.getFrozenPlayers().contains(playerIn)){
            return true;
        }
        return false;
    }

    public List<String> getFrozenPlayers(){
        return this.frozenPlayers;
    }

    /**
     * gets the arena of a player
     * @param player the players name
     * @return the arena of the player, null if the player
     * is not in a arena
     */
    public DuelArena getPlayersArena(String player) {

        for(DuelArena a: this.getDuelArenas()){
            List<String> players = a.getPlayers();
            if(players.contains(player)){
                return a;
            }
        }
        return null;
    }

    /**
     * handle duel requests
     * @param duelSender the sender of the request
     * @param duelTargetIn the string player of the target player
     */
    public void sendRequest(Player duelSender,String duelTargetIn){

        String duelSenderName = duelSender.getName();

        if(this.duelRequests.containsKey(duelSenderName) && this.duelRequests.containsValue(duelTargetIn)){
            Util.sendMsg(duelSender,ChatColor.YELLOW+"You have already sent a request to " +
                    ChatColor.AQUA + duelTargetIn + ".");
            return;
        }

        Player duelTarget = Bukkit.getPlayer(duelTargetIn);

        if(duelTarget != null){

            String duelTargetName = duelTarget.getName();
            if(duelSenderName == duelTargetName){
                Util.sendMsg(duelSender,ChatColor.RED+"You cannot duel yourself!");
                return;
            }

            Util.sendMsg(duelSender,ChatColor.GREEN + "You have sent a duel request to " + ChatColor.AQUA + duelTargetName + ".");
            Util.sendMsg(duelTarget,ChatColor.translateAlternateColorCodes('&',"&aYou have been sent a duel request from &b" + duelSenderName));
            Util.sendEmptyMsg(duelTarget,ChatColor.translateAlternateColorCodes('&',"&ause &b/duel accept "+duelSenderName+"&a, to accept the request."));
            this.duelRequests.put(duelSenderName,duelTargetName);
        } else {
            Util.sendMsg(duelSender, ChatColor.AQUA+ duelTargetIn + ChatColor.RED + " is not online! Did you type it correctly?");
        }

    }

    /**
     * handles accepting the request with the specified player to accept the duel request
     * @param accepter the player that is accepting the request
     * @param senderIn the string player of whom they are accepting
     */
    public void acceptRequest(Player accepter,String senderIn){

        if(this.duelRequests.containsKey(senderIn) && this.duelRequests.containsValue(accepter.getName())){
          Player sender = Bukkit.getPlayer(senderIn);
          if(sender != null){
            this.duelRequests.remove(senderIn);
            plugin.getConsoleMessageSender().info("starting duel with " + accepter + " and " + sender );
            this.startDuel(accepter,sender);
            return;
          } else {
            Util.sendMsg(accepter,ChatColor.YELLOW+"Duel sender "+ senderIn +" has gone offline!, duel cancelled!");
          }
          this.duelRequests.remove(senderIn);
          return;
        } else {
            Util.sendMsg(accepter,ChatColor.RED+"You do not have any duel requests from "+ ChatColor.AQUA + senderIn +".");
            return;
        }

    }

    /**
     * attempt to start the duel with the two players
     * @param accepter the player that accepted the request
     * @param sender the player that sent the reqest
     */
    public void startDuel(Player accepter, Player sender) {
        String accepterName = accepter.getName();
        String senderName = sender.getName();
        List<DuelArena> arenas = this.getDuelArenas();
        FileManager fm = plugin.getFileManager();

       if(arenas.size() <= 0){
         Util.sendMsg(sender , Util.NO_ARENAS);
         Util.sendMsg(accepter , Util.NO_ARENAS);
         return;
       }
        for(DuelArena a: arenas){
            if(a.getDuelState() == DuelState.WAITING){
              a.setDuelState(DuelState.STARTING);//set the duel state to starting

              a.addPlayer(accepterName);//add the players to the arena
              a.addPlayer(senderName);

              if(fm.isUsingSeperateInventories()){//store the players inventories
                  Util.storeInventory(accepter);
                  Util.storeInventory(sender);
              }

              accepter.teleport(this.generateRandomLocation(a));//teleport the players to a random location in the duel arena
              sender.teleport(this.generateRandomLocation(a));

              frozenPlayers.add(accepter.getName());//freeze the players
              frozenPlayers.add(sender.getName());

              fm.giveDuelItems(accepter);//give them items
              fm.giveDuelItems(sender);

              //TODO start the count down thread

            } else {
                Util.sendMsg(accepter,ChatColor.YELLOW+"There are no free duel arenas, please try again later!");
                Util.sendMsg(sender,ChatColor.YELLOW+"There are no free duel arenas, please try again later!");
                return;
            }
        }

    }

    /**
     * Generates a random point between two other points.
     *
     * @param arg0 Point 1.
     * @param arg1 Point 2.
     * @return A random point.
     */
    private double randomGenRange(double arg0, double arg1) {
        double range = (arg0 < arg1) ? arg1 - arg0 : arg0 - arg1;
        if (range < 1)
            return Math.floor(arg0) + 0.5d;
        double min = (arg0 < arg1) ? arg0 : arg1;
        return Math.floor(min + (Math.random() * range)) + 0.5d;
    }

    /**
     * Generates a random location in a duelarena
     *
     * @param a The arena.
     * @return Random location.
     */
    private Location generateRandomLocation(DuelArena a) {
        double x, y, z;
        World w = a.getPos1().getWorld();
        x = randomGenRange(a.getPos1().getX(), a.getPos2().getX());
        y = randomGenRange(a.getPos1().getY(), a.getPos2().getY());
        z = randomGenRange(a.getPos1().getZ(), a.getPos2().getZ());

        return new Location(w, x, y + 0.5, z);
    }
}
