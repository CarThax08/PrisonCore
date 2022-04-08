package com.github.carthax08.servercore.data.files;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;


public class DataFileHandler {

    public static YamlConfiguration loadOrCreatePlayerFile(Player player) throws IOException {
        File file = new File(Main.getInstance().getDataFolder() + "/players/" + player.getUniqueId() + ".yml");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(!config.isSet("tokens")){
            System.out.println("1");
            config.set("tokens", 0d);
        }
        if(!config.isSet("prestige")){
            System.out.println("2");
            config.set("prestige", 0);
        }
        return config;
    }

    public static void savePlayerDataToFile(ServerPlayer player) throws IOException {
        File file = new File(Main.getInstance().getDataFolder() + "/players/" + player.player.getUniqueId() + ".yml");
        player.config.save(file);
        System.out.println(player.config.getDouble("tokens"));

    }

}
