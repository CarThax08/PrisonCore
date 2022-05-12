package com.github.carthax08.servercore.util;

import com.github.carthax08.servercore.CustomEnchantment;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.data.files.CratesFileHandler;
import com.github.carthax08.servercore.data.files.DataFileHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
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
    /* SCRAPPED, MIGHT ADD IN AN UPDATE
    public static void openEnchantGui(Player player){
        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, "Enchants");
        ItemMeta heldItemMeta = player.getInventory().getItemInMainHand().getItemMeta();
        for (int i = 0; i < inventory.getSize(); i++){
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getDyeData());
            inventory.setItem(i, item);
        }
        for(CustomEnchantment enchant : CustomEnchantment.values()){
            ItemStack item = new ItemStack(enchant.displayMaterial);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(enchant.displayName.toUpperCase());
            switch (enchant) {
                case GREED:
                    List<String> greedLore = new ArrayList<>();
                    String greedEnchant;
                    int greedLevel = 0;

                    for (String string : heldItemMeta.hasLore() ? heldItemMeta.getLore() : new ArrayList<String>()) {
                        if (!string.contains(enchant.displayName))
                            continue;
                        greedEnchant = string;
                        greedLevel = Integer.parseInt(greedEnchant.substring(greedEnchant.indexOf(' ')).replace(" ", "").replace("Greed", ""));
                    }

                    greedLore.add("Levels: " + greedLevel + "/ 10");
                    greedLore.add("Cost: " + (enchant.price + (greedLevel * enchant.price / 5)));

                    meta.setLore(greedLore);
                    item.setItemMeta(meta);
                    break;
                case EXPLOSIVE:
                    List<String> lore = new ArrayList<>();
                    String explosiveEnchant;
                    int explosiveLevel = 0;

                    for (String string : heldItemMeta.hasLore() ? heldItemMeta.getLore() : new ArrayList<String>()) {
                        if (!string.contains(enchant.displayName)) continue;
                        explosiveEnchant = string;
                        explosiveLevel = Integer.parseInt(explosiveEnchant.substring(explosiveEnchant.indexOf(' ')).replace(" ", ""));
                    }

                    lore.add("Levels: " + explosiveLevel + "/ 5");
                    lore.add("Cost: " + (enchant.price + (explosiveLevel * enchant.price / 5)));

                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    break;
            }
            inventory.setItem(enchant.slot, item);
        }
        player.openInventory(inventory);
    }
    */

    public static void openCratesGui(Player sender) {
        YamlConfiguration cratesUi = CratesFileHandler.cratesConfig;

        if(cratesUi == null){
            sender.sendMessage(ChatColor.RED + "Attempted to open the crates UI but the config was null!");
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, "Buy Keys");
        ConfigurationSection items = cratesUi.getConfigurationSection("gui.slots");
        for(String string : items.getKeys(false)){
            Bukkit.getConsoleSender().sendMessage(string);
            ItemStack item = new ItemStack(Material.CHEST, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(items.getString(string + ".name"));
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
}
