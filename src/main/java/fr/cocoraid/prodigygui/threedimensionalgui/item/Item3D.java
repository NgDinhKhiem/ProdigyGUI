package fr.cocoraid.prodigygui.threedimensionalgui.item;

import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import fr.cocoraid.prodigygui.bridge.PlaceholderAPIBridge;
import fr.cocoraid.prodigygui.nms.wrapper.living.WrapperEntityArmorStand;
import fr.cocoraid.prodigygui.nms.wrapper.living.WrapperEntitySlime;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ItemData;
import fr.cocoraid.prodigygui.utils.VersionChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Item3D {
   private Player player;
   private Location location;
   private WrapperEntityArmorStand itemDisplay;
   private WrapperEntityArmorStand displayName;
   private WrapperEntitySlime selector;
   private ItemData data;
   private String name;
   private ItemStack item;
   private boolean spawned = false;
   private boolean enable;
   private boolean isSmall = false;
   private float yawRotation = 0.0F;

   public Item3D(Player player, ItemData data) {
      this.data = data;
      this.player = player;
      this.item = data.getDisplayItem();
      this.name = data.getDisplayname();
      this.enable = true;
      Location temp = player.getEyeLocation();
      temp.setPitch(0.0F);
      this.location = player.getEyeLocation().add(temp.getDirection());
      this.displayName = new WrapperEntityArmorStand(temp, player);
      this.displayName.setCustomName(PlaceholderAPIBridge.setPlaceholders(this.name, player));
      this.displayName.setCustomNameVisible(true);
      this.displayName.setInvisible(true);
      this.displayName.setMarker(true);
      this.displayName.setSmall(true);
      this.itemDisplay = new WrapperEntityArmorStand(temp, player);
      this.itemDisplay.setInvisible(true);
      this.selector = new WrapperEntitySlime(temp, player);
      this.selector.setSize(2);
      this.selector.setInvisible(true);
   }

   public void setSmall() {
      this.itemDisplay.setSmall(true);
      this.isSmall = true;
   }

   public Item3D setPosition(Location loc) {
      if (this.item.getType().isBlock() || this.item.getType() == Material.PLAYER_HEAD || this.item.getType() == Material.CREEPER_HEAD || this.item.getType() == Material.DRAGON_HEAD || this.item.getType() == Material.ZOMBIE_HEAD) {
         this.setSmall();
      }

      Location itemLoc = loc.clone().subtract(0.0D, this.isSmall ? 0.7D : 2.2D, 0.0D);
      if (VersionChecker.isHigherOrEqualThan(VersionChecker.v1_12_R1) && this.item.getType().isItem() && !this.item.getType().isBlock() || VersionChecker.isLowerOrEqualThan(VersionChecker.v1_11_R1) && !this.item.getType().isBlock()) {
         itemLoc.add(itemLoc.getDirection().normalize().multiply(0.2D));
      }

      double tosubDisplay = 0.3D;
      Location l = itemLoc.clone().subtract(0.0D, tosubDisplay, 0.0D);
      l.setYaw(l.getYaw() + (float)this.data.getRotation());
      this.itemDisplay.setLocation(l);
      this.itemDisplay.setMarker(true);
      this.itemDisplay.spawn();
      this.itemDisplay.equip(ItemSlot.HEAD, this.item);
      this.itemDisplay.sendUpdatedmetatada();
      this.displayName.setLocation(loc);
      this.displayName.setMarker(true);
      this.displayName.spawn();
      this.displayName.sendUpdatedmetatada();
      this.selector.setLocation(loc.clone().subtract(0.0D, 0.3D, 0.0D));
      this.selector.spawn();
      this.selector.sendUpdatedmetatada();
      this.location = loc;
      this.spawned = true;
      return this;
   }

   public void move(boolean back) {
      if (back) {
         this.itemDisplay.fakeTeleport(this.itemDisplay.getLocation());
         this.selector.fakeTeleport(this.selector.getLocation());
         this.displayName.teleport(this.displayName.getLocation());
      } else {
         Location l = this.getLocation().clone();
         Vector v = this.player.getLocation().toVector().subtract(l.toVector()).normalize();
         l.setDirection(v);
         l.setPitch(0.0F);
         Vector toadd = l.getDirection().multiply(0.3D);
         this.itemDisplay.fakeTeleport(this.itemDisplay.getLocation().clone().add(toadd));
         this.displayName.fakeTeleport(this.displayName.getLocation().clone().add(toadd));
         this.selector.fakeTeleport(this.selector.getLocation().clone().add(toadd));
      }

   }

   public void teleport(Location loc) {
      Location l = this.isSmall ? loc.clone().add(0.0D, 1.3D, 0.0D) : loc;
      this.itemDisplay.teleport(l);
      this.displayName.teleport(loc.clone().add(0.0D, 0.30000001192092896D, 0.0D));
      this.selector.teleport(loc.clone().add(0.0D, 1.8D, 0.0D));
      this.location = loc;
   }

   public Location getLocation() {
      return this.location;
   }

   public void remove() {
      if (this.itemDisplay != null) {
         this.itemDisplay.despawn();
      }

      if (this.displayName != null) {
         this.displayName.despawn();
      }

      if (this.selector != null) {
         this.selector.despawn();
      }

      this.spawned = false;
   }

   public String getName() {
      return this.name;
   }

   public Player getPlayer() {
      return this.player;
   }

   public boolean isSpawned() {
      return this.spawned;
   }

   public boolean isEnable() {
      return this.enable;
   }

   public WrapperEntitySlime getSelector() {
      return this.selector;
   }

   public float getYawRotation() {
      return this.yawRotation;
   }

   public ItemStack getItem() {
      return this.item;
   }

   public ItemData getData() {
      return this.data;
   }
}
