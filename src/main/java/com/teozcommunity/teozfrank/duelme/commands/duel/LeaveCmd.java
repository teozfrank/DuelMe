package com.teozcommunity.teozfrank.duelme.commands.duel;

import com.teozcommunity.teozfrank.duelme.commands.duel.DuelCmd;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by frank on 11/01/14.
 */
public class LeaveCmd extends DuelCmd {
    public LeaveCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(CommandSender sender, String subCmd, String[] args) {
        if(!(sender instanceof Player)){
            Util.sendMsg(sender, NO_CONSOLE);
            return;
        }

        Player player = (Player) sender;
        String playerName = player.getName();
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();
        ItemManager im = plugin.getItemManager();

        if(dm.isInDuel(playerName)){


            DuelArena arena = dm.getPlayersArena(playerName);
            arena.removePlayer(playerName);
            player.teleport(fm.getLobbySpawnLocation());
            if(plugin.isUsingSeperatedInventories()) {
                dm.restoreInventory(player);
            }

            if(arena.getPlayers().size() == 1){
                im.rewardPlayer(arena);
            }
            arena.getPlayers().clear();
            arena.setDuelState(DuelState.WAITING);
        } else {
            Util.sendMsg(sender, ChatColor.RED + "You cannot leave duel if you are not in one!");
        }
    }
}
