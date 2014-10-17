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
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AboutCmd extends DuelCmd {

    public AboutCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }


    @Override
    public void run(CommandSender sender, String subCmd, String[] args) {
        Util.sendMsg(sender, ChatColor.GREEN + "DuelMe: "+ ChatColor.AQUA + "V" + plugin.getDescription().getVersion());
        Util.sendMsg(sender, ChatColor.GREEN + "Original Author:" +ChatColor.AQUA +" teozfrank."); // please do not remove or modify this. I put countless hours into this project all free of charge, return the favour by leaving me credit.
        Util.sendMsg(sender, ChatColor.GREEN + "Licensed under the" + ChatColor.AQUA + " MIT License.");
    }
}
