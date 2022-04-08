package com.github.carthax08.servercore.events;

import com.github.carthax08.servercore.util.DataStore;
import com.github.carthax08.servercore.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        DataStore.playerData.put(event.getPlayer(), Util.loadPlayerData(event.getPlayer()));
    }
}
