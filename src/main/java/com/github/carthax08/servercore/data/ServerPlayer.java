package com.github.carthax08.servercore.data;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.files.DataFileHandler;
import com.github.carthax08.servercore.prestige.Prestige;
import com.github.carthax08.servercore.prestige.PrestigeHandler;
import net.milkbowl.vault.economy.EconomyResponse;
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
    public double sellMultiplier;
    public ServerPlayer(Player _player, Double _tokenBalance, int _prestigeIndex, boolean _autosmelt, YamlConfiguration _config, double _sellMultiplier){
        player = _player;
        tokenBalance = _tokenBalance;
        if(!PrestigeHandler.prestiges.isEmpty()) {
            prestige = PrestigeHandler.getPrestigeByIndex(_prestigeIndex);
        }
        config = _config;
        pindex = _prestigeIndex;
        autosmelt = _autosmelt;
        sellMultiplier = _sellMultiplier;
    }

    public void savePlayerData(Boolean toFile){
        config.set("tokens", tokenBalance);
        if(prestige != null) {
            config.set("prestige", pindex);
        }
        config.set("autosmelt", autosmelt);
        config.set("multiplier", sellMultiplier);
        if(toFile){
            try {
                DataFileHandler.savePlayerDataToFile(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public double getMoney() {
        return Main.getEcon().getBalance(player);
    }
    public boolean removeMoney(double amount){
        return Main.getEcon().withdrawPlayer(player, amount).type == EconomyResponse.ResponseType.SUCCESS;
    }

    public boolean addMoney(double amount) {
        return Main.getEcon().depositPlayer(player, amount).type == EconomyResponse.ResponseType.SUCCESS;
    }
}
