package fr.cocoraid.prodigygui.bridge;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyBridge {
   private static Economy economy;

   public static boolean setupEconomy() {
      if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
         return false;
      } else {
         RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
         if (rsp == null) {
            return false;
         } else {
            economy = (Economy)rsp.getProvider();
            return economy != null;
         }
      }
   }

   public static boolean hasValidEconomy() {
      return economy != null;
   }

   public static Economy getEconomy() {
      if (!hasValidEconomy()) {
         throw new IllegalStateException("Economy plugin was not found!");
      } else {
         return economy;
      }
   }

   public static double getMoney(Player player) {
      if (!hasValidEconomy()) {
         throw new IllegalStateException("Economy plugin was not found!");
      } else {
         return economy.getBalance(player.getName(), player.getWorld().getName());
      }
   }

   public static boolean hasMoney(Player player, double minimum) {
      if (!hasValidEconomy()) {
         throw new IllegalStateException("Economy plugin was not found!");
      } else if (minimum < 0.0D) {
         throw new IllegalArgumentException("Invalid amount of money: " + minimum);
      } else {
         double balance = economy.getBalance(player.getName(), player.getWorld().getName());
         return !(balance < minimum);
      }
   }

   public static boolean takeMoney(Player player, double amount) {
      if (!hasValidEconomy()) {
         throw new IllegalStateException("Economy plugin was not found!");
      } else if (amount < 0.0D) {
         throw new IllegalArgumentException("Invalid amount of money: " + amount);
      } else {
         EconomyResponse response = economy.withdrawPlayer(player.getName(), player.getWorld().getName(), amount);
         boolean result = response.transactionSuccess();
         return result;
      }
   }

   public static boolean giveMoney(Player player, double amount) {
      if (!hasValidEconomy()) {
         throw new IllegalStateException("Economy plugin was not found!");
      } else if (amount < 0.0D) {
         throw new IllegalArgumentException("Invalid amount of money: " + amount);
      } else {
         EconomyResponse response = economy.depositPlayer(player.getName(), player.getWorld().getName(), amount);
         boolean result = response.transactionSuccess();
         return result;
      }
   }

   public static String formatMoney(double amount) {
      return hasValidEconomy() ? economy.format(amount) : Double.toString(amount);
   }
}
