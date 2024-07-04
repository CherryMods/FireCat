package org.sparklet.firecat;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireCat extends JavaPlugin implements Listener {
    /// All users who have firecat enabled.
    HashMap<UUID, Boolean> fireCats = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage("FireCat enabled");
        getServer().getPluginManager().registerEvents(new PotionListener(), this);
    }

    @Override
    public void onDisable() {

        Bukkit.getServer().getConsoleSender().sendMessage("FireCat disabled");
    }

    @Override
    public boolean onCommand(CommandSender interpreter, Command cmd, String input,
            String[] args) {
        if (interpreter instanceof Player) {
            Player player = (Player) interpreter;

            if (args.length != 1) {
                return false;
            }

            UUID uuid = player.getUniqueId();

            switch (args[0]) {
                case "on":
                    fireCats.put(uuid, true);
                    break;
                case "off":
                    fireCats.put(uuid, false);
                    Disguise.undisguise(player.getName());
                    break;
                default:
                    return false;
            }
        }

        return true;
    }

    class PotionListener implements Listener {
        @EventHandler
        public void onPotionDrink(EntityPotionEffectEvent event) {
            if (event.getEntity().getType() != EntityType.PLAYER) {
                return;
            }

            Player player = (Player) event.getEntity();
            UUID uuid = player.getUniqueId();

            boolean isFireCat = fireCats.containsKey(uuid) && fireCats.get(uuid);
            if (!isFireCat) {
                return;
            }

            // if drank fire res
            PotionEffect newFX = event.getNewEffect();
            if (newFX != null &&
                    newFX.getType() == PotionEffectType.FIRE_RESISTANCE) {
                Bukkit.getServer().broadcastMessage("Drank fire resistance!");
                Disguise.disguise(player.getName(), "cat");
                return;
            }

            // if old effect was fire res and new is null
            PotionEffect oldFX = event.getOldEffect();
            if (oldFX.getType() == PotionEffectType.FIRE_RESISTANCE) {
                Bukkit.getServer().broadcastMessage("Ran out of fire resistance!");
                Disguise.undisguise(player.getName());
                return;
            }
        }
    }
}
