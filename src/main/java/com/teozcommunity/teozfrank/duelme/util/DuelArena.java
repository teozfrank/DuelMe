package com.teozcommunity.teozfrank.duelme.util;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: teoz
 * Date: 26/10/13
 * Time: 18:21
 * To change this template use File | Settings | File Templates.
 */
public class DuelArena {

    private String name;
    private Location pos1;
    private Location pos2;
    private List<String> players;
    private DuelState duelState;

    public DuelArena(String name, Location pos1, Location pos2, List<String> players){
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.players = players;
    }

    public DuelArena(String name, Location pos1, Location pos2){
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.players = new ArrayList<String>();
        this.duelState = DuelState.WAITING;
    }

    public String getName(){
        return name;
    }

    public Location getPos1(){
        return pos1;
    }

    public Location getPos2(){
        return pos2;
    }

    public DuelState getDuelState(){
        return duelState;
    }

    public List<String> getPlayers(){
        return players;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPos1(Location pos1){
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2){
        this.pos2 = pos2;
    }

    public void setPlayers(List<String> players){
        this.players = players;
    }

    public void setDuelState(DuelState duelState){
        this.duelState = duelState;
    }
}
