package uk.firedev.admintools.denizen;

import com.denizenscript.denizencore.events.ScriptEvent;
import org.bukkit.plugin.PluginManager;
import uk.firedev.admintools.AdminTools;
import uk.firedev.admintools.denizen.cmi.events.*;
import uk.firedev.admintools.denizen.cmi.extensions.CMIPlayerExtensions;
import uk.firedev.admintools.denizen.daisylib.events.DaisyLibReloadScriptEvent;
import uk.firedev.admintools.denizen.daisylib.events.PlayerMoveBlockScriptEvent;
import uk.firedev.admintools.denizen.daisylib.events.PlayerMoveChunkScriptEvent;
import uk.firedev.admintools.denizen.daisylib.extensions.DaisyLibElementExtensions;
import uk.firedev.admintools.denizen.daisylib.extensions.DaisyLibLocationExtensions;
import uk.firedev.admintools.denizen.evenmorefish.events.EMFCompetitionStartScriptEvent;
import uk.firedev.admintools.denizen.evenmorefish.events.EMFFishCaughtScriptEvent;
import uk.firedev.admintools.denizen.evenmorefish.events.EMFRewardScriptEvent;
import uk.firedev.admintools.denizen.evenmorefish.extensions.EMFItemExtensions;
import uk.firedev.daisylib.Loggers;

import java.util.logging.Level;

public class DenizenIntegration {

    public static void setup() {
        PluginManager pm = AdminTools.getInstance().getServer().getPluginManager();
        if (pm.isPluginEnabled("Denizen")) {
            // DaisyLib
            DaisyLibLocationExtensions.register();
            DaisyLibElementExtensions.register();
            ScriptEvent.registerScriptEvent(new DaisyLibReloadScriptEvent());
            ScriptEvent.registerScriptEvent(new PlayerMoveBlockScriptEvent());
            ScriptEvent.registerScriptEvent(new PlayerMoveChunkScriptEvent());

            // Other plugins
            if (pm.isPluginEnabled("EvenMoreFish")) {
                ScriptEvent.registerScriptEvent(new EMFFishCaughtScriptEvent());
                ScriptEvent.registerScriptEvent(new EMFRewardScriptEvent());
                ScriptEvent.registerScriptEvent(new EMFCompetitionStartScriptEvent());
                EMFItemExtensions.register();
                Loggers.log(Level.INFO, AdminTools.getInstance().getLogger(), "Loaded bridge for 'EvenMoreFish'!");
            }
            if (pm.isPluginEnabled("CMI")) {
                ScriptEvent.registerScriptEvent(new CMIPlayerVanishScriptEvent());
                ScriptEvent.registerScriptEvent(new CMIPlayerUnVanishScriptEvent());
                ScriptEvent.registerScriptEvent(new CMIPlayerChangesNicknameScriptEvent());
                ScriptEvent.registerScriptEvent(new CMIPlayerAFKEnterScriptEvent());
                ScriptEvent.registerScriptEvent(new CMIPlayerAFKKickScriptEvent());
                ScriptEvent.registerScriptEvent(new CMIPlayerAFKLeaveScriptEvent());
                CMIPlayerExtensions.register();
                Loggers.log(Level.INFO, AdminTools.getInstance().getLogger(), "Loaded bridge for 'CMI'!");
            }
        }
    }

}
