package com.teozcommunity.teozfrank.duelme.commands.admin;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Frank on 27/11/2015.
 */
public class ForceDuel extends DuelAdminCmd {


    public ForceDuel(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {
        String player1Name = getValue(args, 0, "");
        String player2Name = getValue(args, 1, "");

        if(args.length == 0) {
            Util.sendMsg(sender, ChatColor.GOLD + "Usage: /dueladmin forceduel <player1> <player2>");
            return;
        }

        if(args.length != 2) {
            Util.sendMsg(sender, ChatColor.RED + "You must enter a name for each player!");
            return;
        }

        Player player1 = plugin.getServer().getPlayer(player1Name);
        if(player1 == null) {
            Util.sendMsg(sender, "" + ChatColor.AQUA + player1Name + ChatColor.RED + " is not online!");
            return;
        }

        Player player2 = plugin.getServer().getPlayer(player2Name);

        if(player2 == null) {
            Util.sendMsg(sender, "" + ChatColor.AQUA + player2Name + ChatColor.RED + " is not online!");
            return;
        }

        Util.sendMsg(sender, ChatColor.GOLD + "Attempting to force a duel between " + player1Name + " and " + player2Name);
        plugin.getDuelManager().startDuel(player1, player2, 0);
    }
}
