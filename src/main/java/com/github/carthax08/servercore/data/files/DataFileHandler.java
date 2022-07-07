package com.github.carthax08.servercore.data.files;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class DataFileHandler {

    public static YamlConfiguration loadOrCreatePlayerFile(Player player) throws IOException {
        File file = new File(Main.getInstance().getDataFolder() + "/players/" + player.getUniqueId() + ".yml");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(!config.isSet("tokens")){
            config.set("tokens", 0d);
        }
        if(!config.isSet("prestige")){
            config.set("prestige", 0);
        }
        if(!config.isSet("autosmelt")){
            config.set("autosmelt", false);
        }
        if(!config.isSet("multiplier")){
            config.set("multiplier", 1d);
        }
        if(!config.isSet("rank")){
            config.set("rank", 0);
        }
        if(!config.isSet("autosell")){
            config.set("autosell", false);
        }
        if(!config.isSet("backpack")){
            config.set("backpack", new ArrayList<ItemStack>());
        }
        if(!config.isSet("backpackSize")){
            config.set("backpackSize", 50000);
        }
        return config;
    }

    public static void savePlayerDataToFile(ServerPlayer player) throws IOException {
        File file = new File(Main.getInstance().getDataFolder() + "/players/" + player.player.getUniqueId() + ".yml");
        player.config.save(file);

    }

}
