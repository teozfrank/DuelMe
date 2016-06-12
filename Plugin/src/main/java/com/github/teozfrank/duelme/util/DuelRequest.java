package com.github.teozfrank.duelme.util;

import java.util.UUID;

/**
 * Created by Frank on 04/02/2016.
 */
public class DuelRequest {

    private UUID duelSender;
    private UUID duelTarget;
    private String duelArena;
    private Long timeSent;

    public DuelRequest(UUID duelSender, UUID duelTarget, String duelArena) {
        this.duelSender = duelSender;
        this.duelTarget = duelTarget;
        this.duelArena = duelArena;
    }

    public DuelRequest(UUID duelSender, UUID duelTarget) {
        this.duelSender = duelSender;
        this.duelTarget = duelTarget;
        this.duelArena = null;
    }

    public DuelRequest(UUID duelSender, UUID duelTarget, String duelArena, Long timeSent) {
        this.duelSender = duelSender;
        this.timeSent = timeSent;
        this.duelArena = duelArena;
        this.duelTarget = duelTarget;
    }

    public UUID getDuelSender() {
        return duelSender;
    }

    public void setDuelSender(UUID duelSender) {
        this.duelSender = duelSender;
    }

    public String getDuelArena() {
        return duelArena;
    }

    public void setDuelArena(String duelArena) {
        this.duelArena = duelArena;
    }

    public UUID getDuelTarget() {
        return duelTarget;
    }

    public void setDuelTarget(UUID duelTarget) {
        this.duelTarget = duelTarget;
    }

    public Long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Long timeSent) {
        this.timeSent = timeSent;
    }
}
