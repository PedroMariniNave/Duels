package com.zpedroo.duels.utils.config;

import com.zpedroo.duels.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.List;

public class Settings {

    public static final String MAIN_COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.commands.main.command");

    public static final List<String> MAIN_ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.commands.main.aliases");

    public static final String MYTH_COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.commands.myth.command");

    public static final List<String> MYTH_ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.commands.myth.aliases");

    public static final Long SAVE_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.save-interval");

    public static final Long EXPIRE_TIME = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.expire-time");

    public static final Long FINISH_TIME = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.finish-time");

    public static final String MYTH_TAG = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.myth.tag"));
}