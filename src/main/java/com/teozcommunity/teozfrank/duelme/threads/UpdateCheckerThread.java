package com.teozcommunity.teozfrank.duelme.threads;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.UpdateChecker;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by frank on 15/07/2014.
 */
public class UpdateCheckerThread extends BukkitRunnable {

    private DuelMe plugin;

    public UpdateCheckerThread(DuelMe plugin) {
         this.plugin = plugin;
    }

    @Override
    public void run() {
        new UpdateChecker(plugin, 60044);
    }
}
