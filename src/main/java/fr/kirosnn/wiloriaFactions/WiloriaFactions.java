package fr.kirosnn.wiloriaFactions;

import fr.kirosnn.wiloriaFactions.cmd.FactionsCommand;
import fr.kirosnn.wiloriaFactions.cmd.tabcompleter.FactionTabCompleter;
import fr.kirosnn.wiloriaFactions.data.Factions;
import fr.kirosnn.wiloriaFactions.listeners.ClaimEnterListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class WiloriaFactions extends JavaPlugin {
    private static WiloriaFactions instance;
    private Factions factions;
    private final FactionsCommand cmdFactions = new FactionsCommand(this);

    @Override
    public void onEnable() {
        instance = this;
        this.factions = new Factions();
        this.factions.load();

        getCommand("f").setExecutor(new FactionsCommand(this));
        getCommand("f").setTabCompleter(new FactionTabCompleter());

        getServer().getPluginManager().registerEvents(new ClaimEnterListener(this), this);
    }

    @Override
    public void onDisable() {
        if (this.factions != null) {
            this.factions.forceSave();
        }
    }

    public static WiloriaFactions getInstance() {
        return instance;
    }

    public Factions getFactions() {
        return this.factions;
    }
}