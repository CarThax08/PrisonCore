package com.github.carthax08.servercore.data.files;

import com.github.carthax08.servercore.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class RanksFileHandler {
    public static YamlConfiguration ranksConfig;

    public static YamlConfiguration loadOrCreate(){
        Main.getInstance().saveResource("ranks.yml", false);
        File file = new File(Main.getInstance().getDataFolder(), "ranks.yml");
        ranksConfig = YamlConfiguration.loadConfiguration(file);
        return ranksConfig;
    }

    public YamlConfiguration reloadConfig(){
        File file = new File(Main.getInstance().getDataFolder(), "ranks.yml");
        ranksConfig = YamlConfiguration.loadConfiguration(file);
        return ranksConfig;
    }
}
