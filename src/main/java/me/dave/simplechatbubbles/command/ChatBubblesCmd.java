package me.dave.simplechatbubbles.command;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.simplechatbubbles.SimpleChatBubbles;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatBubblesCmd extends Command {

    public ChatBubblesCmd(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!sender.isOp()) {
            return true;
        }

        if (args.length >= 1) {
            switch (args[0]) {
                case "reload" -> {
                    if (!sender.hasPermission("chatbubbles.reload")) {
                        ChatColorHandler.sendMessage(sender, SimpleChatBubbles.getInstance().getConfigManager().getMessage("no-permissions"));
                        return true;
                    }

                    SimpleChatBubbles.getInstance().getConfigManager().reloadConfig();

                    ChatColorHandler.sendMessage(sender, SimpleChatBubbles.getInstance().getConfigManager().getMessage("reload"));
                    return true;
                }
                case "version" -> {
                    ChatColorHandler.sendMessage(sender, "&#a8e1ffYou are currently running SimpleChatBubbles version &#58b1e0" + SimpleChatBubbles.getInstance().getDescription().getVersion());
                    return true;
                }
            }
        } else {
            ChatColorHandler.sendMessage(sender, "&#a8e1ffYou are currently running SimpleChatBubbles version &#58b1e0" + SimpleChatBubbles.getInstance().getDescription().getVersion());
            return true;
        }

        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> tabComplete = new ArrayList<>();
        List<String> wordCompletion = new ArrayList<>();
        boolean wordCompletionSuccess = false;

        if (args.length == 1) {
            if (sender.hasPermission("chatbubbles.reload")) {
                tabComplete.add("reload");
            }
            if (sender.hasPermission("chatbubbles.version")) {
                tabComplete.add("version");
            }
        }

        for (String currTab : tabComplete) {
            int currArg = args.length - 1;
            if (currTab.startsWith(args[currArg])) {
                wordCompletion.add(currTab);
                wordCompletionSuccess = true;
            }
        }

        return wordCompletionSuccess ? wordCompletion : tabComplete;
    }
}
