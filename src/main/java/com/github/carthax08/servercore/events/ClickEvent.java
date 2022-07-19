package com.github.carthax08.servercore.events;


import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.util.DataStore;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ClickEvent implements Listener {

    Main main;
    public ClickEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!e.getItem().getType().equals(Material.PAPER)) {return;}
            PersistentDataContainer contain = e.getItem().getItemMeta().getPersistentDataContainer();
            NamespacedKey amount = new NamespacedKey(main, "amount");
            if (!contain.getKeys().contains(amount)) {return;}
            NamespacedKey type = new NamespacedKey(main, "type");
            if (!contain.getKeys().contains(type)) {return;}
            int storedamount = contain.get(amount, PersistentDataType.INTEGER);
            String storedtype = contain.get(type, PersistentDataType.STRING);
            ServerPlayer player = DataStore.getPlayerData(e.getPlayer());
            switch (storedtype) {
                case ("balance"):
                    if (e.getPlayer().getInventory().getItemInOffHand().equals(e.getItem())) {
                        if (e.getItem().getAmount() > 1) {
                            e.getItem().setAmount(e.getItem().getAmount() - 1);
                        } else {
                            e.getPlayer().getInventory().setItemInOffHand(null);
                        }
                    } else {
                        if (e.getItem().getAmount() > 1) {
                            e.getItem().setAmount(e.getItem().getAmount() - 1);
                        } else {
                            e.getPlayer().getInventory().remove(e.getItem());
                        }
                    }
                    player.addMoney(storedamount);
                    player.savePlayerData(false);
                    break;
                case ("token"):
                    if (e.getPlayer().getInventory().getItemInOffHand().equals(e.getItem())) {
                        if (e.getItem().getAmount() > 1) {
                            e.getItem().setAmount(e.getItem().getAmount() - 1);
                        } else {
                            e.getPlayer().getInventory().setItemInOffHand(null);
                        }
                    } else {
                        if (e.getItem().getAmount() > 1) {
                            e.getItem().setAmount(e.getItem().getAmount() - 1);
                        } else {
                            e.getPlayer().getInventory().remove(e.getItem());
                        }
                    }
                    player.tokenBalance = player.tokenBalance + storedamount;
                    player.savePlayerData(false);
                    break;
            }
        }
    }

}
