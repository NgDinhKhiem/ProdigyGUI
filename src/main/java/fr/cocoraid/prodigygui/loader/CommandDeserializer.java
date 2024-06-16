package fr.cocoraid.prodigygui.loader;

import fr.cocoraid.prodigygui.ProdigyGUI;
import fr.cocoraid.prodigygui.ProdigyGUIPlayer;
import fr.cocoraid.prodigygui.bridge.PlaceholderAPIBridge;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import fr.cocoraid.prodigygui.utils.CC;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandDeserializer {
   private List<String> commands;
   private Player player;

   public CommandDeserializer(Player player, List<String> commands) {
      this.commands = commands;
      this.player = player;
   }

   public void execute() {
      if (!this.commands.isEmpty()) {
         this.commands.forEach((command) -> {
            if (command.contains(":")) {
               String output = command.substring(0, command.indexOf(58));
               String final_cmd = command.substring(command.lastIndexOf(":") + 1).trim();
               final_cmd = PlaceholderAPIBridge.setPlaceholders(final_cmd, this.player);
               String var4 = output.toLowerCase();
               byte var5 = -1;
               switch(var4.hashCode()) {
               case 3553:
                  if (var4.equals("op")) {
                     var5 = 0;
                  }
                  break;
               case 3417674:
                  if (var4.equals("open")) {
                     var5 = 4;
                  }
                  break;
               case 3556273:
                  if (var4.equals("tell")) {
                     var5 = 2;
                  }
                  break;
               case 94756344:
                  if (var4.equals("close")) {
                     var5 = 3;
                  }
                  break;
               case 951510359:
                  if (var4.equals("console")) {
                     var5 = 1;
                  }
               }

               switch(var5) {
               case 0:
                  if (this.player.isOp()) {
                     this.player.chat("/" + final_cmd);
                  } else {
                     this.player.setOp(true);
                     this.player.chat("/" + final_cmd);
                     this.player.setOp(false);
                  }
                  break;
               case 1:
                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), final_cmd);
                  break;
               case 2:
                  this.player.sendMessage(CC.colored(final_cmd));
                  break;
               case 3:
                  if (ProdigyGUIPlayer.getProdigyPlayers().containsKey(this.player.getUniqueId())) {
                     ProdigyGUIPlayer pp = ProdigyGUIPlayer.instanceOf(this.player);
                     if (pp.getThreeDimensionGUI() != null && pp.getThreeDimensionGUI().isSpawned()) {
                        pp.getThreeDimensionGUI().closeGui();
                     }
                  }
                  break;
               case 4:
                  ThreeDimensionalMenu.getMenus().stream().filter((m) -> {
                     return m.getFileName().equalsIgnoreCase(command.substring(command.lastIndexOf(":") + 1).trim());
                  }).findAny().ifPresent((menu) -> {
                     if (menu.getPermission() != null && !this.player.hasPermission(menu.getPermission())) {
                        this.player.sendMessage(ProdigyGUI.getInstance().getLanguage().no_permission);
                     } else {
                        (new ThreeDimensionGUI(this.player, menu)).openGui();
                     }
                  });
               }
            } else {
               this.player.chat("/" + command.replace("/", ""));
            }

         });
      }
   }
}
