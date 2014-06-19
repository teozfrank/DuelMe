package com.teozcommunity.teozfrank.duelme.commands.duel;

import com.teozcommunity.teozfrank.duelme.commands.duel.DuelCmd;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by frank on 11/01/14.
 */
public class LeaveCmd extends DuelCmd {
    public LeaveCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(CommandSender sender, String subCmd, String[] args) {
        if(!(sender instanceof Player)){
            Util.sendMsg(sender, NO_CONSOLE);
            return;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        DuelManager dm = plugin.getDuelManager();

        if(dm.isInDuel(playerUUID)){
            dm.endDuel(player);
        } else {
            Util.sendMsg(sender, ChatColor.RED + "You cannot leave duel if you are not in one!");
        }
    }
}
