package com.github.teozfrank.duelme.nms.v1_16_R1;

import com.github.teozfrank.duelme.api.TitleActionbar;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class NMSHandler implements TitleActionbar {

    @Override
    public void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    @Override
    public void sendActionbar(Player player, Player sender, String message) {
        sendActionbar(player, message);
        sendActionbar(sender, message);
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void sendTitle(Player sender, Player acceptor, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitle(sender, title, subtitle, fadeIn, stay, fadeOut);
        sendTitle(acceptor, title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void sendBossbar(String title, String colorName, String styleName, Player player, Plugin plugin, int period) {
        //BarColor color = BarColor.valueOf(colorName.toUpperCase());
        //BarStyle style = BarStyle.valueOf(styleName.toUpperCase());
        //final BossBar bar = Bukkit.createBossBar(title, color, style);
        //bar.addPlayer(player);
        //bar.setVisible(true);
        //final double interval = 1.0 / (period * 20L);
        /*new BukkitRunnable() {
            @Override
            public void run() {
                double progress = bar.getProgress();
                double newProgress = progress - interval;
                if (progress <= 0.0 || newProgress <= 0.0) {
                    bar.setVisible(false);
                    bar.removeAll();
                    this.cancel();
                } else {
                    bar.setProgress(newProgress);
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1L);*/
    }
}

