package me.dave.simplechatbubbles;

import me.dave.simplechatbubbles.command.ChatBubblesCmd;
import me.dave.simplechatbubbles.config.ConfigManager;
import me.dave.simplechatbubbles.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleChatBubbles extends JavaPlugin {
    private static SimpleChatBubbles plugin;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        plugin = this;

        configManager = new ConfigManager();
        configManager.reloadConfig();

        ChatBubble.enableChatBubbles();

        getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);

        getServer().getCommandMap().register("chatbubbles", new ChatBubblesCmd("chatbubbles"));
    }

    @Override
    public void onDisable() {
        ChatBubble.disableChatBubbles();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static SimpleChatBubbles getInstance() {
        return plugin;
    }
}
