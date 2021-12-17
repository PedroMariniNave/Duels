package com.zpedroo.duels.managers.cache;

import com.zpedroo.duels.mysql.DBConnection;
import com.zpedroo.duels.objects.Duel;
import com.zpedroo.duels.objects.PlayerData;
import com.zpedroo.duels.utils.FileUtils;
import org.bukkit.entity.Player;

import java.util.*;

public class DataCache {

    private Map<Player, PlayerData> playerData;
    private List<PlayerData> topWins;
    private String mythName;
    private Duel duel;

    public DataCache() {
        this.playerData = new HashMap<>(128);
        this.topWins = DBConnection.getInstance().getDBManager().getTop();
        this.mythName = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.myth.name");
    }

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
    }

    public List<PlayerData> getTopWins() {
        return topWins;
    }

    public String getMythName() {
        return mythName;
    }

    public Duel getDuel() {
        return duel;
    }

    public void setDuel(Duel duel) {
        this.duel = duel;
    }

    public void setTopWins(List<PlayerData> topWins) {
        this.topWins = topWins;
    }

    public void setMythName(String mythName) {
        this.mythName = mythName;
    }
}