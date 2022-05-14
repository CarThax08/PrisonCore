package com.github.carthax08.servercore.util;

import com.github.carthax08.servercore.data.ServerPlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerDataHandler {


    public static ServerPlayer loadPlayerData(YamlConfiguration config, Player player) {

        double tokens = config.getDouble("tokens");
        int prestigeIndex = config.getInt("prestige");
        boolean autosmelt = config.getBoolean("autosmelt");
        double multi = config.getDouble("multiplier");

        return new ServerPlayer(player, tokens, prestigeIndex, autosmelt, config, multi);
    }
}
