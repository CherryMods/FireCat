package org.sparklet.firecat;

import java.util.Arrays;
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
    HashMap<UUID, boolean[]> fireCats = new HashMap<>();

    final String[] ENABLE_MSG = {
            C.color("&6Fire resistance potions now taste extra sweet!"),
            C.color("&6Water breathing potions now taste a bit less salty!"),
    };
    final String[] DISABLE_MSG = {
            C.color("&6Fire resistance potions no longer taste extra sweet..."),
            C.color("&6Water breathing potions no longer taste less salty..."),
    };

    final String[] TRANSFORM_MSG = {
            C.color("&6Fire resistance has turned you into a cat!"),
            C.color("&6Water breathing has turned you into a salmon!"),
    };
    final String[] DETRANSFORM_MSG = {
            C.color("&6Your fire resistance ran out, so you are once again human."),
            C.color("&6Your water breathing ran out, so you are once again human."),
    };

    // Potion mob ids
    final int PM_CAT = 0;
    final int PM_SALMON = 1;

    final String[] POTION_MOBS = {
            "cat",
            "salmon",
    };

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

            if (args.length < 2) {
                return false;
            }

            UUID uuid = player.getUniqueId();

            int index = Arrays.asList(POTION_MOBS).indexOf(args[0]);
            if (index == -1) {
                return false;
            }

            boolean[] map = fireCats.getOrDefault(uuid, new boolean[2]);
            switch (args[1]) {
                case "on":
                    map[index] = true;
                    player.sendMessage(ENABLE_MSG[index]);
                    break;
                case "off":
                    map[index] = false;
                    player.sendMessage(DISABLE_MSG[index]);
                    Disguise.undisguise(player.getName());
                    break;
                default:
                    return false;
            }

            // only required for people who didn't previously have an entry
            fireCats.put(uuid, map);
        }

        return true;
    }

    class PotionListener implements Listener {
        boolean isFireCat(Player player, int potionMobId) {
            UUID uuid = player.getUniqueId();

            boolean hasKey = fireCats.containsKey(uuid);
            String permNode = "firecat." + POTION_MOBS[potionMobId] + ".default";
            boolean isFCByDefault = player.hasPermission(permNode);

            boolean[] map = fireCats.get(uuid);
            return (hasKey && map[potionMobId]) || (!hasKey && isFCByDefault);
        }

        @EventHandler
        public void onPotionDrink(EntityPotionEffectEvent event) {
            if (event.getEntity().getType() != EntityType.PLAYER) {
                return;
            }

            Player player = (Player) event.getEntity();

            //
            // TODO no more hardcoding
            //

            if (isFireCat(player, PM_CAT)) {
                // if drank fire res
                PotionEffect newFX = event.getNewEffect();
                if (newFX != null &&
                        newFX.getType() == PotionEffectType.FIRE_RESISTANCE) {
                    player.sendMessage(TRANSFORM_MSG[PM_CAT]);
                    Disguise.disguise(player.getName(), "cat");
                    return;
                }

                // if old effect was fire res and new is null
                PotionEffect oldFX = event.getOldEffect();
                if (oldFX != null &&
                        oldFX.getType() == PotionEffectType.FIRE_RESISTANCE) {
                    player.sendMessage(DETRANSFORM_MSG[PM_CAT]);
                    Disguise.undisguise(player.getName());
                    return;
                }
            }

            if (isFireCat(player, PM_SALMON)) {
                // if drank water breathing
                PotionEffect newFX = event.getNewEffect();
                if (newFX != null &&
                        newFX.getType() == PotionEffectType.WATER_BREATHING) {
                    player.sendMessage(TRANSFORM_MSG[PM_SALMON]);
                    Disguise.disguise(player.getName(), "salmon");
                    return;
                }

                // if old effect was fire res and new is null
                PotionEffect oldFX = event.getOldEffect();
                if (oldFX != null &&
                        oldFX.getType() == PotionEffectType.WATER_BREATHING) {
                    player.sendMessage(DETRANSFORM_MSG[PM_SALMON]);
                    Disguise.undisguise(player.getName());
                    return;
                }
            }
        }
    }
}
