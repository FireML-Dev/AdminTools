package uk.firedev.admintools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MessageConfig;

import java.util.ArrayList;
import java.util.List;

public class AdminToolsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equals("reload")) {
            AdminTools.getInstance().reload();
            MessageConfig.getInstance().sendPrefixedMessageFromConfig(sender, "messages.reloaded");
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            result.add("reload");
        }
        List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], result, completions);
        return result;
    }

}
