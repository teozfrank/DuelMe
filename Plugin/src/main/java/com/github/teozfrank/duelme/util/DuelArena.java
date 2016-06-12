package com.github.teozfrank.duelme.util;

/**
        The MIT License (MIT)

        Copyright (c) 2014 teozfrank

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DuelArena {

    private String name;
    private Location pos1;
    private Location pos2;
    private Location spawnpoint1;
    private Location spawnpoint2;

    private List<UUID> players;
    private DuelState duelState;

    public DuelArena(String name, Location pos1, Location pos2){
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.players = new ArrayList<UUID>();
        this.duelState = DuelState.WAITING;
        this.spawnpoint1 = null;
        this.spawnpoint2 = null;
    }

    public DuelArena(String name, Location pos1, Location pos2, Location spawnpoint1, Location spawnpoint2){
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.players = new ArrayList<UUID>();
        this.duelState = DuelState.WAITING;
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
