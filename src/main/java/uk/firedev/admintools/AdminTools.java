package uk.firedev.admintools;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import uk.firedev.admintools.commands.AdminToolsCommand;
import uk.firedev.admintools.commands.DisablePluginCommand;
import uk.firedev.admintools.commands.resourceadmin.ResourceAdminCommand;
import uk.firedev.admintools.commands.resourceadmin.ResourceAdminListener;
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
import uk.firedev.daisylib.Loggers;
import uk.firedev.daisylib.libs.Anon8281.universalScheduler.UniversalScheduler;
import uk.firedev.daisylib.libs.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;

import java.util.logging.Level;

public class AdminTools extends JavaPlugin implements Listener {

    private static AdminTools instance;
    private static TaskScheduler scheduler;

    public MultiverseCore mvCore = null;

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
            ResourceAdminCommand.getInstance().register();
            pm.registerEvents(new ResourceAdminListener(), this);
            Loggers.log(Level.INFO, getLogger(), "ResourceAdmin Command has been enabled!");
        }
        if (pm.isPluginEnabled("PlotSquared")) {
            pm.registerEvents(new PreventMap(), this);
            Loggers.log(Level.INFO, getLogger(), "Mapart Protection has been enabled!");
        }
        if (pm.isPluginEnabled("Jobs")) {
            pm.registerEvents(new JobsPlacePatch(), this);
            Loggers.log(Level.INFO, getLogger(), "Jobs Place Patch has been enabled.");
            if (pm.isPluginEnabled("Denizen")) {
                pm.registerEvents(new DenizenNoPaymentFlag(), this);
                pm.registerEvents(new DenizenKillLimiter(), this);
                Loggers.log(Level.INFO, getLogger(), "Denizen Jobs Hooks have been enabled.");
            }
        }
        new RewardLoader().load();
    }

    public void reload() {
        this.reloadConfig();
        MainConfig.getInstance().reload();
        MessageConfig.getInstance().reload();
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

}