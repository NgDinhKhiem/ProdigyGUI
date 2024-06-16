package fr.cocoraid.prodigygui.utils;

import org.bukkit.Bukkit;

public class CachedGetters {
   private static long lastOnlinePlayersRefresh;
   private static int onlinePlayers;

   public static int getOnlinePlayers() {
      long now = System.currentTimeMillis();
      if (lastOnlinePlayersRefresh == 0L || now - lastOnlinePlayersRefresh > 1000L) {
         lastOnlinePlayersRefresh = now;
         onlinePlayers = Bukkit.getOnlinePlayers().size();
      }

      return onlinePlayers;
   }
}
