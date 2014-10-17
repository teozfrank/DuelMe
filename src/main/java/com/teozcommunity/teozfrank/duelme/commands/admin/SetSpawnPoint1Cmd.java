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
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnPoint1Cmd extends DuelAdminCmd {

    public SetSpawnPoint1Cmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {
        if(!(sender instanceof Player)){
            Util.sendMsg(sender, NO_CONSOLE);
            return;
        }

        String duelArenaName = duelArena.getName();
        Player player = (Player) sender;

        Location pos1 = duelArena.getPos1();
        Location pos2 = duelArena.getPos2();
        Location playerLocation = player.getLocation();

        double x = playerLocation.getBlockX();
        double y = playerLocation.getBlockY();
        double z = playerLocation.getBlockZ();

        if(!Util.isInRegion(playerLocation, pos1, pos2)) {
            Util.sendMsg(sender, ChatColor.translateAlternateColorCodes('&',
                    "&cYou must be inside the region for arena: &b" + duelArenaName + " &cto set a spawnpoint!"));
            return;
        }

        playerLocation.setY(y + 2.0);
        duelArena.setSpawnpoint1(playerLocation);//offset so player does not spawn in the ground if the chunks are not loaded.
        Util.sendMsg(sender, ChatColor.translateAlternateColorCodes('&',
                "&aSpawnpoint1 set to: " + "&a(&b" + x + "&a)(&b" + y + "&a)(&b" + z + "&a)"
                        + ChatColor.GREEN + " for arena "  + ChatColor.AQUA + duelArenaName));
    }
}
