package uk.firedev.admintools.denizen.daisylib.extensions;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import uk.firedev.daisylib.utils.BlockUtils;

public class DaisyLibLocationExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <LocationTag.is_player_placed>
        // @returns ElementTag(Boolean)
        // @plugin AdminTools, DaisyLib
        // @description
        // Returns whether this location ever had a block placed by a player
        // -->
        LocationTag.tagProcessor.registerTag(ElementTag.class, "is_player_placed", (attribute, location) -> new ElementTag(BlockUtils.isPlayerPlaced(location)));

        // <--[tag]
        // @attribute <LocationTag.is_player_broken>
        // @returns ElementTag(Boolean)
        // @plugin AdminTools, DaisyLib
        // @description
        // Returns whether this location ever had a placed block broken by a player
        // -->
        LocationTag.tagProcessor.registerTag(ElementTag.class, "is_player_broken", (attribute, location) -> new ElementTag(BlockUtils.isPlayerBroken(location)));
    }

}
