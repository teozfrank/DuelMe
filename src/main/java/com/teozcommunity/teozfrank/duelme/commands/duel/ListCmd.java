package com.teozcommunity.teozfrank.duelme.commands.duel;

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
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ListCmd extends DuelCmd {
    public ListCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(CommandSender sender, String subCmd, String[] args) {

        DuelManager dm = plugin.getDuelManager();

        Util.sendEmptyMsg(sender, Util.LINE_BREAK);
        Util.sendEmptyMsg(sender, ChatColor.GOLD + "                   DuelMe - List Duel Arena status(es)");
        Util.sendEmptyMsg(sender, "");
        if(dm.getDuelArenas().size() > 0) {
            for(DuelArena duelArena: dm.getDuelArenas()) {
                Util.sendEmptyMsg(sender, ChatColor.GREEN + "Name: " + ChatColor.AQUA + duelArena.getName());
                Util.sendEmptyMsg(sender, ChatColor.GREEN + "Status: " + ChatColor.AQUA + duelArena.getDuelState());
                Util.sendEmptyMsg(sender, ChatColor.GREEN + "Has Stake: " + ChatColor.AQUA + duelArena.hasBet());
                if(duelArena.hasBet()) {
                    Util.sendEmptyMsg(sender, ChatColor.GREEN + "Stake Amount: " + ChatColor.AQUA + duelArena.getBetAmount());
                }
                Util.sendEmptyMsg(sender, "");
            }
        } else {
            Util.sendEmptyMsg(sender, NO_DUEL_ARENAS);
        }

        Util.sendEmptyMsg(sender, "");
        Util.sendCredits(sender);
        Util.sendEmptyMsg(sender, Util.LINE_BREAK);
    }
}
