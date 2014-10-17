package com.teozcommunity.teozfrank.duelme.commands.admin;

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
import com.teozcommunity.teozfrank.duelme.util.FileManager;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCmd extends DuelAdminCmd {
    public ReloadCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {

        FileManager fm = plugin.getFileManager();
        Util.sendMsg(sender, ChatColor.YELLOW + "Reloading.");
        fm.reloadDuelArenas();
        Util.sendMsg(sender, ChatColor.YELLOW + "Reloaded duelarenas.yml!");
        plugin.reloadConfig();
        Util.sendMsg(sender, ChatColor.YELLOW + "Reloaded config.yml!");
        Util.sendMsg(sender, ChatColor.YELLOW + "Saving Duel arenas!");
        fm.saveDuelArenas();
        Util.sendMsg(sender, ChatColor.YELLOW + "Loading Duel arenas!");
        fm.loadDuelArenas();
        Util.sendMsg(sender, ChatColor.GREEN + "Complete!");
    }
}
