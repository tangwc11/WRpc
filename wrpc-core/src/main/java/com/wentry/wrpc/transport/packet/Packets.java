package com.wentry.wrpc.transport.packet;

import java.util.HashMap;
import java.util.Map;

public class Packets {

    static Map<Integer, Class<? extends Packet>> packetCls;

    static{
        packetCls = getAllPacket();
    }
    private static Map<Integer, Class<? extends Packet>> getAllPacket() {
        Map<Integer, Class<? extends Packet>> res = new HashMap<>();
        res.put(new ReqPacket().type(), ReqPacket.class);
        res.put(new RespPacket().type(), RespPacket.class);
        return res;
    }

    public static Class<? extends Packet> getClz(int type) {
        return packetCls.get(type);
    }

}