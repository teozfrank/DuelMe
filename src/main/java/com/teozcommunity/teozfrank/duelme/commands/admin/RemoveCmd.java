package com.teozcommunity.teozfrank.duelme.commands.admin;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by frank on 30/12/13.
 */
public class RemoveCmd extends DuelAdminCmd {

    public RemoveCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {

        if(args.length < 1){
            Util.sendMsg(sender,ChatColor.GREEN + "Usage: /dueladmin remove <arenaname>");
            return;
        }

        DuelManager dm = plugin.getDuelManager();

        dm.removeDuelArena(duelArena);

        Util.sendMsg(sender,ChatColor.GREEN + "Removed Duel Arema " + ChatColor.AQUA + duelArena.getName());
    }
}
