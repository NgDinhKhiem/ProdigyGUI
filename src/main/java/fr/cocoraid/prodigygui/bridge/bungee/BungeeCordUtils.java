package fr.cocoraid.prodigygui.bridge.bungee;

import fr.cocoraid.prodigygui.ProdigyGUI;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BungeeCordUtils {
   public static boolean connect(Player player, String server) {
      try {
         if (server.length() == 0) {
            player.sendMessage("§cTarget server was \"\" (empty string) cannot connect to it.");
            return false;
         } else {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(byteArray);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(ProdigyGUI.getInstance(), "BungeeCord", byteArray.toByteArray());
            return true;
         }
      } catch (Exception var4) {
         player.sendMessage(ChatColor.RED + "An unexpected exception has occurred. Please notify the server's staff about this. (They should look at the console).");
         var4.printStackTrace();
         ProdigyGUI.getInstance().getLogger().warning("Could not connect \"" + player.getName() + "\" to the server \"" + server + "\".");
         return false;
      }
   }
}
