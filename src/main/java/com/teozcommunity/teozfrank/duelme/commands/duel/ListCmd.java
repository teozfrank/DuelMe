package com.teozcommunity.teozfrank.duelme.commands.duel;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by frank on 30/01/14.
 */
public class ListCmd extends DuelCmd {
    public ListCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(CommandSender sender, String subCmd, String[] args) {

        DuelManager dm = plugin.getDuelManager();

        Util.sendEmptyMsg(sender, Util.LINE_BREAK);
        Util.sendEmptyMsg(sender, ChatColor.GOLD + "                   DuelMe - List Duel Arena status(es)");
        Util.sendEmptyMsg(sender, Util.LINE_BREAK);
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
        Util.sendEmptyMsg(sender, Util.LINE_BREAK);
        Util.sendCredits(sender);
        Util.sendEmptyMsg(sender, Util.LINE_BREAK);
    }
}
