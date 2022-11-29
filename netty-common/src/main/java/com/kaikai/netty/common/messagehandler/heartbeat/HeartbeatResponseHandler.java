package com.kaikai.netty.common.messagehandler.heartbeat;

import com.kaikai.netty.common.dispatcher.MessageHandler;
import com.kaikai.netty.common.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;


public class HeartbeatResponseHandler implements MessageHandler<HeartbeatResponse> {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void execute(Channel channel, HeartbeatResponse message) {
        logger.info("[execute][收到连接({}) 的心跳响应]", channel.id());
    }

    @Override
    public String getType() {
        return HeartbeatResponse.TYPE;
    }
}
