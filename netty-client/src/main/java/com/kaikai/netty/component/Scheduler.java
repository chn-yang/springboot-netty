package com.kaikai.netty.component;

import com.kaikai.netty.client.NettyClient;
import com.kaikai.netty.common.codec.Invocation;
import com.kaikai.netty.common.message.chat.ChatSendToOneRequest;
import com.kaikai.netty.common.message.heartbeat.HeartbeatRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Component
public class Scheduler {

    @Resource
    NettyClient nettyClient;

    //每30秒发送心跳
    @Scheduled(fixedDelay = 30000)
    public void sayWord() {
        HeartbeatRequest request = new HeartbeatRequest(HeartbeatRequest.TYPE);
        Invocation invocation = new Invocation(ChatSendToOneRequest.TYPE, request);
        // 发送消息
        nettyClient.send(invocation);

        log.info("{} 发送心跳", LocalDateTime.now());
    }
}
