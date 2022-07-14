package com.github.carthax08.servercore.util;

import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.rankup.Rank;
import com.github.carthax08.servercore.rankup.RankHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayerDataHandler {
    public static ServerPlayer loadPlayerData(YamlConfiguration config, Player player) {

        double tokens = config.getDouble("tokens");
        int prestigeIndex = config.getInt("prestige");
        int backpackSize = config.getInt("backpackSize");
        boolean autosmelt = config.getBoolean("autosmelt");
        boolean autosell = config.getBoolean("autosell");
        double multi = config.getDouble("multiplier");
        Rank rank = RankHandler.getRank(config.getInt("rank"));
        ArrayList<ItemStack> backpack = (ArrayList<ItemStack>) config.getList("backpack");

        return new ServerPlayer(player, tokens, prestigeIndex, autosell, config, multi, rank, backpack, backpackSize);
    }
}
