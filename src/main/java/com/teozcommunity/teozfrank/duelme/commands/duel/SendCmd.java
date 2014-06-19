package com.teozcommunity.teozfrank.duelme.commands.duel;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * User: teoz
 * Date: 18/11/13
 * Time: 20:10
 * To change this template use File | Settings | File Templates.
 */
public class SendCmd extends DuelCmd {

    public SendCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(CommandSender sender, String subCmd, String[] args) {
        if (!(sender instanceof Player)) {
            Util.sendMsg(sender, NO_CONSOLE);
            return;
        }

        if(args.length < 1){
            Util.sendMsg(sender, ChatColor.GREEN + "Usage: /duel send <player>");
            Util.sendMsg(sender, ChatColor.GREEN + "Or");
            Util.sendMsg(sender, ChatColor.GREEN + "Usage: /duel send <player> <amount>");
            return;
        }

        Player duelSender = (Player) sender;
        String duelTarget = getValue(args, 0, "");
        DuelManager dm = plugin.getDuelManager();

        if(args.length == 1) {
            dm.sendNormalDuelRequest(duelSender , duelTarget);
        }



    }
}
