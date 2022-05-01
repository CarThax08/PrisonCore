package com.github.carthax08.servercore.util;

import com.github.carthax08.servercore.data.ServerPlayer;
import net.luckperms.api.model.group.Group;
import org.bukkit.entity.Player;

import java.util.HashMap;


public class DataStore {
    public static HashMap<Player, ServerPlayer> playerData = new HashMap<>();


    public static ServerPlayer getPlayerData(Player player){
        return playerData.get(player);
    }
    public static String lastRankGroup;


}
