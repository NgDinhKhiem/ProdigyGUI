package fr.cocoraid.prodigygui.utils.textanim;

import java.util.ArrayList;

public class Pulse {
   private ArrayList<String> frames;
   private int frame;

   public Pulse(String text, Pulse.PulseColor color, int pause) {
      int i;
      label200: {
         label201: {
            label202: {
               label203: {
                  label204: {
                     super();
                     this.frames = new ArrayList();
                     this.frame = 0;
                     switch(color) {
                     case MULTI:
                     case WHITE:
                        this.frames.add("§0" + text);
                        this.frames.add("§8" + text);
                        this.frames.add("§7" + text);
                        this.frames.add("§f" + text);

                        for(i = 0; i < pause; ++i) {
                           this.frames.add("§f" + text);
                        }

                        this.frames.add("§f" + text);
                        this.frames.add("§7" + text);
                        this.frames.add("§8" + text);
                        this.frames.add("§0" + text);

                        for(i = 0; i < pause; ++i) {
                           this.frames.add("§0" + text);
                        }

                        if (color != Pulse.PulseColor.MULTI) {
                           return;
                        }
                     case BLACK:
                        this.frames.add("§f" + text);
                        this.frames.add("§7" + text);
                        this.frames.add("§8" + text);
                        this.frames.add("§0" + text);

                        for(i = 0; i < pause; ++i) {
                           this.frames.add("§0" + text);
                        }

                        this.frames.add("§0" + text);
                        this.frames.add("§7" + text);
                        this.frames.add("§8" + text);
                        this.frames.add("§0" + text);

                        for(i = 0; i < pause; ++i) {
                           this.frames.add("§0" + text);
                        }

                        if (color != Pulse.PulseColor.MULTI) {
                           return;
                        }
                     case RED:
                        break;
                     case YELLOW:
                        break label204;
                     case BLUE:
                        break label203;
                     case PINK:
                        break label202;
                     case GREEN:
                        break label201;
                     case CYAN:
                        break label200;
                     default:
                        return;
                     }

                     this.frames.add("§0" + text);
                     this.frames.add("§8" + text);
                     this.frames.add("§4" + text);
                     this.frames.add("§c" + text);

                     for(i = 0; i < pause; ++i) {
                        this.frames.add("§c" + text);
                     }

                     this.frames.add("§c" + text);
                     this.frames.add("§4" + text);
                     this.frames.add("§8" + text);
                     this.frames.add("§0" + text);

                     for(i = 0; i < pause; ++i) {
                        this.frames.add("§0" + text);
                     }

                     if (color != Pulse.PulseColor.MULTI) {
                        return;
                     }
                  }

                  this.frames.add("§0" + text);
                  this.frames.add("§8" + text);
                  this.frames.add("§6" + text);
                  this.frames.add("§e" + text);

                  for(i = 0; i < pause; ++i) {
                     this.frames.add("§e" + text);
                  }

                  this.frames.add("§e" + text);
                  this.frames.add("§6" + text);
                  this.frames.add("§8" + text);
                  this.frames.add("§0" + text);

                  for(i = 0; i < pause; ++i) {
                     this.frames.add("§0" + text);
                  }

                  if (color != Pulse.PulseColor.MULTI) {
                     return;
                  }
               }

               this.frames.add("§0" + text);
               this.frames.add("§8" + text);
               this.frames.add("§1" + text);
               this.frames.add("§9" + text);

               for(i = 0; i < pause; ++i) {
                  this.frames.add("§9" + text);
               }

               this.frames.add("§9" + text);
               this.frames.add("§1" + text);
               this.frames.add("§8" + text);
               this.frames.add("§0" + text);

               for(i = 0; i < pause; ++i) {
                  this.frames.add("§0" + text);
               }

               if (color != Pulse.PulseColor.MULTI) {
                  return;
               }
            }

            this.frames.add("§0" + text);
            this.frames.add("§8" + text);
            this.frames.add("§5" + text);
            this.frames.add("§d" + text);

            for(i = 0; i < pause; ++i) {
               this.frames.add("§d" + text);
            }

            this.frames.add("§5" + text);
            this.frames.add("§d" + text);
            this.frames.add("§8" + text);
            this.frames.add("§0" + text);

            for(i = 0; i < pause; ++i) {
               this.frames.add("§0" + text);
            }

            if (color != Pulse.PulseColor.MULTI) {
               return;
            }
         }

         this.frames.add("§0" + text);
         this.frames.add("§8" + text);
         this.frames.add("§2" + text);
         this.frames.add("§a" + text);

         for(i = 0; i < pause; ++i) {
            this.frames.add("§a" + text);
         }

         this.frames.add("§a" + text);
         this.frames.add("§2" + text);
         this.frames.add("§8" + text);
         this.frames.add("§0" + text);

         for(i = 0; i < pause; ++i) {
            this.frames.add("§0" + text);
         }

         if (color != Pulse.PulseColor.MULTI) {
            return;
         }
      }

      this.frames.add("§0" + text);
      this.frames.add("§8" + text);
      this.frames.add("§3" + text);
      this.frames.add("§b" + text);

      for(i = 0; i < pause; ++i) {
         this.frames.add("§b" + text);
      }

      this.frames.add("§b" + text);
      this.frames.add("§3" + text);
      this.frames.add("§8" + text);
      this.frames.add("§0" + text);

      for(i = 0; i < pause; ++i) {
         this.frames.add("§0" + text);
      }

   }

   public String next() {
      if (this.frame >= this.frames.size()) {
         this.frame = 0;
      }

      String s = (String)this.frames.get(this.frame);
      ++this.frame;
      return s;
   }

   public ArrayList<String> getFrames() {
      return this.frames;
   }

   public static enum PulseColor {
      MULTI,
      WHITE,
      BLACK,
      RED,
      YELLOW,
      BLUE,
      PINK,
      GREEN,
      CYAN;

      // $FF: synthetic method
      private static Pulse.PulseColor[] $values() {
         return new Pulse.PulseColor[]{MULTI, WHITE, BLACK, RED, YELLOW, BLUE, PINK, GREEN, CYAN};
      }
   }
}
