package com.github.carthax08.servercore.data.files;

import com.github.carthax08.servercore.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class BlocksFileHandler {
    public static YamlConfiguration blocksConfig;

    public static YamlConfiguration loadOrCreate(){
        Main.getInstance().saveResource("tokenblocks.yml", false);
        File file = new File(Main.getInstance().getDataFolder(), "tokenblocks.yml");
        blocksConfig = YamlConfiguration.loadConfiguration(file);
        return blocksConfig;
    }

    public YamlConfiguration reloadConfig(){
        File file = new File(Main.getInstance().getDataFolder(), "tokenblocks.yml");
        blocksConfig = YamlConfiguration.loadConfiguration(file);
        return blocksConfig;
    }
}
