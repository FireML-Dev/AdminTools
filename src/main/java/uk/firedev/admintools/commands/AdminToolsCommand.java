package uk.firedev.admintools.commands;

import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MessageConfig;
import uk.firedev.daisylib.libs.commandapi.CommandAPICommand;
import uk.firedev.daisylib.libs.commandapi.CommandPermission;

public class AdminToolsCommand extends CommandAPICommand {

    private static AdminToolsCommand instance = null;

    private AdminToolsCommand() {
        super("admintools");
        setPermission(CommandPermission.fromString("admintools.command"));
        withShortDescription("Handles the AdminTools Plugin");
        withFullDescription("Handles the AdminTools Plugin");
        withSubcommands(getReloadCommand());
        executes((sender, arguments) -> {
            MessageConfig.getInstance().getMainCommandUsageMessage().sendMessage(sender);
        });
    }

    public static AdminToolsCommand getInstance() {
        if (instance == null) {
            instance = new AdminToolsCommand();
        }
        return instance;
    }

    private CommandAPICommand getReloadCommand() {
        return new CommandAPICommand("reload")
                .executes((sender, arguments) -> {
                    AdminTools.getInstance().reload();
                    MessageConfig.getInstance().getMainCommandReloadedMessage().sendMessage(sender);
                });
    }

}
