package fr.cocoraid.prodigygui.nms.wrapper.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import fr.cocoraid.prodigygui.nms.AbstractPacket;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerEntityMetadata extends AbstractPacket {
   public static final PacketType TYPE;

   public WrapperPlayServerEntityMetadata() {
      super(new PacketContainer(TYPE), TYPE);
      this.handle.getModifier().writeDefaults();
   }

   public WrapperPlayServerEntityMetadata(PacketContainer packet) {
      super(packet, TYPE);
   }

   public int getEntityID() {
      return (Integer)this.handle.getIntegers().read(0);
   }

   public void setEntityID(int value) {
      this.handle.getIntegers().write(0, value);
   }

   public Entity getEntity(World world) {
      return (Entity)this.handle.getEntityModifier(world).read(0);
   }

   public Entity getEntity(PacketEvent event) {
      return this.getEntity(event.getPlayer().getWorld());
   }

   public List<WrappedWatchableObject> getMetadata() {
      return (List)this.handle.getWatchableCollectionModifier().read(0);
   }

   public void setMetadata(List<WrappedWatchableObject> value) {
      this.handle.getWatchableCollectionModifier().write(0, value);
   }

   static {
      TYPE = Server.ENTITY_METADATA;
   }
}
