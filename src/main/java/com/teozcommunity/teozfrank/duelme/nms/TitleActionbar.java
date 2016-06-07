package com.teozcommunity.teozfrank.duelme.nms;

import net.minecraft.server.v1_9_R2.PacketPlayOutTitle;
import org.bukkit.entity.Player;

/**
 * Created by Frank on 06/06/2016.
 */
public interface TitleActionbar {

    public void sendActionbar(Player player, String message);

    public void sendActionbar(Player player, Player sender, String message);

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

    public void sendTitle(Player sender, Player accepter, String title, String subtitle, int fadeIn, int stay, int fadeOut);

}
