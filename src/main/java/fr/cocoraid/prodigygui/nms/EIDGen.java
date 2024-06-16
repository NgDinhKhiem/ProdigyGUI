package fr.cocoraid.prodigygui.nms;

public class EIDGen {
   private static int lastIssuedEID = 2000000000;

   public static int generateEID() {
      return lastIssuedEID++;
   }
}
