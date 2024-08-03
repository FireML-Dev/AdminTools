package uk.firedev.admintools.worldmanager;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.firedev.admintools.AdminTools;
import uk.firedev.daisylib.Config;
import uk.firedev.daisylib.Loggers;
import uk.firedev.daisylib.libs.boostedyaml.block.implementation.Section;
import uk.firedev.daisylib.message.component.ComponentMessage;
import uk.firedev.daisylib.utils.LocationHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldManagerConfig extends Config {

    private static WorldManagerConfig instance;

    private Map<String, ManagedWorld> loadedManagedWorlds;

    private WorldManagerConfig() {
        super("world-manager.yml", "world-manager.yml", AdminTools.getInstance(), false);
    }

    public static WorldManagerConfig getInstance() {
        if (instance == null) {
            instance = new WorldManagerConfig();
            instance.populateManagedWorldMap();
        }
        return instance;
    }

    public void populateManagedWorldMap() {
        if (loadedManagedWorlds == null) {
            loadedManagedWorlds = new HashMap<>();
        }
        loadedManagedWorlds.clear();
        Section section = getConfig().getSection("worlds");
        if (section == null) {
            return;
        }
        section.getRoutesAsStrings(false).forEach(key -> {
            Section worldSection = section.getSection(key);
            if (worldSection != null) {
                ManagedWorld managedWorld = new ManagedWorld(worldSection);
                loadedManagedWorlds.put(managedWorld.getName(), managedWorld);
            }
        });
        System.out.println("Loaded ManagedWorlds: " + getLoadedManagedWorlds().keySet());
    }

    public Map<String, ManagedWorld> getLoadedManagedWorlds() {
        return Map.copyOf(loadedManagedWorlds);
    }

    public List<String> getGlobalPreCreationCommands() {
        return getConfig().getStringList("global-creation-commands.pre-commands");
    }

    public List<String> getGlobalPostCreationCommands() {
        return getConfig().getStringList("global-creation-commands.post-commands");
    }

    public List<String> getGlobalPreDeletionCommands() {
        return getConfig().getStringList("global-deletion-commands.pre-commands");
    }

    public List<String> getGlobalPostDeletionCommands() {
        return getConfig().getStringList("global-deletion-commands.post-commands");
    }

    public void setEvacuationLocation(@NotNull Location location) {
        LocationHelper.addToConfig(getConfig(), "evacuation", location);
        // Save to the file.
        try {
            getConfig().save();
        } catch (IOException exception) {
            Loggers.warn(AdminTools.getInstance().getComponentLogger(), "Could not save world manager evacuation location.");
        }
    }

    public @Nullable Location getEvacuationLocation() {
        return LocationHelper.getFromConfig(getConfig(), "evacuation");
    }

    public @NotNull ComponentMessage getEvacKickMessage() {
        return new ComponentMessage(getConfig(), "evacuation.kick-message", "<aqua>Unable to find a safe evacuation point. You have been removed from the server.");
    }

}
