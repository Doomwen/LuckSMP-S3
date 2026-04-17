package pl.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.Doom.AbilityMapping;
import pl.Main;

public class EquipManager implements Listener {
    private final AbilityManager hackManager = Main.getInstance().getAbilityManager();

    public boolean handleLegendaryHack(Player player, AbilityMapping luckBook) {
        boolean hasLuck = Main.getInstance().getAbilityManager().getAbility(player.getUniqueId()) != null;
        if (hasLuck) {
            player.sendMessage(Component.text("You already have a luck book equipped", NamedTextColor.RED));
            return false;
        }
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "equip")) {
            return false;
        }
        consumeMainHandItem(player);
        hackManager.setAbility(player.getUniqueId(), luckBook);
        CooldownManager.setCooldown(player.getUniqueId(), "equip", 1);
        return true;
    }

    @EventHandler
    public void onPlayerRightclick(PlayerInteractEvent event) {
        if (!event.getAction().toString().contains("RIGHT_")) return;
        
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.ENCHANTED_BOOK) return;

        AbilityMapping luckBook = AbilityMapping.fromItem(item);
        if (luckBook != null) {
            boolean equipped = handleLegendaryHack(player, luckBook);
            if (equipped) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED, 1f, 1f);
            }
        }
    }

    public static void consumeMainHandItem(Player player) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        mainHandItem.setAmount(0);
    }
}
