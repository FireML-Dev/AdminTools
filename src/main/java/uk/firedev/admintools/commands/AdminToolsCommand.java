package uk.firedev.admintools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MessageConfig;
import uk.firedev.daisylib.command.ICommand;

import java.util.List;

public class AdminToolsCommand implements ICommand {

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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return List.of();
        }

        return switch (args.length) {
            case 1 -> processTabCompletions(args[0], List.of(
                    "reload"
            ));
            default -> List.of();
        };
    }

}
