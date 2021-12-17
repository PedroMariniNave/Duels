package com.zpedroo.duels.objects;

import com.zpedroo.duels.enums.DuelStatus;
import org.bukkit.entity.Player;

public class Duel {

    private Player inviter;
    private Player invited;
    private DuelStatus status;

    public Duel(Player inviter, Player invited) {
        this.inviter = inviter;
        this.invited = invited;
        this.status = DuelStatus.PENDING;
    }

    public Player getInviter() {
        return inviter;
    }

    public Player getInvited() {
        return invited;
    }

    public DuelStatus getStatus() {
        return status;
    }

    public void setStatus(DuelStatus status) {
        this.status = status;
    }
}