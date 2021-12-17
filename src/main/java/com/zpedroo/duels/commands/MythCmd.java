package com.zpedroo.duels.commands;

import com.zpedroo.duels.managers.DataManager;
import com.zpedroo.duels.utils.config.Messages;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MythCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            switch (args[0].toUpperCase()) {
                case "SET":
                    if (!sender.hasPermission("duels.admin")) break;
                    if (args.length < 2) break;

                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) break;

                    DataManager.getInstance().setMyth(target);
                    return true;
            }
        }

        String mythName = DataManager.getInstance().getCache().getMythName();

        for (String str : Messages.MYTH) {
            sender.sendMessage(StringUtils.replaceEach(str, new String[]{
                    "{player}"
            }, new String[]{
                    mythName
            }));
        }
        return true;
    }
}
