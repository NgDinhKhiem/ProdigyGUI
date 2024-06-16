package fr.cocoraid.prodigygui.utils.particle;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class NormalParticle extends ParticleBuilder {
   private Vector offset = new Vector(0, 0, 0);
   private double speed = 0.0D;

   public NormalParticle(Location location) {
      super(location);
   }

   public NormalParticle setOffset(Vector offset) {
      this.offset = offset;
      return this;
   }

   public NormalParticle setSpeed(double speed) {
      this.speed = speed;
      return this;
   }

   public double getSpeed() {
      return this.speed;
   }

   public void sendParticle(Player player) {
      player.spawnParticle(this.particle, this.location, this.amount, this.offset.getX(), this.offset.getY(), this.offset.getZ(), this.speed);
   }
}
