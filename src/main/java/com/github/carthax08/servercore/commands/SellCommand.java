package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.data.files.PricesFileHandler;
import com.github.carthax08.servercore.util.DataStore;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class SellCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You can't sell items as console!");
            return true;
        }

        Player player = (Player) sender;

        sellItems(DataStore.getPlayerData(player).backpack, player);
        DataStore.getPlayerData(player).backpack.clear();
        return true;
    }

    public static void sellItems(ArrayList<ItemStack> items, Player player){
        if(PricesFileHandler.pricesConfig == null){
            player.sendMessage(ChatColor.RED + "The configuration for sell prices was not loaded properly. Please notify an admin!");
        }
        YamlConfiguration config = PricesFileHandler.pricesConfig;
        int itemsSold = 0;
        double moneyAdded = 0d;
        for(int i = 0; i < items.size(); i++){
            ItemStack item = items.get(i);
            if(item == null){
                continue;
            }
            ServerPlayer playerData = DataStore.getPlayerData(player);
            if(config.getKeys(false).contains(item.getType().name().toLowerCase())){
                int amount = item.getAmount();
                if(playerData.addMoney(config.getDouble(item.getType().toString().toLowerCase()) * amount)){
                    moneyAdded += config.getDouble(item.getType().toString().toLowerCase()) * amount;
                    itemsSold += amount;
                }
            }
        }

        ServerPlayer playerData = DataStore.getPlayerData(player);

        player.sendMessage(ChatColor.GREEN + "Successfully sold " + itemsSold + " items for $" + moneyAdded + ".");
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(
                        ChatColor.translateAlternateColorCodes('&', Main.backpackBarFormat
                                .replace("%amount%", String.valueOf(playerData.getItemsInBackpack()))
                                .replace("%max%", String.valueOf(playerData.backpackSize))
                        )
                )
        );
    }
}
