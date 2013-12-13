package com.teozcommunity.teozfrank.duelme.commands.dueladmin;

import com.teozcommunity.teozfrank.duelme.commands.SubCmd;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.command.CommandSender;

/**
 * Created by frank on 13/12/13.
 */
public abstract class DuelAdminCmd extends SubCmd {

    public DuelAdminCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    public abstract void run(CommandSender sender, String subCmd, String[] args);
}
