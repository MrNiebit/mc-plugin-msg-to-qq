package cn.lacknb.msgsync;

import cn.lacknb.msgsync.listener.ChatListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class MsgSyncQq extends JavaPlugin {
    private static final Logger log = Logger.getLogger("MsgSyncQq");

    @Override
    public void onEnable() {
        // Plugin startup logic
        log.info("msg to qq plugin is enable !");
        // 加载配置文件
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        boolean isOpen = config.getBoolean("is-open");
        if (!isOpen) {
            log.info(" 开关是关闭的 closed !");
            return;
        }
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log.info("msg to qq  plugin is disabled !");
    }
}
