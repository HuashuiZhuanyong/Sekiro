package hszy.ydy.sekiro;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Sekiro extends JavaPlugin {
    static double magnification,punishment;
    static int enchantedlvl;
    static List<String> tips;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("sekiro Prepared");
        saveDefaultConfig();
        FileConfiguration c = getConfig();
        magnification = (c.getDouble("magnification"));
        punishment =c.getDouble("punishment");
        enchantedlvl = c.getInt("enchantedlvl");
        tips = c.getStringList("tips");
        getServer().getPluginManager().registerEvents(new MyListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("finished");
    }
}
