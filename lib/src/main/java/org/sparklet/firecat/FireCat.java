package org.sparklet.firecat;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FireCat extends JavaPlugin {
    /// All users who have firecat enabled.
    HashMap<Player, Boolean> fireCats = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage("FireCat enabled");
    }

    @Override
    public void onDisable() {

        Bukkit.getServer().getConsoleSender().sendMessage("FireCat disabled");
    }

    @Override
    public boolean onCommand(CommandSender interpreter, Command cmd, String input,
            String[] args) {
        // The plugin only has 1 command. Remove this later if more are added.
        if (!input.equals("firecat")) {
            return false;
        }

        if (interpreter instanceof Player) {
            if (args.length != 1) {
                return false;
            }

            Player player = (Player) interpreter;
            switch (args[0]) {
                case "on":
                    fireCats.put(player, true);
                    break;
                case "off":
                    fireCats.put(player, false);
                    break;
                default:
                    return false;
            }
        }

        return true;
    }
}
