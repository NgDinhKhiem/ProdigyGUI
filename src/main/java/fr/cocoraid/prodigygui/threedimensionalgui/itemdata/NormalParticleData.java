package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import org.bukkit.Particle;

public class NormalParticleData extends ParticleData {
   private double offsetX;
   private double offsetY;
   private double offsetZ = 0.0D;
   private double speed = 0.0D;

   public NormalParticleData(Particle particle) {
      super(particle);
   }

   public NormalParticleData setOffsetX(double offsetX) {
      this.offsetX = offsetX;
      return this;
   }

   public NormalParticleData setOffsetY(double offsetY) {
      this.offsetY = offsetY;
      return this;
   }

   public void setSpeed(double speed) {
      this.speed = speed;
   }

   public NormalParticleData setOffsetZ(double offsetZ) {
      this.offsetZ = offsetZ;
      return this;
   }

   public double getSpeed() {
      return this.speed;
   }

   public double getOffsetX() {
      return this.offsetX;
   }

   public double getOffsetY() {
      return this.offsetY;
   }

   public double getOffsetZ() {
      return this.offsetZ;
   }
}
