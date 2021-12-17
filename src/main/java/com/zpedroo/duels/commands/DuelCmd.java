package com.zpedroo.duels.commands;

import com.zpedroo.duels.enums.CancelReason;
import com.zpedroo.duels.managers.DuelManager;
import com.zpedroo.duels.utils.FileUtils;
import com.zpedroo.duels.utils.menu.Menus;
import com.zpedroo.duels.utils.serialization.LocationSerialization;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class DuelCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length > 0) {
            Location location = player.getLocation();
            String serialized = LocationSerialization.serialize(location);
            FileUtils.FileManager fileManager = FileUtils.get().getFile(FileUtils.Files.LOCATIONS);
            switch (args[0].toUpperCase()) {
                case "SETPOS1":
                    if (!player.hasPermission("x1.admin")) break;
                    DuelManager.getInstance().getSettings().setPos1(location);
                    fileManager.get().set("Locations.pos1", serialized);
                    fileManager.save();
                    return true;

                case "SETPOS2":
                    if (!player.hasPermission("x1.admin")) break;
                    DuelManager.getInstance().getSettings().setPos2(location);
                    fileManager.get().set("Locations.pos2", serialized);
                    fileManager.save();
                    return true;

                case "SETEXIT":
                    if (!player.hasPermission("x1.admin")) break;
                    DuelManager.getInstance().getSettings().setExit(location);
                    fileManager.get().set("Locations.exit", serialized);
                    fileManager.save();
                    return true;

                case "SETBOX":
                    if (!player.hasPermission("x1.admin")) break;
                    DuelManager.getInstance().getSettings().setBox(location);
                    fileManager.get().set("Locations.box", serialized);
                    fileManager.save();
                    return true;

                case "TOP":
                    Menus.getInstance().openTopMenu(player);
                    return true;

                case "REFUSE":
                case "RECUSAR":
                case "NEGAR":
                    DuelManager.getInstance().cancel(player, CancelReason.REFUSED);
                    return true;

                case "ACCEPT":
                case "ACEITAR":
                    DuelManager.getInstance().accept(player);
                    return true;

                case "INVITE":
                case "CONVIDAR":
                case "CONVITE":
                case "ENVIAR":
                case "DESAFIAR":
                    if (args.length < 2) break;
                    Player invited = Bukkit.getPlayer(args[1]);
                    DuelManager.getInstance().invite(player, invited);
                    return true;
                case "CAMAROTE":

                case "BOX":
                    player.teleport(DuelManager.getInstance().getSettings().getBox(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    return true;
            }
        }

        Menus.getInstance().openMainMenu(player);
        return false;
    }
}