package com.zpedroo.duels.tasks;

import com.zpedroo.duels.managers.DataManager;
import com.zpedroo.duels.mysql.DBConnection;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static com.zpedroo.duels.utils.config.Settings.*;

public class SaveTask extends BukkitRunnable {

    public SaveTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, 20 * SAVE_INTERVAL, 20 * SAVE_INTERVAL);
    }

    @Override
    public void run() {
        DataManager.getInstance().saveAll();
        DataManager.getInstance().getCache().setTopWins(DBConnection.getInstance().getDBManager().getTop());
    }
}