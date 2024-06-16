package fr.cocoraid.prodigygui.nms.wrapper.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import fr.cocoraid.prodigygui.nms.AbstractPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class WrapperPlayServerEntityEquipment extends AbstractPacket {
   public static final PacketType TYPE;
   private List<Pair<ItemSlot, ItemStack>> itemList = new ArrayList();

   public WrapperPlayServerEntityEquipment() {
      super(new PacketContainer(TYPE), TYPE);
      this.handle.getModifier().writeDefaults();
   }

   public WrapperPlayServerEntityEquipment(PacketContainer packet) {
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

   public ItemSlot getSlot() {
      return (ItemSlot)this.handle.getItemSlots().read(0);
   }

   public void setSlot(ItemSlot value) {
      this.handle.getItemSlots().write(0, value);
   }

   public void setSlot(int value) {
      this.handle.getIntegers().write(1, value);
   }

   public ItemStack getItem() {
      return (ItemStack)this.handle.getItemModifier().read(0);
   }

   public void setItem(ItemStack value) {
      this.handle.getItemModifier().write(0, value);
   }

   public void setItem(ItemSlot slot, ItemStack stack) {
      Pair<ItemSlot, ItemStack> itemPair = new Pair(slot, stack);
      Optional<Pair<ItemSlot, ItemStack>> optPair = this.itemList.stream().filter((entry) -> {
         return entry.getFirst() == slot;
      }).findFirst();
      if (optPair.isPresent()) {
         this.itemList.remove(optPair.get());
      }

      this.itemList.add(itemPair);
      this.handle.getSlotStackPairLists().write(0, this.itemList);
   }

   static {
      TYPE = Server.ENTITY_EQUIPMENT;
   }
}
