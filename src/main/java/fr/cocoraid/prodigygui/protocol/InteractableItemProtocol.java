package fr.cocoraid.prodigygui.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import fr.cocoraid.prodigygui.ProdigyGUIPlayer;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import fr.cocoraid.prodigygui.threedimensionalgui.item.InteractableItem;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ColoredParticleData;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.NormalParticleData;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ParticleData;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.SoundData;
import fr.cocoraid.prodigygui.utils.particle.ColoredParticle;
import fr.cocoraid.prodigygui.utils.particle.NormalParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class InteractableItemProtocol {
   public InteractableItemProtocol(Plugin plugin) {
      ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, new PacketType[]{Client.USE_ENTITY}) {
         public void onPacketReceiving(PacketEvent event) {
            PacketContainer packet = event.getPacket();
            Player p = event.getPlayer();
            if (p != null && ((WrappedEnumEntityUseAction)packet.getEnumEntityUseActions().read(0)).getAction() == EntityUseAction.ATTACK) {
               int id = (Integer)packet.getIntegers().read(0);
               ProdigyGUIPlayer pp = ProdigyGUIPlayer.instanceOf(p);
               if (pp.getThreeDimensionGUI() != null) {
                  pp.getThreeDimensionGUI().getAllItems().stream().filter((item) -> {
                     return item.getItem().getSelector().getId() == id && pp.getThreeDimensionGUI().isSpawned() && p.equals(item.getItem().getPlayer());
                  }).findFirst().ifPresent((item) -> {
                     if (item.getInteractable() instanceof InteractableItem.InteractClickable) {
                        Bukkit.getScheduler().runTask(this.plugin, () -> {
                           ThreeDimensionalMenu menu;
                           if (item.getItem().getData().getSoundData() != null) {
                              SoundData datax = item.getItem().getData().getSoundData();
                              p.playSound(p.getLocation(), datax.getSound(), datax.getVolume(), datax.getPitch());
                           } else {
                              menu = pp.getThreeDimensionGUI().getMenu();
                              if (menu.getDefaultClickSound() != null) {
                                 p.playSound(p.getLocation(), menu.getDefaultClickSound().getSound(), menu.getDefaultClickSound().getVolume(), menu.getDefaultClickSound().getPitch());
                              }
                           }

                           if (item.getItem().getData().getParticleData() != null) {
                              ParticleData data = item.getItem().getData().getParticleData();
                              InteractableItemProtocol.this.sendParticle(data, item.getItem().getLocation(), p);
                           } else {
                              menu = pp.getThreeDimensionGUI().getMenu();
                              if (menu.getDefaultClickParticle() != null) {
                                 ParticleData dataxx = menu.getDefaultClickParticle();
                                 InteractableItemProtocol.this.sendParticle(dataxx, item.getItem().getLocation(), p);
                              }
                           }

                           ((InteractableItem.InteractClickable)item.getInteractable()).interact(item);
                        });
                     }

                  });
               }
            }

         }
      });
   }

   private void sendParticle(ParticleData data, Location l, Player p) {
      if (data instanceof ColoredParticleData) {
         ColoredParticleData coloredData = (ColoredParticleData)data;
         (new ColoredParticle(l)).setColor(coloredData.getR(), coloredData.getG(), coloredData.getB()).setRadius(coloredData.getRadius()).setAmount(coloredData.getAmount()).setParticle(coloredData.getParticle()).sendParticle(p);
      } else if (data instanceof NormalParticleData) {
         NormalParticleData normalData = (NormalParticleData)data;
         (new NormalParticle(l)).setOffset(new Vector(normalData.getOffsetX(), normalData.getOffsetY(), normalData.getOffsetZ())).setSpeed(normalData.getSpeed()).setAmount(normalData.getAmount()).setParticle(normalData.getParticle()).sendParticle(p);
      }

   }
}
