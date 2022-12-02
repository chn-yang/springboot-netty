package com.kaikai.netty.client.handler;

import com.kaikai.netty.common.codec.InvocationDecoder;
import com.kaikai.netty.common.codec.InvocationEncoder;
import com.kaikai.netty.common.dispatcher.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyClientHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 客户端心跳时间, IdleStateHandler的writerIdleTimeSeconds参数, 超过这个时间则触发IdleStateEvent
     * 在客户端的userEventTriggered方法处理该事件发送心跳请求
     */
    private static final Integer HEARTBEAT_TIMEOUT_SECONDS = 50;

    @Autowired
    private MessageDispatcher messageDispatcher;

    @Autowired
    private NettyClientHandler nettyClientHandler;

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                // 空闲检测, 使用写空闲检测发送心跳包
                .addLast(new IdleStateHandler(0, HEARTBEAT_TIMEOUT_SECONDS, 0))
                // 编码器
                .addLast(new InvocationEncoder())
                // 解码器
                .addLast(new InvocationDecoder())
                // 消息分发器
                .addLast(messageDispatcher)
                // 客户端处理器
                .addLast(nettyClientHandler)
        ;
    }

}
