package pl.managers;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import pl.Books;
import pl.Main;

public class GlowManager implements Listener {
    public static void registerTeams() {
        teams("red_lucksmps3_custom", NamedTextColor.RED);
        teams("aqua_lucksmps3_custom", NamedTextColor.AQUA);
        teams("pink_lucksmps3_custom", NamedTextColor.LIGHT_PURPLE);
        teams("gold_lucksmps3_custom", NamedTextColor.GOLD);
        teams("gray_lucksmps3_custom", NamedTextColor.GRAY);
    }

    public static void teams(String name, NamedTextColor color) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam(name);
        if (team == null) {
            team = board.registerNewTeam(name);
        }

        team.color(color);
    }

    public static void setTeam(Entity entity, String teamName) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam(teamName);

        if (team == null) return;
        team.addEntity(entity);
    }

    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        Entity dropped = event.getItemDrop();
        ItemStack item = event.getItemDrop().getItemStack();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Books.areSimilar(item, Books.createThunderCharge())) {
                    dropped.setGlowing(true);
                    setTeam(dropped, "aqua_lucksmps3_custom");
                } else if (Books.areSimilar(item, Books.createAdrenalineRush())) {
                    setTeam(dropped, "pink_lucksmps3_custom");
                    dropped.setGlowing(true);
                } else if (Books.areSimilar(item, Books.createRawStrength())) {
                    setTeam(dropped, "red_lucksmps3_custom");
                    dropped.setGlowing(true);
                } else if (Books.areSimilar(item, Books.createDarkSense())) {
                    setTeam(dropped, "gray_lucksmps3_custom");
                    dropped.setGlowing(true);
                } else if (Books.areSimilar(item, Books.createWindManipulationBook())) {
                    dropped.setGlowing(true);
                } else if (Books.areSimilar(item, Books.createHellScorch())) {
                    setTeam(dropped, "gold_lucksmps3_custom");
                    dropped.setGlowing(true);
                }
            }
        }.runTaskLater(Main.getInstance(), 2L);
    }

    @EventHandler
    public void death2(PlayerDeathEvent event) {
        Location loc = event.getEntity().getLocation();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Item entity : loc.getWorld().getNearbyEntitiesByType(Item.class, loc, 2, 2, 2)) {
                    ItemStack item = entity.getItemStack();

                    if (item.getType() != Material.ENCHANTED_BOOK) continue;

                    if (Books.areSimilar(item, Books.createThunderCharge())) {
                        setTeam(entity, "aqua_lucksmps3_custom");
                        entity.setGlowing(true);

                    } else if (Books.areSimilar(item, Books.createAdrenalineRush())) {
                        setTeam(entity, "pink_lucksmps3_custom");
                        entity.setGlowing(true);

                    } else if (Books.areSimilar(item, Books.createRawStrength())) {
                        setTeam(entity, "red_lucksmps3_custom");
                        entity.setGlowing(true);

                    } else if (Books.areSimilar(item, Books.createDarkSense())) {
                        setTeam(entity, "gray_lucksmps3_custom");
                        entity.setGlowing(true);

                    } else if (Books.areSimilar(item, Books.createWindManipulationBook())) {
                        entity.setGlowing(true);

                    } else if (Books.areSimilar(item, Books.createHellScorch())) {
                        setTeam(entity, "gold_lucksmps3_custom");
                        entity.setGlowing(true);
                    }
                }

            }
        }.runTaskLater(Main.getInstance(), 2L);
    }
}
