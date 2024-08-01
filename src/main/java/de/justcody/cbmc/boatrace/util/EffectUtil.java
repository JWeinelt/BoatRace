package de.justcody.cbmc.boatrace.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;

public class EffectUtil {
    public void playTotemAnimation(Player p, int customModelData) {
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = totem.getItemMeta();

        assert meta != null;

        meta.setCustomModelData(customModelData);
        totem.setItemMeta(meta);
        ItemStack hand = p.getInventory().getItemInMainHand();
        p.getInventory().setItemInMainHand(totem);
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packetTotem = manager.createPacket(PacketType.Play.Server.ENTITY_STATUS);
        packetTotem.getModifier().writeDefaults();
        packetTotem.getIntegers().write(0, p.getEntityId());
        packetTotem.getBytes().write(0, (byte)35);

        try {
            manager.sendServerPacket(p, packetTotem);
        } catch (InvocationTargetException var9) {
            InvocationTargetException invocationTargetException = var9;
            invocationTargetException.printStackTrace();
        }

        p.getInventory().setItemInMainHand(hand);
    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException var3) {
            return false;
        }
    }
}
