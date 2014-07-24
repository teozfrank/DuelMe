package com.teozcommunity.teozfrank.duelme.util;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private Location spawnpoint1;
    private Location spawnpoint2;

    private List<UUID> players;
    private DuelState duelState;
    private boolean hasBet;
    private double betAmount;

    public DuelArena(String name, Location pos1, Location pos2){
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.players = new ArrayList<UUID>();
        this.duelState = DuelState.WAITING;
        this.hasBet = false;
        this.betAmount = 0;
        this.spawnpoint1 = null;
        this.spawnpoint2 = null;
    }

    public DuelArena(String name, Location pos1, Location pos2, Location spawnpoint1, Location spawnpoint2){
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.players = new ArrayList<UUID>();
        this.duelState = DuelState.WAITING;
        this.hasBet = false;
        this.betAmount = 0;
        this.spawnpoint1 = spawnpoint1;
        this.spawnpoint2 = spawnpoint2;
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

    public List<UUID> getPlayers(){
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

    public void removePlayer(UUID playerUUID){
        this.players.remove(playerUUID);
    }

    public void addPlayerUUID(UUID playerUUID){
        this.players.add(playerUUID);
    }

    public void setPlayers(List<UUID> players){
        this.players = players;
    }

    public void setDuelState(DuelState duelState){
        this.duelState = duelState;
    }

    public boolean hasBet() {
        return hasBet;
    }

    public void setHasBet(boolean hasBet) {
        this.hasBet = hasBet;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }

    public Location getSpawnpoint1() {
        return spawnpoint1;
    }

    public void setSpawnpoint1(Location spawnpoint1) {
        this.spawnpoint1 = spawnpoint1;
    }

    public Location getSpawnpoint2() {
        return spawnpoint2;
    }

    public void setSpawnpoint2(Location spawnpoint2) {
        this.spawnpoint2 = spawnpoint2;
    }
}
