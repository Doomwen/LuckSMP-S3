package pl.Doom.trust;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrustCommand implements CommandExecutor {
    private final TrustManager trustManager;

    public TrustCommand(TrustManager trustManager) {
        this.trustManager = trustManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text("/" + label + " <player>", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(Component.text(args[0] + " is not online", NamedTextColor.RED));
            return true;
        }

        if (label.equalsIgnoreCase("trust")) {
            if (trustManager.isTrusted(player, target)) {
                player.sendMessage(Component.text(target.getName() + " is already trusted.", NamedTextColor.GREEN));
            } else {
                trustManager.trustPlayer(player, target);
                player.sendMessage(Component.text("You have trusted " + target.getName() + ".", NamedTextColor.GREEN));
            }
        } else if (label.equalsIgnoreCase("untrust")) {
            if (!trustManager.isTrusted(player, target)) {
                player.sendMessage(Component.text(target.getName() + " is not trusted.", NamedTextColor.YELLOW));
            } else {
                trustManager.untrustPlayer(player, target);
                player.sendMessage(Component.text("You have untrusted " + target.getName() + ".", NamedTextColor.RED));
            }
        }

        return true;
    }
}