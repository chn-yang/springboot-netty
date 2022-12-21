package com.kaikai.netty.common.dispatcher;


import com.alibaba.fastjson.JSON;
import com.kaikai.netty.common.codec.Invocation;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.NettyRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {

    private final int processors = NettyRuntime.availableProcessors();

    //策略模式
    //消息处理器容器, 在Spring初始化时扫描所有MessageHandler类型
    //每种 Message 对应 一种MessageHandler 添加到容器MessageHandlerContainer中
    //收到消息时从消息体Invocation中获取type类型并在上述容器中获得对应的handler
    //反序列化json消息, 并使用对应的handler丢进线程池处理消息
    @Autowired
    private MessageHandlerContainer messageHandlerContainer;

//    private final ExecutorService executor = new ThreadPoolExecutor(processors, processors * 2, 30, TimeUnit.SECONDS,
//            new ArrayBlockingQueue<>(1024), new ThreadPoolExecutor.DiscardOldestPolicy());


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation invocation) {
        // 获得 type 对应的 MessageHandler 处理器
        MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(invocation.getType());
        // 获得  MessageHandler 处理器 的消息类
        Class<? extends Message> messageClass = MessageHandlerContainer.getMessageClass(messageHandler);
        // 解析消息
        Message message = JSON.parseObject(invocation.getMessage(), messageClass);
        // 执行逻辑
//        executor.submit(() -> {
//            // noinspection unchecked
//        });

        //此处不使用ThreadPoolExecutor属性, 在ChannelInitializer添加channelHandler时使用配置好的EventExecutorGroup bean
        messageHandler.execute(ctx.channel(), message);
    }

}
