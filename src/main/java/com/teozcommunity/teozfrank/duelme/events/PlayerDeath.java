package com.teozcommunity.teozfrank.duelme.events;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * Created with IntelliJ IDEA.
 * Original Author: teozfrank
 * Date: 08/08/13
 * Time: 16:33
 * Project: DuelMe
 * -----------------------------
 * Removing this header is in breach of the license agreement,
 * please do not remove, move or edit it in any way.
 * -----------------------------
 */
public class PlayerDeath implements Listener {
    private DuelMe plugin;

    public PlayerDeath(DuelMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (plugin.duelingPlayers.size() == 2) {
            if (p.getKiller() instanceof Player) {
                Player killer = p.getKiller();
                e.getDrops().clear();//drop nothing on death
                e.setDroppedExp(0);
                e.setKeepLevel(true);
                this.launchFirework(plugin.locations.lobbySpawnLocation());// little suprise for when a player dies
                if (plugin.getConfig().getBoolean("duelme.announce.deaths")) {
                    e.setDeathMessage(plugin.pluginPrefix + ChatColor.YELLOW + p.getName() + ChatColor.AQUA + " Was Killed in a DuelArena by " +
                            ChatColor.YELLOW + killer.getName());
                }
                plugin.util.endDuel();//end the duel
            } else {
                e.getDrops().clear();//drop nothing on death
                if (plugin.getConfig().getBoolean("duelme.announce.deaths")) {
                    e.setDeathMessage(plugin.pluginPrefix + ChatColor.YELLOW + p.getName() + ChatColor.AQUA + " Was Killed in a DuelArena!");
                }
                e.setKeepLevel(true);
                plugin.util.endDuel();//end the duel
            }
        } else {
            plugin.util.endDuel();
        }
    }

    public void launchFirework(Location loc){
        //Spawn the Firework, get the FireworkMeta.
        Firework fw = (Firework) Bukkit.getServer().getWorld(loc.getWorld().getName()).spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder()
                .trail(true)
                .flicker(true)
                .withColor(Color.AQUA)
                .withFade(Color.BLUE).with(FireworkEffect.Type.STAR)
                .build();
        fwm.addEffect(effect);
        fwm.setPower(1);

        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);
    }
}
