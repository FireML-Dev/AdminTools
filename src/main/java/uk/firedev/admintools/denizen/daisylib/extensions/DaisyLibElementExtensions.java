package uk.firedev.admintools.denizen.daisylib.extensions;

import com.denizenscript.denizencore.objects.core.ElementTag;
import uk.firedev.daisylib.utils.StringUtils;

public class DaisyLibElementExtensions {

    public static void register() {
        // <--[tag]
        // @attribute <ElementTag.daisylib_translate_colors>
        // @returns ElementTag
        // @plugin AdminTools, CMI
        // @description
        // Returns the ElementTag, run through DaisyLib's color converter
        // -->
        ElementTag.tagProcessor.registerTag(ElementTag.class, "daisylib_translate_colors", (attribute, element) -> new ElementTag(StringUtils.parseColors(element.asString())));
    }

}
