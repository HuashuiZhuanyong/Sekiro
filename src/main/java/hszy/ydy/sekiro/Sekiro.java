package hszy.ydy.sekiro;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Sekiro extends JavaPlugin {
    static double magnification,punishment1,punishment2,punishment3;
    static int enchantedlvl,angle;
    static List<String> tips;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("sekiro Prepared");
        saveDefaultConfig();
        FileConfiguration c = getConfig();
        magnification = (c.getDouble("magnification"));
        punishment1 =c.getDouble("punishment1");
        punishment2 =c.getDouble("punishment2");
        punishment3 =c.getDouble("punishment3");
        enchantedlvl = c.getInt("enchantedlvl");
        tips = c.getStringList("tips");
        angle = c.getInt("angle")/2;
        getServer().getPluginManager().registerEvents(new MyListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("finished");
    }
}
