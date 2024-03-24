package uk.firedev.admintools.commands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MessageConfig;
import uk.firedev.daisylib.libs.commandapi.CommandAPICommand;
import uk.firedev.daisylib.libs.commandapi.CommandPermission;
import uk.firedev.daisylib.libs.commandapi.arguments.Argument;
import uk.firedev.daisylib.libs.commandapi.arguments.ArgumentSuggestions;
import uk.firedev.daisylib.libs.commandapi.arguments.StringArgument;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class DisablePluginCommand extends CommandAPICommand {

    private static DisablePluginCommand instance = null;

    private DisablePluginCommand() {
        super("disableplugin");
        setPermission(CommandPermission.fromString("admintools.disableplugin"));
        withShortDescription("Disables plugins if they're being annoying");
        withFullDescription("Disables plugins if they're being annoying");
        withArguments(getPluginsArgument());
        executes((sender, arguments) -> {
            String[] args = arguments.rawArgs();
            if (args.length == 0) {
                return;
            }
            PluginManager pm = AdminTools.getInstance().getServer().getPluginManager();
            Plugin pl = pm.getPlugin(args[0]);
            if (pm.isPluginEnabled(pl)) {
                pm.disablePlugin(pl);
                MessageConfig.getInstance().sendPrefixedMessage(sender, "<red>Disabled " + pl.getName() + "</red>");
            }
        });
    }

    public static DisablePluginCommand getInstance() {
        if (instance == null) {
            instance = new DisablePluginCommand();
        }
        return instance;
    }

    private Argument<?> getPluginsArgument() {
        return new StringArgument("plugin").includeSuggestions(ArgumentSuggestions.stringsAsync(info ->
                CompletableFuture.supplyAsync(() ->
                        Arrays.stream(Bukkit.getPluginManager().getPlugins())
                                .filter(Plugin::isEnabled)
                                .map(Plugin::getName)
                                .toArray(String[]::new)
                )));
    }

}
