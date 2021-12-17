package com.zpedroo.duels.utils.config;

import org.bukkit.Location;

public class DuelSettings {

    private Location pos1;
    private Location pos2;
    private Location exit;
    private Location box;

    public DuelSettings(Location pos1, Location pos2, Location exit, Location box) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.exit = exit;
        this.box = box;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public Location getExit() {
        return exit;
    }

    public Location getBox() {
        return box;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public void setExit(Location exit) {
        this.exit = exit;
    }

    public void setBox(Location box) {
        this.box = box;
    }
}