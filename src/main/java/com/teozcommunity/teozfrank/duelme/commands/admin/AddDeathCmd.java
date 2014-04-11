package com.teozcommunity.teozfrank.duelme.commands.admin;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.mysql.FieldName;
import com.teozcommunity.teozfrank.duelme.mysql.MySql;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.FileManager;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by frank on 11/04/14.
 */
public class AddDeathCmd extends DuelAdminCmd {
    public AddDeathCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {
        MySql mySql = plugin.getMySql();
        FileManager fm = plugin.getFileManager();

        if(args.length < 1 ) {
            Util.sendMsg(sender, "Usage /dueladmin addDeath <playername>");
            return;
        }

        if(args.length == 1 ) {
            if(!fm.isMySqlEnabled()) {
                Util.sendMsg(sender, ChatColor.RED + "MySql is NOT enabled you cannot use this command.");
                return;
            }
            String playerNameIn = args[0];
            Player player = plugin.getServer().getPlayer(playerNameIn);

            if(player != null) {
                String playerName = player.getName();
                Util.sendMsg(sender, "Adding death for player: " + ChatColor.AQUA + playerName);
                mySql.addPlayerKillDeath(playerName, FieldName.DEATH);
            } else {
                Util.sendMsg(sender, ChatColor.RED + "Player " + playerNameIn + " is not online, did you type the name correctly?");
            }
        }
    }
}
