package com.kaikai.netty.controller;


import com.kaikai.netty.client.NettyClient;
import com.kaikai.netty.common.codec.Invocation;
import com.kaikai.netty.common.message.auth.AuthRequest;
import com.kaikai.netty.common.message.chat.ChatSendToAllRequest;
import com.kaikai.netty.common.message.chat.ChatSendToOneRequest;
import com.kaikai.netty.common.message.heartbeat.HeartbeatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private NettyClient nettyClient;

    @PostMapping("/mock")
    public String mock(@RequestParam("type") String type, @RequestParam("message") String message) {
        // 创建 Invocation 对象
        Invocation invocation = new Invocation(type, message);
        // 发送消息
        nettyClient.send(invocation);
        return "success";
    }

    @PostMapping("/msg/to/all")
    public String msgToAll(@RequestParam("message") String message) {
        ChatSendToAllRequest request = new ChatSendToAllRequest();
        request.setContent(message);
        request.setMsgId(UUID.randomUUID().toString());
        // 创建 Invocation 对象
        Invocation invocation = new Invocation(ChatSendToAllRequest.TYPE, request);
        // 发送消息
        nettyClient.send(invocation);
        return "success";
    }

    @PostMapping("/msg/to/one")
    public String msgToOne(@RequestParam("user") String user, @RequestParam("message") String message) {
        ChatSendToOneRequest request = new ChatSendToOneRequest();
        request.setContent(message);
        request.setToUser(user);
        request.setMsgId(UUID.randomUUID().toString());
        // 创建 Invocation 对象
        Invocation invocation = new Invocation(ChatSendToOneRequest.TYPE, request);
        // 发送消息
        nettyClient.send(invocation);
        return "success";
    }

    @PostMapping("/auth")
    public String auth(@RequestParam("user") String user) {
        AuthRequest request = new AuthRequest();
        request.setAccessToken(user);
        // 创建 Invocation 对象
        Invocation invocation = new Invocation(AuthRequest.TYPE, request);
        // 发送消息
        nettyClient.send(invocation);
        return "success";
    }

    @PostMapping("/ping")
    public String ping() {
        HeartbeatRequest request = new HeartbeatRequest("{heart:beat}");
        // 创建 Invocation 对象
        Invocation invocation = new Invocation(HeartbeatRequest.TYPE, request);
        // 发送消息
        nettyClient.send(invocation);
        return "success";
    }

}
