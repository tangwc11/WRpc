package com.wentry.wrpc.transport.packet;

/**
 * @Description:
 * @Author: tangwc
 */
public interface Packet {

    int REQ_PACKET = 1;
    int RESP_PACKET = 2;

    int type();

}
