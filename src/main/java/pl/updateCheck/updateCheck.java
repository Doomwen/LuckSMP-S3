package pl.updateCheck;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import pl.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class updateCheck implements Listener {
    public static String plugName = Main.getInstance().getName();

    @EventHandler
    public void join(PlayerJoinEvent event){
        if (event.getPlayer().isOp()){
            checkForUpdate(event.getPlayer());
        }
    }

    @EventHandler
    public void reload(PluginEnableEvent event){
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                checkForUpdate(p);
            }
        }
    }

    public void checkForUpdate(Player player) {
        Bukkit.getAsyncScheduler().runNow(Main.getInstance(), task -> {
            try {
                String updateURL = "https://gist.githubusercontent.com/Doomwen/407b6dc6cf7e8d23432b670045523226/raw/updates.txt?t=" + System.currentTimeMillis();
                URL url = new URL(updateURL);

                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String latestVersion = null;
                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    if (line.startsWith(plugName + ":")) {
                        latestVersion = line.split(":")[1].trim();
                        break;
                    }
                }

                reader.close();

                final String finalLatestVersion = latestVersion;

                String currentVersion = Main.getInstance().getDescription().getVersion();

                if (!latestVersion.equalsIgnoreCase(currentVersion)) {
                    Bukkit.getRegionScheduler().execute(Main.getInstance(), player.getLocation(), () -> {
                        player.sendMessage(Component.text("[" + plugName + "] A new version is available: v" + finalLatestVersion, NamedTextColor.GOLD));
                    });
                }
            } catch (Exception e) {
                Main.getInstance().getLogger().warning("Failed to check for updates: " + e.getMessage());
            }
        });
    }
}
