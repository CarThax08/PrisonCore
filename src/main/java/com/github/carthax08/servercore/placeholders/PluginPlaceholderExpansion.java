package com.github.carthax08.servercore.placeholders;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.util.DataStore;
import com.github.carthax08.servercore.util.Util;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;


public class PluginPlaceholderExpansion extends PlaceholderExpansion {
    @NotNull
    private final Main plugin;

    public PluginPlaceholderExpansion(@NotNull Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "CarThax";
    }

    @Override
    public String getIdentifier() {
        return "prison";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("tokens")){
            return Util.format(DataStore.getPlayerData(player.getPlayer()).tokenBalance);
        }
        if(params.equalsIgnoreCase("autosmelt")){
            return DataStore.getPlayerData(player.getPlayer()).autosmelt ? ChatColor.GREEN + "On" : ChatColor.RED + "Off";
        }
        if(params.equalsIgnoreCase("prestige")){
            return String.valueOf(DataStore.getPlayerData(player.getPlayer()).pindex);
        }
        if(params.equalsIgnoreCase("rankprefix")){
            return ChatColor.translateAlternateColorCodes('&', DataStore.getPlayerData(player.getPlayer()).rank.prefix);
        }
        if(params.equalsIgnoreCase("sell_multiplier")){
            return String.valueOf(DataStore.getPlayerData(player.getPlayer()).sellMultiplier);
        }
        if(params.equalsIgnoreCase("autosell")){
            if(!player.getPlayer().hasPermission("autosell.toggle")) return ChatColor.RED + "Unavailable";
            return DataStore.getPlayerData(player.getPlayer()).autosell ? ChatColor.GREEN + "On" : ChatColor.RED + "Off";
        }
        if(params.equalsIgnoreCase("blocks_broken")){
            return String.valueOf(DataStore.getPlayerData(player.getPlayer()).blocksBroken);
        }
        return null; // Placeholder is unknown by the Expansion
    }
}
