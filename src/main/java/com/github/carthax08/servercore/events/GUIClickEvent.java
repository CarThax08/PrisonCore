package com.github.carthax08.servercore.events;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.data.ServerPlayer;
import com.github.carthax08.servercore.util.DataStore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class GUIClickEvent implements Listener {
    @EventHandler
    public void guiClickEvent(InventoryClickEvent event){
        // Check if a Player clicked the GUI
        if(!(event.getWhoClicked() instanceof Player)){
            return;
        }
        // Check if it's the NovaCoin Shop
        if(event.getView().getTitle().equalsIgnoreCase("NovaCoin Shop")){
            event.setCancelled(true);
            // Get the item that they clicked on
            ItemStack item = event.getInventory().getItem(event.getSlot());
            // Get their PlayerData
            ServerPlayer player = DataStore.getPlayerData((Player) event.getWhoClicked());
            //Check if the item has a command set for it
            if(DataStore.cratesGuiCommands.containsKey(item) && !player.upgradeDebounce){
                // Temp debug message
                player.player.sendMessage("Purchasing...");
                //this whole for loop is to get the cost and deduct it from the player's NovaCoin balance.
                for(String string : item.getItemMeta().getLore()){
                    if(!string.contains("Cost:")){
                        continue;
                    }
                    int cost = Integer.parseInt(string.replace(ChatColor.GOLD + "Cost: ", ""));
                    if(!(player.tokenBalance >= cost)){
                        player.player.playSound(player.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5f, 5f);
                        event.getWhoClicked().sendMessage(ChatColor.RED + "You do not have enough NovaCoins for that!");
                        return;
                    }
                    player.tokenBalance -= cost;
                }

                // Run the command
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), DataStore.cratesGuiCommands.get(item));
                player.player.playSound(player.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5f, 5f);
                //Set debounce
                player.upgradeDebounce = true;

                // Create a new async task that will disable the debounce after 5 server ticks (20 per second)
                new BukkitRunnable() {
                    @Override
                    public void run(){
                        player.upgradeDebounce = false;
                    }
                }.runTaskLaterAsynchronously(Main.getInstance(), 5);
            }
        }
    }

    /*
    May implement later.
    private void handleEnchant(CustomEnchantment enchant, Player player) {
        ServerPlayer playerData = DataStore.getPlayerData(player);
        if(playerData.tokenBalance <= enchant.price){
            player.sendMessage(ChatColor.RED + "You do not have enough tokens for this enchant!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5f, 5f);
            return;
        }
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if(itemInMainHand.getItemMeta().hasLore() && itemInMainHand.getItemMeta().getLore().contains(ChatColor.RESET + enchant.displayName + " " + enchant.maxLevel)) return;
        playerData.tokenBalance -= enchant.price;
        ItemMeta meta = itemInMainHand.getItemMeta();
        List<String> lore = new ArrayList<String>();
        boolean success = false;
        if(meta.hasLore()) {
            lore = meta.getLore();
            for (String string : meta.getLore()) {
                if (string.contains(enchant.displayName)) {
                    if (string.contains("Token") && !enchant.displayName.contains("Token")) continue;
                    assert lore != null;
                    int index = lore.indexOf(string);
                    lore.remove(string);
                    String[] parts = string.split(" ");
                    int oldLevel = 0;
                    if (string.contains("Token")) {
                        oldLevel = Integer.parseInt(parts[2]);
                    } else {
                        oldLevel = Integer.parseInt(parts[1]);
                    }
                    parts[1] = String.valueOf(oldLevel + 1);
                    lore.add(index, parts[0] + parts[1] + (parts.length > 2 ? parts[2] : ""));
                    success = true;
                    break;
                }
            }
        }
        if(!success){
            lore.add(ChatColor.RESET + enchant.displayName + " 1");
        }
        meta.setLore(lore);
        itemInMainHand.setItemMeta(meta);
        player.sendMessage(ChatColor.GREEN + "Successfully enchanted item for " + enchant.price + " tokens!");
    }
     */
}
