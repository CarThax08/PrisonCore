package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.data.files.PricesFileHandler;
import com.github.carthax08.servercore.util.DataStore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SellCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You can't sell items as console!");
            return true;
        }

        Player player = (Player) sender;

        if(PricesFileHandler.pricesConfig == null){
            player.sendMessage(ChatColor.RED + "The configuration for sell prices was not loaded properly. Please notify an admin!");
        }
        YamlConfiguration config = PricesFileHandler.pricesConfig;
        int itemsSold = 0;
        double moneyAdded = 0d;
        for(int i = 0; i < player.getInventory().getSize(); i++){
            ItemStack item = player.getInventory().getItem(i);
            if(item == null){
                continue;
            }
            System.out.println(config.getKeys(false));
            System.out.println(config.getKeys(true));
            if(config.getKeys(false).contains(item.getType().name().toLowerCase())){
                int amount = item.getAmount();
                System.out.println("1");
                if(DataStore.getPlayerData(player).addMoney(config.getDouble(item.getType().toString().toLowerCase()) * amount)){
                    System.out.println("2");
                    moneyAdded += config.getDouble(item.getType().toString().toLowerCase()) * amount;
                    itemsSold += amount;
                    player.getInventory().setItem(i, null);
                }
            }
        }
        player.sendMessage(ChatColor.GREEN + "Successfully sold " + itemsSold + " items for $" + moneyAdded + ".");
        return true;
    }
}
