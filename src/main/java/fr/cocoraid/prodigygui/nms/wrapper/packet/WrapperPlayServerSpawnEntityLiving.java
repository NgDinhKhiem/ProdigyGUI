package fr.cocoraid.prodigygui.nms.wrapper.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import fr.cocoraid.prodigygui.nms.AbstractPacket;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerSpawnEntityLiving extends AbstractPacket {
   public static final PacketType TYPE;
   private static PacketConstructor entityConstructor;

   public WrapperPlayServerSpawnEntityLiving() {
      super(new PacketContainer(TYPE), TYPE);
      this.handle.getModifier().writeDefaults();
   }

   public WrapperPlayServerSpawnEntityLiving(PacketContainer packet) {
      super(packet, TYPE);
   }

   public WrapperPlayServerSpawnEntityLiving(Entity entity) {
      super(fromEntity(entity), TYPE);
   }

   private static PacketContainer fromEntity(Entity entity) {
      if (entityConstructor == null) {
         entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, new Object[]{entity});
      }

      return entityConstructor.createPacket(new Object[]{entity});
   }

   public int getEntityID() {
      return (Integer)this.handle.getIntegers().read(0);
   }

   public Entity getEntity(World world) {
      return (Entity)this.handle.getEntityModifier(world).read(0);
   }

   public Entity getEntity(PacketEvent event) {
      return this.getEntity(event.getPlayer().getWorld());
   }

   public void setEntityID(int value) {
      this.handle.getIntegers().write(0, value);
   }

   public int getTypeID() {
      return (Integer)this.handle.getIntegers().read(1);
   }

   public void setType(int typeID) {
      this.handle.getIntegers().write(1, typeID);
   }

   public double getX() {
      return (Double)this.handle.getDoubles().read(0);
   }

   public void setX(double value) {
      this.handle.getDoubles().write(0, value);
   }

   public double getY() {
      return (Double)this.handle.getDoubles().read(1);
   }

   public void setY(double value) {
      this.handle.getDoubles().write(1, value);
   }

   public double getZ() {
      return (Double)this.handle.getDoubles().read(2);
   }

   public void setZ(double value) {
      this.handle.getDoubles().write(2, value);
   }

   public float getYaw() {
      return (float)(Byte)this.handle.getBytes().read(0) * 360.0F / 256.0F;
   }

   public void setYaw(float value) {
      this.handle.getBytes().write(0, (byte)((int)(value * 256.0F / 360.0F)));
   }

   public float getPitch() {
      return (float)(Byte)this.handle.getBytes().read(1) * 360.0F / 256.0F;
   }

   public void setPitch(float value) {
      this.handle.getBytes().write(1, (byte)((int)(value * 256.0F / 360.0F)));
   }

   public float getHeadPitch() {
      return (float)(Byte)this.handle.getBytes().read(2) * 360.0F / 256.0F;
   }

   public void setHeadPitch(float value) {
      this.handle.getBytes().write(2, (byte)((int)(value * 256.0F / 360.0F)));
   }

   public WrappedDataWatcher getMetadata() {
      return (WrappedDataWatcher)this.handle.getDataWatcherModifier().read(0);
   }

   public void setMetadata(WrappedDataWatcher value) {
      this.handle.getDataWatcherModifier().write(0, value);
   }

   public void setUUID(UUID uuid) {
      this.handle.getUUIDs().write(0, uuid);
   }

   static {
      TYPE = Server.SPAWN_ENTITY_LIVING;
   }
}
