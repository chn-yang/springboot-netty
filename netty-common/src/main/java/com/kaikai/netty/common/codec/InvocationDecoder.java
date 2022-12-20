package com.kaikai.netty.common.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Slf4j
public class InvocationDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // 标记当前读取位置, 因为还什么都没做标记索引将为0
        in.markReaderIndex();
        // 判断是否能够读取 length 长度
        if (in.readableBytes() <= 4) {
            return;
        }
        // 读取长度
        int length = in.readInt();  //从byteBuf读取一个int数值, 这个操作将会把readerIndex + 4
        if (length < 0) {
            throw new CorruptedFrameException("negative length: " + length);
        }
        // 如果 message 不够可读，则退回到原读取位置
        if (in.readableBytes() < length) {
            in.resetReaderIndex();  //传输层接收缓冲区的可读内容还未全部到达, 重新回到markReaderIndex处, 上面标记了为0 即开头
            return;
        }
        // 此处可读字节数达到了整个内容的大小 读取内容
        byte[] content = new byte[length];
        in.readBytes(content);
        // 解析成 Invocation
        Invocation invocation = JSON.parseObject(content, Invocation.class);

        out.add(invocation);

        logger.info("[decode][连接({}) 解析到一条消息({})]", ctx.channel().id(), invocation.toString());
    }
}
