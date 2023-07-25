package cn.lacknb.msgsync.listener;

import cn.lacknb.msgsync.MsgSyncQq;
import cn.lacknb.msgsync.util.MyHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ChatListener implements Listener {

    private static final Logger log = Logger.getLogger("ChatListener");

    /**
     * 发送消息的api
     */
    private final String api;

    /**
     * 消息前缀
     */
    private final String msgPrefix;

    /**
     * 配置文件
     */
    private final FileConfiguration config;

    /**
     * api 请求体
     */
    private final String requestBody;

    public ChatListener(MsgSyncQq msgSyncQq) {
        this.config = msgSyncQq.getConfig();
        this.api = config.getString("api-url");
        this.msgPrefix = config.getString("msg-prefix");
        this.requestBody = config.getString("request-body");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        //todo 敏感信息过滤
        String name = event.getPlayer().getName();
        String content = name + ": " + message;
        sendMessage(content);
    }

    @EventHandler
    public void onPLayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        List<String> playerList = Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName)
                .collect(Collectors.toList());
        String builder = "玩家：" + playerName + " 加入了游戏\n" +
                "\n当前在线玩家：\n" +
                String.join("\n ", playerList);
        sendMessage(builder);
    }

    private void sendMessage(String msg) {
        msg = msgPrefix + msg;
        String replace = requestBody.replace("{msg}", URLEncoder.encode(msg));
        try {
            String post = MyHttpClient.post(api, replace);
            log.info(post);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("msg send error !");
        }
    }

    @EventHandler
    public void onPLayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        List<String> playerList = Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName)
                .collect(Collectors.toList());
        playerList.remove(playerName);
        String builder = "玩家：" + playerName + " 退出了游戏\n" +
                "\n当前在线玩家：\n" +
                String.join("\n ", playerList);
        sendMessage(builder);
    }

}
