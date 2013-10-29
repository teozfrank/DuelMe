package com.teozcommunity.teozfrank.duelme.util;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * hashmap to keep track of the dueling requests
     */
    public HashMap<String, String> duelRequests;

    /**
     * list to hold the current dueling players
     */
    public List<String> duelingPlayers;

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
     * adds players to a duel arena
     * @param player1 the first player
     * @param player2 the second player
     */
    public void addPlayersToArena(String player1,String player2){
        for(DuelArena a: this.getDuelArenas()){
            if(!a.hasStarted()){

            }
        }

    }
}
