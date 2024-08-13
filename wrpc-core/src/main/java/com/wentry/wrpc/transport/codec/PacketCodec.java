package com.wentry.wrpc.transport.codec;

import com.alibaba.fastjson2.JSONObject;
import com.wentry.wrpc.transport.packet.Packet;
import com.wentry.wrpc.transport.packet.Packets;
import com.wentry.wrpc.utils.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PacketCodec extends ByteToMessageCodec<Packet> {

    private static final Logger log = LoggerFactory.getLogger(PacketCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        try {
            log.debug("encode msg:{}", JSONObject.toJSONString(msg));
            out.writeInt(msg.type());//消息类型
            byte[] body = SerializationUtils.serialize(msg);
            out.writeInt(body.length);//消息体长度
            out.writeBytes(body);//消息体内容
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.debug("decode msg:{}", in);
        if (in.readableBytes() < 8) {
            return;
        }
        //和编码的顺序相同
        //读类型
        int type = in.readInt();
        Class<? extends Packet> packetType = Packets.getClz(type);
        if (packetType == null) {
            log.info("unknown packetType, type:{}, byteBuf:{}", type, in.toString(CharsetUtil.UTF_8));
            return;
        }
        //读长度
        int length = in.readInt();
        if (in.readableBytes() < length) {
            return;
        }
        //读数据
        byte[] data = new byte[length];
        in.readBytes(data);
        //解码
        Packet packet = SerializationUtils.deserialize(data, packetType);
        out.add(packet);

    }
}