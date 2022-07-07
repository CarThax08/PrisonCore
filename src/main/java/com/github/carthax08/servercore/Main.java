package com.github.carthax08.servercore;

import com.github.carthax08.servercore.commands.*;
import com.github.carthax08.servercore.data.files.CratesFileHandler;
import com.github.carthax08.servercore.data.files.DataFileHandler;
import com.github.carthax08.servercore.data.files.PricesFileHandler;
import com.github.carthax08.servercore.data.files.RanksFileHandler;
import com.github.carthax08.servercore.events.GUIClickEvent;
import com.github.carthax08.servercore.events.OnBlockBreak;
import com.github.carthax08.servercore.events.OnPlayerJoin;
import com.github.carthax08.servercore.events.OnPlayerLeave;
import com.github.carthax08.servercore.placeholders.PluginPlaceholderExpansion;
import com.github.carthax08.servercore.prestige.PrestigeHandler;
import com.github.carthax08.servercore.rankup.RankHandler;
import com.github.carthax08.servercore.util.DataStore;
import com.github.carthax08.servercore.util.PlayerDataHandler;
import com.github.carthax08.servercore.util.Util;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


public final class Main extends JavaPlugin {

    private static Main instance;
    private static LuckPerms perms;
    private static Economy econ;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(false);
        saveDefaultConfig();
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<LuckPerms> permsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6PrisonCore&7] Prison Core is starting. Please wait."));
        instance = this;
        if(economyProvider != null) {
            econ = economyProvider.getProvider();
        }
        if(permsProvider != null){
            perms = permsProvider.getProvider();
        }
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PluginPlaceholderExpansion(this).register();
        }

        DataStore.lastRankGroup = getConfig().getString("settings.lastRankGroupName");

        registerCommands();
        registerEvents();
        registerConfigurations();
        PrestigeHandler.loadPrestiges((YamlConfiguration) getConfig());
        RankHandler.loadRanks();
        try {
            loadAlreadyOnlinePlayers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadAlreadyOnlinePlayers() throws IOException {
        for (Player player : Bukkit.getOnlinePlayers()){
            Util.loadPlayerData(player);
        }
    }

    private void registerConfigurations() {
        CratesFileHandler.loadOrCreate();
        PricesFileHandler.loadOrCreate();
        RanksFileHandler.loadOrCreate();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new GUIClickEvent(), this);
    }

    private void registerCommands() {
        getCommand("autosmelt").setExecutor(new AutoSmeltCommand());
        getCommand("prestige").setExecutor(new PrestigeCommand());
        getCommand("tokens").setExecutor(new TokensCommand());
        getCommand("tokenshop").setExecutor(new CratesCommand());
        getCommand("sellall").setExecutor(new SellCommand());
        getCommand("vote").setExecutor(new VoteCommand());
        getCommand("rankup").setExecutor(new RankupCommand());
        //getCommand("enchants").setExecutor(new EnchantsCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(Player player : Bukkit.getOnlinePlayers()){

            DataStore.getPlayerData(player).savePlayerData(true);
            DataStore.playerData.remove(player);
        }
    }

    public static Main getInstance() {
        return instance;
    }
    public static Economy getEcon(){
        return econ;
    }
    public static LuckPerms getPerms(){
        return perms;
    }
}
