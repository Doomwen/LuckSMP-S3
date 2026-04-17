package pl.Doom;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.Main;
import pl.managers.CooldownManager;

import java.util.UUID;

public class ActionBarUpdater extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();

            AbilityMapping book = Main.getAbilityManager().getAbility(uuid);

            if (book == null)return;

            TextColor color = book.getColor();
            Component msg = Component.text(formatTime(CooldownManager.getCooldownTimeLeft(uuid, "1")), color);
            if (CooldownManager.isOnCooldown(uuid, "2")) {
                msg = msg.append(Component.text("    |    ", NamedTextColor.GRAY));
                msg = msg.append(Component.text(formatTime(CooldownManager.getCooldownTimeLeft(uuid, "2")), color));
            }

            player.sendActionBar(msg);
        }
    }

    private String formatTime(long totalMS) {
        long seconds = totalMS / 1000;
        if (seconds <= 0){
            return "";
        }
        return seconds + "s";
    }
}
