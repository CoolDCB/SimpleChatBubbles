package org.lushplugins.simplechatbubbles.listener;

import org.lushplugins.simplechatbubbles.ChatBubble;
import org.lushplugins.simplechatbubbles.SimpleChatBubbles;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        String message = event.getMessage();

        Bukkit.getScheduler().runTaskLater(SimpleChatBubbles.getInstance(), () -> {
            if (!event.isCancelled()) {
                ChatBubble.processChatMessage(uuid, message);
            }
        }, 1);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        ChatBubble.removeChatBubble(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        ChatBubble.removeChatBubble(event.getPlayer().getUniqueId());
    }
}
