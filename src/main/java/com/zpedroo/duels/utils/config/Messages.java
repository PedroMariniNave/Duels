package com.zpedroo.duels.utils.config;

import com.zpedroo.duels.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static final String BLACKLISTED_COMMAND = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.blacklisted-command"));

    public static final String INVALID_LOCATION = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-location"));

    public static final String INVITE_YOURSELF = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invite-yourself"));

    public static final String NOTHING_TO_ACCEPT = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.nothing-to-accept"));

    public static final String NOTHING_TO_REFUSE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.nothing-to-refuse"));

    public static final String OFFLINE_PLAYER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.offline-player"));

    public static final String HAPPENING_DUEL = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.happening-duel"));

    public static final List<String> BROADCAST_DUEL = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.broadcast-duel"));

    public static final List<String> PLAYER_INVITED = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.player-invited"));

    public static final List<String> STARTED_DUEL = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.started-duel"));

    public static final List<String> FINISHED_DUEL = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.finished-duel"));

    public static final List<String> EXPIRED_DUEL = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.expired-duel"));

    public static final List<String> REFUSED_DUEL = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.refused-duel"));

    public static final List<String> PLAYER_DISCONNECTED = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.player-disconnected"));

    public static final List<String> MYTH = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.myth"));

    public static final List<String> NEW_MYTH = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.new-myth"));

    private static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private static List<String> getColored(List<String> list) {
        List<String> colored = new ArrayList<>(list.size());
        for (String str : list) {
            colored.add(getColored(str));
        }

        return colored;
    }
}