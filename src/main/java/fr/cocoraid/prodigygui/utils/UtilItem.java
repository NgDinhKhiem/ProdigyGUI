package fr.cocoraid.prodigygui.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class UtilItem {
   public static ItemStack skullTextured(String base64) {
      UUID id = UUID.nameUUIDFromBytes(base64.getBytes());
      int less = (int)id.getLeastSignificantBits();
      int most = (int)id.getMostSignificantBits();
      return Bukkit.getUnsafe().modifyItemStack(new ItemStack(Material.PLAYER_HEAD), "{SkullOwner:{Id:[I;" + less * most + "," + (less >> 23) + "," + most / less + "," + most * 8731 + "],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" + base64 + "\"}]}}}");
   }

   @Nullable
   public static ItemStack getMojangSkull(String textureURL) {
      ItemStack item = new ItemStack(Material.PLAYER_HEAD);
      SkullMeta meta = (SkullMeta)item.getItemMeta();
      textureURL = "http://textures.minecraft.net/texture/" + textureURL;
      GameProfile gameProfile = new GameProfile(UUID.randomUUID(), (String)null);
      byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", textureURL).getBytes());
      gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData)));
      Field profileField = null;

      try {
         profileField = meta.getClass().getDeclaredField("profile");
         profileField.setAccessible(true);
         profileField.set(meta, gameProfile);
      } catch (NoSuchFieldException | IllegalAccessException var7) {
         var7.printStackTrace();
         return null;
      }

      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack getCustomSkull(String url) {
      ItemStack head = new ItemStack(Material.PLAYER_HEAD);
      if (url.isEmpty()) {
         return head;
      } else {
         SkullMeta skullMeta = (SkullMeta)head.getItemMeta();
         GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
         profile.getProperties().put("textures", new Property("textures", url));

         try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
         } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException var5) {
            var5.printStackTrace();
         }

         head.setItemMeta(skullMeta);
         return head;
      }
   }
}
