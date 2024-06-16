package fr.cocoraid.prodigygui.utils;

import org.bukkit.util.Vector;

public class UtilMath {
   public static boolean elapsed(long from, long required) {
      return System.currentTimeMillis() - from > required;
   }

   public static float getLookAtYaw(Vector motion) {
      double dx = motion.getX();
      double dz = motion.getZ();
      double yaw = 0.0D;
      if (dx != 0.0D) {
         if (dx < 0.0D) {
            yaw = 4.71238898038469D;
         } else {
            yaw = 1.5707963267948966D;
         }

         yaw -= Math.atan(dz / dx);
      } else if (dz < 0.0D) {
         yaw = 3.141592653589793D;
      }

      return (float)(-yaw * 180.0D / 3.141592653589793D - 90.0D) + 90.0F;
   }

   public static final Vector rotateAroundAxisX(Vector v, double angle) {
      double cos = Math.cos(angle);
      double sin = Math.sin(angle);
      double y = v.getY() * cos - v.getZ() * sin;
      double z = v.getY() * sin + v.getZ() * cos;
      return v.setY(y).setZ(z);
   }

   public static final Vector rotateAroundAxisY(Vector v, double angle) {
      double cos = Math.cos(angle);
      double sin = Math.sin(angle);
      double x = v.getX() * cos + v.getZ() * sin;
      double z = v.getX() * -sin + v.getZ() * cos;
      return v.setX(x).setZ(z);
   }

   public static final Vector rotateAroundAxisZ(Vector v, double angle) {
      double cos = Math.cos(angle);
      double sin = Math.sin(angle);
      double x = v.getX() * cos - v.getY() * sin;
      double y = v.getX() * sin + v.getY() * cos;
      return v.setX(x).setY(y);
   }

   public static final Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
      rotateAroundAxisX(v, angleX);
      rotateAroundAxisY(v, angleY);
      rotateAroundAxisZ(v, angleZ);
      return v;
   }

   public static byte toPackedByte(float f) {
      return (byte)((int)(f * 256.0F / 360.0F));
   }

   public static double randomRange(double min, double max) {
      return Math.random() < 0.5D ? (1.0D - Math.random()) * (max - min) + min : Math.random() * (max - min) + min;
   }
}
