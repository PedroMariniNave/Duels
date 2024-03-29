package com.zpedroo.duels;

import com.zpedroo.duels.commands.DuelCmd;
import com.zpedroo.duels.commands.MythCmd;
import com.zpedroo.duels.listeners.PlayerGeneralListeners;
import com.zpedroo.duels.managers.DataManager;
import com.zpedroo.duels.managers.DuelManager;
import com.zpedroo.duels.mysql.DBConnection;
import com.zpedroo.duels.tasks.SaveTask;
import com.zpedroo.duels.utils.FileUtils;
import com.zpedroo.duels.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

import static com.zpedroo.duels.utils.config.Settings.*;

public class Duels extends JavaPlugin {

    private static Duels instance;
    public static Duels get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);

        if (!isMySQLEnabled(getConfig())) {
            getLogger().log(Level.SEVERE, "MySQL are disabled! You need to enable it.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new DBConnection(getConfig());
        new DuelManager();
        new Menus();
        new SaveTask(this);

        registerListeners();
        registerCommand(MAIN_COMMAND, MAIN_ALIASES, new DuelCmd());
        registerCommand(MYTH_COMMAND, MYTH_ALIASES, new MythCmd());
    }

    public void onDisable() {
        if (!isMySQLEnabled(getConfig())) return;

        try {
            DataManager.getInstance().saveAll();
            DBConnection.getInstance().closeConnection();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "An error occurred while trying to save data!");
            ex.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }

    private void registerCommand(String command, List<String> aliases, CommandExecutor executor) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, this);
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Boolean isMySQLEnabled(FileConfiguration file) {
        if (!file.contains("MySQL.enabled")) return false;

        return file.getBoolean("MySQL.enabled");
    }
}