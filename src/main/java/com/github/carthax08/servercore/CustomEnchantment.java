package com.github.carthax08.servercore;

import org.bukkit.Material;

public enum CustomEnchantment {
    FORTUNE(11, 5000, 10000, "Fortune", Material.DIAMOND),
    TOKENATOR(12, 1500, 10, "Tokenator", Material.SUNFLOWER),
    KEY_FINDER(14, 10000, 50, "Key Finder", Material.TRIPWIRE_HOOK),
    EXPLOSION(13, 20000, 1000, "Explosion", Material.TNT),
    JACKHAMMER(15, 20000, 10000, "Jackhammer", Material.ANVIL),
    MULTI_DIRECTIONAL(16, 10000, 5000, "Multi-Directional", Material.RED_STAINED_GLASS),
    HASTE(17, 20000, 3, "Haste", Material.GOLDEN_PICKAXE),
    METAL_DETECTOR(18, 10000, 100, "Metal Detector", Material.HOPPER);


    public final int slot, price, maxLevel;
    public final String displayName;
    public final Material displayMaterial;

    CustomEnchantment(int slot, int price, int maxLevel, String displayName, Material displayMaterial){
        this.slot = slot;
        this.price = price;
        this.maxLevel = maxLevel;
        this.displayName = displayName;
        this.displayMaterial = displayMaterial;
    }
}
