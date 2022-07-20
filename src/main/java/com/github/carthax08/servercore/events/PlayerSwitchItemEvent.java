package com.github.carthax08.servercore.events;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerSwitchItemEvent implements Listener {
    @EventHandler
    public void onPlayerSwitchItems(PlayerItemHeldEvent event) {
        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) != null) {
            if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getType().name().toLowerCase().contains("pickaxe")) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false, false));
                return;
            }
        }
        event.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
