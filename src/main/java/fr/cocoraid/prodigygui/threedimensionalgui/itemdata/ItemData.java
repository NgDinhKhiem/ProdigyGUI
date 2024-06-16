package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import fr.cocoraid.prodigygui.utils.CC;
import fr.cocoraid.prodigygui.utils.SkullCreator;
import fr.cocoraid.prodigygui.utils.UtilItem;
import fr.cocoraid.prodigygui.utils.VersionChecker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemData {
   private String displayname;
   private ItemStack displayItem;
   private List<String> command;
   private int price;
   private String permission;
   private String nopermissionmessage;
   private SoundData soundData;
   private ParticleData particleData;
   private String lore;
   private int rotation;

   public ItemData(String displayname, Material ID) {
      this.displayname = CC.colored(displayname);
      this.displayItem = new ItemStack(ID);
   }

   public ItemData(String displayname, String skulltexture, boolean mojang) {
      this.displayname = CC.colored(displayname);
      if (mojang) {
         this.displayItem = UtilItem.getMojangSkull(skulltexture);
      } else {
         if (VersionChecker.isHigherOrEqualThan(VersionChecker.v1_16_R1)) {
            this.displayItem = UtilItem.skullTextured(skulltexture);
         } else {
            this.displayItem = SkullCreator.itemFromBase64(skulltexture);
         }

      }
   }

   public void setCommand(String command) {
      if (command == null) {
         this.command = null;
      } else {
         this.command = new ArrayList(Arrays.asList(command.split("; ")));
      }

   }

   public void setPrice(int price) {
      this.price = price;
   }

   public void setPermission(String permission) {
      this.permission = permission;
   }

   public void setNopermissionmessage(String nopermissionmessage) {
      this.nopermissionmessage = CC.colored(nopermissionmessage);
   }

   public void setSoundData(SoundData soundData) {
      this.soundData = soundData;
   }

   public void setLore(String lore) {
      this.lore = CC.colored(lore);
   }

   public String getDisplayname() {
      return this.displayname;
   }

   public List<String> getCommands() {
      return this.command;
   }

   public int getPrice() {
      return this.price;
   }

   public String getNopermissionmessage() {
      return this.nopermissionmessage;
   }

   public String getPermission() {
      return this.permission;
   }

   public String getLore() {
      return this.lore;
   }

   public void setParticleData(ParticleData particleData) {
      this.particleData = particleData;
   }

   public ParticleData getParticleData() {
      return this.particleData;
   }

   public ItemStack getDisplayItem() {
      return this.displayItem;
   }

   public SoundData getSoundData() {
      return this.soundData;
   }

   public int getRotation() {
      return this.rotation;
   }

   public void setRotation(int rotation) {
      this.rotation = rotation;
   }
}
