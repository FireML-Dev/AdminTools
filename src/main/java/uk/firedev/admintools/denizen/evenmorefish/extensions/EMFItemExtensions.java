package uk.firedev.admintools.denizen.evenmorefish.extensions;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.oheers.fish.FishUtils;

public class EMFItemExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <ItemTag.is_emf_fish>
        // @returns ElementTag(Boolean)
        // @plugin AdminTools, CMI
        // @description
        // Returns whether the item is a valid EMF fish
        // -->
        ItemTag.tagProcessor.registerTag(ElementTag.class, "is_emf_fish", ((attribute, item) -> new ElementTag(FishUtils.isFish(item.getItemStack()))));

        // <--[tag]
        // @attribute <ItemTag.is_emf_bait>
        // @returns ElementTag(Boolean)
        // @plugin AdminTools, CMI
        // @description
        // Returns whether the item is a valid EMF bait
        // -->
        ItemTag.tagProcessor.registerTag(ElementTag.class, "is_emf_bait", ((attribute, item) -> new ElementTag(FishUtils.isBaitObject(item.getItemStack()))));
    }

}