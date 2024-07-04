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

    final String ENABLE_MSG = C.color("&6Fire resistance potions will now turn you into a cat!");
    final String DISABLE_MSG = C.color(
            "&6Fire resistance potions will no longer turn you into a cat...");

    final String TRANSFORM_MSG = C.color("&6Your fire resistance has turned you into a cat!");
    final String DETRANSFORM_MSG = C.color("&6Your fire resistance ran out, so you are once again human.");

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
                    player.sendMessage(ENABLE_MSG);
                    break;
                case "off":
                    fireCats.put(uuid, false);
                    player.sendMessage(DISABLE_MSG);
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
                player.sendMessage(TRANSFORM_MSG);
                Disguise.disguise(player.getName(), "cat");
                return;
            }

            // if old effect was fire res and new is null
            PotionEffect oldFX = event.getOldEffect();
            if (oldFX.getType() == PotionEffectType.FIRE_RESISTANCE) {
                player.sendMessage(DETRANSFORM_MSG);
                Disguise.undisguise(player.getName());
                return;
            }
        }
    }
}
