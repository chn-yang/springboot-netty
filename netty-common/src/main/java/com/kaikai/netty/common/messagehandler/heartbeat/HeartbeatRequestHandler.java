package com.kaikai.netty.common.messagehandler.heartbeat;


import com.kaikai.netty.common.codec.Invocation;
import com.kaikai.netty.common.dispatcher.MessageHandler;
import com.kaikai.netty.common.message.heartbeat.HeartbeatRequest;
import com.kaikai.netty.common.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;


public class HeartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        logger.info("[execute][收到连接({}) 的心跳请求]", channel.id());
        // 响应心跳
        HeartbeatResponse response = new HeartbeatResponse("{heart:beat}");
        channel.writeAndFlush(new Invocation(HeartbeatResponse.TYPE, response));
        /*
        在channel.write 返回的ChannelFuture中添加监听器, 监听器中可以判断future的状况,
        在一些需求中比如发送成功可回写给channel 发送的内容已经接收并处理 用于一些类似回调或需要执行回执的消息处理,
        可在客户端发送的消息中带上ID, 服务端回写消息加上该ID 告诉客户端, 该ID的消息处理完毕
        * .addListener((ChannelFuture f) -> {
            new Invocation(HeartbeatResponse.TYPE, response);
            f.channel().writeAndFlush(new Invocation(HeartbeatResponse.TYPE, new HeartbeatResponse("已收到你发送的心跳")));
        });
        * */
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }

}
