package fr.cocoraid.prodigygui.threedimensionalgui.item;

import org.bukkit.entity.Player;

public class InteractableItem {
   private InteractableItem.Interactable interactable;
   private Item3D item;
   private Player player;

   public InteractableItem(Item3D item, InteractableItem.Interactable interactable) {
      this.item = item;
      this.player = item.getPlayer();
      this.interactable = interactable;
   }

   public Item3D getItem() {
      return this.item;
   }

   public void setItem(Item3D item) {
      this.item = item;
   }

   public InteractableItem.Interactable getInteractable() {
      return this.interactable;
   }

   public interface Interactable {
   }

   public interface InteractClickable extends InteractableItem.Interactable {
      InteractableItem.InteractClickable EMPTY = (SelectorInteractable) -> {
      };

      void interact(InteractableItem var1);
   }
}
