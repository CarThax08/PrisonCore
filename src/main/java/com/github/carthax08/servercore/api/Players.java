package com.github.carthax08.servercore.api;

import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.prestige.Prestige;
import com.github.carthax08.servercore.util.DataStore;
import org.bukkit.entity.Player;

public class Players {
    public static ServerPlayer getPlayerData(Player player){
        return DataStore.getPlayerData(player);
    }

    public static boolean getAutosellStatus(Player player){
        return DataStore.getPlayerData(player).autosell;
    }

    public static double getTokens(Player player){
        return DataStore.getPlayerData(player).tokenBalance;
    }
    public static void setTokens(Player player, double amount){
        DataStore.getPlayerData(player).tokenBalance = amount;
    }

    public static Prestige getPrestige(Player player){
        return DataStore.getPlayerData(player).prestige;
    }

    public static int getPrestigeIndex(Player player){
        return DataStore.getPlayerData(player).pindex;
    }
}
