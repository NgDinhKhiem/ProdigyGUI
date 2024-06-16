package fr.cocoraid.prodigygui.task;

import fr.cocoraid.prodigygui.ProdigyGUI;
import fr.cocoraid.prodigygui.ProdigyGUIPlayer;
import fr.cocoraid.prodigygui.bridge.PlaceholderAPIBridge;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import fr.cocoraid.prodigygui.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ThreeDimensionalGUITask {
   private ProdigyGUI instance;

   public ThreeDimensionalGUITask(ProdigyGUI instance) {
      this.instance = instance;
      this.runAsync();
   }

   public void runSync() {
      (new BukkitRunnable() {
         int i = 0;

         public void run() {
            ++this.i;
            this.i %= 100;
            ProdigyGUIPlayer.getProdigyPlayers().values().forEach((p) -> {
               ThreeDimensionGUI gui = p.getThreeDimensionGUI();
               Player player = p.getPlayer();
               if (player != null && p.getPlayer().isOnline() && gui != null && gui.isSpawned()) {
               }

            });
         }
      }).runTaskTimer(ProdigyGUI.getInstance(), 100L, 20L);
   }

   public void runAsync() {
      (new BukkitRunnable() {
         int i = 0;

         public void run() {
            ++this.i;
            this.i %= 100;
            ProdigyGUIPlayer.getProdigyPlayers().values().forEach((p) -> {
               ThreeDimensionGUI gui = p.getThreeDimensionGUI();
               Player player = p.getPlayer();
               Location location = p.getPlayer().getLocation();
               if (player != null && p.getPlayer().isOnline() && gui != null && gui.isSpawned()) {
                  if (this.i % 20 * 5 == 0) {
                     if (!location.getWorld().equals(gui.getCenter().getWorld())) {
                        gui.closeGui();
                        return;
                     }

                     if (location.distance(gui.getCenter()) > 10.0D) {
                        gui.closeGui();
                        return;
                     }
                  }

                  if (this.i % 20 == 0) {
                     p.getThreeDimensionGUI().setSelected(0);
                  }

                  if (!location.getWorld().equals(gui.getCenter().getWorld())) {
                     return;
                  }

                  if (location.distance(gui.getCenter()) > 2.0D) {
                     return;
                  }

                  gui.getAllItems().stream().filter((i) -> {
                     return i.getItem().getLocation() != null && ThreeDimensionalGUITask.this.isLookingAt(p.getPlayer(), i.getItem().getSelector().getLocation()) && gui.getSelected() <= 4 && i.getItem().isSpawned() && (gui.getLastSelected() == null || !p.getThreeDimensionGUI().getLastSelected().equals(i.getItem()));
                  }).findAny().ifPresent((i) -> {
                     gui.addSelected();
                     if (gui.getLastSelected() != null) {
                        gui.getLastSelected().move(true);
                     }

                     gui.setLastSelected(i.getItem());
                     if (i.getItem().getData().getLore() != null) {
                        Utils.sendActionMessage(player, PlaceholderAPIBridge.setPlaceholders(i.getItem().getData().getLore(), player));
                     }

                     Bukkit.getScheduler().runTask(ThreeDimensionalGUITask.this.instance, () -> {
                        ThreeDimensionalMenu menu = gui.getMenu();
                        if (menu.getDefaultChangeSound() != null) {
                           player.playSound(player.getLocation(), menu.getDefaultChangeSound().getSound(), menu.getDefaultChangeSound().getVolume(), menu.getDefaultChangeSound().getPitch());
                        }

                     });
                     gui.getLastSelected().move(false);
                  });
               }

            });
         }
      }).runTaskTimerAsynchronously(ProdigyGUI.getInstance(), 0L, 0L);
   }

   private boolean isLookingAt(Player player, Location l) {
      Location eye = player.getEyeLocation();
      Vector toEntity = l.toVector().subtract(eye.toVector());
      double dot = toEntity.normalize().dot(eye.getDirection());
      return dot > 0.96D;
   }
}
