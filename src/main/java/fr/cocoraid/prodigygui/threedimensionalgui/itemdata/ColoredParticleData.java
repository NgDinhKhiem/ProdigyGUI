package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Particle;

public class ColoredParticleData extends ParticleData {
   private static List<Particle> colorableParticles;
   private int r;
   private int g;
   private int b = 0;
   private double radius = 0.5D;

   public ColoredParticleData(Particle particle) {
      super(particle);
      Validate.isTrue(colorableParticles.contains(particle));
      this.amount = 0;
   }

   public ColoredParticleData setB(int b) {
      this.b = b;
      return this;
   }

   public ColoredParticleData setG(int g) {
      this.g = g;
      return this;
   }

   public ColoredParticleData setR(int r) {
      this.r = r;
      return this;
   }

   public void setRadius(double radius) {
      this.radius = radius;
   }

   public double getRadius() {
      return this.radius;
   }

   public int getB() {
      return this.b;
   }

   public int getG() {
      return this.g;
   }

   public int getR() {
      return this.r;
   }

   public static List<Particle> getColorableParticles() {
      return colorableParticles;
   }

   static {
      colorableParticles = new ArrayList(Arrays.asList(Particle.REDSTONE, Particle.SPELL_MOB, Particle.SPELL_MOB_AMBIENT));
   }
}
