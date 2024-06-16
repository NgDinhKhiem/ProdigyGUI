package fr.cocoraid.prodigygui.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class PluginFile extends YamlConfiguration {
   private File file;
   private Plugin plugin;

   public PluginFile(Plugin plugin, File file) {
      this.file = file;
      this.plugin = plugin;
   }

   public PluginFile(Plugin plugin, String name) {
      this(plugin, new File(plugin.getDataFolder(), name));
   }

   public void load() throws IOException, InvalidConfigurationException {
      if (!this.file.isFile()) {
         if (this.plugin.getResource(this.file.getName()) != null) {
            this.plugin.saveResource(this.file.getName(), false);
         } else {
            if (this.file.getParentFile() != null) {
               this.file.getParentFile().mkdirs();
            }

            this.file.createNewFile();
         }
      }

      Iterator var1 = this.getKeys(false).iterator();

      while(var1.hasNext()) {
         String section = (String)var1.next();
         this.set(section, (Object)null);
      }

      this.load(this.file);
   }

   public void save() throws IOException {
      this.save(this.file);
   }

   public File getFile() {
      return this.file;
   }

   public Plugin getPlugin() {
      return this.plugin;
   }

   public String getFileName() {
      return this.file.getName();
   }
}
