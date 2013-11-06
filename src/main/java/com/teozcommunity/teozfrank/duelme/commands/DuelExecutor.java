package com.teozcommunity.teozfrank.duelme.commands;

import com.teozcommunity.teozfrank.duelme.commands.dueladmin.AcceptCmd;
import com.teozcommunity.teozfrank.duelme.commands.dueladmin.DuelCmd;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * User: teoz
 * Date: 06/11/13
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class DuelExecutor extends CmdExecutor implements CommandExecutor {

    public DuelExecutor(DuelMe plugin) {
        super(plugin);
        DuelCmd accept = new AcceptCmd(plugin, "duelme.player.accept");

        addCmd("accept", accept, new String[]{
                "a"
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("duel")) {

            if (args.length < 1) {


                Util.sendEmptyMsg(sender, "DuelMe");
                //TODO finish implementing the list of commands for plugin
                return true;
            }

            String sub = args[0].toLowerCase();

            DuelCmd cmd = (DuelCmd) super.getCmd(sub);

            if (cmd == null) {
                Util.sendMsg(sender, ChatColor.RED + "\"" + sub + "\" is not valid for this command.");
                return true;
            }

            sub = cmd.getCommand(sub);

            if (sender instanceof Player) {
                Player p = (Player) sender;

                if (!p.hasPermission(cmd.permission)) {
                    Util.sendMsg(p, cmd.NO_PERM);
                    return true;
                }
            }

            try {
                cmd.run(sender, sub, makeParams(args, 1));
            } catch (ArrayIndexOutOfBoundsException e) {
                Util.sendMsg(sender, ChatColor.RED + "You entered invalid parameters for this command!.");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Util.sendMsg(sender, cmd.GEN_ERROR);
                return true;
            }

            return true;

        }

        return false;
    }

}

