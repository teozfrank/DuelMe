package com.github.teozfrank.duelme.commands.duel;

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.DuelManager;
import com.github.teozfrank.duelme.util.MessageManager;
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
        MessageManager mm = plugin.getMessageManager();

        if(dm.isInDuel(playerUUID)){
            Util.sendMsg(sender, ChatColor.RED + "You cannot join the queue as you are already in a duel!");
            return;
        }

        if(dm.isQueued(playerUUID)) {
            Util.sendMsg(sender, mm.getAlreadyInQueueMessage());
            return;
        }

        dm.addQueuedPlayer(playerUUID);
        String queueJoinedMessage = mm.getQueueJoinMessage();
        queueJoinedMessage = queueJoinedMessage.replaceAll("%queuesize%", "" + dm.getQueuedPlayersSize());
        Util.sendMsg(sender, queueJoinedMessage);
    }
}
