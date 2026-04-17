package pl.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.Books;
import pl.Doom.AbilityMapping;
import pl.Main;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Reroll implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (!Main.getInstance().getConfig().getBoolean("reroll_on_join", true))return;
        if (player.hasPlayedBefore())return;
        if (!Main.started)return;
        roll(player);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!Books.areSimilar(item, Books.createLuckBook())) return;

        if (CooldownManager.isOnCooldown(player.getUniqueId(), "reroll")) return;
        event.setCancelled(true);
        roll(player);
        item.setAmount(item.getAmount() - 1);
        CooldownManager.setCooldown(player.getUniqueId(), "reroll", 1);
        player.playSound(player.getLocation(), Sound.BLOCK_VAULT_INSERT_ITEM, 1f, 1f);
    }


    public static void roll(Player player) {
        List<String> banned = Main.getInstance()
                .getConfig()
                .getStringList("banned_luck_books")
                .stream()
                .map(String::toUpperCase)
                .toList();

        List<AbilityMapping> fullList = Arrays.stream(AbilityMapping.values())
                .filter(mapping -> !banned.contains(mapping.name()))
                .toList();

        if (fullList.isEmpty()) {
            player.sendMessage(Component.text("No available books to roll.", NamedTextColor.RED));
            return;
        }

        AbilityMapping finalMapping;
        Random random = new Random();
        do {
            finalMapping = fullList.get(random.nextInt(fullList.size()));
        } while (fullList.size() > 1 && finalMapping == fullList.get(11 % fullList.size()));

        Component finalName = finalMapping.getHackName();
        final AbilityMapping finalMapping1 = finalMapping;
        final ItemStack item = finalMapping.createItem();

        player.playSound(player.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED, 1F, 1F);

        new BukkitRunnable() {
            int index = 0;
            final int totalCycles = 12;
            final List<AbilityMapping> cycle = fullList;

            private Color getColor(AbilityMapping mapping) {
                return switch (mapping) {
                    case WIND_MANIPULATION -> Color.WHITE;
                    case DARK_SENSE -> Color.GRAY;
                    case ADRENALINE_RUSH -> Color.FUCHSIA;
                    case HELLSCORCH -> Color.ORANGE;
                    case RAW_STRENGTH -> Color.RED;
                    case THUNDERCHARGE -> Color.AQUA;
                };
            }

            @Override
            public void run() {
                if (index < totalCycles) {
                    AbilityMapping current = cycle.get(index % cycle.size());
                    player.showTitle(Title.title(Component.text(current.getCooldownEmoji()), current.getHackName().decorate(TextDecoration.BOLD), Times.times(Duration.ZERO, Duration.ofSeconds(20), Duration.ofSeconds(10))));
                    player.playSound(player.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED, 1F, 1F);
                    index++;
                    Color color = getColor(current);
                    circleParticle(player.getLocation(), color);
                } else {
                    player.showTitle(Title.title(Component.text(finalMapping1.getCooldownEmoji()), finalName.decorate(TextDecoration.BOLD), Times.times(Duration.ofSeconds(10), Duration.ofSeconds(60), Duration.ofSeconds(10))));
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1F);
                    Boolean broadcast = Main.getInstance().getConfig().getBoolean("broadcast_obtaining_luck", true);
                    if (broadcast) {
                        Bukkit.broadcast(Component.text(player.getName() + " has obtained ", NamedTextColor.GREEN).append(item.displayName()));
                    }

                    HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
                    for (ItemStack leftover : leftovers.values()) {
                        player.getWorld().dropItem(player.getLocation(), leftover);
                    }

                    Color color = getColor(finalMapping1);
                    circleParticle(player.getLocation(), color);

                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 10L);
    }

    public static void circleParticle(Location loc, Color color) {
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < 360; i += 30) {
                double radius = 1;
                double angle = Math.toRadians(i);
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Location particleLoc = loc.clone().add(x, h, z);
                loc.getWorld().spawnParticle(
                        Particle.DUST,
                        particleLoc, 1, 0, 0, 0, 0, new Particle.DustOptions(color, 1f)
                );
            }
        }
    }
}
