package uk.firedev.admintools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MessageConfig;

import java.util.ArrayList;
import java.util.List;

public class DisablePluginCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }
        PluginManager pm = AdminTools.getInstance().getServer().getPluginManager();
        Plugin pl = pm.getPlugin(args[0]);
        if (pm.isPluginEnabled(pl)) {
            pm.disablePlugin(pl);
            MessageConfig.getInstance().sendMessage(sender, "<red>Disabled " + pl.getName() + "</red>");
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> names = new ArrayList<>();
        for (Plugin plugin : AdminTools.getInstance().getServer().getPluginManager().getPlugins()) {
            if (plugin.isEnabled()) {
                names.add(plugin.getName());
            }
        }
        List<String> suggest = new ArrayList<>(names);
        List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], suggest, completions);
        return completions;
    }

}
