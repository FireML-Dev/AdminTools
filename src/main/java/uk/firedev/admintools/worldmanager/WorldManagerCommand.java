package uk.firedev.admintools.worldmanager;

import uk.firedev.daisylib.libs.commandapi.CommandAPICommand;
import uk.firedev.daisylib.libs.commandapi.CommandPermission;
import uk.firedev.daisylib.libs.commandapi.arguments.Argument;
import uk.firedev.daisylib.libs.commandapi.arguments.ArgumentSuggestions;
import uk.firedev.daisylib.libs.commandapi.arguments.StringArgument;

import java.util.concurrent.CompletableFuture;

public class WorldManagerCommand extends CommandAPICommand {

    private static WorldManagerCommand instance;

    private WorldManagerCommand() {
        super("worldmanager");
        withPermission(CommandPermission.fromString("admintools.command.worldmanager"));
        withShortDescription("Manage worlds");
        withFullDescription("Manage worlds");
        withSubcommands(getDeleteCommand(), getCreateCommand());
    }

    private CommandAPICommand getDeleteCommand() {
        return new CommandAPICommand("delete")
                .withArguments(getManagedWorldsArgument())
                .executes((sender, arguments) -> {
                    String[] args = arguments.rawArgs();
                    ManagedWorld managedWorld = WorldManagerConfig.getInstance().getLoadedManagedWorlds().get(args[0]);
                    if (managedWorld == null) {
                        // TODO not exists message
                        return;
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
                        // TODO not exists message
                        return;
                    }
                    managedWorld.createWorld(sender);
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
