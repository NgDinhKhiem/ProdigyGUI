package fr.cocoraid.prodigygui.utils.textanim;

import java.util.ArrayList;

public class Glow {
   private ArrayList<String> frames = new ArrayList();
   private int frame = 0;

   public Glow(String text, String normaltextcolor, String startColor, String middleColor, String endColor, int size, int pause) {
      String normalFormat = normaltextcolor;
      String startFormat = startColor;
      String middleFormat = middleColor;
      String endFormat = endColor;
      int length = text.length();
      int iterations = length + size + 2 + 1;
      int startsub = 0;
      int endsub = 0;
      int counter = 0;

      int pos;
      for(pos = 0; pos < iterations; ++pos) {
         if (pos > 1 && pos < iterations - 2) {
            if (counter >= length - size) {
               --startsub;
               ++endsub;
            } else if (startsub + 1 > size) {
               startsub = size;
               ++counter;
            } else {
               ++startsub;
            }
         }

         String startPart = "";
         String middlePart = "";
         String endPart = "";
         String before = "";
         String last = "";
         if (pos >= iterations - length - 1 && pos < iterations - 1) {
            startPart = text.substring(pos - (iterations - length - 1), pos - (iterations - length) + 2);
         }

         if (pos > 1 && pos < iterations - 2) {
            middlePart = text.substring(pos - 1 - startsub - endsub, pos - 1 - endsub);
         }

         if (pos > 0 && pos <= length) {
            endPart = text.substring(pos - 1, pos);
         }

         if (pos < length) {
            last = text.substring(pos);
         }

         if (pos >= iterations - length) {
            before = text.substring(0, pos + 1 - (iterations - length));
         }

         before = normalFormat + before;
         startPart = startFormat + startPart;
         middlePart = middleFormat + middlePart;
         endPart = endFormat + endPart;
         last = normalFormat + last;
         String frame = before + startPart + middlePart + endPart + last;
         this.frames.add(frame);
      }

      for(pos = 0; pos < pause; ++pos) {
         this.frames.add(normalFormat + text);
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
}
