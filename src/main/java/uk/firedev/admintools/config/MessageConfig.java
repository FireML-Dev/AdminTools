package uk.firedev.admintools.config;

import uk.firedev.admintools.AdminTools;
import uk.firedev.daisylib.message.component.ComponentMessage;
import uk.firedev.daisylib.message.component.ComponentReplacer;

import java.util.List;

public class MessageConfig extends uk.firedev.daisylib.Config {

    private static MessageConfig instance = null;

    private MessageConfig() {
        super("messages.yml", AdminTools.getInstance(), true, true);
    }

    public static MessageConfig getInstance() {
        if (instance == null) {
            instance = new MessageConfig();
        }
        return instance;
    }

    public ComponentReplacer getPrefixReplacer() {
        return new ComponentReplacer().addReplacement("prefix", getPrefix().getMessage());
    }

    // GENERAL MESSAGES

    public ComponentMessage getPrefix() {
        return new ComponentMessage(getConfig(), "messages.prefix", "<gray>[<aqua>AdminTools</aqua>]</gray> ");
    }

    public ComponentMessage getMainCommandReloadedMessage() {
        return new ComponentMessage(getConfig(), "messages.main-command.reloaded", "{prefix}<color:#F0E68C>AdminTools Config Reloaded</color>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getMainCommandUsageMessage() {
        String def = String.join("\n", List.of(
                "{prefix}<color:#F0E68C>Usage:",
                "{prefix}<aqua>/admintools reload"
        ));
        return new ComponentMessage(getConfig(), "messages.main-command.usage", def)
                .applyReplacer(getPrefixReplacer());
    }

    // RESOURCEADMIN MESSAGES

    public ComponentMessage getResourceAdminUsageMessage() {
        String def = String.join("\n", List.of(
                "{prefix}<green>ResourceAdmin Usage:</green>",
                "{prefix}<color:#F0E68C>/resourceadmin delete</color>",
                "{prefix}<color:#F0E68C>/resourceadmin setup</color>"
        ));
        return new ComponentMessage(getConfig(), "messages.resource-admin.usage", def)
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminSetupBorderMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.setup.border", "<green>World {world}'s border has been set</green>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminSetupDifficultyMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.setup.difficulty", "<green>World {world}'s difficulty has been set</green>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminSetupExistsMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.setup.exists", "<red>World {world} already exists! Skipping...</red>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminSetupCreatedMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.setup.created", "<green>World {world} has been created</green>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminSetupCompleteMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.setup.complete", "<green>Resource Setup Complete!</green>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminDeleteConfirmMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.delete.confirm", "<red>Type this command again to confirm.</red>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminDeleteStartingMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.delete.starting", "<color:#F0E68C>Resource Deletion Starting...</color>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminDeleteEvacMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.delete.evac", "<gold>Resource is resetting. You have been teleported to Spawn.</gold>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminDeleteNullWorldMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.delete.null", "<red>World {world} is null! Skipping...</red>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminDeleteSuccessMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.delete.success", "<green>World {world} has been deleted.</green>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getResourceAdminDeleteCompleteMessage() {
        return new ComponentMessage(getConfig(), "messages.resource-admin.delete.complete", "<green>Resource Deletion Complete!</green>")
                .applyReplacer(getPrefixReplacer());
    }

    // MENDING PREVENTION MESSAGES

    public ComponentMessage getMendingPreventionDeniedMessage() {
        return new ComponentMessage(getConfig(), "messages.mending-prevention.denied", "<red>You cannot apply mending to custom items!</red>")
                .applyReplacer(getPrefixReplacer());
   }

   // MAPART PROTECTION MESSAGES

    public ComponentMessage getMapartProtectionDeniedMessage() {
        return new ComponentMessage(getConfig(), "messages.mapart-protection.denied", "<red>You cannot use a map inside of this plot!</red>")
                .applyReplacer(getPrefixReplacer());
    }

}
