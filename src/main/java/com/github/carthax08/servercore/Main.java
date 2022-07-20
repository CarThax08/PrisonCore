package com.github.carthax08.servercore;

import com.github.carthax08.servercore.commands.*;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.data.files.*;
import com.github.carthax08.servercore.events.*;
import com.github.carthax08.servercore.placeholders.PluginPlaceholderExpansion;
import com.github.carthax08.servercore.prestige.PrestigeHandler;
import com.github.carthax08.servercore.rankup.RankHandler;
import com.github.carthax08.servercore.util.DataStore;
import com.github.carthax08.servercore.util.PlayerDataHandler;
import com.github.carthax08.servercore.util.Util;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.text.NumberFormat;


public final class Main extends JavaPlugin {

    private static Main instance;
    private static LuckPerms perms;
    private static Economy econ;

    private static FileConfiguration config;

    public static String backpackBarFormat = "";

    private static FileConfiguration getConfigFile(){
        return config;
    }

    public static void reloadPlugin() {
        registerConfigurations();
        DataStore.lastRankGroup = getConfigFile().getString("settings.lastRankGroupName");
        backpackBarFormat = getConfigFile().getString("settings.backpackBarFormat");
        for (Player player : Bukkit.getOnlinePlayers()){
            if(!DataStore.playerData.containsKey(player)){
                try {
                    DataStore.playerData.put(player, PlayerDataHandler.loadPlayerData(DataFileHandler.loadOrCreatePlayerFile(player), player));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(false);
        saveDefaultConfig();
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<LuckPerms> permsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7[&6PrisonCore&7] Prison Core is starting. Please wait."));
        instance = this;
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }
        if (permsProvider != null) {
            perms = permsProvider.getProvider();
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
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
        new BukkitRunnable() {
            @Override
            public void run() {
                // What you want to schedule goes here
                NumberFormat format = NumberFormat.getNumberInstance();
                for (Player player : Bukkit.getOnlinePlayers()){
                    ServerPlayer playerData = DataStore.getPlayerData(player);
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            new TextComponent(
                                    ChatColor.translateAlternateColorCodes('&', Main.backpackBarFormat
                                            .replace("%amount%", format.format(playerData.getItemsInBackpack()))
                                            .replace("%max%", format.format(playerData.backpackSize))
                                    )
                            )
                    );
                }
            }

        }.runTaskTimer(this, 0, 40);
        backpackBarFormat = getConfig().getString("settings.backpackBarFormat");
        config = getConfig();
    }

    private static void loadAlreadyOnlinePlayers() throws IOException {
        for (Player player : Bukkit.getOnlinePlayers()){
            Util.loadPlayerData(player);
        }
    }

    private static void registerConfigurations() {
        CratesFileHandler.loadOrCreate();
        PricesFileHandler.loadOrCreate();
        RanksFileHandler.loadOrCreate();
        BlocksFileHandler.loadOrCreate();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new GUIClickEvent(), this);
        getServer().getPluginManager().registerEvents(new ClickEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerSwitchItemEvent(), this);
    }

    private void registerCommands() {
        getCommand("autosell").setExecutor(new AutoSellCommand());
        getCommand("prestige").setExecutor(new PrestigeCommand());
        getCommand("novacoins").setExecutor(new TokensCommand());
        getCommand("coinshop").setExecutor(new CratesCommand());
        getCommand("sellall").setExecutor(new SellCommand());
        getCommand("vote").setExecutor(new VoteCommand());
        getCommand("rankup").setExecutor(new RankupCommand());
        getCommand("backpacksize").setExecutor(new BackpackSizeCommand());
        getCommand("prisoncorereload").setExecutor(new ReloadCommand());
        getCommand("multiplier").setExecutor(new MultiplierCommand());
        getCommand("withdraw").setExecutor(new WithdrawCommand(this));
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
