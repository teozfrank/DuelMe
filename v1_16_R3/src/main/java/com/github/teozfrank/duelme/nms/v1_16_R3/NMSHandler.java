package com.github.teozfrank.duelme.nms.v1_16_R3;

import com.github.teozfrank.duelme.api.TitleActionbar;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NMSHandler implements TitleActionbar {

    @Override
    public void sendActionbar(Player player, String message) {
        /*IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, ChatMessageType.GAME_INFO);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bar);*/
        player.sendMessage(message);
    }

    @Override
    public void sendActionbar(Player player, Player sender, String message) {
        sendActionbar(player, message);
        sendActionbar(sender, message);
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent titleJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        IChatBaseComponent subtitleJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJSON, fadeIn, stay, fadeOut);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleJSON);
        connection.sendPacket(titlePacket);
        connection.sendPacket(subtitlePacket);
    }

    @Override
    public void sendTitle(Player sender, Player acceptor, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitle(sender, title, subtitle, fadeIn, stay, fadeOut);
        sendTitle(acceptor, title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void sendBossbar(String title, String colorName, String styleName, Player player, Plugin plugin, int period) {
        BarColor color = BarColor.valueOf(colorName.toUpperCase());
        BarStyle style = BarStyle.valueOf(styleName.toUpperCase());
        final BossBar bar = Bukkit.createBossBar(title, color, style);
        bar.addPlayer(player);
        bar.setVisible(true);
        final double interval = 1.0 / (period * 20L);
        new BukkitRunnable() {
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
        }.runTaskTimerAsynchronously(plugin, 0, 1L);
    }
}


