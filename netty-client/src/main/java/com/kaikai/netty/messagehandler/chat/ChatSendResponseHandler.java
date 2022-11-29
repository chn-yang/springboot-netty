package com.kaikai.netty.messagehandler.chat;


import com.kaikai.netty.common.dispatcher.MessageHandler;
import com.kaikai.netty.common.message.chat.ChatSendResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ChatSendResponseHandler implements MessageHandler<ChatSendResponse> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void execute(Channel channel, ChatSendResponse message) {
        logger.info("[execute][发送结果：{}]", message);
    }

    @Override
    public String getType() {
        return ChatSendResponse.TYPE;
    }

}
