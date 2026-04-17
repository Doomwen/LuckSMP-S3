package pl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pl.Doom.*;
import pl.Doom.Abilities.*;
import pl.Doom.Passives.passives;
import pl.Doom.trust.TrustCommand;
import pl.Doom.trust.TrustManager;
import pl.managers.*;

import java.io.File;
import java.util.*;


public final class Main extends JavaPlugin implements Listener {
    private static Main instance;
    private static AbilityManager abilityManager;
    public static TrustManager trustManager;
    public static boolean started = false;

    @Override
    public void onEnable() {
        instance = this;
        trustManager = new TrustManager();
        abilityManager = new AbilityManager();

        //
        messages();
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        //

        listeners();
        commands();
        started = getConfig().getBoolean("started_smp", false);
    }

    public void messages() {
        saveResource("messages.yml", false);
        FileConfiguration messages = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml"));
        pl.Books.load(messages);
    }

    public void listeners() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new EquipManager(), this);
        Bukkit.getPluginManager().registerEvents(new pl.Doom.Books(), this);
        Bukkit.getPluginManager().registerEvents(new EquippedLuckCommand(), this);
        Bukkit.getPluginManager().registerEvents(new Reroll(), this);
        Bukkit.getPluginManager().registerEvents(abilityManager, this);
        Bukkit.getPluginManager().registerEvents(new GlowManager(), this);


        //
        Bukkit.getPluginManager().registerEvents(new Thundercharge(), this);
        Bukkit.getPluginManager().registerEvents(new RawStrength(), this);
        Bukkit.getPluginManager().registerEvents(new Hellscorch(), this);
        Bukkit.getPluginManager().registerEvents(new DarkSense(), this);
        Bukkit.getPluginManager().registerEvents(new WindManipulation(), this);
        Bukkit.getPluginManager().registerEvents(new AdrenalineRush(), this);
        //


        Bukkit.getPluginManager().registerEvents(new passives(), this);
        passives.task();

        boolean actionbar = Main.getInstance().getConfig().getBoolean("actionbar_cooldown", true);
        if (actionbar) {
            new ActionBarUpdater().runTaskTimer(this, 0L, 2L);
        }
    }

    @EventHandler
    public void death(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (getConfig().getBoolean("drop_reroll_on_death", true)) {
            event.getDrops().add(pl.Books.createLuckBook());
        } else {
            AbilityMapping ability = abilityManager.getAbility(player.getUniqueId());
            if (ability == null) return;
            event.getDrops().add(ability.createItem());
        }
        abilityManager.setAbility(player.getUniqueId(), null);
    }

    public void commands() {
        Objects.requireNonNull(getCommand("books")).setExecutor(new pl.Doom.Books());
        Objects.requireNonNull(getCommand("luck")).setExecutor(new EquippedLuckCommand());
        Objects.requireNonNull(getCommand("withdraw")).setExecutor(new Withdraw(this));

        ezCommands();
        Objects.requireNonNull(getCommand("trust")).setExecutor(new TrustCommand(trustManager));
        Objects.requireNonNull(getCommand("untrust")).setExecutor(new TrustCommand(trustManager));

        Objects.requireNonNull(getCommand("cooldown")).setExecutor(new CooldownCommand());
        GlowManager.registerTeams();
    }

    public void ezCommands() {
        Objects.requireNonNull(getCommand("luckbook")).setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }

            if (args.length < 1) {
                player.sendMessage("Usage: /luckbook <amount>");
                return true;
            }

            try {
                int count = Integer.parseInt(args[0]);
                if (count < 1 || count > 64) {
                    player.sendMessage("❌ Amount must be between 1 and 64.");
                    return true;
                }

                ItemStack book = pl.Books.createLuckBook();
                book.setAmount(count);
                player.getInventory().addItem(book);
                player.sendMessage("📘 You have received " + count + " Luck Books!");
            } catch (NumberFormatException e) {
                player.sendMessage("❌ Invalid number. Please enter a valid amount.");
            }

            return true;
        });

        Objects.requireNonNull(getCommand("luckstart")).setExecutor((sender, command, label, args) -> {
            started = true;
            getConfig().set("started_smp", true);
            if (getConfig().getBoolean("reroll_on_start", true)) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Reroll.roll(p);
                }
            }
            return true;
        });

        PluginCommand cmd = getCommand("setluckbook");
        assert cmd != null;
        cmd.setExecutor((sender, command, label, args) -> {
            if (args.length < 2) {
                sender.sendMessage(Component.text("Usage: /setluckbook <player> <book>", NamedTextColor.RED));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(Component.text("Player not found or not online.", NamedTextColor.RED));
                return true;
            }

            AbilityMapping luck;

            try {
                luck = AbilityMapping.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                luck = null;
            }

            abilityManager.setAbility(target.getUniqueId(), luck);
            sender.sendMessage(Component.text("Set luck book for " + target.getName() + " to " + luck, NamedTextColor.GREEN));
            return true;
        });
        cmd.setTabCompleter((sender, command, alias, args) -> {

            List<String> list = new ArrayList<>();

            if (args.length == 1) {
                return null;
            }

            if (args.length == 2) {
                String input = args[1].toLowerCase();

                for (AbilityMapping ability : AbilityMapping.values()) {
                    if (ability.name().toLowerCase().startsWith(input)) {
                        list.add(ability.name());
                    }
                }
            }

            return list;
        });
    }

    public static boolean isTrusted(Player player, Entity target) {
        if (target instanceof Player p) {
            return trustManager.isTrusted(player, p);
        } else {
            return false;
        }
    }

    public static AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public static Main getInstance() {
        return instance;
    }
}
