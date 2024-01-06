package me.dave.simplechatbubbles.listener;

import me.dave.simplechatbubbles.ChatBubble;
import me.dave.simplechatbubbles.SimpleChatBubbles;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
}
