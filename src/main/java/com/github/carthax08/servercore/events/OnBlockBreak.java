package com.github.carthax08.servercore.events;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.util.DataStore;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        ServerPlayer playerData = DataStore.getPlayerData(event.getPlayer());
        if(playerData.getItemsInBackpack() >= playerData.backpackSize){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Your backpack is full! Use /sellall to sell!");
            return;
        }
        playerData.blocksBroken++;
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(event.getPlayer());
        Location loc = BukkitAdapter.adapt(event.getBlock().getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        query.testState(loc, localPlayer, Flags.BUILD);

        if (!query.testState(loc, localPlayer, Flags.BUILD)) {
            event.setCancelled(true);
            return;
        }
        List<ItemStack> drops = new ArrayList<>();
        if(!playerData.autosmelt){
            drops.addAll(event.getBlock().getDrops());
        }else{
            for (ItemStack item : event.getBlock().getDrops()) {
                ItemStack result = attemptSmelt(item);
                drops.add(result != null ? result : item);
            }
        }
        Random random = new Random();
        if(random.nextInt(1000) >= 990){
            int tokens = Math.max(10, random.nextInt(50));
            playerData.tokenBalance += tokens;
            playerData.savePlayerData(false);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You randomly found " + tokens + " tokens!");

        }
        for(ItemStack item : drops) {
            System.out.println("Adding item " + item + " to backpack");
            playerData.addItemToBackpack(item);
        }
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);

        event.getPlayer().spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(
                        ChatColor.translateAlternateColorCodes('&', Main.backpackBarFormat
                            .replace("%amount%", String.valueOf(playerData.getItemsInBackpack()))
                            .replace("%max%", String.valueOf(playerData.backpackSize))
                        )
                )
        );

    }

    private ItemStack attemptSmelt(ItemStack item) {
        ItemStack result = null;
        Iterator<Recipe> iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            if (!(recipe instanceof FurnaceRecipe)) continue;
            FurnaceRecipe frecipe = (FurnaceRecipe) recipe;
            if ((frecipe.getInput().getType() != item.getType())) continue;
            result = frecipe.getResult();
            break;
        }
        return result;
    }
}
