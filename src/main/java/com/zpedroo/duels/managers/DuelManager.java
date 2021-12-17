package com.zpedroo.duels.managers;

import com.zpedroo.duels.Duels;
import com.zpedroo.duels.enums.CancelReason;
import com.zpedroo.duels.enums.DuelStatus;
import com.zpedroo.duels.objects.Duel;
import com.zpedroo.duels.utils.FileUtils;
import com.zpedroo.duels.utils.config.DuelSettings;
import com.zpedroo.duels.utils.config.Messages;
import com.zpedroo.duels.utils.serialization.LocationSerialization;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

import static com.zpedroo.duels.utils.config.Settings.*;

public class DuelManager extends DataManager {

    private static DuelManager instance;
    public static DuelManager getInstance() { return instance; }

    private DuelSettings settings;

    public DuelManager() {
        instance = this;
        this.settings = loadSettings();
    }

    public void invite(Player inviter, Player invited) {
        if (settings.getPos1() == null || settings.getPos2() == null || settings.getExit() == null || settings.getBox() == null) {
            inviter.sendMessage(Messages.INVALID_LOCATION);
            return;
        }

        if (inviter == null) return;
        if (invited == null) {
            inviter.sendMessage(Messages.OFFLINE_PLAYER);
            return;
        }

        if (inviter.equals(invited)) {
            inviter.sendMessage(Messages.INVITE_YOURSELF);
            return;
        }

        if (isHappening()) {
            inviter.sendMessage(Messages.HAPPENING_DUEL);
            return;
        }

        Duel duel = new Duel(inviter, invited);
        getCache().setDuel(duel);

        new HashSet<>(Bukkit.getOnlinePlayers()).forEach(player -> {
            if (StringUtils.equals(player.getUniqueId().toString(), invited.getUniqueId().toString())) return;
            for (String msg : Messages.BROADCAST_DUEL) {
                player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                        "{inviter}",
                        "{invited}",
                        "{expire}"
                }, new String[]{
                        inviter.getName(),
                        invited.getName(),
                        EXPIRE_TIME.toString()
                }));
            }
        });

        for (String msg : Messages.PLAYER_INVITED) {
            invited.sendMessage(StringUtils.replaceEach(msg, new String[]{
                    "{inviter}",
                    "{invited}",
                    "{expire}"
            }, new String[]{
                    inviter.getName(),
                    invited.getName(),
                    EXPIRE_TIME.toString()
            }));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (duel.getStatus() != DuelStatus.PENDING) return;

                duel.setStatus(DuelStatus.FINISHED);
                getCache().setDuel(null);

                for (String msg : Messages.EXPIRED_DUEL) {
                    Bukkit.broadcastMessage(StringUtils.replaceEach(msg, new String[]{
                            "{inviter}",
                            "{invited}",
                            "{expire}"
                    }, new String[]{
                            inviter.getName(),
                            invited.getName(),
                            EXPIRE_TIME.toString()
                    }));
                }
            }
        }.runTaskLaterAsynchronously(Duels.get(), 20 * EXPIRE_TIME);
    }

    public void accept(Player player) {
        Duel duel = getHappeningDuel();
        if (duel == null) {
            player.sendMessage(Messages.NOTHING_TO_ACCEPT);
            return;
        }

        Player inviter = duel.getInviter();
        if (inviter == null) return;

        Player invited = duel.getInvited();
        if (invited == null) return;
        if (!StringUtils.equals(invited.getUniqueId().toString(), player.getUniqueId().toString())) {
            player.sendMessage(Messages.NOTHING_TO_ACCEPT);
            return;
        }

        duel.setStatus(DuelStatus.STARTED);
        inviter.teleport(settings.getPos1(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        invited.teleport(settings.getPos2(), PlayerTeleportEvent.TeleportCause.PLUGIN);

        new HashSet<>(Bukkit.getOnlinePlayers()).forEach(onlinePlayer -> {
            for (String msg : Messages.STARTED_DUEL) {
                onlinePlayer.sendMessage(StringUtils.replaceEach(msg, new String[]{
                        "{inviter}",
                        "{invited}"
                }, new String[]{
                        inviter.getName(),
                        invited.getName()
                }));
            }
        });
    }

    public void cancel(Player player, CancelReason reason) {
        Duel duel = getHappeningDuel();

        switch (reason) {
            case DISCONNECTED:
                for (String msg : Messages.PLAYER_DISCONNECTED) {
                    Bukkit.broadcastMessage(StringUtils.replaceEach(msg, new String[]{
                            "{player}"
                    }, new String[]{
                            player.getName(),
                    }));
                }
                break;
            case REFUSED:
                if (duel == null || duel.getStatus() != DuelStatus.PENDING || !StringUtils.equals(duel.getInvited().getUniqueId().toString(), player.getUniqueId().toString())) {
                    player.sendMessage(Messages.NOTHING_TO_REFUSE);
                    return;
                }

                for (String msg : Messages.REFUSED_DUEL) {
                    Bukkit.broadcastMessage(StringUtils.replaceEach(msg, new String[]{
                            "{player}"
                    }, new String[]{
                            player.getName(),
                    }));
                }
                break;
        }

        if (duel == null) return;

        duel.setStatus(DuelStatus.FINISHED);
        getCache().setDuel(null);
    }

    public void finish(Player winner, Player loser) {
        Duel duel = getHappeningDuel();
        if (duel == null) return;

        duel.setStatus(DuelStatus.FINISHED);

        DataManager.getInstance().load(winner).addWins(1);
        DataManager.getInstance().load(loser).addDefeats(1);

        for (String msg : Messages.FINISHED_DUEL) {
            Bukkit.broadcastMessage(StringUtils.replaceEach(msg, new String[]{
                    "{winner}",
                    "{loser}",
                    "{finish}"
            }, new String[]{
                    winner.getName(),
                    loser.getName(),
                    FINISH_TIME.toString()
            }));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (winner == null) return;

                winner.teleport(settings.getExit(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                getCache().setDuel(null);
            }
        }.runTaskLater(Duels.get(), 20 * FINISH_TIME);
    }

    public Duel getHappeningDuel() {
        return getCache().getDuel();
    }

    public Boolean isHappening() {
        return getCache().getDuel() != null;
    }

    public Boolean isDueling(Player... players) {
        if (!isHappening()) return false;
        if (getHappeningDuel().getStatus() != DuelStatus.STARTED) return false;

        for (Player player : players) {
            if (StringUtils.equals(player.getUniqueId().toString(), getHappeningDuel().getInviter().getUniqueId().toString())) return true;
            if (StringUtils.equals(player.getUniqueId().toString(), getHappeningDuel().getInvited().getUniqueId().toString())) return true;
        }

        return false;
    }

    public DuelSettings getSettings() {
        return settings;
    }

    private DuelSettings loadSettings() {
        FileUtils.Files file = FileUtils.Files.LOCATIONS;

        Location pos1 = LocationSerialization.deserialize(FileUtils.get().getString(file, "Locations.pos1"));
        Location pos2 = LocationSerialization.deserialize(FileUtils.get().getString(file, "Locations.pos2"));
        Location exit = LocationSerialization.deserialize(FileUtils.get().getString(file, "Locations.exit"));
        Location box = LocationSerialization.deserialize(FileUtils.get().getString(file, "Locations.box"));

        return new DuelSettings(pos1, pos2, exit, box);
    }
}