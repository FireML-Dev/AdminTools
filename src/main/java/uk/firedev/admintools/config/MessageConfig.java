package uk.firedev.admintools.config;

import uk.firedev.admintools.AdminTools;
import uk.firedev.daisylib.message.component.ComponentMessage;
import uk.firedev.daisylib.message.component.ComponentReplacer;

import java.util.List;

public class MessageConfig extends uk.firedev.daisylib.Config {

    private static MessageConfig instance = null;

    private MessageConfig() {
        super("messages.yml", "messages.yml", AdminTools.getInstance(), true);
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
        return new ComponentMessage(getConfig(), "prefix", "<gray>[<aqua>AdminTools</aqua>]</gray> ");
    }

    public ComponentMessage getMainCommandReloadedMessage() {
        return new ComponentMessage(getConfig(), "main-command.reloaded", "{prefix}<color:#F0E68C>AdminTools Config Reloaded</color>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getMainCommandUsageMessage() {
        String def = String.join("\n", List.of(
                "{prefix}<color:#F0E68C>Usage:",
                "{prefix}<aqua>/admintools reload"
        ));
        return new ComponentMessage(getConfig(), "main-command.usage", def)
                .applyReplacer(getPrefixReplacer());
    }

    // WORLDMANAGER MESSAGES

    public ComponentMessage getWorldManagerUsageMessage() {
        String def = String.join("\n", List.of(
                "{prefix}<green>WorldManager Usage:</green>",
                "{prefix}<color:#F0E68C>/worldmanager delete [managedWorld]</color>",
                "{prefix}<color:#F0E68C>/worldmanager create [managedWorld]</color>"
        ));
        return new ComponentMessage(getConfig(), "world-manager.usage", def)
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getWorldManagerExistsMessage() {
        return new ComponentMessage(getConfig(), "world-manager.exists", "<red>World {world} already exists! Skipping...</red>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getWorldManagerCreatedMessage() {
        return new ComponentMessage(getConfig(), "world-manager.created", "<green>World {world} has been created</green>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getWorldManagerEvacuationMessage() {
        return new ComponentMessage(getConfig(), "world-manager.evacuation", "<gold>This world is resetting. You have been teleported to an evacuation location.</gold>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getWorldManagerConfirmMessage() {
        return new ComponentMessage(getConfig(), "world-manager.confirm", "<red>Type this command again to confirm deletion of {world}.</red>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getWorldManagerNullWorldMessage() {
        return new ComponentMessage(getConfig(), "world-manager.null-world", "<red>World {world} is null! Skipping...</red>")
                .applyReplacer(getPrefixReplacer());
    }

    public ComponentMessage getWorldManagerDeletedMessage() {
        return new ComponentMessage(getConfig(), "world-manager.deleted", "<green>World {world} has been deleted.</green>")
                .applyReplacer(getPrefixReplacer());
    }

    // MENDING PREVENTION MESSAGES

    public ComponentMessage getMendingPreventionDeniedMessage() {
        return new ComponentMessage(getConfig(), "mending-prevention.denied", "<red>You cannot apply mending to custom items!</red>")
                .applyReplacer(getPrefixReplacer());
   }

}
