package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
            Util.sendMsg(duelSender,ChatColor.YELLOW+"You have already sent a request to "+duelTargetIn);
            return;
        }

        Player duelTarget = Bukkit.getPlayer(duelTargetIn);

        if(duelTarget != null){

            String duelTargetName = duelTarget.getName();
            if(duelSenderName == duelTargetName){
                Util.sendMsg(duelSender,ChatColor.RED+"You cannot duel yourself!");
                return;
            }

            Util.sendMsg(duelSender,"You have sent a duel request to "+duelTargetName);
            Util.sendMsg(duelTarget,"You have been sent a duel request from "+duelSenderName +
            "use /duel accept "+duelSenderName+" ,to accept.");
            this.duelRequests.put(duelSenderName,duelTargetName);
        } else {
            Util.sendMsg(duelSender, ChatColor.RED+ duelTargetIn +"is not online!");
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
            this.startDuel(accepter,sender);
          } else {
            Util.sendMsg(accepter,ChatColor.YELLOW+"Duel sender "+ senderIn +" has gone offline!, duel cancelled!");
          }
          this.duelRequests.remove(senderIn);
          return;
        } else {
            Util.sendMsg(accepter,ChatColor.RED+"You do not have any duel requests to accept!");
            return;
        }

    }

    /**
     * attempt to start the duel with the two players
     * @param accepter the player that accepted the request
     * @param sender the player that sent the reqest
     */
    public void startDuel(Player accepter, Player sender) {

        for(DuelArena a: this.getDuelArenas()){
            if(a.getDuelState() == DuelState.WAITING){
              //TODO teleport the players to an arena, start the countdown, give items....
            } else {
                Util.sendMsg(accepter,ChatColor.YELLOW+"There are no free duel arenas, please try again layer!");
                Util.sendMsg(sender,ChatColor.YELLOW+"There are no free duel arenas, please try again layer!");
                return;
            }
        }

    }
}
