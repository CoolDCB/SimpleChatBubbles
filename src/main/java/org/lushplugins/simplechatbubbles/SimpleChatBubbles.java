package org.lushplugins.simplechatbubbles;

import org.lushplugins.simplechatbubbles.command.ChatBubblesCmd;
import org.lushplugins.simplechatbubbles.config.ConfigManager;
import org.lushplugins.simplechatbubbles.listener.PassengerListener;
import org.lushplugins.simplechatbubbles.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.simplechatbubbles.packets.passenger.PassengerManager;

public final class SimpleChatBubbles extends JavaPlugin {
    private static SimpleChatBubbles plugin;

    private final PassengerManager passengerManager = new PassengerManager();
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        plugin = this;

        this.configManager = new ConfigManager();
        this.configManager.reloadConfig();

        ChatBubble.enableChatBubbles();

        getServer().getPluginManager().registerEvents(new PassengerListener(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);

        getServer().getCommandMap().register("chatbubbles", new ChatBubblesCmd("chatbubbles"));
    }

    @Override
    public void onDisable() {
        ChatBubble.disableChatBubbles();

        this.passengerManager.invalidateAll();
    }

    public PassengerManager getPassengerManager() {
        return passengerManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static SimpleChatBubbles getInstance() {
        return plugin;
    }
}
