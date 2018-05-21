package com.github.teozfrank.duelme.commands.duel;

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.DuelManager;
import com.github.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JoinCmd extends DuelCmd {

    public JoinCmd(DuelMe plugin, String mainPerm) {
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
            Util.sendMsg(sender, ChatColor.RED + "You cannot join a duel as you are already in one!");
            return;
        }

        if(dm.isQueued(playerUUID)) {
            Util.sendMsg(sender, ChatColor.RED + "You are already in the queue.");
            return;
        }

        dm.addQueuedPlayer(playerUUID);
        Util.sendMsg(sender, "You have joined the queue, there is currently " + dm.getQueuedPlayersSize() + " in the Queue");
    }
}
