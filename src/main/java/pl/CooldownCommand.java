package pl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.managers.CooldownManager;

public class CooldownCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player target;
        if (args.length == 0) {
            if (sender instanceof Player player) {
                target = player;
            } else {
                sender.sendMessage(Component.text("You are not a player", NamedTextColor.RED));
                return true;
            }
        } else {
            target = Bukkit.getPlayer(args[0]);
        }

        if (target != null && target.isOnline()) {
            CooldownManager.removeAllCooldowns(target.getUniqueId());
            sender.sendMessage(Component.text("Cooldowns cleared for " + target.getName() + "!", NamedTextColor.GREEN));
        } else {
            sender.sendMessage(Component.text(args[0] + " is not online.", NamedTextColor.RED));
        }

        return true;
    }
}
