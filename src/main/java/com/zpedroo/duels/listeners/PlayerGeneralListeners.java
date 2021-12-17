package com.zpedroo.duels.listeners;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.zpedroo.duels.enums.CancelReason;
import com.zpedroo.duels.enums.DuelStatus;
import com.zpedroo.duels.managers.DataManager;
import com.zpedroo.duels.managers.DuelManager;
import com.zpedroo.duels.objects.Duel;
import com.zpedroo.duels.utils.FileUtils;
import com.zpedroo.duels.utils.config.Messages;
import com.zpedroo.duels.utils.config.Settings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(ChatMessageEvent event) {
        Player player = event.getSender();
        if (!StringUtils.equals(DataManager.getInstance().getCache().getMythName(), player.getName())) return;

        event.setTagValue("myth", Settings.MYTH_TAG);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (StringUtils.equals(DataManager.getInstance().getCache().getMythName(), player.getName())) {
            DataManager.getInstance().setMyth(killer);
        }

        if (!DuelManager.getInstance().isDueling(event.getEntity(), event.getEntity().getKiller())) return;

        DuelManager.getInstance().finish(killer, player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        DataManager.getInstance().save(player);
        Duel duel = DuelManager.getInstance().getHappeningDuel();
        if (duel == null) return;
        if (!StringUtils.equals(duel.getInviter().getUniqueId().toString(), player.getUniqueId().toString())
                && !StringUtils.equals(duel.getInvited().getUniqueId().toString(), player.getUniqueId().toString())) return;
        if (duel.getStatus() == DuelStatus.STARTED) {
            Player winner = duel.getInvited() == player ? duel.getInviter() : duel.getInvited();
            player.damage(9999, winner);
            return;
        }
        if (duel.getStatus() != DuelStatus.PENDING) return;

        DuelManager.getInstance().cancel(player, CancelReason.DISCONNECTED);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!DuelManager.getInstance().isDueling(event.getPlayer())) return;
        if (event.getPlayer().hasPermission("x1.admin")) return;

        String executedCommand = event.getMessage().split(" ")[0].replace("/", "").toLowerCase();
        if (FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Whitelisted-Commands").contains(executedCommand)) return;
        if (StringUtils.equals(Settings.MAIN_COMMAND, executedCommand)) return;
        if (Settings.MAIN_ALIASES.contains(executedCommand)) return;

        Player player = event.getPlayer();

        event.setCancelled(true);
        player.sendMessage(Messages.BLACKLISTED_COMMAND);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeleport(PlayerTeleportEvent event) {
        if (!DuelManager.getInstance().isDueling(event.getPlayer())) return;

        event.setCancelled(true);
    }
}