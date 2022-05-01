package com.github.carthax08.servercore.data;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.files.DataFileHandler;
import com.github.carthax08.servercore.prestige.Prestige;
import com.github.carthax08.servercore.prestige.PrestigeHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ServerPlayer {
    public Player player;
    public double tokenBalance;
    public Prestige prestige;
    public int pindex;
    public boolean autosmelt;
    public YamlConfiguration config;

    public ServerPlayer(Player _player, Double _tokenBalance, int _prestigeIndex, boolean _autosmelt, YamlConfiguration _config){
        player = _player;
        tokenBalance = _tokenBalance;
        if(!PrestigeHandler.prestiges.isEmpty()) {
            prestige = PrestigeHandler.getPrestigeByIndex(_prestigeIndex);
        }
        config = _config;
        pindex = _prestigeIndex;
        autosmelt = _autosmelt;
    }

    public void savePlayerData(Boolean toFile){
        config.set("tokens", tokenBalance);
        if(prestige != null) {
            config.set("prestige", pindex);
        }
        if(toFile){
            try {
                DataFileHandler.savePlayerDataToFile(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public double getMoney(){
        return Main.getEcon().getBalance(player);
    }
}
