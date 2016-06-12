package com.github.teozfrank.duelme.commands.duel;

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

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.DuelManager;
import com.github.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
        UUID playerUUID = player.getUniqueId();
        DuelManager dm = plugin.getDuelManager();

        if(dm.isInDuel(playerUUID)){
            dm.endDuel(player);
        } else {
            Util.sendMsg(sender, ChatColor.RED + "You cannot leave duel if you are not in one!");
        }
    }
}
