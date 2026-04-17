package pl.Doom;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.inventory.ItemStack;
import pl.Books;

public enum AbilityMapping {
    WIND_MANIPULATION(Component.text("Wind Manipulation", NamedTextColor.WHITE), "\uE912") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createWindManipulationBook(); }
    },
    DARK_SENSE(Component.text("Dark Sense", NamedTextColor.GRAY), "\uE913") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createDarkSense(); }
    },
    ADRENALINE_RUSH(Component.text("Adrenaline Rush", NamedTextColor.LIGHT_PURPLE), "\uE914") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createAdrenalineRush(); }
    },
    HELLSCORCH(Component.text("Hellscorch", NamedTextColor.GOLD), "\uE916") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createHellScorch(); }
    },
    RAW_STRENGTH(Component.text("Raw Strength", NamedTextColor.RED), "\uE919") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createRawStrength(); }
    },
    THUNDERCHARGE(Component.text("Thundercharge", NamedTextColor.AQUA), "\uE920") {
        @Override public boolean matchesItem(ItemStack item) { return item.isSimilar(createItem()); }
        @Override public ItemStack createItem() { return Books.createThunderCharge(); }
    },
    ;

    private final Component hackName;
    private final String cooldownEmoji;

    AbilityMapping(Component hackName, String cooldownEmoji) {
        this.hackName = hackName;
        this.cooldownEmoji = cooldownEmoji;
    }

    public Component getHackName() { return hackName; }
    public TextColor getColor() { return hackName.color(); }
    public String getCooldownEmoji() { return cooldownEmoji; }

    public abstract boolean matchesItem(ItemStack item);
    public abstract ItemStack createItem();

    public static AbilityMapping fromItem(ItemStack item) {
        for (AbilityMapping mapping : values()) {
            if (mapping.matchesItem(item)) return mapping;
        }
        return null;
    }
}
