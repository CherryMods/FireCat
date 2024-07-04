package org.sparklet.firecat;

import org.bukkit.Bukkit;

public class Disguise {
    public static void disguise(String player, String disguise) {
        String cmd = "disguiseplayer " + player + " " + disguise;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    public static void undisguise(String player) {
        String cmd = "undisguiseplayer " + player;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }
}
