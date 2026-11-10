package pl.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import pl.Doom.AbilityMapping;
import pl.Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AbilityManager implements Listener {

    public static File file;
    public static FileConfiguration config;
    public static HashMap<UUID, AbilityMapping> BOOK = new HashMap<>();
    public static final boolean NIMLOT = Main.getInstance().getConfig().getBoolean("nimlot", false);

    @EventHandler
    public void enable(PluginEnableEvent event) {
        if (!event.getPlugin().equals(Main.getInstance())) return;
        loadConfig();

        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                AbilityMapping ability = AbilityMapping.valueOf(config.getString(key));

                BOOK.put(uuid, ability);
            } catch (Exception ignored) {

            }
        }
    }

    public AbilityManager() {
        file = new File(Main.getInstance().getDataFolder(), "abilities.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadConfig();
    }

    private long lastModified = 0;

    public void loadConfig() {
        long fileLastModified = file.lastModified();
        if (fileLastModified == lastModified && config != null) {
            return;
        }
        config = YamlConfiguration.loadConfiguration(file);
        lastModified = fileLastModified;
    }

    public static void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setAbility(UUID playerUUID, AbilityMapping hackName) {
        BOOK.put(playerUUID, hackName);
        if (NIMLOT)return;
        if (hackName != null) {
            config.set(playerUUID + "", hackName.toString());
        } else {
            config.set(playerUUID + "", null);
        }
        saveConfig();
    }

    public AbilityMapping getAbility(UUID uuid) {
        return BOOK.getOrDefault(uuid, null);
    }
}
