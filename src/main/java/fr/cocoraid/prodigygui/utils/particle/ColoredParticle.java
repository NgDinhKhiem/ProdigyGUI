package fr.cocoraid.prodigygui.utils.particle;

import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ColoredParticleData;
import fr.cocoraid.prodigygui.utils.UtilMath;
import fr.cocoraid.prodigygui.utils.VersionChecker;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;

public class ColoredParticle extends ParticleBuilder {
   private Color color;
   private double radius = 0.0D;

   public ColoredParticle(Location location) {
      super(location);
      if (ColoredParticleData.getColorableParticles().contains(this.particle)) {
         System.out.println("Error: Particle " + this.particle.name() + " is not available for " + this.getClass().getSimpleName());
      }
   }

   public ColoredParticle setColor(Color color) {
      this.color = color;
      return this;
   }

   public ColoredParticle setRadius(double radius) {
      this.radius = radius;
      return this;
   }

   public ColoredParticle setColor(int r, int g, int b) {
      this.color = Color.fromBGR(b, g, r);
      return this;
   }

   public void sendParticle(Player player) {
      if (VersionChecker.isHigherOrEqualThan(VersionChecker.v1_13_R1)) {
         if (this.particle == Particle.REDSTONE) {
            DustOptions dustOptions = new DustOptions(this.color, 1.0F);

            for(int i = 0; i < this.amount; ++i) {
               Location loc = this.radius > 0.0D ? this.location.clone().add(UtilMath.randomRange(-this.radius, this.radius), UtilMath.randomRange(-this.radius, this.radius), UtilMath.randomRange(-this.radius, this.radius)) : this.location;
               player.spawnParticle(Particle.REDSTONE, loc, 0, dustOptions);
            }
         } else if (this.particle == Particle.SPELL_MOB_AMBIENT || this.particle == Particle.SPELL_MOB) {
            double red = (double)this.color.getRed() / 255.0D;
            double green = (double)this.color.getGreen() / 255.0D;
            double blue = (double)this.color.getBlue() / 255.0D;

            for(int i = 0; i < this.amount; ++i) {
               Location loc = this.radius > 0.0D ? this.location.clone().add(UtilMath.randomRange(-this.radius, this.radius), UtilMath.randomRange(-this.radius, this.radius), UtilMath.randomRange(-this.radius, this.radius)) : this.location;
               player.spawnParticle(Particle.SPELL_MOB, loc, 0, red, green, blue, 1.0D);
            }
         }
      }

   }
}
