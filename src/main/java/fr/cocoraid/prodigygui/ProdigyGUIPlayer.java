package fr.cocoraid.prodigygui;

import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionGUI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

public class ProdigyGUIPlayer {
   private static Map<UUID, ProdigyGUIPlayer> prodigyPlayers = new HashMap();
   private Player player;
   private ThreeDimensionGUI threeDimensionGUI;
   private float previousYaw;
   private int globalTime = 0;

   public ProdigyGUIPlayer(Player player) {
      this.player = player;
   }

   public static ProdigyGUIPlayer instanceOf(Player player) {
      prodigyPlayers.putIfAbsent(player.getUniqueId(), new ProdigyGUIPlayer(player));
      if (prodigyPlayers.containsKey(player.getUniqueId())) {
         ((ProdigyGUIPlayer)prodigyPlayers.get(player.getUniqueId())).updatePlayer(player);
      }

      return (ProdigyGUIPlayer)prodigyPlayers.get(player.getUniqueId());
   }

   public static Map<UUID, ProdigyGUIPlayer> getProdigyPlayers() {
      return prodigyPlayers;
   }

   public void resetProdigyPlayer() {
      this.clearPlayer();
      prodigyPlayers.remove(this.player.getUniqueId());
   }

   public void clearPlayer() {
      if (this.player != null) {
         ;
      }
   }

   public Player getPlayer() {
      return this.player;
   }

   public ThreeDimensionGUI getThreeDimensionGUI() {
      return this.threeDimensionGUI;
   }

   public void setThreeDimensionGUI(ThreeDimensionGUI threeDimensionGUI) {
      this.threeDimensionGUI = threeDimensionGUI;
   }

   public int getGlobalTime() {
      return this.globalTime;
   }

   public void addTime() {
      ++this.globalTime;
      if (this.globalTime >= 999999) {
         this.globalTime = 0;
      }

   }

   public void updatePlayer(Player player) {
      this.player = player;
   }

   public float getPreviousYaw() {
      return this.previousYaw;
   }

   public void setPreviousYaw(float previousYaw) {
      this.previousYaw = previousYaw;
   }
}
