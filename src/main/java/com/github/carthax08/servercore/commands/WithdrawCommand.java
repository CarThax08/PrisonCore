package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.util.DataStore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class WithdrawCommand implements CommandExecutor {

    Main main;
    public WithdrawCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {sender.sendMessage(ChatColor.RED + "This command must be ran by a player!"); return true;}
        if (args.length != 2) {return false;}
        Player player = (Player) sender;
        ServerPlayer playerData = DataStore.getPlayerData(player);
        NamespacedKey amountKey = new NamespacedKey(main, "amount");
        NamespacedKey typeKey = new NamespacedKey(main, "type");
        switch (args[0].toLowerCase()) {
            case ("tokens"):
                int tokenamount;
                try {
                    tokenamount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "You must have a valid number as your second argument!");
                    return true;
                }
                if (playerData.tokenBalance < tokenamount) {
                    sender.sendMessage(ChatColor.RED + "You don't have enough tokens for that!");
                    return true;
                } else {
                    playerData.tokenBalance = playerData.tokenBalance - tokenamount;
                    playerData.savePlayerData(false);
                }
                ItemStack tokenItem = new ItemStack(Material.PAPER);
                ItemMeta tokenMeta = tokenItem.getItemMeta();
                tokenMeta.setDisplayName(ChatColor.GREEN + "Token Banknote");
                tokenMeta.setLore(Arrays.asList("Token Amount: " + tokenamount));
                PersistentDataContainer tokenData = tokenMeta.getPersistentDataContainer();
                tokenData.set(amountKey, PersistentDataType.INTEGER, tokenamount);
                tokenData.set(typeKey, PersistentDataType.STRING, "token");
                tokenItem.setItemMeta(tokenMeta);
                (player).getInventory().addItem(tokenItem);
                sender.sendMessage(ChatColor.GREEN + "Success!");
                break;
            case ("balance"):
                int amount;
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "You must have a valid number as your second argument!");
                    return true;
                }
                if (playerData.tokenBalance < amount) {
                    sender.sendMessage(ChatColor.RED + "You don't have enough money for that!");
                    return true;
                } else {
                    playerData.removeMoney(amount);
                    playerData.savePlayerData(false);
                }
                ItemStack balanceItem = new ItemStack(Material.PAPER);
                ItemMeta balanceMeta = balanceItem.getItemMeta();
                balanceMeta.setDisplayName(ChatColor.GREEN + "Balance Banknote");
                balanceMeta.setLore(Arrays.asList("Balance Amount: " + amount));
                PersistentDataContainer balanceData = balanceMeta.getPersistentDataContainer();
                balanceData.set(amountKey, PersistentDataType.INTEGER, amount);
                balanceData.set(typeKey, PersistentDataType.STRING, "balance");
                balanceItem.setItemMeta(balanceMeta);
                player.getInventory().addItem(balanceItem);
                sender.sendMessage(ChatColor.GREEN + "Success!");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "You need to have either balance or tokens as a first argument!");
        }
        return true;
    }
}
