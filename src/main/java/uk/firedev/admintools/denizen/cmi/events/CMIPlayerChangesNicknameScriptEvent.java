package uk.firedev.admintools.denizen.cmi.events;

import com.Zrips.CMI.events.CMIPlayerNickNameChangeEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CMIPlayerChangesNicknameScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // cmi player changes nickname
    //
    // @Cancellable true
    //
    // @Triggers when a player changes their nickname
    //
    // @Context
    // <context.nickname> returns the new name as an ElementTag
    //
    // @Player Always.
    //
    // @Plugin CMI
    //
    // @Group AdminTools
    //
    // -->

    public CMIPlayerNickNameChangeEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("cmi player changes nickname");
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("nickname")) {
            return new ElementTag(event.getNickName());
        }
        return super.getContext(name);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getUser().getOfflinePlayer()), null);
    }

    @EventHandler
    public void onCMIPlayerChangeNickname(CMIPlayerNickNameChangeEvent event) {
        this.event = event;
        fire(event);
    }

}
