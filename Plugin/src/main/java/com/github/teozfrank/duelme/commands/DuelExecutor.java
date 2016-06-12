package com.github.teozfrank.duelme.commands;

/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2014 teozfrank
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.github.teozfrank.duelme.commands.duel.*;
import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelExecutor extends CmdExecutor implements CommandExecutor {

    public DuelExecutor(DuelMe plugin) {
        super(plugin);
        DuelCmd accept = new AcceptCmd(plugin, "duelme.player.accept");
        DuelCmd send = new SendCmd(plugin, "duelme.player.send");
        DuelCmd leave = new LeaveCmd(plugin, "duelme.player.leave");
        DuelCmd list = new ListCmd(plugin, "duelme.player.list");
        DuelCmd about = new AboutCmd(plugin, "duelme.player.info");

        addCmd("accept", accept, new String[]{
                "a"
        });

        addCmd("send", send, new String[]{
                "s"
        });

        addCmd("leave", leave, new String[]{
                "l"
        });

        addCmd("list", list);

        addCmd("about", about);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("duel")) {

            if (args.length < 1) {
                try {
                    Util.sendEmptyMsg(sender, Util.LINE_BREAK);
                    Util.sendEmptyMsg(sender, ChatColor.GOLD + "                            DuelMe - PVP for fun!");
                    Util.sendEmptyMsg(sender, "");
                    Util.sendEmptyMsg(sender, ChatColor.GREEN + "/duel - " + ChatColor.GOLD + "brings up this message.");
                    Util.sendEmptyMsg(sender, ChatColor.GREEN + "/duel send <player> - " + ChatColor.GOLD + "send a duel request to a player.");
                    Util.sendEmptyMsg(sender, ChatColor.GREEN + "/duel send <player> <amount> - " + ChatColor.GOLD +
                            "send a duel request to a player with a given bet amount.");
                    Util.sendEmptyMsg(sender, ChatColor.GREEN + "/duel accept <player> - " + ChatColor.GOLD + "accept a duel request.");
                    Util.sendEmptyMsg(sender, ChatColor.GREEN + "/duel leave - " + ChatColor.GOLD + "leave a duel.");
                    Util.sendEmptyMsg(sender, ChatColor.GREEN + "/duel list - " + ChatColor.GOLD + "lists duel arenas with their status(es).");
                    Util.sendEmptyMsg(sender, ChatColor.GREEN + "/duel about - " + ChatColor.GOLD + "more about this plugin.");
                    Util.sendEmptyMsg(sender, "");
                    Util.sendCredits(sender);
                    Util.sendEmptyMsg(sender, Util.LINE_BREAK);
                    return true;
                } catch (NoClassDefFoundError e) {
                    this.sendOutdatedMessage(sender);
                    return true;
                }

            }

            String sub = args[0].toLowerCase();

            DuelCmd cmd = (DuelCmd) super.getCmd(sub);

            if (cmd == null) {
                Util.sendMsg(sender, ChatColor.RED + "\"" + sub + "\" is not valid for the duel command.");
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
            } catch (NoClassDefFoundError e) {
                this.sendOutdatedMessage(sender);
                return true;
            }

            return true;

        }

        return false;
    }

    public void sendOutdatedMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You are running an outdated version of spigot: ");
        sender.sendMessage(ChatColor.YELLOW + plugin.getServer().getBukkitVersion());
        sender.sendMessage(ChatColor.RED + "Which this plugin does NOT support");
        sender.sendMessage(ChatColor.RED + "Please update to a newer up to date version in order to use this plugin.");
    }


}

