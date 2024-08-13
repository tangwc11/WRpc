package com.example.config.filters;

import com.alibaba.fastjson2.JSONObject;
import com.wentry.wrpc.common.RpcReq;
import com.wentry.wrpc.common.RpcResp;
import com.wentry.wrpc.filter.InOutBoundFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: tangwc
 */
public class LogInOutBoundFilter implements InOutBoundFilter {

    private static final Logger log = LoggerFactory.getLogger(LogInOutBoundFilter.class);

    @Override
    public void inbound(RpcReq req) {
        log.info("inbound req:{}", JSONObject.toJSONString(req));
    }

    @Override
    public boolean target(RpcReq req) {
        return true;
    }

    @Override
    public void outbound(RpcReq req, RpcResp resp) {
        log.info("outbound req:{}, resp:{}", JSONObject.toJSONString(req), JSONObject.toJSONString(resp));
    }

    @Override
    public boolean target(RpcReq req, RpcResp resp) {
        return true;
    }

}
