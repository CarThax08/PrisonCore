package com.github.carthax08.servercore.data;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.commands.SellCommand;
import com.github.carthax08.servercore.data.files.DataFileHandler;
import com.github.carthax08.servercore.data.files.PricesFileHandler;
import com.github.carthax08.servercore.prestige.Prestige;
import com.github.carthax08.servercore.prestige.PrestigeHandler;
import com.github.carthax08.servercore.rankup.Rank;
import com.github.carthax08.servercore.rankup.RankHandler;
import com.github.carthax08.servercore.util.Util;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;

public class ServerPlayer {
    public Player player;
    public double tokenBalance;
    public Prestige prestige;
    public int pindex;
    public boolean autosmelt;
    public YamlConfiguration config;
    public double sellMultiplier;
    public double tempSellMultiplier = 0;
    public Rank rank;
    public boolean autosell;
    public int blocksBroken;

    public ArrayList<ItemStack> backpack;
    public int backpackSize;
    public boolean upgradeDebounce;

    public ServerPlayer(Player _player, Double _tokenBalance, int _prestigeIndex, boolean _autosell, YamlConfiguration _config, double _sellMultiplier, Rank _rank, ArrayList<ItemStack> _backpack, int _backpackSize) {
        player = _player;
        tokenBalance = _tokenBalance;
        if (!PrestigeHandler.prestiges.isEmpty()) {
            prestige = PrestigeHandler.getPrestigeByIndex(_prestigeIndex);
        }
        config = _config;
        pindex = _prestigeIndex;
        autosell = _autosell;
        sellMultiplier = _sellMultiplier;
        rank = _rank;
        backpack = _backpack;
        backpackSize = _backpackSize;
        upgradeDebounce = false;
    }

    public void savePlayerData(Boolean toFile) {
        config.set("tokens", tokenBalance);
        if (prestige != null) {
            config.set("prestige", pindex);
        }
        config.set("autosell", autosell);
        config.set("multiplier", sellMultiplier);
        config.set("rank", RankHandler.getIndex(rank));
        config.set("backpack", backpack);
        config.set("backpackSize", backpackSize);
        if (toFile) {
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

    public boolean removeMoney(double amount) {
        return Main.getEcon().withdrawPlayer(player, amount).type == EconomyResponse.ResponseType.SUCCESS;
    }

    public boolean addMoney(double amount) {
        return Main.getEcon().depositPlayer(player, amount).type == EconomyResponse.ResponseType.SUCCESS;
    }

    public int getItemsInBackpack() {
        int items = 0;
        for (ItemStack bpItem : backpack) {
            items += bpItem.getAmount();
        }
        return items;
    }

    public void addItemToBackpack(ItemStack item) {
        int items = getItemsInBackpack();
        if (!PricesFileHandler.pricesConfig.isSet(item.getType().name().toLowerCase())) {
            player.getInventory().addItem(item);
            return;
        }
        if (items + item.getAmount() < backpackSize) {
            for (ItemStack item2 : backpack) {
                if (item2.getType() == item.getType()) {
                    item2.setAmount(item2.getAmount() + item.getAmount());
                    return;
                }
            }
            backpack.add(item);
            return;
        } else if (items + item.getAmount() == backpackSize) {
            for (ItemStack item2 : backpack) {
                if (item2.getType() == item.getType()) {
                    item2.setAmount(item2.getAmount() + item.getAmount());
                    return;
                }
            }
            backpack.add(item);
            return;
        }else if(items <= backpackSize){
            int amountToAdd = backpackSize - items;
            item.setAmount(amountToAdd);
            backpack.add(item);
            return;
        }
    }

    public Rank getNextRank() {
        return RankHandler.getNextRank(rank);
    }
}
