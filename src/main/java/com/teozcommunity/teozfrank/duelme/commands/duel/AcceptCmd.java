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
 * Date: 06/11/13
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public class AcceptCmd extends DuelCmd {

    public AcceptCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(CommandSender sender, String subCmd, String[] args) {
        if (!(sender instanceof Player)) {
            Util.sendMsg(sender, NO_CONSOLE);
            return;
        }

        if(args.length < 1){
            Util.sendMsg(sender, ChatColor.GREEN + "Usage: /duel accept <player>");
            return;
        }

        Player accepter = (Player) sender;
        String senderName = getValue(args, 0, "");

        DuelManager dm = plugin.getDuelManager();
        dm.acceptRequest(accepter , senderName);
    }
}
