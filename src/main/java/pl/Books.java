package pl;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
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
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§fᴡɪɴᴅ ᴍᴀɴɪᴘᴜʟᴀᴛɪᴏɴ ʙᴏᴏᴋ");
        meta.setLore(lore("wind-manipulation"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(3);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createHellScorch() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§6ʜᴇʟʟꜱᴄᴏʀᴄʜ ʙᴏᴏᴋ");
        meta.setLore(lore("hellscorch"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(6);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createDarkSense() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§7ᴅᴀʀᴋ ꜱᴇɴꜱᴇ ʙᴏᴏᴋ");
        meta.setLore(lore("dark-sense"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(4);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createAdrenalineRush() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§dᴀᴅʀᴇɴᴀʟɪɴᴇ ʀᴜꜱʜ ʙᴏᴏᴋ");
        meta.setLore(lore("adrenaline-rush"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(5);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createRawStrength() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§cʀᴀᴡ ꜱᴛʀᴇɴɢᴛʜ ʙᴏᴏᴋ");
        meta.setLore(lore("raw-strength"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(10);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createThunderCharge() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§bᴛʜᴜɴᴅᴇʀᴄʜᴀʀɢᴇ ʙᴏᴏᴋ");
        meta.setLore(lore("thunder-charge"));
        meta.setEnchantmentGlintOverride(false);
        meta.setCustomModelData(11);
        meta.setFireResistant(true);
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createLuckBook() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§aʟᴜᴄᴋ ʙᴏᴏᴋ");
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
