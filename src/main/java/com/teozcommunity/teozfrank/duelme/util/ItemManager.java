package com.teozcommunity.teozfrank.duelme.util;


import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Created by frank on 11/01/14.
 */
public class ItemManager {

    private DuelMe plugin;

    public ItemManager(DuelMe plugin){
       this.plugin = plugin;
    }

    /**
     * performs the initial commands for what items players
     * get when a duel starts
     * @param player the player
     */
    public void givePlayerDuelItems(Player player){
        FileManager fm = plugin.getFileManager();
        List<String> commands = fm.getDuelStartCommands();
        for(String commandIn : commands){
            commandIn = commandIn.replaceAll("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),commandIn);
        }
    }

    /**
     * performs the winning player commands
     * @param player the player
     */
    public void giveWinningPlayerRewards(Player player){
        FileManager fm = plugin.getFileManager();
        List<String> commands = fm.getDuelWinnerCommands();
        for(String commandIn : commands){
            commandIn = commandIn.replaceAll("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),commandIn);
        }
    }

    /**
     * reward a player
     * @param arena the arena which the player is in
     */
    public void rewardPlayer(DuelArena arena) {
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if(arena.getPlayers().size() == 1){ //if there is only one player left in the arena
        if(plugin.isDebugEnabled()){
            SendConsoleMessage.debug("1 player left, now rewarding.");
        }
            for(UUID playerIn: arena.getPlayers()){
                Player winningPlayer = Bukkit.getPlayer(playerIn);
                if(winningPlayer != null){
                    dm.restorePlayerData(winningPlayer);
                    this.giveWinningPlayerRewards(winningPlayer);//give them a reward
                }
            }
            arena.getPlayers().clear();
            arena.setDuelState(DuelState.WAITING);
        }

    }

}
