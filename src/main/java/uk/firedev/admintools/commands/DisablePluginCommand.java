package uk.firedev.admintools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MessageConfig;
import uk.firedev.daisylib.command.ICommand;

import java.util.Arrays;
import java.util.List;

public class DisablePluginCommand implements ICommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }
        PluginManager pm = AdminTools.getInstance().getServer().getPluginManager();
        Plugin pl = pm.getPlugin(args[0]);
        if (pm.isPluginEnabled(pl)) {
            pm.disablePlugin(pl);
            MessageConfig.getInstance().sendPrefixedMessage(sender, "<red>Disabled " + pl.getName() + "</red>");
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return List.of();
        }

        return switch (args.length) {
            case 1 -> processTabCompletions(args[0],
                    // List of all enabled plugins
                    Arrays.stream(AdminTools.getInstance().getServer().getPluginManager().getPlugins())
                            .filter(Plugin::isEnabled)
                            .map(Plugin::getName)
                            .toList()
            );
            default -> List.of();
        };
    }

}
