package cn.lacknb.msgsync.listener;

import cn.lacknb.msgsync.MsgSyncQq;
import cn.lacknb.msgsync.util.MyHttpClient;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.logging.Logger;

public class ChatListener implements Listener {

    private static final Logger log = Logger.getLogger("ChatListener");

    private MsgSyncQq plugin;

    public ChatListener(MsgSyncQq msgSyncQq) {
        this.plugin = msgSyncQq;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        //todo 敏感信息过滤

        String name = event.getPlayer().getName();
        FileConfiguration config = plugin.getConfig();
        String api = config.getString("api-url");
        String msgPrefix = config.getString("msg-prefix");
        String requestBody = config.getString("request-body");
        String content = msgPrefix + name + ": " + message;
        assert requestBody != null;
        String replace = requestBody.replace("{msg}", content);
        try {
            String post = MyHttpClient.post(api, replace);
            log.info(post);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("msg send error !");
        }

    }

}
