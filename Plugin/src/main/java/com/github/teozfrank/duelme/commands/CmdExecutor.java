package com.github.teozfrank.duelme.commands;

/**
        The MIT License (MIT)

        Copyright (c) 2014 teozfrank

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/

import com.github.teozfrank.duelme.main.DuelMe;
import java.util.concurrent.ConcurrentHashMap;

public class CmdExecutor {

    public DuelMe plugin;
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
