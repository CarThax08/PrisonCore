package com.github.carthax08.servercore.data.files;

import com.github.carthax08.servercore.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PricesFileHandler {
    public static YamlConfiguration pricesConfig;

    public static YamlConfiguration loadOrCreate(){
        Main.getInstance().saveResource("sellprices.yml", false);
        File file = new File(Main.getInstance().getDataFolder(), "sellprices.yml");
        pricesConfig = YamlConfiguration.loadConfiguration(file);
        return pricesConfig;
    }

    public YamlConfiguration reloadConfig(){
        File file = new File(Main.getInstance().getDataFolder(), "sellprices.yml");
        pricesConfig = YamlConfiguration.loadConfiguration(file);
        return pricesConfig;
    }
}
