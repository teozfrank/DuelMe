package com.teozcommunity.teozfrank.duelme.commands.duel;

import com.teozcommunity.teozfrank.duelme.commands.SubCmd;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: teoz
 * Date: 06/11/13
 * Time: 16:27
 * To change this template use File | Settings | File Templates.
 */
public abstract class DuelCmd extends SubCmd {

    public DuelCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    public abstract void run(CommandSender sender, String subCmd, String[] args);
}
