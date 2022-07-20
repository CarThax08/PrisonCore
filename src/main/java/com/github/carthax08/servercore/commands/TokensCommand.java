package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.util.DataStore;
import com.github.carthax08.servercore.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TokensCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0 && sender instanceof Player){
            sender.sendMessage(ChatColor.GREEN + "NovaCoins: " + Util.format(DataStore.getPlayerData((Player) sender).tokenBalance));
            return true;
        }
        if(!sender.hasPermission("novacoins.edit")){return true;}
        if(args.length == 2 && args[0].equalsIgnoreCase("clear")){
            handleClear(args, sender);
        }else if(args.length == 3){
            switch (args[0].toLowerCase()){
                case "set":
                    handleSet(args, sender);
                    break;
                case "add":
                    handleAdd(args, sender);
                    break;
                case "remove":
                    handleRemove(args, sender);
                    break;
            }
        }else{
            sender.sendMessage(ChatColor.RED + command.getUsage());
        }
        return true;
    }

    private void handleSet(String[] args, CommandSender sender) {
        Player player = Bukkit.getPlayer(args[1]);
        if(player != null) {
            DataStore.getPlayerData(player).tokenBalance = Double.parseDouble(args[2]);
            DataStore.getPlayerData(player).savePlayerData(false);
            sender.sendMessage(ChatColor.GREEN + "Successfully set " + player.getDisplayName() + "'s balance to " + ChatColor.GREEN + args[2] + " NovaCoins");
        }else{
            sender.sendMessage(ChatColor.RED + "That player does not exist!");
        }
    }
    private void handleAdd(String[] args, CommandSender sender) {
        Player player = Bukkit.getPlayer(args[1]);
        if(player != null) {
            double amount = Double.parseDouble(args[2]);
            DataStore.getPlayerData(player).tokenBalance += amount;
            DataStore.getPlayerData(player).savePlayerData(false);
            sender.sendMessage(ChatColor.GREEN + "Successfully added " + args[2] + " NovaCoins to " + player.getDisplayName() + ChatColor.GREEN + "'s balance");
        }else{
            sender.sendMessage(ChatColor.RED + "That player does not exist!");
        }
    }
    private void handleRemove(String[] args, CommandSender sender) {
        Player player = Bukkit.getPlayer(args[1]);
        if(player != null) {
            double amount = Double.parseDouble(args[2]);
            DataStore.getPlayerData(player).tokenBalance -= amount;
            DataStore.getPlayerData(player).savePlayerData(false);
            sender.sendMessage(ChatColor.GREEN + "Successfully removed " + args[2] + " NovaCoins from " + player.getDisplayName() + ChatColor.GREEN + "'s balance");
        }else{
            sender.sendMessage(ChatColor.RED + "That player does not exist!");
        }
    }
    private void handleClear(String[] args, CommandSender sender) {
        Player player = Bukkit.getPlayer(args[1]);
        if(player != null) {
            DataStore.getPlayerData(player).tokenBalance = 0;
            sender.sendMessage(ChatColor.GREEN + "Successfully cleared " + player.getDisplayName() + ChatColor.GREEN + "'s balance");
            DataStore.getPlayerData(player).savePlayerData(false);
        }else{
            sender.sendMessage(ChatColor.RED + "That player does not exist!");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        if(sender.hasPermission("tokens.edit") && args.length == 0){
            tabComplete.add("add");
            tabComplete.add("set");
            tabComplete.add("remove");
            tabComplete.add("clear");
        }
        if(sender.hasPermission("tokens.edit") && args.length == 1){
            for (Player player : Bukkit.getOnlinePlayers()){
                tabComplete.add(player.getName());
            }
        }
        return tabComplete;
    }
}