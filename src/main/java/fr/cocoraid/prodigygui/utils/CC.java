package fr.cocoraid.prodigygui.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public class CC {
   public static final Map<String, Color> COLOURS = new HashMap<String, Color>() {
      {
         this.put("RED", Color.RED);
         this.put("GREEN", Color.GREEN);
         this.put("BLUE", Color.BLUE);
         this.put("WHITE", Color.AQUA);
         this.put("BLACK", Color.BLACK);
         this.put("FUSCHSIA", Color.FUCHSIA);
         this.put("LIME", Color.LIME);
         this.put("GRAY", Color.GRAY);
         this.put("MAROON", Color.MAROON);
         this.put("OLIVE", Color.OLIVE);
         this.put("NAVY", Color.NAVY);
         this.put("ORANGE", Color.ORANGE);
         this.put("PURPLE", Color.PURPLE);
         this.put("SILVER", Color.SILVER);
         this.put("TEAL", Color.TEAL);
         this.put("WHITE", Color.WHITE);
         this.put("YELLOW", Color.YELLOW);
      }
   };
   public static String aqua;
   public static String black;
   public static String blue;
   public static String d_aqua;
   public static String d_blue;
   public static String d_gray;
   public static String d_green;
   public static String d_purple;
   public static String d_red;
   public static String gold;
   public static String gray;
   public static String green;
   public static String l_purple;
   public static String red;
   public static String white;
   public static String yellow;
   public static String bold;
   public static String italic;
   public static String magic;
   public static String reset;
   public static String underline;
   public static String strike;
   public static String arrow;
   public static String headkey;

   public static String rainbowlize(String string) {
      int lastColor = 0;
      String newString = "";
      String colors = "123456789abcde";

      for(int i = 0; i < string.length(); ++i) {
         int currColor;
         do {
            currColor = (new Random()).nextInt(colors.length() - 1) + 1;
         } while(currColor == lastColor);

         newString = newString + ChatColor.RESET.toString() + ChatColor.getByChar(colors.charAt(currColor)) + "" + string.charAt(i);
      }

      return newString;
   }

   public static String colored(String s) {
      return s == null ? null : s.replace("&", "§");
   }

   static {
      aqua = ChatColor.AQUA + "";
      black = ChatColor.BLACK + "";
      blue = ChatColor.BLUE + "";
      d_aqua = ChatColor.DARK_AQUA + "";
      d_blue = ChatColor.DARK_BLUE + "";
      d_gray = ChatColor.DARK_GRAY + "";
      d_green = ChatColor.DARK_GREEN + "";
      d_purple = ChatColor.DARK_PURPLE + "";
      d_red = ChatColor.DARK_RED + "";
      gold = ChatColor.GOLD + "";
      gray = ChatColor.GRAY + "";
      green = ChatColor.GREEN + "";
      l_purple = ChatColor.LIGHT_PURPLE + "";
      red = ChatColor.RED + "";
      white = ChatColor.WHITE + "";
      yellow = ChatColor.YELLOW + "";
      bold = ChatColor.BOLD + "";
      italic = ChatColor.ITALIC + "";
      magic = ChatColor.MAGIC + "";
      reset = ChatColor.RESET + "";
      underline = ChatColor.UNDERLINE + "";
      strike = ChatColor.STRIKETHROUGH + "";
      arrow = "➽";
      headkey = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";
   }
}
