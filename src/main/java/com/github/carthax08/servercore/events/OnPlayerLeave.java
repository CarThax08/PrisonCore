package com.github.carthax08.servercore.events;

import com.github.carthax08.servercore.util.DataStore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        DataStore.playerData.get(event.getPlayer()).savePlayerData(true);
        DataStore.playerData.remove(event.getPlayer());
    }
}
