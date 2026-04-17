package pl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Books {
    public static FileConfiguration messages;

    public static void load(FileConfiguration config) {
        messages = config;
    }

    private static List<String> lore(String key) {
        return messages.getStringList("books." + key);
    }


    public static ItemStack createWindManipulationBook() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text("ᴡɪɴᴅ ᴍᴀɴɪᴘᴜʟᴀᴛɪᴏɴ ʙᴏᴏᴋ", NamedTextColor.WHITE));
        meta.setLore(lore("wind-manipulation"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(3);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createHellScorch() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text("ʜᴇʟʟꜱᴄᴏʀᴄʜ ʙᴏᴏᴋ", NamedTextColor.GOLD));
        meta.setLore(lore("hellscorch"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(6);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createDarkSense() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text("ᴅᴀʀᴋ ꜱᴇɴꜱᴇ ʙᴏᴏᴋ", NamedTextColor.GRAY));
        meta.setLore(lore("dark-sense"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(4);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createAdrenalineRush() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text("ᴀᴅʀᴇɴᴀʟɪɴᴇ ʀᴜꜱʜ ʙᴏᴏᴋ", NamedTextColor.LIGHT_PURPLE));
        meta.setLore(lore("adrenaline-rush"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(5);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createRawStrength() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text("ʀᴀᴡ ꜱᴛʀᴇɴɢᴛʜ ʙᴏᴏᴋ", NamedTextColor.RED));
        meta.setLore(lore("raw-strength"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(10);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createThunderCharge() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text("ᴛʜᴜɴᴅᴇʀᴄʜᴀʀɢᴇ ʙᴏᴏᴋ", NamedTextColor.AQUA));
        meta.setLore(lore("thunder-charge"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(11);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createLuckBook() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text("ʟᴜᴄᴋ ʙᴏᴏᴋ", NamedTextColor.GREEN));
        meta.setLore(lore("luck"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(88);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static boolean areSimilar(ItemStack one, ItemStack two) {
        if (one == null || two == null) return false;
        if (one.getType() != two.getType()) return false;

        ItemMeta m1 = one.getItemMeta();
        ItemMeta m2 = two.getItemMeta();

        if (m1 == null && m2 == null) return true;
        if (m1 == null || m2 == null) return false;

        if (!m1.getDisplayName().equals(m2.getDisplayName())) return false;

        if (m1.hasCustomModelData() && m2.hasCustomModelData()) {
            return m1.getCustomModelData() == m2.getCustomModelData();
        }

        return !m1.hasCustomModelData() && !m2.hasCustomModelData();
    }
}
