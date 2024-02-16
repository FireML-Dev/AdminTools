package uk.firedev.admintools.denizen.cmi.extensions;

import com.Zrips.CMI.Containers.CMIUser;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;

public class CMIPlayerExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <ElementTag.cmi_last_location>
        // @returns LocationTag
        // @plugin AdminTools, CMI
        // @description
        // Returns the player's last location according to CMI
        // -->
        PlayerTag.tagProcessor.registerTag(LocationTag.class, "cmi_last_location", ((attribute, player) -> new LocationTag(CMIUser.getUser(player.getUUID()).getLastTeleportLocation())));

        // <--[mechanism]
        // @object PlayerTag
        // @name cmi_last_location
        // @input LocationTag
        // @plugin AdminTools, CMI
        // @description
        // Sets the player's last location in the CMI plugin
        // @tags
        // <PlayerTag.cmi_last_location>
        // -->
        PlayerTag.tagProcessor.registerMechanism("cmi_last_location", false, LocationTag.class, (player, mechanism, location) -> CMIUser.getUser(player.getUUID()).setLastTeleportLocation(location));

        // <--[tag]
        // @attribute <PlayerTag.is_cmi_vanished>
        // @returns ElementTag
        // @plugin AdminTools, CMI
        // @description
        // Returns whether the player is vanished in the CMI plugin
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "is_cmi_vanished", ((attribute, player) -> new ElementTag(CMIUser.getUser(player.getUUID()).isCMIVanished())));

        // <--[mechanism]
        // @object PlayerTag
        // @name is_cmi_vanished
        // @input ElementTag
        // @plugin AdminTools, CMI
        // @description
        // Sets whether the player is vanished in the CMI plugin
        // @tags
        // <PlayerTag.is_cmi_vanished>
        // -->
        PlayerTag.tagProcessor.registerMechanism("is_cmi_vanished", false, ElementTag.class, (player, mechanism, element) -> {
            if (mechanism.requireBoolean()) {
                CMIUser.getUser(player.getUUID()).setVanished(mechanism.getValue().asBoolean());
            }
        });
    }

}