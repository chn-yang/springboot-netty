package com.kaikai.netty.messagehandler.auth;

import com.kaikai.netty.common.dispatcher.MessageHandler;
import com.kaikai.netty.common.message.auth.AuthResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class AuthResponseHandler implements MessageHandler<AuthResponse> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void execute(Channel channel, AuthResponse message) {
        logger.info("[execute][认证结果：{}]", message);
    }

    @Override
    public String getType() {
        return AuthResponse.TYPE;
    }

}
