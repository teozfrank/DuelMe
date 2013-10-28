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
    public List<DuelArena> arenas;

    public DuelManager(DuelMe plugin){
        this.plugin = plugin;
    }

    /**
     * gets a list of the arena objects
     * @return list of arenas
     */
    public List<DuelArena> getArenas(){
        return arenas;
    }

    /**
     * if a player is in a duel
     * @param player the players name
     * @return true if is in a duel, false if not
     */
    public boolean isInDuel(String player){
        for(DuelArena a: this.getArenas()){
            if(a.getPlayers().contains(player)){
                return true;
            }
        }
        return false;
    }






}
