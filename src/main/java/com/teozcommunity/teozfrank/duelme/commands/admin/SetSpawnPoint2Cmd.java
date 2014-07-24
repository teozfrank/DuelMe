package com.teozcommunity.teozfrank.duelme.commands.admin;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.FileManager;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by frank on 17/07/2014.
 */
public class SetSpawnPoint2Cmd extends DuelAdminCmd {

    public SetSpawnPoint2Cmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {
        if(!(sender instanceof Player)){
            Util.sendMsg(sender, NO_CONSOLE);
            return;
        }

        String duelArenaName = duelArena.getName();
        Player player = (Player) sender;

        Location pos1 = duelArena.getPos1();
        Location pos2 = duelArena.getPos2();
        Location playerLocation = player.getLocation();

        double x = playerLocation.getBlockX();
        double y = playerLocation.getBlockY();
        double z = playerLocation.getBlockZ();

        if(!Util.isInRegion(playerLocation, pos1, pos2)) {
            Util.sendMsg(sender, ChatColor.translateAlternateColorCodes('&',
                    "&cYou must be inside the region for arena: &b" + duelArenaName + " &cto set a spawnpoint!"));
            return;
        }

        playerLocation.setY(y + 2.0);//offset so player does not spawn in the ground if the chunks are not loaded.
        duelArena.setSpawnpoint2(playerLocation);
        Util.sendMsg(sender, ChatColor.translateAlternateColorCodes('&',
                "&aSpawnpoint2 set to: " + "&a(&b" + x + "&a)(&b" + y + "&a)(&b" + z + "&a)"
                        + ChatColor.GREEN + " for arena "  + ChatColor.AQUA + duelArenaName));
    }
}
