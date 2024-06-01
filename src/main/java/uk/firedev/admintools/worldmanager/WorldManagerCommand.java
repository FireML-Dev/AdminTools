package uk.firedev.admintools.worldmanager;

import org.bukkit.entity.Player;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MessageConfig;
import uk.firedev.daisylib.libs.commandapi.CommandAPICommand;
import uk.firedev.daisylib.libs.commandapi.CommandPermission;
import uk.firedev.daisylib.libs.commandapi.arguments.Argument;
import uk.firedev.daisylib.libs.commandapi.arguments.ArgumentSuggestions;
import uk.firedev.daisylib.libs.commandapi.arguments.StringArgument;
import uk.firedev.daisylib.message.component.ComponentReplacer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WorldManagerCommand extends CommandAPICommand {

    private static WorldManagerCommand instance;

    private List<UUID> deleteConfirmList = new ArrayList<>();

    private WorldManagerCommand() {
        super("worldmanager");
        withPermission(CommandPermission.fromString("admintools.command.worldmanager"));
        withShortDescription("Manage worlds");
        withFullDescription("Manage worlds");
        withSubcommands(getDeleteCommand(), getDeleteAllCommand(), getCreateCommand(), getCreateAllCommand());
        executes((sender, arguments) -> {
            MessageConfig.getInstance().getWorldManagerUsageMessage().sendMessage(sender);
        });
    }

    private CommandAPICommand getDeleteCommand() {
        return new CommandAPICommand("delete")
                .withArguments(getManagedWorldsArgument())
                .executes((sender, arguments) -> {
                    String[] args = arguments.rawArgs();
                    ManagedWorld managedWorld = WorldManagerConfig.getInstance().getLoadedManagedWorlds().get(args[0]);
                    if (managedWorld == null) {
                        final ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", args[0]);
                        MessageConfig.getInstance().getWorldManagerNullWorldMessage().applyReplacer(replacer).sendMessage(sender);
                        return;
                    }
                    if (sender instanceof Player player) {
                        if (deleteConfirmList == null) {
                            deleteConfirmList = new ArrayList<>();
                        }
                        UUID uuid = player.getUniqueId();
                        if (!deleteConfirmList.contains(uuid)) {
                            deleteConfirmList.add(uuid);
                            final ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", managedWorld.getName());
                            MessageConfig.getInstance().getWorldManagerConfirmMessage().applyReplacer(replacer).sendMessage(player);
                            AdminTools.getScheduler().runTaskLater(() -> deleteConfirmList.remove(uuid), 100L);
                            return;
                        }
                        deleteConfirmList.remove(uuid);
                    }
                    managedWorld.deleteWorld(sender);
                });
    }

    private CommandAPICommand getCreateCommand() {
        return new CommandAPICommand("create")
                .withArguments(getManagedWorldsArgument())
                .executes((sender, arguments) -> {
                    String[] args = arguments.rawArgs();
                    ManagedWorld managedWorld = WorldManagerConfig.getInstance().getLoadedManagedWorlds().get(args[0]);
                    if (managedWorld == null) {
                        final ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", args[0]);
                        MessageConfig.getInstance().getWorldManagerNullWorldMessage().applyReplacer(replacer).sendMessage(sender);
                        return;
                    }
                    managedWorld.createWorld(sender);
                });
    }

    private CommandAPICommand getCreateAllCommand() {
        return new CommandAPICommand("createAll")
                .executes((sender, arguments) -> {
                    WorldManagerConfig.getInstance().getLoadedManagedWorlds().forEach((name, managedWorld) -> managedWorld.createWorld(sender));
                });
    }

    private CommandAPICommand getDeleteAllCommand() {
        return new CommandAPICommand("deleteAll")
                .executes((sender, arguments) -> {
                    if (sender instanceof Player player) {
                        if (deleteConfirmList == null) {
                            deleteConfirmList = new ArrayList<>();
                        }
                        UUID uuid = player.getUniqueId();
                        if (!deleteConfirmList.contains(uuid)) {
                            deleteConfirmList.add(uuid);
                            final ComponentReplacer replacer = new ComponentReplacer().addReplacement("world", "<red>ALL CONFIGURED WORLDS");
                            MessageConfig.getInstance().getWorldManagerConfirmMessage().applyReplacer(replacer).sendMessage(player);
                            AdminTools.getScheduler().runTaskLater(() -> deleteConfirmList.remove(uuid), 100L);
                            return;
                        }
                        deleteConfirmList.remove(uuid);
                    }
                    WorldManagerConfig.getInstance().getLoadedManagedWorlds().forEach((name, managedWorld) -> managedWorld.deleteWorld(sender));
                });
    }

    private Argument<?> getManagedWorldsArgument() {
        return new StringArgument("managedWorld").includeSuggestions(ArgumentSuggestions.stringsAsync(info ->
                CompletableFuture.supplyAsync(() ->
                        WorldManagerConfig.getInstance().getLoadedManagedWorlds().keySet().toArray(String[]::new)
                )
        ));
    }

    public static WorldManagerCommand getInstance() {
        if (instance == null) {
            instance = new WorldManagerCommand();
        }
        return instance;
    }

}
