package fr.cocoraid.prodigygui.language;

import fr.cocoraid.prodigygui.ProdigyGUI;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageLoader {
   private static Map<String, Language> languages = new HashMap();

   public LanguageLoader(ProdigyGUI instance) {
      if (!instance.getDataFolder().exists()) {
         instance.getDataFolder().mkdir();
      }

      File databaseFolder = new File(instance.getDataFolder(), "language");
      if (!databaseFolder.exists()) {
         databaseFolder.mkdirs();
      }

      File file = this.getLanguageFile("english");
      int var7;
      if (!file.exists()) {
         try {
            FileConfiguration c = YamlConfiguration.loadConfiguration(file);
            file.createNewFile();
            Language language = new Language(file, c);
            Field[] var6 = language.getClass().getDeclaredFields();
            var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Field field = var6[var8];
               if (!Modifier.isTransient(field.getModifiers())) {
                  if (field.getType().equals(String.class)) {
                     String s = (String)field.get(language);
                     c.set(field.getName(), s.replace("§", "&"));
                  } else if (field.getType().equals(List.class)) {
                     List<String> list = new ArrayList();
                     ((List)field.get(language)).forEach((sx) -> {
                        list.add(sx.replace("§", "&"));
                     });
                     c.set(field.getName(), list);
                  }
               }
            }

            c.save(file);
         } catch (IllegalAccessException | IOException var19) {
            var19.printStackTrace();
         }
      }

      File[] files = (new File(instance.getDataFolder(), "language")).listFiles();
      File[] var21 = files;
      int var22 = files.length;

      for(var7 = 0; var7 < var22; ++var7) {
         File f = var21[var7];
         if (f.isFile() && f.getName().endsWith(".yml")) {
            try {
               FileConfiguration c = YamlConfiguration.loadConfiguration(f);
               Language lang = new Language(f, c);
               List<String> cache = new ArrayList();
               Field[] var12 = lang.getClass().getDeclaredFields();
               int var13 = var12.length;

               int var14;
               for(var14 = 0; var14 < var13; ++var14) {
                  Field field = var12[var14];
                  cache.add(field.getName());
               }

               List<String> yml = new ArrayList();
               c.getConfigurationSection("").getKeys(false).forEach((sx) -> {
                  yml.add(sx);
               });
               yml.removeAll(cache);
               c.getConfigurationSection("").getKeys(false).forEach((sx) -> {
                  yml.add(sx);
               });
               cache.removeAll(yml);
               Field[] var28 = lang.getClass().getDeclaredFields();
               var14 = var28.length;

               for(int var29 = 0; var29 < var14; ++var29) {
                  Field field = var28[var29];
                  if (!Modifier.isTransient(field.getModifiers())) {
                     ArrayList list;
                     if (!cache.contains(field.getName())) {
                        if (field.getType().equals(String.class)) {
                           String s = c.getString(field.getName());
                           field.set(lang, s.replace("&", "§"));
                        } else if (field.getType().equals(List.class)) {
                           list = new ArrayList();
                           c.getStringList(field.getName()).forEach((sx) -> {
                              list.add(sx.replace("&", "§"));
                           });
                           field.set(lang, list);
                        }
                     } else if (field.getType().equals(String.class)) {
                        c.set(field.getName(), ((String)field.get(lang)).replace("§", "&"));
                     } else if (field.getType().equals(List.class)) {
                        list = new ArrayList();
                        c.getStringList(field.getName()).forEach((sx) -> {
                           list.add(sx.replace("&", "§"));
                        });
                        c.set(field.getName(), list);
                     }
                  }
               }

               languages.put(f.getName().replace(".yml", ""), lang);
               c.save(f);
            } catch (IllegalAccessException | IOException var18) {
               var18.printStackTrace();
            }
         }
      }

   }

   public static Language getLanguage(String lang) {
      return (Language)getLanguages().get(lang);
   }

   public static Map<String, Language> getLanguages() {
      return languages;
   }

   private File getLanguageFile(String language) {
      return new File(ProdigyGUI.getInstance().getDataFolder() + "/language", language + ".yml");
   }
}
