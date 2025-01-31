package org.lushplugins.simplechatbubbles.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.lushplugins.simplechatbubbles.SimpleChatBubbles;

public class PassengerListener implements Listener {

    @EventHandler
    public void onEntityMount(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(SimpleChatBubbles.getInstance(), () -> {
            SimpleChatBubbles.getInstance().getPassengerManager().getPassengerContainer(player.getEntityId()).sendPacket(Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).toList());
        }, 1);
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(SimpleChatBubbles.getInstance(), () -> {
            SimpleChatBubbles.getInstance().getPassengerManager().getPassengerContainer(player.getEntityId()).sendPacket(Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).toList());
        }, 1);
    }
}
