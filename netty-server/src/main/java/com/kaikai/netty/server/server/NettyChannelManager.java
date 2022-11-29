package com.kaikai.netty.server.server;


import com.kaikai.netty.common.codec.Invocation;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 客户端 Channel 管理器。提供两种功能：
 * 1. 客户端 Channel 的管理
 * 2. 向客户端 Channel 发送消息
 */
@Component
public class NettyChannelManager {

    /**
     * {@link Channel#attr(AttributeKey)} 属性中，表示 Channel 对应的用户
     */
    private static final AttributeKey<String> CHANNEL_ATTR_KEY_USER = AttributeKey.newInstance("user");

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Object channelVal = new Object();
    /**
     * Channel 映射
     */
    private final ConcurrentMap<Channel, Object> channels = new ConcurrentHashMap<>();
    /**
     * 用户与 Channel 的映射。
     *
     * 通过它，可以获取用户对应的 Channel。这样，我们可以向指定用户发送消息。
     */
    private final ConcurrentMap<String, Channel> userChannels = new ConcurrentHashMap<>();

    /**
     * 添加 Channel 到 {@link #channels} 中
     *
     * @param channel Channel
     */
    public void add(Channel channel) {
        channels.put(channel, channelVal);
        logger.info("[add][一个连接({})加入]", channel.id());
    }

    /**
     * 添加指定用户到 {@link #userChannels} 中
     *
     * @param channel Channel
     * @param user 用户
     */
    public void addUser(Channel channel, String user) {
        if (!channels.containsKey(channel)) {
            logger.error("[addUser][连接({}) 不存在]", channel.id());
            return;
        }
        // 设置属性
        channel.attr(CHANNEL_ATTR_KEY_USER).set(user);
        // 添加到 userChannels
        userChannels.put(user, channel);
    }

    /**
     * 将 Channel 从 {@link #channels} 和 {@link #userChannels} 中移除
     *
     * @param channel Channel
     */
    public void remove(Channel channel) {
        // 移除 channels
        channels.remove(channel);
        // 移除 userChannels
        if (channel.hasAttr(CHANNEL_ATTR_KEY_USER)) {
            userChannels.remove(channel.attr(CHANNEL_ATTR_KEY_USER).get());
        }
        logger.info("[remove][一个连接({})离开]", channel.id());
    }

    /**
     * 向指定用户发送消息
     *
     * @param user 用户
     * @param invocation 消息体
     */
    public void send(String user, Invocation invocation) {
        // 获得用户对应的 Channel
        Channel channel = userChannels.get(user);
        if (channel == null) {
            logger.error("[send][连接不存在]");
            return;
        }
        if (!channel.isActive()) {
            logger.error("[send][连接({})未激活]", channel.id());
            return;
        }
        // 发送消息
        channel.writeAndFlush(invocation);
    }

    /**
     * 向所有用户发送消息
     *
     * @param invocation 消息体
     */
    public void sendAll(Invocation invocation) {
        for (Channel channel : channels.keySet()) {
            if (!channel.isActive()) {
                logger.error("[send][连接({})未激活]", channel.id());
                break;
            }
            // 发送消息
            channel.writeAndFlush(invocation);
        }
    }

}
