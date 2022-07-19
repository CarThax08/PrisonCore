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
        ServerPlayer playerData = DataStore.getPlayerData((Player) sender);
        switch (args[0].toLowerCase()) {
            case ("tokens"):
                int tokenamount;
                try {
                    tokenamount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "You must have a valid number as your second argument!");
                    return true;
                }
                ItemStack tokenitem = new ItemStack(Material.PAPER);
                ItemMeta tokenmeta = tokenitem.getItemMeta();
                tokenmeta.setDisplayName(ChatColor.GREEN + "Token Banknote");
                if (playerData.tokenBalance < tokenamount) {
                    sender.sendMessage("You are too broke.");
                    return true;
                } else {
                    playerData.tokenBalance = playerData.tokenBalance - tokenamount;
                    playerData.savePlayerData(false);
                }
                tokenmeta.setLore(Arrays.asList("Token Amount: " + tokenamount));
                PersistentDataContainer tokendata = tokenmeta.getPersistentDataContainer();
                NamespacedKey tokenamountkey = new NamespacedKey(main, "amount");
                NamespacedKey tokentypekey = new NamespacedKey(main, "type");
                tokendata.set(tokenamountkey, PersistentDataType.INTEGER, tokenamount);
                tokendata.set(tokentypekey, PersistentDataType.STRING, "token");
                tokenitem.setItemMeta(tokenmeta);
                ((Player) sender).getInventory().addItem(tokenitem);
                break;
            case ("balance"):
                int amount;
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "You must have a valid number as your second argument!");
                    return true;
                }
                ItemStack balanceitem = new ItemStack(Material.PAPER);
                ItemMeta balancemeta = balanceitem.getItemMeta();
                balancemeta.setDisplayName(ChatColor.GREEN + "Balance Banknote");
                if (playerData.tokenBalance < amount) {
                    sender.sendMessage("You are too broke.");
                    return true;
                } else {
                    playerData.removeMoney(amount);
                    playerData.savePlayerData(false);
                }
                balancemeta.setLore(Arrays.asList("Balance Amount: " + amount));
                PersistentDataContainer balancedata = balancemeta.getPersistentDataContainer();
                NamespacedKey balanceamountkey = new NamespacedKey(main, "amount");
                NamespacedKey balancetypekey = new NamespacedKey(main, "type");
                balancedata.set(balanceamountkey, PersistentDataType.INTEGER, amount);
                balancedata.set(balancetypekey, PersistentDataType.STRING, "balance");
                balanceitem.setItemMeta(balancemeta);
                ((Player) sender).getInventory().addItem(balanceitem);
                break;
            default:
                sender.sendMessage("You need to have either balance or tokens as a first argument!");
        }
        return false;
    }
}
