package uk.firedev.admintools.denizen;

import com.denizenscript.denizencore.events.ScriptEvent;
import org.bukkit.plugin.PluginManager;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.config.MainConfig;
import uk.firedev.admintools.denizen.daisylib.events.DaisyLibReloadScriptEvent;
import uk.firedev.admintools.denizen.daisylib.events.PlayerMoveBlockScriptEvent;
import uk.firedev.admintools.denizen.daisylib.events.PlayerMoveChunkScriptEvent;
import uk.firedev.admintools.denizen.daisylib.extensions.DaisyLibLocationExtensions;
import uk.firedev.admintools.denizen.evenmorefish.events.EMFCompetitionEndScriptEvent;
import uk.firedev.admintools.denizen.evenmorefish.events.EMFCompetitionStartScriptEvent;
import uk.firedev.admintools.denizen.evenmorefish.events.EMFFishCaughtScriptEvent;
import uk.firedev.admintools.denizen.evenmorefish.events.EMFRewardScriptEvent;
import uk.firedev.admintools.denizen.evenmorefish.extensions.EMFItemExtensions;
import uk.firedev.daisylib.Loggers;

public class DenizenIntegration {

    public static void setup() {
        PluginManager pm = AdminTools.getInstance().getServer().getPluginManager();
        if (pm.isPluginEnabled("Denizen")) {
            // DaisyLib
            if (MainConfig.getInstance().isDenizenDaisyLibHook()) {
                DaisyLibLocationExtensions.register();
                ScriptEvent.registerScriptEvent(new DaisyLibReloadScriptEvent());
                ScriptEvent.registerScriptEvent(new PlayerMoveBlockScriptEvent());
                ScriptEvent.registerScriptEvent(new PlayerMoveChunkScriptEvent());
                Loggers.info(AdminTools.getInstance().getComponentLogger(), "Loaded bridge for 'DaisyLib'!");
            }

            // Other plugins
            if (MainConfig.getInstance().isDenizenEMFHook() && pm.isPluginEnabled("EvenMoreFish")) {
                ScriptEvent.registerScriptEvent(new EMFFishCaughtScriptEvent());
                ScriptEvent.registerScriptEvent(new EMFRewardScriptEvent());
                ScriptEvent.registerScriptEvent(new EMFCompetitionStartScriptEvent());
                ScriptEvent.registerScriptEvent(new EMFCompetitionEndScriptEvent());
                EMFItemExtensions.register();
                Loggers.info(AdminTools.getInstance().getComponentLogger(), "Loaded bridge for 'EvenMoreFish'!");
            }
        }
    }

}
