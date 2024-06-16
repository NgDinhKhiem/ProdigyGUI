package fr.cocoraid.prodigygui.utils;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCreator {
   /** @deprecated */
   @Deprecated
   public static ItemStack itemFromName(String name) {
      ItemStack item = getPlayerSkullItem();
      return itemWithName(item, name);
   }

   /** @deprecated */
   @Deprecated
   public static ItemStack itemWithName(ItemStack item, String name) {
      notNull(item, "item");
      notNull(name, "name");
      return Bukkit.getUnsafe().modifyItemStack(item, "{SkullOwner:\"" + name + "\"}");
   }

   public static ItemStack itemFromUuid(UUID id) {
      ItemStack item = getPlayerSkullItem();
      return itemWithUuid(item, id);
   }

   public static ItemStack itemWithUuid(ItemStack item, UUID id) {
      notNull(item, "item");
      notNull(id, "id");
      SkullMeta meta = (SkullMeta)item.getItemMeta();
      meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack itemFromBase64(String base64, String name) {
      ItemStack item = itemWithBase64(getPlayerSkullItem(), base64);
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(name);
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack itemFromBase64(String base64) {
      ItemStack item = getPlayerSkullItem();
      return itemWithBase64(item, base64);
   }

   public static ItemStack itemWithBase64(ItemStack item, String base64) {
      notNull(item, "item");
      notNull(base64, "base64");
      UUID hashAsId = new UUID((long)base64.hashCode(), (long)base64.hashCode());
      return Bukkit.getUnsafe().modifyItemStack(item, "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" + base64 + "\"}]}}}");
   }

   private static ItemStack getPlayerSkullItem() {
      return VersionChecker.isHigherOrEqualThan(VersionChecker.v1_13_R2) ? new ItemStack(Material.PLAYER_HEAD) : new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short)3);
   }

   private static void notNull(Object o, String name) {
      if (o == null) {
         throw new NullPointerException(name + " should not be null!");
      }
   }
}
