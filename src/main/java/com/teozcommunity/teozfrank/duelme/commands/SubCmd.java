package com.teozcommunity.teozfrank.duelme.commands;

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

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SubCmd {

    public final String GEN_ERROR = ChatColor.RED + "An error has occurred with this command. Please recheck what you typed!";
    public final String NO_CONSOLE = "This command cannot be used by the console!";
    public final String NO_OBJECT = ChatColor.RED + "That object doesn't exist.";
    public final String NO_PERM = ChatColor.RED + "You do not have permission to use this command!";
    public final String NOT_CMD = ChatColor.RED + "That is not a command for DuelMe";
    public final String NOT_FLOAT = ChatColor.RED + "You must input a float for the amount.";
    public final String NOT_INT = ChatColor.RED + "You must input an integer for the amount. e.g. 1,3,6,7";
    public final String NO_DUEL_ARENAS = ChatColor.RED + "There are no duel arenas!";

    public FileConfiguration config;
    public DuelMe plugin;
    public boolean needsObject;

    public String permission;

    private Map<String, String> aliases = new HashMap<String, String>();

    public SubCmd(DuelMe plugin, String mainPerm) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.permission = mainPerm;
        this.needsObject = true;
    }

    /**
     * Add an alias for this command.
     *
     * @param alias The alias.
     */
    public void addAlias(String alias, String aliasTo) {
        this.aliases.put(alias.toLowerCase(), aliasTo);
    }

    /**
     * Get all the aliases for this command.
     *
     * @return List of aliases.
     */
    public Map<String, String> getAliases() {
        return aliases;
    }

    /**
     * Get the main command name from an alias.
     *
     * @param alias The alias.
     * @return The main command name, i.e. the first parameter in addCommand().
     */
    public String getCommand(String alias) {
        return aliases.get(alias);
    }

    /**
     * Get the permission for this command.
     *
     * @return The permission node.
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Get a value from the input arguments array in a command and convert to lower case.
     * Also supports a default object to return if the index is out of bounds.
     *
     * @param input  The array of input strings from a command.
     * @param index  The element to get from.
     * @param defVal The default object to return if the index is out of bounds.
     * @return Lower case string representation of the string (or default object) at the specified index.
     */
    public String getValue(String[] input, int index, Object defVal) {
        if (input.length > index) {
            return input[index];
        } else {
            return String.valueOf(defVal).toLowerCase();
        }
    }

    /**
     * Check if this command has a given alias.
     *
     * @param alias Alias to check.
     * @return True if this command has the given alias.
     */
    public boolean hasAlias(String alias) {
        alias = alias.toLowerCase();
        return this.aliases.containsKey(alias);
    }

    /**
     * Check if a command sender has permission for this command.
     *
     * @param sender The command sender.
     * @param perm   The permission to check or null to check this command's permission.
     * @return True if the command sender has permission.
     */
    public boolean permissible(CommandSender sender, String perm) {

        if (perm == null)
            perm = this.permission;

        if (perm.isEmpty())
            perm = this.permission;

        if (sender instanceof Player) {
            return ((Player) sender).hasPermission(perm);
        } else {
            return true;
        }
    }

    /**
     * Remove an alias.
     *
     * @param alias The alias to remove.
     */
    public void removeAlias(String alias) {
        this.aliases.remove(alias.toLowerCase());
    }

    /**
     * Set the aliases of this command.
     *
     * @param aliases List of aliases.
     */
    public void setAliases(Map<String, String> aliases) {
        this.aliases = aliases;
    }

    /**
     * Set the permission of this command.
     *
     * @param permission The permission.
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

}
