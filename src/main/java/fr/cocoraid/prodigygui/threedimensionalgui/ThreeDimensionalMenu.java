package fr.cocoraid.prodigygui.threedimensionalgui;

import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ItemData;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ParticleData;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.SoundData;
import fr.cocoraid.prodigygui.utils.CC;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Material;

public class ThreeDimensionalMenu {
   private static LinkedList<ThreeDimensionalMenu> menus = new LinkedList();
   private LinkedList<ItemData> itemDataList;
   private LinkedList<ItemData> barItemsList;
   private double radius;
   private String title;
   private String fileName;
   private String previousMenu;
   private String openItemName;
   private int angle_step = 35;
   private SoundData defaultClickSound;
   private SoundData defaultChangeSound;
   private ParticleData defaultClickParticle;
   private String permission;
   private List<String> commands;
   private Material openItem;

   public ThreeDimensionalMenu(String filename) {
      this.fileName = filename;
   }

   public ThreeDimensionalMenu setRadius(double radius) {
      this.radius = radius;
      return this;
   }

   public ThreeDimensionalMenu setAngleStep(int angle_step) {
      this.angle_step = angle_step;
      return this;
   }

   public ThreeDimensionalMenu setTitle(String title) {
      this.title = CC.colored(title);
      return this;
   }

   public ThreeDimensionalMenu setOpenItem(Material openItem) {
      this.openItem = openItem;
      return this;
   }

   public ThreeDimensionalMenu setCommand(List<String> commands) {
      this.commands = commands;
      return this;
   }

   public ThreeDimensionalMenu setOpenItemName(String openItemName) {
      this.openItemName = openItemName;
      return this;
   }

   public ThreeDimensionalMenu getPreviousMenu() {
      return this.previousMenu == null ? null : (ThreeDimensionalMenu)getMenus().stream().filter((m) -> {
         return m.getFileName().equalsIgnoreCase(this.previousMenu);
      }).findAny().orElseGet(() -> {
         return null;
      });
   }

   public void setPreviousMenu(String previousMenu) {
      this.previousMenu = previousMenu;
   }

   public double getRadius() {
      return this.radius;
   }

   public String getTitle() {
      return this.title;
   }

   public Material getOpenItem() {
      return this.openItem;
   }

   public List<String> getCommands() {
      return this.commands;
   }

   public void setPermission(String permission) {
      this.permission = permission;
   }

   public String getPermission() {
      return this.permission;
   }

   public String getOpenItemName() {
      return this.openItemName;
   }

   public LinkedList<ItemData> getItemDataList() {
      return this.itemDataList;
   }

   public String getFileName() {
      return this.fileName;
   }

   public void setItemDataList(LinkedList<ItemData> itemDataList) {
      this.itemDataList = itemDataList;
   }

   public void setBarItemsList(LinkedList<ItemData> barItemsList) {
      this.barItemsList = barItemsList;
   }

   public LinkedList<ItemData> getBarItemsList() {
      return this.barItemsList;
   }

   public static List<ThreeDimensionalMenu> getMenus() {
      return menus;
   }

   public static ThreeDimensionalMenu getMenu(String menuName) {
      return (ThreeDimensionalMenu)getMenus().stream().filter((m) -> {
         return m.getFileName().equalsIgnoreCase(menuName);
      }).findAny().get();
   }

   public int getAngleStep() {
      return this.angle_step;
   }

   public ThreeDimensionalMenu setDefaultClickParticle(ParticleData defaultClickParticle) {
      this.defaultClickParticle = defaultClickParticle;
      return this;
   }

   public ThreeDimensionalMenu setDefaultClickSound(SoundData defaultClickSound) {
      this.defaultClickSound = defaultClickSound;
      return this;
   }

   public void setDefaultChangeSound(SoundData defaultChangeSound) {
      this.defaultChangeSound = defaultChangeSound;
   }

   public SoundData getDefaultChangeSound() {
      return this.defaultChangeSound;
   }

   public ParticleData getDefaultClickParticle() {
      return this.defaultClickParticle;
   }

   public SoundData getDefaultClickSound() {
      return this.defaultClickSound;
   }
}
