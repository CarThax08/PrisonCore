package com.github.carthax08.servercore.commands;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.rankup.Rank;
import com.github.carthax08.servercore.rankup.RankHandler;
import com.github.carthax08.servercore.util.DataStore;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You must be a player to do that!");
            return true;
        }
        Player player = (Player) sender;
        ServerPlayer playerData = DataStore.getPlayerData(player);

        Rank playerRank = playerData.rank;
        Rank nextRank = RankHandler.getNextRank(playerRank);

        if(!(playerData.getMoney() >= nextRank.cost)){
            player.sendMessage(ChatColor.RED + "You do not have enough money for that! You need " + (nextRank.cost - playerData.getMoney()) + " more to rankup!");
            return true;
        }

        if(!playerData.removeMoney(nextRank.cost)){
            player.sendMessage(ChatColor.RED + "An unknown error has occurred while trying to process your request. Please let an admin know!");
            return true;
        }

        playerData.rank = nextRank;

        LuckPerms perms = Main.getPerms();
        User user = perms.getPlayerAdapter(Player.class).getUser(player);
        user.data().add(Node.builder("group." + nextRank.groupName).build());
        perms.getUserManager().saveUser(user);

        player.sendMessage(ChatColor.GREEN + "You have successfully ranked up to " + nextRank.name + ".");

        return true;
    }
}
