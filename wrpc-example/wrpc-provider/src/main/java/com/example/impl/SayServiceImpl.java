package com.example.impl;

import com.example.service.SayService;
import com.wentry.wrpc.annotation.WRpcService;

/**
 * @Description:
 * @Author: tangwc
 */
@WRpcService
public class SayServiceImpl implements SayService {
    @Override
    public String sayHello() {
        return "Hello world, i'm WRpc, a minimum viable Rpc implement. ";
    }
}
