package com.teozcommunity.teozfrank.duelme.commands;

/**
        The MIT License (MIT)

        Copyright (c) 2014 teozfrank

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/

import com.teozcommunity.teozfrank.duelme.commands.admin.*;
import com.teozcommunity.teozfrank.duelme.commands.duel.AcceptCmd;
import com.teozcommunity.teozfrank.duelme.commands.duel.DuelCmd;
import com.teozcommunity.teozfrank.duelme.commands.duel.SendCmd;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.SendConsoleMessage;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelAdminExecutor extends CmdExecutor implements CommandExecutor {

    public DuelAdminExecutor(DuelMe plugin) {
        super(plugin);

        DuelAdminCmd create = new CreateCmd(plugin, "duelme.admin.create");
        DuelAdminCmd remove = new RemoveCmd(plugin, "duelme.admin.remove");
        DuelAdminCmd list = new ListCmd(plugin, "duelme.admin.list");
        DuelAdminCmd reload = new ReloadCmd(plugin, "duelme.admin.reload");
        DuelAdminCmd addKill = new AddKillCmd(plugin, "duelme.admin.addkill");
        DuelAdminCmd addDeath = new AddDeathCmd(plugin, "duelme.admin.adddeath");
        DuelAdminCmd setSpawnPoint1 = new SetSpawnPoint1Cmd(plugin, "duelme.admin.setspawnpoint1");
        DuelAdminCmd setSpawnPoint2 = new SetSpawnPoint2Cmd(plugin, "duelme.admin.setspawnpoint2");
        DuelAdminCmd restore = new RestoreCmd(plugin, "duelme.admin.restore");

        addCmd("create", create, new String[] {
            "c,new"
        });

        addCmd("remove", remove, new String[]{
                "r","delete"
        });

        addCmd("list", list, new String[]{
                "l"
        });

        addCmd("addkill", addKill, new String[]{
                "ak"
        });

        addCmd("adddeath", addDeath, new String[]{
                "ad"
        });

        addCmd("reload", reload);

        addCmd("setspawnpoint1", setSpawnPoint1, new String[] {
                "ss1"
        });

        addCmd("setspawnpoint2", setSpawnPoint2, new String[] {
                "ss2"
        });

        addCmd("restore", restore);

        create.needsObject = false;
        remove.needsObject = true;
        list.needsObject = false;
        reload.needsObject = false;
        addKill.needsObject = false;
        addDeath.needsObject = false;
        setSpawnPoint1.needsObject = true;
        setSpawnPoint2.needsObject = true;
        restore.needsObject = false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        final String INVALID_PARAMS = ChatColor.RED + "You entered invalid parameters for this command.";


        if (command.getName().equalsIgnoreCase("dueladmin")) {

            if (args.length < 1) {

                Util.sendEmptyMsg(sender, Util.LINE_BREAK);
                Util.sendEmptyMsg(sender, ChatColor.GOLD + "                           DuelMe - Admin Commands");
                Util.sendEmptyMsg(sender, "");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin create <arenaname> - "+ ChatColor.GOLD + "create a duel arena with the given name.");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin list - "+ ChatColor.GOLD + "list the duel arenas.");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin remove <arenaname> - "+ ChatColor.GOLD + "removes a duel arena with the given name.");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin setspawnpoint1 <arenaname> - "+ ChatColor.GOLD + "set the first spawnpoint for a duel arena.");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin setspawnpoint2 <arenaname> - "+ ChatColor.GOLD + "set the second spawnpoint for a duel arena.");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin reload - "+ ChatColor.GOLD + "reload the plugin configs.");
                Util.sendEmptyMsg(sender, "");
                Util.sendEmptyMsg(sender, ChatColor.GOLD + "Below commands only work if MySql is setup and enabled.");
                Util.sendEmptyMsg(sender, "");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin addkill <playername> - "+ ChatColor.GOLD + "add a kill for a player.");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin adddeath <playername> - "+ ChatColor.GOLD + "add a death for a player.");
                Util.sendEmptyMsg(sender, "");
                Util.sendCredits(sender);
                Util.sendEmptyMsg(sender, Util.LINE_BREAK);
                return true;
            }

        }
        DuelArena arena = null;
        String sub = args[0].toLowerCase();
        String objId = "";
        String[] params;

        if (args.length > 1)
            objId = args[1];

        DuelAdminCmd cmd = (DuelAdminCmd) super.getCmd(sub);

        if (cmd == null) {
            Util.sendMsg(sender, ChatColor.RED + "\"" + sub + "\" is not valid for the DuelAdmin command.");
            return true;
        }

        sub = cmd.getCommand(sub);


        if (!cmd.permissible(sender, null)) {
            Util.sendMsg(sender, cmd.NO_PERM);
            return true;
        }

        if (cmd.needsObject) {

            DuelManager dm = plugin.getDuelManager();
            arena = dm.getDuelArenaByName(objId);

            if(dm.getDuelArenas().size() == 0){
                Util.sendMsg(sender,ChatColor.RED + "There are no arenas to remove!");
                return true;
            }

            if(args.length == 1){
                Util.sendMsg(sender,ChatColor.YELLOW + "You must provide a Duel Arena name for this command!");
                return true;
            }

            if (arena == null) {
                Util.sendMsg(sender,ChatColor.RED + "Duel Arena " + ChatColor.AQUA + objId + ChatColor.RED + " does not exist!");
                return true;
            }

            params = makeParams(args, 2);

        } else {

            params = makeParams(args, 1);

            try {
                cmd.run(null, sender, sub, params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        try {
            cmd.run(arena, sender, sub, params);
        } catch (ArrayIndexOutOfBoundsException e) {
            Util.sendMsg(sender, INVALID_PARAMS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

        return true;
    }

}
