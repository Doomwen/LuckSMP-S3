package pl.Doom;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.managers.CooldownManager;

import java.util.UUID;

public class CooldownCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /cooldown
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                CooldownManager.removeAllCooldowns(player.getUniqueId());
                player.sendMessage(Component.text("Your cooldowns have been cleared!", NamedTextColor.GREEN));
                player.setCooldown(Material.SCULK_CATALYST, 1);
                player.updateInventory();
            } else {
                sender.sendMessage(Component.text("Only players can clear their own cooldowns.", NamedTextColor.RED));
            }
            return true;
        }

        // /cooldown <player>
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer != null && targetPlayer.isOnline()) {
            UUID playerUUID = targetPlayer.getUniqueId();
            CooldownManager.removeAllCooldowns(playerUUID);
            sender.sendMessage(Component.text("Cooldowns cleared for " + targetPlayer.getName() + "!", NamedTextColor.GREEN));

            targetPlayer.setCooldown(Material.SCULK_CATALYST, 1);
            targetPlayer.updateInventory();
        } else {
            sender.sendMessage(Component.text("Player '" + args[0] + "' not found or not online.", NamedTextColor.RED));
        }

        return true;
    }
}
