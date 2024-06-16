package fr.cocoraid.prodigygui.nms.wrapper.living;

import com.comphenix.protocol.wrappers.Vector3F;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import fr.cocoraid.prodigygui.utils.VersionChecker;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WrapperEntityArmorStand extends WrappedEntityLiving {
   private boolean small = false;
   private boolean noBasePlate = true;
   private boolean marker = false;
   private static int armorIndex;
   private static int headPosIndex;
   private static byte markerMask;
   private static int id;

   public WrapperEntityArmorStand(Location location, Player player) {
      super(location, player, id);
   }

   public void setHeadPose(float x, float y, float z) {
      Serializer serializer = Registry.getVectorSerializer();
      this.getDataWatcher().setObject(headPosIndex, serializer, new Vector3F(x, y, z));
   }

   public void setSmall(boolean small) {
      this.small = small;
      this.setDataWatcherObject(Byte.class, armorIndex, (byte)((small ? 1 : 0) | (this.noBasePlate ? 8 : 0) | (this.marker ? markerMask : 0)));
   }

   public void setMarker(boolean marker) {
      this.marker = marker;
      this.setDataWatcherObject(Byte.class, armorIndex, (byte)((this.small ? 1 : 0) | (this.noBasePlate ? 8 : 0) | (marker ? markerMask : 0)));
   }

   public void setNoBasePlate(boolean noBasePlate) {
      this.noBasePlate = noBasePlate;
      this.setDataWatcherObject(Byte.class, armorIndex, (byte)((this.small ? 1 : 0) | (noBasePlate ? 8 : 0) | (this.marker ? markerMask : 0)));
   }

   static {
      if (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_9_R2)) {
         armorIndex = 10;
      } else if (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_14_R1)) {
         armorIndex = 11;
      } else if (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_16_R1)) {
         armorIndex = 14;
      } else {
         armorIndex = 15;
      }

      headPosIndex = VersionChecker.isLowerOrEqualThan(VersionChecker.v1_9_R2) ? 11 : 12;
      markerMask = 16;
      id = 1;
      if (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_12_R1)) {
         id = 30;
      }

   }
}
