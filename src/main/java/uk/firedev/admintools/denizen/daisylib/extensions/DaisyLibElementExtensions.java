package uk.firedev.admintools.denizen.daisylib.extensions;

import com.denizenscript.denizencore.objects.core.ElementTag;
import net.Zrips.CMILib.Colors.CMIChatColor;

public class DaisyLibElementExtensions {

    public static void register() {
        // <--[tag]
        // @attribute <ElementTag.parse_cmi_colors>
        // @returns ElementTag
        // @plugin AdminTools, CMI
        // @description
        // Returns the ElementTag, run through CMI's colorizer
        // -->
        ElementTag.tagProcessor.registerTag(ElementTag.class, "parse_cmi_colors", (attribute, element) ->
                new ElementTag(CMIChatColor.colorize(element.asString())));
    }

}
