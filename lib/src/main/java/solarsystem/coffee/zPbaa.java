package solarsystem.coffee;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import solarsystem.coffee.utilz.C;

public class zPbaa extends JavaPlugin {

Player player;
  ;
  @Override
  public void onEnable() {
    Bukkit.getServer().getConsoleSender().sendMessage("zPbaa enabled");
  }
    @Override
  public void onDisable() {

    Bukkit.getServer().getConsoleSender().sendMessage("zPbaa disabled");
  }




  @Override
  public boolean onCommand(CommandSender interpreter, Command cmd, String input, String[] args) {

    player = (Player) interpreter;
    Server server = interpreter.getServer();
    String serverName = server.getName();


    if (interpreter instanceof Player) {


      if (input.equals("baa") || input.equals("boop")) {

        if (args.length != 1) {
          player.sendMessage(C.color("&6use: /baa or /boop playername"));
          return false;
        }

        String Name = args[0];

        Player Target = null;
        for (Player p : Bukkit.getOnlinePlayers()) {
          if (p.getName().equalsIgnoreCase(Name)) {
            Target = p;

          }
        }

        //Execute Boop
        if (Target != null) {
          Target.sendMessage(C.color("&6&l" + player.getName() + "&6 send you " + "&6&l✂ Baaaaaaaaaaaa ✂ "));
          Target.playSound(Target.getLocation(), Sound.ENTITY_SHEEP_AMBIENT, (float) 40.0, (float) 18.0);
          player.sendMessage(C.color("&6&l| &6&l✂ Baaaa ✂ &4&l| &6&l" + Target.getName() + " &6&l <3 &6&l| "));
        } else {
          player.sendMessage(ChatColor.LIGHT_PURPLE + "Target Player not found");
        }
        return true;
      }


    }
    return true;
  }
  }
