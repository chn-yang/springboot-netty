package com.kaikai.netty.server.server.handler;

import com.kaikai.netty.common.codec.InvocationDecoder;
import com.kaikai.netty.common.codec.InvocationEncoder;
import com.kaikai.netty.common.dispatcher.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 服务端心跳超时时间, 心跳超时服务端会将客户端断开连接
     */
    private static final int HEARTBEAT_TIMEOUT_SECONDS = 55;
    private static final int READ_TIMEOUT_SECONDS = 55;

    @Autowired
    private MessageDispatcher messageDispatcher;    //消息分发处理器

    @Autowired
    private NettyServerHandler nettyServerHandler;  //连接管理服务器

    @Qualifier("serverMessageDispatcherExecutorGroup")
    @Autowired
    private EventExecutorGroup messageDispatcherExecutorGroup;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // 获得 Channel 对应的 ChannelPipeline
        ChannelPipeline channelPipeline = ch.pipeline();
        // 添加一堆 NettyServerHandler 到 ChannelPipeline 中
        channelPipeline.addLast(new IdleStateHandler(HEARTBEAT_TIMEOUT_SECONDS, 0, 0))  //空闲检测, 使用读空闲检测实现心跳
                //读超时处理器, 超时会触发io.netty.handler.timeout.ReadTimeoutException异常, 在handler的exceptionCaught方法处理
                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                // 编码器
                .addLast(new InvocationEncoder())
                // 解码器
                .addLast(new InvocationDecoder())
                // 消息分发器
                .addLast(messageDispatcherExecutorGroup, messageDispatcher)
                // 服务端处理器
                .addLast(nettyServerHandler);
    }
}
