package com.teozcommunity.teozfrank.duelme.commands.admin;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Frank on 18/01/2015.
 */
public class RestoreCmd extends DuelAdminCmd {


    public RestoreCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {
        DuelManager dm = plugin.getDuelManager();

        if (args.length < 1) {
            Util.sendMsg(sender, ChatColor.GREEN + "Usage: /dueladmin restore <player>");
            return;
        }

        String playerName = getValue(args, 0, "");

        Player player = Bukkit.getPlayerExact(playerName);
        if(player != null) {
            boolean success = dm.restorePlayerData(player);
            if(success) {
                Util.sendMsg(sender, ChatColor.GREEN + "Player data for " + playerName + " was successfully restored!");
            } else {
                Util.sendMsg(sender, ChatColor.RED + "There was an error trying to restore player data for player " + playerName);
            }
        } else {
            Util.sendMsg(sender, ChatColor.RED + "The player " + playerName + " is not online! did you type it correctly?");
        }
    }
}
