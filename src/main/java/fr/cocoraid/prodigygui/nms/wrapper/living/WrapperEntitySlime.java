package fr.cocoraid.prodigygui.nms.wrapper.living;

import fr.cocoraid.prodigygui.utils.VersionChecker;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WrapperEntitySlime extends WrappedEntityLiving {
   private static int id = 80;

   public WrapperEntitySlime(Location location, Player player) {
      super(location, player, id);
   }

   public void setSize(int size) {
      if (VersionChecker.isHigherOrEqualThan(VersionChecker.v1_16_R1)) {
         this.setDataWatcherObject(Integer.class, 16, size);
      } else {
         this.setDataWatcherObject(Integer.class, 12, size);
      }

   }

   static {
      if (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_16_R1)) {
         id = 68;
      } else if (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_12_R1)) {
         id = 55;
      } else if (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_13_R2)) {
         id = 64;
      } else if (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_14_R1)) {
         id = 67;
      }

   }
}
