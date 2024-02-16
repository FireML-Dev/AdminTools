package uk.firedev.admintools;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import uk.firedev.daisylib.Loggers;
import uk.firedev.daisylib.libs.Anon8281.universalScheduler.UniversalScheduler;
import uk.firedev.daisylib.libs.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import uk.firedev.admintools.commands.DisablePluginCommand;
import uk.firedev.admintools.commands.AdminToolsCommand;
import uk.firedev.admintools.commands.resourceadmin.ResourceAdminCommand;
import uk.firedev.admintools.commands.resourceadmin.ResourceAdminListener;
import uk.firedev.admintools.config.ConfigManager;
import uk.firedev.admintools.config.MessageManager;
import uk.firedev.admintools.denizen.DenizenIntegration;
import uk.firedev.admintools.jobs.JobsPlacePatch;
import uk.firedev.admintools.listeners.BowProtection;
import uk.firedev.admintools.listeners.MendingPrevention;
import uk.firedev.admintools.plotsquared.PreventMap;

import java.util.logging.Level;

public class AdminTools extends JavaPlugin implements Listener {

    private static AdminTools instance;
    private static TaskScheduler scheduler;

    public MultiverseCore mvCore = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        scheduler = UniversalScheduler.getScheduler(this);
        saveDefaultConfig();
        reload();
        loadCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    private void onServerLoad(ServerLoadEvent event) {
        PluginManager pm = this.getServer().getPluginManager();
        if (pm.isPluginEnabled("Denizen")) {
            DenizenIntegration.setup();
        }
        if (pm.isPluginEnabled("Multiverse-Core")) {
            mvCore = (MultiverseCore) pm.getPlugin("Multiverse-Core");
            this.getCommand("resourceadmin").setExecutor(new ResourceAdminCommand());
            this.getCommand("resourceadmin").setTabCompleter(new ResourceAdminCommand());
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
        }
    }

    public void disablePlugin() {
        this.getServer().getPluginManager().disablePlugin(this);
    }

    public void reload() {
        this.reloadConfig();
        if (ConfigManager.getInstance() == null) {
            new ConfigManager("config.yml", this);
        } else {
            ConfigManager.getInstance().reload();
        }
        if (MessageManager.getInstance() == null) {
            new MessageManager("messages.yml", this);
        } else {
            MessageManager.getInstance().reload();
        }
        if (MessageUtils.getInstance() == null) {
            new MessageUtils(MessageManager.getInstance().getConfig());
        } else {
            MessageUtils.getInstance().reload(MessageManager.getInstance().getConfig());
        }
    }

    private void loadCommands() {
        this.getCommand("admintools").setExecutor(new AdminToolsCommand());
        this.getCommand("admintools").setTabCompleter(new AdminToolsCommand());

        this.getCommand("disableplugin").setExecutor(new DisablePluginCommand());
        this.getCommand("disableplugin").setTabCompleter(new DisablePluginCommand());
    }

    private void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new MendingPrevention(), this);
        pm.registerEvents(new BowProtection(), this);
    }

    public static AdminTools getInstance() { return instance; }

    public static TaskScheduler getScheduler() { return scheduler; }

}