package pl.managers;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import pl.Doom.AbilityMapping;
import pl.Main;

public class Withdraw implements CommandExecutor, Listener {

    private final AbilityManager hackManager = new AbilityManager();
    private final Plugin plugin;

    public Withdraw(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        final AbilityMapping hack = Main.getInstance().getAbilityManager().getAbility(player.getUniqueId());
        if (hack == null) {
            player.sendMessage(ChatColor.RED + "No luck book equipped");
            return true;
        }
        ItemStack item = hack.createItem();
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
            player.sendMessage(ChatColor.YELLOW + "The book was dropped on the ground.");
        } else {
            player.getInventory().addItem(item);
        }

        AbilityManager.setAbility(player.getUniqueId(), null);
        player.playSound(player.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP, 1f, 1f);
        return true;
    }
}
