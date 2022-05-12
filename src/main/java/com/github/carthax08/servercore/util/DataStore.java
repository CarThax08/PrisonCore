package com.github.carthax08.servercore.util;

import com.github.carthax08.servercore.data.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;


public class DataStore {
    public static HashMap<Player, ServerPlayer> playerData = new HashMap<>();


    public static ServerPlayer getPlayerData(Player player){
        return playerData.get(player);
    }
    public static String lastRankGroup;

    public static Map<ItemStack, String> cratesGuiCommands = new HashMap<>();


}
