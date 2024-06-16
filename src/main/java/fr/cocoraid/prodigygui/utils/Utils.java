package fr.cocoraid.prodigygui.utils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;
import fr.cocoraid.prodigygui.nms.wrapper.packet.WrapperPlayServerChat;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;

public class Utils {
   public static void sendActionMessage(Player p, String msg) {
      WrappedChatComponent chatComponent = WrappedChatComponent.fromText(msg);
      if (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_11_R1)) {
         ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
         PacketContainer chatPacket = protocolManager.createPacket(Server.CHAT);
         chatPacket.getChatComponents().write(0, chatComponent);
         chatPacket.getBytes().write(0, (byte)2);

         try {
            protocolManager.sendServerPacket(p, chatPacket);
         } catch (InvocationTargetException var6) {
            throw new RuntimeException(var6);
         }
      } else {
         WrapperPlayServerChat chatPacket = new WrapperPlayServerChat();
         chatPacket.setMessage(chatComponent);
         chatPacket.setChatType(ChatType.GAME_INFO);
         chatPacket.sendPacket(p);
      }

   }
}
