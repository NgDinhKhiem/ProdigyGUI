package fr.cocoraid.prodigygui.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;

public abstract class AbstractPacket {
   protected PacketContainer handle;

   protected AbstractPacket(PacketContainer handle, PacketType type) {
      if (handle == null) {
         throw new IllegalArgumentException("Packet handle cannot be NULL.");
      } else if (!Objects.equal(handle.getType(), type)) {
         throw new IllegalArgumentException(handle.getHandle() + " is not a packet of type " + type);
      } else {
         this.handle = handle;
      }
   }

   public PacketContainer getHandle() {
      return this.handle;
   }

   public void sendPacket(Player receiver) {
      try {
         ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, this.getHandle());
      } catch (InvocationTargetException var3) {
         throw new RuntimeException("Cannot send packet.", var3);
      }
   }
}
