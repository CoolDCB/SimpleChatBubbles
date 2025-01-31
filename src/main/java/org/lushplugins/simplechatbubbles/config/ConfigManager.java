package org.lushplugins.simplechatbubbles.config;

import org.lushplugins.simplechatbubbles.SimpleChatBubbles;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class ConfigManager {
    private String messageFormat;
    private int messageLifespan;
    private int maxBubbleCount;
    private double distanceBetweenBubbles;
    private double bubbleOffset;
    private final HashMap<String, String> messages = new HashMap<>();
    private final HashMap<Character, Integer> fontWidths = new HashMap<>();

    public ConfigManager() {
        SimpleChatBubbles.getInstance().saveDefaultConfig();

        File fontFile = new File(SimpleChatBubbles.getInstance().getDataFolder(),"font-widths.txt");
        if (!fontFile.exists()) {
            fontFile.getParentFile().mkdirs();
            SimpleChatBubbles.getInstance().saveResource("font-widths.txt", false);
        }
    }

    public void reloadConfig() {
        SimpleChatBubbles.getInstance().reloadConfig();
        FileConfiguration config = SimpleChatBubbles.getInstance().getConfig();

        messageFormat = config.getString("bubbles.message-format", "%message%");
        messageLifespan = (int) (20 * config.getDouble("bubbles.lifespan", 10.0));
        maxBubbleCount = config.getInt("bubbles.max-count", 3);
        distanceBetweenBubbles = config.getDouble("bubbles.distance-between", 0.1);
        bubbleOffset = config.getDouble("bubbles.player-offset", 0.0);

        messages.clear();
        ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        if (messagesSection != null) {
            messagesSection.getValues(false).forEach((key, value) -> messages.put(key, (String) value));
        }

        fontWidths.clear();
        try {
            File fontFile = new File(SimpleChatBubbles.getInstance().getDataFolder(),"font-widths.txt");
            Scanner sc = new Scanner(fontFile, StandardCharsets.UTF_8);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.length() >= 3) {
                    fontWidths.put(line.charAt(0),Integer.parseInt(line.substring(2)));
                }
            }

            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessageFormat() {
        return messageFormat;
    }

    public int getMessageLifespan() {
        return messageLifespan;
    }

    public int getMaxBubbleCount() {
        return maxBubbleCount;
    }

    public double getDistanceBetweenBubbles() {
        return distanceBetweenBubbles;
    }

    public double getBubbleOffset() {
        return bubbleOffset;
    }

    public String getMessage(String messageName) {
        return getMessage(messageName, "");
    }

    public String getMessage(String messageName, String def) {
        String output = messages.getOrDefault(messageName, def);

        if (messages.containsKey("prefix")) {
            return output.replaceAll("%prefix%", messages.get("prefix"));
        } else {
            return output;
        }
    }

    public int getStringWidth(String string) {
        int totalWidth = 0;

        for (Character character : string.toCharArray()) {
            totalWidth += fontWidths.getOrDefault(character, 6);
        }

        return totalWidth;
    }
}
