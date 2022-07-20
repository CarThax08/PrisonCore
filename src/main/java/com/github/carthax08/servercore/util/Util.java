package com.github.carthax08.servercore.util;

import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.data.files.CratesFileHandler;
import com.github.carthax08.servercore.data.files.DataFileHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static ServerPlayer loadPlayerData(Player player) throws IOException {
        return PlayerDataHandler.loadPlayerData(DataFileHandler.loadOrCreatePlayerFile(player), player);
    }

    public static void openTokenShop(Player sender) {
        YamlConfiguration cratesUi = CratesFileHandler.cratesConfig;

        if(cratesUi == null){
            sender.sendMessage(ChatColor.RED + "Attempted to open the crates UI but the config was null!");
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, "NovaCoin Shop");
        ConfigurationSection items = cratesUi.getConfigurationSection("gui.slots");
        for(String string : items.getKeys(false)){
            Bukkit.getConsoleSender().sendMessage(string);
            ItemStack item = new ItemStack(Material.CHEST, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', items.getString(string + ".name")));
            List<String> lore = new ArrayList<>(items.getStringList(string + ".lore"));
            lore.add("\n");
            lore.add(ChatColor.GOLD + "Cost: " + items.getString(string + ".tokencost"));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(Integer.parseInt(string), item);
            DataStore.cratesGuiCommands.put(item, items.getString(string + ".command").replace("%player%", sender.getName()));
        }
        sender.openInventory(inventory);
    }

    public static String format(double value) {
        if(value >= 1000000000000d){
            value = value / 1000000000000d;
            String returnValue = value + "T";
            if(returnValue.contains(".")){
                returnValue = returnValue.substring(0, returnValue.indexOf('.')+2) + "T";
            }
            if(returnValue.contains(".0")){
                returnValue = returnValue.replace(".0", "");
            }
            return returnValue;
        } else if(value >= 1000000000d){
            value = value / 1000000000d;
            String returnValue = value + "B";
            if(returnValue.contains(".")){
                returnValue = returnValue.substring(0, returnValue.indexOf('.')+2) + "B";
            }
            if(returnValue.contains(".0")){
                returnValue = returnValue.replace(".0", "");
            }
            return returnValue;
        } else if(value >= 1000000d){
            value = value / 1000000d;
            String returnValue = value + "M";
            if(returnValue.contains(".")){
                returnValue = returnValue.substring(0, returnValue.indexOf('.')+2) + "M";
            }
            if(returnValue.contains(".0")){
                returnValue = returnValue.replace(".0", "");
            }
            return returnValue;
        } else if(value >= 1000d){
            value = value / 1000d;
            String returnValue = value + "K";
            if(returnValue.contains(".")){
                returnValue = returnValue.substring(0, returnValue.indexOf('.')+2) + "K";
            }
            if(returnValue.contains(".0")){
                returnValue = returnValue.replace(".0", "");
            }
            return returnValue;
        } else{
            return String.valueOf(value);
        }
    }

    public static void openBackpackShopGui(Player player) {
        //Implement Later
    }
}
