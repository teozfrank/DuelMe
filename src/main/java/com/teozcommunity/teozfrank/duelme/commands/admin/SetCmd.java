package com.teozcommunity.teozfrank.duelme.commands.admin;

import com.teozcommunity.teozfrank.duelme.commands.SubCmd;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by frank on 25/12/13.
 */
public class SetCmd extends DuelAdminCmd {

    public SetCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {
        if(!(sender instanceof Player)){
            Util.sendMsg(sender, NO_CONSOLE);
            return;
        }

        if(args.length < 1){
           Util.sendEmptyMsg(sender, Util.LINE_BREAK);
           Util.sendEmptyMsg(sender, "Duel set comands");
           Util.sendEmptyMsg(sender, ChatColor.GREEN+ "/dueladmin set lobbyspawn - "+ ChatColor.GOLD + "set the duel lobby spawn");
           Util.sendEmptyMsg(sender, Util.LINE_BREAK);
           return;
        }
    }

}
