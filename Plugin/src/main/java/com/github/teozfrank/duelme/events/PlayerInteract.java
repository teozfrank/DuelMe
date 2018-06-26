package com.github.teozfrank.duelme.events;

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.SendConsoleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    private DuelMe plugin;

    public PlayerInteract(DuelMe plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerInteractSign(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block clickedBlock = e.getClickedBlock();
        Action action = e.getAction();



        if(action == Action.RIGHT_CLICK_BLOCK) {

            if (! clickedBlock.getType().equals(Material.WALL_SIGN)) {
                return;
            }
            SendConsoleMessage.debug("Right click wall sign yay!.");

            try {
                Sign sign = (Sign) clickedBlock.getState();
                String line1 = sign.getLine(0);
                line1 = ChatColor.stripColor(line1);
                if(line1.equals("[DuelMe]")) {
                    player.performCommand("duel join");
                }
            } catch (Exception ex) {

            }
        }
    }
}
