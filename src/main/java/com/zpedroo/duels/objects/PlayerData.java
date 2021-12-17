package com.zpedroo.duels.objects;

import java.util.UUID;

public class PlayerData {

    private UUID uuid;
    private Integer wins;
    private Integer defeats;
    private Boolean update;

    public PlayerData(UUID uuid, Integer wins, Integer defeats) {
        this.uuid = uuid;
        this.wins = wins;
        this.defeats = defeats;
        this.update = false;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Integer getWins() {
        return wins;
    }

    public Integer getDefeats() {
        return defeats;
    }

    public Boolean isQueueUpdate() {
        return update;
    }

    public void addWins(Integer wins) {
        this.wins += wins;
        this.update = true;
    }

    public void addDefeats(Integer defeats) {
        this.defeats += defeats;
        this.update = true;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }
}
