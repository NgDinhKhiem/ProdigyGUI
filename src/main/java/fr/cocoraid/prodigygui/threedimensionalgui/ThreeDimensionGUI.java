package fr.cocoraid.prodigygui.threedimensionalgui;

import fr.cocoraid.prodigygui.ProdigyGUI;
import fr.cocoraid.prodigygui.ProdigyGUIPlayer;
import fr.cocoraid.prodigygui.bridge.EconomyBridge;
import fr.cocoraid.prodigygui.bridge.PlaceholderAPIBridge;
import fr.cocoraid.prodigygui.config.CoreConfig;
import fr.cocoraid.prodigygui.language.Language;
import fr.cocoraid.prodigygui.loader.CommandDeserializer;
import fr.cocoraid.prodigygui.nms.wrapper.living.WrapperEntityArmorStand;
import fr.cocoraid.prodigygui.threedimensionalgui.item.InteractableItem;
import fr.cocoraid.prodigygui.threedimensionalgui.item.Item3D;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ItemData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ThreeDimensionGUI {
   private static CoreConfig config = ProdigyGUI.getInstance().getConfiguration();
   private static Language lang = ProdigyGUI.getInstance().getLanguage();
   protected Player player;
   private static final int maxItemNumber = 5;
   private LinkedList<InteractableItem> items = new LinkedList();
   private List<InteractableItem> allItems = new ArrayList();
   private Item3D lastSelected;
   private int selected = 0;
   private Location center;
   private ProdigyGUIPlayer pp;
   private WrapperEntityArmorStand displayName;
   protected Location[] positions = new Location[5];
   protected Location[] bar = new Location[2];
   private Item3D nextButton;
   private Item3D previousButton;
   private boolean isSpawned;
   private int currentPage = 0;
   private Location previous;
   private Location next;
   private double radius;
   private int angle_step;
   private ThreeDimensionalMenu menu;

   public ThreeDimensionGUI(Player player, ThreeDimensionalMenu menu) {
      this.player = player;
      this.center = player.getLocation();
      this.menu = menu;
      this.pp = ProdigyGUIPlayer.instanceOf(player);
      this.radius = menu.getRadius();
      this.angle_step = menu.getAngleStep();
   }

   public ThreeDimensionGUI setRotation(float yaw) {
      this.center.setYaw(yaw);
      return this;
   }

   public ThreeDimensionGUI setCenter(double x, double y, double z) {
      this.center.setX(x);
      this.center.setY(y);
      this.center.setZ(z);
      return this;
   }

   public void inizializeGUI() {
      this.menu.getItemDataList().forEach((i) -> {
         final int moneyPrice = i.getPrice();
         Item3D i3D = new Item3D(this.player, i);
         this.items.add(new InteractableItem(i3D, new InteractableItem.InteractClickable() {
            public void interact(InteractableItem listener) {
               if (i.getPermission() != null && !ThreeDimensionGUI.this.player.hasPermission(i.getPermission())) {
                  ThreeDimensionGUI.this.player.sendMessage(i.getNopermissionmessage() != null ? i.getNopermissionmessage() : ThreeDimensionGUI.lang.no_permission);
               } else {
                  if (moneyPrice > 0) {
                     if (!EconomyBridge.hasValidEconomy()) {
                        ThreeDimensionGUI.this.player.sendMessage(ChatColor.RED + "This command has a price, but Vault with a compatible economy plugin was not found. For security, the command has been blocked. Please inform the staff.");
                        return;
                     }

                     if (!EconomyBridge.hasMoney(ThreeDimensionGUI.this.player, (double)moneyPrice)) {
                        ThreeDimensionGUI.this.player.sendMessage((new String(ThreeDimensionGUI.lang.no_money)).replace("{money}", EconomyBridge.formatMoney((double)moneyPrice)));
                        return;
                     }

                     if (!EconomyBridge.takeMoney(ThreeDimensionGUI.this.player, (double)moneyPrice)) {
                        return;
                     }
                  }

                  if (i.getCommands() != null) {
                     (new CommandDeserializer(ThreeDimensionGUI.this.player, i.getCommands())).execute();
                  }

               }
            }
         }));
      });
      this.setupItems();
      this.allItems.addAll(this.items);
   }

   public void openGui() {
      if (this.pp.getThreeDimensionGUI() != null) {
         this.center.setYaw(this.pp.getPreviousYaw());
         if (!this.pp.getThreeDimensionGUI().isSpawned) {
            return;
         }

         if (this.pp.getThreeDimensionGUI().menu.getFileName().equals(this.menu.getFileName())) {
            this.pp.getThreeDimensionGUI().closeGui();
            return;
         }

         this.pp.getThreeDimensionGUI().closeGui();
      } else {
         this.pp.setPreviousYaw(this.center.getYaw());
      }

      double buttonsHeight = 0.5D;
      Location l = this.center.clone();
      l.setPitch(0.0F);
      l.setYaw(this.center.getYaw() - (float)(this.angle_step + 17));
      this.previous = l.clone().add(0.0D, buttonsHeight, 0.0D).toVector().add(l.getDirection().multiply(this.radius)).toLocation(this.center.getWorld());
      this.previous.setDirection(this.player.getLocation().toVector().subtract(this.previous.toVector()));
      this.bar[0] = this.previous.clone().add(0.0D, 2.3D, 0.0D);
      l.setYaw(this.center.getYaw() + (float)(this.angle_step + 17));
      this.next = l.clone().add(0.0D, buttonsHeight, 0.0D).toVector().add(l.getDirection().multiply(this.radius)).toLocation(this.center.getWorld());
      this.next.setDirection(this.player.getLocation().toVector().subtract(this.next.toVector()));
      this.bar[1] = this.next.clone().add(0.0D, 2.3D, 0.0D);
      l.setYaw(this.center.getYaw());
      this.displayName = new WrapperEntityArmorStand(l.toVector().add(l.getDirection().multiply(this.radius + 0.2D)).toLocation(this.center.getWorld()).add(0.0D, 2.0D, 0.0D), this.player);
      this.displayName.setCustomNameVisible(true);
      this.displayName.setCustomName(PlaceholderAPIBridge.setPlaceholders(this.menu.getTitle(), this.player));
      this.displayName.setInvisible(true);
      this.displayName.setSmall(true);
      this.displayName.setMarker(true);
      this.pp.setThreeDimensionGUI(this);
      this.inizializeGUI();
      this.currentPage = 0;
      if (Math.ceil((double)((float)this.items.size() / 5.0F)) >= 2.0D) {
         this.addNextPageButton();
      }

      if (this.menu.getPreviousMenu() != null) {
         this.addPreviousPageButton();
      }

      int i = 0;

      for(Iterator var5 = this.menu.getBarItemsList().iterator(); var5.hasNext(); ++i) {
         ItemData barItem = (ItemData)var5.next();
         if (i <= 1) {
            int moneyPrice = barItem.getPrice();
            Item3D i3D = new Item3D(this.player, barItem);
            this.allItems.add(new InteractableItem(i3D, (e) -> {
               if (barItem.getPermission() != null && !this.player.hasPermission(barItem.getPermission())) {
                  this.player.sendMessage(barItem.getNopermissionmessage() != null ? barItem.getNopermissionmessage() : lang.no_permission);
               } else {
                  if (moneyPrice > 0) {
                     if (!EconomyBridge.hasValidEconomy()) {
                        this.player.sendMessage(ChatColor.RED + "This command has a price, but Vault with a compatible economy plugin was not found. For security, the command has been blocked. Please inform the staff.");
                        return;
                     }

                     if (!EconomyBridge.hasMoney(this.player, (double)moneyPrice)) {
                        this.player.sendMessage((new String(lang.no_money)).replace("{money}", EconomyBridge.formatMoney((double)moneyPrice)));
                        return;
                     }

                     if (!EconomyBridge.takeMoney(this.player, (double)moneyPrice)) {
                        return;
                     }
                  }

                  if (barItem.getCommands() != null) {
                     (new CommandDeserializer(this.player, barItem.getCommands())).execute();
                  }

               }
            }));
            Location loc = this.bar[i].clone();
            loc.setDirection(this.center.toVector().subtract(loc.toVector()).normalize());
            i3D.setPosition(loc);
         }
      }

      this.displayName.spawn();
      this.displayName.sendUpdatedmetatada();
      this.isSpawned = true;
   }

   private void addPreviousPageButton() {
      ItemData data = new ItemData(lang.button_previous_name, config.button_previous_texture, false);
      this.previousButton = new Item3D(this.player, data);
      this.previousButton.setSmall();
      this.previousButton.setPosition(this.previous);
      this.allItems.add(new InteractableItem(this.previousButton, (e) -> {
         if (this.currentPage <= 0) {
            if (this.menu.getPreviousMenu() != null && this.menu.getPreviousMenu() != null) {
               ThreeDimensionalMenu previous = this.menu.getPreviousMenu();
               (new ThreeDimensionGUI(this.player, previous)).openGui();
            }
         } else {
            --this.currentPage;
            this.switchPage();
         }

      }));
   }

   private void addNextPageButton() {
      ItemData data = new ItemData(lang.button_next_name, config.button_next_texture, false);
      this.nextButton = new Item3D(this.player, data);
      this.nextButton.setSmall();
      this.nextButton.setPosition(this.next);
      this.allItems.add(new InteractableItem(this.nextButton, (e) -> {
         ++this.currentPage;
         this.switchPage();
      }));
   }

   private void setupItems() {
      float yaw = this.center.getYaw();
      int maxNumber = Math.ceil((double)(this.items.size() / 5)) <= (double)this.currentPage ? this.items.size() : 5 * (this.currentPage + 1);
      int p = 0;

      for(int k = 0; k < this.items.size(); ++k) {
         if (k % 5 == 0) {
            if (p == this.currentPage) {
               int pos = 0;

               int n;
               for(n = p * 5; n < maxNumber; ++n) {
                  ++pos;
               }

               n = pos % 2 == 0 ? pos / 2 : (pos - 1) / 2;
               Location l = this.center.clone().add(0.0D, 1.5D, 0.0D);
               l.setPitch(0.0F);
               if (pos % 2 != 0) {
                  this.positions[0] = l.toVector().add(l.getDirection().multiply(this.radius)).toLocation(this.center.getWorld());
               }

               int po = 0;

               int nbToDisplay;
               int index;
               int i;
               for(nbToDisplay = 1; nbToDisplay <= n; ++nbToDisplay) {
                  for(index = 1; index <= 2; ++index) {
                     int check = index % 2 == 0 ? -1 : 1;
                     i = this.angle_step * check * nbToDisplay;
                     ++po;
                     l.setYaw(yaw - (float)i / (float)(pos % 2 == 0 ? 1.5D : 1.0D) * (float)(pos % 2 == 0 && nbToDisplay == 2 ? 1.4D : 1.0D));
                     int i = pos % 2 == 0 ? po - 1 : po;
                     this.positions[i] = l.toVector().add(l.getDirection().multiply(this.radius)).toLocation(this.center.getWorld());
                  }
               }

               nbToDisplay = 0;

               for(index = p * 5; index < maxNumber; ++index) {
                  ++nbToDisplay;
               }

               index = 0;
               Map<Item3D, Location> map = new HashMap();

               for(i = p * 5; i < maxNumber; ++i) {
                  Item3D it = ((InteractableItem)this.items.get(i)).getItem();
                  map.put(it, this.positions[this.indexConverter(nbToDisplay, index)]);
                  ++index;
               }

               if (this.pp.getThreeDimensionGUI() != null) {
                  map.keySet().forEach((itx) -> {
                     Location loc = (Location)map.get(itx);
                     loc.setDirection(this.center.toVector().subtract(loc.toVector()).normalize());
                     itx.setPosition(loc);
                  });
               }
               break;
            }

            ++p;
         }
      }
      Sound.UI_BUTTON_CLICK

   }

   private int indexConverter(int nbToDisplay, int currentIndex) {
      if (nbToDisplay == 5) {
         if (currentIndex == 0) {
            return 3;
         }

         if (currentIndex == 2) {
            return 0;
         }

         if (currentIndex == 3) {
            return 2;
         }
      } else if (nbToDisplay == 4) {
         if (currentIndex == 0) {
            return 2;
         }

         if (currentIndex == 1) {
            return 0;
         }

         if (currentIndex == 2) {
            return 1;
         }
      } else if (nbToDisplay == 3) {
         if (currentIndex == 1) {
            return 0;
         }

         if (currentIndex == 0) {
            return 1;
         }
      }

      return currentIndex;
   }

   private void switchPage() {
      this.getAllItems().stream().filter((item) -> {
         return this.items.contains(item);
      }).forEach((item) -> {
         if (item.getItem().isSpawned()) {
            item.getItem().remove();
         }

      });
      if (this.currentPage >= (int)Math.ceil((double)((float)this.items.size() / 5.0F)) - 1) {
         if (this.nextButton != null && this.nextButton.isSpawned()) {
            this.nextButton.remove();
         }
      } else if (this.nextButton == null || this.nextButton != null && !this.nextButton.isSpawned()) {
         this.addNextPageButton();
      }

      if (this.currentPage >= 1) {
         if (this.previousButton == null || this.previousButton != null && !this.previousButton.isSpawned()) {
            this.addPreviousPageButton();
         }
      } else if (this.menu.getPreviousMenu() != null) {
         if (this.previousButton == null || this.previousButton != null && !this.previousButton.isSpawned()) {
            this.addPreviousPageButton();
         }
      } else if (this.previousButton != null && this.previousButton.isSpawned()) {
         this.previousButton.remove();
      }

      this.setupItems();
   }

   private void reset() {
      this.displayName.despawn();
      if (this.previousButton != null) {
         this.previousButton.remove();
      }

      if (this.nextButton != null) {
         this.nextButton.remove();
      }

      this.items.clear();
      this.allItems.clear();
      this.pp.setThreeDimensionGUI((ThreeDimensionGUI)null);
   }

   public Location getCenter() {
      return this.center;
   }

   public void closeGui() {
      if (this.pp.getThreeDimensionGUI() != null) {
         this.getAllItems().forEach((item) -> {
            if (item.getItem().isSpawned()) {
               item.getItem().remove();
            }

         });
         this.reset();
         this.isSpawned = false;
      }
   }

   public List<InteractableItem> getAllItems() {
      return this.allItems;
   }

   public boolean isSpawned() {
      return this.isSpawned;
   }

   public void setLastSelected(Item3D lastSelected) {
      this.lastSelected = lastSelected;
   }

   public Item3D getLastSelected() {
      return this.lastSelected;
   }

   public int getSelected() {
      return this.selected;
   }

   public void setSelected(int selected) {
      this.selected = selected;
   }

   public void addSelected() {
      ++this.selected;
   }

   public ThreeDimensionalMenu getMenu() {
      return this.menu;
   }
}
