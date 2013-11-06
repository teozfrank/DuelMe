package com.teozcommunity.teozfrank.duelme.commands;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: teoz
 * Date: 06/11/13
 * Time: 16:11
 * To change this template use File | Settings | File Templates.
 */

public class CmdExecutor {

    private DuelMe plugin;
    public ConcurrentHashMap<String, SubCmd> commands;

    public CmdExecutor(DuelMe plugin) {
        this.plugin = plugin;
        this.commands = new ConcurrentHashMap<String, SubCmd>();
    }

    public void addCmd(String name, SubCmd executor) {
        this.commands.put(name, executor);
    }

    public void addCmd(String name, SubCmd executor, String[] aliases) {
        commands.put(name, executor);
        executor.addAlias(name, name);
        for (String alias : aliases) {
            commands.put(alias, executor);
            executor.addAlias(alias, name);
        }
    }

    public SubCmd getCmd(String command) {
        return commands.get(command);
    }

    public void removeCmd(String name) {
        commands.remove(name);
    }

    public String[] makeParams(String[] baseParams, int offset) {
        int pLength = baseParams.length - offset;

        if (pLength < 1)
            return new String[0];

        String[] newParams = new String[pLength];
        for (int i = 0; i < pLength; i++) {
            newParams[i] = baseParams[i + offset];
        }

        return newParams;
    }


}
