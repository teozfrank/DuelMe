package com.teozcommunity.teozfrank.duelme.commands;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
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

            if(length<1){
                p.sendMessage(ChatColor.GREEN + "0o--------------- " + plugin.pluginPrefix + ChatColor.GOLD + "PVP for fun" + ChatColor.GREEN + " ---------------o0\n\n" +

                        ChatColor.GREEN + "/dueladmin setplayer1spawn " + ChatColor.GOLD + "- Set player 1's spawn location \n" +
                        ChatColor.GREEN + "/dueladmin setplayer2spawn " + ChatColor.GOLD + "- Set player 2's spawn location \n" +
                        ChatColor.GREEN + "/dueladmin setlobbyspawn " + ChatColor.GOLD + "- Set the lobby spawn location \n" +
                        ChatColor.GREEN + "/dueladmin setspectatespawn " + ChatColor.GOLD + "- Set the specate spawn location \n" +
                        ChatColor.GREEN + "/dueladmin reload " + ChatColor.GOLD + "- Reload the plugin config from disk \n\n" +

                        ChatColor.GREEN + "0o--------------" + ChatColor.GOLD + " V" + plugin.version + " BETA by TeOzFrAnK " + ChatColor.GREEN + "-------------o0\n" +
                        ChatColor.GREEN + "0o-----" + ChatColor.GOLD + " http://dev.bukkit.org/bukkit-plugins/duelme/ " + ChatColor.GREEN + "------o0"

                );
                return true;
            }

            if(length==1){
                if(args[0].equals("setplayer1spawn")){
                   plugin.locations.setSenderSpawnLocation(p.getPlayer());
                    return true;
                }
                else if(args[0].equals("setplayer2spawn")){
                    plugin.locations.setTargetSpawnLocation(p.getPlayer());
                    return true;
                }
                else if(args[0].equals("setlobbyspawn")){
                    plugin.locations.setLobbySpawnLocation(p.getPlayer());
                    return true;
                }
                else if(args[0].equals("setspectatespawn")){
                    plugin.locations.setSpectateLocation(p.getPlayer());
                    return true;
                }
                else if(args[0].equals("reload")){
                    plugin.reloadConfig();
                    plugin.fileManager.reloadLocations();
                    p.sendMessage(plugin.pluginPrefix+ ChatColor.GREEN+"Config.yml and locations.yml reloaded from disk.");
                    return true;
                }
                else {
                    p.sendMessage(plugin.pluginPrefix+ChatColor.RED+"Unknown Command!");
                    return true;
                }

            }
        }



       return false;
    }
}
