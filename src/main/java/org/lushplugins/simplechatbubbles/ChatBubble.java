package org.lushplugins.simplechatbubbles;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.data.TextHologramData;
import de.oliver.fancyholograms.api.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatBubble {
    // TODO: Create ChatBubbleManager class instead of static variables
    private static BukkitTask bubbleTask;
    private static ConcurrentHashMap<UUID, String> processMap;
    private static ConcurrentHashMap<UUID, ChatBubble> bubbleMap;

    private final Player player;
    private final Hologram[] chatBubbles = new Hologram[SimpleChatBubbles.getInstance().getConfigManager().getMaxBubbleCount()];
    private final int[] duration = new int[chatBubbles.length];

    public ChatBubble(Player player) {
        this.player = player;
    }

    public void addChat(String message) {
        message = SimpleChatBubbles.getInstance().getConfigManager().getMessageFormat().replace("%message%", message);

        if (chatBubbles[chatBubbles.length - 1] != null) {
            removeChat(chatBubbles[chatBubbles.length - 1]);
        }

        for (int i = chatBubbles.length - 1; i > 0; i--) {
            chatBubbles[i] = chatBubbles[i - 1];
            duration[i] = duration[i - 1];
        }

        Hologram hologram = FancyHologramsPlugin.get().getHologramManager().create(new TextHologramData("chat_bubble", player.getLocation().setDirection(new Vector(90, 0, 0)))
            .setText(List.of(message))
            .setBillboard(Display.Billboard.VERTICAL)
            .setTranslation(new Vector3f(0, (float) (1.1 + SimpleChatBubbles.getInstance().getConfigManager().getBubbleOffset()), 0)));

        Bukkit.getOnlinePlayers().forEach(hologram::forceShowHologram);

        SimpleChatBubbles.getInstance().getPassengerManager().addPassenger(player.getEntityId(), player.getUniqueId(), hologram.getEntityId(), hologram.getViewers());

        chatBubbles[0] = hologram;
        duration[0] = 0;

        for (int i = chatBubbles.length - 1; i > 0; i--) {
            if (chatBubbles[i] != null) {
                Hologram currHologram = chatBubbles[i];
                TextHologramData hologramData = (TextHologramData) currHologram.getData();

                float translation = (float) (((double) SimpleChatBubbles.getInstance().getConfigManager().getStringWidth(hologramData.getText().get(0)) / 200 + 1) * 0.3F + SimpleChatBubbles.getInstance().getConfigManager().getDistanceBetweenBubbles());
                hologramData.setTranslation(hologramData.getTranslation().add(new Vector3f(0F, translation,0F)));
                currHologram.refreshHologram(Bukkit.getOnlinePlayers());
            }
        }
    }

    public void removeChat(Hologram hologram) {
        Bukkit.getOnlinePlayers().forEach(hologram::forceHideHologram);

        SimpleChatBubbles.getInstance().getPassengerManager().removePassenger(player.getEntityId(), hologram.getEntityId(), hologram.getViewers());

        hologram.deleteHologram();
    }

    public void increaseDuration(int val) {
        for (int i = 0; i < duration.length; i++) {
            if (duration[i] + val >= SimpleChatBubbles.getInstance().getConfigManager().getMessageLifespan()) {
                duration[i] = 0;
                if (chatBubbles[i] != null) {
                    removeChat(chatBubbles[i]);
                }

                chatBubbles[i] = null;
            } else {
                duration[i] = duration[i] + val;
            }
        }
    }

    public boolean isEmpty() {
        return chatBubbles[0] == null;
    }

    public void remove() {
        for (int i = 0; i < chatBubbles.length; i++) {
            if (chatBubbles[i] != null) {
                removeChat(chatBubbles[i]);
            }

            chatBubbles[i] = null;
        }
    }


    public static void enableChatBubbles() {
        processMap = new ConcurrentHashMap<>();
        bubbleMap = new ConcurrentHashMap<>();

        bubbleTask = Bukkit.getScheduler().runTaskTimer(SimpleChatBubbles.getInstance(), () -> {
            processMap.forEach((uuid, message) -> {
                ChatBubble chatBubble;
                if (bubbleMap.containsKey(uuid)) {
                    chatBubble = bubbleMap.get(uuid);
                } else {
                    chatBubble = new ChatBubble(Bukkit.getServer().getPlayer(uuid));
                    bubbleMap.put(uuid, chatBubble);
                }

                chatBubble.addChat(message);
            });
            processMap.clear();

            bubbleMap.forEach((uuid, chatBubble) -> {
                chatBubble.increaseDuration(2);

                if (chatBubble.isEmpty()) {
                    chatBubble.remove();
                    bubbleMap.remove(uuid);
                }
            });
        }, 0L, 2L);
    }

    public static void disableChatBubbles() {
        if (bubbleTask != null) {
            bubbleTask.cancel();
            bubbleTask = null;
        }

        removeChatBubble();

        processMap = null;
        bubbleMap = null;
    }

    public static void processChatMessage(UUID uuid, String message) {
        processMap.put(uuid, message);
    }

    public static void removeChatBubble(UUID uuid) {
        if (processMap != null) {
            processMap.remove(uuid);
        }

        if (bubbleMap != null && bubbleMap.containsKey(uuid)) {
            bubbleMap.get(uuid).remove();
            bubbleMap.remove(uuid);
        }
    }

    public static void removeChatBubble() {
        if (processMap != null) {
            processMap.clear();
        }

        if (bubbleMap != null) {
            bubbleMap.values().forEach(ChatBubble::remove);
            bubbleMap.clear();
        }
    }
}
