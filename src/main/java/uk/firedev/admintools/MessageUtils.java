package uk.firedev.admintools;

import org.bukkit.configuration.file.FileConfiguration;

public class MessageUtils extends uk.firedev.daisylib.utils.MessageUtils {

    private static MessageUtils instance = null;

    public MessageUtils(FileConfiguration config) {
        super(config);
        instance = this;
    }

    public static MessageUtils getInstance() {
        return instance;
    }

}
