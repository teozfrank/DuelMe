package com.teozcommunity.teozfrank.duelme.util;

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

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

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
        MessageManager mm = plugin.getMessageManager();

        if(arena.getPlayers().size() == 1){ //if there is only one player left in the arena
        if(plugin.isDebugEnabled()){
            SendConsoleMessage.debug("1 player left, now rewarding.");
        }
            for(UUID playerIn: arena.getPlayers()){
                Player winningPlayer = Bukkit.getPlayer(playerIn);
                String winningPlayerName = winningPlayer.getName();
                if(winningPlayer != null){
                    dm.restorePlayerData(winningPlayer);
                    this.giveWinningPlayerRewards(winningPlayer);//give them a reward
                    Util.sendMsg(winningPlayer, mm.getDuelRewardMessage());
                }
            }

            dm.resetArena(arena);
        }

    }

}
