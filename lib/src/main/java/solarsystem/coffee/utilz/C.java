package solarsystem.coffee.utilz;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;

public class C {
    public C() {
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static List<String> colorLines(List<String> lore) {
        List<String> color = new ArrayList<String>();

        for (String s : lore) {
            color.add(color(s));
        }

        return color;
    }
}
