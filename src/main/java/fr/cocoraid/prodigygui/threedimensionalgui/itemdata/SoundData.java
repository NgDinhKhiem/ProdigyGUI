package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import org.bukkit.Sound;

public class SoundData {
   private Sound sound;
   private float volume = 1.0F;
   private float pitch = 1.0F;

   public SoundData(Sound sound) {
      this.sound = sound;
   }

   public SoundData setVolume(float volume) {
      this.volume = volume;
      return this;
   }

   public SoundData setPitch(float pitch) {
      this.pitch = pitch;
      return this;
   }

   public float getPitch() {
      return this.pitch;
   }

   public float getVolume() {
      return this.volume;
   }

   public Sound getSound() {
      return this.sound;
   }
}
