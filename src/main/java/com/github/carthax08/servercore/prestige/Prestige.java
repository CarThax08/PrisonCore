package com.github.carthax08.servercore.prestige;

import org.bukkit.configuration.file.YamlConfiguration;

public class Prestige {

    public String[] permissions;
    public double cost;
    public int index;
    public Prestige(double cost, String[] permissions, int index){
        this.cost = cost;
        this.permissions = permissions;
        this.index = index;

    }

    public void saveToConfig(YamlConfiguration config){
        config.set("prestige." + index + "cost", cost);
        config.set("prestige." + index + "permissions", permissions);
    }
}
