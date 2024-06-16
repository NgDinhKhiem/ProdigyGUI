package fr.cocoraid.prodigygui;

import fr.cocoraid.prodigygui.bridge.EconomyBridge;
import fr.cocoraid.prodigygui.bridge.PlaceholderAPIBridge;
import fr.cocoraid.prodigygui.config.CoreConfig;
import fr.cocoraid.prodigygui.event.BreakBlockEvent;
import fr.cocoraid.prodigygui.event.ItemInteractEvent;
import fr.cocoraid.prodigygui.event.JoinQuitEvent;
import fr.cocoraid.prodigygui.language.Language;
import fr.cocoraid.prodigygui.language.LanguageLoader;
import fr.cocoraid.prodigygui.loader.CommandListener;
import fr.cocoraid.prodigygui.loader.FileLoader;
import fr.cocoraid.prodigygui.protocol.InteractableItemProtocol;
import fr.cocoraid.prodigygui.task.ThreeDimensionalGUITask;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import fr.cocoraid.prodigygui.utils.CC;
import fr.cocoraid.prodigygui.utils.Metrics;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ProdigyGUI extends JavaPlugin {
   private Language language;
   private CoreConfig config;
   private static ProdigyGUI instance;
   private Metrics metrics;

   public void onEnable() {
      instance = this;
      ConsoleCommandSender c = Bukkit.getServer().getConsoleSender();
      this.metrics = new Metrics(this);
      this.loadConfiguration();
      if (!EconomyBridge.setupEconomy()) {
         this.getLogger().warning("Vault with a compatible economy plugin was not found! Icons with a PRICE or commands that give money will not work.");
      }

      PlaceholderAPIBridge placeholderAPIBridge = new PlaceholderAPIBridge();
      placeholderAPIBridge.setupPlugin();
      if (PlaceholderAPIBridge.hasValidPlugin()) {
         this.getLogger().info("Hooked PlaceholderAPI");
      }

      new LanguageLoader(this);
      if (!LanguageLoader.getLanguages().containsKey(this.config.language.toLowerCase())) {
         c.sendMessage("§c Language not found ! Please check your language folder");
      } else {
         this.language = LanguageLoader.getLanguage(this.config.language.toLowerCase());
      }

      c.sendMessage(CC.d_green + "Language: " + (this.language == null ? "english" : this.config.language.toLowerCase()));
      if (this.language == null) {
         this.language = LanguageLoader.getLanguage("english");
      }

      new FileLoader(this);
      new InteractableItemProtocol(this);
      new ThreeDimensionalGUITask(this);
      Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
      Bukkit.getPluginManager().registerEvents(new JoinQuitEvent(), this);
      Bukkit.getPluginManager().registerEvents(new BreakBlockEvent(), this);
      Bukkit.getPluginManager().registerEvents(new ItemInteractEvent(), this);
   }

   public void onDisable() {
      ProdigyGUIPlayer.getProdigyPlayers().values().stream().filter((pp) -> {
         return pp.getThreeDimensionGUI() != null && pp.getThreeDimensionGUI().isSpawned();
      }).forEach((pp) -> {
         pp.getThreeDimensionGUI().closeGui();
      });

      try {
         this.config.save();
      } catch (InvalidConfigurationException var2) {
         var2.printStackTrace();
         this.getLogger().log(Level.SEVERE, "Oooops ! Something went wrong while saving the configuration !");
      }

   }

   private void loadConfiguration() {
      Logger logger = this.getLogger();

      try {
         this.config = new CoreConfig(new File(this.getDataFolder(), "configuration.yml"));
         this.config.load();
      } catch (InvalidConfigurationException var3) {
         var3.printStackTrace();
         logger.log(Level.SEVERE, "Oooops ! Something went wrong while loading the configuration !");
         Bukkit.getPluginManager().disablePlugin(this);
      }

   }

   private ThreeDimensionalMenu checkConditions(CommandSender sender, String[] args) {
      if (sender instanceof Player) {
         Player p = (Player)sender;
         if (!p.hasPermission("prodigygui.other.open")) {
            p.sendMessage(this.language.no_permission);
            return null;
         }
      }

      if (Bukkit.getPlayer(args[2]) != null && (Bukkit.getPlayer(args[2]) == null || Bukkit.getPlayer(args[2]).isOnline())) {
         ThreeDimensionalMenu menu = (ThreeDimensionalMenu)ThreeDimensionalMenu.getMenus().stream().filter((m) -> {
            return m.getFileName().replace(".yml", "").equalsIgnoreCase(args[1]);
         }).findAny().orElseGet(() -> {
            return null;
         });
         if (menu == null) {
            sender.sendMessage("§cMenu " + args[1] + " could not be found !");
            return null;
         } else {
            return menu;
         }
      } else {
         sender.sendMessage("Player " + args[2] + " is not online");
         return null;
      }
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (command.getName().equalsIgnoreCase("prodigygui")) {
         if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player && !((Player)sender).hasPermission("prodigygui.reload")) {
               sender.sendMessage("§cYou do not have the permission !");
               return false;
            }

            ProdigyGUIPlayer.getProdigyPlayers().values().stream().filter((pp) -> {
               return pp.getThreeDimensionGUI() != null && pp.getThreeDimensionGUI().isSpawned();
            }).forEach((pp) -> {
               pp.getThreeDimensionGUI().closeGui();
            });

            try {
               this.config.save();
            } catch (InvalidConfigurationException var10) {
               var10.printStackTrace();
               this.getLogger().log(Level.SEVERE, "Oooops ! Something went wrong while saving the configuration !");
            }

            this.loadConfiguration();
            ConsoleCommandSender c = Bukkit.getServer().getConsoleSender();
            if (!LanguageLoader.getLanguages().containsKey(this.config.language.toLowerCase())) {
               c.sendMessage("§c Language not found ! Please check your language folder");
            } else {
               this.language = LanguageLoader.getLanguage(this.config.language.toLowerCase());
            }

            c.sendMessage(CC.d_green + "Language: " + (this.language == null ? "english" : this.config.language.toLowerCase()));
            if (this.language == null) {
               this.language = LanguageLoader.getLanguage("english");
            }

            ThreeDimensionalMenu.getMenus().clear();
            new FileLoader(this);
            if (sender instanceof Player) {
               ((Player)sender).sendMessage("§bConfiguration reloaded !");
            } else {
               c.sendMessage("§bConfiguration reloaded !");
            }
         }

         ThreeDimensionalMenu menu;
         if (args.length == 3) {
            menu = this.checkConditions(sender, args);
            if (menu == null) {
               return false;
            }

            (new ThreeDimensionGUI(Bukkit.getPlayer(args[2]), menu)).openGui();
         } else if (args.length == 7) {
            if (args[0].equalsIgnoreCase("open")) {
               menu = this.checkConditions(sender, args);
               if (menu == null) {
                  return false;
               }

               try {
                  Double.valueOf(args[2]);
               } catch (Exception var9) {
                  sender.sendMessage("§cThe yaw rotation " + args[3] + " must be integer !");
                  sender.sendMessage("§c/prodigygui open <menu> <playername> <yawRotation> <x> <y> <z> ");
                  return false;
               }

               try {
                  Double.valueOf(args[4]);
                  Double.valueOf(args[5]);
                  Double.valueOf(args[6]);
               } catch (Exception var8) {
                  sender.sendMessage("§cThe y,y,z positions " + args[1] + " must be integer !");
                  sender.sendMessage("§c/prodigygui open <menu> <playername> <yawRotation> <x> <y> <z>");
                  return false;
               }

               (new ThreeDimensionGUI(Bukkit.getPlayer(args[2]), menu)).setRotation(Float.valueOf(args[3])).setCenter(Double.valueOf(args[4]), Double.valueOf(args[5]), Double.valueOf(args[6])).openGui();
            }
         } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("open")) {
               menu = this.checkConditions(sender, args);
               if (menu == null) {
                  return false;
               }

               try {
                  Double.valueOf(args[3]);
               } catch (Exception var7) {
                  sender.sendMessage("§cThe yaw rotation " + args[3] + " must be integer !");
                  sender.sendMessage("§c/prodigygui open <menu> <playername> <yawRotation>");
                  return false;
               }

               Player p = Bukkit.getPlayer(args[2]);
               (new ThreeDimensionGUI(p, menu)).setRotation(Float.valueOf(args[3])).openGui();
            }
         } else {
            sender.sendMessage("§c/prodigygui open <menu> <playername> <yawRotation> <x> <y> <z>");
            sender.sendMessage("§c/prodigygui open <menu> <playername> <yawRotation>");
            sender.sendMessage("§c/prodigygui open <menu> <playername>");
         }
      }

      return false;
   }

   public CoreConfig getConfiguration() {
      return this.config;
   }

   public Language getLanguage() {
      return this.language;
   }

   public static ProdigyGUI getInstance() {
      return instance;
   }
}
