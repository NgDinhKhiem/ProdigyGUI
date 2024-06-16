package fr.cocoraid.prodigygui.loader;

import fr.cocoraid.prodigygui.ProdigyGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ColoredParticleData;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ItemData;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.NormalParticleData;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ParticleData;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.SoundData;
import fr.cocoraid.prodigygui.utils.PluginFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public class FileLoader {
   private ProdigyGUI plugin;
   private boolean debug = false;
   private static final String MENU_TITLE = "menu-settings.title";
   private static final String MENU_RADIUS_DISTANCE = "menu-settings.radius-distance";
   private static final String MENU_PERMISSION = "menu-settings.permission";
   private static final String MENU_ANGLE_STEP = "menu-settings.angle-step";
   private static final String MENU_COMMAND = "menu-settings.command";
   private static final String PREVIOUS_MENU = "menu-settings.previous-menu";
   private static final String OPEN_ITEM_MATERIAL = "menu-settings.open-with-item.id";
   private static final String OPEN_ITEM_NAME = "menu-settings.open-with-item.name";
   private static final String DEFAULT_CLICK_SOUND_PATH = "menu-settings.slot-click-sound";
   private static final String DEFAULT_CHANGE_SOUND_PATH = "menu-settings.slot-change-sound";
   private static final String SOUND_NAME = "name";
   private static final String SOUND_VOLUME = "volume";
   private static final String SOUND_PITCH = "pitch";
   private static final String DEFAULT_CLICK_PARTICLE_PATH = "menu-settings.slot-click-particle";
   private static final String PARTICLE_NAME = "name";
   private static final String PARTICLE_AMOUNT = "amount";
   private static final String PARTICLE_OFFSET_X = "offsetX";
   private static final String PARTICLE_OFFSET_Y = "offsetY";
   private static final String PARTICLE_OFFSET_Z = "offsetZ";
   private static final String PARTICLE_SPEED = "speed";
   private static final String PARTICLE_RED = "red";
   private static final String PARTICLE_GREEN = "green";
   private static final String PARTICLE_BLUE = "blue";
   private static final String PARTICLE_RADIUS = "radius";
   public static final String ID = "ID";
   public static final String SKULL_TEXTURE = "SKULL-TEXTURE";
   public static final String SKULL_URL = "SKULL-URL";
   public static final String NAME = "NAME";
   public static final String LORE = "LORE";
   public static final String COMMAND = "COMMAND";
   public static final String PRICE = "PRICE";
   public static final String PERMISSION = "PERMISSION";
   public static final String PERMISSION_MESSAGE = "PERMISSION-MESSAGE";
   public static final String SOUND = "SOUND";
   public static final String PARTICLE = "PARTICLE";
   public static final String ROTATION = "ROTATION";

   public FileLoader(ProdigyGUI plugin) {
      this.plugin = plugin;
      File menusFolder = new File(ProdigyGUI.getInstance().getDataFolder(), "menu");
      if (!menusFolder.isDirectory()) {
         menusFolder.mkdirs();

         try {
            plugin.saveResource("menu" + File.separator + "example.yml", false);
         } catch (Exception var4) {
         }
      }

      LinkedList<PluginFile> list = this.loadFileMenus();
      list.forEach((file) -> {
         ThreeDimensionalMenu menu = this.loadMenuSettings(file);
         menu.setBarItemsList(this.loadItemBarData(file));
         menu.setItemDataList(this.loadItemDatas(file));
         ThreeDimensionalMenu.getMenus().add(menu);
         if (this.debug) {
            System.out.println("settings .... :");
            System.out.println("menu " + menu.getTitle());
            System.out.println("open item " + menu.getOpenItem());
            System.out.println("item name " + menu.getOpenItemName());
            System.out.println("command " + menu.getCommands());
            System.out.println("Items  .... :");
            menu.getItemDataList().forEach((item) -> {
               System.out.println("displayname " + item.getDisplayname());
               System.out.println("lore " + item.getLore());
               System.out.println("no perm message " + item.getNopermissionmessage());
               System.out.println("perm " + item.getPermission());
               System.out.println("price " + item.getPrice());
            });
         }

      });
   }

   private LinkedList<PluginFile> checkForMenus(File file) {
      LinkedList<PluginFile> list = new LinkedList();
      if (file.isDirectory()) {
         File[] var3 = file.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File subFile = var3[var5];
            list.addAll(this.checkForMenus(subFile));
         }
      } else if (file.isFile() && file.getName().endsWith(".yml")) {
         list.add(new PluginFile(this.plugin, file));
      }

      return list;
   }

   private LinkedList<PluginFile> loadFileMenus() {
      File menusFolder = new File(this.plugin.getDataFolder(), "menu");
      LinkedList<PluginFile> menusList = this.checkForMenus(menusFolder);
      Iterator var3 = menusList.iterator();

      while(var3.hasNext()) {
         PluginFile menuConfig = (PluginFile)var3.next();

         try {
            menuConfig.load();
         } catch (IOException var6) {
            var6.printStackTrace();
            System.out.println("I/O error while loading the menu \"" + menuConfig.getFileName() + "\". Is the file in use?");
         } catch (InvalidConfigurationException var7) {
            var7.printStackTrace();
            System.out.println("Invalid YAML configuration for the menu \"" + menuConfig.getFileName() + "\". Please look at the error above, or use an online YAML parser (google is your friend).");
         }
      }

      return menusList;
   }

   private ThreeDimensionalMenu loadMenuSettings(PluginFile file) {
      ThreeDimensionalMenu menu = (new ThreeDimensionalMenu(file.getFileName())).setTitle(file.getString("menu-settings.title")).setRadius(file.getDouble("menu-settings.radius-distance"));
      if (file.isString("menu-settings.command")) {
         List<String> list = new ArrayList(Arrays.asList(file.getString("menu-settings.command").split("; ")));
         menu.setCommand(list);
      }

      if (file.isString("menu-settings.open-with-item.id")) {
         menu.setOpenItem(Material.valueOf(file.getString("menu-settings.open-with-item.id").toUpperCase()));
      }

      if (file.isInt("menu-settings.angle-step")) {
         menu.setAngleStep(file.getInt("menu-settings.angle-step"));
      }

      if (file.isString("menu-settings.open-with-item.name")) {
         menu.setOpenItemName(file.getString("menu-settings.open-with-item.name"));
      }

      if (file.isString("menu-settings.previous-menu") && !file.getString("menu-settings.previous-menu").isEmpty()) {
         menu.setPreviousMenu(file.getString("menu-settings.previous-menu"));
      }

      if (file.isString("menu-settings.permission") && !file.getString("menu-settings.permission").isEmpty()) {
         menu.setPermission(file.getString("menu-settings.permission"));
      }

      menu.setDefaultClickSound(this.soundDataConverter(file, "menu-settings.slot-click-sound"));
      menu.setDefaultChangeSound(this.soundDataConverter(file, "menu-settings.slot-change-sound"));
      menu.setDefaultClickParticle(this.particleDataConverter(file, "menu-settings.slot-click-particle"));
      return menu;
   }

   private ParticleData particleDataConverter(PluginFile file, String mainpath) {
      ConfigurationSection sectionParticle = file.getConfigurationSection(mainpath);
      if (sectionParticle != null) {
         Particle particle = Particle.valueOf(sectionParticle.getString("name"));
         Object data;
         if (ColoredParticleData.getColorableParticles().contains(particle)) {
            ColoredParticleData coloredData = new ColoredParticleData(particle);
            if (sectionParticle.isInt("red")) {
               coloredData.setR(sectionParticle.getInt("red"));
            }

            if (sectionParticle.isInt("green")) {
               coloredData.setG(sectionParticle.getInt("green"));
            }

            if (sectionParticle.isInt("blue")) {
               coloredData.setB(sectionParticle.getInt("blue"));
            }

            if (sectionParticle.isDouble("radius")) {
               coloredData.setRadius(sectionParticle.getDouble("radius"));
            }

            data = coloredData;
         } else {
            NormalParticleData normalData = new NormalParticleData(particle);
            if (sectionParticle.isDouble("offsetX")) {
               normalData.setOffsetX(sectionParticle.getDouble("offsetX"));
            }

            if (sectionParticle.isDouble("offsetY")) {
               normalData.setOffsetY(sectionParticle.getDouble("offsetY"));
            }

            if (sectionParticle.isDouble("offsetZ")) {
               normalData.setOffsetZ(sectionParticle.getDouble("offsetZ"));
            }

            if (sectionParticle.isDouble("speed")) {
               normalData.setSpeed(sectionParticle.getDouble("speed"));
            }

            data = normalData;
         }

         if (data != null && sectionParticle.isInt("amount")) {
            ((ParticleData)data).setAmount(sectionParticle.getInt("amount"));
         }

         return (ParticleData)data;
      } else {
         return null;
      }
   }

   private SoundData soundDataConverter(PluginFile file, String mainpath) {
      ConfigurationSection section = file.getConfigurationSection(mainpath);
      if (section != null) {
         SoundData soundData = new SoundData(Sound.valueOf(section.getString("name")));
         if (section.isDouble("volume")) {
            soundData.setVolume((float)section.getDouble("volume"));
         }

         if (section.isDouble("pitch")) {
            soundData.setPitch((float)section.getDouble("pitch"));
         }

         return soundData;
      } else {
         return null;
      }
   }

   private LinkedList<ItemData> loadItemBarData(PluginFile file) {
      LinkedList<ItemData> itemdatas = new LinkedList();
      Iterator var3 = file.getKeys(false).iterator();

      while(true) {
         String subSectionName;
         do {
            if (!var3.hasNext()) {
               return itemdatas;
            }

            subSectionName = (String)var3.next();
         } while(!subSectionName.equalsIgnoreCase("bar-items"));

         ConfigurationSection section = file.getConfigurationSection(subSectionName);
         Iterator var6 = section.getKeys(false).iterator();

         while(var6.hasNext()) {
            String item = (String)var6.next();
            ConfigurationSection s = file.getConfigurationSection(section.getCurrentPath() + "." + item);
            ItemData itemdata = null;
            if (s.isString("ID")) {
               itemdata = new ItemData(s.getString("NAME"), Material.valueOf(s.getString("ID").toUpperCase()));
            } else if (s.isSet("SKULL-TEXTURE")) {
               itemdata = new ItemData(s.getString("NAME"), s.getString("SKULL-TEXTURE"), false);
            } else if (s.isSet("SKULL-URL")) {
               itemdata = new ItemData(s.getString("NAME"), s.getString("SKULL-URL"), true);
            } else {
               System.out.println("ProdigyGUI ERROR: You need at least ID or SKULL TEXTURE to create any item");
            }

            itemdata.setCommand(s.getString("COMMAND"));
            itemdata.setLore(s.getString("LORE"));
            itemdata.setPrice(s.getInt("PRICE"));
            itemdata.setPermission(s.getString("PERMISSION"));
            itemdata.setNopermissionmessage(s.getString("PERMISSION-MESSAGE"));
            itemdata.setParticleData(this.particleDataConverter(file, item + "." + "PARTICLE"));
            itemdata.setSoundData(this.soundDataConverter(file, item + "." + "SOUND"));
            itemdata.setRotation(s.getInt("ROTATION"));
            itemdatas.add(itemdata);
         }
      }
   }

   private LinkedList<ItemData> loadItemDatas(PluginFile file) {
      LinkedList<ItemData> itemdatas = new LinkedList();
      Iterator var3 = file.getKeys(false).iterator();

      while(var3.hasNext()) {
         String subSectionName = (String)var3.next();
         if (!subSectionName.equals("menu-settings") && !subSectionName.equals("bar-items")) {
            ConfigurationSection s = file.getConfigurationSection(subSectionName);
            ItemData itemdata = null;
            if (s.isString("ID")) {
               itemdata = new ItemData(s.getString("NAME"), Material.valueOf(s.getString("ID").toUpperCase()));
            } else if (s.isSet("SKULL-TEXTURE")) {
               itemdata = new ItemData(s.getString("NAME"), s.getString("SKULL-TEXTURE"), false);
            } else if (s.isSet("SKULL-URL")) {
               itemdata = new ItemData(s.getString("NAME"), s.getString("SKULL-URL"), true);
            } else {
               System.out.println("ProdigyGUI ERROR: You need at least ID or SKULL TEXTURE to create any item");
            }

            itemdata.setCommand(s.getString("COMMAND"));
            itemdata.setLore(s.getString("LORE"));
            itemdata.setPrice(s.getInt("PRICE"));
            itemdata.setPermission(s.getString("PERMISSION"));
            itemdata.setNopermissionmessage(s.getString("PERMISSION-MESSAGE"));
            itemdata.setParticleData(this.particleDataConverter(file, subSectionName + "." + "PARTICLE"));
            itemdata.setSoundData(this.soundDataConverter(file, subSectionName + "." + "SOUND"));
            itemdata.setRotation(s.getInt("ROTATION"));
            itemdatas.add(itemdata);
         }
      }

      return itemdatas;
   }
}
