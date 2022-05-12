package com.github.carthax08.servercore.prestige;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class PrestigeHandler {

    public static ArrayList<Prestige> prestiges = new ArrayList<>();

    public static Prestige deserialize(ConfigurationSection config, int index) {

        return new Prestige(config.getDouble("cost"), config.getStringList("grantperms").toArray(new String[0]), index);
    }

    public static void loadPrestiges(YamlConfiguration config){
        ConfigurationSection prestigesConfig = config.getConfigurationSection("prestiges");
        assert prestigesConfig != null;
        for (String key : prestigesConfig.getKeys(false)) {
            prestiges.add(deserialize(prestigesConfig.getConfigurationSection(key), Integer.parseInt(key)));
        }
    }

    public static Prestige getPrestigeByIndex(int prestigeIndex) {
        if(prestigeIndex == 0) return null;
        Prestige prestigeForIndexInList = prestiges.get(prestigeIndex - 1);
        if(prestigeForIndexInList.index == prestigeIndex) return prestigeForIndexInList;
        for(Prestige prestige : prestiges){
            if(prestige.index == prestigeIndex){
                return prestige;
            }
        }
        return null;
    }
}
