package com.github.carthax08.servercore;

import org.bukkit.Material;

public enum CustomEnchantment {
    FORTUNE(2, 5000, 10000, "Fortune", "Increase the number of blocks you get from mining!", Material.DIAMOND),
    TOKENATOR(4, 1500, 10, "Tokenator", "Increase your chances of finding tokens while mining!", Material.SUNFLOWER),
    KEY_FINDER(13, 10000, 50, "Key Finder", "Have a chance to find Keys while mining!", Material.TRIPWIRE_HOOK),
    EXPLOSION(20, 20000, 1000, "Explosion", "Have a chance to cause an explosion while mining", Material.TNT),
    JACKHAMMER(6, 20000, 10000, "Jackhammer", "Have a chance to clear an entire layer of the mine!",Material.ANVIL),
    MULTI_DIRECTIONAL(24, 10000, 5000, "Multi-Directional", "Have a chance to mine a cross (+) in the mine!", Material.RED_STAINED_GLASS),
    LASER(15, 10000, 10000, "Laser", "Have a chance to mine a straight line in the direction you're facing!", Material.RED_STAINED_GLASS_PANE),
    HASTE(11, 20000, 3, "Haste", "Give yourself the Vanilla Haste status effect while holding!", Material.GOLDEN_PICKAXE),
    METAL_DETECTOR(22, 10000, 100, "Metal Detector", "Chance to get a 2x boost for a random amount of time while mining!", Material.HOPPER);


    public final int slot, price, maxLevel;
    public final String displayName;
    public final Material displayMaterial;
    public final String description;

    CustomEnchantment(int slot, int price, int maxLevel, String displayName, String description, Material displayMaterial){
        this.slot = slot;
        this.price = price;
        this.maxLevel = maxLevel;
        this.displayName = displayName;
        this.displayMaterial = displayMaterial;
        this.description = description;
    }
}
