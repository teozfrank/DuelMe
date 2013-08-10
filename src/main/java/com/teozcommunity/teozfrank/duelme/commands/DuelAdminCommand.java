package com.teozcommunity.teozfrank.duelme.commands;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Frank
 * Date: 08/08/13
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 */
public class DuelAdminCommand implements CommandExecutor {

    private DuelMe plugin;

    public DuelAdminCommand(DuelMe plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            int length = args.length;
            if(length==1){
                if(args[0].equals("accept")){

                }

            }
        }



       return false;
    }
}
