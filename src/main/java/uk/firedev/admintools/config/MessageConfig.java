package uk.firedev.admintools.config;

import uk.firedev.admintools.AdminTools;

public class MessageConfig extends uk.firedev.daisylib.Config implements uk.firedev.daisylib.utils.MessageUtils {

    private static MessageConfig instance = null;

    private MessageConfig() {
        super("messages.yml", AdminTools.getInstance());
    }

    public static MessageConfig getInstance() {
        if (instance == null) {
            instance = new MessageConfig();
        }
        return instance;
    }

}
