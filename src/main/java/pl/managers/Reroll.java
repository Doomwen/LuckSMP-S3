package pl.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
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
import pl.Doom.Utils.ChatUtil;
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
                .filter(mapping -> !banned.contains(ChatColor.stripColor(mapping.name()).toUpperCase()))
                .toList();

        if (fullList.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No available books to roll.");
            return;
        }

        AbilityMapping finalMapping;
        Random random = new Random();
        do {
            finalMapping = fullList.get(random.nextInt(fullList.size()));
        } while (fullList.size() > 1 && finalMapping == fullList.get((12 - 1) % fullList.size()));

        String finalName = finalMapping.getHackName();
        AbilityMapping finalMapping1 = finalMapping;

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
                if (index < totalCycles) { // rolling
                    AbilityMapping current = cycle.get(index % cycle.size());
                    Title title = Title.title(
                            current.getCooldownEmoji(),
                            Component.text("§l" + current.getHackName()),
                            Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofMillis(0))
                    );
                    player.showTitle(title);

                    player.playSound(player.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED, 1F, 1F);
                    index++;
                    Color color = getColor(current);
                    circleParticle(player.getLocation(), color);
                } else { // selected
                    Title title = Title.title(
                            finalMapping1.getCooldownEmoji(),
                            Component.text("§l" + finalName),
                            Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500))
                    );
                    player.showTitle(title);


                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1F);
                    Boolean broadcast = Main.getInstance().getConfig().getBoolean("broadcast_obtaining_luck", true);
                    if (broadcast) {
                        Bukkit.broadcastMessage("§a" + player.getName() + " has obtained " + ChatUtil.fancyFont(finalName));
                    }

                    ItemStack reward = finalMapping1.createItem();
                    HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(reward);
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
