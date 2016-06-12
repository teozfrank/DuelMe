package com.github.teozfrank.duelme.commands.admin;

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

import com.github.teozfrank.duelme.mysql.FieldName;
import com.github.teozfrank.duelme.util.DuelArena;
import com.github.teozfrank.duelme.util.FileManager;
import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.mysql.MySql;
import com.github.teozfrank.duelme.util.SendConsoleMessage;
import com.github.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AddKillCmd extends DuelAdminCmd {
    public AddKillCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {
        MySql mySql = plugin.getMySql();
        FileManager fm = plugin.getFileManager();

        if(args.length < 1 ) {
            Util.sendMsg(sender, "Usage /dueladmin addkill <playername>");
            return;
        }

        if(args.length == 1 ) {
            if(!fm.isMySqlEnabled()) {
                Util.sendMsg(sender, ChatColor.RED + "MySql is NOT enabled you cannot use this command.");
                return;
            }
            String playerNameIn = args[0];
            SendConsoleMessage.debug(playerNameIn);
            Player player = plugin.getServer().getPlayerExact(playerNameIn);
            UUID playerUUID = player.getUniqueId();

            if(player != null) {
                String playerName = player.getName();
                Util.sendMsg(sender, "Adding kill for player: " + ChatColor.AQUA + playerName);
                mySql.addPlayerKillDeath(playerUUID, playerName, FieldName.KILL);
            } else {
                Util.sendMsg(sender, ChatColor.RED + "Player " + playerNameIn + " is not online, did you type the name correctly?");
            }
        }
    }
}
