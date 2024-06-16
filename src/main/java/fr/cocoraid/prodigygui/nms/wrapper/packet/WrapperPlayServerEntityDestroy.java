package fr.cocoraid.prodigygui.nms.wrapper.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import fr.cocoraid.prodigygui.nms.AbstractPacket;
import java.util.Arrays;
import java.util.List;

public class WrapperPlayServerEntityDestroy extends AbstractPacket {
   public static final PacketType TYPE;

   public WrapperPlayServerEntityDestroy() {
      super(new PacketContainer(TYPE), TYPE);
      this.handle.getModifier().writeDefaults();
   }

   public WrapperPlayServerEntityDestroy(PacketContainer packet) {
      super(packet, TYPE);
   }

   public int getCount() {
      return ((int[])this.handle.getIntegerArrays().read(0)).length;
   }

   public int[] getEntityIDs() {
      return (int[])this.handle.getIntegerArrays().read(0);
   }

   public void setEntityIds(Integer[] value) {
      List<Integer> list = Arrays.asList(value);
      this.handle.getIntLists().write(0, list);
   }

   static {
      TYPE = Server.ENTITY_DESTROY;
   }
}
