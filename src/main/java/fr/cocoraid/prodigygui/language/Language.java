package fr.cocoraid.prodigygui.language;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class Language {
   private transient File file;
   private transient FileConfiguration langFile;
   public String no_permission = "§cYou do not have the permission to do that";
   public String no_money = "§cYou do not have enough money, §cyou need {money}$";
   public String button_next_name = "§bnext";
   public String button_previous_name = "§bprevious";

   public Language(File file, FileConfiguration fc) {
      this.file = file;
      this.langFile = fc;
   }

   public File getFile() {
      return this.file;
   }

   public FileConfiguration getLangFile() {
      return this.langFile;
   }
}
