package com.github.carthax08.servercore.data.files;

import com.github.carthax08.servercore.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CratesFileHandler {

    public static YamlConfiguration cratesConfig;

    public static YamlConfiguration loadOrCreate(){
        Main.getInstance().saveResource("tokenshop.yml", false);
        File file = new File(Main.getInstance().getDataFolder(), "tokenshop.yml");
        cratesConfig = YamlConfiguration.loadConfiguration(file);
        return cratesConfig;
    }

    public YamlConfiguration reloadConfig(){
        File file = new File(Main.getInstance().getDataFolder(), "tokenshop.yml");
        cratesConfig = YamlConfiguration.loadConfiguration(file);
        return cratesConfig;
    }

}
