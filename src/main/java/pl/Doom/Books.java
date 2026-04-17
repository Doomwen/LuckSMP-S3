package pl.Doom;

import java.util.*;
import java.util.stream.Collectors;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;

public class Books implements Listener, CommandExecutor {
    private final List<ItemStack> books = Arrays.stream(AbilityMapping.values())
            .map(AbilityMapping::createItem)
            .collect(Collectors.toList());

    Component GUI = Component.text("\uE900\uE901");

    private void openSwordSelectionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 18, GUI);
        List<Integer> ignoredSlots = Arrays.asList(0, 1, 7, 8, 9, 10, 11, 12);
        int placed = 0;

        for (int slot = 0; slot < 18 && placed < books.size(); slot++) {
            if (ignoredSlots.contains(slot)) continue;

            gui.setItem(slot, books.get(placed));
            placed++;
        }
        player.openInventory(gui);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("books")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    this.openSwordSelectionGUI(player);
                }
            }
            return true;
        }

        return false;
    }

    @EventHandler
    public void Interact(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            Component guiTitle = event.getView().title();

            if (guiTitle.equals(GUI)) {
                if (!player.isOp()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
