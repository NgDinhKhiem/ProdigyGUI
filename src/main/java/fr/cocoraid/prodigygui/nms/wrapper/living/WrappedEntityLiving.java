package fr.cocoraid.prodigygui.nms.wrapper.living;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import fr.cocoraid.prodigygui.nms.EIDGen;
import fr.cocoraid.prodigygui.nms.wrapper.packet.WrapperPlayServerEntityDestroy;
import fr.cocoraid.prodigygui.nms.wrapper.packet.WrapperPlayServerEntityEquipment;
import fr.cocoraid.prodigygui.nms.wrapper.packet.WrapperPlayServerEntityHeadRotation;
import fr.cocoraid.prodigygui.nms.wrapper.packet.WrapperPlayServerEntityMetadata;
import fr.cocoraid.prodigygui.nms.wrapper.packet.WrapperPlayServerEntityTeleport;
import fr.cocoraid.prodigygui.nms.wrapper.packet.WrapperPlayServerSpawnEntityLiving;
import fr.cocoraid.prodigygui.utils.UtilMath;
import fr.cocoraid.prodigygui.utils.VersionChecker;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class WrappedEntityLiving {
   private static Serializer itemSerializer;
   private static Serializer intSerializer;
   private static Serializer byteSerializer;
   private static Serializer stringSerializer;
   private static Serializer booleanSerializer;
   private WrapperPlayServerSpawnEntityLiving spawnPacket;
   private WrapperPlayServerEntityDestroy destroyPacket;
   private WrapperPlayServerEntityMetadata metaPacket;
   private WrapperPlayServerEntityTeleport teleportPacket;
   private WrapperPlayServerEntityHeadRotation yawPacket;
   private WrappedDataWatcher dataWatcher;
   protected Map<ItemSlot, WrapperPlayServerEntityEquipment> equipments = new HashMap();
   private Location location;
   private Player player;
   private int typeID;
   private int id;

   public WrappedEntityLiving(Location location, Player player, int typeID) {
      this.location = location;
      this.id = EIDGen.generateEID();
      this.typeID = typeID;
      this.player = player;
      this.spawnPacket = new WrapperPlayServerSpawnEntityLiving();
      this.spawnPacket.setEntityID(this.id);
      this.spawnPacket.setType(typeID);
      this.spawnPacket.setPitch(location.getPitch());
      this.spawnPacket.setHeadPitch(location.getPitch());
      this.spawnPacket.setYaw(location.getYaw());
      this.spawnPacket.setX(location.getX());
      this.spawnPacket.setY(location.getY());
      this.spawnPacket.setZ(location.getZ());
      this.spawnPacket.setUUID(UUID.randomUUID());
      this.yawPacket = new WrapperPlayServerEntityHeadRotation();
      this.yawPacket.setEntityID(this.id);
      this.yawPacket.setHeadYaw(UtilMath.toPackedByte(location.getYaw()));
      this.destroyPacket = new WrapperPlayServerEntityDestroy();
      this.destroyPacket.setEntityIds(new Integer[]{this.id});
      this.dataWatcher = new WrappedDataWatcher();
      this.metaPacket = new WrapperPlayServerEntityMetadata();
      this.metaPacket.setEntityID(this.id);
      this.teleportPacket = new WrapperPlayServerEntityTeleport();
      ItemSlot[] var4 = ItemSlot.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ItemSlot itemSlot = var4[var6];
         WrapperPlayServerEntityEquipment equip = new WrapperPlayServerEntityEquipment();
         equip.setEntityID(this.id);
         this.equipments.put(itemSlot, equip);
      }

   }

   public void teleport(Location newLocation) {
      this.teleportPacket.setEntityID(this.id);
      this.teleportPacket.setX(newLocation.getX());
      this.teleportPacket.setY(newLocation.getY());
      this.teleportPacket.setZ(newLocation.getZ());
      this.teleportPacket.setYaw(newLocation.getYaw());
      this.teleportPacket.setPitch(newLocation.getPitch());
      this.teleportPacket.sendPacket(this.player);
      this.location = newLocation;
      this.spawnPacket.setPitch(this.location.getPitch());
      this.spawnPacket.setHeadPitch(this.location.getPitch());
      this.spawnPacket.setYaw(this.location.getYaw());
      this.spawnPacket.setX(this.location.getX());
      this.spawnPacket.setY(this.location.getY());
      this.spawnPacket.setZ(this.location.getZ());
      this.yawPacket.setHeadYaw(UtilMath.toPackedByte(this.location.getYaw()));
   }

   public void fakeTeleport(Location l) {
      this.teleportPacket.setEntityID(this.id);
      this.teleportPacket.setX(l.getX());
      this.teleportPacket.setY(l.getY());
      this.teleportPacket.setZ(l.getZ());
      this.teleportPacket.setYaw(l.getYaw());
      this.teleportPacket.setPitch(l.getPitch());
      this.teleportPacket.sendPacket(this.player);
   }

   public void updateYaw() {
      this.yawPacket.sendPacket(this.player);
   }

   public void spawnClient(Player client) {
      this.spawnPacket.sendPacket(client);
      if (this.typeID != 0) {
         this.yawPacket.sendPacket(client);
      }

   }

   public void updateForClient(Player client) {
   }

   public void despawnClient(Player client) {
      this.destroyPacket.sendPacket(client);
   }

   public void spawn() {
      this.spawnPacket.sendPacket(this.player);
      if (this.typeID != 0) {
         this.yawPacket.sendPacket(this.player);
      }

   }

   public void setCustomName(String name) {
      if (VersionChecker.isHigherOrEqualThan(VersionChecker.v1_13_R1)) {
         Optional<?> opt = Optional.of(WrappedChatComponent.fromChatMessage(name)[0].getHandle());
         this.dataWatcher.setObject(new WrappedDataWatcherObject(2, Registry.getChatComponentSerializer(true)), opt);
      } else {
         this.dataWatcher.setObject(new WrappedDataWatcherObject(2, stringSerializer), name);
      }

   }

   public void setCustomNameVisible(boolean visible) {
      this.dataWatcher.setObject(new WrappedDataWatcherObject(3, booleanSerializer), visible);
   }

   public void setInvisible(boolean invisible) {
      this.setDataWatcherObject(Byte.class, 0, Byte.valueOf((byte)(invisible ? 32 : 0)));
   }

   public void despawn() {
      this.destroyPacket.sendPacket(this.player);
   }

   public void equip(ItemSlot slot, ItemStack item) {
      WrapperPlayServerEntityEquipment equipPacket = (WrapperPlayServerEntityEquipment)this.equipments.get(slot);
      if (VersionChecker.isHigherOrEqualThan(VersionChecker.v1_16_R1)) {
         equipPacket.setItem(slot, item);
      } else {
         equipPacket.setItem(item);
         equipPacket.setSlot(slot);
      }

      equipPacket.sendPacket(this.player);
   }

   public void setDataWatcherObject(Class<?> type, int objectIndex, Object object) {
      WrappedDataWatcherObject watcherObject = new WrappedDataWatcherObject(objectIndex, Registry.get(type));
      this.dataWatcher.setObject(watcherObject, object);
   }

   public void sendUpdatedmetatada() {
      this.metaPacket.setMetadata(this.dataWatcher.getWatchableObjects());
      this.metaPacket.sendPacket(this.player);
   }

   public WrappedDataWatcher getDataWatcher() {
      return this.dataWatcher;
   }

   public void setLocation(Location location) {
      this.spawnPacket.setPitch(location.getPitch());
      this.spawnPacket.setHeadPitch(location.getPitch());
      this.spawnPacket.setYaw(location.getYaw());
      this.spawnPacket.setX(location.getX());
      this.spawnPacket.setY(location.getY());
      this.spawnPacket.setZ(location.getZ());
      this.location = location;
      this.yawPacket.setHeadYaw(UtilMath.toPackedByte(location.getYaw()));
   }

   public int getId() {
      return this.id;
   }

   public Location getLocation() {
      return this.location;
   }

   static {
      if (VersionChecker.isHigherOrEqualThan(VersionChecker.v1_9_R1)) {
         itemSerializer = Registry.get(MinecraftReflection.getItemStackClass());
         intSerializer = Registry.get(Integer.class);
         byteSerializer = Registry.get(Byte.class);
         stringSerializer = Registry.get(String.class);
         booleanSerializer = Registry.get(Boolean.class);
      }

   }
}
