package com.teozcommunity.teozfrank.duelme.nms;

import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R2.PlayerConnection;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Frank on 06/06/2016.
 */
public class TitleActionbar_1_9_R2 implements TitleActionbar {

    @Override
    public void sendActionbar(Player player, String message) {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bar);
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
}
