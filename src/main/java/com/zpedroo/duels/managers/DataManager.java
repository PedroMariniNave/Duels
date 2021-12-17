package com.zpedroo.duels.managers;

import com.zpedroo.duels.managers.cache.DataCache;
import com.zpedroo.duels.mysql.DBConnection;
import com.zpedroo.duels.objects.PlayerData;
import com.zpedroo.duels.utils.FileUtils;
import com.zpedroo.duels.utils.config.Messages;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private DataCache dataCache;

    public DataManager() {
        instance = this;
        this.dataCache = new DataCache();
    }

    public PlayerData load(Player player) {
        PlayerData data = dataCache.getPlayerData().get(player);
        if (data == null) {
            data = DBConnection.getInstance().getDBManager().loadData(player);
            dataCache.getPlayerData().put(player, data);
        }

        return data;
    }

    public void save(Player player) {
        if (player == null) return;

        PlayerData data = dataCache.getPlayerData().get(player);
        if (data == null) return;
        if (!data.isQueueUpdate()) return;

        DBConnection.getInstance().getDBManager().saveData(data);
        data.setUpdate(false);
    }

    public void saveAll() {
        new HashSet<>(dataCache.getPlayerData().keySet()).forEach(this::save);
    }

    public void setMyth(Player player) {
        dataCache.setMythName(player.getName());

        for (String msg : Messages.NEW_MYTH) {
            Bukkit.broadcastMessage(StringUtils.replaceEach(msg, new String[]{
                    "{player}"
            }, new String[]{
                    player.getName()
            }));
        }

        new HashSet<>(Bukkit.getOnlinePlayers()).forEach(target -> {
            target.playSound(target.getLocation(), Sound.ANVIL_USE, 0.2f, 10f);
        });

        FileUtils.Files file = FileUtils.Files.CONFIG;
        FileUtils.get().getFile(file).get().set("Settings.myth.name", player.getName());
        FileUtils.get().getFile(file).save();
    }

    public DataCache getCache() {
        return dataCache;
    }
}