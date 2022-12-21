package com.kaikai.netty.server.config;


import com.kaikai.netty.common.dispatcher.MessageDispatcher;
import com.kaikai.netty.common.dispatcher.MessageHandlerContainer;
import com.kaikai.netty.common.messagehandler.heartbeat.HeartbeatRequestHandler;
import com.kaikai.netty.common.messagehandler.heartbeat.HeartbeatResponseHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class NettyServerConfig {

    @Bean
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }

    @Bean
    public MessageHandlerContainer messageHandlerContainer() {
        return new MessageHandlerContainer();
    }

    @Bean
    public HeartbeatRequestHandler heartbeatRequestHandler() {
        return new HeartbeatRequestHandler();
    }

    @Bean
    public HeartbeatResponseHandler heartbeatResponseHandler() {
        return new HeartbeatResponseHandler();
    }

    @Bean("serverMessageDispatcherExecutorGroup")
    public EventExecutorGroup messageDispatcherExecutorGroup() {
        final int processors = NettyRuntime.availableProcessors();
        ThreadFactory threadFactory = new DefaultThreadFactory("s-md-pool");
        final ExecutorService executor = new ThreadPoolExecutor(processors, processors * 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024), threadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
        return new DefaultEventExecutor(executor);
    }
}
