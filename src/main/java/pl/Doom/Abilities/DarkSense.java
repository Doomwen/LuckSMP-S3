package pl.Doom.Abilities;

import org.bukkit.*;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import pl.Doom.AbilityMapping;
import pl.Main;

public class DarkSense implements Listener {
    @EventHandler
    public void interact(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!event.getAction().isRightClick())return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.SCULK_CATALYST)return;
        if (player.hasCooldown(Material.SCULK_CATALYST))return;
        if (Main.getAbilityManager().getAbility(player.getUniqueId()) != AbilityMapping.DARK_SENSE)return;
        event.setCancelled(true);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
        String name = "dark_sense";
        int chance = Main.getInstance().getConfig().getInt("chances." + name, 50);
        int damage = Main.getInstance().getConfig().getInt("damage." + name, 7);
        int cooldown = Main.getInstance().getConfig().getInt("cooldowns." + name, 25);
        player.setCooldown(Material.SCULK_CATALYST, cooldown * 20);
        item.setAmount(item.getAmount() - 1);

        if ((Math.random() * 100) < chance) {
            beam(player, damage);
        }
    }

    public static void beam(Player player, int damage) {
        double step = 0.5;
        int range = Main.getInstance().getConfig().getInt("dark_sense_range", 20);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 1);
        for (double i = 1.5; i < range; i += step) {
            Location eye = player.getEyeLocation();
            Vector direction = eye.getDirection().normalize();
            Location loc = eye.clone().add(direction.multiply(i));

            loc.getWorld().spawnParticle(Particle.SONIC_BOOM, loc, 0, 0, 0, 0, 0);

            for (Entity entity : loc.getNearbyEntities(1, 0.75, 1)) {
                if (!(entity instanceof LivingEntity target)) continue;
                if (target.equals(player)) continue;

                target.setNoDamageTicks(0);

                target.damage(damage, DamageSource.builder(DamageType.SONIC_BOOM).build());

                return;
            }
        }
    }
}
