package uk.firedev.admintools;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import uk.firedev.admintools.commands.AdminToolsCommand;
import uk.firedev.admintools.commands.DisablePluginCommand;
import uk.firedev.admintools.config.MainConfig;
import uk.firedev.admintools.config.MessageConfig;
import uk.firedev.admintools.denizen.DenizenIntegration;
import uk.firedev.admintools.jobs.DenizenKillLimiter;
import uk.firedev.admintools.jobs.DenizenNoPaymentFlag;
import uk.firedev.admintools.jobs.JobsPlacePatch;
import uk.firedev.admintools.listeners.CustomItemProtection;
import uk.firedev.admintools.listeners.MendingPrevention;
import uk.firedev.admintools.plotsquared.PreventMap;
import uk.firedev.admintools.reward.RewardLoader;
import uk.firedev.admintools.worldmanager.WorldManagerCommand;
import uk.firedev.admintools.worldmanager.WorldManagerConfig;
import uk.firedev.daisylib.Loggers;
import uk.firedev.daisylib.libs.Anon8281.universalScheduler.UniversalScheduler;
import uk.firedev.daisylib.libs.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;

import java.util.logging.Level;

public class AdminTools extends JavaPlugin implements Listener {

    private static AdminTools instance;
    private static TaskScheduler scheduler;

    private MultiverseCore mvCore = null;

    @Override
    public void onEnable() {
        instance = this;
        scheduler = UniversalScheduler.getScheduler(this);
        reload();
        loadCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {}

    @EventHandler
    private void onServerLoad(ServerLoadEvent event) {
        PluginManager pm = this.getServer().getPluginManager();
        if (pm.isPluginEnabled("Denizen")) {
            DenizenIntegration.setup();
        }
        if (pm.isPluginEnabled("Multiverse-Core")) {
            mvCore = (MultiverseCore) pm.getPlugin("Multiverse-Core");
            WorldManagerConfig.getInstance().reload();
            WorldManagerCommand.getInstance().register();
            Loggers.info(getComponentLogger(), "WorldManager Command has been enabled!");
        }
        if (pm.isPluginEnabled("PlotSquared")) {
            pm.registerEvents(new PreventMap(), this);
            Loggers.info(getComponentLogger(), "Mapart Protection has been enabled!");
        }
        if (pm.isPluginEnabled("Jobs")) {
            pm.registerEvents(new JobsPlacePatch(), this);
            Loggers.info(getComponentLogger(), "Jobs Place Patch has been enabled.");
            if (pm.isPluginEnabled("Denizen")) {
                pm.registerEvents(new DenizenNoPaymentFlag(), this);
                pm.registerEvents(new DenizenKillLimiter(), this);
                Loggers.info(getComponentLogger(), "Denizen Jobs Hooks have been enabled.");
            }
        }
        new RewardLoader().load();
    }

    public void reload() {
        MainConfig.getInstance().reload();
        MessageConfig.getInstance().reload();
        if (mvCore != null) {
            WorldManagerConfig.getInstance().reload();
            WorldManagerConfig.getInstance().populateManagedWorldMap();
        }
    }

    private void loadCommands() {
        AdminToolsCommand.getInstance().register();
        DisablePluginCommand.getInstance().register();
    }

    private void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new MendingPrevention(), this);
        pm.registerEvents(new CustomItemProtection(), this);
    }

    public static AdminTools getInstance() { return instance; }

    public static TaskScheduler getScheduler() { return scheduler; }

    public @Nullable MVWorldManager getMultiverseWorldManager() {
        if (mvCore == null) {
            return null;
        }
        return mvCore.getMVWorldManager();
    }

}