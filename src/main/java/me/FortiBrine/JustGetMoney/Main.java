package me.FortiBrine.JustGetMoney;

import me.FortiBrine.JustGetMoney.listeners.Listener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getMain() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        File config = new File(this.getDataFolder()+File.separator+"config.yml");
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }

        Bukkit.getPluginManager().registerEvents(new Listener(), this);
    }

}
