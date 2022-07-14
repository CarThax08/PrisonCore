package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.data.files.PricesFileHandler;
import com.github.carthax08.servercore.util.DataStore;
import com.github.carthax08.servercore.util.Util;
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

import java.text.NumberFormat;
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
        return true;
    }

    public static void sellItems(ArrayList<ItemStack> items, Player player){
        if(PricesFileHandler.pricesConfig == null){
            player.sendMessage(ChatColor.RED + "The configuration for sell prices was not loaded properly. Please notify an admin!");
        }
        YamlConfiguration config = PricesFileHandler.pricesConfig;
        int itemsSold = 0;
        double moneyAdded = 0d;
        ServerPlayer playerData = DataStore.getPlayerData(player);
        for (ItemStack item : items) {
            if (item == null) {
                continue;
            }
            if (config.getKeys(false).contains(item.getType().name().toLowerCase())) {
                int amount = item.getAmount();
                if (playerData.addMoney((config.getDouble(item.getType().toString().toLowerCase()) * amount) * (playerData.sellMultiplier + playerData.tempSellMultiplier))) {
                    moneyAdded += config.getDouble(item.getType().toString().toLowerCase()) * amount;
                    itemsSold += amount;
                }
            }
        }


        //TODO: Make Toggleable.
        player.sendMessage(ChatColor.GREEN + "Successfully sold " + Util.format(itemsSold) + " items for $" + Util.format(moneyAdded) + ".");
        NumberFormat format = NumberFormat.getNumberInstance();
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(
                        ChatColor.translateAlternateColorCodes('&', Main.backpackBarFormat
                                .replace("%amount%", format.format(playerData.getItemsInBackpack()))
                                .replace("%max%", format.format(playerData.backpackSize))
                        )
                )
        );
        DataStore.getPlayerData(player).backpack.clear();
    }
}
