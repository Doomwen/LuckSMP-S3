package pl.Doom;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import pl.Books;

public enum AbilityMapping {
    WIND_MANIPULATION("§fWind Manipulation", "\uF003") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createWindManipulationBook(); }
    },
    DARK_SENSE("§7Dark Sense", "\uF004") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createDarkSense(); }
    },
    ADRENALINE_RUSH("§dAdrenaline Rush", "\uF005") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createAdrenalineRush(); }
    },
    HELLSCORCH("§6Hellscorch", "\uF006") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createHellScorch(); }
    },
    RAW_STRENGTH("§cRaw Strength", "\uF007") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createRawStrength(); }
    },
    THUNDERCHARGE("§bThundercharge", "\uF008") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createThunderCharge(); }
    },
    ;

    private final String hackName;
    private final String cooldownEmoji;

    AbilityMapping(String hackName, String cooldownEmoji) {
        this.hackName = hackName;
        this.cooldownEmoji = cooldownEmoji;
    }

    public String getHackName() { return hackName; }
    public String getColor() { return ChatColor.getLastColors(hackName); }

    public static final Key FONT = Key.key("minecraft", "luck");

    public Component getCooldownEmoji() {
        return Component.text(cooldownEmoji).font(FONT);
    }

    public abstract boolean matchesItem(ItemStack item);
    public abstract ItemStack createItem();

    public static AbilityMapping fromItem(ItemStack item) {
        for (AbilityMapping mapping : values()) {
            if (mapping.matchesItem(item)) return mapping;
        }
        return null;
    }
}
