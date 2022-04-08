package com.github.carthax08.servercore.commands.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TokensTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tabComplete = new ArrayList<String>();
        if(sender.hasPermission("tokens.edit") && args.length == 0){
            tabComplete.add("add");
            tabComplete.add("set");
            tabComplete.add("remove");
            tabComplete.add("clear");
        }
        return tabComplete;
    }
}
