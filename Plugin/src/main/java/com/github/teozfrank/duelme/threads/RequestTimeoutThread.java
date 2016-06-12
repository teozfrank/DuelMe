package com.github.teozfrank.duelme.threads;

import com.github.teozfrank.duelme.main.DuelMe;
import com.github.teozfrank.duelme.util.DuelRequest;
import com.github.teozfrank.duelme.util.SendConsoleMessage;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Frank on 04/02/2016.
 */
public class RequestTimeoutThread extends BukkitRunnable {

    private DuelMe plugin;

    public RequestTimeoutThread(DuelMe plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<DuelRequest> duelRequestListToRemove = new ArrayList<DuelRequest>();//list to store the duel requests we want to remove

        for(DuelRequest duelRequest: plugin.getDuelManager().getDuelRequests()) {
            Long timeSent = duelRequest.getTimeSent();
            Long now = System.currentTimeMillis();

            Long calc = timeSent - now;

            long time = TimeUnit.MILLISECONDS.toMinutes(calc);

            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug(""+ time);
            }

            if(time >= 1L) {
                duelRequestListToRemove.add(duelRequest);
            }
        }

        for(DuelRequest duelRequest: duelRequestListToRemove) {
            plugin.getDuelManager().getDuelRequests().remove(duelRequest);
        }
    }
}
