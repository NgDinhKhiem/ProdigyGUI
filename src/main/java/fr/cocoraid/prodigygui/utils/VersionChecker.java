package fr.cocoraid.prodigygui.utils;

import org.bukkit.Bukkit;

public enum VersionChecker {
   v1_9_R1(3),
   v1_9_R2(4),
   v1_10_R1(5),
   v1_11_R1(6),
   v1_12_R1(7),
   v1_13_R1(8),
   v1_13_R2(9),
   v1_14_R1(10),
   v1_15_R1(11),
   v1_16_R1(12),
   v1_17_R1(13),
   v1_18_R1(14),
   v1_18_R2(15),
   v1_19_R1(16);

   private static VersionChecker currentVersion = valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]);
   private int index;

   private VersionChecker(int index) {
      this.index = index;
   }

   public static boolean isHigherOrEqualThan(VersionChecker v) {
      return currentVersion.getIndex() >= v.getIndex();
   }

   public static boolean isLowerOrEqualThan(VersionChecker v) {
      return currentVersion.getIndex() <= v.getIndex();
   }

   public int getIndex() {
      return this.index;
   }

   public static VersionChecker getCurrentVersion() {
      return currentVersion;
   }

   // $FF: synthetic method
   private static VersionChecker[] $values() {
      return new VersionChecker[]{v1_9_R1, v1_9_R2, v1_10_R1, v1_11_R1, v1_12_R1, v1_13_R1, v1_13_R2, v1_14_R1, v1_15_R1, v1_16_R1, v1_17_R1, v1_18_R1, v1_18_R2, v1_19_R1};
   }
}
